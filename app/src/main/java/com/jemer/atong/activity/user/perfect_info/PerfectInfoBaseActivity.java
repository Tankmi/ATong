package com.jemer.atong.activity.user.perfect_info;

import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.activity.HomeActivity;
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.user.perfect_info.GuidanceBirthdayFragment;
import com.jemer.atong.fragment.user.perfect_info.GuidanceHeightFragment;
import com.jemer.atong.fragment.user.perfect_info.GuidanceLoseWeightPeriodFragment;
import com.jemer.atong.fragment.user.perfect_info.GuidanceSexFragment;
import com.jemer.atong.fragment.user.perfect_info.GuidanceTargetWeightFragment;
import com.jemer.atong.fragment.user.perfect_info.GuidanceWeightFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.view.dialog.DialogUIUtils;

/**
 * 完善信息
 *
 * @author ZhuTao
 * @date 2018/11/16
 * @params
 */


public class PerfectInfoBaseActivity extends BaseFragmentActivity implements View.OnClickListener, GuidanceSexFragment.OnGuidanceSexListener {

    /** 个人中心重设数据，设置完后，需要跳转到weightActivity */
    protected boolean isReinstall;

    public PerfectInfoBaseActivity(int layoutId)
    {
        super(layoutId);
    }

    @Override
    protected void initHead()
    {
        setStatusBarColor(true, true, mContext.getResources().getColor(R.color.transparency));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        showLoading(false);
    }

    private Button btn_perfect_info_close;
    private Button btn_perfect_info_next;    //下一步

    @Override
    protected void initContent()
    {
        btn_perfect_info_close = findViewByIds(R.id.btn_perfect_info_close);
        btn_perfect_info_next = findViewByIds(R.id.btn_perfect_info_next);
        btn_perfect_info_close.setOnClickListener(this);
        btn_perfect_info_next.setOnClickListener(this);
    }

    @Override
    protected void initLocation()
    {
        mLayoutUtil.drawViewDefaultLayout(btn_perfect_info_close, 120, 56, 0, 0, -1, 0);
        mLayoutUtil.drawViewRBLayout(btn_perfect_info_next, 0, 0, 0, 0, 0, 195);
    }

