package com.yq.dao;

import com.yq.entity.Order;
import java.util.List;
import java.util.Map;

public abstract interface OrderDao
{

  public abstract int queryGoodsOrder(int goodsId);
	
  public abstract int insert(Order paramOrder);

  public abstract int upstatus(Map<String, Object> paramMap);

  public abstract int delete(Map<String, Object> paramMap);

  public abstract List<Order> list(Order paramOrder);

  public abstract List<Order> listById(Order paramOrder);

  public abstract List<Order> listJson(Order paramOrder);

  public abstract int count(Order paramOrder);

  public abstract int listJsonCount(Order paramOrder);
  
  public abstract String queryOrderGoodsId(int orderId);
}

/* Location:           
 * Qualified Name:     com.yq.dao.OrderDao
 * JD-Core Version:    0.6.2
 */