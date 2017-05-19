package com.example.xjtuse_pc.coursettable2;



/**
 * Course entity. @author MyEclipse Persistence Tools
 */

public class Course  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String courseId;
     private String courseName;


    // Constructors

    /** default constructor */
    public Course() {
    }

    
    /** full constructor */
    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseId() {
        return this.courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
   








}