package com.jemer.atong.fragment.personal_center;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.activity.HomeActivity;
import com.jemer.atong.activity.HomeBaseActivity;
import com.jemer.atong.activity.user.perfect_info.PerfectInfoActivity;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.personal_center.dialog.BirthdayDialogFragment;
import com.jemer.atong.fragment.personal_center.dialog.SexDialogFragment;
import com.jemer.atong.net.DefaultObserver;
import com.jemer.atong.net.RetrofitHelper;
import com.jemer.atong.net.service.HomeService;
import com.jemer.atong.share.WechatShareDialogFragment;
import com.jemer.atong.util.VersionTools;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.BitmapUtils;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.view.dialog.DialogUIUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@SuppressLint("ValidFragment")
public class PersonalCenterBaseFragment extends BaseFragment implements
        OnClickListener {


    private String birthday;
    private String user_header;
    //1男2女
    private String user_sex;

    public PersonalCenterBaseFragment(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void initContent() {
        tv_sett_phone.setText("手机号");
        tv_sett_bir.setText("生日");
        tv_sett_sex.setText("性别");
    }


    @Override
    public void error(String msg, int type) {
        super.error(msg, type);

    }


    @Override
    protected void initHead() {
    }

    @Override
    protected void onVisibile() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    public void setData(UserEntity.Data userInfo) {
        if (userInfo == null) userInfo = UserEntity.getUserInfo();
        NewWidgetSetting.setViewText(tv_setti_name, userInfo.name, "未知");
        birthday = userInfo.birthday == null ? "未设置" : tranTimes.convert(userInfo.birthday, "yyyy.MM.dd");

        NewWidgetSetting.setViewText(tv_sett_phone_value, userInfo.phone, "未知");

        setBirthday(birthday);
        setSexInfo(userInfo.sex, userInfo.head);
    }

    private void setBirthday(String birthday){
        NewWidgetSetting.setViewText(tv_setti_phone, birthday, "");
        NewWidgetSetting.setViewText(tv_sett_bir_value, birthday, "");
    }

    /**
     * 设置用户头像
     */
    private void setSexHeader(String sex, String header) {
        user_header = header;
        RequestOptions options;
        if (sex != null && sex.equals("1")) {    //男
            options = new RequestOptions()
                    .placeholder(R.drawable.iv_man_bef)
                    .circleCrop();
        } else {
            options = new RequestOptions()
                    .placeholder(R.drawable.iv_woman_bef)
                    .circleCrop();
        }

        Glide.with(mContext).load(user_header).apply(options).into(iv_sett_header);
    }

    private void setSexInfo(String sex, String header) {
        if (sex == null || sex.equals("")) return;
        user_sex = sex;
        iv_sett_header_sex.setImageResource((sex != null && sex.equals("1")) ? R.drawable.icon_sex_man : R.drawable.icon_sex_woman);
        tv_sett_sex_value.setText((sex != null && sex.equals("1")) ? "男" : "女");
//        if(header != null && !header.equals(""))
            setSexHeader(sex,header);

    }

    SexDialogFragment playQueueFragment;
    BirthdayDialogFragment birthDialogFragment;
    private FragmentManager fragmentManager;
    private String DIALOG_SEX_TAG = "sexdialog";
    private String DIALOG_BIR_TAG = "birdialog";
    
    /**
     * 显示选择性别框
     */
    protected void ShowMovementDialog(String url)
    {
        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        playQueueFragment.setSexListener(state -> {
            LOG("回调性别：" + state);
            if(!user_sex.equals("" + state)){
                updateUserInfo("sex",state + "");
            }
        });
        playQueueFragment.show(fragmentManager,DIALOG_SEX_TAG);
    }

    /**
     * 显示选择性别框
     */
    protected void ShowBirthdayDialog()
    {
        if (birthDialogFragment == null) birthDialogFragment = new BirthdayDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        birthDialogFragment.setSexListener(bir -> {
            LOG("回调生日：" + bir);
            birthday = bir;
            if(!PreferenceEntity.perfectInfoBirthday.equals("")){
                updateUserInfo("birthday",PreferenceEntity.perfectInfoBirthday);
            }

        });
        birthDialogFragment.show(fragmentManager,DIALOG_BIR_TAG);
    }



    protected final int GETUSERINFO = 10003;


    public UserEntity mUserEntity;

    public void getUserInfo(){
        setLoading(true, "");

        HomeService service;
        service = RetrofitHelper.getService().getApi();
        service.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {

                        Gson gson = new Gson();
                        try {
                            String str = StringUtils.replaceJson(data.string());
                            mUserEntity = gson.fromJson(str, UserEntity.class);
                        } catch (Exception e) {
                            return;
                        }
                        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                            PreferenceEntity.setUserInfoEntity(mUserEntity.data);
                            setData(mUserEntity.data);
                        } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                            reLoading();
                        } else {
                            ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        LOGUtils.LOG("getModel  onError" + error);
                    }

                    @Override
                    public void onFinish() {
                        setLoading(false, "");
                        LOGUtils.LOG("getModel  onFinish");
                    }
                });
    }

    public void updateUserInfo(String name,String value){
        setLoading(true, "");

        Map<String,String> mMap = new HashMap<>();
        mMap.put(name, value);

        HomeService service;
        service = RetrofitHelper.getService().getApi();
        service.updateUserInfo(mMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {

                        Gson gson = new Gson();
                        try {
                            String str = StringUtils.replaceJson(data.string());
                            mUserEntity = gson.fromJson(str, UserEntity.class);
                        } catch (Exception e) {
                            return;
                        }
                        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                            ToastUtils.showToast(mUserEntity.msg);
                            if(name.equals("sex")){
                                PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_SEX, value);
                                setSexInfo(value, user_header);
                            }else if(name.equals("birthday")){
                                try {
                                    PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_BIR, tranTimes.dateToStamp(value,"yyyy-MM-dd"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                setBirthday(birthday);
                            }

                        } else if (mUserEntity.code == ContextConstant.RESPONSECODE_310) {    //登录信息过时跳转到登录页
                            reLoading();
                        } else {
                            ToastUtils.showToast(NewWidgetSetting.getInstance().filtrationStringbuffer(mUserEntity.msg, "接口信息异常！"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        LOGUtils.LOG("getModel  onError" + error);
                    }

                    @Override
                    public void onFinish() {
                        setLoading(false, "");
                        LOGUtils.LOG("getModel  onFinish");
                    }
                });
    }

    protected MyHandler mHandler;

    protected class MyHandler extends Handler {

        // SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
        private final WeakReference<Context> mActivity;

        protected MyHandler(Context activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 0: // 获取版本号

                    break;
            }
        }

    }


    @BindView(R.id.sc_settings)
    protected ScrollView sc_settings;
    @BindView(R.id.rl_settings_title)
    protected RelativeLayout rl_settings_title;

    @BindView(R.id.rel_sett_info)
    protected RelativeLayout rel_sett_info;
    @BindView(R.id.rl_sett_header)
    protected RelativeLayout rl_sett_header;
    @BindView(R.id.iv_sett_header)
    protected ImageView iv_sett_header;
    @BindView(R.id.iv_sett_header_sex)
    protected ImageView iv_sett_header_sex;
    @BindView(R.id.tv_setti_name)
    protected TextView tv_setti_name;
    @BindView(R.id.tv_setti_phone)
    protected TextView tv_setti_phone;

    @BindView(R.id.ll_sett_phone)
    protected LinearLayout ll_sett_phone;
    @BindView(R.id.tv_sett_phone)
    protected TextView tv_sett_phone;
    @BindView(R.id.tv_sett_phone_value)
    protected TextView tv_sett_phone_value;
    @BindView(R.id.ll_sett_bir)
    protected LinearLayout ll_sett_bir;
    @BindView(R.id.tv_sett_bir)
    protected TextView tv_sett_bir;
    @BindView(R.id.tv_sett_bir_value)
    protected TextView tv_sett_bir_value;
    @BindView(R.id.ll_sett_sex)
    protected LinearLayout ll_sett_sex;
    @BindView(R.id.tv_sett_sex)
    protected TextView tv_sett_sex;
    @BindView(R.id.tv_sett_sex_value)
    protected TextView tv_sett_sex_value;


    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewRBLinearLayout(rl_settings_title, 0, 415, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(rel_sett_info, 0, 123, -1, -1, 80, -1);
//        rel_sett_info.setMinimumHeight(mLayoutUtil.getWidgetHeight(123));
//        mLayoutUtil.drawViewDefaultLayout(rel_settings_title, -1,
//                mLayoutUtil.getWidgetHeight(362) - (int) PreferenceEntity.ScreenTop + mLayoutUtil.getWidgetHeight(215) / 2,
//                -1, -1, (int) PreferenceEntity.ScreenTop, -1);
//        mLayoutUtil.drawViewRBLayout(btn_sett_setting, 117, 56, 0, -1, -1, 0);


        mLayoutUtil.drawViewRBLayout(rl_sett_header, 114, 114, 0, 0, -1, -1);
        mLayoutUtil.drawViewRBLayout(iv_sett_header, 114, 114, 0, 0, -1, -1);
        mLayoutUtil.drawViewRBLayout(iv_sett_header_sex, 40, 40, 0, 0, -1, -1);
//        mLayoutUtil.drawViewRBLayout(tv_setti_name, -1, -1, -1, -1, 10, -1);
//        mLayoutUtil.drawViewRBLayout(tv_setti_phone, -1, -1, -1, -1, -1, 15);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initLogic() {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {
    }

}
