package com.yq.controller;

import com.google.gson.Gson;
import com.yq.entity.Address;
import com.yq.entity.Category;
import com.yq.entity.Coupons;
import com.yq.entity.Freight;
import com.yq.entity.Goods;
import com.yq.entity.Order;
import com.yq.entity.User;
import com.yq.service.AddressService;
import com.yq.service.CategoryService;
import com.yq.service.CouponsService;
import com.yq.service.FreightService;
import com.yq.service.GoodsService;
import com.yq.service.OrderService;
import com.yq.service.UserService;
import com.yq.util.PageUtil;
import com.yq.util.StringUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/"})
public class GoodsCtrl extends StringUtil
{

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private CategoryService categoryService;
	private Goods goods = new Goods();
	private Category category = new Category();

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
	private OrderService orderService;
	private Order order = new Order();


	@Autowired
	private UserService userService;
	private User user = new User(); 
	Map<String, Object> map = new HashMap();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	private static Gson gson = new Gson();

	@RequestMapping({"/main/goodsAddjsp.html"})
	public ModelAndView addjsp() { ModelAndView ml = new ModelAndView();
	this.category.setStatus(Integer.valueOf(1));
	List list = this.categoryService.list(this.category);
	ml.addObject("category", list);
	ml.setViewName("main/goods/add");
	return ml;
	}

	/**
	 * 添加商品
	 * @param goods_name
	 * @param goods_img
	 * @param goods_spe
	 * @param goods_price
	 * @param goods_num
	 * @param goods_detail
	 * @param ctg_id
	 * @param status
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/main/goodsInsert.html"})
	public String insert(String goods_name, String goods_img, String goods_spe, Float goods_price,@RequestParam(defaultValue="0") Integer goods_num,@RequestParam(defaultValue="0") Integer goods_order,String goods_detail, Integer ctg_id, Integer status, Integer type)
	{
		String add_time = this.sf.format(new Date());
		this.map.put("goods_name", goods_name);
		this.map.put("goods_img", goods_img);
		this.map.put("goods_spe", goods_spe);
		this.map.put("goods_price", goods_price);
		this.map.put("goods_detail", goods_detail);
		this.map.put("add_time", add_time);
		this.map.put("ctg_id", ctg_id);
		this.map.put("goods_num", goods_num);
		this.map.put("goods_order", goods_order);
		this.map.put("status", Integer.valueOf(1));
		this.map.put("type", Integer.valueOf(1));
		if(goods_order > 0) {
			int updateGoodsOrder = this.goodsService.uppdateGoodsOrder(goods_order);
			System.out.println("updateGoodsOrder = " + updateGoodsOrder);
		}
		return String.valueOf(this.goodsService.insert(this.map));
	}

	/** 更新商品信息 */
	@ResponseBody
	@RequestMapping({"/main/goodsUpdate.html"})
	public String update(String goods_name, String goods_img, String goods_spe, Float goods_price,@RequestParam(defaultValue="0") Integer goods_num, @RequestParam(defaultValue="0") Integer goods_order, String goods_detail, String add_time, Integer ctg_id, Integer goods_id)
	{	
		this.map.put("goods_name", goods_name);
		this.map.put("goods_img", goods_img);
		this.map.put("goods_spe", goods_spe);
		this.map.put("goods_price", goods_price);
		this.map.put("goods_num", goods_num);
		this.map.put("goods_order", goods_order);
		this.map.put("goods_detail", goods_detail);
		this.map.put("add_time", add_time);
		this.map.put("ctg_id", ctg_id);
		this.map.put("goods_id", goods_id);
		this.map.put("type", Integer.valueOf(1));
		if(goods_order > 0){
			int result = this.goodsService.uppdateGoodsOrder(goods_order);
			System.out.println("resutl = " + result);
		} 
		return String.valueOf(this.goodsService.update(this.map));
	}

	@ResponseBody
	@RequestMapping({"/main/goodsUpstatus.html"})
	public String upstatus(Integer goods_id, Integer status) {
		this.map.put("status", status);
		this.map.put("goods_id", goods_id);
		return String.valueOf(this.goodsService.upstatus(this.map));
	}
	@ResponseBody
	@RequestMapping({"/main/goodsUpisrec.html"})
	public String upisrec(Integer goods_id, Integer is_recommend) {
		this.map.put("is_recommend", is_recommend);
		this.map.put("goods_id", goods_id);
		return String.valueOf(this.goodsService.upisrec(this.map));
	}

