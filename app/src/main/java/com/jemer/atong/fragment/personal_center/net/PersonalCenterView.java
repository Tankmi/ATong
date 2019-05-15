package com.jemer.atong.fragment.personal_center.net;

import com.jemer.atong.net.base.BasePresenter;
import com.jemer.atong.net.base.BaseView;
import com.jemer.atong.net.model.BaseHttpEntity;

import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:05
 * 描述：动态 mvp
 */
public interface PersonalCenterView extends BaseView {


    void changeHeaderSuccess(String url);

   void changeHeaderFailed(String msg);


}
