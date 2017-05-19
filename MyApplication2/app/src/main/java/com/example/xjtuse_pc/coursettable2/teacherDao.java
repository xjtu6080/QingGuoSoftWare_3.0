package com.example.xjtuse_pc.coursettable2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/4/15.
 */

public class teacherDao {


  /*  public static void main(String args[]) {

        // map.g
        String Url = Url;

        // String Url="http://xg.zdsoft.com.cn/";
        //Scanner scan=new Scanner(System.in);
        System.out.println((GetSemesterItem(Url)));
        // String YearId=scan.next();

        GetTeachers(Url, "20161");
        // String TaeacherId=scan.next();

        // System.out.println("输入验证码");
        Url(Url);//获取验证码
        //String YZM_code=scan.next();

        Map<String, List<String>> map = SendPost(Url, "20161", "0000609", "1", "1","" );

        System.out.println(map);
        // System.out.println(HandleMessage(htmlStr));

    }*/
  static String cookie = "";
    static String localhost = "115.159.119.20:80";
    public static Map<String, String> GetSemesterItem(String UrlStr) {
        Map<String,String>SemesterMap=new HashMap<String, String>();
        SemesterMap.put("2016-2017学年第二学期","20161");
        SemesterMap.put("2016-2017学年第一学期","20161");
        SemesterMap.put("2015-2016学年第二学期","20161");
        SemesterMap.put("2015-2016学年第一学期","20161");
        SemesterMap.put("2014-2015学年第二学期","20161");
        SemesterMap.put("2014-2015学年第一学期","20161");
        return SemesterMap;
    }


    public static List<String> show() {
        System.out.println("被调用");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        return list;
    }


    //老师函数

