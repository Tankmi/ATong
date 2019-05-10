package com.jemer.atong.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jemer.atong.R;
import com.jemer.atong.activity.user.SelLoginActivity;
import com.jemer.atong.context.Constantss;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;
	private TextView tv_wxentry;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constantss.APP_ID);
		api.handleIntent(getIntent(), this);
		tv_wxentry = (TextView) findViewById(R.id.tv_wxentry);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	/**
	 * 微信发送请求到你的应用，将通过IWXAPIEventHandler接口的onReq方法进行回调
	 * @param baseReq
	 */
	@Override
	public void onReq(BaseReq baseReq) {

	}

	/**
	 * 应用请求微信的响应结果将通过onResp回调。
	 * @param resp
	 */
	@Override
	public void onResp(BaseResp resp) {
		Log.i("spoort_list","微信操作：errCode" + resp.errCode);
		switch (resp.errCode){
			case BaseResp.ErrCode.ERR_OK:	//操作成功
				LOG("用户操作成功");
				if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {	//分享成功
					LOG("用户分享成功");
					break;
				}
				SendAuth.Resp senA = (SendAuth.Resp) resp;
				String code = senA.code;
				Intent intent = new Intent(WXEntryActivity.this, SelLoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("wx_code",code + "");
//				setResult(200,intent);
				WXEntryActivity.this.startActivity(intent);
				LOG("微信登录，code:" + code);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:	//用户拒绝
				LOG("用户拒绝");
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:	//用户取消
				LOG("用户取消");
				break;
		}
		finish();

	}


	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有�?�添加工具�?�，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打�?任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 处理微信向第三方应用发起的消�?
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，�?�分享一段应用的自定义信息�?�接受方的微�?
	 * 客户端会通过这个方法，将这个信息发�?�回接收方手机上的本demo中，当作
	 * 回调�?
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，�?�不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null && (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

	private  void LOG(String data){
		Log.i("spoort_list","微信操作：" + data);
	}
}

