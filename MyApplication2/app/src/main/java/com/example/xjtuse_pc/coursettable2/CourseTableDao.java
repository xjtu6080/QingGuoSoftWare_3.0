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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
public class CourseTableDao{
	static String localhost = "115.159.119.20:80";
	static String meCookie = "";
	static String errorMessage = "";
	static String cookie="";
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

	public  static Map<String,String> GetCourseList(String localhost,String YearID) {
		Map<String,String>CourseMap=new HashMap<String, String>();
		try {
			String url = "http://" + localhost + "/course/" + YearID + "/courseList";

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


	//向服务器发送请求 以post方式  传递json数据
public static List<ClassInfo1>  sendJsonToServer(String YearId,String CourseId,String TypeID,String YZM_code) {
	List<ClassInfo1> Info1List = new ArrayList<>();
	String url="http://" + localhost + "/course/queryClassByCourse";
		URL myurl = null;
		try {
           JSONObject params = new JSONObject();
			params.put("term", "20161");
			params.put("course", CourseId);
			params.put("yzm", YZM_code);
			Log.i("jsonParam:", params.toString());
			// 把Json数据转换成String类型，使用输出流向服务器写
			final String str = String.valueOf(params);

			myurl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoOutput(true);// 设置允许输出
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Cookie", meCookie);
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // 内容类型
			OutputStream os = conn.getOutputStream();
			os.write(str.getBytes());
			os.close();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				String json = readString(is);
				Info1List=stringJsonToList(json);
			} else {
				errorMessage = "返回一个" + conn.getResponseCode() + "状态码";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	return Info1List;
	}

	//stringJsonToList里面用到的方法 把字节流以utf-8格式转换为string返回
	public  static String readString(InputStream is) throws IOException {
		if (is == null)
			return null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024];
		while ((len = is.read(buf)) != -1) {
			bout.write(buf, 0, len);
		}
		is.close();
		return URLDecoder.decode(new String(bout.toByteArray()), "utf-8");
	}

	//把返回的json解析存放到list里面用以绑定到页面上
	public static List<ClassInfo1> stringJsonToList(String json) {
		List<ClassInfo1> Info1List = new ArrayList<>();
		try {
			System.out.print("得到数据"+json);

			JSONObject jsonObject = new JSONObject(json);

			String success = jsonObject.getString("success");
			String error = jsonObject.getString("error");
			if (success.equals("true")) { //返回了data 也可能是返回了验证码
				if (error.equals("null")) {
					JSONArray data = jsonObject.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObjectSon = (JSONObject) data.opt(i);
						String term = jsonObjectSon.getString("term");
						String course = jsonObjectSon.getString("course");
						String week = jsonObjectSon.getString("week");
						String lesson = jsonObjectSon.getString("lesson");
						String info = jsonObjectSon.getString("info");

						ClassInfo1 cou = new ClassInfo1();
						cou.setWeek(week);
						cou.setLesson(lesson);
						cou.setInfo(info);
						//Log.i("sqlite", cou.getInfo());
						Info1List.add(cou);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Info1List;
	}


public  static  Map<String,List<String>> SendPost(String UrlStr, String YearId,String CourseId,String TypeID,String YZM_code) {

	Map<String, List<String>> CourseMap = new TreeMap<String, List<String>>();
	StringBuilder br = null;
	String content = "";
	URL url = null;
	HttpURLConnection conn = null;
	try {
		url = new URL("http://120.24.228.174/entity/SendPost?flag=Course&YearId=" + YearId + "&UrlStr=" + UrlStr + "&CourseId=" + CourseId + "&TypeID=" + TypeID + "&YZM_code=" + YZM_code);

		conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);

		if (conn.getResponseCode() == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			br = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null)
				br.append(line);
		}
		content = br.toString();
		Gson gson = new Gson();

		CourseMap = gson.fromJson(content, new TypeToken<TreeMap<String, List<String>>>() {
		}.getType());
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			if (conn != null)
				conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	return CourseMap;

}





		
		
		
	public static String TransFormate(String str){//���ڸ�ʽһ���صĿα���
			
		Document doc = Jsoup.parse(str);
		Elements Tables = doc.select("table");
		String Context="";
		int count=0;//��¼����
		StringBuilder builder=new StringBuilder();
	for (Element table:Tables) {
		  Elements Trs=table.select("tr");
		  for (Element Tr:Trs) {
			  
			  Elements Tds=Tr.select("td");
			  
			   for (Element Td:Tds) {
				
				   count++;
				  // System.out.println("���ǵ�"+count+"��"); 
				  
				     Context=Td.html();
				     
				 //if(Context.equals("")) continue;
				 if(count>=2)break;
				 builder.append(Context);
	          }
		
		  }
	}
		
		
		Document doc2 = Jsoup.parse(builder.toString());
			Elements Tables2 = doc.select("table");
			String Context2="";
			int count2=0;//��¼����
			StringBuilder build=new StringBuilder();
		for (Element tb:Tables2) {
			  Elements trs=tb.select("tr");
			  for (Element Tr:trs) {
				  
				  Elements Tds=Tr.select("td");
				  
				   for (Element Td:Tds) {
					
					   count2++;
					  // System.out.println("���ǵ�"+count+"��"); 
					  
					     Context2=Td.html();
					     
					 //if(Context.equals("")) continue;
					 if(count2>=2)break;
					 build.append(Context2);
					
		          }
			
			  }
		
		}//ȥ���޹صı��ȡ�����ĵĿγ̱��

	return build.toString();
		
		}
		
	 
	public  static void showList(List<String>list){
		
		for(String str:list)
			System.out.println(str);	
	}


	public static Map<String,List<String>> HandleMessageTypeone(String Html_Str)	
	{
		 
		 Document TableDoc=Jsoup.parse(Html_Str);
		   int index=0;
	    Elements Trs=TableDoc.select("table").select("tr");
	      CourseVO course=new CourseVO();
	 
	      //List<ArrayList<String>>list=new ArrayList<ArrayList<String>>();;
	      String context="";
	    Map<String,List<String>>map=new HashMap<String, List<String>>();
	  
	  for (int i=0;i<Trs.size();i++) {
			  Element Tr=Trs.get(i);
			  Elements Tds=Tr.select("td");
			 
			   for (int j=0;j<Tds.size();j++) {
				  
				   
				   Element Td=Tds.get(j);
				   context=Td.text();
				  
				   if(i==0&&j==0)
				   course.setTitle(context);
				   else if(i==1&&j==0)
				    course.setTermTitle(context);
				  else  if(i==2&&j==0)
				course.setTeacherInfo(context);
				 else 
					 {
					  if(i>=4&&i<=7&&context.length()>3){//�ж�ÿ��������������Ŀγ̲�Ϊ�ա�j��ֵ�������ڵ�ֵ
						 
					 
						   SaveInfo(map,(j-1)+"",i+context);
						 //  map.put((i-1)+"",DayCourse);//ͬһ��key���ܶ�Ӧ���values
						   continue;
						}
					   
					   //SaveInfo(map,j+"",context);
					   
					
				   } 
			  }
			   
			   }

	return map;

	}
	public static Map<String,List<String>> HandleMessageTypetwo(String Html_Str)	
	{
		
		Map<String,List<String>>TableCourse=new HashMap<String, List<String>>();
		List<String>list=null;
		Map<Integer,String>MapTitle=new HashMap<Integer, String>();//�α�ı�ͷ���ֵı���
		    System.out.println("��ʽ2");
			Document doc = Jsoup.parse(Html_Str);
			Elements Tables = doc.select("tr");
			String Context="";
			CourseVO course=new CourseVO();//�γ̵Ľ�ʦ��Ϣ��
			int index=0;//����ÿһ�������˿α�Ķ�����ȥ
			
		for (int i=0;i<Tables.size();i++) {
			Element Table=Tables.get(0);
			  Elements Tds=Table.select("td");
			  if(i>0)break;
			  for (int j=0;j<Tds.size();j++) {
				  System.out.println("i=="+i+",j="+j);
				  Element Td=Tds.get(j);
				  Context=Td.text();
				  System.out.println(Context);
				   if(i==0&&j==0)continue;
				  else if(i==0&&j==1)
					   course.setTitle(Context);
				  else if(i==0&&j==2)
					    course.setTermTitle(Context);
				  else  if(i==0&&j==3)
					    course.setTeacherInfo(Context);
			      else if(j>3&&j<=12)
				      {
					  MapTitle.put(j,Context);
				      }
				  else if(j%9==4)
				    {
					
				   list=new ArrayList<String>();
				  
				
				    }
				    else if(j>=13&&j%9==3){
				    	list.add(Context);
				    	TableCourse.put(index+"", list);
				    	list=null;
				    	  index++;
				    } 
				    else list.add(Context);
				    }
				
				
			  }
		  return TableCourse;

			   }

	 public static void SaveInfo( Map<String,List<String>>map,String index,String info){
		 
		 List<String>DayCourse=null;
		 DayCourse=map.get(index+"");
		  if(DayCourse!=null)DayCourse.add(info);
		  else 
		 {
			  DayCourse=new ArrayList<String>();
			  DayCourse.add(info);
			 
			  }
		  map.put(index+"", DayCourse);
		 
	 }


	}



