/**
 * 
 */
package com.jemer.atong.entity.home;


import java.util.List;

public class HomeEntity {

   public int code;
   public String cost;
   public String msg;
   public Data data;

   public static class Data {
       public List<HomeData> list;

       public static class HomeData {
           public String title;
           public String content;
           public String image;
           public String url;
       }
   }


}
