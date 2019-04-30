package com.jemer.atong.base;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;

import com.jemer.atong.context.PreferenceEntity;

import huitx.libztframework.utils.LayoutUtil;
import huitx.libztframework.utils.TransitionTime;

public abstract class MBaseAdapter extends BaseAdapter {
	
	public Context mContext;
	

	/** 时间展示格式转换工具 */
	public TransitionTime tranTimes;
	
	/**
	 *	软键盘的处理
	 * 	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);	//显示软键盘
	 *	imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0); //强制隐藏键盘  
	 */
	public InputMethodManager imm;
	// 布局
	public int screenWidth;
	public int screenHeight;
	protected LayoutUtil mLayoutUtil;
	
	public MBaseAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		init();
	}
	
	/**
	 * 初始化头中的各个控件,以及公共控件ImageLoader
	 * 
	 */
	protected void init() {
		//初始化软键盘操作
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		//初始化布局参数
		screenWidth = PreferenceEntity.screenWidth;
		screenHeight = PreferenceEntity.screenHeight;

		mLayoutUtil = LayoutUtil.getInstance();
		tranTimes = TransitionTime.getInstance();
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);//初始化软键盘处理的方法

	}
	
	public <T> T findViewByIds(View view, int viewId) {
		return (T) view.findViewById(viewId);
	}

	/**
	 * 判断是否登录
	 * @return 登录返回true，否则返回false
	 */
	public boolean isLogin(){
		return PreferenceEntity.isLogin;
	}
	
	/**
	 * 打印日志
	 * @param data	需要打印的内容
	 */
	public void LOG(String data){
		Log.i("spoort_list", data + "");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
