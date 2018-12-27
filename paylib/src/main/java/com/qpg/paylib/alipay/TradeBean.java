package com.qpg.paylib.alipay;

import androidx.annotation.NonNull;

/**
 * @author qpg
 * @date 2018-05-11
 * @description:
 */
public class TradeBean {
    private String body;//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
    @NonNull
    private String subject;//商品的标题/交易标题/订单标题/订单关键字等。
    @NonNull
    private String out_trade_no;//商户网站唯一订单号
    private String timeout_express;//该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。注：若为空，则默认为15d。
    @NonNull
    private String total_amount;//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
    @NonNull
    private String product_code;//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
    private String goods_type;//商品主类型：0—虚拟类商品，1—实物类商品,注：虚拟类商品不支持使用花呗渠道
    private String passback_params;//公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝
    private String promo_params;//优惠参数,注：仅与支付宝协商后可用
    private String extend_params;//业务扩展参数
    private String enable_pay_channels;//可用渠道，用户只能在指定渠道范围内支付,当有多个渠道时用“,”分隔.注：与disable_pay_channels互斥
    private String disable_pay_channels;//禁用渠道，用户不可用指定渠道支付,当有多个渠道时用“,”分隔。注：与enable_pay_channels互斥
    private String store_id;//商户门店编号。该参数用于请求参数中以区分各门店，非必传项。
    private String ext_user_info;//外部指定买家，详见外部用户ExtUserInfo参数说明

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @NonNull
    public String getSubject() {
        return subject;
    }

    public void setSubject(@NonNull String subject) {
        this.subject = subject;
    }

    @NonNull
    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(@NonNull String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    @NonNull
    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(@NonNull String total_amount) {
        this.total_amount = total_amount;
    }

    @NonNull
    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(@NonNull String product_code) {
        this.product_code = product_code;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getPassback_params() {
        return passback_params;
    }

    public void setPassback_params(String passback_params) {
        this.passback_params = passback_params;
    }

    public String getPromo_params() {
        return promo_params;
    }

    public void setPromo_params(String promo_params) {
        this.promo_params = promo_params;
    }

    public String getExtend_params() {
        return extend_params;
    }

    public void setExtend_params(String extend_params) {
        this.extend_params = extend_params;
    }

    public String getEnable_pay_channels() {
        return enable_pay_channels;
    }

    public void setEnable_pay_channels(String enable_pay_channels) {
        this.enable_pay_channels = enable_pay_channels;
    }

    public String getDisable_pay_channels() {
        return disable_pay_channels;
    }

    public void setDisable_pay_channels(String disable_pay_channels) {
        this.disable_pay_channels = disable_pay_channels;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getExt_user_info() {
        return ext_user_info;
    }

    public void setExt_user_info(String ext_user_info) {
        this.ext_user_info = ext_user_info;
    }
}
