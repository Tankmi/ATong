package com.jemer.atong.view.dialog.affirm;

import java.io.Serializable;

public class AffirmBean implements Serializable {
    String title;
    String content;
    int titleColor;
    int contentColor;
    String affirm;
    int affirmColor;
    String cancel;
    int cancelColor;

    boolean isBackCancel;

    public boolean isBackCancel() {
        return isBackCancel;
    }

    public void setBackCancel(boolean backCancel) {
        isBackCancel = backCancel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }

    public String getAffirm() {
        return affirm;
    }

    public void setAffirm(String affirm) {
        this.affirm = affirm;
    }

    public int getAffirmColor() {
        return affirmColor;
    }

    public void setAffirmColor(int affirmColor) {
        this.affirmColor = affirmColor;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public int getCancelColor() {
        return cancelColor;
    }

    public void setCancelColor(int cancelColor) {
        this.cancelColor = cancelColor;
    }
}
