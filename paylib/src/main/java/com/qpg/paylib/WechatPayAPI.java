package com.qpg.paylib;

/**
 * 微信支付API
 * 
 * 使用:
 * 
 * WechatPayAPI.getInstance().sendPayReq(wechatPayReq);
 */
public class WechatPayAPI {

	/**
	 * 获取微信支付API
	 */
    private static final Object mLock = new Object();
    private static WechatPayAPI mInstance;

    public static WechatPayAPI getInstance(){
        if(mInstance == null){
            synchronized (mLock){
                if(mInstance == null){
                    mInstance = new WechatPayAPI();
                }
            }
        }
        return mInstance;
    }
    
    /**
     * 发送微信支付请求
     * @param wechatPayReq
     */
    public void sendPayReq(WechatPayReq wechatPayReq){
    	wechatPayReq.send();
    }
}
