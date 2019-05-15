/**
 * 
 */
package com.jemer.atong.entity.home;


import java.util.List;

public class BannerEntity {

   public int code;
   public String cost;
   public String msg;
   public Data data;

   public static class Data {
       public List<BannerData> list;

       public static class BannerData {
           public String image;
           public String url;
           public String title;
       }
   }


}
