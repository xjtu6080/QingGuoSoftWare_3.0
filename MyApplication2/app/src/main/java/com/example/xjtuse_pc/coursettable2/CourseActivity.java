package com.example.xjtuse_pc.coursettable2;




import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CourseActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterobjet;



    //数据库
   // private DBconnection connection;

private  DatabaseDao DBdao;
    private EditText etObjectName;
    private Spinner spinnerobject;
    private Button searchbutton;
    private ImageView imyanzheng;
    private EditText et_yanzheng;
    private ListView listview;
    private RadioGroup  rg;
    private RadioButton form0nebtn;
    private RadioButton formtwobtn;
    private String typevalue="2";
    private static String localhost = "115.159.119.20:80";
    private static String meCookie="";
    private static String errorMessage="";
    private Map<String, String> mapSem;//保存获取过来的学期
    private Map<String, String> mapCour;//保存获取过来的teacher
    private CourseTableDao Courserdao = new CourseTableDao();
    private String str;
    private String SleSem="20161";//保存选择的学期，默认为20161

    private String Slecourse;//保存选择的老师
    private String courseID="";//选择保存的老师ID,默认值第一个人
    private Map<String, List<String>> getCourse = new TreeMap<String, List<String>>();//保存获取过来的课程
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Bitmap bitmap=null;
 private static String Url="";
   // private static String Url="http://218.62.46.130/jwmis/";
