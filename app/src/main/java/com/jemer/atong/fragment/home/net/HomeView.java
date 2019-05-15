package com.jemer.atong.fragment.home.net;

import com.jemer.atong.net.base.BaseView;

import java.util.List;

/**
 * 作者：ZhuTao
 * 创建时间：2019/3/26 : 15:05
 * 描述：动态 mvp
 */
public interface HomeView<T,E> extends BaseView {

    void getBannerData(boolean state, List<E> data);

    /**
     *
     * @param state 1,下拉刷新，2，加载更多
     */
    void getListDataSuccess(int state, List<T> data);

    /**
     * 请求失败
     * @param state 1,下拉刷新，2，加载更多
     * @param parameter 搜索的关键字
     */
    void getListDataFailed(int state, String parameter);


}
