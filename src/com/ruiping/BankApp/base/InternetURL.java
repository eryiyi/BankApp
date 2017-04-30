package com.ruiping.BankApp.base;

/**
 * Created by zhanghl on 2015/1/12.
 */
public class InternetURL {
//    public static final String INTERNAL = "http://223.99.167.142:8088/Manage_ssm/";
    public static final String INTERNAL = "http://192.168.0.188:8080/Manage_ssm/";

    //多媒体文件上传接口
    public static final String UPLOAD_FILE = INTERNAL + "uploadFileController.do?uploadImage";

    //下载路径
    public static final String DOWNLOAD_FILE_URL = "/Android/data/com.ruiping.BankApp/";
    public static final String OPEN_FILE_URL = "/storage/emulated/0/Android/data/com.ruiping.BankApp/";

    //登陆
    public static final String LOGIN_URL = INTERNAL + "appBankEmp.do?empLogin";

    //    日报
    //    1.保存会员日报（周报）信息
    public static final String REPORT_SAVE_URL = INTERNAL + "appReportController.do?save";
    //    2.获得会员日报信息列表
    public static final String REPORT_LISTS_URL = INTERNAL + "appReportController.do?findEmpReport";
    //    3获得下属日报列表
    public static final String REPORT_LISTS_SUBORDINATE_URL = INTERNAL + "appReportController.do?findAllSubordinate";
    //    4获得日报详情
    public static final String REPORT_DETAIL_BY_ID_URL = INTERNAL + "appReportController.do?ById";
    //    5获得日报评论列表
    public static final String REPORT_COMMENT_LIST_URL = INTERNAL + "appReportCommentController.do?findCommentByReportId";
    //    6.获得日报查阅列表
    public static final String REPORT_DONE_LISTS_URL = INTERNAL + "appReportDoneController.do?findByReportId";
    //    7.添加日报评论
    public static final String REPORT_COMMENT_SAVE_URL = INTERNAL + "appReportCommentController.do?save";
    //    8.我的日报统计
    public static final String REPORT_LIST_MINE_URL = INTERNAL + "appReportController.do?statisticsReport";
    //    9.评论我的日报数
    public static final String REPORT_COMMENT_COUNT_URL = INTERNAL + "appReportCommentController.do?getReportCmmentCount";
    //    10.根据日报id查询操作日志信息列表
    public static final String REPORT_ALL_BY_ID_URL = INTERNAL + "findByAllReportId";
    //    11.根据日报id删除日报信息
    public static final String REPORT_DELETE_URL = INTERNAL + "appReportController.do?deleteemp";
    //    12.根据日报id修改日报信息
    public static final String REPORT_UPDATE_BY_ID_URL = INTERNAL + "appReportController.do?appupdate";
    //    13.获得我有评论的日报列表
    public static final String REPORT_COMMENTS_MINE_URL = INTERNAL + "appReportController.do?findReportComments";
    //    获取指定用户指定日期的日报信息
    public static final String REPORT_DATE_BY_EMP_ID_URL = INTERNAL + "appReportController.do?getMyCurrentReport";


