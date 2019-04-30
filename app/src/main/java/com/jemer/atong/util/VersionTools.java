package com.jemer.atong.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * app 获取版本号以及是否存在SDCard
 * @author ZhuTao
 * @date 2018/6/7
 * @params
*/

public class VersionTools {
    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回版本号或者版本名称，默认返回版本号
     */
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            int versioncode = info.versionCode;
            return versioncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** 更新安装包 */
    public static void update(Context context, String path) {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        File file = new File(Environment.getExternalStorageDirectory() , DOWNLOAD_NAME);
        File file = new File(path);
        if(!file.exists()){
           Log.i("sporrt_list","文件不存在！");
            return;
        }
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, ProviderUtil.getFileProviderName(), file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
