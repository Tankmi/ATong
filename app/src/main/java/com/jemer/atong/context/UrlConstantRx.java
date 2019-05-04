package com.jemer.atong.context;

/**
 * @author : Zhutao
 * @version 创建时间：@2016年12月19日
 * @Description:
 * @params：
 */
public class UrlConstantRx {

//    public static final String IP = "192.168.0.126:8090"; // 测试服务器地址
    public static final String IP = "182.92.109.146:7779"; // 正式服务器地址

	public static final String API_BASE = "http://" + IP +"/";
	public static final String API_BASEH5 =  "http://" + IP +"/";

//    public static final String API_BASE = "http://192.168.0.126:8090/"; // 测试服务器地址  张琪
//    public static final String API_BASE = "http://192.168.0.28:8090/"; // 测试服务器地址  高玲
//    public static final String API_BASEH5 = "http://192.168.0.142:8090/"; // H5测试服务器地址
    /**
     * 示例： 接口介绍
     * @param 参数名  ：参数介绍 ...
     */
    public static final String API_XXX = API_BASE + "...";

    /**
     * 获取精选内容
     */
    public static final String POST_POST_MAINPAGE = API_BASE + "main/mainPage.do";

    /**
     * 登录
     * @param user ：用户名
     * @param psw ：密码md5(psw)
     * @param imei  ：手机imei号
     */
    public static final String API_LOGIN = "sys/login.do";

    /** 微信登录 */
    public static final String API_WX_LOGIN = "sys/wechatLogin.do";

    /** 微信登录 */
    public static final String API_WX_BIND = "sys/bind.do";

    /** QQ登录 */
    public static final String API_QQ_LOGIN = "sys/qqLogin.do";

    /**
     * 获取验证码
     *
     * @param phone  ：手机号
     * @param imei ：手机imei号
     */
//    public static final String API_VERIFICATION = "sys/validcode.do";
    public static final String API_VERIFICATION = "sys/validcode.do";


    /**
     * 注册成功后补全信息
     *
     * @param birthday" : "生日"
     * @param "height" : "升高"   double类型
     * @param isDiabetes" : "是否糖尿病"  0是  1否
     * @param "isKedneyDisease" : "是否肾功能不全"  0是  1否
     */
    public static final String API_SYSISALL = "user/isall.do";

    /**
     * 修改用户信息
     *
     * @param birthday" : "生日"
     * @param "height" : "升高"   double类型
     * @param isDiabetes" : "是否糖尿病"  0是  1否
     * @param "isKedneyDisease" : "是否肾功能不全"  0是  1否
     */
    public static final String API_UPDATEPERSONAL = "personal/updatePersonal.do";

    /** 上传头像 * @param header：头像二进制字节流 */
    public static final String API_USER_CHANGEHEADER = "user/chgheader.do";

    public static final String API_JOINGROUP = "customer/addGroupMember.do";
    public static final String API_FEEDBACK = "personal/addFeedback.do";

    // ***********************************个人中心

    // ***********************************首页
    public static final String API_INSERTSPORT= "sports/insertSport.do";  //今日运动，添加
    public static final String API_INSERTWEIGHJT = "sports/insertWeight.do";  //录入体重(手动、自动)
    public static final String API_GETNOWEIGHJT = "sports/getNowWeight.do";  //获取体重信息
    public static final String API_GETALLWEIGHJT = "sports/getAllWeight.do";  //获取体重历史记录信息
    public static final String API_GETSPORT = "sports/getSport.do";  //查询当前运动记录统计信息
    public static final String API_GETHISTORYSPORT = "sports/getHistorySport.do";  //查询历史运动记录统计信息
    public static final String API_GETALLSLEEP = "sports/getAllSleep.do";  //获取睡眠记录

    public static final String API_SPORTRANK = "rank/sportsRank.do";  //运动排行（日、周、月）
    public static final String API_LOSEWEIGHTRANK = "rank/loseWeightRank.do";  //减重排行（周、月）
    public static final String API_SPORTSRANKALL = "rank/sportsRankAll.do";  // 点击查看全部运动排行（日、周、月）
    public static final String API_LOSEWEIGHTRANKALL = "rank/loseWeightRankAll.do";  //点击查看全部减重排行（周、月）

    public static final String API_FOODPLAN = "food/getfoodplan.do";  //饮食方案
    public static final String API_SPORTPLAN = "sports/getsportplan.do";  //运动方案
    public static final String API_ENERGYLIST = "food/energyList.do";  //获取&查询食物能量列表

    // **********************************用户个人信息
    /** 获取用户个人信息 */
    public static final String GET_PERSONAL_CENTER= "personal/myCenter.do";

    /** 修改绑定的手机号 */
    public static String API_CHANGE_PHONE = "user/chgphone.do";


    // ***********************************动态
    /** 上传图片 */
    public static final String API_UPLOADINGPICTURE = "H5/uploadImg.do";
    /** 发帖 */
    public static final String API_SENDDYNAMIC = "H5/postLuntan.do";

}
