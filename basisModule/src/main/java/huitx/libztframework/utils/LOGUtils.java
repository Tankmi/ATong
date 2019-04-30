package huitx.libztframework.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static android.R.attr.data;

/**
 * 打印日志
 * @author ZhuTao
 * @date 2018/11/27
 * @params
*/

public class LOGUtils {

    /** 默认 spoort_list*/
  public static void LOG(String data){
      LOG("spoort_list", data);
  }

  public static void LOG(String TAG, String data){
      LOG(TAG, "", data);
  }

  public static void LOG(String TAG, String name, String data){
      Log.i(TAG, (name== null || name.equals("")?"":(name+"")) + data);
  }

}
