package huitx.libztframework.utils;

import android.app.Application;
import android.util.Log;

import huitx.libztframework.context.LibApplicationData;

import static android.R.attr.data;

/**
 *
 *  数字操作
 * @author ZhuTao
 * @date 2018/11/27
 * @params
 */

public class MathUtils {

    private static float EPSINON = 0.00001f;

    /**float 数字大小比较 */
    public static boolean compareFloat(float data, float data1)
    {
        if (Math.abs(data - data1) <= EPSINON) return true;
        else return false;
    }


    /** sp中获取缓存数据，转换成float类型，扎心操作，下次框架改回去*/
    public static float getFloatForPreference(String data,float defaultValue){
        return PreferencesUtils.getFloat(LibApplicationData.context, data, defaultValue);
    }
    /** sp中获取缓存数据，转换成float类型，扎心操作，下次框架改回去*/
    public static float stringToFloatForPreference(String data,float defaultValue){
        String value = PreferencesUtils.getString(LibApplicationData.context, data, defaultValue + "");
        return  stringToFloat(value,defaultValue);  //如果对象为空，不会默认附加defaultValue,默认加一遍保险
    }

    public static float stringToFloat(String data){
        return  stringToFloat(data,0);
    }

    public static float stringToFloat(String data, float normal){
        data = NewWidgetSetting.getInstance().filtrationStringbuffer(data,normal+"");
        return Float.parseFloat(data);
    }

    /** sp中获取缓存数据，转换成int类型*/
    public static int stringToIntForPreference(String data,int defaultValue){
        String value = PreferencesUtils.getString(LibApplicationData.context, data, defaultValue + "");
        return  stringToInt(value,defaultValue);  //如果对象为空，不会默认附加defaultValue,默认加一遍保险
    }

    public static int stringToInt(String data){
        return  stringToInt(data,0);
    }

    public static int stringToInt(String data, int normal){
        data = NewWidgetSetting.getInstance().filtrationStringbuffer(data,normal+"");
        return Integer.parseInt(data);
    }



}
