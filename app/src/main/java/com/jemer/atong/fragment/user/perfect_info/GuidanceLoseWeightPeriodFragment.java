/**
 * 
 */
package com.jemer.atong.fragment.user.perfect_info;

import android.widget.ImageView;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.view.loading.RadioHorizonalRuler;

import java.text.DecimalFormat;

import huitx.libztframework.utils.MathUtils;
import huitx.libztframework.utils.PreferencesUtils;

/**
 * 选择减肥周期
 * @author ZhuTao
 * @date 2017/4/21 
 * @params 
*/

public class GuidanceLoseWeightPeriodFragment extends BaseFragment {


	private ImageView iv_guidance_lose_weight_period;
	private TextView tv_glwp;
	private TextView tv_glwp_value;
	private RadioHorizonalRuler view_glwp;
	private TextView tv_glwp_warning;
	private TextView tv_glwp_hint;

	private int sex = -1;


	public GuidanceLoseWeightPeriodFragment() {
		super(R.layout.fragment_guidance_lose_weight_period);
	}


	@Override
	protected void initHead() {


	}
	@Override
	protected void initContent() {
		iv_guidance_lose_weight_period = findViewByIds(R.id.iv_guidance_lose_weight_period);
		tv_glwp = findViewByIds(R.id.tv_glwp);
		view_glwp = findViewByIds(R.id.view_glwp);
		tv_glwp_value = findViewByIds(R.id.tv_glwp_value);
		tv_glwp_warning = findViewByIds(R.id.tv_glwp_warning);
		tv_glwp_hint = findViewByIds(R.id.tv_glwp_hint);

	}
	@Override
	protected void initLocation() {
		mLayoutUtil.setIsFullScreen(true);
		mLayoutUtil.drawViewRBLayout(iv_guidance_lose_weight_period, 132, 132, -1, -1, 140, -1);
		mLayoutUtil.drawViewRBLayout(tv_glwp_value, 0, 0, 0, 0, 176, -1);
		mLayoutUtil.drawViewRBLayout(view_glwp, 0, 110, -1, -1, 38, -1);
		mLayoutUtil.drawViewRBLayout(tv_glwp_warning, 0, 0, 0, 0, 165, -1);
		mLayoutUtil.drawViewRBLayout(tv_glwp_hint, 0, 0, 0, 0, -1, 74);
	}
	@Override
	protected void initLogic() {

	}

	@Override
	public void onResume()
	{
		super.onResume();

		int sex = MathUtils.stringToIntForPreference(PreferenceEntity.KEY_USER_SEX, 1);
		if(sex == 1) iv_guidance_lose_weight_period.setBackgroundResource(R.drawable.iv_man_bef);
		else iv_guidance_lose_weight_period.setBackgroundResource(R.drawable.iv_woman_bef);

//		mWeight = MathUtils.stringToIntForPreference(PreferenceEntity.KEY_USER_INITIAL_WEIGHT, 50);
//		mTargetWeight = MathUtils.stringToIntForPreference(PreferenceEntity.KEY_USER_TARGET_WEIGHT, mWeight);
		mWeight =  PreferencesUtils.getFloat(mContext, PreferenceEntity.KEY_USER_INITIAL_WEIGHT,50.0f);
		mTargetWeight =  PreferencesUtils.getFloat(mContext, PreferenceEntity.KEY_USER_TARGET_WEIGHT,mWeight-1);
		loseWeight = mWeight - mTargetWeight;
		int maxWeek =  Math.round(loseWeight * 10);;
//		int maxWeek = (int) loseWeight * 10;
		warnWeight= mWeight * 0.01f;

		view_glwp.initViewParam(maxWeek, maxWeek, (int)loseWeight, 10);	//设置默认值，最大值，最小值，间隔
		//设置监听
		view_glwp.setValueChangeListener(new RadioHorizonalRuler.OnValueChangeListener(){

			@Override
			public void onValueChange(int value) {
				updateView(value);
			}

		});
		tv_glwp_value.setText(view_glwp.getValue() + "周");

		updateView(view_glwp.getValue());
	}

	private void updateView(int value){
		periodValue = value;
		float averageLoseWeight = 0.1f*10*loseWeight/value;
		DecimalFormat df = new DecimalFormat("#.00");
		averageLoseWeight = Float.parseFloat(df.format(averageLoseWeight));
		if(averageLoseWeight>warnWeight) tv_glwp_warning.setText("减重速度过快");
		else tv_glwp_warning.setText("");
		tv_glwp_value.setText(String.valueOf(value) + "周");
		PreferenceEntity.ValueLostWeightTime = System.currentTimeMillis() + (value * 7L * 24 * 60 * 60 * 1000);
		String times = tranTimes.convert("" + PreferenceEntity.ValueLostWeightTime," yyyy年M月d日");
		LOG("System.currentTimeMillis():" + System.currentTimeMillis());
		LOG("PreferenceEntity.ValueLostWeightTime:" + PreferenceEntity.ValueLostWeightTime);
		tv_glwp_hint.setText("预计在" + times + "(" + 7 * periodValue + "天之后)达成\n平均每周减重" + averageLoseWeight + "公斤");
	}

	private float mWeight,mTargetWeight,loseWeight;
			/** 减肥周数 */
			private int periodValue;
	private float warnWeight;
	/** 保存数据并确定是否可以正常进行下一步 */
	public boolean isNext(){
		PreferencesUtils.putString(mContext, PreferenceEntity.KEY_USER_LOSE_WEIGHT_PERIOD, periodValue + "");
		return true;

	}

	@Override
	protected void pauseClose() {

	}

	@Override
	protected void destroyClose() {

	}

	public void paddingDatas(String mData, int type) {

	}

	@Override
	public void error(String msg, int type) {

	}
}