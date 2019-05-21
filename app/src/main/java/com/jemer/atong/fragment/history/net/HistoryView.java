package com.jemer.atong.fragment.history.net;

import com.jemer.atong.net.base.BaseView;

public interface HistoryView<T> extends BaseView {
    /**
     *
     * @param data
     * @param state 1近视，2，远视
     */
    void getHisSuccess(T data,int state);
}
