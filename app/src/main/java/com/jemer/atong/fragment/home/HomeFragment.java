package com.jemer.atong.fragment.home;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jemer.atong.R;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.PreferenceEntity;
import com.jemer.atong.entity.home.BannerEntity;
import com.jemer.atong.entity.user.PictureEntity;
import com.jemer.atong.fragment.home.net.HomePresenter;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterPresenter;
import com.jemer.atong.net.select_photo.SelectPhotoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.OnClick;
import huitx.libztframework.context.ContextConstant;
import huitx.libztframework.utils.BitmapUtils;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.NewWidgetSetting;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.StringUtils;
import huitx.libztframework.utils.ToastUtils;
import huitx.libztframework.utils.permission.IPermissionListenerWrap;
import huitx.libztframework.utils.permission.PermissionsHelper;

/**
 * 个人中心
 *
 * @author ZhuTao
 * @date 2018/11/28
 * @params
 */

public class HomeFragment extends HomeBaseFragment {


    public HomeFragment() {
        super(R.layout.fragment_home);
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
        if (mPresenter == null) {
            mPresenter = new HomePresenter();
        }
        mPresenter.attachView(this);
    }

    @Override
    protected void initLogic() {
        super.initLogic();

        et_home_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        onRefresh();
        mPresenter.getBannerData();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusSelPhoto(PictureEntity data) {

    }


//    @OnClick({R.id.tv_home_search,R.id.banner_home})
    @OnClick({R.id.tv_home_search})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_search:    //搜索
                LOG("搜索");
                search();
                break;
        }
    }

    private void search() {
        mSearchText = et_home_search.getText().toString();
        if(!StringUtils.isBlank(mSearchText))  onRefresh();

    }

    @Override
    protected void pauseClose() {
        super.pauseClose();
    }

    @Override
    protected void destroyClose() {
        super.destroyClose();
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        if (EventBus.getDefault().isRegistered(this)) {
            LOGUtils.LOG("解除EventBus 注册");
            EventBus.getDefault().unregister(this);
        }
        if (mPresenter != null) mPresenter.detachView();

       if(banner_home != null) banner_home.close();
    }

}
