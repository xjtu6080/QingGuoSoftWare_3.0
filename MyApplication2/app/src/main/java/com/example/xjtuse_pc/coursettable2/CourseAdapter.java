package com.example.xjtuse_pc.coursettable2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by john on 17/4/13.
 */

public class CourseAdapter extends ArrayAdapter<ClassInfo1>{
    private Context context;
    private int resource;
    private List<ClassInfo1> daycourse;

    public CourseAdapter(Context context, int resource, List<ClassInfo1> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.daycourse=objects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//星期几的课程表
        final ClassInfo1 course=daycourse.get(position);
        //List day=course.getCourse();
      //  String str=new teacherDao().showList(day);
        String str=course.toString();
        convertView= LayoutInflater.from(context).inflate(resource,parent,false);
       //TextView idTv=(TextView)convertView.findViewById(R.id.courseID);

     /*  switch (position) {
            case 0:
                TextView Tv_one=(TextView)convertView.findViewById(R.id.class_onMonday);
                Tv_one.setText(str);
                break;
            case 1:
                TextView Tv_two=(TextView)convertView.findViewById(R.id.class_onTuesday);
                Tv_two.setText(str);
                break;
            case 2:
                TextView Tv_three=(TextView)convertView.findViewById(R.id.class_onWednesday);
                Tv_three.setText(str);
                break;
            case 3:
                TextView Tv_four=(TextView)convertView.findViewById(R.id.class_onThursday);
                Tv_four.setText(str);
                break;
            case 4:
                TextView Tv_five=(TextView)convertView.findViewById(R.id.class_onFriday);
                Tv_five.setText(str);
                break;
            case 5:
                TextView Tv_six=(TextView)convertView.findViewById(R.id.class_onSaturday);
                Tv_six.setText(str);
                break;
            case 6:
                TextView Tv_seven=(TextView)convertView.findViewById(R.id.class_onSunday);
                Tv_seven.setText(str);
                break;
            default:
                System.out.println("default!");
        }*/

    TextView idTv=(TextView)convertView.findViewById(R.id.courseID);
   idTv.setText(str);
      return convertView;
    }
}
