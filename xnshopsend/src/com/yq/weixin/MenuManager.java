 package com.yq.weixin;
 
 import com.yq.weixin.menu.Button;
import com.yq.weixin.menu.ComplexButton;
import com.yq.weixin.menu.Menu;
import com.yq.weixin.menu.ViewButton;
import com.yq.weixin.pojo.Token;
import com.yq.weixin.util.CommonUtil;
import com.yq.weixin.util.MenuUtil;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 public class MenuManager
 {
   private static Logger log = LoggerFactory.getLogger(MenuManager.class);
   static CommonUtil commonUtil = new CommonUtil();
   public static final String appId = "wxc1c62269461a335d";
   public static final  String appSecret = "af656d37f16486eb5536c05e5c637f68";
   public static final  String baseUrl = "http://gmri.mastergroup.com.cn";
  // public static final  String baseUrl = "http://gmri.mastergroup.com.cn";
   public static final  String Urlmain = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+baseUrl+"/xnshop/page/index.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect";
   public static final  String Urluser = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+baseUrl+"/xnshop/page/center.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect";
   public static final  String UrlcartList = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+baseUrl+"/xnshop/page/cartList.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect";
   public static final  String UrlDevStatus = baseUrl + ":3300/devStatus";
   
   private static Menu getMenu()
   {
     ViewButton btn13 = new ViewButton();
     btn13.setName("Home Page");
     btn13.setType("view");
     btn13.setUrl(Urlmain);
 
//     ViewButton btn14 = new ViewButton();
//     btn14.setName("首页");
//     btn14.setType("view");
//     btn14.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1c62269461a335d&redirect_uri=http://gmri.mastergroup.com.cn/xnshop/page/index.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect");
// 
//     ViewButton btn15 = new ViewButton();
//     btn15.setName("首页");
//     btn15.setType("view");
//     btn15.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1c62269461a335d&redirect_uri=http://gmri.mastergroup.com.cn/xnshop/page/index.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect");
 
     ViewButton btn21 = new ViewButton();
     btn21.setName("My Cart");
     btn21.setType("view");
     btn21.setUrl(UrlcartList);
 
     ViewButton btn31 = new ViewButton();
     btn31.setName("My");
     btn31.setType("view");
     btn31.setUrl(Urluser);
 
     ViewButton btn32 = new ViewButton();
     btn32.setName("My Order");
     btn32.setType("view");
     btn32.setUrl(UrlcartList);
     	
     ViewButton btn33 = new ViewButton();
     btn33.setName("devStatus");
     btn33.setType("view");
     btn33.setUrl(UrlDevStatus);
     
     ComplexButton mainBtn1 = new ComplexButton();
     mainBtn1.setName("My mall");
 
     mainBtn1.setSub_button(new Button[] { btn13 });
 
     ComplexButton mainBtn2 = new ComplexButton();
     mainBtn2.setName("Cart");
     mainBtn2.setSub_button(new Button[] { btn21 });
 
     ComplexButton mainBtn3 = new ComplexButton();
     mainBtn3.setName("Customer Center");
     mainBtn3.setSub_button(new Button[] { btn31, btn32 });
 

     ComplexButton mainBtn4 = new ComplexButton();
     mainBtn3.setName("Customer Center");
     mainBtn3.setSub_button(new Button[] { btn31, btn33 });
 
     
     Menu menu = new Menu();
     menu.setButton(new Button[] { btn13, btn21,  btn33 });
 
     return menu;
   }
 
   public static void main(String[] args)
   {
//     String appId = "wxc1c62269461a335d";
// 
//     String appSecret = "af656d37f16486eb5536c05e5c637f68";
 
	   
     Token token = commonUtil.getToken(appId, appSecret);
 
     if (token != null)
     {
       boolean result = MenuUtil.createMenu(getMenu(), token.getAccessToken());
       if (result) {
         log.info("菜单创建成功！");
         System.out.println("菜单创建成功！");
       } else {
         log.info("菜单创建失败！");
         System.out.println("菜单创建失败！");
       }
     }
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.MenuManager
 * JD-Core Version:    0.6.2
 */