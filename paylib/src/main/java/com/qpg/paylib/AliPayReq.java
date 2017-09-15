package com.qpg.paylib;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.qpg.paylib.alipay.OrderInfoUtil2_0;
import com.qpg.paylib.alipay.PayResult;
import com.qpg.paylib.alipay.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付宝支付请求
 *
 */
public class AliPayReq {

	/**
	 * ali pay sdk flag
	 */
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	private Activity mActivity;
	
	//支付宝支付的配置
	private AliPayAPI.Config mConfig;
	
	// 商户网站唯一订单号
	private String outTradeNo;
	// 商品名称
	private String subject;
	// 商品详情
	private String body;
	// 商品金额
	private String price;
	// 服务器异步通知页面路径
	private String callbackUrl;
	
	private Handler mHandler;

	public AliPayReq() {
		super();




		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SDK_PAY_FLAG: {
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
						if(mOnAliPayListener != null) mOnAliPayListener.onPaySuccess(resultInfo);
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
							if(mOnAliPayListener != null) mOnAliPayListener.onPayConfirmimg(resultInfo);

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
							if(mOnAliPayListener != null) mOnAliPayListener.onPayFailure(resultInfo);
						}
					}
					break;

				}
				case SDK_CHECK_FLAG: {
					Toast.makeText(mActivity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
					if(mOnAliPayListener != null) mOnAliPayListener.onPayCheck(msg.obj.toString());
					break;
				}
				default:
					break;
				}
			}
			
		};
	}


	/**
	 * 发送支付宝支付请求
	 * @param config  支付宝配置
	 */
	public void sendWithConfig(AliPayAPI.Config config) {
		this.mConfig = config;
		send();
	}


	/**
	 * 发送支付宝支付请求
	 */
	public void send() {

		boolean rsa2 = (this.mConfig.getAliRsa2Private().length() > 0);
		HashMap<String,Object> map=new HashMap<>();
		map.put("app_id",this.mConfig.getAppid());
		map.put("notify_url",this.callbackUrl);
		map.put("out_trade_no",this.outTradeNo);
		map.put("total_amount",this.price);
		map.put("body",this.body);
		map.put("subject",this.subject);

		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(map, rsa2);
		// 创建订单信息
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? this.mConfig.getAliRsa2Private() : this.mConfig.getAliRsaPrivate();
		// 对订单做RSA或RSA2 签名
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderParam + "&" + sign;


		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(mActivity);
				Map<String, String> result = alipay.payV2(payInfo, true);
				Log.i("msp", result.toString());

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	public static class Builder{
		//上下文
		private Activity activity;
		//支付宝支付配置
		private AliPayAPI.Config config;
		// 商户网站唯一订单号
		private String outTradeNo;
		// 商品名称
		private String subject;
		// 商品详情
		private String body;
		// 商品金额
		private String price;
		// 服务器异步通知页面路径
		private String callbackUrl;
		public Builder() {
			super();
		}
		
		public Builder with(Activity activity){
			this.activity = activity;
			return this;
		}
		
		public Builder apply(AliPayAPI.Config config){
			this.config = config;
			return this;
		}
		
		/**
		 * 设置唯一订单号
		 * @param outTradeNo
		 * @return
		 */
		public Builder setOutTradeNo(String outTradeNo){
			this.outTradeNo = outTradeNo;
			return this;
		}
		
		/**
		 * 设置订单标题
		 * @param subject
		 * @return
		 */
		public Builder setSubject(String subject){
			this.subject = subject;
			return this;
		}
		
		/**
		 * 设置订单内容
		 * @param body
		 * @return
		 */
		public Builder setBody(String body){
			this.body = body;
			return this;
		}
		
		/**
		 * 设置订单价格
		 * @param price
		 * @return
		 */
		public Builder setPrice(String price){
			this.price = price;
			return this;
		}
		
		/**
		 * 设置回调
		 * @param callbackUrl
		 * @return
		 */
		public Builder setCallbackUrl(String callbackUrl){
			this.callbackUrl = callbackUrl;
			return this;
		}
		
		public AliPayReq create(){
			AliPayReq aliPayReq = new AliPayReq();
			aliPayReq.mActivity = this.activity;
			aliPayReq.mConfig = this.config;
			aliPayReq.outTradeNo = this.outTradeNo;
			aliPayReq.subject = this.subject;
			aliPayReq.body = this.body;
			aliPayReq.price = this.price;
			aliPayReq.callbackUrl = this.callbackUrl;
			
			return aliPayReq;
		}
		
	}
	
	
	//支付宝支付监听
	private OnAliPayListener mOnAliPayListener;
	public AliPayReq setOnAliPayListener(OnAliPayListener onAliPayListener) {
		this.mOnAliPayListener = onAliPayListener;
		return this;
	}

	/**
	 * 支付宝支付监听
	 * @author Administrator
	 *
	 */
	public interface OnAliPayListener{
		public void onPaySuccess(String resultInfo);
		public void onPayFailure(String resultInfo);
		public void onPayConfirmimg(String resultInfo);
		public void onPayCheck(String status);
	}
}
