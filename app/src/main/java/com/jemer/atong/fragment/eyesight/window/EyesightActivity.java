package com.jemer.atong.fragment.eyesight.window;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jemer.atong.R;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.eyesight.EyesightHintStepBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.RequiresApi;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.ToastUtils;

/**
 * intent 数据， state 1,近视，2，远视
 */
public class EyesightActivity extends EyesightBaseActivity {

   public EyesightActivity() {
       super(R.layout.activity_eye_sight);
       TAG = getClass().getSimpleName();
   }

   @Override
   protected void initHead() {
       setStatusBarColor(true, true, mContext.getResources().getColor(R.color.white));
       iv_title_status_bar_fill.setBackgroundResource(0x00000000);

       eyeSightState = getIntent().getIntExtra("state",1);
       eyeSightStep = 1;
       if (mHandler == null) mHandler = new MyHandler(this);
       if (!EventBus.getDefault().isRegistered(this)) {
           EventBus.getDefault().register(this);
       }



   }

    @Override
   protected void initLogic() {
        eyeSightFragment();
        eyeProcedure = -1;
        updateView();
        LOG("initLogic");
   }

    @Override
    protected void onResume() {
       LOG("onResume");
        super.onResume();

    }

    //再来一遍
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusHintSuccess(EyesightHintStepBean hintbean) {
       if(hintbean.isAgain()){
           eyeProcedure = -1;
           eyeSightStep = 1;
           updateView();
       }
    }

   //保存页面的缓存信息，在onCreate方法中可以进行数据的初始化
   @Override
   protected void onSaveInstanceState(Bundle outState) {
       // TODO Auto-generated method stub
       super.onSaveInstanceState(outState);
       outState.putString("home_datas", "非正常退出！");
   }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       LOG("event.getKeyCode() :" + event.getKeyCode());

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(event.getKeyCode(), event);
    }

   @Override
   protected void pauseClose() {
       super.pauseClose();
       LOG("pauseClose");
   }

   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
   @Override
   protected void destroyClose() {
       super.destroyClose();
       if(mHandler != null) mHandler.removeCallbacksAndMessages(null);
       if (EventBus.getDefault().isRegistered(this)) {
           LOGUtils.LOG("解除EventBus 注册");
           EventBus.getDefault().unregister(this);
       }
   }


}
