package com.jemer.atong.activity.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.fragment.user.bind_phone.BindPhoneFragment;
import com.jemer.atong.fragment.user.login.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NetUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.utils.permission.IPermissionListenerWrap;
import huitx.libztframework.utils.permission.Permission;
import huitx.libztframework.utils.permission.PermissionsHelper;


/**
 * 选择登录页
 *
 * @author ZhuTao
 * @date 2018/8/17
 * @params
 */

public class SelLoginActivity extends BaseFragmentActivity {

    @BindView(R.id.iv_sel_login_logo)
    protected ImageView iv_sel_login_logo;
    @BindView(R.id.lin_other_login_hint)
    protected LinearLayout lin_other_login_hint;
    @BindView(R.id.rel_other_login)
    protected RelativeLayout rel_other_login;
    @BindView(R.id.btn_wx_login)
    protected Button btn_wx_login;
    @BindView(R.id.btn_qq_login)
    protected Button btn_qq_login;
    @BindView(R.id.lin_phone_login)
    protected LinearLayout lin_phone_login;
    @BindView(R.id.btn_phone_login)
    protected Button btn_phone_login;

    private String MAIN_CONTENT_TAG = "main_content";
    private LoginFragment loginFragment;
    private BindPhoneFragment bindPhoneFragment;
    private FragmentManager fragmentManager;

    public SelLoginActivity() {
        super(R.layout.activity_sel_login);
        TAG = getClass().getName();
    }

    @Override
    protected void initHead() {
        PreferenceEntity.isSyncUserDatas = false;
        setStatusBarColor(true, true, mContext.getResources().getColor(R.color.transparency));
    }

    @Override
    protected void initContent() {

    }

    @Override
    protected void initLogic() {
        ShowOrHideLoginView(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        LOG("onResume");
//        onResumeNext();
    }

//    private void onResumeNext(){
//        String code = this.getIntent().getStringExtra("wx_code");
//        if (code != null && !code.equals("")) {   //微信登录
//            WXLogins(code);
//        } else setLoading(false);
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LOG("onNewIntent");
        setIntent(intent);
    }

