package com.jemer.atong.activity.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;

import org.json.JSONException;
import org.json.JSONObject;

import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NetUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.ToastUtils;


/**
 * 选择登录页
 *
 * @author ZhuTao
 * @date 2018/8/17
 * @params
 */

public class SelLoginActivity extends BaseFragmentActivity implements View.OnClickListener {

    public SelLoginActivity()
    {
        super(R.layout.activity_sel_login);
        TAG = getClass().getName();
    }

    @Override
    protected void initHead()
    {
        PreferenceEntity.isSyncUserDatas = false;
        setStatusBarColor(true, true, mContext.getResources().getColor(R.color.transparency));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(ApplicationData.imei == null || ApplicationData.imei.equals("")){
            getPermission();
            return;
        }
        onResumeNext();
    }

    private void onResumeNext(){
        String code = this.getIntent().getStringExtra("wx_code");
        if (code != null && !code.equals("")) {   //微信登录
            WXLogins(code);
        } else setLoading(false);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        LOG("onNewIntent");
        setIntent(intent);
    }

    private String MAIN_CONTENT_TAG = "main_content";
    private LoginFragment loginFragment;
    private BindPhoneFragment bindPhoneFragment;
    private FragmentManager fragmentManager;

    @Override
    public void onClick(View view)
    {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_wx_login: //微信登录
                WXLogin();
                break;
            case R.id.btn_qq_login: //QQ登录
                QQLogin();
                break;
            case R.id.lin_phone_login: //手机登录
            case R.id.btn_phone_login:
                ShowOrHideLoginView(true);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    protected IWXAPI msgApi;

    private void WXLogin()
    {
        if (msgApi == null)
            msgApi = WXAPIFactory.createWXAPI(mContext, Constantss.APP_ID, false);
        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtils.showToast("您的手机暂不支持微信登录，请下载最新版本的微信！");
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run()
            {
//                setLoading(true);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "huidaifu_demo_test";
                msgApi.sendReq(req);
            }
        });
    }

    protected Tencent mTencent;

    private void QQLogin()
    {
        mTencent = Tencent.createInstance(Constantss.QQ_APP_ID, this.getApplicationContext());
        mTencent.login(this, "all", mQQListener);
    }

    IUiListener mQQListener = new IUiListener() {
        @Override
        public void onComplete(Object o)
        {
            LOG("登录成功");
            JSONObject mJson = (JSONObject) o;
            try {
                String openID = mJson.getString("openid");
                String accessToken = mJson.getString("access_token");
                String expires = mJson.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);

                QQLogins(accessToken, openID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError)
        {
            LOG("登录出错");
        }

        @Override
        public void onCancel()
        {
            LOG("取消登录");
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg)
        {
//			UserEntity user_Entity = (UserEntity) msg.obj;
            switch (msg.what) {

            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Constants.REQUEST_LOGIN) {
            mTencent.handleLoginData(data, mQQListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        setLoading(false);
    }

    private ImageView iv_sel_login_logo;
    private LinearLayout lin_other_login_hint;
    private RelativeLayout rel_other_login;
    private Button btn_wx_login;
    private Button btn_qq_login;
    private LinearLayout lin_phone_login;
    private Button btn_phone_login;

    @Override
    protected void initContent()
    {
        iv_sel_login_logo = findViewByIds(R.id.iv_sel_login_logo);
        lin_other_login_hint = findViewByIds(R.id.lin_other_login_hint);
        rel_other_login = findViewByIds(R.id.rel_other_login);
        btn_wx_login = findViewByIds(R.id.btn_wx_login);
        btn_qq_login = findViewByIds(R.id.btn_qq_login);
        lin_phone_login = findViewByIds(R.id.lin_phone_login);
        btn_phone_login = findViewByIds(R.id.btn_phone_login);

        btn_wx_login.setOnClickListener(this);
        btn_qq_login.setOnClickListener(this);
        lin_phone_login.setOnClickListener(this);
        btn_phone_login.setOnClickListener(this);

//        btn_wx_login.setCompoundDrawables(NewWidgetSetting.getInstance().getWeightDrawable(R.drawable.iv_wx, 36, 36, false), null, null, null);
    }

    @Override
    protected void initLocation()
    {
        mLayoutUtil.drawViewRBLayout(iv_sel_login_logo, 251, 213, 0, 0, 180, 0);
        mLayoutUtil.drawViewRBLayout(lin_other_login_hint, 0, 0, 0, 0, 242, 0);
        mLayoutUtil.drawViewRBLayout(rel_other_login, 350, 0, 0, 0, 36, 0);
        mLayoutUtil.drawViewRBLayout(btn_wx_login, 99, 154, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(btn_qq_login, 99, 152, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(lin_phone_login, 0, 0, 0, 0, 0, 195);
    }

    @Override
    protected void initLogic()
    {
    }


    @Override
    protected void pauseClose()
    {
    }

    @Override
    protected void destroyClose()
    {
    }

    /**
     * 微信登录 2
     */
    public void WXLogins(String code)
    {
        setLoading(true);
        String token = XGPushConfig.getToken(mContext);

        final StringBuilder url = new StringBuilder();
        url.append(UrlConstant.API_WX_LOGIN);
        RequestParams params = new RequestParams();
        params.addBodyParameter("code1", code);
        params.addBodyParameter("imei", ApplicationData.imei);

        params.addBodyParameter("outerToken", "a" + token);
        LOG("微信登录 code:" + code);
        LOG("微信登录 ApplicationData.imei:" + ApplicationData.imei);
        mgetNetData.GetData(this, url.toString(), 2, params);
    }

    /**
     * QQ登录 3
     */
    public void QQLogins(String accessToken, String openId)
    {
        setLoading(true);
        PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_APP_WX_QQ_UNIONID, openId + "");
        String token = XGPushConfig.getToken(mContext);

        RequestParams params = new RequestParams();
        params.addBodyParameter("accessToken", accessToken);
        params.addBodyParameter("openId", openId);
        params.addBodyParameter("oauthConsumerKey", "101518907");
        params.addBodyParameter("imei", ApplicationData.imei);
        params.addBodyParameter("outerToken", "a" + token);

        LOG("QQ accessToken:" + accessToken);
        LOG("QQ openId:" + openId);
        LOG("QQ ApplicationData.imei:" + ApplicationData.imei);
        mgetNetData.GetData(this, UrlConstant.API_QQ_LOGIN, 3, params);
    }

    protected UserEntity mUserEntity;

    @Override
    public void paddingDatas(String mData, int type)
    {
        setLoading(false);
        Gson gson = new Gson();
        try {
            mUserEntity = gson.fromJson(mData, UserEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            if (!NetUtils.isAPNType(mContext)) {
            } else {
                ToastUtils.showToast("微信登录失败，请稍候重试！");
            }
            return;
        }
        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
            if (type == 2 || type == 3) {   //微信登录, QQ登录
                if (type == 2)  PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_APP_WX_QQ_UNIONID, mUserEntity.data.unionId + "");
                PreferenceEntity.setUserEntity(mUserEntity.data);
                if (mUserEntity.data.type.equals("1")) {
                    ShowOrHideBindPhoneView(true, type);
                } else {
                    Intent intent_home;
                    if (mUserEntity.data.isall.equals("0")) {
                        intent_home = new Intent(mContext, PerfectInfoActivity.class);
                    } else {
                        intent_home = new Intent(mContext, HomeActivity.class);
                    }
                    startActivity(intent_home);
                    finish();
                }
            }
        } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
            reLoading();
        } else {
            ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
        }
    }

    @Override
    public void error(String msg, int type)
    {
        setLoading(false);
//            if (type == 2 || type == 3)    //微信登录, QQ登录
//                ToastUtils.showToast("账号绑定失败，请稍候尝试");
    }

    /**
     * 显示或者隐藏登录页
     */
    private void ShowOrHideLoginView(boolean isShow)
    {
        if (loginFragment == null) loginFragment = new LoginFragment();
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTran = fragmentManager.beginTransaction();

        if (isShow) {
            if (loginFragment.isAdded()) fragmentTran.show(loginFragment);
            else fragmentTran.replace(R.id.fram_login_sel, loginFragment, MAIN_CONTENT_TAG);
            fragmentTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTran.addToBackStack(null);
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
    private void ShowOrHideBindPhoneView(boolean isShow, int type)
    {
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            ShowOrHideLoginView(false);
            return true;
        }
        return super.onKeyDown(event.getKeyCode(), event);
    }

    /**
     * 网络请求时等情况时，设置页面的控件是否可以点击
     *
     * @param state
     */
    public void setControlEnable(boolean state)
    {
        if (state) {
            btn_wx_login.setEnabled(true);
            btn_phone_login.setEnabled(true);
        } else {
            btn_wx_login.setEnabled(false);
            btn_phone_login.setEnabled(false);
        }
    }

    private int READ_PHONE_STATEDATA = 100;

    private void getPermission()
    {
        // 判断没有获取权限的话，申请获取权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)){
                // 向用户详细解释申请该权限的原因
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("向服务端进行进口请求需要获取手机设备号信息，用以保证请求安全，如果拒绝此权限，将无法正常进行数据请求！")
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SelLoginActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATEDATA);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("不给", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .show();
            }else{
                ActivityCompat.requestPermissions(SelLoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATEDATA);
            }

        } else {
            ApplicationData.getDatas();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == READ_PHONE_STATEDATA){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授予权限
                onResumeNext();
            } else {
                getPermission();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

//    //获取弹窗权限
//    private void getDialogPermission(){
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            if (Settings.canDrawOverlays(SelLoginActivity.this)) {
////                Intent intent = new Intent(MainActivity.this, MainService.class);
////                startService(intent);
////                finish();
//            } else {
//                //若没有权限，提示获取.
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                Toast.makeText(SelLoginActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//            }
//
//        }
//    }
}