  /*  public static Map<String, String> GetTeachers(String UrlStr, String YearID) {
      Map<String,String>TeacherMap=new HashMap<String, String>();

        StringBuilder br=null;
        String content="";
        URL url=null;
        HttpURLConnection conn=null;
        try {
            url=new URL("http://120.24.228.174/entity/GetTeacherList?UrlStr="+UrlStr+"&YearID="+YearID);
            conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            if(conn.getResponseCode()==200){
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                br=new StringBuilder();
                String line="";
                while((line=reader.readLine())!=null)
                    br.append(line);
            }
            content=br.toString();
            Gson gson=new Gson();

            TeacherMap=gson.fromJson(content,new TypeToken<HashMap<String, String>>(){}.getType());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(conn!=null)
                    conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return TeacherMap;
    }*/
  public  static Map<String,String> GetTeachers(String localhost,String YearID) {
      Map<String,String>CourseMap=new HashMap<String, String>();
      try {
          String url = "http://" + localhost + "/teacher/" + YearID + "/teacherList";

          String jsonString = getJsonString(url);
          System.out.print(jsonString);
          Log.i("aaaa", jsonString);
          JSONArray jsonObject = new JSONArray(jsonString);
          for (int i = 0; i < jsonObject.length(); i++) {
              JSONObject jsonobj = jsonObject.getJSONObject(i);
              String listName = jsonobj.getString("listName");
              String listValue = jsonobj.getString("listValue");
              CourseMap.put(listName, listValue);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }


      return CourseMap;
  }

    //访问url获得String类型的json数据
    public static String getJsonString(String urlPath) throws Exception {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        //对应的字符编码转换
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str = null;
        StringBuffer sb = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str);
        }
        reader.close();
        inputStream.close();
        connection.disconnect();
        return sb.toString();
    }

    //图片函数
    static Bitmap bitmap;





    //访问 图片url 获取验证码
    public Bitmap getBitmap_imgs(String urlPath) {
        Bitmap bitmap_jb = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            bitmap_jb = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap_jb;
    }







   public static Bitmap GetImage(String UrlStr) {
       HttpURLConnection con = null;
       URL url= null;
       Bitmap img=null;
       InputStream isr=null;
       try {
           url = new URL("http://120.24.228.174/entity/GetTeacherImag?Url="+UrlStr);
           con = (HttpURLConnection) url.openConnection();
           if(con.getResponseCode()==200) {
                isr= con.getInputStream();
               img = BitmapFactory.decodeStream(isr);

           }
       } catch (Exception e) {
           e.printStackTrace();
       }finally {

           try {
               if(isr!=null)
                   isr.close();
               if(con!=null)
                   con.disconnect();
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return img;


   }



    //点击检索函数时间后的函数
    public static Map<String, List<String>> SendPost(String UrlStr, String YearId, String TaeacherId, String TypeID, String FormateID, String YZM_code) {

        Map<String,List<String>>CourseMap=new TreeMap<String,List<String>>();
        StringBuilder br=null;
        String content="";
        URL url=null;
        HttpURLConnection conn=null;
        try {
            url=new URL("http://120.24.228.174/entity/SendPost?flag=Teacher&YearId="+YearId+"&UrlStr="+UrlStr+"&TeacherId="+TaeacherId+"&TypeID="+TypeID+"&FormateID="+FormateID+"&YZM_code="+YZM_code);
            conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            if(conn.getResponseCode()==200){
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                br=new StringBuilder();
                String line="";
                while((line=reader.readLine())!=null)
                    br.append(line);
            }
            content=br.toString();
            Gson gson=new Gson();

            CourseMap=gson.fromJson(content,new TypeToken<TreeMap<String,List<String>>>(){}.getType());
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(conn!=null)
                    conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return CourseMap;



    }




    public static String TransFormate(String str) {//对于格式一返回的课表处理

        Document doc = Jsoup.parse(str);
        Elements Tables = doc.select("table");
        String Context = "";
        int count = 0;//记录次数
        StringBuilder builder = new StringBuilder();
        for (Element table : Tables) {
            Elements Trs = table.select("tr");
            for (Element Tr : Trs) {

                Elements Tds = Tr.select("td");

                for (Element Td : Tds) {

                    count++;
                    // System.out.println("这是第"+count+"次");

                    Context = Td.html();

                    //if(Context.equals("")) continue;
                    if (count >=2 ) break;
                    builder.append(Context);
                }

            }
        }


        Document doc2 = Jsoup.parse(builder.toString());
        Elements Tables2 = doc.select("table");
        String Context2 = "";
        int count2 = 0;//记录次数
        StringBuilder build = new StringBuilder();
        for (Element tb : Tables2) {
            Elements trs = tb.select("tr");
            for (Element Tr : trs) {

                Elements Tds = Tr.select("td");

                for (Element Td : Tds) {

                    count2++;
                    // System.out.println("这是第"+count+"次");

                    Context2 = Td.html();

                    //if(Context.equals("")) continue;
                    if (count2 >=2) break;
                    build.append(Context2);

                }

            }

        }//去除无关的表格，取到核心的课程表格

        return build.toString();

    }


    public static String showList(List<ClassInfo1> list) {
          StringBuilder bulider=new StringBuilder();
        for (ClassInfo1 vo : list)
            bulider.append(vo+"\n");
        return bulider.toString();
    }


    //返回课表
    public static Map<String, List<String>> HandleMessageTypeone(String Html_Str) {

        Document TableDoc = Jsoup.parse(Html_Str);
        int index = 0;
        Elements Trs = TableDoc.select("table").select("tr");
        CourseVO course = new CourseVO();

        //List<ArrayList<String>>list=new ArrayList<ArrayList<String>>();;
        String context = "";
        Map<String, List<String>> map = new TreeMap<String, List<String>>();

        for (int i = 0; i < Trs.size(); i++) {
            Element Tr = Trs.get(i);
            Elements Tds = Tr.select("td");

            for (int j = 0; j < Tds.size(); j++) {


                Element Td = Tds.get(j);
                context = Td.text();

                if (i == 0 && j == 0)
                    course.setTitle(context);
                else if (i == 1 && j == 0)
                    course.setTermTitle(context);
                else if (i == 2 && j == 0)
                    course.setTeacherInfo(context);
                else {
                    if (i >= 4 && i <= 7 && context.length() > 3) {//判断每发个星期中那天的课程不为空。j的值代表星期的值


                        SaveInfo(map, (j - 1) + "", i+context);
                        //  map.put((i-1)+"",DayCourse);//同一个key不能对应多个values
                        continue;
                    }

                    //SaveInfo(map,j+"",context);


                }
            }

        }

        return map;

    }

    public static void SaveInfo(Map<String, List<String>> map, String index, String info) {

        List<String> DayCourse = null;
        DayCourse = map.get(index + "");
        if (DayCourse != null) DayCourse.add(info);
        else {
            DayCourse = new ArrayList<String>();
            DayCourse.add(info);

        }
        map.put(index + "", DayCourse);

    }


    public static Map<String, List<String>> HandleMessageTypetwo(String Html_Str) {

        Map<String, List<String>> TableCourse = new HashMap<String, List<String>>();
        List<String> list = null;
        Map<Integer, String> MapTitle = new HashMap<Integer, String>();//课表的表头部分的标题
        System.out.println("格式2");
        Document doc = Jsoup.parse(Html_Str);
        Elements Tables = doc.select("tr");
        String Context = "";
        CourseVO course = new CourseVO();//课程的教师信息等
        int index = 0;//纪律每一个接受了课表的对象存进去
        for (int i = 0; i < Tables.size(); i++) {
            Element Table = Tables.get(0);
            Elements Tds = Table.select("td");
            if (i > 0) break;
            for (int j = 0; j < Tds.size(); j++) {


                System.out.println("i==" + i + ",j=" + j);
                Element Td = Tds.get(j);
                Context = Td.text();
                System.out.println(Context);
                if (i == 0 && j == 0) continue;
                else if (i == 0 && j == 1)
                    course.setTitle(Context);
                else if (i == 0 && j == 2)
                    course.setTermTitle(Context);
                else if (i == 0 && j == 3)
                    course.setTeacherInfo(Context);
                else if (j > 3 && j <= 13) {
                    MapTitle.put(j, Context);
                } else if (j > 3 && j % 10 == 4) {

                    list = new ArrayList<String>();


                } else if (j % 10 == 3 && j > 3) {
                    list.add(Context);
                    TableCourse.put(index + "", list);
                    list = null;
                    index++;
                } else list.add(Context);
            }


        }
        return TableCourse;

    }

}