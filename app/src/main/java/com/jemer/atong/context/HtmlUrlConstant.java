package com.jemer.atong.context;

/**
* @author : Zhutao 
* @version 创建时间：@2016年12月19日
* @Description: 
* @params：
*/ 
public class HtmlUrlConstant {

	public static final String HTML_POSTUSERDATA = UrlConstant.API_BASEH5 + "html/getId.jsp?uId=";//缓存用户数据

	public static final String HTML_MAIN_DYNAMIC = UrlConstant.API_BASEH5 + "html/index.html";	//动态 首页
	public static final String HTML_USER_DYNAMICDETAILS = UrlConstant.API_BASEH5 + "html/momentDetail.html?momentId="; //动态详情 /html/momentDetail.html?momentId=111
	public static final String HTML_USER_INFO = UrlConstant.API_BASEH5 + "html/personal.html?personId="; //个人主页 /html/personal.html?personId=111
	public static final String HTML_RELEASEDYNAMIC = UrlConstant.API_BASEH5 + "html/post.html";	//发布动态
	public static final String HTML_SPORTSCHEME = UrlConstant.API_BASEH5 + "html/sportsScheme.html";	//运动方案
	public static final String HTML_ABOUTUS = UrlConstant.API_BASEH5 + "html/AboutUs.html";	//关于我们
	public static final String HTML_QUESTIONS = UrlConstant.API_BASEH5 + "html/questions.html";	//问题反馈


	//********************************截取
	public static final String HTML_CUT_GOBACK = "?back";	//返回键
	public static final String HTML_CUT_RELEASEDYNAMICSUCCESS = "?release";	//动态发布成功
	public static final String HTML_CUT_DYNAMIC_DETAIL = "post?postId=";	//截取 动态详情 href="post?postId="+id
	public static final String HTML_CUT_SHAREWECHAT = "wechat?";	//截取 微信分享 "wechat?"+id
	public static final String HTML_CUT_SHAREWECHATMOMENT = "moment?";	//截取 微信朋友圈分享  "moment?"+id
	public static final String HTML_CUT_SHARE = "share?";	//截取 分享  share?"+id
	public static final String HTML_CUT_USERINFO = "person?id=";	//截取 个人主页 "person?id="+id

	//********************************截取
	public static final String HTM_SPLICE_WECHATSHARE =  UrlConstant.API_BASEH5 + "html/momentDetail.html?uId=";	//微信分享链接 http://192.168.0.142:8090/html/momentDetail.html?uId=49&momentId=275

	/**
	 * 商城
	 * http://192.168.0.142:8090/html/index.jsp
	 * */
	public static final String HTML_MARKET = UrlConstant.API_BASEH5 + "html/index.jsp";

	/**
	 * 首页 运动营养处方完善信息页面：
	 * */
	public static final String HTML_ANALYSIS_ANAINFO = UrlConstant.API_BASEH5 + "html/analysis/anaInfo.jsp";

	/**
	 * 个人主页 发布动态
	 * */
	public static final String HTML_USER_SENDDYNAMIC = UrlConstant.API_BASEH5 + "html/posts/sendPostt.jsp";

//	public static final String HTML_USER_INFO = UrlConstant.API_BASEH5 + "html/posts/personal.jsp?perId=";

	/**
	 * 个人主页 动态
	 * http://192.168.0.142:8090/html/posts/postList.jsp?perId=12
	 *  perId:用户id
	 * */
	public static final String HTML_USER_DYNAMIC = UrlConstant.API_BASEH5 + "html/posts/postList.jsp?perId=";



	/**
	 * 截取 动态列表点击头像进入个人主页
	 * href = "person?id=24"
	 * */
	public static final String HTML_CUTOUT_DYNAMIC_PERSON = "person?id=";

	/**
	 * 截取 个人主页 动态详情
	 * href = "post?postId=11"
	 * postId:动态id
	 * */
	public static final String HTML_CUTOUT_DYNAMIC_DETAILS = "post?postId=";

	/**
	 * 截取 个人主页 发布动态
	 * */
	public static final String HTML_CUTOUT_DYNAMIC_SEND = "send?post";

	/**
	 * 截取 个人主页 发布动态 返回
	 * */
	public static final String HTML_CUTOUT_DYNAMIC_SENDBACK = "?back";

	/**
	 * 截取 个人主页 发布动态  完成
	 * */
	public static final String HTML_CUTOUT_DYNAMIC_SENDOKAY = "done?okay";

	/**
	 * 截取 营养处方，截取到后直接退出页面
	 * */
	public static final String HTML_CUTOUT_ANALYSIS_ANASURVEY = "html/analysis/anaSurvey.jsp";
	/**
	 * 截取 营养处方，截取到后直接退出页面
	 * */
	public static final String HTML_CUTOUT_ANALYSIS_ANAREPORT = "html/analysis/anaReport.jsp";
	/**
	 * 截取 商城 充值
	 * */
	public static final String HTML_CUTOUT_RECHARGE = "go?charge";








	/**
	 * 刷新
	 * */
	public static final String HTML_REFRESH = "?refresh";

	/**
	 * 继续监测
	 * @param href="href="?goOn
	 * */
	public static final String HTML_CUT_GOON = UrlConstant.API_BASEH5 + "html/report.html?goOn";

	/**
	 * 电子病历
	 * @param  cusId:用户id
	 * */
	public static final String HTML_EMR = UrlConstant.API_BASEH5 + "html/medicalReport.html?cusId=";

	/**
	 * 电子病历 分享
	 * @param  http://192.168.0.142:8091/html/goShare?date=2018-06-22
	 * */
//	public static final String HTML_EMR_SHARE = UrlConstant.API_BASEH5 + "html/medicalReport.html?goShare";
	public static final String HTML_EMR_SHARE = UrlConstant.API_BASEH5 + "html/goShare?date=";

	/**
	 * 电子病历 分享的链接
	 * @param  /html/medicalReport.html?cusId=36&date=2018-6-25
	 * */
	public static final String HTML_EMR_SHARE_ADDRESS = UrlConstant.API_BASEH5 + "/html/medicalReport.html?cusId=";

	/**
	 * 耳穴历史记录
	 * @param  /html/oneRecord.html?id=36&date=2018-6-6
	 * */
	public static final String HTML_EAR_RECORD = UrlConstant.API_BASEH5 + "/html/oneRecord.html?id=";



}
