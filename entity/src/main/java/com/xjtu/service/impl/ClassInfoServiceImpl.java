package com.xjtu.service.impl;

import com.xjtu.dao.*;
import com.xjtu.vo.*;
import com.xjtu.exception.NoLocalDataException;
import com.xjtu.exception.VerificationException;
import com.xjtu.service.ClassInfoService;
import com.xjtu.service.HtmlParseJsonService;
import com.xjtu.service.UrlDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llh.xjtu on 17-4-24.
 */
@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    private Logger logger = LoggerFactory.getLogger(ClassInfoService.class);


    @Autowired
    private ClassInfoByCourseDao classInfoByCourseDao;

    @Autowired
    private ClassInfoByOptionalDao classInfoByOptionalDao;

    @Autowired
    private ClassInfoByTeacherDao classInfoByTeacherDao;

    @Autowired
    private ClassInfoByClassDao classInfoByClassDao;

    @Autowired
    private UrlDataService urlDataService;

    @Autowired
    private HtmlParseJsonService htmlParseJsonService;

    @Autowired
    private ListInfoDao listInfoDao;

    @Autowired
    private ListInfo listInfo;


    @Override
    public String code() {
        //TODO
        //删除
        //如果cookie为空则获取cookie
        if (urlDataService.getSessionId() == null) {
            urlDataService.getCookie();
        }
        urlDataService.getImage(1);
        return urlDataService.getSessionId() + ".jpg";
    }

    @Override
    public byte[] getVerImg() {
        //如果cookie为空则获取cookie
        if (urlDataService.getSessionId() == null) {
            urlDataService.getCookie();
        }
        return urlDataService.getImage(1);
    }


    @Override
    public List<ListResult> queryList(String term, int type) {
        List<ListInfo> InfoLists = new ArrayList<>();
        List<ListResult> listResults = new ArrayList<>();
        InfoLists = listInfoDao.queryAllList(term, type);
        if (InfoLists.size() == 0) {
            urlDataService.getCookie();
            // urlData.getImage(1,);
            //本地list为空，访问网络
            //1得验证码 ，2得到list
            String string = null;
            switch (type){
                case 1:
                    string = urlDataService.getXNXQKC(term, "");
                    break;
                case 2:
                    string = urlDataService.getTeacherList(term,"");
                    break;
                case 3:
                    string = urlDataService.getListROOM(term,"");
                    break;
                case 4:
                   //任选课表学校信息列表，必须从数据库获取
                    throw new NoLocalDataException("无本地数据");
                   // string = urlDataService.getListXZBJTerm(term,"");

            }

            listResults = htmlParseJsonService.optiontoList(string);
            //存进数据库,速度较慢，后期使用非关系数据库
            int num = insertList(term, type, listResults);
            logger.info("insert list num:", num);
        } else {
            for (int i = 0; i < InfoLists.size(); i++) {
                ListResult listResult = new ListResult();
                listResult.setListName(InfoLists.get(i).getListContent());
                listResult.setListValue(InfoLists.get(i).getValue());
                listResults.add(listResult);
            }
        }

        return listResults;
    }


    /**
     * 将数据插入数据库，并返回受影响行数
     *
     * @param term      学期
     * @param type      类型
     * @param listInfos
     * @return 返回受影响行数
     */
    @Transactional
    private int insertList(String term, int type, List<ListResult> listInfos) {
        int num = 0;
        for (ListResult result : listInfos) {
            listInfo.setTerm(term);
            listInfo.setListContent(result.getListName());
            listInfo.setValue(result.getListValue());
            listInfo.setType(type);
            num += listInfoDao.insertCourseList(listInfo);
        }
        return num;
    }


    @Override
    public List<ClassInfoByCourse> queryByCourse(String term, String course, String yzm) {
        List<ClassInfoByCourse> lists = new ArrayList<>();
        //查询本地数据库看是否有数据
        lists = classInfoByCourseDao.queryByKeyWithCourse(term, course);
        logger.info("term---course:", term + "---" + course);
        if (lists.size() != 0) {
            //本地数据有数据直接返回数据
            return lists;
        } else {
            //本地数据库无数据查找网络端，并返回
            if(yzm.equals("isNUll")){
                throw new NoLocalDataException("无本地数据，需要输入验证码");
            }
            String type = "1";
            try {

                String string = urlDataService.getKBFBLessonSel(term, course, type, yzm);
                lists = htmlParseJsonService.getClassInfo2(string);
                //往数据库存
                int num = insertCourse(term, course, lists);
                logger.info("insert course num:", num);
            } catch (VerificationException e1) {
                logger.error("VerificationException");
                throw new VerificationException("验证码错误");

            }


        }
        return lists;

    }

    @Override
    public List<ClassInfoByTeacher> queryByTeacher(String term, String teacher, String yzm) {
        List<ClassInfoByTeacher> lists = new ArrayList<>();
        //查询本地数据库看是否有数据
        lists = classInfoByTeacherDao.queryByKeyWithTeacher(term, teacher);
        logger.info("term---course:", term + "---" + teacher);
        if (lists.size() != 0) {
            //本地数据有数据直接返回数据
            return lists;
        } else {
            //本地数据库无数据查找网络端，并返回
            if(yzm.equals("isNUll")){
                throw new NoLocalDataException("无本地数据，需要输入验证码");
            }
            String type = "1";
            try {

                String string = urlDataService.getTeacherKBFB(term, teacher, type, yzm);
                lists = htmlParseJsonService.getClassInfo1(string);
                //往数据库存
                int num = insertTeacher(term, teacher, lists);
                logger.info("insert course num:", num);
            } catch (VerificationException e1) {
                logger.error("VerificationException");
                throw new VerificationException("验证码错误");

            }


        }
        return lists;
    }

    @Override
    public List<ClassInfoByClass> queryByRoom(String term, String room, String yzm) {
        List<ClassInfoByClass> lists = new ArrayList<>();
        //查询本地数据库看是否有数据
        lists = classInfoByClassDao.queryByKeyWithClass(term, room);
        logger.info("term---course:", term + "---" + room);
        if (lists.size() != 0) {
            //本地数据有数据直接返回数据
            return lists;
        } else {
            //本地数据库无数据查找网络端，并返回
            if(yzm.equals("isNUll")){
                throw new NoLocalDataException("无本地数据，需要输入验证码");
            }
            String type = "1";
            try {
                String string = urlDataService.getKBFBRoomSel(term,type,yzm,"1","101", room);
                lists = htmlParseJsonService.getClassInfo3(string);
                //往数据库存
                int num = insertRoom(term, room, lists);
                logger.info("insert Room num:", num);
            } catch (VerificationException e1) {
                logger.error("VerificationException");
                throw new VerificationException("验证码错误");

            }


        }
        return lists;
    }

    @Override
    public List<ClassInfoByOptional> queryByOptional(String term, String school, String yzm) {
        List<ClassInfoByOptional> lists = new ArrayList<>();
        //查询本地数据库看是否有数据
        lists = classInfoByOptionalDao.queryByKeyWithOptional(term, school);
        logger.info("term---course:", term + "---" + school);
        if (lists.size() != 0) {
            //本地数据有数据直接返回数据
            return lists;
        } else {
            //本地数据库无数据查找网络端，并返回
            if(yzm.equals("isNUll")){
                throw new NoLocalDataException("无本地数据，需要输入验证码");
            }
            String type = "1";
            try {
                String string = urlDataService.GetRXKBSel(term,school,yzm);
                lists = htmlParseJsonService.getClassInfo4(string);
                //往数据库存
                int num = insertOptional(term, school, lists);
                logger.info("insert optional num:", num);
            } catch (VerificationException e1) {
                logger.error("VerificationException");
                throw new VerificationException("验证码错误");

            }


        }
        return lists;
    }

    /**
     * 将课程数据插入数据库
     *
     * @param term        学期
     * @param course      课程id
     * @param courseLists 插入课程
     * @return 返回插入数量
     */
    @Transactional
    private int insertCourse(String term, String course, List<ClassInfoByCourse> courseLists) {
        int num = 0;
        for (ClassInfoByCourse infoByCourse : courseLists) {
            infoByCourse.setTerm(term);
            infoByCourse.setCourse(course);
            num += classInfoByCourseDao.insertCourse(infoByCourse);

        }
        return num;
    }


    /**
     * 将教师课程数据插入数据库
     *
     * @param term        学期
     * @param teacher      教师id
     * @param courseLists 插入课程
     * @return 返回插入数量
     */
    @Transactional
    private int insertTeacher(String term, String teacher, List<ClassInfoByTeacher> courseLists) {
        int num = 0;
        for (ClassInfoByTeacher infoByCourse : courseLists) {
            infoByCourse.setTerm(term);
            infoByCourse.setTeacher(teacher);
            num += classInfoByTeacherDao.insertTeacher(infoByCourse);

        }
        return num;
    }

    /**
     * 将教室数据插入数据库
     *
     * @param term        学期
     * @param room      教室id
     * @param roomLists 插入课程
     * @return 返回插入数量
     */
    @Transactional
    private int insertRoom(String term, String room, List<ClassInfoByClass> roomLists) {
        int num = 0;
        for (ClassInfoByClass infoByClass : roomLists) {
            infoByClass.setTerm(term);
            infoByClass.setClassname(room);
            num += classInfoByClassDao.insertClass(infoByClass);

        }
        return num;
    }


    @Transactional
    private int insertOptional(String term, String school, List<ClassInfoByOptional> roomLists) {
        int num = 0;
        for (ClassInfoByOptional infoByClass : roomLists) {
            infoByClass.setTerm(term);
            infoByClass.setSchool(school);
            num += classInfoByOptionalDao.insertCourse(infoByClass);

        }
        return num;
    }

}
