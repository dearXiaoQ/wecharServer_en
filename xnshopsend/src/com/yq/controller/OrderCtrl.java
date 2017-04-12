package com.yq.controller;

import com.google.gson.Gson;
import com.yq.entity.Address;
import com.yq.entity.Area;
import com.yq.entity.Cart;
import com.yq.entity.Coupons;
import com.yq.entity.Freight;
import com.yq.entity.Goods;
import com.yq.entity.GoodsJson;
import com.yq.entity.GoodsOther;
import com.yq.entity.Order;
import com.yq.entity.User;
import com.yq.service.AddressService;
import com.yq.service.AreaService;
import com.yq.service.CartService;
import com.yq.service.CouponsService;
import com.yq.service.FreightService;
import com.yq.service.GoodsService;
import com.yq.service.OrderService;
import com.yq.service.UserService;
import com.yq.util.PageUtil;
import com.yq.util.StringUtil;
import com.yq.weixin.pay.action.NotifyServlet;
import com.yq.weixin.pay.action.TopayServlet;
import com.yq.weixin.pay.util.GetWxOrderno;
import com.yq.weixin.servlet.WechatPushMassage;
import com.yq.util.HttpUtils;







import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ezmorph.array.IntArrayMorpher;
import net.sf.json.JSONArray;

import org.aspectj.apache.bcel.generic.NEW;
import org.json.JSONObject;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yq.weixin.util.SignUtil;

@Controller
@RequestMapping
public class OrderCtrl extends StringUtil
{	

	@Autowired
	private OrderService orderService;
	private Order order = new Order();


	@Autowired
	private CartService cartService;
	private Cart cart = new Cart();

	@Autowired
	private CouponsService couponsService;
	private Coupons coupons = new Coupons();

	@Autowired
	private AddressService addressService;
	private Address address = new Address();

	@Autowired
	private FreightService freightService;
	private Freight freight = new Freight();

	@Autowired
	private UserService userService;
	private User user = new User();

	@Autowired
	private AreaService areaService;
	private Area area = new Area();

	@Autowired
	private GoodsService goodsService;
	private Goods goods = new Goods();
	Gson gson = new Gson();
	Map<String, Object> map = new HashMap();
	int oldList0Size = 0;
	int oldList1Size = 0;
	int oldList2Size = 0;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	WechatPushMassage wechatPushMassage = new WechatPushMassage();



	/** 微信支付 */
	@ResponseBody
	@RequestMapping({"/page/orderInsert.html"})
	public String insert(String goods_id, String goods_name, String goods_img, String goods_spe, String goods_price, String goods_num, Float goods_total, Integer goods_total_num, Integer cps_id, String cps_name, @RequestParam(defaultValue="0") Float cps_price, String addr_name, String receive, String oppen_id, Integer status, String note, HttpSession session)
	{
		String add_time = this.sdf.format(new Date());
		oppen_id = getOppen_id(session);
		Order order = new Order();
		order.setGoods_id(goods_id);
		order.setGoods_name(goods_name);
		order.setGoods_img(goods_img);
		order.setGoods_spe(goods_spe);
		order.setGoods_price(goods_price);
		order.setGoods_num(goods_num);
		order.setGoods_total(goods_total);
		order.setGoods_total_num(goods_total_num);
		order.setCps_id(cps_id);
		order.setCps_name(cps_name);
		order.setAddr_name(addr_name);
		order.setCps_price(cps_price);
		order.setReceive(receive);
		order.setOppen_id(oppen_id);
		order.setAdd_time(add_time);
		order.setStatus(0);
		order.setNote(note.trim());
		if (this.orderService.insert(order) == 1) {
			if (goods_id.contains(",-=")) {
				String[] gids = goods_id.split(",-=");
				for (int i = 0; i < gids.length; i++) {
					this.map.put("goods_id", gids[i]);
					this.cartService.delete(this.map);
				}
			} else {
				this.map.put("goods_id", goods_id);
				session.setAttribute("cart_num", Integer.valueOf(0));
				this.cartService.delete(this.map);
			}
			if (cps_id != null) {
				this.map.put("status", Integer.valueOf(0));
				this.map.put("cps_id", cps_id);
				this.couponsService.upstatus(this.map);
			}
			int orderId = order.getOrder_id();
			return String.valueOf(order.getOrder_id());
		}
		return "0";
	}

