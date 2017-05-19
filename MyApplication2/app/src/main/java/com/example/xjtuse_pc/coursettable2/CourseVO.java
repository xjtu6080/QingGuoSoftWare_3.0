package com.example.xjtuse_pc.coursettable2;

/**
 * Created by Administrator on 2017/4/16.
 */

public class CourseVO {
    private String title;
    private String TermTitle;//第几学期的课表
    private String[]CourseList;
    //private Teacher teacher;//任课教师的个人信息
    private String TeacherInfo;


    public String getTeacherInfo() {
        return TeacherInfo;
    }
    public void setTeacherInfo(String teacherInfo) {
        TeacherInfo = teacherInfo;
    }
    public String getTermTitle() {
        return TermTitle;
    }
    public void setTermTitle(String termTitle) {
        TermTitle = termTitle;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String[] getCourseList() {
        return CourseList;
    }
    public void setCourseList(String[] courseList) {
        CourseList = courseList;
    }







}
