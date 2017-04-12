 package com.yq.weixin.pay.action;
 
 import com.yq.entity.Order;
import com.yq.weixin.util.SignUtil;
import com.yq.weixin.pay.util.CommonUtil;
import com.yq.weixin.pay.util.GetWxOrderno;
import com.yq.weixin.pay.util.RequestHandler;
import com.yq.weixin.pay.util.Sha1Util;
import com.yq.weixin.pay.util.TenpayUtil;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
 
 public class TopayServlet
 {
   public static String getPackage(List<Order> list, HttpServletRequest request, HttpServletResponse response, HttpSession session)
   {
	 String url = (request.getRequestURL() + "?" + request.getQueryString()).toString();
     String goods_name = ((Order)list.get(0)).getGoods_name();
     if (goods_name.contains("-=")) {
       goods_name = goods_name.replace("-=", "");
     }
     String body = goods_name;
     String orderNo = String.valueOf(((Order)list.get(0)).getOrder_id());
     float money = ((Order)list.get(0)).getGoods_total().floatValue();
     System.out.println("money==" + money);
 
     int price = (int)(money * 100.0F);
     String totalFee = String.valueOf(price);
     System.out.println("totalFee==" + totalFee);
 
     String appid = "wxc1c62269461a335d";
     String appsecret = "af656d37f16486eb5536c05e5c637f68";
     String partner = "1306697601";
     String partnerkey = "yqkj1100yqkj1100yqkj1100yqkj1100";
     String openId = (String)session.getAttribute("oppen_id");
     String code = (String)session.getAttribute("code");
     String notify_url = "http://gmri.mastergroup.com.cn/xnshop/page/noticeOrder.html";
     String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + 
       appid + "&secret=" + appsecret + "&code=" + code + 
       "&grant_type=authorization_code";
 
     JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
 
     String currTime = TenpayUtil.getCurrTime();
 
     String strTime = currTime.substring(8, currTime.length());
 
     String strRandom = String.valueOf(TenpayUtil.buildRandom(4));
 
     String strReq = strTime + strRandom;
 
     String mch_id = partner;
 
     String device_info = "";
 
     String nonce_str = strReq;
 
     String attach = "";
 
     String out_trade_no = orderNo;
 
     String spbill_create_ip = request
       .getRemoteAddr();
 
     String trade_type = "JSAPI";
     String openid = openId;
 
     SortedMap packageParams = new TreeMap();
     packageParams.put("appid", appid);
     packageParams.put("mch_id", mch_id);
     packageParams.put("nonce_str", nonce_str);
     packageParams.put("body", body);
     packageParams.put("attach", attach);
     packageParams.put("out_trade_no", out_trade_no);
 
     packageParams.put("total_fee", totalFee);
 
     packageParams.put("spbill_create_ip", spbill_create_ip);
     packageParams.put("notify_url", notify_url);
     packageParams.put("trade_type", trade_type);
     packageParams.put("openid", openid);
 
     RequestHandler reqHandler = new RequestHandler(
       request, 
       response);
     reqHandler.init(appid, appsecret, partnerkey);
 
     String sign = reqHandler.createSign(packageParams);
     String xml = "<xml><appid>" + appid + "</appid>" + "<mch_id>" + 
       mch_id + "</mch_id>" + "<nonce_str>" + nonce_str + 
       "</nonce_str>" + "<sign>" + sign + "</sign>" + 
       "<body><![CDATA[" + body + "]]></body>" + "<attach>" + attach + 
       "</attach>" + 
       "<out_trade_no>" + 
       out_trade_no + 
       "</out_trade_no>" + 
       "<total_fee>" + 
       totalFee + 
       "</total_fee>" + 
       "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + 
       "<notify_url>" + notify_url + "</notify_url>" + 
       "<trade_type>" + trade_type + "</trade_type>" + "<openid>" + 
       openid + "</openid>" + "</xml>";
 
     String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
     String prepay_id = "";
     try {
       new GetWxOrderno(); 
       prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);
       if (prepay_id.equals("")) {
         request.setAttribute("ErrorMsg", 
           "统一支付接口获取预支付订单出错");
       }
     }
     catch (Exception e1)
     {
       e1.printStackTrace();
     }
     SortedMap finalpackage = new TreeMap();
     String appid2 = appid;
     String timestamp = Sha1Util.getTimeStamp();
     String nonceStr2 = nonce_str;
     String prepay_id2 = "prepay_id=" + prepay_id;
     String packages = prepay_id2;
     finalpackage.put("appId", appid2);
     finalpackage.put("timeStamp", timestamp);
     finalpackage.put("nonceStr", nonceStr2);
     finalpackage.put("package", packages);
     finalpackage.put("signType", "MD5");
 
     String finalsign = reqHandler.createSign(finalpackage);
 
     String packageStr = "\"appid\":\"" + appid2 + "\"," + 
       "\"timeStamp\":\"" + timestamp + "\"," + "\"nonceStr\":\"" + 
       nonceStr2 + "\"," + "\"package\":\"" + packages + "\"," + 
       "\"signType\":\"" + "MD5\"" + "," + "\"paySign\":\"" + 
       finalsign + "\"";
   //获取微信签名数据
     Map<String, String> configStr = SignUtil.getSign(url);
     System.out.println("--------------configStr-----"+ configStr.toString());
     request.setAttribute("timeStampConfig", configStr.get("timestamp"));
     request.setAttribute("nonceStrConfig", configStr.get("nonceStr"));
     request.setAttribute("signatureConfig", configStr.get("signature"));
     
     request.setAttribute("appId", appid2);
     request.setAttribute("timeStamp", timestamp);
     request.setAttribute("nonceStr", nonceStr2);
     request.setAttribute("package", packages);
     request.setAttribute("signType", "MD5");
     request.setAttribute("paySign", finalsign);
     return packageStr;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.pay.action.TopayServlet
 * JD-Core Version:    0.6.2
 */