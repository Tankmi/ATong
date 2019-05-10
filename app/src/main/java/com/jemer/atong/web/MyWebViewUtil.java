package com.jemer.atong.web;

public class MyWebViewUtil {
   static MyWebViewUtil mMyWebViewUtil;

    public  MyWebViewUtil(){  }

    public static MyWebViewUtil getInstance() {
        synchronized (MyWebViewUtil.class){
            if(mMyWebViewUtil == null){
                mMyWebViewUtil = new MyWebViewUtil();
            }
        }
        return mMyWebViewUtil;
    }

    /** 通过截取获取指定字符串对象 */
    public String getSubString(String strData, String startIndex, String endIndex){
        try{
            return (strData.contains(startIndex) && strData.contains(endIndex))?strData.substring(strData.indexOf(startIndex)+startIndex.length(),strData.indexOf(endIndex)):"";
        }catch(Exception e){
            return "";
        }

    }

    /** 通过截取获取指定字符串对象 */
    public String getSubString(String strData, String startIndex){
        try{
            return strData.contains(startIndex)?strData.substring(strData.indexOf(startIndex)+startIndex.length()):"";
        }catch(Exception e){
            return "";
        }
    }

}
