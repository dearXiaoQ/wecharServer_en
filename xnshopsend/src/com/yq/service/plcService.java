package com.yq.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.dao.PlcDao;


/**
 * plc订单反馈
 * @author xioaQ
 *
 */
@Service
public class plcService {
	
	@Autowired
	private PlcDao plcDao;
	
	public int plcOrderUpstatus(Map<String, Object> map){
		return this.plcDao.orderUpstatus(map);
	}
	
}
