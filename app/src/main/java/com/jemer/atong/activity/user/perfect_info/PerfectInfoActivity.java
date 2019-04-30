package com.jemer.atong.activity.user.perfect_info;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.jemer.atong.R;

/**
 * 完善信息
 * @author ZhuTao
 * @date 2018/11/16 
 * @params isReinstall true 个人中心重设数据，设置完后，需要跳转到weightActivity
*/


public class PerfectInfoActivity extends PerfectInfoBaseActivity {


    public PerfectInfoActivity()
    {
        super(R.layout.activity_perfect_info);
        TAG = getClass().getSimpleName();
    }

    @Override
    protected void initHead()
    {
        setStatusBarColor(true, true, mContext.getResources().getColor(R.color.transparency));
        isReinstall = getIntent().getBooleanExtra("isReinstall",false);
    }

    @Override
    protected void initLogic()
    {
        super.initLogic();
//        SOHGuidanceSexView(true);
        mViewNext();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        LOG("onNewIntent");
//        setIntent(intent);
    }


    @Override
    public void onClick(View view)
    {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_perfect_info_close:
                mViewLast();
                break;
            case R.id.btn_perfect_info_next:    //下一步
                mViewNext();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private Button btn_perfect_info_close;



    @Override
    protected void pauseClose()
    {
    }

    @Override
    protected void destroyClose()
    {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            SOHGuidanceSexView(false);
            mViewLast();
            return true;
        }
        return super.onKeyDown(event.getKeyCode(), event);
    }

}
