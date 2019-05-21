package com.jemer.atong.view.perfect_info;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jemer.atong.R;
import com.jemer.atong.context.PreferenceEntity;

import java.util.ArrayList;
import java.util.Calendar;

import huitx.libztframework.utils.LayoutUtil;
import huitx.libztframework.view.wheel.OnWheelChangedListener;
import huitx.libztframework.view.wheel.OnWheelScrollListener;
import huitx.libztframework.view.wheel.WheelView;
import huitx.libztframework.view.wheel.adapter.AbstractWheelTextAdapter;

/**
 * 账户日期选择对话框
 * 
 */
public class ChangeDateBir extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private WheelView wv_ad_year;
	private WheelView wv_ad_month;
	private WheelView wv_ad_day;

	private ArrayList<String> arry_years = new ArrayList<String>();
	private ArrayList<String> arry_months = new ArrayList<String>();
	private ArrayList<String> arry_days = new ArrayList<String>();
	
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDayAdapter;

	/** 选中的对象下标 */
	private int currentYear = 0;
	/** 选中的对象下标 */
	private int currentMonth = 0;
	/** 选中的对象下标 */
	private int currentDay = 0;

	/** 选中的值 */
	private String selectYear;
	/** 选中的值 */
	private String selectMonth;
	/** 选中的值 */
	private String selectDay;
	
	/** 当前月数 */
	private int month;
	/** 当前天数 */
	private int day;
	
	private int maxTextSize = 14;
	private int minTextSize = 14;
	/** 是否手动设置初始值 */
	private boolean issetdata = false;
	/** 显示的条目数 */
	public int visibileItems = 3;
	private OnBirthListener onBirthListener;

	public ChangeDateBir(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
		initYears();
		initMonths(getYear());
		initDays(getDay());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_change_birthday);
		findView();
		if (!issetdata) initData();

		mYearAdapter = new CalendarTextAdapter(context, arry_years, currentYear, maxTextSize, minTextSize);
		wv_ad_year.setVisibleItems(visibileItems);
		wv_ad_year.setViewAdapter(mYearAdapter);
		wv_ad_year.setCurrentItem(currentYear);
		wv_ad_year.setCenterDrawable(R.drawable.back_wheel_birthday_draw);
//		wv_ad_year.closeDrawShadow();
		wv_ad_year.setCyclic(true);

	
		mMonthAdapter = new CalendarTextAdapter(context, arry_months,currentMonth, maxTextSize, minTextSize);
		wv_ad_month.setVisibleItems(visibileItems);
		wv_ad_month.setViewAdapter(mMonthAdapter);
		wv_ad_month.setCurrentItem(currentMonth);
		wv_ad_month.setCenterDrawable(R.drawable.back_wheel_birthday_draw);
//		wv_ad_month.closeDrawShadow();
		wv_ad_month.setCyclic(true);
		
		mDayAdapter = new CalendarTextAdapter(context, arry_days,currentDay, maxTextSize, minTextSize);
		wv_ad_day.setVisibleItems(visibileItems);
		wv_ad_day.setViewAdapter(mDayAdapter);
		wv_ad_day.setCurrentItem(currentDay);
//		wv_ad_day.setCenterDrawable(R.color.consult_time_dialog);
		wv_ad_day.setCenterDrawable(R.drawable.back_wheel_birthday_draw);
