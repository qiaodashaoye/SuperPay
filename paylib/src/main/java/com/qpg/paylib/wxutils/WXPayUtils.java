package com.qpg.paylib.wxutils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.qpg.paylib.PayConstants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/28.
 */

public class WXPayUtils {
    private GetPrepayIdTask mTask ;
    private IWXAPI api;

    public String signn;

    public String asd;
    private  BackResult backResult;
    //执行线程生成sign
    public WXPayUtils init(Context context){

        api = WXAPIFactory.createWXAPI(context, PayConstants.WXPay_APPID);
        mTask = new GetPrepayIdTask();
        mTask.execute();
        return this;
    }

    public WXPayUtils setListener(BackResult backResult){
        this.backResult=backResult;
        return this;
    }
    /*
     * 异步网络请求获取预付Id
     */
    private class GetPrepayIdTask extends AsyncTask<String,String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
            // 第三步, 发送支付请求
            sendPayReq(result);
        }

        @Override
        protected String doInBackground(String... params) {
            // 网络请求获取预付Id
            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genEntity();
            byte[] buf = Util.httpPost(url, entity);
            if (buf != null && buf.length > 0) {
                try {
                    Map<String,String> map =doXMLParse(new String(buf));
                    asd=map.get("prepay_id");
                    backResult.getInfo(map.get("prepay_id"),signn);
                    return map.get("prepay_id");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    /*
     * 微信支付，构建统一下单请求参数
     */
    public String genEntity() {
        String nonceStr = genNonceStr();
        List<NameValuePair> packageParams = new ArrayList<NameValuePair>();
        // APPID
        packageParams
                .add(new BasicNameValuePair("appid", PayConstants.WXPay_APPID));
        // 商品描述
        packageParams.add(new BasicNameValuePair("body","1231"));
        // 商户ID
        packageParams.add(new BasicNameValuePair("mch_id",PayConstants.WXPay_mch_id));
        // 随机字符串
        packageParams.add(new BasicNameValuePair("nonce_str",nonceStr));
        // 回调接口地址
        packageParams.add(new BasicNameValuePair("notify_url","www.baidu.com"));
        // 我们的订单号
        packageParams.add(new BasicNameValuePair("out_trade_no","no2017062800346232"));
        // 提交用户端ip
        packageParams.add(new BasicNameValuePair("spbill_create_ip",getLocalHostIp()));
        BigDecimal totalFeeBig = new BigDecimal("100");
        int totalFee = totalFeeBig.multiply(new BigDecimal(1)).intValue();//订单金额
        // 总金额 !
        packageParams.add(new BasicNameValuePair("total_fee",String.valueOf(totalFee)));
        // 支付类型， APP
        packageParams.add(new BasicNameValuePair("trade_type","APP"));
        // 生成签名
        String sign = genPackageSign(packageParams);
        packageParams.add(new BasicNameValuePair("sign", sign));
        String xmlstring = toXml(packageParams);
        try {
            //避免商品描述中文字符编码格式造成支付失败
            return new String(xmlstring.toString().getBytes(), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 生成签名
     */
    public String genPackageSign(List<NameValuePair> params) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                sb.append(params.get(i).getName());
                sb.append('=');
                sb.append(params.get(i).getValue());
                sb.append('&');
            }
            sb.append("key=");
            sb.append(PayConstants.WXPay_key);

            String packageSign = MD5.getMessageDigest(
                    sb.toString().getBytes("utf-8")).toUpperCase();
            signn=packageSign;
            return packageSign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*

      * 微信支付调用统一下单接口，随机字符串
     */
    public static String genNonceStr() {
        try {
            Random random = new Random();
            String rStr = MD5.getMessageDigest(String.valueOf(
                    random.nextInt(10000)).getBytes("utf-8"));
            return rStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
     * 生成 XML
     */
    public static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");

            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");

        Log.e("orion","2----"+sb.toString());
        return sb.toString();
    }

    /* 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
            *
            * @param strxml
     * @return
             * @throws JDOMException
     * @throws IOException
     */
    public static Map<String,String> doXMLParse(String strxml) throws Exception {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(strxml));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            //实例化student对象
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
        }
        return null;
    }
    /*
     *发送支付请求
     * @param prepayId 预付Id
     */
    private  void sendPayReq(String prepayId) {
        PayReq req = new PayReq();
        req.appId = PayConstants.WXPay_APPID;
        req.partnerId = PayConstants.WXPay_mch_id;
        req.prepayId = prepayId;
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        req.packageValue = "Sign=WXPay";

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        //再次签名
        req.sign = genPackageSign(signParams);
        // 传递的额外信息,字符串信息,自定义格式
        // req.extData = type +"#" + out_trade_no + "#" +total_fee;
        // 微信支付结果界面对调起支付Activity的处理
        // APPCache.payActivity.put("调起支付的Activity",(调起支付的Activity)context);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.registerApp(PayConstants.WXPay_APPID);
        api.sendReq(req);
        // 支付完成后微信会回调 wxapi包下 WXPayEntryActivity 的public void onResp(BaseResp
        // resp)方法，所以后续操作，放在这个回调函数中操作就可以了
    }

    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    // 得到本机ip地址
    public static String getLocalHostIp(){
        String ipaddress = "";
        try
        {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements())
            {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements())
                {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress()))
                    {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        }
        catch (SocketException e)
        {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

   public interface BackResult{
        void getInfo(String prepayId, String sign);
    }
}
