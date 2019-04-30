package com.jemer.atong.net.base;

/**
 * 和界面相关
 * @param <T>
 */
public interface BaseView<T> {

    void loadingShow();
    void loadingDissmis();
    void loginOut();
    void setPresenter(T presenter);
}