	/** 修改订单号状态（仅限广交会模拟支付使用） */
	@ResponseBody
	@RequestMapping({"/page/modifyOrderStatus.html"})
	public void updateOrederStatus(String order_id, HttpServletResponse response, HttpSession session)  {
		//83,-=82,-=85
		
		
	/*	System.out.println("修改的订单号为： "  + order_id);
		HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		map3.put("rs_code", 1);
		map3.put("cart_num", 0);
		this.map.put("order_id", order_id);
		this.map.put("status", 1);
		int result = this.orderService.upstatus(this.map);
		System.out.println("result = " + result);
		try {
			response.getWriter().write(gson.toJson(map3));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ;*/
	
		String goodsStr = this.orderService.queryOrderGoodsId(Integer.valueOf(order_id));
		String[] arr = goodsStr.split(",-=");
		int[] goodsList = new int[arr.length]; 
		for(int i = 0; i < arr.length; i ++) {
			goodsList[i] = this.orderService.queryGoodsIdToGoodsOrder(Integer.valueOf(arr[i]));
			System.out.println(goodsList[i]);
		}

		//httpsRequest(targetUrl, "POST", gson.toJson(orders).toString());
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObjectOrder = new JSONObject();
		//json数组
		//组装json数组
		jsonObjectOrder.put("orderNumber", order_id);
		jsonObjectOrder.put("tableNumber", 1);
		jsonObjectOrder.put("orderList", goodsList);
		jsonObject.put("order", jsonObjectOrder);

		try{	
			HttpUtils.login(jsonObject.toString());
		} catch (Exception e){
			System.out.println("下达订单到PLC 失败！");
			e.printStackTrace();
			try{	
				HttpUtils.login(jsonObject.toString());
			} catch (Exception exception){
				System.out.println("下达订单到PLC 失败！");
				exception.printStackTrace();
			}
		} finally{
		}

		System.out.println("修改的订单号为： "  + order_id);
		HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		map3.put("rs_code", 1);
		map3.put("cart_num", 0);
		this.map.put("order_id", order_id);
		this.map.put("status", 1);
		int result = this.orderService.upstatus(this.map);
		System.out.println("result = " + result);
		try {
			response.getWriter().write(gson.toJson(map3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping({"/page/orderUpdate.html"})
	public String update(Integer order_id, @RequestParam(defaultValue="1") Integer status, HttpSession session)
	{
		this.map.put("order_id", order_id);
		this.map.put("status", status);
		return String.valueOf(this.orderService.upstatus(this.map));
	}
	@ResponseBody
	@RequestMapping({"/main/orderUpstatus.html"})
	public String upstatus(Integer status, Integer order_id) {
		this.map.put("order_id", order_id);
		this.map.put("status", status);
		int rs = this.orderService.upstatus(this.map);
		if (rs == 1) {
			this.order.setOrder_id(order_id.intValue());
			List list = this.orderService.listById(this.order);
			if (status.intValue() == -6)
				this.map.put("result", "商家已同意退款");
			else {
				this.map.put("result", "商家已发货");
			}

			this.map.put("body", ((Order)list.get(0)).getGoods_name().replace("-=", ""));
			this.map.put("price", ((Order)list.get(0)).getGoods_total());
			this.map.put("oppen_id", ((Order)list.get(0)).getOppen_id());
			this.wechatPushMassage.pushMessage(this.map);
		}
		return String.valueOf(rs);
	}
	@ResponseBody
	@RequestMapping({"/main/orderDel.html"})
	public String delete(Integer order_id) {
		this.map.put("order_id", order_id);
		return String.valueOf(this.orderService.delete(this.map));
	}


	@RequestMapping({"/page/orderList.html"})
	public ModelAndView list(@RequestParam(defaultValue="-2") Integer status, String oppen_id, HttpSession session)
	{
		oppen_id = getOppen_id(session);
		this.order.setOppen_id(oppen_id);
		this.order.setStatus(-2);
		this.order.setStart_time("");
		this.order.setEnd_time("");
		this.order.setCtg_name("");
		this.order.setGoods_name("");
		this.order.setAddr_name("");
		List list = this.orderService.list(this.order);
		System.out.println("list=" + list.size());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list.get(i)).getGoods_num().split(",-=");

				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord" + i, ordList);
			}
		}
		this.order.setStatus(0);
		List list0 = this.orderService.list(this.order);
		if (list0.size() > 0) {
			for (int i = 0; i < list0.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list0.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list0.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list0.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list0.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list0.get(i)).getGoods_num().split(",-=");
				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord0" + i, ordList);
			}
		}

		this.order.setStatus(1);
		List list1 = this.orderService.list(this.order);
		if (list1.size() > 0) {
			for (int i = 0; i < list1.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list1.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list1.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list1.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list1.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list1.get(i)).getGoods_num().split(",-=");
				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord1" + i, ordList);
			}
		}
		
		
		this.order.setStatus(2);
		List list2 = this.orderService.list(this.order);
		if (list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list2.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list2.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list2.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list2.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list2.get(i)).getGoods_num().split(",-=");
				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord2" + i, ordList);
			}
		}
		


		this.cart.setOppen_id(oppen_id);
		int carNum = this.cartService.goodstotalnum(cart);
		session.setAttribute("cart_num", Integer.valueOf(carNum));
		this.map.put("list", list);
		this.map.put("list0", list0);
		this.map.put("list1", list1);
		this.map.put("list2", list2);
		oldList0Size = list0.size();
		oldList1Size = list1.size();
		oldList2Size = list2.size();
		ModelAndView ml = new ModelAndView();
		ml.addObject("map", this.map);
		ml.setViewName("page/order-list");
		return ml;
	}
	
	
	
	@RequestMapping({"/page/refreshOrderList.html"})
	public void refresh(@RequestParam(defaultValue="-2") Integer status, String open_id, HttpServletResponse response,HttpSession session)
	{	
		
		this.order.setOppen_id(getOppen_id(session));
		this.order.setStatus(-2);
		this.order.setStart_time("");
		this.order.setEnd_time("");
		this.order.setCtg_name("");
		this.order.setGoods_name("");
		this.order.setAddr_name("");
		List list = this.orderService.list(this.order);
		System.out.println("list=" + list.size());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list.get(i)).getGoods_num().split(",-=");

				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord" + i, ordList);
			}
		}
		this.order.setStatus(0);
		List list0 = this.orderService.list(this.order);
		if (list0.size() > 0) {
			for (int i = 0; i < list0.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list0.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list0.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list0.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list0.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list0.get(i)).getGoods_num().split(",-=");
				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord0" + i, ordList);
			}
		}

		this.order.setStatus(1);
		List list1 = this.orderService.list(this.order);
		if (list1.size() > 0) {
			for (int i = 0; i < list1.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list1.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list1.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list1.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list1.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list1.get(i)).getGoods_num().split(",-=");
				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord1" + i, ordList);
			}
		}
		
		
		this.order.setStatus(2);
		List list2 = this.orderService.list(this.order);
		if (list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list2.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list2.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list2.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list2.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list2.get(i)).getGoods_num().split(",-=");
				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord2" + i, ordList);
			}
		}
		

		
		this.cart.setOppen_id(open_id);
		int carNum = this.cartService.goodstotalnum(cart);
		session.setAttribute("cart_num", Integer.valueOf(carNum));
		this.map.put("list", list);
		this.map.put("list0", list0);
		this.map.put("list1", list1);
		this.map.put("list2", list2);
		
		
		HashMap<String, Integer> map3 = new HashMap<String, Integer>();
		if(oldList0Size == list0.size() && oldList1Size == list1.size() && oldList2Size == list2.size()) {
			map3.put("rs_code", 0);			
			System.out.println("订单数据没有更新！");
		}  else {
			map3.put("rs_code", 1);			
			System.out.println("订单数据已经更新！");
		}
		try {
			response.getWriter().write(gson.toJson(map3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@RequestMapping({"/order.html"})
	public void orderListJs(@RequestParam(value="c", defaultValue="1") Integer currentPage, @RequestParam(value="p", defaultValue="0") Integer pageSize, HttpServletRequest request, HttpServletResponse response)
			throws IOException
			{
		int total = this.orderService.listJsonCount(this.order);
		PageUtil.pager(currentPage.intValue(), pageSize.intValue(), total, request);
		this.order.setPageSize(pageSize.intValue());
		this.order.setCurrentNum(PageUtil.currentNum(currentPage.intValue(), pageSize.intValue()));
		List list = this.orderService.listJson(this.order);
		List goList = new ArrayList();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List goodsList = new ArrayList();
				String[] gName = ((Order)list.get(i)).getGoods_name().split(",-=");
				String[] gNum = ((Order)list.get(i)).getGoods_num().split(",-=");
				String[] gPrice = ((Order)list.get(i)).getGoods_price().split(",-=");
				for (int m = 0; m < gName.length; m++) {
					GoodsJson gj = new GoodsJson();
					gj.setGoods_name(gName[m]);
					gj.setGoods_num(gNum[m]);
					gj.setGoods_price(gPrice[m]);
					goodsList.add(gj);
				}
				GoodsOther go = new GoodsOther();
				go.setAddr_name(((Order)list.get(i)).getAddr_name());
				go.setNote(((Order)list.get(i)).getNote());
				go.setAdd_time(((Order)list.get(i)).getAdd_time());
				go.setGoodsList(goodsList);
				go.setTotal(Integer.valueOf(total));
				goList.add(go);
			}
		}
		JSONArray json = JSONArray.fromObject(goList);

		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
			}

	@RequestMapping({"/main/orderList.html"})
	public ModelAndView orderList(@RequestParam(defaultValue="1") Integer currentPage, @RequestParam(defaultValue="-2") Integer status, @RequestParam(defaultValue="") String start_time, @RequestParam(defaultValue="") String end_time, @RequestParam(defaultValue="") String ctg_name, @RequestParam(defaultValue="") String goods_name, @RequestParam(defaultValue="") String addr_name, HttpServletRequest request, HttpSession session)
			throws UnsupportedEncodingException
			{
		start_time = URLDecoder.decode(start_time, "utf-8");
		end_time = URLDecoder.decode(end_time, "utf-8");
		ctg_name = URLDecoder.decode(ctg_name, "utf-8");
		goods_name = URLDecoder.decode(goods_name, "utf-8");
		addr_name = URLDecoder.decode(addr_name, "utf-8");
		this.order.setOppen_id("");
		this.order.setStatus(status.intValue());
		this.order.setStart_time(start_time);
		this.order.setEnd_time(end_time);
		this.order.setCtg_name(ctg_name);
		this.order.setGoods_name(goods_name);
		this.order.setAddr_name(addr_name);
		int total = this.orderService.count(this.order);
		PageUtil.pager(currentPage.intValue(), this.pagesize_1.intValue(), total, request);
		this.order.setPageSize(this.pagesize_1.intValue());
		this.order.setCurrentNum(PageUtil.currentNum(currentPage.intValue(), this.pagesize_1.intValue()));
		List list = this.orderService.list(this.order);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list.get(i)).getGoods_num().split(",-=");

				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord" + i, ordList);
			}
		}

		this.map.put("list", list);
		ModelAndView ml = new ModelAndView();
		ml.addObject("map", this.map);
		ml.addObject("status", status);
		ml.addObject("start_time", start_time);
		ml.addObject("end_time", end_time);
		ml.addObject("ctg_name", ctg_name);
		ml.addObject("goods_name", goods_name);
		ml.addObject("addr_name", addr_name);
		ml.setViewName("main/order/list");
		return ml;
			}


	/**微信支付页面*/
	@RequestMapping({"/page/payOrder.html"})
	public ModelAndView payOrder(Integer order_id, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	{
		this.order.setOrder_id(order_id.intValue());
		List list = this.orderService.listById(this.order);
		this.map.put("list", list);

		ModelAndView ml = new ModelAndView();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List ordList = new ArrayList();
				String[] gId = ((Order)list.get(i)).getGoods_id().split(",-=");
				String[] gName = ((Order)list.get(i)).getGoods_name().split(",-=");
				String[] gImg = ((Order)list.get(i)).getGoods_img().split(",-=");
				String[] gPrice = ((Order)list.get(i)).getGoods_price().split(",-=");
				String[] gNum = ((Order)list.get(i)).getGoods_num().split(",-=");

				for (int m = 0; m < gId.length; m++) {
					Order ord = new Order();
					ord.setGoods_id(gId[m]);
					ord.setGoods_name(gName[m]);
					ord.setGoods_img(gImg[m]);
					ord.setGoods_price(gPrice[m]);
					ord.setGoods_num(gNum[m]);
					ordList.add(ord);
				}
				this.map.put("ord" + i, ordList);
			}
			if (((Order)list.get(0)).getGoods_total().floatValue() != 0.0F) {
				TopayServlet.getPackage(list, request, response, session);
				ml.addObject("map", this.map);
				ml.setViewName("page/pay-order");
			} else {
				this.map.put("status", Integer.valueOf(1));
				this.map.put("order_id", order_id);
				String url = this.orderService.upstatus(this.map) == 1 ? "redirect:orderList.html" : "error";
				ml.setViewName(url);
			}
			return ml;
		}
		ml.addObject("error", "payOrder无带支付订单！");
		ml.setViewName("page/error");
		return ml;
	}

	@RequestMapping({"/page/noticeOrder.html"})
	public void noticeOrder(HttpServletRequest request)
	{
		//注释：
		String xmlStr = NotifyServlet.getWxXml(request);
		Map map2 = GetWxOrderno.doXMLParse(xmlStr);
		String return_code = (String)map2.get("return_code");
		String order_id = (String)map2.get("out_trade_no");
		this.order.setOrder_id(Integer.parseInt(order_id));
		List list = this.orderService.listById(this.order);
		this.map.put("order_id", order_id);
		this.map.put("status", Integer.valueOf(1));
		if ((((Order)list.get(0)).getStatus() == 0) && 
				(return_code.equals("SUCCESS"))) {
			this.orderService.upstatus(this.map);
			this.map.put("result", "订单支付成功");
			this.map.put("body", ((Order)list.get(0)).getGoods_name().replace("-=", ""));
			this.map.put("price", ((Order)list.get(0)).getGoods_total());
			this.map.put("oppen_id", ((Order)list.get(0)).getOppen_id());
			this.wechatPushMassage.pushMessage(this.map);
		}
	}

	/**
	 * 结算请求
	 * @param cps_id		
	 * @param addr_id
	 * @param cps_name
	 * @param cps_price
	 * @param oppen_id
	 * @param session
	 * @return
	 */  
	/** 备注：修改该方法，直接转到微信支付界面（用于广交会测试） */
	@RequestMapping({"/page/cartOrderList.html"})
	public ModelAndView cartList(@RequestParam(defaultValue="0") Integer cps_id, @RequestParam(defaultValue="0") Integer addr_id, String cps_name, @RequestParam(defaultValue="0") Float cps_price, String oppen_id, HttpSession session)
	{
		ModelAndView ml = new ModelAndView();

		oppen_id = getOppen_id(session);
		this.cart.setOppen_id(oppen_id);
		List list = this.cartService.list(this.cart);
		Float tprice = this.cartService.goodstotalprice(this.cart);
		ml.addObject("price", tprice);
		int tnum = this.cartService.goodstotalnum(this.cart);
		if (cps_id != null) {
			System.out.println(cps_id);
			if (cps_id.intValue() != 0) {
				this.coupons.setCps_id(cps_id);
				List cps = this.couponsService.listById(this.coupons);
				if (cps.size() > 0) {
					cps_price = ((Coupons)cps.get(0)).getCps_price();
				}
				ml.addObject("cps", cps);
			}
		}
		List addr = new ArrayList();
		if (addr_id.intValue() == 0) {
			this.address.setOppen_id(oppen_id);
			addr = this.addressService.list(this.address);
		} else {
			this.address.setAddr_id(addr_id);
			addr = this.addressService.listById(this.address);
		}
		tprice = Float.valueOf(tprice.floatValue() - cps_price.floatValue());
		List fgt = this.freightService.list(this.freight);
		if (fgt.size() > 0) {
			if (tprice.floatValue() < ((Freight)fgt.get(0)).getFree_price().floatValue()) {
				tprice = Float.valueOf(tprice.floatValue() + ((Freight)fgt.get(0)).getFgt_price().floatValue());
				ml.addObject("fgt_price", ((Freight)fgt.get(0)).getFgt_price());
			} else {
				ml.addObject("fgt_price", Integer.valueOf(0));
			}
		}
		String add_time = this.sf.format(new Date());

		this.coupons.setOppen_id(oppen_id);
		this.coupons.setCps_level(Integer.valueOf(-1));
		this.coupons.setCps_time(add_time);
		this.coupons.setStatus(1);
		List cps = this.couponsService.list(this.coupons);

		this.area.setStatus(Integer.valueOf(1));
		this.area.setLevel(Integer.valueOf(0));
		List areaList = this.areaService.list(this.area);

		ml.addObject("goods", list);
		ml.addObject("tprice", tprice);
		ml.addObject("addr", addr);
		ml.addObject("tnum", Integer.valueOf(tnum));
		ml.addObject("cpsCount", Integer.valueOf(cps.size()));
		ml.addObject("cps_id", cps_id);
		ml.addObject("addr_id", addr_id);

		ml.addObject("areaList", areaList);
		ml.setViewName("page/cart-order");




		return ml;
	}

	@RequestMapping({"/page/goodsOrderSure.html"})
	public ModelAndView goodsOrder(Integer goods_id, Integer goods_num, @RequestParam(defaultValue="0") Integer cps_id, @RequestParam(defaultValue="0") Integer addr_id, String cps_name, @RequestParam(defaultValue="0") Float cps_price, String oppen_id, HttpSession session)
	{
		ModelAndView ml = new ModelAndView();
		oppen_id = getOppen_id(session);
		this.cart.setOppen_id(oppen_id);
		this.goods.setGoods_id(goods_id);
		List list = this.goodsService.listById(this.goods);
		Float goods_total = Float.valueOf(goods_num.intValue() * ((Goods)list.get(0)).getGoods_price());
		Float tprice = Float.valueOf(goods_num.intValue() * ((Goods)list.get(0)).getGoods_price());
		ml.addObject("price", tprice);
		int tnum = this.cartService.goodstotalnum(this.cart);

		if (cps_id != null) {
			System.out.println(cps_id);
			if (cps_id.intValue() != 0) {
				this.coupons.setCps_id(cps_id);
				List cps = this.couponsService.listById(this.coupons);

				if (cps.size() > 0) {
					cps_price = ((Coupons)cps.get(0)).getCps_price();
				}
				ml.addObject("cps", cps);
			}
		}
		List addr = new ArrayList();
		if (addr_id.intValue() == 0) {
			this.address.setOppen_id(oppen_id);
			addr = this.addressService.list(this.address);
		} else {
			this.address.setAddr_id(addr_id);
			addr = this.addressService.listById(this.address);
		}
		tprice = Float.valueOf(tprice.floatValue() - cps_price.floatValue());
		List fgt = this.freightService.list(this.freight);
		if (fgt.size() > 0) {
			if (tprice.floatValue() < ((Freight)fgt.get(0)).getFree_price().floatValue()) {
				tprice = Float.valueOf(tprice.floatValue() + ((Freight)fgt.get(0)).getFgt_price().floatValue());
				ml.addObject("fgt_price", ((Freight)fgt.get(0)).getFgt_price());
			} else {
				ml.addObject("fgt_price", Integer.valueOf(0));
			}
		}
		String add_time = this.sf.format(new Date());
		this.coupons.setOppen_id(oppen_id);
		this.coupons.setCps_level(Integer.valueOf(-1));
		this.coupons.setCps_time(add_time);
		this.coupons.setStatus(1);
		List cps = this.couponsService.list(this.coupons);
		this.user.setOppen_id(oppen_id);
		List userList = this.userService.listById(this.user);
		this.area.setStatus(Integer.valueOf(1));
		this.area.setLevel(Integer.valueOf(0));
		List areaList = this.areaService.list(this.area);
		ml.addObject("goods", list);
		ml.addObject("tprice", tprice);
		ml.addObject("addr", addr);
		ml.addObject("tnum", goods_num);
		ml.addObject("cps_id", cps_id);
		ml.addObject("addr_id", addr_id);
		ml.addObject("userList", userList);
		ml.addObject("goods_id", goods_id);
		ml.addObject("goods_num", goods_num);
		ml.addObject("goods_total", goods_total);

		ml.addObject("tnum", Integer.valueOf(tnum));
		ml.addObject("cpsCount", Integer.valueOf(cps.size()));

		ml.addObject("areaList", areaList);
		ml.setViewName("page/goods-order-sure");
		return ml;
	}
}

/* Location:           
 * Qualified Name:     com.yq.controller.OrderCtrl
 * JD-Core Version:    0.6.2
 */