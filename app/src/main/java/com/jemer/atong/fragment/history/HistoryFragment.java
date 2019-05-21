package com.jemer.atong.fragment.history;


import android.content.Intent;
import android.os.Bundle;

import com.jemer.atong.R;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.history.PointLineTableBean;
import com.jemer.atong.fragment.history.net.HistoryPresenter;
import com.jemer.atong.net.select_photo.SelectPhotoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.permission.IPermissionListenerWrap;
import huitx.libztframework.utils.permission.PermissionsHelper;

/**
 * 历史记录
 *
 * @author ZhuTao
 * @date 2018/11/28
 * @params
 */

public class HistoryFragment extends HistoryBaseFragment {

    public HistoryFragment() {
        super(R.layout.fragment_history);
        TAG = getClass().getSimpleName() + "     ";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initHead() {
        if (mHandler == null) mHandler = new MyHandler(mContext);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (mPresenter == null){
            mPresenter = new HistoryPresenter();
        }
        mPresenter.attachView(this);
    }

    @Override
    protected void initLogic() {
        super.initLogic();
        LOG("initLogic");
        mPresenter.getHistoryData(1,"");
    }

    @Override
    public void onResume() {
        super.onResume();
        LOG("onResume");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusRefreshView(PointLineTableBean bean) {
        if(bean != null){
            LOG("刷新页面 ");
            mPresenter.getHistoryData(HisState, userId);
        }else{
            LOG("不刷新页面 ");
        }

    }

    @Override
    protected void pauseClose() {
        LOG("pauseClose");
        super.pauseClose();
    }

    @Override
    protected void destroyClose() {
        LOG("destroyClose");
        super.destroyClose();
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        if (mPresenter != null) mPresenter.detachView();
        if (EventBus.getDefault().isRegistered(this)) {
            LOGUtils.LOG("解除EventBus 注册");
            EventBus.getDefault().unregister(this);
        }
    }

}
