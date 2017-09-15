package com.qpg.paylib;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信支付请求
 * 
 * @author Administrator
 *
 */
public class WechatPayReq{
	
	private static final String TAG = WechatPayReq.class.getSimpleName();

	private Activity mActivity;

	//微信支付AppID
	private String appId;
	//微信支付商户号
	private String partnerId;
	//预支付码（重要）
	private String prepayId;
	//"Sign=WXPay"
	private String packageValue;
	private String nonceStr;
	//时间戳
	private String timeStamp;
	//签名
	private String sign;
	
	//微信支付核心api
    IWXAPI mWXApi;
	
	public WechatPayReq() {
		super();
	}


	/**
	 * 发送微信支付请求
	 */
	public void send() {
        mWXApi = WXAPIFactory.createWXAPI(mActivity, null);
        mWXApi.registerApp(this.appId);
        
        PayReq request = new PayReq();

        request.appId = this.appId;
        request.partnerId = this.partnerId;
        request.prepayId= this.prepayId;
        request.packageValue = this.packageValue != null ? this.packageValue : "Sign=WXPay";
        request.nonceStr= this.nonceStr;
        request.timeStamp= this.timeStamp;
        request.sign = this.sign;
        
        mWXApi.sendReq(request);
	}
	
	public static class Builder{
		//上下文
		private Activity activity;
		//微信支付AppID
		private String appId;
		//微信支付商户号
		private String partnerId;
		//预支付码（重要）
		private String prepayId;
		//"Sign=WXPay"
		private String packageValue="Sign=WXPay";
		private String nonceStr;
		//时间戳
		private String timeStamp;
		//签名
		private String sign;
		public Builder() {
			super();
		}
		
		public Builder with(Activity activity){
			this.activity = activity;
			return this;
		}
		
		/**
		 * 设置微信支付AppID
		 * @param appId
		 * @return
		 */
		public Builder setAppId(String appId){
			this.appId = appId;
			return this;
		}
		
		/**
		 * 微信支付商户号
		 * @param partnerId
		 * @return
		 */
		public Builder setPartnerId(String partnerId){
			this.partnerId = partnerId;
			return this;
		}
		
		/**
		 * 设置预支付码（重要）
		 * @param prepayId
		 * @return
		 */
		public Builder setPrepayId(String prepayId){
			this.prepayId = prepayId;
			return this;
		}
		
		
		/**
		 * 设置
		 * @param packageValue
		 * @return
		 */
		public Builder setPackageValue(String packageValue){
			this.packageValue = packageValue;
			return this;
		}
		
		
		/**
		 * 随机字符串
		 * @param nonceStr
		 * @return
		 */
		public Builder setNonceStr(String nonceStr){
			this.nonceStr = nonceStr;
			return this;
		}
		
		/**
		 * 设置时间戳
		 * @param timeStamp
		 * @return
		 */
		public Builder setTimeStamp(String timeStamp){
			this.timeStamp = timeStamp;
			return this;
		}
		
		/**
		 * 设置签名
		 * @param sign
		 * @return
		 */
		public Builder setSign(String sign){
			this.sign = sign;
			return this;
		}
		
		
		
		public WechatPayReq create(){
			WechatPayReq wechatPayReq = new WechatPayReq();
			
			wechatPayReq.mActivity = this.activity;
			//微信支付AppID
			wechatPayReq.appId = this.appId;
			//微信支付商户号
			wechatPayReq.partnerId = this.partnerId;
			//预支付码（重要）
			wechatPayReq.prepayId = this.prepayId;
			//"Sign=WXPay"
			wechatPayReq.packageValue = this.packageValue;
			//随机字符串
			wechatPayReq.nonceStr = this.nonceStr;
			//时间戳
			wechatPayReq.timeStamp = this.timeStamp;
			//签名
			wechatPayReq.sign = this.sign;
			
			return wechatPayReq;
		}
		
	}

}
