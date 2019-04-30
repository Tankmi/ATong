/**
 * 
 */
package com.jemer.atong.fragment.user.perfect_info;

import android.widget.ImageView;
import android.widget.TextView;

import com.huidf.slimming.R;
import com.huidf.slimming.base.BaseFragment;
import com.huidf.slimming.context.PreferenceEntity;
import com.huidf.slimming.view.loading.RadioHorizonalRulerDecimals;

import huitx.libztframework.utils.MathUtils;
import huitx.libztframework.utils.PreferencesUtils;

/**
 * 选择体重
 * @author ZhuTao
 * @date 2017/4/21 
 * @params 
*/

public class GuidanceWeightFragment extends BaseFragment{

	private ImageView iv_guidance_weight;
	private TextView tv_guidance_weight;
	private RadioHorizonalRulerDecimals view_guidance_weight;
	private TextView tv_guidance_weight_value;

	private int sex = -1;


	public GuidanceWeightFragment() {
		super(R.layout.fragment_guidance_weight);
	}


	@Override
	protected void initHead() {


	}
	@Override
	protected void initContent() {
		iv_guidance_weight = findViewByIds(R.id.iv_guidance_weight);
		tv_guidance_weight = findViewByIds(R.id.tv_guidance_weight);
		view_guidance_weight = findViewByIds(R.id.view_guidance_weight);
		tv_guidance_weight_value = findViewByIds(R.id.tv_guidance_weight_value);

	}
	@Override
	protected void initLocation() {
		mLayoutUtil.setIsFullScreen(true);
		mLayoutUtil.drawViewRBLayout(iv_guidance_weight, 132, 132, -1, -1, 140, -1);
		mLayoutUtil.drawViewRBLayout(tv_guidance_weight_value, 0, 0, 0, 0, 176, -1);
		mLayoutUtil.drawViewRBLayout(view_guidance_weight, 0, 110, -1, -1, 38, -1);
	}
	@Override
	protected void initLogic() {

	}

	@Override
	public void onResume()
	{
		super.onResume();

		int sex  = MathUtils.stringToIntForPreference(PreferenceEntity.KEY_USER_SEX, 1);
		if(sex == 1) iv_guidance_weight.setBackgroundResource(R.drawable.iv_man_bef);
		else iv_guidance_weight.setBackgroundResource(R.drawable.iv_woman_bef);

//		mWeight = MathUtils.stringToFloatForPreference(PreferenceEntity.KEY_USER_INITIAL_WEIGHT,50);
		mWeight = PreferencesUtils.getFloat(mContext, PreferenceEntity.KEY_USER_INITIAL_WEIGHT,50.0f);
		float height = MathUtils.stringToFloatForPreference(PreferenceEntity.KEY_USER_HEIGHT, 100) * 0.01f;
		int minWeight = (int) (height*height*15.5f);

		view_guidance_weight.initViewParam(mWeight<minWeight?minWeight:mWeight, 200, minWeight, 10);	//设置默认值，最大值，最小值，间隔
//		view_guidance_weight.initViewParam(50.1f, 70.7f, 20.1f, 10);	//设置默认值，最大值，最小值，间隔
		//设置监听
		view_guidance_weight.setValueChangeListener(new RadioHorizonalRulerDecimals.OnValueChangeListener(){

			@Override
			public void onValueChange(float value) {
				mWeight = value;
				tv_guidance_weight_value.setText(String.valueOf(value) + "KG");
			}

		});
		tv_guidance_weight_value.setText(view_guidance_weight.getValue() + "KG");
	}

	private float mWeight;
	/** 保存数据并确定是否可以正常进行下一步 */
	public boolean isNext(){
		PreferencesUtils.putFloat(mContext, PreferenceEntity.KEY_USER_INITIAL_WEIGHT, mWeight);
		return true;

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