/*
 * 技术支持QQ: 320346009
 */

package com.qpg.simplepay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	private String APP_ID = "111111122222222"; //这里需要替换你的APP_ID

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, APP_ID);
		api.handleIntent(getIntent(), this);
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
		//        0	成功	展示成功页面
//        -1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//        -2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。

	}
}
