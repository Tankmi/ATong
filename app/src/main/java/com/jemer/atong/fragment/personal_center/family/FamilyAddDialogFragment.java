package com.jemer.atong.fragment.personal_center.family;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.personal_center.dialog.BirthdayDialogFragment;
import com.jemer.atong.fragment.personal_center.dialog.SexDialogFragment;
import com.jemer.atong.fragment.personal_center.family.net.FamilyPresenter;
import com.jemer.atong.fragment.personal_center.family.net.FamilyView;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;

//调用方式
//    SexDialogFragment playQueueFragment;
//    private FragmentManager fragmentManager;
//    private String MOVEMENT_TIME_TAG = "sharedialog";
//    /**
//     * 显示分享框
//     */
//    private void ShowSexDialog(String url)
//    {
//        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
//        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
//        playQueueFragment.setShareInfo(url);
//        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
//    }
public class FamilyAddDialogFragment extends BaseDialogFragment implements FamilyView<UserEntity> {

    @BindView(R.id.bt_eyert_left)  Button bt_eyert_left;
    @BindView(R.id.tv_eyert_title)  TextView tv_eyert_title;
    @BindViews({R.id.tv_addfam_bir, R.id.tv_addfam_sex})  protected List<TextView> tvLists;
    @BindView(R.id.et_addfam_name)  protected EditText et_addfam_name;

    private String mSex;    //1男,2女
    private String myear,mmonth,mday;

    protected FamilyPresenter mPresenter;
    private boolean isGetNetData = false;

    public FamilyAddDialogFragment() {
        super(R.layout.fragment_add_family);
        TAG = getClass().getSimpleName();
    }


    @OnClick({R.id.tv_addfam_bir, R.id.tv_addfam_sex, R.id.btn_addfam_affirm, R.id.bt_eyert_left})
    void inputInfo(View view) {
        switch (view.getId()) {
            case R.id.tv_addfam_bir:   //选择生日
                ShowBirthdayDialog();
                break;
            case R.id.tv_addfam_sex:   //选择性别
                ShowSexDialog();
                break;
            case R.id.btn_addfam_affirm:   //提交
                LOG("提交");
                String name = et_addfam_name.getText().toString();
                String bir = tvLists.get(0).getText().toString();
                String sex = tvLists.get(1).getText().toString();
                if(StringUtils.isBlank(name) || StringUtils.isBlank(bir) || StringUtils.isBlank(sex)){
                    ToastUtils.showToast("请检查信息是否输入完整。");
                    return;
                }
                mPresenter.addFamily(name,PreferenceEntity.perfectInfoBirthday,sex.equals("男")?"1":"2");
                break;
            case R.id.bt_eyert_left:   //返回
                LOG("返回");
                if(isGetNetData) return;
                if(mAddListener != null) mAddListener.onAddFamily(false);
                dismiss();
                break;
        }
    }

    @Override
    protected void initHead() {
        tv_eyert_title.setText("添加成员");
        if(mPresenter == null){
            mPresenter = new FamilyPresenter();
            mPresenter.attachView(this);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(677));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        //去除阴影
        WindowManager.LayoutParams layoutParams =  getDialog().getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        getDialog().getWindow().setAttributes(layoutParams);

    }


    @Override
    protected void initLogic() {
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(isGetNetData) return true;
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP: //键盘松开
                            if(mAddListener != null) mAddListener.onAddFamily(false);
                            dismiss();
                            break;
                        case KeyEvent.ACTION_DOWN: //键盘按下
                            break;
                    }
                    return true;
                }else if(keyCode == KeyEvent.KEYCODE_MENU) {
                    LOG("KEYCODE_MENU");
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        LOG("onResume");
    }

    @Override
    protected void initContent() {
    }

    @Override
    protected void initLocation() {
    }

    @Override
    protected void pauseClose() {

    }

    @Override
    protected void destroyClose() {
        mPresenter.detachView();
    }

    SexDialogFragment playQueueFragment;
    BirthdayDialogFragment birthDialogFragment;
    private FragmentManager fragmentManager;
    private String DIALOG_SEX_TAG = "sexdialog";
    private String DIALOG_BIR_TAG = "birdialog";

    /**
     * 显示选择性别框
     */
    protected void ShowSexDialog()
    {
        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();
        playQueueFragment.setSexListener(state -> {
            LOG("回调性别：" + state);
            tvLists.get(1).setText("" + (state ==1 ?"男":"女"));
        });
        playQueueFragment.show(fragmentManager,DIALOG_SEX_TAG);
    }

    /**
     * 显示选择生日框
     */
    protected void ShowBirthdayDialog()
    {
        if (birthDialogFragment == null) {
            birthDialogFragment = new BirthdayDialogFragment();

            birthDialogFragment.setSexListener(bir -> {
                LOG("回调生日：" + bir);
                tvLists.get(0).setText(bir);

            });
        }
        if (fragmentManager == null) fragmentManager = getChildFragmentManager();

        birthDialogFragment.show(fragmentManager,DIALOG_BIR_TAG);
    }

    @Override
    public void getUserInfoSuccess(UserEntity data) {

    }

    @Override
    public void addUserInfoState(boolean state, String str) {
        if(mAddListener != null){
            mAddListener.onAddFamily(true);
        }
        dismiss();
    }

    @Override
    public void delUserInfoState(boolean state, int str) {

    }

    @Override
    public void loadingShow() {
        isGetNetData = true;
        setLoading(true);
    }

    @Override
    public void loadingDissmis() {
        isGetNetData = false;
        setLoading(false);
    }

    @Override
    public void loginOut() {

    }

    private onAddFamilyListener mAddListener;

    public interface onAddFamilyListener {
        void onAddFamily(boolean state);
    }
    public void setOnAddFamilyListener(onAddFamilyListener listener) {
        mAddListener = listener;
    }

}