    @OnClick({R.id.btn_wx_login, R.id.btn_qq_login, R.id.lin_phone_login, R.id.btn_phone_login})
    void click(View view) {
        Intent intent = null;
        switch (view.getId()) {
//            case R.id.btn_wx_login: //微信登录
//                WXLogin();
//                break;
//            case R.id.btn_qq_login: //QQ登录
//                QQLogin();
//                break;
            case R.id.lin_phone_login: //手机登录
            case R.id.btn_phone_login:
               if(isPermission())
                   ShowOrHideLoginView(true);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private boolean isPermission(){
        if (ApplicationData.imei == null || ApplicationData.imei.equals("")) {
            requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE});
            return false;
        }
        return true;
    }

//    protected IWXAPI msgApi;
//
//    private void WXLogin()
//    {
//        if (msgApi == null)
//            msgApi = WXAPIFactory.createWXAPI(mContext, Constantss.APP_ID, false);
//        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        if (!isPaySupported) {
//            ToastUtils.showToast("您的手机暂不支持微信登录，请下载最新版本的微信！");
//            return;
//        }
//        mHandler.post(new Runnable() {
//            @Override
//            public void run()
//            {
////                setLoading(true);
//                SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "huidaifu_demo_test";
//                msgApi.sendReq(req);
//            }
//        });
//    }
//
//    protected Tencent mTencent;
//
//    private void QQLogin()
//    {
//        mTencent = Tencent.createInstance(Constantss.QQ_APP_ID, this.getApplicationContext());
//        mTencent.login(this, "all", mQQListener);
//    }
//
//    IUiListener mQQListener = new IUiListener() {
//        @Override
//        public void onComplete(Object o)
//        {
//            LOG("登录成功");
//            JSONObject mJson = (JSONObject) o;
//            try {
//                String openID = mJson.getString("openid");
//                String accessToken = mJson.getString("access_token");
//                String expires = mJson.getString("expires_in");
//                mTencent.setOpenId(openID);
//                mTencent.setAccessToken(accessToken, expires);
//
//                QQLogins(accessToken, openID);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onError(UiError uiError)
//        {
//            LOG("登录出错");
//        }
//
//        @Override
//        public void onCancel()
//        {
//            LOG("取消登录");
//        }
//    };
//
//    Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg)
//        {
////			UserEntity user_Entity = (UserEntity) msg.obj;
//            switch (msg.what) {
//
//            }
//        }
//
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent list)
//    {
//        if (requestCode == Constants.REQUEST_LOGIN) {
//            mTencent.handleLoginData(list, mQQListener);
//        }
//        super.onActivityResult(requestCode, resultCode, list);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        setLoading(false);
    }

    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewRBLayout(iv_sel_login_logo, 251, 213, 0, 0, 180, 0);
        mLayoutUtil.drawViewRBLayout(lin_other_login_hint, 0, 0, 0, 0, 242, 0);
        mLayoutUtil.drawViewRBLayout(rel_other_login, 350, 0, 0, 0, 36, 0);
        mLayoutUtil.drawViewRBLayout(btn_wx_login, 99, 154, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(btn_qq_login, 99, 152, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(lin_phone_login, 0, 0, 0, 0, 0, 195);
    }




    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {
    }

//    /**
//     * 微信登录 2
//     */
//    public void WXLogins(String code)
//    {
//        setLoading(true);
//        String token = XGPushConfig.getToken(mContext);
//
//        final StringBuilder url = new StringBuilder();
//        url.append(UrlConstant.API_WX_LOGIN);
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("code1", code);
//        params.addBodyParameter("imei", ApplicationData.imei);
//
//        params.addBodyParameter("outerToken", "a" + token);
//        LOG("微信登录 code:" + code);
//        LOG("微信登录 ApplicationData.imei:" + ApplicationData.imei);
//        mgetNetData.GetData(this, url.toString(), 2, params);
//    }
//
//    /**
//     * QQ登录 3
//     */
//    public void QQLogins(String accessToken, String openId)
//    {
//        setLoading(true);
//        PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_APP_WX_QQ_UNIONID, openId + "");
//        String token = XGPushConfig.getToken(mContext);
//
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("accessToken", accessToken);
//        params.addBodyParameter("openId", openId);
//        params.addBodyParameter("oauthConsumerKey", "101518907");
//        params.addBodyParameter("imei", ApplicationData.imei);
//        params.addBodyParameter("outerToken", "a" + token);
//
//        LOG("QQ accessToken:" + accessToken);
//        LOG("QQ openId:" + openId);
//        LOG("QQ ApplicationData.imei:" + ApplicationData.imei);
//        mgetNetData.GetData(this, UrlConstant.API_QQ_LOGIN, 3, params);
//    }
//
//    protected UserEntity mUserEntity;
//
//    @Override
//    public void paddingDatas(String mData, int type)
//    {
//        setLoading(false);
//        Gson gson = new Gson();
//        try {
//            mUserEntity = gson.fromJson(mData, UserEntity.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (!NetUtils.isAPNType(mContext)) {
//            } else {
//                ToastUtils.showToast("微信登录失败，请稍候重试！");
//            }
//            return;
//        }
//        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
//            if (type == 2 || type == 3) {   //微信登录, QQ登录
//                if (type == 2)  PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_APP_WX_QQ_UNIONID, mUserEntity.list.unionId + "");
//                PreferenceEntity.setUserEntity(mUserEntity.list);
//                if (mUserEntity.list.type.equals("1")) {
//                    ShowOrHideBindPhoneView(true, type);
//                } else {
//                    Intent intent_home;
//                    if (mUserEntity.list.isall.equals("0")) {
//                        intent_home = new Intent(mContext, PerfectInfoActivity.class);
//                    } else {
//                        intent_home = new Intent(mContext, HomeActivity.class);
//                    }
//                    startActivity(intent_home);
//                    finish();
//                }
//            }
//        } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
//            reLoading();
//        } else {
//            ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
//        }
//    }
//
//    @Override
//    public void error(String msg, int type)
//    {
//        setLoading(false);
////            if (type == 2 || type == 3)    //微信登录, QQ登录
////                ToastUtils.showToast("账号绑定失败，请稍候尝试");
//    }


    FragmentTransaction fragmentTran;
    /**
     * 显示或者隐藏登录页
     */
    private void ShowOrHideLoginView(boolean isShow) {
        if (loginFragment == null){
            loginFragment = new LoginFragment();
        }
        if (fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentTran = fragmentManager.beginTransaction();

        if (isShow) {
            if (loginFragment.isAdded()) fragmentTran.show(loginFragment);
            else fragmentTran.replace(R.id.fram_login_sel, loginFragment, MAIN_CONTENT_TAG);
            fragmentTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            fragmentTran.addToBackStack(null);
        } else if (loginFragment.isVisible()) {
            if (loginFragment.fragementIsGoBack()) {
                fragmentTran.hide(loginFragment);
            } else {
                return;
            }
        } else {
            ShowOrHideBindPhoneView(false, 2); //判断完后继续判断绑定手机号页面是否显示，没有显示的话，直接finish
        }
        fragmentTran.commitAllowingStateLoss();
    }

    /**
     * 显示或者隐藏绑定手机号页
     *
     * @param isShow
     * @param type   isShow为true时传，绑定手机号 微信2 QQ3
     */
    private void ShowOrHideBindPhoneView(boolean isShow, int type) {
        if (bindPhoneFragment == null) bindPhoneFragment = new BindPhoneFragment();
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTran = fragmentManager.beginTransaction();
        if (isShow) {
            bindPhoneFragment.setType(type);
            if (bindPhoneFragment.isAdded()) fragmentTran.show(bindPhoneFragment);
            else fragmentTran.replace(R.id.fram_login_sel, bindPhoneFragment, MAIN_CONTENT_TAG);
            fragmentTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTran.addToBackStack(null);
        } else if (bindPhoneFragment.isVisible()) {

            fragmentTran.hide(bindPhoneFragment);

        } else finish();  //没有显示的话，直接finish
        fragmentTran.commitAllowingStateLoss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            ShowOrHideLoginView(false);
//            return true;
//        }
        return super.onKeyDown(event.getKeyCode(), event);
    }

    /**
     * 网络请求时等情况时，设置页面的控件是否可以点击
     *
     * @param state
     */
    public void setControlEnable(boolean state) {
        if (state) {
            btn_wx_login.setEnabled(true);
            btn_phone_login.setEnabled(true);
        } else {
            btn_wx_login.setEnabled(false);
            btn_phone_login.setEnabled(false);
        }
    }

    private void requestPermission(final String[] permissions) {
        PermissionsHelper
                .init(SelLoginActivity.this)
                .requestEachPermissions(permissions, new IPermissionListenerWrap.IEachPermissionListener() {
                    @Override
                    public void onAccepted(Permission permission) {
                        show(permission);
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    private void show(Permission permission) {
        if (permission.granted) {
//            show("授予权限 ：" + permission.name);
            ApplicationData.getDatas();
        } else {
            if (permission.shouldShowRequestPermissionRationale) {
//                show("没有勾选不再提醒，拒绝权限 ：" + permission.name);
            } else {
                PermissionsHelper
                        .requestDialogAgain(SelLoginActivity.this, "温馨提示",
                                "向服务端进行进口请求需要获取手机设备号信息，用以保证请求安全，如果拒绝此权限，将无法正常进行数据请求！"
                                , "好的", "不给");
//                show("勾选不再提醒，拒绝权限 ：" + permission.name);
            }
        }
    }

    void show(CharSequence text) {
        Toast.makeText(SelLoginActivity.this, text, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "show: text =" + text);

    }
}