	@RequestMapping({"/main/goodsList.html"})
	public ModelAndView list(Integer status, @RequestParam(defaultValue="") String goods_name, @RequestParam(defaultValue="0") Integer ctg_id, @RequestParam(defaultValue="1") Integer currentPage, HttpServletRequest request)
			throws UnsupportedEncodingException
			{
		goods_name = URLDecoder.decode(goods_name, "utf-8");
		this.goods.setStatus(status);
		this.goods.setGoods_name(goods_name);
		this.goods.setCtg_id(ctg_id);
		this.goods.setType(Integer.valueOf(1));
		this.goods.setIs_recommend(Integer.valueOf(0));
		System.out.println(request.getParameter("goods_name"));
		int total = this.goodsService.count(this.goods);
		PageUtil.pager(currentPage.intValue(), this.pagesize_1.intValue(), total, request);
		this.goods.setPageSize(this.pagesize_1.intValue());
		this.goods.setCurrentNum(PageUtil.currentNum(currentPage.intValue(), this.pagesize_1.intValue()));
		List list = this.goodsService.list(this.goods);
		ModelAndView ml = new ModelAndView();
		ml.addObject("goods", list);
		ml.addObject("goods_name", goods_name);
		ml.setViewName("main/goods/list");
		return ml;
			}

	@RequestMapping({"/main/goodsListById.html"})
	public ModelAndView listById(Integer goods_id)
	{
		this.goods.setGoods_id(goods_id);
		List list = this.goodsService.listById(this.goods);
		ModelAndView ml = new ModelAndView();
		this.category.setStatus(Integer.valueOf(1));
		List ctg = this.categoryService.list(this.category);
		ml.addObject("category", ctg);
		ml.addObject("list", list);
		ml.setViewName("main/goods/info");
		return ml;
	}

	@RequestMapping({"/page/goodsListById.html"})
	public ModelAndView goodsListById(Integer goods_id)
	{
		this.goods.setGoods_id(goods_id);
		List list = this.goodsService.listById(this.goods);
		ModelAndView ml = new ModelAndView();
		ml.addObject("list", list);
		ml.addObject("goods_id", goods_id);
		ml.setViewName("page/goods-info");

		return ml;
	}

	@RequestMapping({"/page/goodsOrder.html"})
	public ModelAndView goodsOrder(Integer goods_id)
	{
		this.goods.setGoods_id(goods_id);
		List list = this.goodsService.listById(this.goods);
		((Goods)list.get(0)).setGoods_num(Integer.valueOf(1));
		ModelAndView ml = new ModelAndView();
		ml.addObject("goods", list);
		ml.addObject("goods_id", goods_id);
		ml.setViewName("page/goods-order");
		return ml;
	}

	@ResponseBody
	@RequestMapping({"/page/judgeStock.html"})
	public void judgeGoodsStock(Integer goods_id, HttpServletResponse response, HttpSession session) {
		int goodsOrder = this.orderService.queryGoodsIdToGoodsOrder(Integer.valueOf(goods_id));
		Map map2 = new HashMap();
		int rs;
		if(goodsOrder > 0) {	//plc中存在的商品
			rs = 1;
		} else {
			rs = 2;
		}
		try{
			map2.put("rs_code", Integer.valueOf(rs));
			response.getWriter().write(gson.toJson(map2));
		} catch(Exception e) {
			e.printStackTrace();
			rs = 0;
			try{
				map2.put("rs_code", Integer.valueOf(rs));
				response.getWriter().write(gson.toJson(map2));
			}catch(Exception exception){exception.printStackTrace();}

		}
	}

	@RequestMapping({"/page/secGoodsList.html"})
	public ModelAndView secGoodsList(String goods_name, @RequestParam(defaultValue="0") Integer is_recommend, @RequestParam(defaultValue="1") Integer status, @RequestParam(defaultValue="0") Integer ctg_id, @RequestParam(defaultValue="1") Integer currentPage, HttpServletRequest request)
	{
		try
		{
			if (StringUtils.isNotEmpty(goods_name)) {
				goods_name = new String(goods_name.getBytes("iso8859-1"), 
						"utf-8");
			}
			this.goods.setType(Integer.valueOf(1));
			this.goods.setStatus(status);
			this.goods.setCtg_id(ctg_id);
			this.goods.setGoods_name(goods_name);
			this.goods.setIs_recommend(is_recommend);
			List list = this.goodsService.list(this.goods);
			ModelAndView ml = new ModelAndView();
			ml.addObject("goods", list);
			ml.setViewName("page/goods-list");
			return ml;
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}return null;
	}
}

/* Location:           
 * Qualified Name:     com.yq.controller.GoodsCtrl
 * JD-Core Version:    0.6.2
 */