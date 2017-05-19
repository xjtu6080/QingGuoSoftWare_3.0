package com.example.xjtuse_pc.coursettable2;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class SecondActivity extends TabActivity{

     private TextView xuexiaotv;
     private  TextView urltv;
     public void onCreate( Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_second);
        xuexiaotv = (TextView) findViewById(R.id.xuexiao_tv);
        // urltv = (TextView) findViewById(R.id.url_tv);
         Intent intent = getIntent();
         String url = intent.getStringExtra("url");
        // urltv.setText(url);
    System.out.println("1---2的url是"+url);
        String getxuexiao = intent.getStringExtra("usename");
         xuexiaotv.setText(getxuexiao);


         TabHost tabhost = getTabHost();
         TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
         tab1.setIndicator("按课程查询");
         Intent intent_course=new Intent();
         intent_course.setClass(SecondActivity.this,CourseActivity.class);
         intent_course.putExtra("url",url);
         tab1.setContent(intent_course);
         tabhost.addTab(tab1);



         TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
         tab2.setIndicator("按教师查询");
         Intent intent_teacher=new Intent();
         intent_teacher.setClass(SecondActivity.this,TeacherActivity.class);
         intent_teacher.putExtra("url",url);
         tab2.setContent(intent_teacher);
         tabhost.addTab(tab2);




        /* TabHost tabhost = getTabHost();
         TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
         tab1.setIndicator("按教师查询");
         Intent intent_teacher=new Intent();
         intent_teacher.setClass(SecondActivity.this,TeacherActivity.class);
         intent_teacher.putExtra("url",url);
         tab1.setContent(intent_teacher);
         tabhost.addTab(tab1);

         TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
         tab2.setIndicator("按课程查询");
         Intent intent_course=new Intent();
         intent_course.setClass(SecondActivity.this,CourseActivity.class);
         intent_course.putExtra("url",url);
         tab2.setContent(intent_course);
         tabhost.addTab(tab2);*/
     }


    public void changschool(View view) {
        Intent intent= new Intent(this,FirstActivity.class);
        startActivity(intent);
    }
}

