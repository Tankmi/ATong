package huitx.libztframework.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
* @Title: MTextView.java
* @Package com.text.text
* @Description: TODO(自定义TextView 跑马灯)
* @author ZhuTao
* @date 2015年3月25日 下午4:29:47
* @version V1.0
*/
public class MTextViewMarquee extends TextView {


   public MTextViewMarquee(Context context) {
       super(context);
   }

   public MTextViewMarquee(Context context, AttributeSet attrs) {
       super(context, attrs);
   }

    @Override
    public boolean isFocused() {
        return true;
    }
}