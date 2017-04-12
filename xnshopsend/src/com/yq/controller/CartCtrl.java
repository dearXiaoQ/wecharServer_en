package com.yq.controller;

import com.google.gson.Gson;
import com.yq.entity.Cart;
import com.yq.entity.Order;
import com.yq.entity.User;
import com.yq.service.CartService;
import com.yq.service.OrderService;
import com.yq.service.UserService;
import com.yq.util.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class CartCtrl extends StringUtil
{

	@Autowired
	private CartService cartService;
	private Cart cart = new Cart();

	@Autowired
	private OrderService orderService;
	private Order order = new Order();

	@Autowired
	private UserService userService;
	private User user = new User();
	private static Gson gson = new Gson();
	Map<String, Object> map = new HashMap();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

	/*** 
	 * 商品添加到购物车
	 * @param goods_id		商品id
	 * @param goods_name     商品名称
	 * @param goods_img		图片地址
	 * @param goods_spe		商品描述
	 * @param goods_price	商品价格
	 * @param goods_total	
	 * @param goods_num	 	添加的数量
	 * @param oppen_id				
	 * @param response
	 * @param session
	 */
	@ResponseBody
	@RequestMapping({"/page/cartInsert.html"})
	public void insert(Integer goods_id, String goods_name, String goods_img, String goods_spe, Float goods_price, Float goods_total, @RequestParam(defaultValue="1") Integer goods_num, String oppen_id, HttpServletResponse response, HttpSession session)
	{	
		int goodsOrder = this.orderService.queryGoodsIdToGoodsOrder(Integer.valueOf(goods_id));
		Map map2 = new HashMap();
		if(goodsOrder > 0) {	//plc中存在的商品
			try {
				this.map.put("goods_id", goods_id);
				this.map.put("goods_name", goods_name);
				this.map.put("goods_img", goods_img);
				this.map.put("goods_spe", goods_spe);
				this.map.put("goods_price", goods_price);
				oppen_id = getOppen_id(session);
				this.map.put("oppen_id", oppen_id);
				this.cart.setGoods_id(goods_id);
				this.cart.setOppen_id(oppen_id);
				int total = this.cartService.count(this.cart);
				int cart_num = Integer.parseInt(session.getAttribute("cart_num").toString()) + 1;
				session.setAttribute("cart_num", Integer.valueOf(cart_num));
				int rs = 0;
				/** 同一种商品最多添加一次  */
				if (total > 0) {
					rs = 2 ;   
					/* 广交会暂时屏蔽   goods_num = Integer.valueOf(this.cartService.goodsnum(this.cart) + 1);
         goods_total = Float.valueOf(goods_price.floatValue() * goods_num.intValue());
         this.map.put("goods_total", goods_total);
         this.map.put("goods_num", goods_num);
         rs = this.cartService.update(this.map);*/
				} else {
					goods_total = Float.valueOf(goods_price.floatValue() * goods_num.intValue());
					this.map.put("goods_total", goods_total);
					this.map.put("goods_num", goods_num);
				
					rs = this.cartService.insert(this.map);
				}
				this.cart.setOppen_id(oppen_id);
				int number = this.cartService.goodstotalnum(cart);
			
				map2.put("rs_code", Integer.valueOf(rs));
				map2.put("cart_num", number);
				response.getWriter().write(gson.toJson(map2));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			int cart_num = Integer.parseInt(session.getAttribute("cart_num").toString());
		   int rs = 2;
			map2.put("rs_code", Integer.valueOf(rs));
			map2.put("cart_num", Integer.valueOf(cart_num));
			try {
				response.getWriter().write(gson.toJson(map2));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * 购物车更新数据
	 * @param goods_price
	 * @param goods_total
	 * @param goods_num
	 * @param goods_id
	 * @param s
	 * @param response
	 * @param session
	 */
	@ResponseBody
	@RequestMapping({"/page/cartUpdate.html"})
	public void update(Float goods_price, Float goods_total, Integer goods_num, Integer goods_id, Integer s, HttpServletResponse response, HttpSession session) {
		try { this.map.put("oppen_id", getOppen_id(session));
		this.map.put("goods_num", goods_num);
		this.map.put("goods_total", Float.valueOf(goods_num.intValue() * goods_price.floatValue()));
		this.map.put("goods_id", goods_id);
		int cart_num = Integer.parseInt(session.getAttribute("cart_num").toString());
		if (s.intValue() == 1)
			cart_num++;
		else {
			cart_num--;
		}
		session.setAttribute("cart_num", Integer.valueOf(cart_num));
		Map map2 = new HashMap();
		int rs = 0;
		if (goods_num.intValue() < 1)
			rs = this.cartService.delete(this.map);
		else {
			rs = this.cartService.update(this.map);
		}
		map2.put("rs_code", Integer.valueOf(rs));
		map2.put("cart_num", Integer.valueOf(cart_num));

		response.getWriter().write(gson.toJson(map2));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping({"/page/cartDel.html"})
	public void delete(Integer goods_id, HttpSession session, HttpServletResponse response) {
		int cart_num = Integer.parseInt(session.getAttribute("cart_num").toString()) - 1;
		session.setAttribute("cart_num", Integer.valueOf(cart_num));
		int rs = this.cartService.delete(this.map);
		Map map2 = new HashMap();
		this.map.put("goods_id", goods_id);
		map2.put("rs_code", Integer.valueOf(rs));
		map2.put("cart_num", Integer.valueOf(cart_num));
		try {
			response.getWriter().write(gson.toJson(map2));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 购物车页面
	 * @param oppen_id
	 * @param session
	 * @return 
	 */
	@RequestMapping({"/page/cartList.html"})
	public ModelAndView list(String oppen_id, HttpSession session)
	{
		this.cart.setOppen_id(getOppen_id(session));
		List list = this.cartService.list(this.cart);
		Float tprice = this.cartService.goodstotalprice(this.cart);
		int tnum = this.cartService.goodstotalnum(this.cart);
		ModelAndView ml = new ModelAndView();
		ml.addObject("goods", list);
		ml.addObject("tprice", tprice.toString());
		ml.addObject("tnum", tnum);
		ml.setViewName("page/cart");
		return ml;
	}
}

/* Location:           
 * Qualified Name:     com.yq.controller.CartCtrl
 * JD-Core Version:    0.6.2
 */