//private static String Url="http://qg.peizheng.net.cn/";
    private List<DayCourse> CourseTable=new ArrayList<DayCourse>();
    private ArrayList<ClassInfo1> Info1List = new ArrayList<>();
    private  CourseAdapter adaptercourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
      Url = intent.getStringExtra("url");
        spinner = (Spinner) findViewById(R.id.class_select);//学期的下拉列表
        spinnerobject = (Spinner) findViewById(R.id.class_objectnamelist);//课程关键字的下拉列表
        searchbutton = (Button) findViewById(R.id.class_search);//为查询按钮添加监听事件
        imyanzheng = (ImageView) findViewById(R.id.yanzheng);//验证码
        imyanzheng.setVisibility(View.INVISIBLE);
        etObjectName = (EditText) findViewById(R.id.class_objectname);//关键字输入框
        et_yanzheng = (EditText) findViewById(R.id. et_yanzheng);//获取用户输入的验证码
        et_yanzheng.setVisibility(View.INVISIBLE);
        listview = (ListView) findViewById(R.id.list_Course);//获取listView的id


        adapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_spinner_dropdown_item, getDataSource());//最后最后一个getDataSource()可以设置学期信息
        spinner.setAdapter(adapter);

        adapterobjet = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_spinner_dropdown_item, getObjectSource());//最后一个getDataSource()可以设置根据课程关键字找到的内容
        spinnerobject.setAdapter(adapterobjet);
       // getYZMMethod();//获取验证码图片



        spinner.setOnItemSelectedListener(new OnItemSelectedListenerImpl());//获取设置的是哪一个学期，调用OnItemSelectedListenerImpl()可以获取选择的是哪个学期

        //imyanzheng.setImageResource(R.drawable.test2.jpg);//设置验证码图片
        imyanzheng.setOnClickListener(new OnClickListenerImageViewImpl());//为验证码设置点击更换验证码图片
        searchbutton.setOnClickListener(new OnClickListenerImpl());//为查询按钮添加事件


        //etObjectName.setOnFocusChangeListener(new OnFocusChangeListenerImpl() );//判断课程输入的焦点是否发生变化，发生变化说明已经输入完毕，可以根据输入信息，查找后将结果设置在spinnerobject下拉列表里
        etObjectName.addTextChangedListener(new TextChangedListenerIml());//文档内容改变监听

        //为listView设置适配器new CourseAdapter(this,R.layout.course_main,CourseTable);
        adaptercourse = new CourseAdapter(CourseActivity.this, R.layout.course_main,Info1List);
        listview.setAdapter(adaptercourse);
        spinnerobject.setOnItemSelectedListener(new OnItemSelectedListenerObjectImpl());//获取设置的是哪一个课程，调用OnItemSelectedListenerImpl()可以获取选择的是哪个学期
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        rg=(RadioGroup)findViewById(R.id.rediogruop);
        form0nebtn=(RadioButton)findViewById(R.id.formone);
        formtwobtn=(RadioButton)findViewById(R.id.formtwo);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
          public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

              switch (checkedId) {
                  case R.id.formone:
                      typevalue = "1";
                      break;
                  case R.id.formtwo:
                      typevalue = "2";
                      break;
                  default:
                      break;
              }
        }});}

    //获取验证码图片
    private void getYZMMethod() {
        new Thread() {
            @Override
            public void run() {
                //  Courserdao.GetImage2(Url);
                //final Bitmap bitmap = Courserdao.GetImage(Url);
                bitmap =Courserdao.getBitmap_imgs("http://" + localhost + "/course/getVerImg");
                //   final Bitmap bitmap =  Courserdao.SetBitmap(Url);
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imyanzheng.setImageBitmap(bitmap);//设置验证码图片
                    }
                });
            }
        }.start();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //获取选中教师下拉列表的哪个选项
    private class OnItemSelectedListenerObjectImpl implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String teacher = parent.getItemAtPosition(position).toString();
            Slecourse = teacher;
            courseID = mapCour.get(Slecourse);
            Info1List.clear();
          /* Toast.makeText(CourseActivity.this, "选择的老师是：" + courseID,
                    Toast.LENGTH_LONG).show();*/
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }

    }

    //当选择的学期改变的时候
    public List<String> getObjectSourceChange(String str, String text) {
        final String text1 = text;
        final List<String> list = new ArrayList<String>();
        new Thread() {
            @Override
            public void run() {
               /* Map<String, String> map = Courserdao.GetCourseList(Url, SleSem);*/

                Map<String, String> map = Courserdao.GetCourseList(localhost, SleSem);
                //Log.i("12345678", map.size() + ".....");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().indexOf(text1) >= 0)
                        list.add(entry.getKey());

                }
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterobjet.notifyDataSetChanged();
                        // connection = new DBconnection(this);//数据库连接
                        //DBdao=new DatabaseDao();
                    }
                });
            }
        }.start();

        return list;
    }

    //文档内容改变监听
    public class TextChangedListenerIml implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            String text = etObjectName.getText().toString();
            adapterobjet = new ArrayAdapter<String>(CourseActivity.this,android.R.layout.simple_spinner_dropdown_item, getObjectSourceChange(str, text));//最后一个getDataSource()可以设置根据课程关键字找到的内容
            spinnerobject.setAdapter(adapterobjet);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }


    //为按钮设置事件监听
    public class OnClickListenerImpl implements View.OnClickListener {
        public void onClick(View v) {
            Info1List.clear();
            Log.i("show", SleSem);
            new Thread() {
                @Override

                public void run() {
                    Log.i("yzm", et_yanzheng.getText().toString());

                    //getCourse = Courserdao.SendPost(Url, SleSem, courseID, typevalue, et_yanzheng.getText().toString());

                    //List<ClassInfo1>list=Courserdao.sendJsonToServer(SleSem,courseID,typevalue,et_yanzheng.getText().toString());
                  sendJsonToServer(SleSem,courseID,typevalue,et_yanzheng.getText().toString());


                    CourseTable = s(getCourse);
                    System.out.println("取到地课表是是"+CourseTable);
                            if(typevalue.equals("2")) {
                                CourseActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(Info1List.size()==0){
                                            Toast toast = Toast.makeText(getApplicationContext(), "该课程为空！", Toast.LENGTH_LONG);
                                            //显示toast信息
                                            toast.show();
                                        }else{
                                            adaptercourse.notifyDataSetChanged();
                                        }
                                    }
                                });

                            }else{

                                Intent intent = new Intent(CourseActivity.this, FiveActivity.class);

                                Bundle bundle=new Bundle();
                                SerializeMap tmpmap=new SerializeMap();
                                tmpmap.setMap(getCourse);
                                bundle.putSerializable("CourseMap", tmpmap);
                                intent.putExtras(bundle);
                               startActivity(intent);

                            }
                    }


        }.start();

            CourseTable.clear();
        }
    }
    //将获取的课程数据拆分
    public List<DayCourse> s(Map<String, List<String>> course) {
        for (Map.Entry<String, List<String>> entry : course.entrySet()) {

               // System.out.println("星期" + Integer.parseInt(entry.getKey()) + "  " + Courserdao.showList(entry.getValue()));
                DayCourse day = new DayCourse();
                day.setCourse(entry.getValue());
                CourseTable.add(day);
                Log.i("list", "星期" + entry.getKey() + "  " + entry.getValue());
            }
        return CourseTable;
        }




    //为验证码控件设置点击更换图片
    public class OnClickListenerImageViewImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getYZMMethod();
        }
    }


    //获取选中下拉列表的哪个选项
    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String date = parent.getItemAtPosition(position).toString();
            SleSem = mapSem.get(date);
           /* Toast.makeText(CourseActivity.this, "选择的学期是：" + SleSem,
                    Toast.LENGTH_LONG).show();*/
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }

    }


    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }


    //为下来列表设置内容
    public List<String> getDataSource() {
        final List<String> list = new ArrayList<String>();
        new Thread() {
            @Override
            public void run() {
                mapSem = Courserdao.GetSemesterItem(Url);

                //Log.i("1234567", mapSem.size() + ".....");
                for (Map.Entry<String, String> entry : mapSem.entrySet()) {
                    list.add(entry.getKey());
                    //Log.i(ThridActivity.ACTIVITY_SERVICE,"这是map" +entry.getValue());
                    //  Log.i("1234567", "test" + entry.getKey()+""+entry.getValue());
                }
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();


        return list;


    }


    //为课程下来列表设置内容
    public List<String> getObjectSource() {
        final List<String> list = new ArrayList<String>();
        new Thread() {
            @Override
            public void run() {
               // mapCour = Courserdao.GetCourseList(Url, SleSem);
                mapCour= Courserdao.GetCourseList(localhost, SleSem);
                //Log.i("12345678", map.size() + ".....");
                for (Map.Entry<String, String> entry : mapCour.entrySet()) {
                    list.add(entry.getKey());
                    //Log.i(ThridActivity.ACTIVITY_SERVICE,"这是map" +entry.getValue());
                    // Log.i("1234567", "test" + entry.getKey());
                }
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterobjet.notifyDataSetChanged();

                     //   DBdao.setmapCour(mapCour);
                       // DBdao.insert(mapCour,connection);

                      //  Toast.makeText(TeacherActivity.this, "数据库中查找的老师间是：" + DBdao.findkey(Slecourse),
                              //  Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
        return list;
    }


    public  void  sendJsonToServer(String YearId,String CourseId,String TypeID,String YZM_code) {

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
                stringJsonToList(json);
            } else {
                errorMessage = "返回一个" + conn.getResponseCode() + "状态码";
              CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);

                        toast.show();

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //stringJsonToList里面用到的方法 把字节流以utf-8格式转换为string返回
    public   String readString(InputStream is) throws IOException {
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
    public  void stringJsonToList(String json) {

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
            }else {
                errorMessage = error;

                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast toast = Toast.makeText(getApplicationContext(), "本地数据库没有数据，请输入验证码，准备发送网络请求", Toast.LENGTH_LONG);
                        //显示toast信息
                        toast.show();
                         getYZMMethod();
                        imyanzheng.setVisibility(View.VISIBLE);
                       et_yanzheng.setVisibility(View.VISIBLE);

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}