package huitx.libztframework.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

/**
 * @author 作者 E-mail: ZT 
 * @version 创建时间：2016年9月19日 上午10:42:41 
 * 控件管理类 
 */
public class WidgetSetting {

	private static WidgetSetting mWidgetSetting;

	public static WidgetSetting getInstance(){
		synchronized(WidgetSetting.class){
			if(mWidgetSetting==null){
				mWidgetSetting=new WidgetSetting();
			}
		}
		return mWidgetSetting;
	}


	/** 过滤空字符串 如果为空或者null返回指定默认值*/
	public static String filtrationStringbuffer(String text, String normal){
		return text == null?"" + normal:text.equals("")?"" + normal:text.equals("null")?"" + normal:text;
	}

	/**
	 * 为文本框设置内容，过滤null字段
	 * @param view 视图对象
	 * @param text 文本
	 * @param normal 文本对象为空时的默认值
	 */
	public static void setViewText(TextView view,String text,String normal){
		view.setText(text == null?"" + normal:text.equals("")?"" + normal:text.equals("null")?"" + normal:text);
	}

	/**
	 * 针对ellipsize属性，不能使用append，对换行文本进行拼接
	 * */
	public static String appendViewTextString(String text1,String text2,String normal,boolean feedLine){

		String text = getStr(text1,normal);
		if(feedLine) text=text + ("\n");
		text = text +  getStr(text2,normal);
		return text;
	}

	private static String getStr(String text,String normal){
		return text == null?"" + normal:text.equals("")?"" + normal:text.equals("null")?"" + normal:text;
	}

	
	/**
	 * 为文本框设置内容，过滤null字段
	 * @param view 视图对象
	 * @param text1 默认文本
	 * @param text 文本
	 * @param def 文本的默认值（不是默认文本）
	 * @param state true，默认文本在前
	 * 
	 */
	public static void setViewText(TextView view,String text1,String text,String def,boolean state){
		if(state){
			view.setText(text1 + (text == null?def:text.equals("")?def:text));
		}else{
			view.setText((text == null?def:text.equals("")?def:text) + text1);
		}
	
	}
	
	/**
	 * 判断文本框内容是否为空
	 * @param view
	 * @param hint 为空时的提示语句
	 * @return 为空返回false
	 */
	public static boolean notNull(TextView view,String hint){
		
		if(view.getText().toString().trim().length() <=0){
			ToastUtils.showToast(hint);
			return false;
		}
		
		return true;
		
	}

	/**
	 * 设置文本框文字不同颜色			不建议使用，使用下面的方法
	 * @param view	文本控件
	 * @param color	特殊字体颜色
	 * @param text	文本内容 (所有内容)
	 * @param start	特殊字体设置起始位置
	 */
	public static void setIdenticalLineTvColor(TextView view,int color,String text,int start,int end){
		view.setText("");
		SpannableString spanText = new SpannableString(text);
		spanText.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		view.append(spanText);
	}

	/**
	 * 文本框连接特殊字体(颜色，大小)
	 * @param view	文本控件
	 * @param color	特殊字体颜色
	 * @param percentage	相对大小倍数
	 * @param text	添加内容
	 * @param feedLine	是否换行
	 */
	public static void setIdenticalLineTvColor(TextView view, int color, float percentage, String text, boolean feedLine){
		SpannableString spanText = new SpannableString(text);
		spanText.setSpan(new ForegroundColorSpan(color), 0, spanText.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new RelativeSizeSpan(percentage), 0, spanText.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		if(feedLine) view.append("\n");
		view.append(spanText);
	}


	/**
	 * 同一文本框换行后的特殊字体大小	不建议使用，使用上面的方法
	 */
	public static void setTextAppend(TextView mTv, String content, float percentage) {
		SpannableString spanText = new SpannableString(content);
		//参数proportion:比例大小
		spanText.setSpan(new RelativeSizeSpan(percentage), 0, spanText.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mTv.append("\n");
		mTv.append(spanText);
	}

	/** 调整指定图片的大小
	 * eg：为文本框设置图片 tv.setCompoundDrawables（）
	 * */
	public static Drawable getWeightDrawable(Context context, int id, int width, int height){
		LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
		Drawable drawable = null;
		drawable = context.getResources().getDrawable(id);
		if(drawable != null){
			drawable.setBounds(0, 0, mLayoutUtil.getWidgetWidth(width) ,mLayoutUtil.getWidgetHeight(height));
		}
		return drawable;
	}
}
