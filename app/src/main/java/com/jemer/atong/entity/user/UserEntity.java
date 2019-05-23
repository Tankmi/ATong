/**
 * 
 */
package com.jemer.atong.entity.user;


import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;

import java.util.List;

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
       /** 个人中心，修改头像地址 */
       public String header;
       public String id;
       public String msg;
       public String age;
       public String birthday;
       public String head;
       public String name;

       /** 1男，2女 */
       public String sex;
       /** 0未补全个人信息  1已补全 */
       public String isall;

       public String phone;



       public String pwd;
       public String imei;
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

       public List<FamilyData> list;
       public static class FamilyData{
            public int id;
            public String name;
            public String birthday;
            public String sex;
            /** 是否是本人，0是，1否 */
            public String type;
       }
   }

   public static UserEntity.Data getUserInfo(){
       UserEntity.Data mData = new UserEntity.Data();

       mData.phone =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_ACCOUNT, "");
       mData.sign =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_SIGNATURE, "");
       mData.name =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_NICK);
       mData.head =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_HEADER);
       mData.sex =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_SEX);
       mData.age =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_AGE);
       mData.birthday =  PreferencesUtils.getString(ApplicationData.context, PreferenceEntity.KEY_USER_BIR);

       return mData;
   }

}
