package huitx.libztframework.utils;

import java.text.DecimalFormat;

/**
 * 数字位数精简转换
 * @author ZhuTao
 * @date 2017/5/12
 * @params
*/

public class NumberConversion {

	private static NumberConversion mUnitConversion;

	public static NumberConversion getInstance(){
		synchronized(NumberConversion.class){
			if(mUnitConversion==null){
				mUnitConversion=new NumberConversion();
			}
		}
		return mUnitConversion;
	}

	/** 满万转万位 float类型的对象
	*  @param  defaultData 需要转换的对象
	*  @param  places 小数点后保留的位数
	* */
	public String turnOver(float defaultData,String places){
		DecimalFormat df = new DecimalFormat("#0." + places);
		if(defaultData>10000){
			defaultData = defaultData/10000;

			return df.format (defaultData) + "万";
		}else if(defaultData <= 0) {
			return 0 + "";
		}else{
			return df.format (defaultData) + "";
		}
	}

	/** 满万转万位 int类型的对象
	*  @param  defaultData 需要转换的对象
	*  @param  places 小数点后保留的位数
	* */
	public String turnOver(int defaultData,String places){
		DecimalFormat df = new DecimalFormat("#0." + places);
		float data;
		if(defaultData>10000){
			data = (float)defaultData/10000.0f;

			return df.format (data) + "万";
		}else if(defaultData <= 0) {
			return 0 + "";
		}else{
			return defaultData + "";
		}
	}

	/**  int类型的对象 指定位数转换
	*  @param  defaultData 需要转换的对象
	*  @param  places 小数点后保留的位数 一个0代表一位
	*  @param  place 满多少转
	*  @param  replaceName 替代的单位名称
	* */
	public String turnOvers(int defaultData,String places,float place,String replaceName){
		DecimalFormat df = new DecimalFormat("#0." + places);
		float data;
		if(defaultData>place){
			data = (float)defaultData/place;

			return df.format (data) + replaceName;
		}else if(defaultData <= 0) {
			return 0 + "";
		}else{
			return defaultData + "";
		}
	}

	/** 字符串数据转int */
	public Integer StringToInt(String data,int normal){
		if(data == null || data.equals("")) return normal;
		try{
			return Integer.parseInt(data);
		}catch (Exception e){
			return normal;
		}
	}

	/**
	 * 精确小数点后的位数
	 * @param data
	 * @param places 小数点后保留的位数
	 * @return
	 */
	public static float preciseNumber(float data, int places){
		String pattern = "#.";
		for(int i=0; i<places; i++){
            pattern = pattern + "0";
		}
		DecimalFormat df = new DecimalFormat(pattern);
		data = Float.parseFloat(df.format(data));

		return data;
	}

	/** 精简小数点，小数点后为0时，不显示小数点后的位数 */
	public static String reducedPoint(float data){
		DecimalFormat df = new DecimalFormat("###.####");

		return df.format(data);
	}
}
