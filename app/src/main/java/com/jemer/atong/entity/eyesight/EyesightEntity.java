/**
 * 
 */
package com.jemer.atong.entity.eyesight;


import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;

import java.io.Serializable;
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
public class EyesightEntity implements Serializable {

   public int code;
   public String cost;
   public String msg;
   public Data data;

   public static class Data implements Serializable {
       public String lefteye;
       public String leftresult;
       public String righteye;
       public String rightresult;

       //历史记录
       public List<EyesightHis> list;
       public static class EyesightHis implements Serializable {
           public String lefteye;
           public String leftresult;
           public String righteye;
           public String rightresult;
           public String createtime;
       }
   }
}
