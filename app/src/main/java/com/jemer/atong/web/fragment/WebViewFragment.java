package com.jemer.atong.web.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import com.jemer.atong.R;
import com.jemer.atong.context.HtmlUrlConstant;
import com.jemer.atong.web.MyWebViewUtil;

import static huitx.libztframework.utils.LOGUtils.LOG;


/**
 * Fragment WebView
 * @author ZhuTao
 * @date 2017/7/4 
 * @params
*/

public class WebViewFragment extends WebViewBaseFragment implements View.OnClickListener{
//	private String titleName;
	private static final String ARGUMENT_URL = "url";


	public WebViewFragment(){
		super(R.layout.fragment_webview);
//		this.urlLoading = "http://www.jemer.com/";
		TAG = this.getClass().getName();
	}

	@SuppressLint("ValidFragment")
	public WebViewFragment(String urlLoading) {
		super(R.layout.fragment_webview);
		LOG("WebViewFragment    " + urlLoading);
		this.urlLoading = urlLoading;
		TAG = this.getClass().getName();
	}

	@Override
	protected boolean filtrationUrl(String mUrl) {
		Intent intent = null;
		if(mUrl.contains(HtmlUrlConstant.HTML_REFRESH)) {  //刷新
			LOG("刷新");
			mWebView.loadUrl(urlLoading);
			return true;
		}
		return false;
	}


	@Override
	protected void initHead() {
		super.initHead();
//		urlLoading  = "http://192.168.0.142:8090/html/oneRecord.html?id=12&date=2018-2-26";

		mWebUtil = MyWebViewUtil.getInstance();

	}
	
	@Override
	protected void initLogic() {
		super.initLogic();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_title_view_right:
				mWebView.reload();
				break;
		}
	}

}