    //周报
    //    13.查询我的周报
    public static final String WEEK_DATE_MINE_URL = INTERNAL + "appReportController.do?getMyWeekByDate";
    //我的周报列表
    public static final String WEEK_MINE_URL = INTERNAL + "appReportController.do?findEmpWeekReport";
    //    14.查询下属周报列表
    public static final String WEEK_ALL_SUBORDINATE_URL = INTERNAL + "appReportController.do?findAllSubordinate";
    //    15.根据周报id 查询评论列表
    public static final String WEEK_COMMENT_BY_ID_URL = INTERNAL + "appReportCommentController.do?findCommentByReportId";
    //    16.获得周报查阅列表
    public static final String WEEK_DONE_BY_ID_URL = INTERNAL + "appReportDoneController.do?findByReportId";
    //    17.获得周报详情
    public static final String WEEK_DETAIL_BY_ID_URL = INTERNAL + "appReportController.do?ById";
    //    18.获得我有评论的周报列表
    public static final String WEEK_COMMENT_LIST_MINE_URL = INTERNAL + "appReportController.do?findReportComments";
    //    19.获得我的周报数统计
    public static final String WEEK_COUNT_STATISTIC_URL = INTERNAL + "appReportController.do?statisticType";
    //    20.获得下属周报数
//    public static final String WEEK_COMMENT_BY_ID_URL = INTERNAL + "appReportController.do?statisticType";
    //    21.根据日报id查询操作日志信息列表
    public static final String WEEK_ALL_BY_ID_URL = INTERNAL + "appReportDoneController.do?findByAllReportId";
    //    22.根据周报（季报）id删除日报信息
    public static final String WEEK_DELETE_EMP_URL = INTERNAL + "appReportController.do?deleteemp";
    //    24.根据周报（季报）id修改日报信息
    public static final String WEEK_UPDATE_EMP_URL = INTERNAL + "appReportController.do?appupdate";
    //    24.评论我的周报数
    public static final String WEEK_COMMENT_COUNT_URL = INTERNAL + "appReportCommentController.do?getReportCmmentCount";
    //    25.添加周报评论
    public static final String WEEK_SAVE_COMMENT_URL = INTERNAL + "appReportCommentController.do?save";
    //    26.保存会员周报信息
    public static final String WEEK_SAVE_URL = INTERNAL + "appReportController.do?saveWeekReport";

    //修改报表附件
    public static final String WEEK_UPDATE_FILE_URL = INTERNAL + "appReportController.do?appUpdateFile";

    //备忘录
    //    26.获得我的备忘录列表
    public static final String MOME_NOTE_LIST_URL = INTERNAL + "appMomeController.do?findByselfNote";
    //    27获得我完成的备忘录列表
    public static final String MOME_NOTE_DONE_LIST_URL = INTERNAL + "appMomeController.do?findByfinishedNote";
    //    28.根据备忘录id查找备忘录信息
    public static final String MOME_NOTE_DETAIL_BY_ID_URL = INTERNAL + "appMomeController.do?findByNoteId";
    //    29.保存备忘录信息
    public static final String MOME_NOTE_SAVE_URL = INTERNAL + "appMomeController.do?save";
    //    30.修改备忘录信息
    public static final String MOME_NOTE_UPDATE_BY_ID_URL = INTERNAL + "appMomeController.do?update";
    //    31.根据备忘录id 删除备忘录信息
    public static final String MOME_NOTE_DELETE_BY_ID_URL = INTERNAL + "appMomeController.do?delete";
    //    32.获得我的备忘录数
    public static final String MOME_NOTE_COUNT_URL = INTERNAL + "appMomeController.do?getMyNoteCount";
    //    33.获得我完成的备忘录数
    public static final String MOME_NOTE_FINISHED_COUNT_URL = INTERNAL + "appMomeController.do?getMyFinishedNoteCount";

    //    34.查询主页面统计数据
    public static final String MINE_INDEX_COUNT_URL = INTERNAL + "appReportController.do?getReportCount";
    //    35.我的日报（周报 季报 年报）统计
    public static final String MINE_REPORT_COUNT_URL = INTERNAL + "appReportController.do?getReportCatalog";

    //----老胡接口
    //设置操作
    //1.修改手机号  2.修改邮箱  3.修改性别：
    public static final String UPDATE_INFO_URL = INTERNAL + "appBankEmp.do?updateInfo";
    //4.修改头像
    public static final String UPDATE_INFO_COVER_URL = INTERNAL + "appBankEmp.do?updatePic";
    //5.修改密码
    public static final String UPDATE_INFO_PWD_URL = INTERNAL + "appBankEmp.do?updatePwd";
    //  通讯录
    public static final String GET_FRIENDS_URL = INTERNAL + "appBankEmp.do?findAddressBook";

