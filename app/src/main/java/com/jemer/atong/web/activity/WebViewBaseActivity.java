package com.jemer.atong.web.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
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
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.context.HtmlUrlConstant;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.share.WechatShareDialogFragment;
import com.jemer.atong.web.FileUtils;
import com.jemer.atong.web.MyWebView;
import com.jemer.atong.web.MyWebViewUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;

import androidx.fragment.app.FragmentManager;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;

public class WebViewBaseActivity extends BaseFragmentActivity {

    /** 加载的链接地址  */
    protected String urlLoading;
    protected MyWebViewUtil mWebUtil;
    /** 初次加载显示加载进度条 */
    private boolean firstLoad = false;
    protected String titleName;
    protected String userId;


    protected RelativeLayout rel_webview_main;
    protected WebView mWebView;
//    protected ImageView iv_webview_hint;
    protected ValueCallback<Uri> mUploadMessage;
    protected ValueCallback<Uri[]> uploadMessage;
    protected static final int FILECHOOSER_RESULTCODE = 1;
    protected static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
    protected static final int REQ_CHOOSE = REQ_CAMERA + 1;
    protected static final int REQ_CHOOSE_5 = REQ_CHOOSE + 1;

    public WebViewBaseActivity(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void initHead() {

    }

    @Override
    protected void initContent() {
        rel_webview_main = findViewByIds(R.id.rel_webview_main);
        mWebView = findViewByIds(R.id.web_webview);
//        iv_webview_hint = findViewByIds(R.id.iv_webview_hint);
        MyWebView.getInstance().getWebView(mWebView);
        mWebView.setWebChromeClient(new MWebChromClient());
        mWebView.setWebViewClient(mWebViewClient);  //当用户点击一个连接的时候不跳转到手机的浏览器上！
    }

    private boolean isFirstLoad = true;
    /**
     * 是否需要在onResume中进行刷新
     */
    protected boolean isRefresh = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
            mWebView.loadUrl(urlLoading);
        } else if (isRefresh) {
            mWebView.reload();
//            mWebView.loadUrl(urlLoading);
        }
    }

    @Override
    protected void initLocation() {
        mWebView.setMinimumHeight(mLayoutUtil.getWidgetHeight(500));
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
            if(isRefresh) urlLoading = failingUrl;
//            iv_webview_hint.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.iv_web_load_err));
//            iv_webview_hint.setVisibility(View.VISIBLE);
            LOG("加载失败");
            view.loadUrl("file:///android_asset/404.html");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            LOG("开始加载url:" + url);
//            try {
//                filtrationloadUrl(URLDecoder.decode(url,"utf-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            if (!firstLoad) {
                firstLoad = true;
                mWebView.setVisibility(View.GONE);
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
            mUrl = url;
            mWebView.setVisibility(View.VISIBLE);
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

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            LOG("MyWebChromeClient openFileChooser <3.0");
            openFileChooser(uploadMsg, "");
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
            startActivityForResult(Intent.createChooser(i, "File Browser"), REQ_CHOOSE);

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

    private String mUrl;
    /**
     * 过滤url
     */
    private boolean filtrationUrl(String mUrl) {
        Intent intent = null;
        if (mUrl.contains(HtmlUrlConstant.HTML_CUT_GOBACK) || mUrl.contains(HtmlUrlConstant.HTML_CUT_RELEASEDYNAMICSUCCESS)) {  //返回上一级
            if (mUrl.contains(HtmlUrlConstant.HTML_CUT_RELEASEDYNAMICSUCCESS)) {    //动态发布成功时，刷新首页的动态列表
                PreferenceEntity.isRefreshDynamic = true;
                PreferenceEntity.isGoDynamicView = true;
            }
            finish();
            return true;
        }else if(mUrl.contains(HtmlUrlConstant.HTML_CUT_SHARE)) {  //分享
            String pId = MyWebViewUtil.getInstance().getSubString(mUrl, "moment?");
            LOG("微信分享" + pId);
            String userId = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_ID);
            if(NewWidgetSetting.filtrationStringbuffer(userId,"").equals("")) return true;
            ShowMovementDialog(HtmlUrlConstant.HTM_SPLICE_WECHATSHARE + userId + "&momentId=" + pId);
            return true;
        }else if(mUrl.contains(HtmlUrlConstant.HTML_CUT_DYNAMIC_DETAIL)) {  //动态详情
            String pId = MyWebViewUtil.getInstance().getSubString(mUrl, "?postId=");
            intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("url", HtmlUrlConstant.HTML_USER_DYNAMICDETAILS + pId);
            intent.putExtra("is_refresh", false);
            startActivity(intent);
            return true;
        }else if(mUrl.contains(HtmlUrlConstant.HTML_CUT_USERINFO)) {  //个人主页
            String pId = MyWebViewUtil.getInstance().getSubString(mUrl, "person?id=");
            intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("url", HtmlUrlConstant.HTML_USER_INFO + pId);
            intent.putExtra("is_refresh", false);
            startActivity(intent);
            return true;
        }

        return false;
    }

    protected MyHandler mHandler;

    protected class MyHandler extends Handler {

        // SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
        private final WeakReference<Activity> mActivity;

        protected MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivity.get();
            if (activity != null){
                //做操作
                switch (msg.what) {
//                    case GETSPORTNOW:
//                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 判断是否关闭页面
     */
    private boolean isFinish() {
         if ((mUrl != null) && (mUrl.contains(HtmlUrlConstant.HTML_CUTOUT_ANALYSIS_ANASURVEY) || mUrl.contains(HtmlUrlConstant.HTML_CUTOUT_ANALYSIS_ANAREPORT)) ) {
            return true;
        }
        return false;
    }

    /**
     * 拍照后的照片路径
     */
    private String compressPath = "";
    private String imagePaths;

    /**
     * 拍照结束后
     */
    public void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        File newFile = FileUtils.compressFile(f.getPath(), compressPath);
    }

    /**
     * 解决拍照后在相册中找不到的问题
     */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        if (!isFinish() && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
        } else {
//            if ((java.lang.System.currentTimeMillis() - exitTime) > 2500) {
//                ToastUtils.showToast("再按一次后退键，退出页面。");
//                exitTime = java.lang.System.currentTimeMillis();
//            } else {
            finish();
//            }
        }
    }

    WechatShareDialogFragment playQueueFragment;
    private FragmentManager fragmentManager;
    private String MOVEMENT_TIME_TAG = "sharedialog";
    /**
     * 显示分享框
     */
    private void ShowMovementDialog(String url)
    {
        if (playQueueFragment == null) playQueueFragment = new WechatShareDialogFragment();
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        playQueueFragment.setShareInfo(url);
        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
    }

}
