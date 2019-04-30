package com.jemer.atong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jemer.atong.R;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;

import huitx.libztframework.utils.LOGUtils;

public class WelcomeActivity extends WelcomeBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        mgetNetData = GetNetData.getInstance();
        mContext = ApplicationData.context;
        setStatusBarColor(true, false, 0);

        View main = getLayoutInflater().inflate(R.layout.activity_welcome, null);
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  //隐藏虚拟键盘，自适应
        setContentView(main);

        if (mHandler == null) mHandler = new MyHandler(this);
//        getParameterByIntent();
        //        initXGPush();
//        selIntent();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            selIntent();
        }
    }

    public void getParameterByIntent() {
        Intent mIntent = this.getIntent();
        intent_state = mIntent.getIntExtra("intent_state", -1);
        LOGUtils.LOG("intent 启动，传来的值" + intent_state);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
//        // click.getCustomContent()
//        LOG("信鸽推送 通知被点击 onResumeXGPushClickedResult:" + (click == null ? false : click.toString() + "\n Content" + click.getContent()
//                + "\n getCustomContent" + click.getCustomContent()));
//
//        if (click != null) {
//            PushEntity pushEntity;
//            Gson gson = new Gson();
//            try {
//                pushEntity = gson.fromJson(click.getCustomContent(), PushEntity.class);
//                if (pushEntity.id.equals("1")) {
//                    mHandler.sendEmptyMessageDelayed(3, 200);
//                }else {
//                    selIntent();
//                }
//            } catch (Exception e) {
//
//            }
//        }else{
//            selIntent();
//        }
    }

    protected void selIntent(){
//        if (!PreferencesUtils.getBoolean(mContext, PreferenceEntity.KEY_IS_CLEAR_SP,false)) {    //没清空过缓存的话，清空一下
//            if(PreferencesUtils.clearData(ApplicationData.context)){
//                PreferencesUtils.putBoolean(mContext, PreferenceEntity.KEY_IS_CLEAR_SP,true);
//                mHandler.sendEmptyMessageDelayed(0, 1000);
//            }
//            return;
//        }
        if (PreferenceEntity.isLogin()) {
            mHandler.sendEmptyMessageDelayed(1, 1000);
        } else {
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
//		}else{	//第一次进来，跳转到引导页
//			mHandler.sendEmptyMessageDelayed(2, 200);
//		}
    }

//    protected void initXGPush() {
//        //开启信鸽的日志输出，线上版本不建议调用
//        XGPushConfig.enableDebug(mContext, true);
//
//        XGPushManager.registerPush(this, new XGIOperateCallback() {
//            @Override
//            public void onSuccess(Object data, int flag) {
//                //token在设备卸载重装的时候有可能会变
//                LOG("信鸽推送，注册成功，设备token为：" + data);
//            }
//
//            @Override
//            public void onFail(Object data, int errCode, String msg) {
//                LOG("信鸽推送，注册失败，错误码：" + errCode + ",错误信息：" + msg);
//            }
//        });
//
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
//        XGPushManager.onActivityStoped(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
