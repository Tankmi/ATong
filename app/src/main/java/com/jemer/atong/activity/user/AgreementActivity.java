package com.jemer.atong.activity.user;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragmentActivity;


/**
* @Title: RegisterBaseActivity.java
* @Package com.huidf.fifth.activity.user.login
* @Description: TODO(声明类)
* @author ZhuTao
* @date 2015年12月9日 下午3:57:39
* @version V1.0
*/
public class AgreementActivity extends BaseFragmentActivity implements OnClickListener, OnCheckedChangeListener{

   private WebView web_agreement;

   public AgreementActivity() {
       super(R.layout.activity_agreement);
//		mContext = AgreementActivity.this;
       TAG = getClass().getName();
   }

   @Override
   protected void initHead() {
       mTvTitle.setText("声明");
       setStatusBarColor(true, false, mContext.getResources().getColor(R.color.translucence));
   }

   @Override
   protected void initContent() {
       web_agreement = findViewByIds(R.id.web_agreement);
       web_agreement.loadUrl("file:///android_asset/policy.html");
//		web_agreement.loadUrl("assets://policy.html");
   }


   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
               && event.getAction() == KeyEvent.ACTION_DOWN) {
           finish();
           return true;
       }
       return super.onKeyDown(event.getKeyCode(), event);
   }

   @Override
   protected void initLocation() {


   }

   public void findView(){}


   @Override
   protected void initLogic() { }

   @Override
   protected void destroyClose() { }

   @Override
   protected void pauseClose() { }

   @Override
   public void onClick(View arg0) { }

   @Override
   public void onCheckedChanged(CompoundButton arg0, boolean arg1) {}


}
