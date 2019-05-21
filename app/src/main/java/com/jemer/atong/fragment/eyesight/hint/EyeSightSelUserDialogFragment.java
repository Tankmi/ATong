package com.jemer.atong.fragment.eyesight.hint;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.user.UserEntity;
import com.jemer.atong.fragment.eyesight.view.EyesightSelUserWheelView;
import com.jemer.atong.fragment.user.LoginController;
import com.jemer.atong.fragment.user.LoginPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.NewWidgetSetting;
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
//    private void ShowSexDialog(String url)
//    {
//        if (playQueueFragment == null) playQueueFragment = new SexDialogFragment();
//        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
//        playQueueFragment.setShareInfo(url);
//        playQueueFragment.show(fragmentManager,MOVEMENT_TIME_TAG);
//    }
public class EyeSightSelUserDialogFragment extends BaseDialogFragment implements EyesightSelUserWheelView.OnSelUserListener {

    @BindView(R.id.bt_dialog_eyesight_seluser_close)
    Button mBtClose;
    @BindView(R.id.wv_dialog_eyesight_seluser)
    EyesightSelUserWheelView mSelUserView;
    @BindView(R.id.bt_dialog_eyesight_seluser_affirm)
    Button mBtAffirm;

    private String userId;

    public EyeSightSelUserDialogFragment() {
        super(R.layout.fra_dialog_eyesight_seluser);
    }

    @OnClick({R.id.bt_dialog_eyesight_seluser_close, R.id.bt_dialog_eyesight_seluser_affirm})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_dialog_eyesight_seluser_close:   //关闭

                dismiss();
                break;
            case R.id.bt_dialog_eyesight_seluser_affirm:   //确认
                if(mselUserListener != null){
                    mselUserListener.onSelUser(userId);
                }
                dismiss();
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
        userId = PreferencesUtils.getString(mContext, PreferenceEntity.KEY_CACHE_FAMILY_USERID,"");
        mSelUserView.setSelUserListener(this);
        if(!StringUtils.isBlank(userId)) mSelUserView.setNotifyData(userId);
        mSelUserView.getData();
    }

    @Override
    public void onClick(String id) {
        userId = id;
        LOG("选中的用户ID   " + id);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void initContent() {

    }

    @Override
    protected void initLocation() {
        mLayoutUtil.drawViewRBLinearLayout(mBtClose, 33, 33, -1, -1, -1, -1);
    }


    @Override
    protected void pauseClose() {

    }

    @Override
    protected void destroyClose() {
    }


    public void  setSelUserListener(OnSelUserListener mselUserListener){
        this.mselUserListener = mselUserListener;
    }

    OnSelUserListener mselUserListener;

    public interface OnSelUserListener{
        void onSelUser(String userid);
    }


}
