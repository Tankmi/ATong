package com.jemer.atong.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jemer.atong.R;
import com.jemer.atong.context.Constantss;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
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

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.i("spoort_list","微信操作：errCode" + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Intent intent = new Intent();
			String msg = "";
			if(resp.errCode == 0){	//成功
				msg = "支付成功！";
				intent.putExtra("pay_result","success");
				setResult(200,intent);
			}else if(resp.errCode == -2){	//取消支付
				msg = "您取消了支付";
				intent.putExtra("pay_result","cancel");
				setResult(200,intent);
			}else if(resp.errCode == -1){	//失败
				msg = "支付失败！";
				intent.putExtra("pay_result","fail");
				setResult(200,intent);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("支付结果通知");
			builder.setMessage(msg);
			builder.setInverseBackgroundForced(true);
			// builder.setCustomTitle();
			builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();

				}
			});
			builder.create().show();
		}
	}
}