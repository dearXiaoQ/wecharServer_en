package com.yq.dao;

import java.util.Map;

public abstract interface PlcDao {
	/** 更新订单状态 */
	public abstract int orderUpstatus(Map<String, Object> paramMap);
	
}
