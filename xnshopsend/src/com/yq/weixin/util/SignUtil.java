 package com.yq.weixin.util;
 
 import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.yq.weixin.pojo.Token;
import com.yq.weixin.util.CommonUtil;
import com.yq.weixin.util.StringUtil;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yq.entity.AccessToken;
import com.yq.service.AccessTokenService;
 
 public class SignUtil
 {
   private static String token = "123";
   
   public static boolean checkSignature(String signature, String timestamp, String nonce)
   {
     String[] paramArr = { token, timestamp, nonce };
     Arrays.sort(paramArr);
 
     String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
 
     String ciphertext = null;
     try {
       MessageDigest md = MessageDigest.getInstance("SHA-1");
 
       byte[] digest = md.digest(content.toString().getBytes());
       ciphertext = byteToStr(digest);
     } catch (NoSuchAlgorithmException e) {
       e.printStackTrace();
     }
 
     return ciphertext != null ? ciphertext.equals(signature.toUpperCase()) : false;
   }
   
 
   private static String byteToStr(byte[] byteArray)
   {
     String strDigest = "";
     for (int i = 0; i < byteArray.length; i++) {
       strDigest = strDigest + byteToHexStr(byteArray[i]);
     }
     return strDigest;
   }
 
   private static String byteToHexStr(byte mByte)
   {
     char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
     char[] tempArr = new char[2];
     tempArr[0] = Digit[(mByte >>> 4 & 0xF)];
     tempArr[1] = Digit[(mByte & 0xF)];
 
     String s = new String(tempArr);
     return s;
   }
   
  public static  Map<String, String> getSign(String url){

	  CommonUtil util = new CommonUtil();
	  Token token = new Token();
	  token = util.getToken(StringUtil.appid, StringUtil.appsecret);
	  String jsapi_ticket = token.getJsapiTicket();
	  return sign(jsapi_ticket,url);
  }
  
   
   public static Map<String, String> sign(String jsapi_ticket, String url) {
       Map<String, String> ret = new HashMap<String, String>();
       String nonce_str = create_nonce_str();
       String timestamp = create_timestamp();
       String string1;
       String signature = "";

       //注意这里参数名必须全部小写，且必须有序
       string1 = "jsapi_ticket=" + jsapi_ticket +
                 "&noncestr=" + nonce_str +
                 "&timestamp=" + timestamp +
                 "&url=" + url;
       System.out.println(string1);

       try
       {
           MessageDigest crypt = MessageDigest.getInstance("SHA-1");
           crypt.reset();
           crypt.update(string1.getBytes("UTF-8"));
           signature = byteToHex(crypt.digest());
       }
       catch (NoSuchAlgorithmException e)
       {
           e.printStackTrace();
       }
       catch (UnsupportedEncodingException e)
       {
           e.printStackTrace();
       }

       ret.put("url", url);
       ret.put("jsapi_ticket", jsapi_ticket);
       ret.put("nonceStr", nonce_str);
       ret.put("timestamp", timestamp);
       ret.put("signature", signature);

       return ret;
   }

   private static String byteToHex(final byte[] hash) {
       Formatter formatter = new Formatter();
       for (byte b : hash)
       {
           formatter.format("%02x", b);
       }
       String result = formatter.toString();
       formatter.close();
       return result;
   }

   private static String create_nonce_str() {
       return UUID.randomUUID().toString();
   }

   private static String create_timestamp() {
       return Long.toString(System.currentTimeMillis() / 1000);
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.util.SignUtil
 * JD-Core Version:    0.6.2
 */