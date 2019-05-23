package com.jemer.atong.fragment.eyesight.window;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragmentActivity;
import com.jemer.atong.entity.eyesight.EyesightBean;
import com.jemer.atong.entity.eyesight.EyesightEntity;
import com.jemer.atong.entity.history.PointLineTableBean;
import com.jemer.atong.fragment.eyesight.dialog.EyesightHintDialogFragment;
import com.jemer.atong.fragment.eyesight.dialog.EyesightResultDialogFragment;
import com.jemer.atong.fragment.eyesight.net.EyesightPresenter;
import com.jemer.atong.fragment.eyesight.net.EyesightView;
import com.jemer.atong.fragment.eyesight.view.Eyesightview;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;

public class EyesightBaseActivity extends BaseFragmentActivity implements Eyesightview.EyesightViewListener, EyesightView<EyesightEntity.Data> {

    protected MyHandler mHandler;

    //state 1,近视，2，远视
    protected static int eyeSightState;
    /**
     * 1，右眼，2，左眼
     */
    protected static int eyeSightStep = 1;

    /**
     * 测试状态，1，测试，2，提示或者生成结论，屏蔽滑动事件
     */
    protected static int eyeSightStatus = 1;
    /**
     * 测试下标，从0开始
     */
    protected static int eyeProcedure = 0;
    //标记是否测试结束
    protected boolean isFinish = false;

    protected float leftEyesight, rightEyesight;
    protected List<EyesightBean> mEyesights;

    EyesightPresenter mPresenter;

    FragmentManager fragmentManager;

    @BindView(R.id.rl_eyesight_view)
    Eyesightview mEyeView;
    @BindView(R.id.iv_eyesight_view)
    ImageView mImgView;
    @BindView(R.id.iv_eyesight_direction_state)
    ImageView mDirectionState;

    public EyesightBaseActivity(int layoutId) {
        super(layoutId);
    }


    @Override
    protected void initContent() {
        mEyeView.setEyesightListener(this);

        mEyesights = new ArrayList<>();
        if (eyeSightState == 1) {
            mEyesights.add(new EyesightBean(random(), 0.1f, 36.4f));
            mEyesights.add(new EyesightBean(random(), 0.15f, 24.2f));
            mEyesights.add(new EyesightBean(random(), 0.2f, 18.2f));
            mEyesights.add(new EyesightBean(random(), 0.25f, 14.6f));
            mEyesights.add(new EyesightBean(random(), 0.3f, 11.5f));
            mEyesights.add(new EyesightBean(random(), 0.4f, 9.2f));
            mEyesights.add(new EyesightBean(random(), 0.5f, 7.2f));
            mEyesights.add(new EyesightBean(random(), 0.6f, 5.7f));
            mEyesights.add(new EyesightBean(random(), 0.8f, 4.1f));
            mEyesights.add(new EyesightBean(random(), 1.0f, 3.6f));
            mEyesights.add(new EyesightBean(random(), 1.2f, 2.3f));
        } else {
            mEyesights.add(new EyesightBean(random(), 0.1f, 3.64f));
            mEyesights.add(new EyesightBean(random(), 0.15f, 2.3f));
            mEyesights.add(new EyesightBean(random(), 0.2f, 1.82f));
            mEyesights.add(new EyesightBean(random(), 0.25f, 1.45f));
            mEyesights.add(new EyesightBean(random(), 0.3f, 1.15f));
            mEyesights.add(new EyesightBean(random(), 0.4f, 0.91f));
            mEyesights.add(new EyesightBean(random(), 0.5f, 0.73f));
            mEyesights.add(new EyesightBean(random(), 0.6f, 0.58f));
            mEyesights.add(new EyesightBean(random(), 0.8f, 0.46f));
            mEyesights.add(new EyesightBean(random(), 1.0f, 0.36f));
//            mEyesights.add(new EyesightBean(random(), 1.2f, 0.23f));
        }
    }

    @Override
    protected void initLocation() {

    }

    EyesightBean eyesightBean;

    protected void updateView() {
        eyeProcedure++;
        eyeSightStatus = 1;
        LOG("总长度：" + mEyesights.size() + "测试 " + (eyeSightStep == 1 ? "右眼" : "左眼"));

        if (eyeProcedure <= mEyesights.size() - 1) {    //继续测试
            eyesightBean = mEyesights.get(eyeProcedure);
            float scale = eyesightBean.getMultiple();

            LOG("测试下标： " + eyeProcedure + "  缩放倍数：" + scale
                    + "  方向： " + eyesightBean.getDirection()
                    + "  度数： " + eyesightBean.getEyesight());

            if (scale <= 1f) {
                LOG("small");
                mImgView.setImageResource(R.drawable.icon_e_small);
            } else {
                LOG("normal");
                mImgView.setImageResource(R.drawable.icon_e);
            }
            mImgView.setScaleX(scale);
            mImgView.setScaleY(scale);
            mImgView.setRotation(getRotation(eyesightBean.getDirection()));
            mImgView.requestLayout();
        } else {  //最后一个值
            LOG("最后一个值");
            getNext();
        }

    }

