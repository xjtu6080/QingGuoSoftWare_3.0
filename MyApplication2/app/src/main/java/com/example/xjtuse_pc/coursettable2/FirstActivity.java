package com.example.xjtuse_pc.coursettable2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstActivity extends AppCompatActivity{
    protected TextView  xinantv;
    private TextView  fujiantv;
    private TextView  changchuntv;
    private TextView  guangpeitv;
    private TextView  zhongzhoutv;
    private  String str;
    private ImageView image;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        xinantv=(TextView)findViewById(R.id.xinan_tv);
        fujiantv=(TextView)findViewById(R.id.fujian_tv);
        changchuntv=(TextView)findViewById(R.id.changchun_tv);
        guangpeitv=(TextView)findViewById(R.id.guangpei_tv);
        zhongzhoutv=(TextView)findViewById(R.id.zhongzhou_tv);
        image=(ImageView)findViewById(R.id.first_iv);
     //   image.setBackground(getResources().getDrawable(R.drawable.txiao));
    }

    public void xuanzeclick1(View view) {
        /*Intent intent= new Intent(this,SecondActivity.class);
        String str="http://jwgl.lzmc.edu.cn/";
        String xi = xinantv.getText().toString();
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);*/

        Intent intent= new Intent(this,SecondActivity.class);
        String xi = guangpeitv.getText().toString();
        String str="http://jwxt.cqtbi.edu.cn/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);

    }
    public void xuanzeclick2(View view) {
       /* Intent intent= new Intent(this,SecondActivity.class);
        String xi = fujiantv.getText().toString();
        String str="http://jwweb.fjny.edu.cn:8081/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);*/


        Intent intent= new Intent(this,SecondActivity.class);
        String xi = guangpeitv.getText().toString();
        String str="http://jwxt.cqtbi.edu.cn/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);
    }
    public void xuanzeclick3(View view) {
        /*Intent intent= new Intent(this,SecondActivity.class);
        String xi = changchuntv.getText().toString();
        String str="http://218.62.46.130/jwmis/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);*/

        Intent intent= new Intent(this,SecondActivity.class);
        String xi = guangpeitv.getText().toString();
        String str="http://jwxt.cqtbi.edu.cn/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);


    }
    public void xuanzeclick4(View view) {
        Intent intent= new Intent(this,SecondActivity.class);
        String xi = guangpeitv.getText().toString();
        String str="http://jwxt.cqtbi.edu.cn/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);
    }
    public void xuanzeclick5(View view) {
        /*Intent intent= new Intent(this,SecondActivity.class);
        String xi = zhongzhoutv.getText().toString();
        String str="http://211.67.107.38/jwweb/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);*/

        Intent intent= new Intent(this,SecondActivity.class);
        String xi = guangpeitv.getText().toString();
        String str="http://jwxt.cqtbi.edu.cn/";
        intent.putExtra("usename",xi);
        intent.putExtra("url",str);
        startActivity(intent);



    }

    public void finsh(View view) {
        this.finish();
    }


}


