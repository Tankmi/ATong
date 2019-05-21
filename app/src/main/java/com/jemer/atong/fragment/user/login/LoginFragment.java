package com.jemer.atong.fragment.user.login;

import android.Manifest;
import android.view.View;


import com.jemer.atong.R;
import com.jemer.atong.activity.user.SelLoginActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;

import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.permission.IPermissionListenerWrap;
import huitx.libztframework.utils.permission.Permission;
import huitx.libztframework.utils.permission.PermissionsHelper;


/**
 * @author ZhuTao
 * @version V1.0
 * @Title: RegisterActivity.java
 * @Package com.huidaifu.liangzi.activity.user.login
 * @Description: TODO(登录页)
 * @date 2015年12月9日 下午3:57:19
 */
public class LoginFragment extends LoginBindBaseFragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
        TAG = this.getClass().getName();
    }

    @Override
    protected void initHead() {
        super.initHead();
//        setTittle("登录");
    }

    @Override
    protected void initLogic() {
        super.initLogic();
        mUserEntity = new UserEntity();
        mTimeCount = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        et_login_account.setText(NewWidgetSetting.getInstance().filtrationStringbuffer(PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_ACCOUNT),""));
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        LOG("close");
        switch (arg0.getId()) {
            case R.id.btn_login_close:    //关闭页面
//                isCanGoBack();
                getActivity().finish();
                break;
            case R.id.tv_login_veri:    //获取验证码
                LOG("获取验证码");
                if(isPermission())
                    getVerification();
                break;
        }
    }

    /**
     * 当Fragment的显示状态通过FragmentTransition改变时(hide和show),就会回调这个函数,参数hidden将告诉你这个Fragment现在是被隐藏还是显示着.
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    private boolean isPermission(){
        if (ApplicationData.imei == null || ApplicationData.imei.equals("")) {
            requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE });
            return false;
        }
        return true;
    }

    private void requestPermission(final String[] permissions) {
        PermissionsHelper
                .init(getActivity())
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
                        .requestDialogAgain(getActivity(), "温馨提示",
                                "向服务端进行进口请求需要获取手机设备号信息，用以保证请求安全，如果拒绝此权限，将无法正常进行数据请求！"
                                , "好的", "不给");
//                show("勾选不再提醒，拒绝权限 ：" + permission.name);
            }
        }
    }

    @Override
    protected void pauseClose() {
        super.pauseClose();

        imm.hideSoftInputFromWindow(et_login_account.getWindowToken(), 0); //强制隐藏键盘
    }

}
