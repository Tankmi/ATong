/**
 * 
 */
package com.jemer.atong.entity.eyesight;

import java.io.Serializable;

/**
 *
 * 视力测试页面，弹框提示后，弹框通过 eventbus 回调，返回测试内容
 */
public class EyesightHintStepBean implements Serializable {

    private boolean again;  //重新测试，false 下一步


    public EyesightHintStepBean(boolean again) {
        this.again = again;
    }

    public boolean isAgain() {
        return again;
    }

    public void setAgain(boolean again) {
        this.again = again;
    }
}
