package com.qpg.simplepay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qpg.paylib.AliPayAPI;
import com.qpg.paylib.AliPayReq;
import com.qpg.paylib.PayAPI;
import com.qpg.paylib.PayConstants;

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
                        .setRsaPrivate(PayConstants.PRIVATEKEY) // rsa private key from partner (pkcs8 format)
                        .setRsaPublic(PayConstants.PRIVATEKEY)//ali rsa public key
                        .setPartner(PayConstants.Partner) //set partner
                        .setSeller(PayConstants.Seller) //set partner seller accout
                        .create();

                //step 2 create reqeust for ali
                AliPayReq aliPayReq = new AliPayReq.Builder()
                        .with(MainActivity.this)//Activity instance
                        .apply(config)// the above custome config
                        .setOutTradeNo("no12335357745")//set unique trade no
                        .setPrice("0.01")//set price
                        .setSubject("蛋")//set order subject
                        .setBody("鸡蛋")//set order detail
                        .setCallbackUrl("www.baidu.com")//set callback for pay reqest
                        .create()//
                        .setOnAliPayListener(null);//

                //step 3 send the request for ali pay
                PayAPI.getInstance().sendPayRequest(aliPayReq);
            }
        });
    }
}
