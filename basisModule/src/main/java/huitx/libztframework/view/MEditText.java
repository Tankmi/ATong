package huitx.libztframework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
* @Title: MTextView.java
* @Package com.text.text
* @Description: TODO(自定义EditText 否则回车键自定义后，文本不会换行)
* @author ZhuTao
* @date 2015年3月25日 下午4:29:47
* @version V1.0
*/
public class MEditText extends EditText {


   public MEditText(Context context) {
       super(context);
   }

   public MEditText(Context context, AttributeSet attrs) {
       super(context, attrs);
   }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int imeActions = outAttrs.imeOptions&EditorInfo.IME_MASK_ACTION;
        if ((imeActions&EditorInfo.IME_ACTION_DONE) != 0) {
            // clear the existing action
            outAttrs.imeOptions ^= imeActions;
            // set the DONE action
            outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
        }
        if ((outAttrs.imeOptions&EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return connection;
    }

}