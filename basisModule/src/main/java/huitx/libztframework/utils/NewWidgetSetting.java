package huitx.libztframework.utils;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import huitx.libztframework.context.LibApplicationData;
import huitx.libztframework.view.MTextView;


/**
 * @author 作者 E-mail: ZT 
 * @version 创建时间：2016年9月19日 上午10:42:41 
 * 控件管理类 
 */
public class NewWidgetSetting {

	private static NewWidgetSetting mWidgetSetting;

	public static NewWidgetSetting getInstance(){
		synchronized(WidgetSetting.class){
			if(mWidgetSetting==null){
				mWidgetSetting=new NewWidgetSetting();
			}
		}
		return mWidgetSetting;
	}

	/**
	 * 针对ellipsize属性，不能使用append，对换行文本进行拼接
	 * */
	public static String appendViewTextString(String text1,String text2,String normal,boolean feedLine){

		String text = filtrationStringbuffer(text1,normal);
		if(feedLine) text=text + ("\n");
		text = text +  filtrationStringbuffer(text2,normal);
		return text;
	}

	/** 过滤空字符串 如果为空或者null返回指定默认值*/
	public static String filtrationStringbuffer(String text, String normal){
		return text == null?"" + normal:text.equals("")?"" + normal:text.equals("null")?"" + normal:text;
	}

	public static int filtrationStringbuffer(String text, int normal){
		text = text == null?"" + normal:text.equals("")?"" + normal:text.equals("null")?"" + normal:text;
		return Integer.parseInt(text);
	}

	public static float filtrationStringbuffer(String text, float normal){
		text = text == null?"" + normal:text.equals("")?"" + normal:text.equals("null")?"" + normal:text;
		return Float.parseFloat(text);
	}

	/**
	 * 为文本框设置内容，过滤null字段
	 * @param view 视图对象
	 * @param text 文本
	 * @param normal 文本对象为空时的默认值
	 */
	public static void setViewText(TextView view,String text,String normal){
		view.setText(filtrationStringbuffer(text,normal));
	}

	/**
	 * 为文本框设置内容，过滤null字段
	 * @param view 视图对象
	 * @param text1 默认文本(例如：单位)
	 * @param text 文本
	 * @param def 文本的默认值（不是默认文本）
	 * @param state true，默认文本在前
	 * @param hideUnit true，如果只显示文本的默认值（def），就不显示单位
	 *
	 */
	public static void setViewText(TextView view,String text1,String text,String def,boolean state,boolean hideUnit){
		text = filtrationStringbuffer(text,def);
		if(state)
			view.setText((hideUnit?text.equals(def)?"":text1:text1) + text);
		else
			view.setText(text + (hideUnit?text.equals(def)?"":text1:text1));
	}

	/**
	 * 判断文本框内容是否为空
	 * @param view
	 * @param hint 为空时的提示语句
	 * @return 为空返回false
	 */
	public static boolean notNull(TextView view,String hint){

		if(view.getText().toString().trim().length() <=0){
			if(!hint.equals(""))ToastUtils.showToast(hint);
			return false;
		}
		return true;
	}


