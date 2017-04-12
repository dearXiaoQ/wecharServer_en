 package com.yq.util;
 
 import javax.servlet.http.HttpSession;
 
 public class StringUtil
 {
   protected Integer pagesize_1 = Integer.valueOf(10);
 
   public String getOppen_id(HttpSession session)
   {
     return (String)session.getAttribute("oppen_id");
   }
 
   public void setOppen_id(String oppen_id, HttpSession session)
   {
     session.setAttribute("oppen_id", oppen_id);
   }
 
   public Integer getPagesize_1() {
     return this.pagesize_1;
   }
 
   public void setPagesize_1(Integer pagesize_1) {
     this.pagesize_1 = pagesize_1;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.util.StringUtil
 * JD-Core Version:    0.6.2
 */