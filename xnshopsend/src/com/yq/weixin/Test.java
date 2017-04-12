 package com.yq.weixin;
 
 import java.io.PrintStream;
 import java.util.Hashtable;
 
 public class Test
 {
   public static void main(String[] args)
   {
     ConnDbSqlServerWXG db_wxg = new ConnDbSqlServerWXG();
     String asql = "select * from weixin_yikangjian_material where MID=5";
     Hashtable ahash = db_wxg.getValue(asql);
     System.out.println(ahash);
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.Test
 * JD-Core Version:    0.6.2
 */