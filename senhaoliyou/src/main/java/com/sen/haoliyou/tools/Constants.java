package com.sen.haoliyou.tools;

/**
 * Created by Sen on 2016/1/29.
 */
public class Constants {
    //腾讯 bugly
    public static final String APPID= "900022459";

    //接口

    // 外网地址
    // public static final String PATH = "http://192.168.0.20/";
//	 public static final String PATH = "http://sec.cncm.com.cn/";
//    public static final String PATH = "http://111.202.9.28:15030/";
    public static final String PATH = "http://139.129.8.158/";
//    public static final String PATH = "http://192.168.50.124/";

//    1.app 更新
public static final String APK_PATH = "http://139.129.8.158/www/resource/phoneDownload/android.html";


    // 1.登陆
    public static final String PATH_LOGIN = "MobileServer/Login";
    // 参数1：username 类型：String 描述：用户名
    // 参数2：password 类型：String 描述：密码
    // 获取版本信息
    public static final String VERSION = "MobileServer/Version";
    // 获得积分
    public static final String USERINFO = "MobileServer/UserInfo";
    // 传递参数:userId
    // 获得考试：
    public static final String GETEXAM = "MobileServer/GetExam";
    // 传递参数:userId
    // 获得试题：
    public static final String PAPERTOPIC = "MobileServer/PaperTopic";
    // 传递参数:examId

    //答题后提交
    public static final String UpdateExamResult = "MobileServer/UpdateExamResult";
    // 传递参数:userId、examId（考试Id） 、mark（答题分数）

    // 1、获得考试：
    // 接口： getExam
    // 传递参数:userid
    // 接受参数：ExamList
    // 2、获得试题：
    // 接口：PaperTopic
    // 传递参数:examId
    // 接受参数：paper（试卷） 、questionList（试题）
    // 3、答题后提交
    // 接口： UpdateExamResult
    // 传递参数:userId、examId（考试Id） 、mark（答题分数）
    // 接受参数：msg
    // 4、获得积分：
    // 接口： UserInfo
    // 传递参数:userId
    // 接受参数：integral
    // 2.自选课（mylearn）
    public static final String PATH_AllOfMyCourses = "MobileServer/AllOfMyCourses";
    // 参数1：userid 类型：String 描述：用户ID

    // 3.选课或退课
    public static final String PATH_CourseSelection = "MobileServer/CourseSelection";
    // 参数1：userid 类型：String 描述：用户ID
    // 参数2：leid 类型：String 描述：课程ID
    // 参数3：flag 类型：String 描述：1.选课操作 2.退课操作

    // 5.资源库
    public static final String PATH_Repository = "MobileServer/Repository";
    // 参数1：search 类型：String 描述：搜索内容
    // 参数2：knoid 类型：String 描述：知识分类ID
    // 参数3：userid 类型：String 描述：用户ID

    // 获取课程评论
    public static final String PATH_GETLESSONCOMMENT = "MobileServer/GetCommentsList";
    //获取课程的章节
    public static final String PATH_GETSECTION = "MobileServer/GetSection";
    //参数   String leid  课程id

    // pageNum 页号
    // 参数2：leid 类型：String 描述：知识分类ID

    // 7.获取课程评论
    public static final String PATH_GetCommentsList = "MobileServer/GetCommentsList";
    // 参数1：leid 类型：String 描述：课程ID

    // 8. 发表课程评论
    public static final String PATH_CourseComments = "MobileServer/CourseComments";
    // 参数1：leid 类型：String 描述：课程ID
    // 参数2：userid 类型：String 描述：用户ID
    // 参数3：content 类型：String 描述：评论内容

    //
    public static final String PATH_PICTURE = PATH+"www/resource/lessonImg/";
    public static final String PATH_PLAYER =  PATH+"www/resource/mp4Course/";
    public static final String PATH_PORTRAIT =  PATH+"www/resource/user/images/";

    // 10. 获得论坛模块
    public static final String PATH_BBSList = "MobileServer/tbztBBSList";
    // 参数2：user_id 类型：String 描述：用户ID

    // 11. 获得论坛下的主题
    public static final String PATH_NoticesList = "MobileServer/zhuanQuLunTanList";
    // 主贴列表或主贴回复列表
    // 参数1：flag 类型：String 描述：1 主贴 2 主贴下的回帖
    // 参数2：bbschildId 类型：String 描述：论坛id
    // 参数2：ask_id 类型：String 描述：主贴id
    // 参数2：pageNum 类型：String 描述：页码
    // 参数2：user_id 类型：String 描述：用户id

    // 12. 发主题或者是回帖
    public static final String PATH_UserPosting = "MobileServer/UserPosting";

    // 参数1：flag 类型：String 描述：1.发主帖 2.回主帖
    // 参数2：userid 类型：String 描述：用户ID
    // 参数3：content 类型：String 描述：发帖或回帖的内容
    // 参数4：ask_id 类型：String 描述：主题ID
    // 参数5：title 类型：String 描述：主题的标题
    // 参数6：bbsid 类型：String 描述：论坛ID

    // 6. 获得试题分类列表
    public static final String PATH_JobCategory = "MobileServer/JobCategory";
    // 参数1：jobid 类型：String 描述：职种ID

    // 4.获取试题
    public static final String PATH_TestQuestions = "MobileServer/TestQuestions";
    // 参数1：jobid 类型：String 描述:职种ID

    // 4.获取试题
    public static final String PATH_ENTEREXAM = "MobileServer/EnterExam";
    public static final String PATH_SUBMITEXAM = "MobileServer/SubmitExam";
}
