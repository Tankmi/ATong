package com.jemer.atong.fragment.personal_center.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jemer.atong.R;
import com.jemer.atong.activity.HomeActivity;
import com.jemer.atong.activity.user.perfect_info.PerfectInfoActivity;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.fragment.user.LoginController;
import com.jemer.atong.fragment.user.LoginPresenter;
import com.jemer.atong.fragment.user.login.LoginBindBaseFragment;
import com.jemer.atong.view.EditTextNumberView;
import com.jemer.atong.view.perfect_info.BirthdayRelativelayoutWheelView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;

//调用方式
//    SexDialogFragment playQueueFragment;
//    private FragmentManager fragmentManager;
//    private String MOVEMENT_TIME_TAG = "sharedialog";
//    /**
//     * 显示分享框
//     */
//    private void ShowMovementDialog(String url)
//    {
//        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
//        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
//        playQueueFragment.setShareInfo(url);
//        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
//    }
public class AlterPhoneDialogFragment extends BaseDialogFragment implements LoginController.LoginView {

    @BindView(R.id.btn_ap_close)
    protected Button btn_ap_close;
    @BindView(R.id.et_ap_account)
    protected EditText et_ap_account;
    @BindView(R.id.et_ap_veri) protected EditText et_ap_veri;
    @BindView(R.id.tv_ap_veri) protected TextView tv_ap_veri;
//    @BindView(R.id.btn_ap_dialog_affirm) protected Button btn_ap_dialog_affirm;

    LoginPresenter mPresenter;
    private String phoneNumber;

    public AlterPhoneDialogFragment() {
        super(R.layout.fra_dialog_alter_phone);
    }

    @OnClick ({R.id.tv_ap_veri, R.id.btn_ap_close, R.id.btn_ap_dialog_affirm})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ap_close:   //关闭
                imm.hideSoftInputFromWindow(et_ap_account.getWindowToken(), 0); //强制隐藏键盘
                imm.hideSoftInputFromWindow(et_ap_veri.getWindowToken(), 0); //强制隐藏键盘
                dismiss();
                break;
            case R.id.tv_ap_veri:   //获取验证码
                phoneNumber = et_ap_account.getText().toString();
                mPresenter.getVerifyCode(phoneNumber);
                break;
            case R.id.btn_ap_dialog_affirm:   //修改手机号
                LOG("登录");
                if(canLogin())
                {
                    String verifyCode = et_ap_veri.getText().toString();
                    Map<String,String> mMap = new HashMap<>();
                    mMap.put("phone", phoneNumber);
                    mMap.put("vd", verifyCode);
                    mPresenter.UpdatePhone(mMap);
                }
                else
                    ToastUtils.showToast("请检查输入内容是否正确！");

                break;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AlterPhoneDialog);
    }

           @Override
        protected void initHead() {
            //设置无标题
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            //设置展示位置
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            getDialog().getWindow().setAttributes(params);

            //点击外部不可取消
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
        }

        @Override
        public void onStart() {
            super.onStart();
            //设置dialog尺寸高度 、宽度
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void initLogic() {
        mTimeCount = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        addTextchange();
        lastPhone = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ACCOUNT, "");
        et_ap_account.setText("");

        if(mPresenter == null){
            mPresenter = new LoginPresenter();
        }
        mPresenter.attachView(this);
    }

    String lastPhone;

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 输入框添加监听
     */
    public void addTextchange() {

        et_ap_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0) {    //有内容
                    if (StringUtils.isMobileNO(et_ap_account.getText().toString()))
                        tv_ap_veri.setEnabled(true);
                    else tv_ap_veri.setEnabled(false);
                } else {    //无内容
                    tv_ap_veri.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



    @Override
    protected void initContent() {
    }

    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewRBLinearLayout(btn_ap_close, 40, 40, -1,-1,-1,-1);
    }

    public boolean canLogin() {
        if (et_ap_veri.getText().length() > 0 && StringUtils.isMobileNO(et_ap_account.getText().toString())) {
            return true;
        }
        return false;
    }

    @Override
    protected void pauseClose() {

    }

    @Override
    protected void destroyClose() {
        mPresenter.detachView();
    }

    protected TimeCount mTimeCount;
    @Override
    public void getVerifyCodeState(boolean state) {
        if(!state){
            LOG("验证码获取失败，错误信息：");
            return;
        }
        if(state){
            ToastUtils.showToast("验证码已发送至手机");
            mTimeCount.start();
        }
    }

    @Override
    public void loginState(boolean state,String isall) {
        if(!state){
            LOG("手机号修改失败，错误信息：" + isall);
            return;
        }
        PreferencesUtils.putString(mContext, PreferenceEntity.KEY_USER_ACCOUNT, phoneNumber);
    }

    @Override
    public void loadingShow() {
        setLoading(true);
    }

    @Override
    public void loadingDissmis() {
        setLoading(false);
    }

    @Override
    public void loginOut() {
        loginOut();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    /* 定义一个倒计时的内部类 */
    protected class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            tv_ap_veri.setText("获取验证码");
//			btn_register_veri.setClickable(true);
            tv_ap_veri.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_ap_veri.setEnabled(false);
            tv_ap_veri.setText("" + millisUntilFinished / 1000 + "s");
        }

    }
}