    @Override
    protected void initLogic()
    {
        initMFragments();
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
     * 完善信息 101
     */
    public void postPerfectInfo()
    {
//        RequestParams params = PreferenceEntity.getLoginParams();
//        String height = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_HEIGHT, 160 + "");
//        float weight = PreferencesUtils.getFloat(mContext, PreferenceEntity.KEY_USER_INITIAL_WEIGHT),
//        targetWeight = PreferencesUtils.getFloat(mContext, PreferenceEntity.KEY_USER_TARGET_WEIGHT, 50.0f );
//        String targetCycle = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_LOSE_WEIGHT_PERIOD, 66 + "");
//        String sex = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_SEX, "1");
//
//        params.addBodyParameter("birthday", PreferenceEntity.perfectInfoBirthday + " 00:00:00");
//        params.addBodyParameter("height", height);
//        params.addBodyParameter("weight", weight + "");
//        params.addBodyParameter("targetWeight", targetWeight + "");
//        params.addBodyParameter("targetCycle", targetCycle);
//        params.addBodyParameter("targetTime", PreferenceEntity.ValueLostWeightTime + "");
//        params.addBodyParameter("sex", sex);
//
//        LOG("PreferenceEntity.perfectInfoBirthday：  " + PreferenceEntity.perfectInfoBirthday + " 00:00:00");
//        mgetNetData.GetData(this, UrlConstant.API_SYSISALL, PerfectInfo, params);
//        setLoading(true, "");
    }


    /**
     * 实体类
     */
    protected UserEntity mUserEntity;
    protected final int PerfectInfo = 101;

    public void paddingDatas(String mData, int type)
    {
        setLoading(false, "");
        Gson gson = new Gson();
        try {
            mUserEntity = gson.fromJson(mData, UserEntity.class);
        } catch (Exception e) {
            return;
        }
        if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
            if (type == PerfectInfo) {
                PreferencesUtils.putString(ApplicationData.context, PreferenceEntity.KEY_USER_ISALL, "1");
                if(isReinstall){
                    setResult(200);
                } else  {
                    Intent intent_home = new Intent(this, HomeActivity.class);
                    startActivity(intent_home);
                }

                finish();
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
        super.error(msg,type);
        showLoading(false);
    }


    @Override
    public void onGuidanceSex(int sex)
    {
        LOG("收到回调性别");
        mViewNext();
    }

    private String MAIN_CONTENT_TAG = "main_content";
    private FragmentManager fragmentManager;
    /**
     * 标识加载的页面 0-6
     */
    private int pageNum;
    private int maxNum = 6;

    private GuidanceSexFragment guidanceSexFragment;
    private GuidanceBirthdayFragment guidanceBirthdayFragment;
    private GuidanceHeightFragment guidanceHeightFragment;
    private GuidanceWeightFragment guidanceWeightFragment;
    private GuidanceTargetWeightFragment guidanceTargetWeightFragment;
    private GuidanceLoseWeightPeriodFragment guidanceLoseWeightPeriodFragment;
    private Fragment[] fragmentlists;
    private Fragment mFragment;

    private void initMFragments()
    {
        fragmentlists = new Fragment[maxNum];
        pageNum = -1;
        guidanceSexFragment = new GuidanceSexFragment();
        guidanceSexFragment.setOnGuidanceSexListener(this);
        guidanceBirthdayFragment = new GuidanceBirthdayFragment();
        guidanceHeightFragment = new GuidanceHeightFragment();
        guidanceWeightFragment = new GuidanceWeightFragment();
        guidanceTargetWeightFragment = new GuidanceTargetWeightFragment();
        guidanceLoseWeightPeriodFragment = new GuidanceLoseWeightPeriodFragment();
        fragmentlists[0] = guidanceSexFragment;
        fragmentlists[1] = guidanceBirthdayFragment;
        fragmentlists[2] = guidanceHeightFragment;
        fragmentlists[3] = guidanceWeightFragment;
        fragmentlists[4] = guidanceTargetWeightFragment;
        fragmentlists[5] = guidanceLoseWeightPeriodFragment;
    }

    protected void mViewControl(int hideView, int showView)
    {
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTran = fragmentManager.beginTransaction();
        //隐藏上一个页面
        if (hideView >= 0) {  //第一个值是-1，需要过滤
            mFragment = fragmentlists[hideView];
            fragmentTran.hide(mFragment);
        }

        //显示下一个页面
        mFragment = fragmentlists[showView];
        if (mFragment.isAdded()) fragmentTran.show(mFragment);
        else fragmentTran.replace(R.id.fram_perfect_info, mFragment, MAIN_CONTENT_TAG);
        fragmentTran.commitAllowingStateLoss();

        if (showView == 0) btn_perfect_info_next.setVisibility(View.GONE);
        else {
            btn_perfect_info_next.setVisibility(View.VISIBLE);
            if (showView == maxNum - 1) btn_perfect_info_next.setText("完成");
            else btn_perfect_info_next.setText("继续");
        }

    }

    protected void mViewNext()
    {
        if (pageNum != 0) { //判断页面是否输入内容，是否可以下一步，正向判断，逆向不做判断
            if (pageNum == 1) {   //生日
                GuidanceBirthdayFragment mFragment = (GuidanceBirthdayFragment) fragmentlists[pageNum];
                if (!mFragment.isNext()) return;
            } else if (pageNum == 2) { //身高
                GuidanceHeightFragment mFragment = (GuidanceHeightFragment) fragmentlists[pageNum];
                if (!mFragment.isNext()) return;
            } else if (pageNum == 3) { //体重
                GuidanceWeightFragment mFragment = (GuidanceWeightFragment) fragmentlists[pageNum];
                if (!mFragment.isNext()) return;
            } else if (pageNum == 4) { //设置目标体重
                GuidanceTargetWeightFragment mFragment = (GuidanceTargetWeightFragment) fragmentlists[pageNum];
                if (!mFragment.isNext()) return;
            } else if (pageNum == 5) { //设置减肥周期
                GuidanceLoseWeightPeriodFragment mFragment = (GuidanceLoseWeightPeriodFragment) fragmentlists[pageNum];
                if (!mFragment.isNext()) return;
            }
        }
        if (pageNum < maxNum - 1) {
            LOG("mViewNext,下一步");
            mViewControl(pageNum, pageNum + 1);
            pageNum++;
        } else {  //最后一页，点击后提交数据
            LOG("最后一页，点击后提交数据");
            postPerfectInfo();
        }

    }


    protected void mViewLast()
    {
        if (pageNum >= 1) {
            mViewControl(pageNum, pageNum - 1);
            pageNum--;
        } else {  //退出页面
            finish();
        }

    }

    protected Dialog mBuildDialog;

    private void showLoading(boolean isShowLoading)
    {
        if (isShowLoading) {
            if (mBuildDialog == null)
                mBuildDialog = DialogUIUtils.showLoading(mContext, "", true, true, false, true).show();
            else mBuildDialog.show();
        } else if (mBuildDialog != null) mBuildDialog.dismiss();
        setControlEnable(!isShowLoading);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
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
    public void setControlEnable(boolean state)
    {
        if (state) {
//            btn_phone_login.setEnabled(true);
        } else {
//            btn_phone_login.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view)
    {

    }

}
