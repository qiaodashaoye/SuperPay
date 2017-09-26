# SimplePay

[![](https://jitpack.io/v/qiaodashaoye/SimplePay.svg)](https://jitpack.io/#qiaodashaoye/SimplePay)
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/qpglibs/maven/SimplePay/images/download.svg)](https://bintray.com/qpglibs/maven/SimplePay/_latestVersion) ](https://bintray.com/qpglibs/maven/SimplePay/_latestVersion)
集成微信和支付宝支付，微信支付遇到的坑，我来帮你填，App内嵌支付如此简单。
# 一、集成步骤

> compile 'com.qpg:paylib:1.0.0'

# 二、使用
### 微信支付使用（两种方案）

##方案一：统一下单接口的调取在服务端（推荐）

```java
        //1.创建微信支付请求
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
        //2.发送微信支付请求
        PayAPI.getInstance().sendPayRequest(wechatPayReq);

```
>注意：prepayid(预支付码)一定要正确，里面包含订单的金额，。


##方案二：统一下单接口的调取在移动端

```java
     //字段名不能改，改了会报错
      map.put("wx_appid",PayConstants.APP_ID);
      map.put("wx_mch_id",PayConstants.MCH_ID);
      map.put("wx_key",PayConstants.API_KEY);
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
                            .setAppId(PayConstants.WXPay_APPID) //wechat pay AppID
                            .setPartnerId(PayConstants.WXPay_mch_id)//wechat pay partner id
                            .setPrepayId(prepayId)//预订单号
                            .setNonceStr("")
                            .setTimeStamp("")//时间戳，可为空
                            .setSign(sign)//签名
                            .create();
                    //2. send the request with wechat pay
                    PayAPI.getInstance().sendPayRequest(wechatPayReq);
                }
            });
```
> 关于微信支付完成（失败、成功、取消）后的回调，必须在与包名同级下建立名为
wxapi的文件夹，并在文件夹下建立WXPayEntryActivity类并实现IWXAPIEventHandler接口，
所建的类名必须是WXPayEntryActivity，不然不会执行onResp()回调方法。
### 支付宝支付使用
 > 写在前面：
 ```java
  /** 商户私钥，pkcs8格式 */
  /** 如下私钥，RSA_PRIVATE 或者 RSA2_PRIVATE 只需要填入一个 */
  /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
  /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
  /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
  /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
```
                     
#### 支付宝支付第一种方式(不建议用这种方式，商户私钥暴露在客户端，极其危险，推荐用第二种支付方式)
```java

        //1.创建支付宝支付配置
        AliPayAPI.Config config = new AliPayAPI.Config.Builder()
                  .setRsaPrivate("") //设置RSA私钥
                  .setRsa2Private("") //设置RSA2私钥
                  .setRsaPublic("")//设置公钥
                  .setAppid(PayConstants.Appid)//设置appid
                  .create();

        //2.创建支付宝支付请求
        AliPayReq aliPayReq = new AliPayReq.Builder()
                .with(activity)//Activity实例
                .apply(config)//支付宝支付通用配置
                .setOutTradeNo(outTradeNo)//设置唯一订单号
                .setPrice(price)//设置订单价格
                .setSubject(orderSubject)//设置订单标题
                .setBody(orderBody)//设置订单内容 订单详情
                .setCallbackUrl(callbackUrl)//设置回调地址
                .create()//
                .setOnAliPayListener(null);//

        //3.发送支付宝支付请求
        PayAPI.getInstance().sendPayRequest(aliPayReq);

        //关于支付宝支付的回调
        //aliPayReq.setOnAliPayListener(new OnAliPayListener);

```

#### 支付宝支付第二种方式(**强烈推荐**)

```java
        //1.创建支付宝支付订单的信息
        String rawAliOrderInfo = new AliPayReq2.AliOrderInfo()
                 .setAppid(appid)  // appid
                 .setIsRsa2(true)  // 是否为RSA2私钥
                 .setOutTradeNo(outTradeNo) //设置唯一订单号
                 .setSubject(orderSubject) //设置订单标题
                 .setBody(orderBody) //设置订单内容
                 .setPrice(price) //设置订单价格
                 .setCallbackUrl(callbackUrl) //设置回调链接
                 .createOrderInfo(); //创建支付宝支付订单信息


        //2.签名  支付宝支付订单的信息 ===>>>  商户私钥签名之后的订单信息
        //TODO 这里需要从服务器获取用商户私钥签名之后的订单信息
        String signAliOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);

        //3.发送支付宝支付请求
        AliPayReq2 aliPayReq = new AliPayReq2.Builder()
                .with(activity)//Activity实例
                .setRawAliPayOrderInfo(rawAliOrderInfo)//支付宝支付订单信息
                .setSignedAliPayOrderInfo(signAliOrderInfo) //设置 商户私钥RSA加密后的支付宝支付订单信息
                .create()//
                .setOnAliPayListener(null);//
        PayAPI.getInstance().sendPayRequest(aliPayReq);

        //关于支付宝支付的回调
        //aliPayReq.setOnAliPayListener(new OnAliPayListener);
```

## 帮助

### 微信支付官方文档 支付流程
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5

### 支付宝支付官方文档 支付流程
https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.sdGXaH&treeId=204&articleId=105296&docType=1

### 支付宝支付的密钥处理体系文档。
https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.1wPnBT&treeId=204&articleId=106079&docType=1
