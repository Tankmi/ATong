package com.jemer.atong.web;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jemer.atong.context.ApplicationData;

/**
 * Created by Administrator on 2017/7/24.
 */

public class MyWebView {
    private static MyWebView mMyWebView;

    public static MyWebView getInstance(){
        synchronized(MyWebView.class){
            if(mMyWebView==null){
                mMyWebView=new MyWebView();
            }
        }
        return mMyWebView;
    }

    /** 设置webview的基本参数 */
    public void getWebView( WebView mWebView){
        mWebView.getSettings().setJavaScriptEnabled(true);// 可用JS
        mWebView.getSettings().setSupportZoom(true);// 支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);// 显示放大缩小[/mw_shl_code]
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = ApplicationData.context.getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
//        if(NetUtils.isAPNType(ApplicationData.context)){
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        }else{
//            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);	//显示缓存
//        }
    }
}