    // 1.根据empId查询我得任务列表
    public static final String GET_TASK_BY_EMP_ID_URL = INTERNAL + "appBankJobTask.do?findTask";
    // 2. 根据empId查询我创建任务列表
    public static final String GET_TASK_CREATE_BY_EMP_ID_URL = INTERNAL + "appBankJobTask.do?findCreateTask";
    // 3.根据empId查询下属任务列表
    public static final String GET_TASK_BRANCH_BY_EMP_ID_URL = INTERNAL + "appBankJobTask.do?findBranchTask";
    // 4.根据empId查询未完成任务列表
    public static final String GET_TASK_UNFINISH_BY_EMP_ID_URL = INTERNAL + "appBankJobTask.do?findUnfinishTask";
    // 5.根据empId查询全部任务列表
    public static final String GET_TASK_ALL_BY_EMP_ID_URL = INTERNAL + "appBankJobTask.do?findAllTask";
    //     1.根据taskId查询所有任务信息下的评论：
    public static final String TASK_COMMENT_LIST_URL = INTERNAL + "appBankJobTaskComment.do?findAllComment";

    //2.我的任务标题录入：
    public static final String ADD_TASK_TITLE_URL = INTERNAL + "appBankJobTask.do?save";
    //  3.我的任务相关信息展示：. 主任务查询展示
    public static final String GET_DETAIL_TASK_URL = INTERNAL + "appBankJobTask.do?getDetailTask";
    //子任务信息列表
    public static final String GET_CHILDREN_TASK_URL = INTERNAL + "appBankJobTask.do?findByPid";

    //10.负责人和参与人员信息列表
    public static final String GET_TASK_PERSON_ALL_URL = INTERNAL + "appBankJobTask.do?getTaskManager";
    //已选择任务参与人员信息列表
    public static final String GET_TASK_PERSON_URL = INTERNAL + "appBankJobTask.do?getTaskEmp";


    //4我的任务相关信息操作：
    // （4).参与人员录入
    public static final String ADD_TASK_PERSON_URL = INTERNAL + "appBankJobTaskEmp.do?save";
    //标记完成任务
    public static final String TASK_FINISHED_URL = INTERNAL + "appBankJobTask.do?doneJobTask";
    // 删除任务
    public static final String TASK_DELETE_URL = INTERNAL + "appBankJobTask.do?putInTheRecycleBin";
    //更新任务附件 主任务
    public static final String TASK_UPDATE_FILE_URL = INTERNAL + "appBankJobTask.do?updateFile";
    //更新任务负责人 根据taskid 更新empidf
    public static final String TASK_UPDATE_MANAGER_URL = INTERNAL + "appBankJobTask.do?updateManagerEmpId";
//    （6). 删除参与人员
    public static final String TASK_DELETE_EMP_URL = INTERNAL + "appBankJobTaskEmp.do?delete";
    //更新开始日期
    public static final String TASK_UPDATE_START_TIME_URL = INTERNAL + "appBankJobTask.do?updateBeginDate";
    //更新结束日期
    public static final String TASK_UPDATE_END_TIME_URL = INTERNAL + "appBankJobTask.do?updateEndDate";
    //（7). 标题编辑
    public static final String TASK_UPDATE_TITLE_URL = INTERNAL + "appBankJobTask.do?updateTaskTitle";
    //紧急程度编辑
    public static final String TASK_UPDATE_URGENCY_URL = INTERNAL + "appBankJobTask.do?updateUrgency";

    //跟新进度
    public static final String TASK_UPDATE_PRO_URL = INTERNAL + "appBankJobTask.do?updatePrograss";
    //根据主任务查询子任务
    public static final String TASK_GET_CHILD_BY_ID_URL = INTERNAL + "appBankJobTask.do?findByPid";
    //2.对评论回复操作
    public static final String TASK_REPLY_COMMENT_URL = INTERNAL + "appBankJobTaskComment.do?save";

    //推送绑定
    public static final String UPDATE_PUSH_ID_URL = INTERNAL + "appBankEmp.do?updatePushId";
    //根据会员ID查询会员详细信息
    public static final String GET_EMP_DETAIL_BY_ID_URL = INTERNAL + "appBankEmp.do?findEmpById";

    //通过环信username获取用户昵称
    public static final String GET_INVITE_CONTACT_URL = INTERNAL + "appBankEmp.do?findHXEmpById";

