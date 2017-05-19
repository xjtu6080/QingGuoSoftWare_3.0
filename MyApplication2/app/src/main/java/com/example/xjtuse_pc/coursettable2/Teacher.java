package com.example.xjtuse_pc.coursettable2;



/**
 * Teacher entity. @author MyEclipse Persistence Tools
 */

public class Teacher  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String teachId;
     private String teachName;


    // Constructors

    /** default constructor */
    public Teacher() {
    }

    
    /** full constructor */
    public Teacher(String teachId, String teachName) {
        this.teachId = teachId;
        this.teachName = teachName;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeachId() {
        return this.teachId;
    }
    
    public void setTeachId(String teachId) {
        this.teachId = teachId;
    }

    public String getTeachName() {
        return this.teachName;
    }
    
    public void setTeachName(String teachName) {
        this.teachName = teachName;
    }
   








}