	/**
	 * 文本框连接特殊字体(颜色，大小)
	 * @param view	文本控件
	 * @param color	特殊字体颜色	-999 不修改颜色
	 * @param percentage	相对大小倍数	1 不修改大小
	 * @param text	添加内容
	 * @param feedLine	是否换行
	 */
	public static void setIdenticalLineTvColor(TextView view, int color, float percentage, String text, boolean feedLine){
		SpannableString spanText = new SpannableString(text);
		if(color != -999)spanText.setSpan(new ForegroundColorSpan(color), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new RelativeSizeSpan(percentage), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		if(feedLine) view.append("\n");
		view.append(spanText);
	}

	/**
	 * 文本框连接特殊字体(颜色，大小)
	 * @param view	文本控件
	 * @param color	特殊字体颜色	-999 不修改颜色
	 * @param percentage	相对大小倍数	1 不修改大小
	 * @param text	添加内容
	 * @param feedLine	是否换行
	 */
	public static void setIdenticalLineTvColor(MTextView view, int color, float percentage, String text, boolean feedLine){
		SpannableString spanText = new SpannableString(text);
		if(color != -999)spanText.setSpan(new ForegroundColorSpan(color), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new RelativeSizeSpan(percentage), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		if(feedLine) view.append("\n");
		view.append(spanText);
	}

	/**
	 * 同一文本框换行后的特殊字体大小	建议使用上面的方法
	 */
	public static void setTextAppend(TextView mTv, String content, float percentage) {
		SpannableString spanText = new SpannableString(content);
		//参数proportion:比例大小
		spanText.setSpan(new RelativeSizeSpan(percentage), 0, spanText.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mTv.append("\n");
		mTv.append(spanText);
	}

	/**
	 * 拼接基于基线的图片(文本上放置图片的位置用“i”标识)
	 * @param view 文本控件
	 * @param id 图片地址
	 * @param width 宽
	 * @param height 高
	 * @param text 文本
	 * @param percentage 文本相对大小倍数
	 * @param color 字体颜色
     * @param feedLine 是否换行
     */
	public static void appendDrawable(TextView view, final int id, final int width, final int height,String text, float percentage, int color, boolean feedLine){
		SpannableString spanText = new SpannableString(text);
		DynamicDrawableSpan drawableSpan = new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
					@Override
					public Drawable getDrawable() {
//						Drawable d = ApplicationData.context.getResources().getDrawable(id);
//						d.setBounds(0, 0, 50, 50);
						return getWeightDrawable(id,width,height);
					}
				};
		int index = text.indexOf("i");
		spanText.setSpan(drawableSpan, index, index + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//		spanText.setSpan(new ImageSpan(getWeightDrawable(id,width,height)), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		if(color != -999)spanText.setSpan(new ForegroundColorSpan(color), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new RelativeSizeSpan(percentage), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		if(feedLine) view.append("\n");
		view.append(spanText);
	}

	/** 调整指定图片的大小
	 * eg：为文本框设置图片 tv.setCompoundDrawables（）
	 * */
	public static Drawable getWeightDrawable(int id, int width, int height){
		LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
		Drawable drawable = null;
		drawable = LibApplicationData.context.getResources().getDrawable(id);
		if(drawable != null){
			drawable.setBounds(0, 0, mLayoutUtil.getWidgetWidth(width) ,mLayoutUtil.getWidgetHeight(height));
		}
		return drawable;
	}

	/** 调整指定图片的大小
	 * eg：为文本框设置图片 tv.setCompoundDrawables（）
	 * @param isZoom 是否需要适配切图比例。false：不适配,显示原始大小
	 * */
	public static Drawable getWeightDrawable(int id, int width, int height, boolean isZoom){
		LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
		Drawable drawable = null;
		drawable = LibApplicationData.context.getResources().getDrawable(id);
		if( isZoom) drawable.setBounds(0, 0, mLayoutUtil.getWidgetWidth(width, true) ,mLayoutUtil.getWidgetHeight(height));
			else drawable.setBounds(0, 0, width ,height);
		return drawable;
	}

	/** 调整指定图片的大小
	 * eg：为文本框设置图片 tv.setCompoundDrawables（）
	 * */
	public static Drawable getWeightDrawable(int id, int width, int height, int margintLeft, int marginTop){
		LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
		Drawable drawable = null;
		drawable = LibApplicationData.context.getResources().getDrawable(id);
		if(drawable != null){
			drawable.setBounds(mLayoutUtil.getWidgetWidth(margintLeft), mLayoutUtil.getWidgetHeight(marginTop), mLayoutUtil.getWidgetWidth(width) ,mLayoutUtil.getWidgetHeight(height));
		}
		return drawable;
	}
}
