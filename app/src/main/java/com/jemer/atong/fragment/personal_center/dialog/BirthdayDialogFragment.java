package com.jemer.atong.fragment.personal_center.dialog;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseDialogFragment;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.view.perfect_info.BirthdayRelativelayoutWheelView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import huitx.libztframework.utils.PreferencesUtils;

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
public class BirthdayDialogFragment extends BaseDialogFragment implements BirthdayRelativelayoutWheelView.OnBirthListener {

    @BindView(R.id.view_birthday_dialog_wheel)
    protected BirthdayRelativelayoutWheelView mBirView;
    @BindView(R.id.btn_birthday_dialog_affirm)
    protected Button btnSubmit;

    public BirthdayDialogFragment() {
        super(R.layout.fragment_dialog_bir);
    }

    @Override
    protected void initHead() {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, mLayoutUtil.getWidgetHeight(550));
        getDialog().setCanceledOnTouchOutside(true);
    }


    @Override
    protected void initLogic() {

    }

    String myear,mmonth,mday;

    @Override
    public void onResume() {
        super.onResume();
        String birStamp = PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_BIR, "");
        if(!birStamp.equals("")){
            myear = tranTimes.convert(birStamp,"yyyy");
            mmonth = tranTimes.convert(birStamp,"MM");
            mday = tranTimes.convert(birStamp,"dd");
        }

        mBirView.setBirthdayListener(this);
        mBirView.setNotifyData(myear, mmonth, mday);
        mBirView.getData();
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

    }

    @OnClick (R.id.btn_birthday_dialog_affirm)
    void onClick() {
        LOG("修改生日");

        int m  = Integer.parseInt(mmonth);
        int d  = Integer.parseInt(mday);
        if(mBirListener != null) mBirListener.onBirth(myear + "." + (m<10?"0"+m:m) + "." + (d<10?"0"+d:d));
        dismiss();
    }

    @Override
    public void onClick(String year, String month, String day) {
        LOG(year + "年" + month + "月" + day + "日");

        myear = year;
        mmonth = month;
        mday = day;
        int m  = Integer.parseInt(month);
        int d  = Integer.parseInt(day);

        PreferenceEntity.perfectInfoBirthday = year + "-" + (m<10?"0"+m:m) + "-" + (d<10?"0"+d:d);
    }

    private onBirthdayListener mBirListener;
    public interface onBirthdayListener {
        /** 1999.9.9 */
        void onBirth(String bir);
    }
    public void setSexListener(onBirthdayListener listener) {
        mBirListener = listener;
    }

}
