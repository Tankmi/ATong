/**
 * 
 */
package com.jemer.atong.fragment.user.perfect_info;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huidf.slimming.R;
import com.huidf.slimming.base.BaseFragment;
import com.huidf.slimming.context.PreferenceEntity;

import huitx.libztframework.utils.PreferencesUtils;

/**
 * 选择性别
 * @author ZhuTao
 * @date 2017/4/21 
 * @params 
*/

public class GuidanceSexFragment extends BaseFragment implements OnClickListener {

	private TextView tv_loading_sex;	//提示
	private RadioGroup rg_loading_sex;	//
	private RadioButton rb_loading_sex_woman;	//值
	private RadioButton rb_loading_sex_man;	//值
//	private Button btn_guidance_sex_next;	//下一步

	private int sex = -1;


	public GuidanceSexFragment() {
		super(R.layout.fragment_guidance_sex);
	}


	@Override
	protected void initHead() {
		try{
			sex  = Integer.parseInt(PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_SEX, "-1"));
		}catch(Exception e){
			sex = -1;
		}
		tv_loading_sex = findViewByIds(R.id.tv_loading_sex);
		rg_loading_sex = findViewByIds(R.id.rg_loading_sex);
		rb_loading_sex_woman = findViewByIds(R.id.rb_loading_sex_woman);
		rb_loading_sex_man = findViewByIds(R.id.rb_loading_sex_man);

		rb_loading_sex_woman.setOnClickListener(this);
		rb_loading_sex_man.setOnClickListener(this);


	}
	@Override
	protected void initContent() {
	}
	@Override
	protected void initLocation() {
		mLayoutUtil.setIsFullScreen(true);
		mLayoutUtil.drawViewRBLayout(tv_loading_sex,0,0,0,0,138,0);
		mLayoutUtil.drawViewRBLayout(rg_loading_sex, 0, 0, 0, 0, 110, 0);
		mLayoutUtil.drawViewRBLinearLayout(rb_loading_sex_woman, 216, 216, 0, 0, 0, 0);
		mLayoutUtil.drawViewRBLinearLayout(rb_loading_sex_man, 216, 216, 0, 0, 110, 0);
	}
	@Override
	protected void initLogic() {
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.rb_loading_sex_woman: //女
				sex = 2;
				break;
			case R.id.rb_loading_sex_man: //男
				sex = 1;
				break;
//			case btn_guidance_sex_next: // 下一步
//				if(sex == -1){
//					ToastUtils.showToast("请选择性别！");
//					return;
//				}
//				PreferencesUtils.putString(mContext, PreferenceEntity.KEY_USER_SEX, sex + "");
////				startActivity(new Intent(mContext, GuidanceAgeActivity.class));
////				finish();
//				break;
		}
		PreferencesUtils.putString(mContext, PreferenceEntity.KEY_USER_SEX, sex + "");
		if(mGuidanceSexListener != null){
			mGuidanceSexListener.onGuidanceSex(sex);
		}
	}


	private OnGuidanceSexListener mGuidanceSexListener;

	public void setOnGuidanceSexListener(OnGuidanceSexListener mGuidanceSexListener){
		this.mGuidanceSexListener = mGuidanceSexListener;
	}

	public interface OnGuidanceSexListener{
		/** 1,男，2，女 */
		void onGuidanceSex(int sex);
	}


	@Override
	protected void pauseClose() {

	}

	@Override
	protected void destroyClose() {

	}

	@Override
	public void paddingDatas(String mData, int type) {

	}

	@Override
	public void error(String msg, int type) {

	}
}