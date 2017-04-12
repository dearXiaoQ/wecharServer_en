 package com.yq.util;
 
 import com.google.gson.Gson;
 
 
 
 import java.util.HashMap;
 import java.util.Map;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 import org.springframework.web.servlet.ModelAndView;
 import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
 
 public class LoginInterceptor extends HandlerInterceptorAdapter
 {
   private static final String[] IGNORE_URI = { "/login.jsp", "/Login/", "/main/", "/admin/", "/userInsert.html", 
     "/oauth2/", "/noticeOrder.html" };
 
   private static final String plcRequest = "/plc.html";
   
   Gson gson = new Gson();
   Map<String, Object> map = new HashMap();
 
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
   {
     HttpSession session = request.getSession();
     boolean flag = false;
 
     flag = session.getAttribute("oppen_id") != null;
     
     
     String url = (request.getRequestURL() + "?" + request.getQueryString()).toString();
     System.out.println("here: " );
     System.out.println(">>>: " + url);
     
     if(url.indexOf(plcRequest) > 0) {
    	// System.out.println("plc中转服务器请求！");
    	 return true;
     }
     
     for (String s : IGNORE_URI) {
       if (url.contains(s)) {
         flag = true;
         break;
       }
     }
     if (!flag) {
       if ((request.getHeader("x-requested-with") != null) && 
         (request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest"))) {
         this.map.put("rs_code", Integer.valueOf(1005));
         response.getWriter().write(this.gson.toJson(this.map));
       }
       else {
         response.sendRedirect(
           "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1c62269461a335d&redirect_uri=http://gmri.mastergroup.com.cn/xnshop/page/userInsert.html?url=" + 
           url + 
           "&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect");
       }
     }  
 
     return flag;
   }
 
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
     throws Exception
   {
     super.postHandle(request, response, handler, modelAndView);
   }
 }

/* Location:           
 * Qualified Name:     com.yq.util.LoginInterceptor
 * JD-Core Version:    0.6.2
 */