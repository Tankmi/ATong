package com.jemer.atong.fragment.personal_center;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jemer.atong.R;
import com.jemer.atong.activity.user.SelLoginActivity;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.view.dialog.affirm.AffirmBean;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.personal_center.dialog.AlterPhoneDialogFragment;
import com.jemer.atong.fragment.personal_center.dialog.BirthdayDialogFragment;
import com.jemer.atong.fragment.personal_center.dialog.SexDialogFragment;
import com.jemer.atong.view.dialog.affirm.AffirmDialogFragment;
import com.jemer.atong.fragment.personal_center.family.FamilyDialogFragment;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterPresenter;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterView;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;

@SuppressLint("ValidFragment")
public class PersonalCenterBaseFragment extends BaseFragment implements
        OnClickListener, PersonalCenterView<UserEntity.Data> {
    protected MyHandler mHandler;

    protected PersonalCenterPresenter mPersonPresenter;
    protected boolean isgetUsetInfo = true;

    private String birthday;
    private String user_header;
    //1男2女
    private String user_sex = "";
    protected String userHeader;

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


    public void setData(UserEntity.Data userInfo) {
        if (userInfo == null) userInfo = UserEntity.getUserInfo();
        NewWidgetSetting.setViewText(tv_setti_name, userInfo.name, "未知");
        birthday = userInfo.birthday == null ? "未设置" : tranTimes.convert(userInfo.birthday, "yyyy.MM.dd");

        NewWidgetSetting.setViewText(tv_sett_phone_value, userInfo.phone, "未知");

        setBirthday(birthday);
        setSexInfo(userInfo.sex, userInfo.head);
    }

    private void setBirthday(String birthday) {
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
        setSexHeader(sex, header);

    }

    SexDialogFragment playQueueFragment;
    BirthdayDialogFragment birthDialogFragment;
    AlterPhoneDialogFragment alterPhoneDialogFragment;
//    FamilyDialogFragment familyDialogFragment;
    protected FragmentManager fragmentManager;
    private String DIALOG_SEX_TAG = "sexdialog";
    private String DIALOG_BIR_TAG = "birdialog";
    private String DIALOG_AP_TAG = "apdialog";
    private String DIALOG_familyadd_TAG = "addfamily";

    /**
     * 显示选择性别框
     */
    protected void ShowSexDialog() {
        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        playQueueFragment.setSexListener(state -> {
            LOG("回调性别：" + state);
            if (!user_sex.equals("" + state)) {
                mPersonPresenter.modificationUserInfo("sex", state + "");
            }
        });
        playQueueFragment.show(fragmentManager, DIALOG_SEX_TAG);
    }

    /**
     * 显示选择生日框
     */
    protected void ShowBirthdayDialog() {
        if (birthDialogFragment == null) birthDialogFragment = new BirthdayDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        birthDialogFragment.setSexListener(bir -> {
            LOG("回调生日：" + bir);
            birthday = bir;
            if (!PreferenceEntity.perfectInfoBirthday.equals("")) {
                mPersonPresenter.modificationUserInfo("birthday", PreferenceEntity.perfectInfoBirthday);
            }

        });
        birthDialogFragment.show(fragmentManager, DIALOG_BIR_TAG);
    }

    /**
     * 显示修改手机号框
     */
    protected void ShowAlterPhoneDialog() {
        if (alterPhoneDialogFragment == null)
            alterPhoneDialogFragment = new AlterPhoneDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        alterPhoneDialogFragment.show(fragmentManager, DIALOG_AP_TAG);
    }

    /**
     * 添加用户
     */
    protected void showAddFamily() {
        FamilyDialogFragment familyDialogFragment = new FamilyDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        familyDialogFragment.show(fragmentManager, DIALOG_BIR_TAG);
    }

    protected void ShowAffirmDialog() {
        AffirmBean bean = new AffirmBean();
        bean.setTitle("");
        bean.setContent("确定退出登录吗？");
        AffirmDialogFragment affirmDialogFragment = AffirmDialogFragment.getInstance(bean);
        affirmDialogFragment.setOnAffirmListener(state -> {
            if(state){
                PreferenceEntity.clearData();
                ApplicationData.getInstance().exit();
                PreferenceEntity.isLogin = false;
                Intent intent = new Intent(getActivity(), SelLoginActivity.class);
                startActivity(intent);
            }
        });
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();

        affirmDialogFragment.show(fragmentManager, "123");

    }

    @Override
    public void changeHeaderSuccess(String url) {
        LOG("用户头像上传成功");
    }

    @Override
    public void changeHeaderFailed(String msg) {
    }

    @Override
    public void getUserInfoSuccess(UserEntity.Data data) {
        isgetUsetInfo = false;
        setData(data);
    }

    @Override
    public void modificationUserInfoSuccess(String name, String value) {
        if (name.equals("sex")) {
            PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_SEX, value);
            setSexInfo(value, user_header);
        } else if (name.equals("birthday")) {
            try {
                PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_BIR, tranTimes.dateToStamp(value, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setBirthday(birthday);
        }
    }

    @Override
    public void loadingShow() {
        setLoading(true, "");
    }

    @Override
    public void loadingDissmis() {
        setLoading(false, "");
    }

    @Override
    public void loginOut() {
        reLoading();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    protected class MyHandler extends Handler {

        // SoftReference<Activity> 也可以使用软应用 只有在内存不足的时候才会被回收
        private final WeakReference<Context> mActivity;

        protected MyHandler(Context activity) {
            mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
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
    @BindView(R.id.ll_sett_family)
    protected LinearLayout ll_sett_family;
    @BindView(R.id.tv_sett_family)
    protected TextView tv_sett_family;
    @BindView(R.id.tv_sett_family_value)
    protected TextView tv_sett_family_value;
    @BindView(R.id.ll_sett_logout)
    protected LinearLayout ll_sett_logout;
    @BindView(R.id.tv_sett_logout)
    protected TextView tv_sett_logout;


    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewRBLinearLayout(rl_settings_title, 0, 433, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(rel_sett_info, 0, 114, -1, -1, 120, -1);
        mLayoutUtil.drawViewRBLayout(rl_sett_header, 114, 114, 0, 0, -1, -1);
        mLayoutUtil.drawViewRBLayout(iv_sett_header, 114, 114, 0, 0, -1, -1);
        mLayoutUtil.drawViewRBLayout(iv_sett_header_sex, 40, 40, 0, 0, -1, -1);
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
