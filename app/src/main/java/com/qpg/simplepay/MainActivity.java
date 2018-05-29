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
                /** 商户私钥，pkcs8格式 */
                /** 如下私钥，setRsaPrivate 或者 setRsa2Private 只需要填入一个 */
                /** 如果商户两个都设置了，优先使用 setRsa2Private */
                /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 setRsa2Private */
                /** 获取 setRsa2Private，建议使用支付宝提供的公私钥生成工具生成， */
                /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
                AliPayAPI.Config config = new AliPayAPI.Config.Builder()
                        .setRsaPrivate("") //设置RSA私钥
                        .setRsa2Private(Contant.RSA_PRIVATE) //设置RSA2私钥
                        .setRsaPublic(Contant.RSA_PUBLIC)//设置公钥
                        .setAppid(Contant.APPID)//设置appid
                        .create();

                AliPayReq aliPayReq = new AliPayReq.Builder()
                        .with(MainActivity.this)//Activity实例
                        .apply(config)//支付宝支付通用配置
                        .setOutTradeNo(System.currentTimeMillis()+"")//设置唯一订单号
                        .setPrice("0.1")//设置订单价格
                        .setSubject("充值")//设置订单标题
                        .setBody("31323")//设置订单内容 订单详情
                        .setCallbackUrl("www.baidu.com")//设置回调地址
                        .create();//
                AliPayAPI.getInstance().apply(config).sendPayReq(aliPayReq);

            }
        });

        PayWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> map=new HashMap<String, Object>();
                map.put("wx_appid",Contant.APP_ID);
                map.put("wx_mch_id",Contant.MCH_ID);
                map.put("wx_key",Contant.API_KEY);

                map.put("orderNo","");
                //   map.put("orderMoney",confirmMoney*100);
                map.put("orderMoney",1);
                map.put("notify_url","www.baidu.com");
                map.put("body","商品描述");

                new WXPayUtils().init(MainActivity.this,map)
                        .setListener(new WXPayUtils.BackResult() {
                            @Override
                            public void getInfo(String prepayId, String sign) {
                                WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                                        .with(MainActivity.this) //activity instance
                                        .setAppId(Contant.APP_ID) //wechat pay AppID
                                        .setPartnerId(Contant.MCH_ID)//wechat pay partner id
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
