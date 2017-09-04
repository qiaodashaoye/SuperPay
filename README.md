# SimplePay
集成微信和支付宝支付，微信支付遇到的坑，我来帮你填，App内嵌支付如此简单。
## 使用

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


        //微信支付结果的回调，如果不用可以不添加
        //wechatPayReq.setOnWechatPayListener(new OnWechatPayListener);


```

##方案二：统一下单接口的调取在移动端

```java

     //字段名不能改，改了会报错
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

>注意：这里没有金额设置，金额的信息已经包含在预支付码prepayid了。

### 支付宝支付使用

#### 支付宝支付第一种方式(不建议用这种方式，商户私钥暴露在客户端，极其危险，推荐用第二种支付方式)
```java

        //1.创建支付宝支付配置
        AliPayAPI.Config config = new AliPayAPI.Config.Builder()
                .setRsaPrivate(rsa_private) //设置私钥 (商户私钥，pkcs8格式)
                .setRsaPublic(rsa_public)//设置公钥(// 支付宝公钥)
                .setPartner(partner) //设置商户
                .setSeller(seller) //设置商户收款账号
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
                .setPartner(partner) //商户PID || 签约合作者身份ID
                .setSeller(seller)  // 商户收款账号 || 签约卖家支付宝账号
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

## 文档

### 微信支付官方文档 支付流程
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5

### 支付宝支付官方文档 支付流程
https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.sdGXaH&treeId=204&articleId=105296&docType=1



## 注意

### 微信支付

 - 微信支付必须要在**正式签名**和**正确包名**的应用中才能成功调起。(**重点)

    即商户在微信开放平台申请开发应用后对应包名和对应签名的应用才能成功调起。
    详情请参考微信支付的开发流程文档。

    https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5

 - 微信支付API没有在客户端显示的设置回调，回调是在Server端设置的。(与支付宝支付的区别，支付宝的回调是在客户端设置的)

### 支付宝支付

 - 支付宝支付为了保证交易双方的身份和数据安全， 需要配置双方密钥。
    详情请参考支付宝支付的密钥处理体系文档。
    https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.1wPnBT&treeId=204&articleId=106079&docType=1
