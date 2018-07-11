package com.qpg.simplepay.temp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayOrderInfo implements Serializable{
    private String order_no;
    private WXInfo wx_result;
    private String ali_result;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public WXInfo getWx_result() {
        return wx_result;
    }

    public void setWx_result(WXInfo wx_result) {
        this.wx_result = wx_result;
    }

    public String getAli_result() {
        return ali_result;
    }

    public void setAli_result(String ali_result) {
        this.ali_result = ali_result;
    }

    public class WXInfo{
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packages;
        private String partnerid;
        private String prepayid;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
