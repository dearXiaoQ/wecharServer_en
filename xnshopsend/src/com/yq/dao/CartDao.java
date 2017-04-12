package com.yq.dao;

import com.yq.entity.Cart;
import java.util.List;
import java.util.Map;

public abstract interface CartDao
{
  public abstract int insert(Map<String, Object> paramMap);

  public abstract int update(Map<String, Object> paramMap);

  public abstract int delete(Map<String, Object> paramMap);

  public abstract List<Cart> list(Cart paramCart);

  public abstract int count(Cart paramCart);

  public abstract int goodsnum(Cart paramCart);

  public abstract Float goodstotalprice(Cart paramCart);

  public abstract int goodstotalnum(Cart paramCart);
}

/* Location:           
 * Qualified Name:     com.yq.dao.CartDao
 * JD-Core Version:    0.6.2
 */