package com.jemer.atong.fragment.eyesight.net;

import com.jemer.atong.net.base.BaseView;

public interface EyesightView<T> extends BaseView {
    void putEyesightSuccess(T data);
}
