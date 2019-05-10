package com.jemer.atong.share;

import android.content.Context;

import com.jemer.atong.R;
import com.jemer.atong.context.ApplicationData;
import com.jemer.atong.context.Constantss;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import huitx.libztframework.utils.BitmapUtils;
import huitx.libztframework.utils.ToastUtils;

/**
 * 作者：ZhuTao
 * 创建时间：2018/12/25 : 16:19
 * 描述：微信分享
 */
public class WechatShareUtil {

    private static WechatShareUtil mLayoutUtilInstance;

    public static WechatShareUtil getInstance() {
        synchronized (WechatShareUtil.class) {
            if (mLayoutUtilInstance == null) {
                mLayoutUtilInstance = new WechatShareUtil();
            }
        }
        return mLayoutUtilInstance;
    }


    /**
     * 微信分享
     * @param url
     * @param type  1，分享到聊天，2，分享到朋友圈
     * @return
     */
    public static boolean wechatShare(Context mContext, String url, int type)
    {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, Constantss.APP_ID, false);
        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtils.showToast("您的手机暂不支持微信操作，请下载最新版本的微信！");
            return false;
        }
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = ApplicationData.context.getString(R.string.app_name);
        msg.description = "我在慧大夫瘦身发了条动态，快来和我一起做健身达人！";
//        Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        try {
            msg.thumbData = BitmapUtils.readStream(R.mipmap.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        //构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        req.scene = mTargetScene;
//        req.userOpenId = getOpenId();

        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "huidaifu_wechat_share_comment";
        req.message = msg;
        if(type == 1) req.scene = SendMessageToWX.Req.WXSceneSession  ;   //分享到聊天
        else req.scene = SendMessageToWX.Req.WXSceneTimeline ;   //分享到朋友圈

//        req.userOpenId = getOpenId();
//
//        //调用api接口，发送数据到微信
        return msgApi.sendReq(req);
//        mHandler.post(new Runnable() {
//            @Override
//            public void run()
//            {
////                setLoading(true);
////                SendAuth.Req req = new SendAuth.Req();
////                req.scope = "snsapi_userinfo";
////                req.state = "huidaifu_demo_test";
//                msgApi.sendReq(req);
//            }
//        });
    }
}
