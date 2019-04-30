/**
 * 
 */
package com.jemer.atong.fragment.user.perfect_info;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jemer.atong.R;
import com.jemer.atong.base.BaseFragment;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.view.loading.VerticalRuler;

import huitx.libztframework.utils.MathUtils;
import huitx.libztframework.utils.PreferencesUtils;

/**
 * 选择身高
 * @author ZhuTao
 * @date 2017/4/21 
 * @params 
*/

public class GuidanceHeightFragment extends BaseFragment {

	private ImageView iv_guidance_height;
	private TextView tv_guidance_height;
	private RelativeLayout rel_guidance_height_view;
	private VerticalRuler view_guidance_height;
	private RelativeLayout rel_guidance_height_view_value;
	private TextView tv_guidance_height_value;


	public GuidanceHeightFragment() {
		super(R.layout.fragment_guidance_height);
	}


	@Override
	protected void initHead() {


	}
	@Override
	protected void initContent() {
		iv_guidance_height = findViewByIds(R.id.iv_guidance_height);
		tv_guidance_height = findViewByIds(R.id.tv_guidance_height);
		rel_guidance_height_view = findViewByIds(R.id.rel_guidance_height_view);
		view_guidance_height = findViewByIds(R.id.view_guidance_height);
		rel_guidance_height_view_value = findViewByIds(R.id.rel_guidance_height_view_value);
		tv_guidance_height_value = findViewByIds(R.id.tv_guidance_height_value);

	}
	@Override
	protected void initLocation() {
		mLayoutUtil.setIsFullScreen(true);
		mLayoutUtil.drawViewRBLayout(iv_guidance_height, 132, 132, -1, -1, 140, -1);

		mLayoutUtil.drawViewRBLayout(rel_guidance_height_view, -1, 643, -1, -1, -1, -1);
		mLayoutUtil.drawViewDefaultLayout(rel_guidance_height_view, mLayoutUtil.getWidgetHeight(622), -1,
				(int) (mLayoutUtil.getScreenWidth() - mLayoutUtil.getWidgetHeight(622) + mLayoutUtil.getWidgetHeight(175))
				, -1, -1, -1);
//		mLayoutUtil.drawViewRBLayout(rel_guidance_height_view, -1, 624, -1, -1, -1, -1);
//		mLayoutUtil.drawViewDefaultLayout(rel_guidance_height_view, mLayoutUtil.getWidgetHeight(622), -1,
//				(int) (mLayoutUtil.getScreenWidth() - mLayoutUtil.getWidgetHeight(622) + mLayoutUtil.getWidgetHeight(175))
//				, -1, -1, -1);
		mLayoutUtil.drawViewDefaultLayout(view_guidance_height, mLayoutUtil.getWidgetHeight(622),  mLayoutUtil.getWidgetHeight(175), 0, -1, mLayoutUtil.getWidgetHeight(311-87.5f), -1);
//		mLayoutUtil.drawViewRBLayout(rel_guidance_height_view_value, 0, 624, -1, 454.5f, -1, -1);
		mLayoutUtil.drawViewRBLayout(rel_guidance_height_view_value, 0, 624, -1, 380, -1, -1);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		int sex  = Integer.parseInt(PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_SEX, "1"));
		if(sex == 1) iv_guidance_height.setBackgroundResource(R.drawable.iv_man_bef);
		else iv_guidance_height.setBackgroundResource(R.drawable.iv_woman_bef);

		mHeight = (int) MathUtils.stringToFloatForPreference(PreferenceEntity.KEY_USER_HEIGHT,160);
		view_guidance_height.initViewParam(mHeight, 240, 100, 10);	//设置默认值，最大值，间隔
		//设置监听
		view_guidance_height.setValueChangeListener(new VerticalRuler.OnValueChangeListener(){


			@Override
			public void onValueChange(int value) {
				mHeight = value;
				tv_guidance_height_value.setText(value + "CM");
			}

		});
		tv_guidance_height_value.setText(view_guidance_height.getValue() + "CM");
	}

	private int mHeight;
	/** 保存数据并确定是否可以正常进行下一步 */
	public boolean isNext(){
		PreferencesUtils.putString(mContext, PreferenceEntity.KEY_USER_HEIGHT, mHeight + "");
		return true;

	}

	@Override
	protected void initLogic() {

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