    //公告列表
    public static final String GET_NOTICES_URL = INTERNAL + "appBankNotices.do?findAll";
    //根据公告id获取公告信息
    public static final String GET_NOTICE_DETAIL_URL = INTERNAL + "appBankNotices.do?findById";

    //根据会员ID获取任务统计
    public static final String GET_RENWU_COUNT_URL = INTERNAL + "appTask.do?doneTask";

    //根据用户id获取未完成任务列表，公告列表，备忘录列表
    public static final String GET_MAIN_COUNT_URL = INTERNAL + "appBankEmp.do?findListByEmpId";

    //--------------------共享任务------------------------
    //1.查询所有部门
    public static final String GET_ALL_GROUPS_URL = INTERNAL + "appBankJobTask.do?findAllGroups";
    //2.查询同部门和下属用户信息
    public static final String GET_MINE_GROUP_SHARE_URL = INTERNAL + "appBankJobTask.do?getTaskManagers";
    //3.录入共享信息
    public static final String GET_SAVE_GROUP_SHARE_URL = INTERNAL + "appBankJobTask.do?saveShare";
    //4. 删除共享信息
    public static final String GET_DELETE_GROUP_SHARE_URL = INTERNAL + "appBankJobTask.do?deleteShare";
    //5.查询任务共享信息列表
    public static final String GET_PERSONS_URL = INTERNAL + "appBankJobTask.do?getShareEmp";
    //6.查询任务共享总数
    public static final String GET_RENWU_SHARE_COUNT_URL = INTERNAL + "appBankJobTask.do?getShareEmpNum";

    //8.办公会列表
    public static final String GET_MEETING_URL = INTERNAL + "appMeeting.do?find";
    //保存消息对象给服务器
    public static final String SAVE_MSG_URL = INTERNAL + "bankMessage.do?saveMsg";
    //查询关于我们
    public static final String GET_ABOUT_US_URL = INTERNAL + "aboutUs.do?find";
    //检查新版本
    public static final String CHECK_VERSION_CODE_URL = INTERNAL + "appBankVersion.do?byVersion";
    //7. 查询共享任务
    public static final String GET_BANK_TASK_SHARE_URL = INTERNAL + "appBankJobTask.do?findShareTask";

    //查询下属最新动态列表（含任务和报表）：
    public static final String findBankRelation = INTERNAL + "appBankRelation.do?findBankRelation";
    //修改阅读操作
    public static final String updateIsRead = INTERNAL + "appBankRelation.do?updateIsRead";

    //2.根据用户id 年份 月份获取日报信息
    public static final String getReportonMoths = INTERNAL + "appReportController.do?getReportonMoths";//empId  year  month



    //录入起始日期和截止日期
    public static final String appBankJobTaskUpdateTaskDate = INTERNAL + "appBankJobTask.do?updateTaskDate";
    //更新负责人
    public static final String appBankJobTaskUpdateManagerEmpId = INTERNAL + "appBankJobTask.do?updateManagerEmpId";
    //更新参与人
    public static final String appBankJobTaskSaveSub = INTERNAL + "appBankJobTask.do?saveSub";
    //根据taskId查询任务和子任务列表
    public static final String appBankJobTaskFindTaskAndSubTask = INTERNAL + "appBankJobTask.do?findTaskAndSubTask";
    //更新任务详细信息
    public static final String appBankJobTaskUpdateDetailTask = INTERNAL + "appBankJobTask.do?updateDetailTask";
    //查询所有角色
    public static final String appBankJobTaskFindAllRole = INTERNAL + "appBankJobTask.do?findAllRole";
    //分享角色操作
    public static final String appBankJobTaskSaveRoleShare = INTERNAL + "appBankJobTask.do?saveRoleShare";

    //修改任务来源
    public static final String appBankJobTaskupdateTaskSource = INTERNAL + "appBankJobTask.do?updateTaskSource";
    //获取全部任务来源
    public static final String appBankTaskSourcefindall = INTERNAL + "appBankTaskSource.do?findall";
}
