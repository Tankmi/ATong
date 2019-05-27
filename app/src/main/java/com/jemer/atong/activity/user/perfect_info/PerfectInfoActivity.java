package com.jemer.atong.activity.user.perfect_info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.activity.HomeActivity;
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.personal_center.dialog.AlterPhoneDialogFragment;
import com.jemer.atong.fragment.personal_center.dialog.BirthdayDialogFragment;
import com.jemer.atong.fragment.personal_center.dialog.SexDialogFragment;
import com.jemer.atong.fragment.personal_center.family.FamilyDialogFragment;
import com.jemer.atong.net.DefaultObserver;
import com.jemer.atong.net.RetrofitHelper;
import com.jemer.atong.net.service.HomeService;
import com.jemer.atong.view.perfect_info.ChangeDateBir;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.view.dialog.DialogUIUtils;
import huitx.libztframework.view.dialog.listener.DialogUIItemListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 完善信息
 *
 * @author ZhuTao
 * @date 2018/11/16
 * @params isReinstall true 个人中心重设数据，设置完后，需要跳转到weightActivity
 */


public class PerfectInfoActivity extends BaseFragmentActivity {

    @BindViews({R.id.tv_perinfo_bir, R.id.tv_perinfo_sex})
    protected List<TextView> tvLists;
    @BindView(R.id.et_perinfo_name)
    protected EditText et_perinfo_name;
    @BindView(R.id.btn_phone_login)
    protected Button btnSubmit;

    private String mSex;    //1男,2女
    private String myear, mmonth, mday;

    public PerfectInfoActivity() {
        super(R.layout.activity_perfect_info);
        TAG = getClass().getSimpleName();
    }


    @Override
    protected void initHead() {
        setStatusBarColor(false, true, mContext.getResources().getColor(R.color.transparency));
        setTittle("完善资料");
    }


    @Override
    protected void initContent() {
        mSex = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_SEX, "1");
        myear = PreferencesUtils.getString(mContext, PreferenceEntity.perfectInfoBirthday_year, 1989 + "");
        mmonth = PreferencesUtils.getString(mContext, PreferenceEntity.perfectInfoBirthday_month, 11 + "");
        mday = PreferencesUtils.getString(mContext, PreferenceEntity.perfectInfoBirthday_day, 16 + "");

        int m = Integer.parseInt(mmonth);
        int d = Integer.parseInt(mday);
        PreferenceEntity.perfectInfoBirthday = myear + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
        tvLists.get(0).setText(myear + "." + (m < 10 ? "0" + m : m) + "." + (d < 10 ? "0" + d : d));