    //进入下一步
    protected void getNext() {
        if (eyeSightStep == 1) {    //右眼的话，切换到左眼
            rightEyesight = eyesightBean.getEyesight();
            eyeSightStep = 2;
            eyeProcedure = -1;
//            updateView();
            eyeSightFragment();
        } else if (eyeSightStep == 2) {    //左眼的话，生成结论
            eyeSightStatus = 2;
            leftEyesight = eyesightBean.getEyesight();

            isFinish = true;
            LOG("生成结论 rightEyesight : " + rightEyesight
                    + "leftEyesight : " + leftEyesight);

            mPresenter.putEyesightData(eyeSightState, leftEyesight + "", rightEyesight + "");

        }
    }

    @Override
    public void putEyesightSuccess(EyesightEntity.Data data) {
        LOG("数据上传成功！" + eyeSightState);
        eyesightResultFragment(data);
        EventBus.getDefault().post(new PointLineTableBean("1","1"));
    }

    @Override
    public void onEyesightTouch(int direction) {
        LOG((eyeStepHintFragment != null ? String.valueOf((eyeStepHintFragment.isHidden())) : "false") + "eyeSightStatus: " + eyeSightStatus + "滑动方向" + direction);
        if (eyeSightStatus == 1) {
            setHint(direction == eyesightBean.getDirection());
        }
    }

    protected void setHint(boolean state){
        if(state){
            LOG("方向一致");
            mDirectionState.setImageResource(R.drawable.iv_eyesight_correct);
            mDirectionState.setVisibility(View.VISIBLE);
            eyeSightStatus = 2;
            mDirectionState.postDelayed(()->{
                eyeSightStatus = 1;
                mDirectionState.setVisibility(View.GONE);
                updateView();
            }, 500);//500ms 后设置不可见
        }else{
            LOG("方向错误");
            mDirectionState.setImageResource(R.drawable.iv_eyesight_mistake);
            mDirectionState.setVisibility(View.VISIBLE);
            eyeSightStatus = 2;
            mDirectionState.postDelayed(()->{
                eyeSightStatus = 1;
                mDirectionState.setVisibility(View.GONE);
                getNext();
            }, 500);//500ms 后设置不可见
        }

    }

    EyesightHintDialogFragment eyeStepHintFragment;
    EyesightResultDialogFragment eyesightResultDialogFragment;

    protected void eyeSightFragment() {
        mImgView.setVisibility(View.GONE);
        eyeStepHintFragment = EyesightHintDialogFragment.getInstance(eyeSightState, eyeSightStep);
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        eyeStepHintFragment.show(fragmentManager, "123");
    }
    
    protected void eyesightResultFragment(EyesightEntity.Data data) {
        eyesightResultDialogFragment = EyesightResultDialogFragment.getInstance(data);
        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();
        eyesightResultDialogFragment.show(fragmentManager, "122");
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
        private final WeakReference<Activity> mActivity;

        protected MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            LOG("msg.what:" + msg.what);
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    }

    @Override
    protected void pauseClose() {
    }

    @Override
    protected void destroyClose() {
    }

    @Override
    protected void initHead() {
    }

    @Override
    protected void initLogic() {
    }

    private int random() {
        int data = new Random().nextInt(5 - 1) + 1;
        LOG("随机数：  " + data);
        return data;
    }

    public static final int Eyesight_up = 1;
    public static final int Eyesight_down = 2;
    public static final int Eyesight_left = 3;
    public static final int Eyesight_right = 4;

    public static final int Eyesight_rotation_up = -90;
    public static final int Eyesight_rotation_down = 90;
    public static final int Eyesight_rotation_right = 0;
    public static final int Eyesight_rotation_left = 180;

    //返回旋转方向对应的度数
    private static int getRotation(int rotation) {
        return rotation == Eyesight_up ? Eyesight_rotation_up :
                rotation == Eyesight_right ? Eyesight_rotation_right :
                        rotation == Eyesight_down ? Eyesight_rotation_down :
                                Eyesight_rotation_left;
    }
}