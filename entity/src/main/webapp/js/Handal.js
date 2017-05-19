function Handal(){
alert("ok");
$(document).ready(function() { 
   $("#TeacherBtn").click(function(){
     $("#Teacher").show();
     $("#Course").hide();
  
    
    });
    $("#CourseBtn").click(function(){
      
     $("#Course").show();
     $("#Teacher").hide();
    });
   
    
    
   $.get("GetTeacherList",{'UrlStr':"http://jwgl.lzmc.edu.cn/"},function(data)
{
   /* alert("数据是"+data); */
     var TeacherSel=$("#selctor2");
    var Teachermap=JSON.parse(data);
   $.each(Teachermap, function(key, value) {
   if(key.length>1&&value.length>1)
   TeacherSel.append("<option value="+value+">"+key+"</option>");//添加option
   

   });
   });
  
  $.get("GetSemester",{'UrlStr':"http://jwgl.lzmc.edu.cn/"},function(semester)
{
    /*  alert("数据是"+semester); */
     var SemesterSel=$("#selector1");
    var SemesterMap=JSON.parse(semester);
   $.each(SemesterMap, function(key, value) {
   if(key.length>1&&value.length>1)
   SemesterSel.append("<option value="+value+">"+key+"</option>");//添加option
   

   });
   });
   
  /*  加载图片 */  
  
  //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp  
    var curWwwPath=window.document.location.href;  
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp  
    var pathName=window.document.location.pathname;  
    var pos=curWwwPath.indexOf(pathName);  
    //获取主机地址，如： http://localhost:8083  
    var localhostPaht=curWwwPath.substring(0,pos);  
    //获取带"/"的项目名，如：/uimcardprj  
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
    var path=localhostPaht+projectName+"/image/Teacher/image.jpg";
     //$("#img").css("background-image","url("+path+")"); 
     
    $.get("GetTeacherImag",{'Url':"http://jwgl.lzmc.edu.cn/"},function(imag)
     {
    $("#img").attr("src",path);
   // $("#img").attr("src",function(){return this.src+path+"?v=1";});
     });
 
}); 

}

