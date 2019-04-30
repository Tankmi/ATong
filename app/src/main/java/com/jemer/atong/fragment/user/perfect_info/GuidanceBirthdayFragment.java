/**
 * 
 */
package com.jemer.atong.fragment.user.perfect_info;

import android.widget.ImageView;
import android.widget.TextView;

import com.huidf.slimming.R;
import com.huidf.slimming.base.BaseFragment;
import com.huidf.slimming.context.PreferenceEntity;
import com.huidf.slimming.view.perfect_info.BirthdayRelativelayoutWheelView;

import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.ToastUtils;

/**
 * 选择生日
 * @author ZhuTao
 * @date 2017/4/21 
 * @params 
*/

public class GuidanceBirthdayFragment extends BaseFragment implements BirthdayRelativelayoutWheelView.OnBirthListener {

	private ImageView iv_guidance_birthday;
	private TextView tv_guidance_birthday;
	private TextView tv_guidance_birthday_value;
	private BirthdayRelativelayoutWheelView view_guidance_birthday;



	public GuidanceBirthdayFragment() {
		super(R.layout.fragment_guidance_birthday);
	}


	@Override
	protected void initHead() {
	}

	@Override
	protected void initContent() {
		iv_guidance_birthday = findViewByIds(R.id.iv_guidance_birthday);
		tv_guidance_birthday = findViewByIds(R.id.tv_guidance_birthday);
		tv_guidance_birthday_value = findViewByIds(R.id.tv_guidance_birthday_value);
		view_guidance_birthday = findViewByIds(R.id.view_guidance_birthday);
		view_guidance_birthday.setBirthdayListener(this);
	}
	@Override
	protected void initLocation() {
		mLayoutUtil.setIsFullScreen(true);
		mLayoutUtil.drawViewRBLayout(iv_guidance_birthday, 132, 132, -1, -1, 140, -1);
		mLayoutUtil.drawViewRBLayout(tv_guidance_birthday_value, 0, 0, -1, -1, 112, -1);
		mLayoutUtil.drawViewRBLayout(view_guidance_birthday, 0, 0, -1, -1, 84, -1);
	}
	@Override
	protected void initLogic() {

	}

	@Override
	public void onResume()
	{
		super.onResume();
		int sex  = Integer.parseInt(PreferencesUtils.getString(mContext, PreferenceEntity.KEY_USER_SEX, "1"));
		if(sex == 1) iv_guidance_birthday.setBackgroundResource(R.drawable.iv_man_bef);
		else iv_guidance_birthday.setBackgroundResource(R.drawable.iv_woman_bef);

		myear = PreferencesUtils.getString(mContext, PreferenceEntity.perfectInfoBirthday_year, 1989 + "");
		mmonth = PreferencesUtils.getString(mContext, PreferenceEntity.perfectInfoBirthday_month, 11 + "");
		mday = PreferencesUtils.getString(mContext, PreferenceEntity.perfectInfoBirthday_day, 16 + "");

		view_guidance_birthday.setNotifyData(myear, mmonth, mday);
		view_guidance_birthday.getData();
	}

	/** 保存数据并确定是否可以正常进行下一步 */
	public boolean isNext(){
		if(PreferenceEntity.perfectInfoBirthday != null && !PreferenceEntity.perfectInfoBirthday.equals("")){
			PreferencesUtils.putString(mContext, PreferenceEntity.perfectInfoBirthday_year, myear + "");
			PreferencesUtils.putString(mContext, PreferenceEntity.perfectInfoBirthday_month, mmonth + "");
			PreferencesUtils.putString(mContext, PreferenceEntity.perfectInfoBirthday_day, mday + "");
			return true;
		}

		ToastUtils.showToast("请选择生日！");
		return false;

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

	String myear,mmonth,mday;

	@Override
	public void onClick(String year, String month, String day)
	{
		tv_guidance_birthday_value.setText(year + "年" + month + "月" + day + "日");

		myear = year;
		mmonth = month;
		mday = day;
		int m  = Integer.parseInt(month);
		int d  = Integer.parseInt(day);

		PreferenceEntity.perfectInfoBirthday = year + "-" + (m<10?"0"+m:m) + "-" + (d<10?"0"+d:d);
	}
}