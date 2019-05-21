package com.jemer.atong.fragment.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.eyesight.EyesightBean;
import com.jemer.atong.entity.eyesight.EyesightEntity;
import com.jemer.atong.entity.history.HistoryEntity;
import com.jemer.atong.entity.history.PointLineTableBean;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.eyesight.hint.EyeGuideHintDialogFragment;
import com.jemer.atong.fragment.eyesight.hint.EyeSightSelUserDialogFragment;
import com.jemer.atong.fragment.eyesight.window.EyesightActivity;
import com.jemer.atong.fragment.history.net.HistoryPresenter;
import com.jemer.atong.fragment.history.net.HistoryView;
import com.jemer.atong.fragment.history.view.PointLineView;
import com.jemer.atong.fragment.history.view.YCoordPointLine;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;

@SuppressLint("ValidFragment")
public class HistoryBaseFragment extends BaseFragment implements HistoryView<HistoryEntity.Data>, EyeSightSelUserDialogFragment.OnSelUserListener {

    // 1近视，2远视
    protected int HisState = 1;
    protected String userId = "";

    protected MyHandler mHandler;
    FragmentManager fragmentManager;

    HistoryPresenter mPresenter;

    /**
     * 1,近视，2，远视
     */
    private int eyeState;

    @BindView(R.id.iv_his_translucence)
    ImageView ivHisBg;
    @BindView(R.id.rl_history_title)
    RelativeLayout rl_his_title;
    @BindView(R.id.tv_history_title)
    TextView tv_eyet_title;
    @BindView(R.id.bt_history_family)
    Button selFamily;
    @BindView(R.id.yc_eyesight_hisotry_left)
    YCoordPointLine leftYCoord;
    @BindView(R.id.plv_eyesight_hisotry_left)
    PointLineView leftView;
    @BindView(R.id.yc_eyesight_hisotry_right)
    YCoordPointLine rightYCoord;
    @BindView(R.id.plv_eyesight_hisotry_right)
    PointLineView rightView;

    public HistoryBaseFragment(int layoutId) {
        super(layoutId);
    }

    @OnClick({R.id.tv_history_title, R.id.bt_history_family})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_history_title:
//                HisState = HisState == 1 ? 2 : 1;
//                tv_eyet_title.setText(HisState == 1 ? "近视测试" : "远视测试");
//                mPresenter.getHistoryData(HisState, userId);

                showPopup();
                break;
            case R.id.bt_history_family:

                selUserFragment();
                break;
        }
    }

    PopupWindow popupWindow;
    TextView hisMenuLong, hisMenuShort;

    private void showPopup(){
        if(popupWindow == null){
            View keyboardView = LayoutInflater.from(getActivity()).inflate(R.layout.view_popup_his_sel, null);

            hisMenuLong = keyboardView.findViewById(R.id.tv_his_menu_long);
            hisMenuShort = keyboardView.findViewById(R.id.tv_his_menu_short);

            popupWindow = new PopupWindow(keyboardView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,false);
            //设置点击窗口外边窗口消失
            popupWindow.setOutsideTouchable(true);
            // 设置此参数获得焦点，否则无法点击
            popupWindow.setFocusable(true);

            hisMenuLong.setOnClickListener(view ->{
                HisState = 2;
                tv_eyet_title.setText("远视测试");
                ivHisBg.setVisibility(View.GONE);
               if( popupWindow.isShowing())  popupWindow.dismiss();
                mPresenter.getHistoryData(HisState, userId);
            });

            hisMenuShort.setOnClickListener(view ->{
                HisState = 1;
                tv_eyet_title.setText("近视测试");
                ivHisBg.setVisibility(View.GONE);
                if( popupWindow.isShowing())  popupWindow.dismiss();
                mPresenter.getHistoryData(HisState, userId);

            });
        }


// popupWindow.setBackgroundDrawable(new BitmapDrawable());  //comment by danielinbiti,如果添加了这行，那么标注1和标注2那两行

        ivHisBg.setVisibility(View.VISIBLE);
//        popupWindow.showAtLocation(tv_eyet_title, Gravity.BOTTOM, 0, 0);
        popupWindow.showAsDropDown(tv_eyet_title);
    }

    @Override
    public void onSelUser(String userid) {
        this.userId = userid;
        mPresenter.getHistoryData(HisState, userid);
    }

    EyeSightSelUserDialogFragment selUserFragment;

    /**
     * 选择用户
     */
    private void selUserFragment() {
        if (!hasFamily()) {
            ToastUtils.showToast("请先添加用户！");
            return;
        }
        if (selUserFragment == null) {
            selUserFragment = new EyeSightSelUserDialogFragment();
            selUserFragment.setSelUserListener(this);
        }
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        selUserFragment.show(fragmentManager, "123");
    }

    private boolean hasFamily() {
        String FamilyData = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_CACHE_FAMILY);
        if (!StringUtils.isBlank(FamilyData)) {
            Gson gson = new Gson();
            UserEntity mUserEntity;
            try {
                mUserEntity = gson.fromJson(FamilyData, UserEntity.class);
            } catch (Exception e) {
                return false;
            }
            if (mUserEntity.code == ContextConstant.RESPONSECODE_200) {
                if (mUserEntity.data.list != null && mUserEntity.data.list.size() > 1) {  //有家庭用户
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void initContent() {
//        if(mUserEntity == null){
//            leftView.setData(null);
//            rightView.setData(null);
//            return;
//        }
//
//        if(mUserEntity.list3 != null && mUserEntity.list3.size()>0){
//            mLayoutUtil.drawViewDefaultLinearLayout(
//                    view_weight_hsi_3month, view_weight_hsi_3month.setData(compileData(mUserEntity.list3, "y.M")));
//        }else{
//            view_weight_hsi_3month.setData(null);
//        }

    }

    /**
     * @param mList
     * @param timeSmaple 时间格式"y.M.d"
     * @return
     */
    private List<PointLineTableBean> compileData(List<EyesightEntity.Data.EyesightHis> mList, String timeSmaple) {
        List<PointLineTableBean> mDrawlist = new ArrayList<>();
        for (EyesightEntity.Data.EyesightHis mData : mList) {
            PointLineTableBean lineData = new PointLineTableBean(mData.lefteye, tranTimes.convert(mData.createtime, timeSmaple));
            mDrawlist.add(lineData);
        }

        return mDrawlist;
    }

    @Override
    protected void initHead() {

    }

    @Override
    protected void onVisibile() {
        LOG("onVisibile");
    }

    @Override
    public void getHisSuccess(HistoryEntity.Data data, int state) {
        LOG("state: " + state);
        if (data == null) {
            leftView.setData(null);
            rightView.setData(null);
            return;
        }

        mLayoutUtil.drawViewDefaultLinearLayout(leftView, leftView.setData(compileData(data.list, 1)));
        mLayoutUtil.drawViewDefaultLinearLayout(rightView, rightView.setData(compileData(data.list, 2)));

    }

    private List<PointLineTableBean> compileData(List<HistoryEntity.Data.EyesightHis> mList, int state) {
        String timeSmaple = "M.d";
        List<PointLineTableBean> mDrawlist = new ArrayList<>();
        if (state == 1) {
            for (HistoryEntity.Data.EyesightHis mData : mList) {
                PointLineTableBean lineData = new PointLineTableBean(tranTimes.convert(mData.createtime, timeSmaple), mData.lefteye);
                mDrawlist.add(lineData);
            }

        } else {
            for (HistoryEntity.Data.EyesightHis mData : mList) {
                PointLineTableBean lineData = new PointLineTableBean(tranTimes.convert(mData.createtime, timeSmaple), mData.righteye);
                mDrawlist.add(lineData);
            }

        }

        return mDrawlist;
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

    @Override
    protected void initLocation() {
//        mLayoutUtil.drawViewRBLinearLayout(rl_settings_title, 0, 433, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLayout(rl_his_title, -1, -1, 0, 0, PreferenceEntity.ScreenTop, -1);
        mLayoutUtil.drawViewRBLayout(selFamily, 170, 86, 0, 0, -1, -1);

        mLayoutUtil.drawViewRBLinearLayout(leftYCoord, 80, 330, -1, 0, 0, 0);
        mLayoutUtil.drawViewRBLinearLayout(leftView, -1, 330, 0, 0, 0, 0);
        mLayoutUtil.drawViewRBLinearLayout(rightYCoord, 80, 330, -1, 0, 0, 0);
        mLayoutUtil.drawViewRBLinearLayout(rightView, -1, 330, 0, 0, 0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initLogic() {
    }

    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {

    }

}
