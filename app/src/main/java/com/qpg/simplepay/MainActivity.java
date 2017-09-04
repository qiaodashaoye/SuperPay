package com.qpg.simplepay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qpg.paylib.AliPayAPI;
import com.qpg.paylib.AliPayReq;
import com.qpg.paylib.PayAPI;
import com.qpg.paylib.PayConstants;
import com.qpg.paylib.WechatPayReq;
import com.qpg.paylib.wxutils.WXPayUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView payAli,PayWeChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payAli=(TextView)findViewById(R.id.pay_ali);
        PayWeChat=(TextView)findViewById(R.id.pay_wechat);
        payAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // step 1 create config for ali pay
                AliPayAPI.Config config = new AliPayAPI.Config.Builder()
                        .setRsaPrivate(PayConstants.RSA_PRIVATE) // 商户私钥，pkcs8格式
                        .setRsaPublic(PayConstants.RSA_PUBLIC)//支付宝公钥
                        .setPartner(PayConstants.PARTNER) //商户PID
                        .setSeller(PayConstants.SELLER) //商户收款账号
                        .create();

                //step 2 create reqeust for ali
                AliPayReq aliPayReq = new AliPayReq.Builder()
                        .with(MainActivity.this)//Activity instance
                        .apply(config)// the above custome config
                        .setOutTradeNo("no12335357745")//set unique trade no
                        .setPrice("0.01")//set price
                        .setSubject("测试")//set order subject
                        .setBody("测试")//set order detail
                        .setCallbackUrl("www.baidu.com")//set callback for pay reqest
                        .create()//
                        .setOnAliPayListener(null);//

                //step 3 send the request for ali pay
                PayAPI.getInstance().sendPayRequest(aliPayReq);
            }
        });

        PayWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> map=new HashMap<String, Object>();
                map.put("WXPay_APPID","123131231");
                map.put("WXPay_mch_id","123131231");
                map.put("orderNo","123131231");
                map.put("orderMoney",1);
                map.put("notify_url","www.baidu.com");
                map.put("body","商品描述");

                new WXPayUtils().init(MainActivity.this,map)
                        .setListener(new WXPayUtils.BackResult() {
                            @Override
                            public void getInfo(String prepayId, String sign) {
                                WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                                        .with(MainActivity.this) //activity instance
                                        .setAppId(PayConstants.APP_ID) //wechat pay AppID
                                        .setPartnerId(PayConstants.MCH_ID)//wechat pay partner id
                                        .setPrepayId(prepayId)//pre pay id
                                        .setNonceStr("")
                                        .setTimeStamp("")//time stamp
                                        .setSign(sign)//sign
                                        .create();
                                //2. send the request with wechat pay
                                PayAPI.getInstance().sendPayRequest(wechatPayReq);
                            }
                        });

            }
        });
    }


}