//		wv_ad_day.closeDrawShadow();
		wv_ad_day.setCyclic(true);

		wv_ad_year.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
				selectYear = currentText.substring(0, currentText.indexOf("年"));
				currentYear = arry_years.indexOf(currentText);
				initMonths(Integer.parseInt(selectYear));
				mMonthAdapter = new CalendarTextAdapter(context, arry_months,0, maxTextSize, minTextSize);
				wv_ad_month.setVisibleItems(visibileItems);
				wv_ad_month.setViewAdapter(mMonthAdapter);
				wv_ad_month.setCurrentItem(0);
			}
		});

		wv_ad_year.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel .getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
			}
		});

		wv_ad_month.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
				selectMonth = currentText.substring(0, currentText.indexOf("月"));
				currentMonth = arry_months.indexOf(currentText);
				calDays(Integer.parseInt(selectYear), Integer.parseInt(selectMonth));
				initDays(day);
				mDayAdapter = new CalendarTextAdapter(context, arry_days,currentDay, maxTextSize, minTextSize);
				wv_ad_day.setVisibleItems(visibileItems);
				wv_ad_day.setViewAdapter(mDayAdapter);
				wv_ad_day.setCurrentItem(0);
			}
		});

		wv_ad_month.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
			}
		});
		
		wv_ad_day.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDayAdapter);
				selectDay = currentText.substring(0, currentText.indexOf("日"));

			}
		});
		
		wv_ad_day.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDayAdapter);
			}
		});

	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list,
                                      int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			LinearLayout linear = view .findViewById(R.id.linear_bir_item);
			mLayoutUtil.drawViewRBLinearLayout(linear, -1, 70, -1,  -1, -1, -1);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}
	
	public void initYears() {
		for (int i = getYear(); i >= 1940; i--) {
			arry_years.add(i + "年");
		}
	}
	
	/**
	 * 选中的年份
	 *  */
	public void initMonths(int year) {
		if (year != getYear()) {
			month = 12;
		} else {
			month = getMonth();
		}
		arry_months.clear();
		for (int i = 1; i <= month; i++) {
			arry_months.add(i + "月");
		}
	}

	public void initDays(int days) {
		arry_days.clear();
		for (int i = 1; i <= days; i++) {
			arry_days.add(i + "日");
		}
	}
	

	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}
	
	public void initData() {
		setDate(0, 0, 0);
	}

	/**
	 * 设置年月日
	 * 
	 * @param year	是值，非下标
	 * @param month	是值，非下标
	 * @param day	是值，非下标
	 */
	public void setDate(int year, int month,int day) {
		issetdata = true;
		selectYear = arry_years.get(year).substring(0,arry_years.get(year).indexOf("年"));
		selectMonth = arry_months.get(month).substring(0,arry_months.get(year).indexOf("月"));
		selectDay = arry_days.get(day).substring(0,arry_days.get(year).indexOf("日"));
		this.currentYear = year;
		this.currentMonth = month;
		this.currentDay = day;
		if (year == getYear()) {
			this.month = getMonth();
		} else {
			this.month = 12;
		}
		calDays(year, month);
	}
	

	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.day = 31;
				break;
			case 2:
				if (leayyear) {
					this.day = 29;
				} else {
					this.day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.day = 30;
				break;
			}
		}
		if (year == getYear() && month == getMonth()) {
			this.day = getDay() - 6;
		}
	}
	
	/**
	 * 设置默认值
	 * @param start 起始值
	 * @param end 结束值
	 */
	public void setNotifyData(String start, String end, String day){
		int yearIndex = arry_years.indexOf(start + "年");
		int monthIndex = arry_months.indexOf(end + "月");
//		int dayIndex = arry_days.indexOf(end);
		if(yearIndex != -1){
			if(monthIndex != -1){
//				if(dayIndex != -1){
//					setDate(yearIndex,monthIndex,dayIndex);
//				}else{
					setDate(yearIndex,monthIndex,0);
//				}
			}else{
//				if(dayIndex != -1){
//					setDate(yearIndex,0,dayIndex);
//				}else{
					setDate(yearIndex,0,0);
//				}
			}
		}else{
			setDate(0,0,0);
		}
		
	}

	@Override
	public void onClick(View v) {

		if (v == btn_adc_sure) {
			if (onBirthListener != null) {
				onBirthListener.onClick(selectYear, selectMonth, selectDay);
				dismiss();
			}
		}else if(v==btn_adc_cancel){
			onBirthListener.onClick("-1", "-1", "-1");
			dismiss();
		}else if (v == rel_account_date) {
			return;
		} else {
			dismiss();
		}
		dismiss();

	}

	public void findView() {
		// 初始化布局参数
		screenWidth = PreferenceEntity.screenWidth;
		screenHeight = PreferenceEntity.screenHeight;
		mLayoutUtil = new LayoutUtil();

		wv_ad_year = (WheelView) findViewById(R.id.wv_ad_year);
		wv_ad_month = (WheelView) findViewById(R.id.wv_ad_month);
		wv_ad_day = (WheelView) findViewById(R.id.wv_ad_day);

		linear_account_date_main = findViewById(R.id.linear_account_date_main);
		rel_account_date = findViewById(R.id.rel_account_date);
//		rel_account_date_title = findViewById(R.id.rel_account_date_title);
//		tv_ad_title = (TextView) findViewById(R.id.tv_ad_title);
		linear_account_date = findViewById(R.id.linear_account_date);
		rel_account_date_child = findViewById(R.id.rel_account_date_child);
		btn_adc_sure = (TextView) findViewById(R.id.btn_adc_sure);
		btn_adc_cancel = (TextView) findViewById(R.id.btn_adc_cancel);

		linear_account_date_main.setOnClickListener(this);
		rel_account_date.setOnClickListener(this);
		btn_adc_sure.setOnClickListener(this);
		btn_adc_cancel.setOnClickListener(this);

		mLayoutUtil.drawViewRBLinearLayouts(rel_account_date, 0.83f, 0.33f, 0.0f, 0.0f, 0.0f, 0.0f);
//		mLayoutUtil.drawViewRBLayouts(rel_account_date_title, 0.0f, 0.062f, 0.063f, 0.063f, 0.0f, 0.0f);
		
		mLayoutUtil.drawViewRBLayouts(linear_account_date, 0.0f, 0.258f, 0.19f, 0.19f, 0.0f, 0.0f);
		mLayoutUtil.drawViewRBLinearLayouts(wv_ad_year, 0.153f, 0.255f, 0.0f, 0.0f, 0.0f, 0.0f);
		mLayoutUtil.drawViewRBLinearLayouts(wv_ad_month, 0.153f, 0.255f, 0.0f, 0.0f, 0.0f, 0.0f);
		mLayoutUtil.drawViewRBLinearLayouts(wv_ad_day, 0.153f, 0.255f, 0.0f, 0.0f, 0.0f, 0.0f);
		
		mLayoutUtil.drawViewRBLayouts(rel_account_date_child, 0.0f, 0.072f, 0.0f, 0.0f, 0.0f, 0.0f);
		mLayoutUtil.drawViewRBLayouts(btn_adc_sure, 0.415f, 0.072f, 0.0f, 0.0f, 0.0f, 0.0f);
		mLayoutUtil.drawViewRBLayouts(btn_adc_cancel, 0.415f, 0.072f, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	private View linear_account_date_main;
	private View rel_account_date;
//	private View rel_account_date_title;
//	private TextView tv_ad_title;
	private View linear_account_date;
	private View rel_account_date_child;
	private TextView btn_adc_sure;
	private TextView btn_adc_cancel;

	// 布局
	public int screenWidth;
	public int screenHeight;
	protected LayoutUtil mLayoutUtil;
	
	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}
	
	public void setBirthdayListener(OnBirthListener onBirthListener) {
		this.onBirthListener = onBirthListener;
	}

	public interface OnBirthListener {
		public void onClick(String year, String month, String day);
	}

}