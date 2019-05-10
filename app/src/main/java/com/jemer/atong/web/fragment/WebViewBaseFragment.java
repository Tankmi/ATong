package com.jemer.atong.web.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;


import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.web.MyWebView;
import com.jemer.atong.web.MyWebViewUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import androidx.annotation.Nullable;


public abstract class WebViewBaseFragment extends BaseFragment {

    /** 加载的链接地址  */
    protected String urlLoading;
    protected MyWebViewUtil mWebUtil;
    /**  初次加载显示加载进度条 */
    private boolean firstLoad = false;

    protected RelativeLayout rel_webview_main;
    protected WebView mWebView;
//    protected ImageView iv_webview_hint;
    protected ValueCallback<Uri> mUploadMessage;
    protected ValueCallback<Uri[]> uploadMessage;
    protected static final int FILECHOOSER_RESULTCODE = 1;
    protected static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
    protected static final int REQ_CHOOSE = REQ_CAMERA + 1;
    protected static final int REQ_CHOOSE_5 = REQ_CHOOSE + 1;

    public WebViewBaseFragment(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void initHead() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandledInterface)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //告诉FragmentActivity，当前Fragment在栈顶
        mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    protected void initContent() {
        LOG("setLoadUrlsinitContent    " + urlLoading);
        rel_webview_main = findViewByIds(R.id.rel_webview_main);
        mWebView = findViewByIds(R.id.web_webview);
//        iv_webview_hint = findViewByIds(R.id.iv_webview_hint);
        MyWebView.getInstance().getWebView(mWebView);
        mWebView.setWebChromeClient(new MWebChromClient());
        mWebView.setWebViewClient(mWebViewClient);  //当用户点击一个连接的时候不跳转到手机的浏览器上！
        mWebView.loadUrl(urlLoading);
    }

    /**
     * 加载一个地址
     */
    public void setLoadUrls(String url) {
        this.urlLoading = url;
        LOG("setLoadUrls    " + urlLoading);
        if (mWebView != null) {   //加载webview
            mWebView.loadUrl(urlLoading);
        }
    }

    /**
     * 是否需要返回
     */
    private boolean isGoBack;

    /**
     * 设置是否返回
     */
    public void setIsGoBack(boolean isGoBack) {
        this.isGoBack = isGoBack;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    protected void initLocation() {
//       mLayoutUtil.drawViewRBLayout(rel_login_account, 0, 80, 30, 30, 16, 0);
    }

    @Override
    protected void initLogic() {
    }

    @Override
    protected void destroyClose() {
    }

    @Override
    protected void pauseClose() {
    }

    /**
     * 过滤url
     */
    protected abstract boolean filtrationUrl(String mUrl);

    WebViewClient mWebViewClient = new WebViewClient() {

        /** 返回false 表示继续加载下一个页面 */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                String mUrl = URLDecoder.decode(url, "utf-8");
                LOG("加载的URL,，转译：" + mUrl);
                return filtrationUrl(mUrl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            view.loadUrl(url);
            return false;

        }

        /**
         * @param failingUrl :当前访问的url
         * */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            LOG("加载失败");
            view.loadUrl("file:///android_asset/policy.html");
//            view.loadUrl("file:///android_asset/404.html");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            LOG("开始加载url:" + url);
            if (!firstLoad) {
                firstLoad = true;
//                iv_webview_hint.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.iv_web_loading));
//                iv_webview_hint.setVisibility(View.VISIBLE);
                setLoading(true);
            } else {
//                iv_webview_hint.setVisibility(View.GONE);
                setLoading(false);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LOG("加载url结束:" + url);
//            iv_webview_hint.setVisibility(View.GONE);
            setLoading(false);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            // TODO Auto-generated method stub
            LOG("shouldInterceptRequest:");
            return super.shouldInterceptRequest(view, request);
        }

    };

    private class MWebChromClient extends WebChromeClient {

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            LOG("webview 扩充缓存回调");
            quotaUpdater.updateQuota(requiredStorage * 2);
        }

        // 获得网页的加载进度，显示在右上角的TextView控件中
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress < 100) {
                String progress = newProgress + "%";
                LOG("网页加载进度：" + progress);
            } else {
//                iv_webview_hint.setVisibility(View.GONE);
                setLoading(false);
            }
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            // if (mUploadMessage != null) return;
            LOG("MyWebChromeClient openFileChooser >3.0");
            mUploadMessage = uploadMsg;
            // selectImage();
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            // startActivityForResult( Intent.createChooser( i, "File Chooser"
            // ), REQ_CHOOSE );
            startActivityForResult(i, REQ_CHOOSE);

        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            LOG("MyWebChromeClient openFileChooser <3.0");
            openFileChooser(uploadMsg, "");
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            LOG("MyWebChromeClient openFileChooser >4.1");
            openFileChooser(uploadMsg, acceptType);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            LOG("MyWebChromeClient onShowFileChooser 5.0");
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            // startActivityForResult(Intent.createChooser(i, "文件选择"),
            // REQ_CHOOSE_5);

            try {
                startActivityForResult(Intent.createChooser(i, "文件选择"), REQ_CHOOSE_5);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                LOG("MyWebChromeClient: + Cannot Open File Chooser");
                return false;
            }
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            // TODO Auto-generated method stub
            return super.onJsAlert(view, url, message, result);
        }

    }

    public boolean onBackPressed() {
        LOG("webFragment goback");
        if (isGoBack && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }

    ;

    protected BackHandledInterface mBackHandledInterface;

    public interface BackHandledInterface {
        /**
         * 用于Fragment中监听返回键 ，详细用法，音乐中看我的朋友
         */
        void setSelectedFragment(WebViewBaseFragment selectedFragment);
    }
}
