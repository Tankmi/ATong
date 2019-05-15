package com.jemer.atong.web.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;

import com.jemer.atong.R;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.web.MyWebViewUtil;

import androidx.annotation.RequiresApi;
import huitx.libztframework.utils.PreferencesUtils;


/**
 * 网页浏览器
 * @author ZhuTao
 * @date 2017/7/4 
 * @params 	url 地址  ;
 * user_id:用户id，默认是访问者本身的id（我的收藏，最近播放两个接口用到）
 * title_name: 标题名称
 * is_refresh（boolean值） 是否需要在onResume中进行刷新
 *
 *    intent_home = new Intent(mContext, WebViewActivity.class);
 *  intent_home.putExtra("url", "https://mp.weixin.qq.com/s/ZhS8OmxfWsFlC0CIpMbEBw");
 *             intent_home.putExtra("title_name", "文章");
 *             intent_home.putExtra("is_refresh", false);
*/

public class WebViewActivity extends WebViewBaseActivity implements View.OnClickListener{

	public WebViewActivity() {
		super(R.layout.activity_webview);
		TAG = this.getClass().getName();
	}
	
	@Override
	protected void initHead() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.initHead();
		setStatusBarColor(true,true,mContext.getResources().getColor(R.color.transparency));
		urlLoading = getIntent().getStringExtra("url");
		userId = getIntent().getStringExtra("user_id");
		titleName = getIntent().getStringExtra("title_name");
		isRefresh = getIntent().getBooleanExtra("is_refresh",true);

		if(urlLoading == null || urlLoading.equals("")) finish();
		if(userId == null || userId.equals("")) userId = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_ID);
		LOG("判空后的用户ID：" + userId);
		if(titleName !=null && !titleName.equals("")){
			mTitleView.setVisibility(View.VISIBLE);
			if(titleName.length() > 10 ){
				titleName = titleName.substring(0,10) + "...";
			}
			setTittle("" + titleName);
		}
		mWebUtil = MyWebViewUtil.getInstance();
		mBtnLeft.setOnClickListener(this);

		if (mHandler == null) mHandler = new MyHandler(this);

	}
	
	@Override
	protected void initLogic() {
//		if(urlLoading.contains(HtmlUrlConstant.HTML_USER_DYNAMICDETAILS)){	//显示标题栏
////			setStatusBarColor(false,mContext.getResources().getColor(R.color.transparent_bg));
//			setTittle("动态详情");
//			mTitleView.setVisibility(View.VISIBLE);
//		}
		super.initLogic();
		mWebView.getSettings().setSupportZoom(false);// 支持缩放
		mWebView.getSettings().setBuiltInZoomControls(false);// 显示放大缩小[/mw_shl_code]
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		LOG("选择照片 onActivityResult 回调 ");

		Uri uri = null;
		if(requestCode == REQ_CHOOSE){

			if (null == mUploadMessage){
				LOG("选择照片 onActivityResult 回调 5.0以下机型  mUploadMessage == null   as");
				return;
			}
			if(intent != null){
				uri = intent.getData();
			}else{
				LOG("选择照片 onActivityResult 回调 5.0以下机型  intent == null   as");
			}

			if(uri != null){
				LOG("选择照片 onActivityResult 回调 5.0以下机型 uri:" + uri);
				mUploadMessage.onReceiveValue(uri);
			}else{
				LOG("选择照片 onActivityResult 回调 5.0以下机型 null");
				mUploadMessage.onReceiveValue(null);
			}
			mUploadMessage = null;
		} else if(requestCode == REQ_CHOOSE_5){	//5.0
			LOG("选择照片 5.0 回调 ");
			if(intent != null){
				if (uploadMessage == null)
					return;
				LOG("选择照片 5.0 回调 uploadMessage != null");
				uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));

			}else{
				uploadMessage.onReceiveValue(null);
			}
			uploadMessage = null;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_title_view_left:
				onBackPressed();
				break;
			case R.id.btn_title_view_right:
				mWebView.reload();
				break;
		}
	}

	@Override
	protected void destroyClose()
	{
		super.destroyClose();
		if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
	}
}
