/**
 * 
 */
package com.jemer.atong.entity.eyesight;

import java.io.Serializable;

public class EyesightHintBean implements Serializable {

    private int imgId;
    private String content;

    public EyesightHintBean(int imgId, String content) {
        this.imgId = imgId;
        this.content = content;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
