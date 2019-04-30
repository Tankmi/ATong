/**
 * 
 */
package com.jemer.atong.entity.user;


import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;

import huitx.libztframework.utils.PreferencesUtils;

/**
* @Title: EvaluateEntity.java
* @Package com.huidf.doctor.entity.more
* @Description: TODO(用户登录信息实体类)
* @author ZhuTao
* @date 2015年3月23日 下午1:49:19
* @version V1.0
*/
public class UserEntity {

   public int code;
   public String cost;
   public String msg;
   public Data data;

   public static class Data {
       public String id;
       public String msg;
       public String age;
       public String balance;
       public String birthday;
       public String head;
       public String name;
       public String address;

       /** 1男，2女 */
       public String sex;
       /** 0未补全个人信息  1已补全 */
       public String isall;
       /** 0未审核  1已审核   2未通过 */
       public String check;
       /** 上传头像，返回的头像地址 */
       public String header;
       /** 上传资格证，返回的图片地址 */
       public String pic;
       public String img;

       /** 动态数 */
       public String count;
       public String account;
       public String phone;
       public String height;
       /** 初始体重 */
       public String weight;
       /** 最新体重 */
       public String latestWeight;
       /** 目标体重 */
       public String targetWeight;
       /** 目标减肥周期 */
       public String targetCycle;
       /** 目标达成时间 */
       public String targetTime;
       public String bmi;
       /** 今日运动时间 */
       public String sumSportTime;



       public String pwd;
       public String imei;
       public String doctorId;
       public String money;
       public String url;
       public int version;

       //微信登录
       /** :type=2时返回以下数据（直接登录） type=1时进入绑定手机号页面 */
       public String type;
       /** 微信唯一标识 */
       public String unionId;

       //用户个人信息
       public String sign;
   }

   public static UserEntity.Data getUserInfo(){
       UserEntity.Data mData = new UserEntity.Data();

       mData.account =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ACCOUNT, "");
       mData.sign =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_SIGNATURE, "");
       mData.name =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_NICK);
       mData.head =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_HEADER);
       mData.bmi =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_BMI);
       mData.height =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_HEIGHT);
       mData.sex =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_SEX);
       mData.age =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_AGE);
       mData.birthday =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_BIR);
       mData.weight =  PreferencesUtils.getFloat(ApplicationData.context, PreferenceEntity.KEY_USER_INITIAL_WEIGHT) + "";
       mData.targetWeight =  PreferencesUtils.getFloat(ApplicationData.context, PreferenceEntity.KEY_USER_TARGET_WEIGHT) + "";
       mData.latestWeight =  PreferencesUtils.getFloat(ApplicationData.context, PreferenceEntity.KEY_USER_CURRENT_WEIGHT) + "";
       mData.targetCycle =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_LOSE_WEIGHT_PERIOD);
       mData.targetTime =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_TARGET_WEIGHT_TIME);
       mData.count =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_DYNAMIC);

       return mData;
   }

}
