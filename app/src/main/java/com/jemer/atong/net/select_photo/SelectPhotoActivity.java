package com.jemer.atong.net.select_photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import com.jemer.atong.R;
import com.jemer.atong.util.ProviderUtil;

import java.io.File;

import androidx.core.content.FileProvider;
import huitx.libztframework.utils.BitmapUtils;

/**
 * @author : Zhutao
 * @version 创建时间：@2017年1月16日
 * @Description: 个人中心，个人设置
 * @params：
 */
public class SelectPhotoActivity extends SelectPhotoBaseActivity {

    /**
     * 记录页面退出时间
     */
    private long exitTime = 0;

    public SelectPhotoActivity() {
        super(R.layout.activity_select_photo);
        TAG = this.getClass().getName();
    }

    @Override
    protected void initHead() {
        super.initHead();
        setStatusBarColor(true, mContext.getResources().getColor(R.color.status_bar_color));
        setTittle("照片");
        mHandler = new MyHandler(this);
    }

    @Override
    protected void initLogic() {
        super.initLogic();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_title_view_right: //完成
                LOG("完成");
                if(IsTailor){
                    IntentCutOutPhoto(BitmapUtils.compressImg(selectedImagePath + "",480,800));
                }else{
                    modifyOperation(selectedImagePath);
                }
                break;
//            case R.id.textEnter:
//                Intent intent = new Intent(mActivity, MainActivity.class);
//                intent.putExtra("resultPath", resultList);
//                setResult(200, intent);
//                finish();
//                break;
        }
    }

    /** 标记是否需要裁剪 */
    public boolean IsTailor = false;
    /** 用来标识截取照片的activity */
    private static final int PHOTO_CUT_OUT_DATA = 1003;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_WITH_DATA) {
//            resultList.clear();
//            String absolutePath = createCreamePath.getAbsolutePath();
//            resultList.add(absolutePath);
//            Intent intent = new Intent(mContext, MainActivity.class);
//            intent.putExtra("resultPath", resultList);
//            setResult(200, intent);
//            finish();

            if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists() && mCurrentPhotoFile.length() > 0) {
                selectedImagePath = mCurrentPhotoFile.getAbsolutePath();
                if(IsTailor){
                    IntentCutOutPhoto(BitmapUtils.compressImg(selectedImagePath + "",480,800));
                }else{
                    modifyOperation(selectedImagePath);
                }

            }else{	//没有获取到照片
                LOG("mCurrentPhotoFile != null: " + "mCurrentPhotoFile = null");
            }
        }
        if (requestCode == PHOTO_CUT_OUT_DATA) {//裁剪
            if(data!=null && data.getExtras() != null){
                Bitmap bitmap =  (Bitmap) data.getExtras().get("data");
                selectedImagePath = BitmapUtils.getImg(bitmap);
                if(bitmap != null) bitmap.recycle();
                modifyOperation(selectedImagePath);
            }
        }

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case Intent_Photo_100: // 设置 头像
//                if (resultCode == 200) {
//                    String uri = data.getExtras().getString("intent_photo_uri");
//                    if (!uri.equals("")) {
//                        uri = BitmapUtils.compressImg(uri);
//                        File file = new File(uri);
//                        if (file.exists() && file.length() > 0) {
//                            userHeader = "file://" + uri;
//                            uploadingCredentials(uri);
//                        } else {
//                            ToastUtils.showToast("图片选择失败，请重新选择！");
//                            return;
//                        }
//                    }
//                } else if (resultCode == 300) {// 取消
//                    LOG("取消了");
//                }
//                break;
//            case 6:
//                if (data != null) {  //获取到昵称！
//                    String user_nick = data.getStringExtra("user_nick");//获取到的昵称！
//                    if (user_nick != null && !user_nick.equals("")) {
//                        userNick = user_nick;
//                        updateUserInfo("name", user_nick);
//                    }
//
//                }
//                break;
//        }
//    }

    private void IntentCutOutPhoto(String uri_cutout){
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");

        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            uri= Uri.parse("file://" + uri_cutout);
        }else{
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            File mCurrentPhotoFile = new File("file:///" + uri_cutout);
//            File mCurrentPhotoFile = new File(uri_cutout);
            uri = FileProvider.getUriForFile(SelectPhotoActivity.this,
                    ProviderUtil.getFileProviderName(), mCurrentPhotoFile);
        }
//         Uri uri = Uri.parse("file:///storage/emulated/0/ljd_doc/1444880835469.jpg");
        intent.setDataAndType(uri, "image/*");
//         intent.setDataAndType("assets://iv_examination.png", "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);



        SelectPhotoActivity.this.startActivityForResult(intent, PHOTO_CUT_OUT_DATA);
    }

    /**
     * 选择照片后操作,回调图片地址
     * */
    private void modifyOperation(String uri){
        Intent intent = new Intent(SelectPhotoActivity.this,getIntent().getClass());
        intent.putExtra("intent_photo_uri", uri);
        setResult(200, intent);
        finish();
    }

    @Override
    protected void pauseClose() {
        super.pauseClose();
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void destroyClose() {
        super.destroyClose();
        if(mHandler != null) mHandler.removeCallbacksAndMessages(null);
    }


    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            return true;
//        }
//        return super.onKeyDown(event.getKeyCode(), event);
//    }

}
