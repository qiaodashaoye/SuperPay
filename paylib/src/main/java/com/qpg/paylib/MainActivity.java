package com.qpg.paylib;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 微信支付Test
     */
    public void testWechatPay(){
        String appid        = "";
        String partnerid    = "";
        String prepayid     = "";
        String noncestr     = "";
        String timestamp    = "";
        String sign         = "";
        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                .with(this) //activity实例
                .setAppId(appid) //微信支付AppID
                .setPartnerId(partnerid)//微信支付商户号
                .setPrepayId(prepayid)//预支付码
//								.setPackageValue(wechatPayReq.get)//"Sign=WXPay"
                .setNonceStr(noncestr)
                .setTimeStamp(timestamp)//时间戳
                .setSign(sign)//签名
                .create();

        PayAPI.getInstance().sendPayRequest(wechatPayReq);
//								.setOnWechatPayListener(new OnWechatPayListener() {
//
//									@Override
//									public void onPaySuccess(int errorCode) {
//										ToastUtil.show(mContext, "支付成功" + errorCode);
//
//									}
//
//									@Override
//									public void onPayFailure(int errorCode) {
//										ToastUtil.show(mContext, "支付失败" + errorCode);
//
//									}
//								});
//        WechatPayAPI.getInstance().sendPayReq(wechatPayReq);

        PayAPI.getInstance().sendPayRequest(wechatPayReq);

    }


    /**
     * 支付宝支付测试
     */
    public void testAliPay(){
        String rsa_private      = "";
        String rsa2_private      = "";
        String rsa_public       = "";
        String appid          = "";

        Activity activity       = this;
        String outTradeNo       = "";
        String price            = "";
        String orderSubject     = "";
        String orderBody        = "";
        String callbackUrl      = "";

        /** 商户私钥，pkcs8格式 */
        /** 如下私钥，setRsaPrivate 或者 setRsa2Private 只需要填入一个 */
        /** 如果商户两个都设置了，优先使用 setRsa2Private */
        /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 setRsa2Private */
        /** 获取 setRsa2Private，建议使用支付宝提供的公私钥生成工具生成， */
        /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
        AliPayAPI.Config config = new AliPayAPI.Config.Builder()
                .setRsaPrivate(rsa_private) //设置私钥
                .setRsa2Private(rsa2_private) //设置私钥
                .setRsaPublic(rsa_public)//设置公钥
                .setAppid(appid)//设置appid
                .create();

        AliPayReq aliPayReq = new AliPayReq.Builder()
                .with(activity)//Activity实例
                .apply(config)//支付宝支付通用配置
                .setOutTradeNo(outTradeNo)//设置唯一订单号
                .setPrice(price)//设置订单价格
                .setSubject(orderSubject)//设置订单标题
                .setBody(orderBody)//设置订单内容 订单详情
                .setCallbackUrl(callbackUrl)//设置回调地址
                .create();//
        AliPayAPI.getInstance().apply(config).sendPayReq(aliPayReq);

        PayAPI.getInstance().sendPayRequest(aliPayReq);
    }

    /**
     * 安全的支付宝支付测试
     */
    public void testAliPaySafely(){
        String appid           = "";

        Activity activity       = this;
        String outTradeNo       = "";
        String price            = "";
        String orderSubject     = "";
        String orderBody        = "";
        String callbackUrl      = "";


        String rawAliOrderInfo = new AliPayReq2.AliOrderInfo()
                                .setAppid(appid)  // appid
                                .setIsRsa2(true)  // 是否为RSA2私钥
                                .setOutTradeNo(outTradeNo) //设置唯一订单号
                                .setSubject(orderSubject) //设置订单标题
                                .setBody(orderBody) //设置订单内容
                                .setPrice(price) //设置订单价格
                                .setCallbackUrl(callbackUrl) //设置回调链接
                                .createOrderInfo(); //创建支付宝支付订单信息


        //TODO 这里需要从服务器获取用商户私钥签名之后的订单信息
        String signAliOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);

        AliPayReq2 aliPayReq = new AliPayReq2.Builder()
                .with(activity)//Activity实例
                .setRawAliPayOrderInfo(rawAliOrderInfo)//set the ali pay order info
                .setSignedAliPayOrderInfo(signAliOrderInfo) //set the signed ali pay order info
                .create()//
                .setOnAliPayListener(null);//
        AliPayAPI.getInstance().sendPayReq(aliPayReq);

        PayAPI.getInstance().sendPayRequest(aliPayReq);
    }

    /**
     * 获取签名后的支付宝订单信息  (用商户私钥RSA加密之后的订单信息)
     * @param rawAliOrderInfo
     * @return
     */
    private String getSignAliOrderInfoFromServer(String rawAliOrderInfo) {
        return null;
    }

}
