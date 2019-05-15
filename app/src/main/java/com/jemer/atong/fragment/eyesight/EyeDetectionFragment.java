package com.jemer.atong.fragment.eyesight;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jemer.atong.R;
import com.jemer.atong.entity.user.PictureEntity;
import com.jemer.atong.fragment.personal_center.net.PersonalCenterPresenter;
import com.jemer.atong.net.select_photo.SelectPhotoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.OnClick;
import huitx.libztframework.utils.BitmapUtils;
import huitx.libztframework.utils.LOGUtils;
import huitx.libztframework.utils.NewWidgetSetting;
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

public class EyeDetectionFragment extends EyeDetectionBaseFragment {

    protected boolean isUpdateHeader = false;
    protected boolean isgetUsetInfo = true;

    public EyeDetectionFragment() {
        super(R.layout.fragment_eye_detection);
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
        if(mPersonPresenter == null){
            mPersonPresenter = new PersonalCenterPresenter();
        }
        mPersonPresenter.attachView(this);

    }

    @Override
    protected void initLogic() {
        super.initLogic();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusUpdatePhone(String state) {

    }




//    @OnClick({R.id.iv_sett_header, R.id.ll_sett_phone, R.id.ll_sett_bir, R.id.ll_sett_sex})
//    void onViewClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_sett_header:    //头像
//                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
//                break;
//        }
//    }


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
        if(mPersonPresenter!=null)mPersonPresenter.detachView();
    }

    private void requestPermission(final String[] permissions) {
        PermissionsHelper
                .init(getActivity())
                .requestPermissions(permissions, new IPermissionListenerWrap.IPermissionListener() {
                    @Override
                    public void onAccepted(boolean isGranted) {
                        LOG("权限申请结果：" + isGranted);
                        if (isGranted) {
                            Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                            intent.putExtra("intent_title", "修改头像");
                            startActivity(intent);
                        } else {
                            PermissionsHelper
                                    .requestDialogAgain(getActivity(), "温馨提示",
                                            "拍摄照片需要获取相应权限"
                                            , "好的", "不给");
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }
}
