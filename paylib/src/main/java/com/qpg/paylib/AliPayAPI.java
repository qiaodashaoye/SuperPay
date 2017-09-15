package com.qpg.paylib;

/**
 * 支付宝支付API
 * 
 * 使用:
 * 
 * AliPayAPI.getInstance().apply(config).sendPayReq(aliPayReq);
 *
 */
public class AliPayAPI {
	
	private Config mConfig;

	/**
	 * 获取支付宝支付API
	 */
    private static final Object mLock = new Object();
    private static AliPayAPI mInstance;

    public static AliPayAPI getInstance(){
        if(mInstance == null){
            synchronized (mLock){
                if(mInstance == null){
                    mInstance = new AliPayAPI();
                }
            }
        }
        return mInstance;
    }
	
    
    /**
     * 配置支付宝配置
     * @param config
     * @return
     */
    public AliPayAPI apply(Config config){
    	this.mConfig = config;
    	return this;
    }

	/**
	 * 发送支付宝支付请求
	 * @param aliPayReq
	 */
    public void sendPayReq(AliPayReq aliPayReq){
    	aliPayReq.send();
    }


	/**
	 * 发送支付宝支付请求
	 * @param aliPayReq2
	 */
	public void sendPayReq(AliPayReq2 aliPayReq2){
		aliPayReq2.send();
	}
    
    
    /**
     * 支付宝支付配置
     * @author Administrator
     *
     */
    public static class Config{
        //ali pay config
        // 商户私钥，pkcs8格式
        private String aliRsaPrivate;
        private String aliRsa2Private;
        private String appid;
        // 支付宝公钥
        private String aliRsaPublic;
        // 商户PID
        // 签约合作者身份ID
        private String partner;
        // 商户收款账号
        // 签约卖家支付宝账号
        private String seller;
        
        public String getAliRsaPrivate() {
			return aliRsaPrivate;
		}
        public String getAliRsa2Private() {
			return aliRsa2Private;
		}
		public String getAppid() {
			return appid;
		}

		public void setAliRsaPrivate(String aliRsaPrivate) {
			this.aliRsaPrivate = aliRsaPrivate;
		}

		public void setAliRsa2Private(String aliRsa2Private) {
			this.aliRsa2Private = aliRsa2Private;
		}
		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getAliRsaPublic() {
			return aliRsaPublic;
		}

		public void setAliRsaPublic(String aliRsaPublic) {
			this.aliRsaPublic = aliRsaPublic;
		}

		public String getPartner() {
			return partner;
		}

		public void setPartner(String partner) {
			this.partner = partner;
		}

		public String getSeller() {
			return seller;
		}

		public void setSeller(String seller) {
			this.seller = seller;
		}

		public static class Builder{
            //ali pay config
			/** 商户私钥，pkcs8格式 */
			/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
			/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
			/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
			/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
			/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
			private String aliRsa2Private = "";
			private String aliRsaPrivate = "";
			private String appid = "";












            // 支付宝公钥
            private String aliRsaPublic;
            public Builder() {
				super();
			}

			public Builder setRsaPrivate(String aliRsaPrivate){
            	this.aliRsaPrivate = aliRsaPrivate;
            	return this;
            }

			public Builder setRsa2Private(String aliRsa2Private){
				this.aliRsa2Private = aliRsa2Private;
				return this;
			}
			public Builder setAppid(String appid){
				this.appid = appid;
				return this;
			}
			public Builder setRsaPublic(String aliRsaPublic){
            	this.aliRsaPublic = aliRsaPublic;
            	return this;
            }

			public Config create(){
				Config conf = new Config();
				conf.aliRsaPrivate = this.aliRsaPrivate;
				conf.aliRsa2Private = this.aliRsa2Private;
				conf.appid = this.appid;
				conf.aliRsaPublic = this.aliRsaPublic;
				return conf;
			}
        }
    }
}
