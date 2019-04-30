package huitx.libztframework.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import huitx.libztframework.R;
import huitx.libztframework.context.LibApplicationData;

public class NetUtils{

	private static final int CMNET = 3;
	private static final int CMWAP = 2;
	private static final int WIFI = 1;

	/**
	 * 获取当前的网络状态-1：没有网络；1：WIFI网络；2：wap网络；3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(@NonNull Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			 Resources resource = (Resources)context.getResources();
//			ToastUtils.showToast("" + resource.getString(R.string.netstate_error));
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().equalsIgnoreCase("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}
	
	/**
	 * 检查是否有网络
	 */
	public static boolean isAPNType() {
		return isAPNType(LibApplicationData.context);
	}

	public static boolean isAPNType(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())  return true;
		ToastUtils.showToast("" + context.getResources().getString(R.string.netstate_error));
		return false;


	}
}
