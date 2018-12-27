package com.qpg.simplepay;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.qpg.paylib.AliPayReq2;
import com.qpg.paylib.PayAPI;
import com.qpg.simplepay.temp.BaseModel;
import com.qpg.simplepay.temp.OrderNoInfo;
import com.qpg.simplepay.temp.PayOrderInfo;
import com.qpg.superhttp.SuperHttp;
import com.qpg.superhttp.callback.SimpleCallBack;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView payAli,PayWeChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SuperHttp.init(this.getApplication());
        SuperHttp.config()
                //配置请求主机地址
                .setBaseUrl("http/");
        payAli=(TextView)findViewById(R.id.pay_ali);
        PayWeChat=(TextView)findViewById(R.id.pay_wechat);
        payAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HashMap<String,String> map=new HashMap<>();
                map.put("token","53cb3f8556350d95eccc4f54be4fca11");
                map.put("worker_id","32");
                map.put("service_list","[{\"num\":\"1\",\"service_id\":\"10\"}]");
                map.put("main_price","28.60");
                map.put("total_price","28.60");
                map.put("interval_time","1");
                map.put("sundry_price","0");
                map.put("sundry_remark","");
                map.put("address","中国浙江省杭州市西湖区古翠路80号");
                map.put("starttime","2018-07-06");
                map.put("endtime","2018-07-06");
                map.put("owner_name","qwe");
                map.put("mobile","15000000000");
                map.put("order_type","1");
                Set<String> set = map.keySet();
                for (String s : set) {
                    Log.e("HMin66", s + "---" + map.get(s));
                }
                SuperHttp.post("MakeOrder_createOrder")
                        .addParams(map)
                        .request(new SimpleCallBack<BaseModel<OrderNoInfo>>() {
                            @Override
                            public void onSuccess(BaseModel<OrderNoInfo> data) {
                                if(data.getRe().equals("1")){

                                    SuperHttp.post("MakeOrder_payOrder")
                                            .addParam("token","53cb3f8556350d95eccc4f54be4fca11")
                                            .addParam("order_no",data.getData().getOrder_no())
                                            .addParam("pay_way","1")
                                            .request(new SimpleCallBack<BaseModel<PayOrderInfo>>() {
                                                @Override
                                                public void onSuccess(BaseModel<PayOrderInfo> data) {
                                                    if(data.getRe().equals("1")){
                                                        final String info=data.getData().getAli_result();
//                                                        Runnable payRunnable = new Runnable() {
//
//                                                            @Override
//                                                            public void run() {
//                                                                PayTask alipay = new PayTask(MainActivity.this);
//                                                                Map<String, String> result=alipay.payV2(info,true);
////                                                                Message msg = new Message();
////                                                                msg.what = SDK_AUTH_FLAG;
////                                                                msg.obj = result;
////                                                                mHandler.sendMessage(msg);
//                                                            }
//                                                        };
//                                                        // 必须异步调用
//                                                        Thread payThread = new Thread(payRunnable);
//                                                        payThread.start();
                                                        AliPayReq2 aliPayReq = new AliPayReq2.Builder()
                                                                .with(MainActivity.this)//Activity实例
                                                                .setSignedAliPayOrderInfo(info) //设置 商户私钥RSA加密后的支付宝支付订单信息
                                                                .create()//
                                                                .setOnAliPayListener(null);//
                                                        PayAPI.getInstance().sendPayRequest(aliPayReq);

                                                        //关于支付宝支付的回调
                                                        //aliPayReq.setOnAliPayListener(new OnAliPayListener);

                                                    }else {
                                                    }
                                                }

                                                @Override
                                                public void onFail(int errCode, String errMsg) {

                                                }
                                            });
                                }else {
                                }
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {

                            }
                        });

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

            }
        });
    }


}
