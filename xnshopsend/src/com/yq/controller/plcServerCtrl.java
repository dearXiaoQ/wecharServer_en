package com.yq.controller;

import java.beans.FeatureDescriptor;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yq.service.plcService;
	
/** 
 * 微信火锅餐厅
 * @author mars
 *
 */
@Controller
@RequestMapping
public class plcServerCtrl {
	
	@Autowired
	private plcService plcService;
	
	@Autowired
	Map<String, Object> map = new HashMap();
	
	/** 处理plc中转服务器的Post请求 */
	@RequestMapping(value =  "/page/index/plc.html", method = RequestMethod.POST)
	@ResponseBody
	public String handlePlcServerPostRequest(@RequestBody String data) {
		System.out.println("进入处理Plc中转服务器的Post请求的方法：");
		System.out.println(data);
		//提取第一层
		JSONObject requestJsonObject = new JSONObject(data);
		String plcHanderInfo = requestJsonObject.get("plcHanderOrderInfo").toString();
		System.out.println(plcHanderInfo);
		//提取第二层面
		JSONObject plcHanderInfoJsonObject = new JSONObject(plcHanderInfo.substring(1, plcHanderInfo.length() - 1));
		String order_id = plcHanderInfoJsonObject.getString("orderNumber");
		Integer status = plcHanderInfoJsonObject.getInt("orderState");
		if(status == 0) {
			status = 2;
		} else {
			status = 1;
		}
		this.map.put("order_id", order_id);
		this.map.put("status", status);
		int result = this.plcService.plcOrderUpstatus(map);
		System.out.println("orderNumber = " + order_id);
		System.out.println("orederState = "  + status);
		
		System.out.println("数据库操作结果：" + result);
		
		//返回处理信息
		JSONObject jsonObjectrespons = new JSONObject();
		String resultStr = "";
		if(result == 1) {
			result = 0;
			resultStr = "ok";
		} else {
			result = -1;
			resultStr = "failure";
		}
		
		jsonObjectrespons.put("errorCode", result);
		jsonObjectrespons.put("message", "ok");
		jsonObjectrespons.put("res", new String[]{});
		return jsonObjectrespons.toString();
	}
	
}
