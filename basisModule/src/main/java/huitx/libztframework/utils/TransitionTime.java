package huitx.libztframework.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuTao
 * @version V1.0
 * @Title: TransitionTime.java
 * @Package com.huidf.oldversion.util
 * @Description: TODO(时间格式转换与封装)
 * @date 2015年2月3日 下午2:33:27
 */
public class TransitionTime {

    /**
     * 开始的时间
     */
    private static Date endDate;
    public long timeMills = 0;
    private static TransitionTime mTransitionTime;

    public static TransitionTime getInstance()
    {
        synchronized (NumberConversion.class) {
            if (mTransitionTime == null) {
                mTransitionTime = new TransitionTime();
                endDate = new Date(System.currentTimeMillis());
            }
        }
        return mTransitionTime;
    }

    public void setEndDate(long endTime)
    {
        endDate = new Date(endTime);
    }

    /**
     * 时间转换 返回格式化后的指定天数的时间列表数据
     *
     * @param time       1970的毫秒数
     * @param timeFormat 时间的格式 eg: yyyy-MM-dd hh:mm:ss
     * @return
     */
    public List<String> convertTimeList(String time, String timeFormat, int length)
    {
        List<String> timeList = new ArrayList<String>();
        long timeMills = Long.parseLong(time);

        for (int i = 0; i < length; i++) {
            Date date = new Date(timeMills + (i * 24 * 60 * 60 * 1000));
            String strs = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                strs = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeList.add(strs);
        }


        return timeList;
    }

    /**
     * 时间转换
     *
     * @param time       1970的毫秒数 int类型
     * @param timeFormat 时间的格式 eg: yyyy-MM-dd hh:mm:ss
     * @return
     */
    public String convert(int time, String timeFormat){

        return convert(time+"", timeFormat);
    }

    public String convert(long time, String timeFormat){

        return convert(time+"", timeFormat);
    }

    public String convert(String time){

        return convert(time, "yyyy-MM-dd");
    }

    public String convert(long time){

        return convert(time+"", "yyyy-MM-dd");
    }

    /**
     * 时间转换
     *
     * @param time       1970的毫秒数 String 类型
     * @param timeFormat 时间的格式 eg: yyyy-MM-dd hh:mm:ss
     * @return
     */
    public String convert(String time, String timeFormat)
    {
        if(time == null || time.equals("")){
            time = System.currentTimeMillis() + "";
        }
        timeMills = Long.parseLong(time);
        Date date = new Date(timeMills);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return strs;
    }

    /**
     *
     * 秒 转换 h:m:s，对应位数为0就不返
     * */
    public String secondFormating(int second)
    {
        int h, m, s;
        h = second / 3600;
        m = second % 3600 / 60;
        s = second % 3600 % 60;
        String str = (h<=0?"00:":numberCovering(h,":"))
                + (m<=0?"00:":numberCovering(m,":"))
                + (s<=0?"00":numberCovering(s,""));
        return str;
    }

    /** 数字补位，默认不大于10 第一位补0 */
    private String numberCovering(int data,String placeHolder){
        return (data<10?(0 + "" + data):data) + placeHolder;
    }


    /**
     * 获取距离时间数
     *
     * @param startTime
     * @return
     */
    public String twoDateDistance(String startTime)
    {

        if (startTime.equals("")) {

            return "";
        }
        timeMills = Long.parseLong(startTime);
        Date startDate = new Date(timeMills);

        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong <= 0) {
            return "刚刚";
        } else if (timeLong < 60 * 1000)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        }
    }
}
