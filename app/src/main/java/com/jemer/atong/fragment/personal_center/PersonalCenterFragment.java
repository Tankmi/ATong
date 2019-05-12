package com.jemer.atong.fragment.personal_center;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jemer.atong.R;
import com.jemer.atong.activity.HomeBaseActivity;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.net.select_photo.SelectPhotoActivity;

import androidx.annotation.Nullable;
import butterknife.OnClick;
import huitx.libztframework.utils.PreferencesUtils;
import huitx.libztframework.utils.permission.IPermissionListenerWrap;
import huitx.libztframework.utils.permission.Permission;
import huitx.libztframework.utils.permission.PermissionsHelper;

/**
 * 个人中心
 * @author ZhuTao
 * @date 2018/11/28 
 * @params 
*/

public class PersonalCenterFragment extends PersonalCenterBaseFragment {

    public PersonalCenterFragment() {
        super(R.layout.fragment_personal_center);
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initHead() {
        if (mHandler == null) mHandler = new MyHandler(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData(null);
//        getUserInfo();
    }

    @OnClick({R.id.iv_sett_header, R.id.ll_sett_phone, R.id.ll_sett_bir, R.id.ll_sett_sex})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sett_header:    //头像
                requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                break;
            case R.id.ll_sett_phone:    //手机号
                ShowAlterPhoneDialog();
                break;
            case R.id.ll_sett_bir:    //生日
                ShowBirthdayDialog();
                break;
            case R.id.ll_sett_sex:    //性别
                ShowMovementDialog("123");
                break;
        }
    }


    @Override
    protected void pauseClose() {
        super.pauseClose();
    }

    @Override
    protected void destroyClose() {
        super.destroyClose();
        if(mHandler != null) mHandler.removeCallbacksAndMessages(null);
    }




    private void requestPermission(final String[] permissions) {
        PermissionsHelper
                .init(getActivity())
                .requestEachPermissions(permissions, new IPermissionListenerWrap.IEachPermissionListener() {
                    @Override
                    public void onAccepted(Permission permission) {
                        show(permission);
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    private void show(Permission permission) {
        if (permission.granted) {
            // 授予权限
            Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
            intent.putExtra("intent_title", "修改头像");
//            startActivityForResult(intent, Intent_Photo_100);
            startActivity(intent);
        } else {
            if (permission.shouldShowRequestPermissionRationale) {
//                show("没有勾选不再提醒，拒绝权限 ：" + permission.name);
            } else {
                PermissionsHelper
                        .requestDialogAgain(getActivity(), "温馨提示",
                                "拍摄照片需要获取相应权限"
                                , "好的", "不给");
//                show("勾选不再提醒，拒绝权限 ：" + permission.name);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG("fragment onresult");
    }
}