        tvLists.get(1).setText("" + (mSex.equals("1") ? "男" : "女"));

    }

    @Override
    protected void initLocation() {

    }

    @Override
    protected void initLogic() {
    }


    @OnClick({R.id.ll_perinfo_bir, R.id.tv_perinfo_bir, R.id.ll_perinfo_sex})
    void inputInfo(View view) {
        switch (view.getId()) {
            case R.id.tv_perinfo_bir:   //选择生日
            case R.id.ll_perinfo_bir:   //选择生日
//                selecttime("", "", "");
                ShowBirthdayDialog();
                break;
            case R.id.ll_perinfo_sex:   //选择性别
//                String sexStr = tvLists.get(1).getText().toString();
//                tvLists.get(1).getText();
//                String[] words2 = new String[]{"男", "女"};
//                DialogUIUtils.showSingleChoose(this, "性别", (sexStr != null && !sexStr.equals("")) ? sexStr.equals("男") ? 0 : 1 : 0, words2, new DialogUIItemListener() {
//                    @Override
//                    public void onItemClick(CharSequence text, int position) {
//                        LOG(text + "--" + position);
//                        tvLists.get(1).setText("" + text);
//                    }
//                }).show();
                ShowSexDialog();
                break;
        }
    }


    SexDialogFragment playQueueFragment;
    BirthdayDialogFragment birthDialogFragment;
    AlterPhoneDialogFragment alterPhoneDialogFragment;
    FamilyDialogFragment familyDialogFragment;
    private FragmentManager fragmentManager;
    private String DIALOG_SEX_TAG = "sexdialog";
    private String DIALOG_BIR_TAG = "birdialog";
    private String DIALOG_AP_TAG = "apdialog";
    private String DIALOG_familyadd_TAG = "addfamily";

    /**
     * 显示选择生日框
     */
    protected void ShowBirthdayDialog() {
        if (birthDialogFragment == null) birthDialogFragment = new BirthdayDialogFragment();
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        birthDialogFragment.setSexListener(bir -> {
            LOG("回调生日：" + bir);
            tvLists.get(0).setText(bir);
//            if(!PreferenceEntity.perfectInfoBirthday.equals("")){
//                mPersonPresenter.modificationUserInfo("birthday",PreferenceEntity.perfectInfoBirthday);
//            }

        });
        birthDialogFragment.show(fragmentManager, DIALOG_BIR_TAG);
    }

    /**
     * 显示选择性别框
     */
    protected void ShowSexDialog()
    {
        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        playQueueFragment.setSexListener(state -> {
            LOG("回调性别：" + state);
            mSex = state + "";
            tvLists.get(1).setText(state == 1?"男":"女");
//            if(!user_sex.equals("" + state)){
//                mPersonPresenter.modificationUserInfo("sex",state + "");
//            }
        });
        playQueueFragment.show(fragmentManager,DIALOG_SEX_TAG);
    }


    ChangeDateBir mChangeTimeDialog;

    /**
     * @param year
     * @param month
     * @param day
     */
    public void selecttime(String year, String month, String day) {
        if (mChangeTimeDialog == null) mChangeTimeDialog = new ChangeDateBir(this);
        if (mContext == null) {
            return;
        }
        if (isFinishing()) {
            return;
        }
        mChangeTimeDialog.show();
        mChangeTimeDialog.setBirthdayListener(new ChangeDateBir.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                if (year.equals("-1")) {
                    LOG("取消");
                } else {
                    myear = year;
                    mmonth = month;
                    mday = day;
                    int m = Integer.parseInt(month);
                    int d = Integer.parseInt(day);

                    tvLists.get(0).setText(year + "." + (m < 10 ? "0" + m : m) + "." + (d < 10 ? "0" + d : d));

                    PreferenceEntity.perfectInfoBirthday = year + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
//                    set_age.setText(year + "年" + month + "月" + day + "日");
//                    updateUserInfo("birthday",year + "-" + month + "-" + day);
                }

            }
        });
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_phone_login)
    void submit() {
        String name = et_perinfo_name.getText().toString();

        if (StringUtils.isEmpty(name)) {
            ToastUtils.showToast("请输入姓名。");
            return;
        }

        setLoading(true, "");

        Map<String, String> mMap = new HashMap<>();
        mMap.put("birthday", PreferenceEntity.perfectInfoBirthday + " 00:00:00");
        mMap.put("sex", mSex);
        mMap.put("name", et_perinfo_name.getText().toString());

        HomeService service;
        service = RetrofitHelper.getService().getApi();
        service.perfectInfo(mMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody data) {

                        Gson gson = new Gson();
                        try {
                            String str = StringUtils.replaceJson(data.string());
                            PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_CACHE_FAMILY,str);
                            mUserEntity = gson.fromJson(str, UserEntity.class);
                        } catch (Exception e) {
                            return;
                        }
                        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {

                            String eyeId =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_CACHE_FAMILY_USERID,"");
                            if(StringUtils.isBlank(eyeId)){
                                for (UserEntity.Data.FamilyData datas : mUserEntity.data.list ) {
                                    if(!StringUtils.isBlank(datas.type) && datas.type.equals("0"))
                                        PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_CACHE_FAMILY_USERID, datas.id+"");
                                }
                            }

                            PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_ISALL, "1");
                            Intent intent_home = new Intent(PerfectInfoActivity.this, HomeActivity.class);
                            startActivity(intent_home);
                            finish();
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


    /**
     * 实体类
     */
    protected UserEntity mUserEntity;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            SOHGuidanceSexView(false);
            return true;
        }
        return super.onKeyDown(event.getKeyCode(), event);
    }

    /**
     * 网络请求时等情况时，设置页面的控件是否可以点击
     *
     * @param state
     */
    public void setControlEnable(boolean state) {
        if (state) {
//            btn_phone_login.setEnabled(true);
        } else {
//            btn_phone_login.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void pauseClose() {
        PreferencesUtils.putString(mContext, PreferenceEntity.KEY_USER_SEX, mSex + "");
        PreferencesUtils.putString(mContext, PreferenceEntity.perfectInfoBirthday_year, myear + "");
        PreferencesUtils.putString(mContext, PreferenceEntity.perfectInfoBirthday_month, mmonth + "");
        PreferencesUtils.putString(mContext, PreferenceEntity.perfectInfoBirthday_day, mday + "");
    }

    @Override
    protected void destroyClose() {
    }


}
