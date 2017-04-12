 package com.yq.service;
 
 import com.yq.dao.OrderDao;
 import com.yq.entity.Order;
 import java.util.List;
 import java.util.Map;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 /**
  * 订单
  * @author mars
  *
  */
 @Service
 public class OrderService
 {
 
   @Autowired
   private OrderDao orderDao;
   
   
   public String queryOrderGoodsId(int orderId){
	   return this.orderDao.queryOrderGoodsId(orderId);
   }
  
   public int queryGoodsIdToGoodsOrder(int GoodsId){
	   return this.orderDao.queryGoodsOrder(GoodsId);
   }
   
   
   public int insert(Order order)
   {
     return this.orderDao.insert(order);
   }
  
   
   public int upstatus(Map<String, Object> map) {
     return this.orderDao.upstatus(map);
   }
 
   public int delete(Map<String, Object> map) {
     return this.orderDao.delete(map);
   }
 
   public List<Order> list(Order order) {
     return this.orderDao.list(order);
   }
 
   public List<Order> listById(Order order) {
     return this.orderDao.listById(order);
   }
 
   public int count(Order order) {
     return this.orderDao.count(order);
   }
   public List<Order> listJson(Order order) {
     return this.orderDao.listJson(order);
   }
 
   public int listJsonCount(Order order)
   {
     return this.orderDao.listJsonCount(order);
   }
   
   /** 更新订单状态 */ 
 }

/* Location:           
 * Qualified Name:     com.yq.service.OrderService
 * JD-Core Version:    0.6.2
 */