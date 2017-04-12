 package com.yq.controller;
 
 import com.yq.entity.Cart;
import com.yq.entity.Category;
import com.yq.entity.Goods;
import com.yq.service.CartService;
import com.yq.service.CategoryService;
import com.yq.service.GoodsService;
import com.yq.weixin.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 
 @Controller
 @RequestMapping({"/"})
 public class CategoryCtrl extends com.yq.util.StringUtil
 {
 
   @Autowired
   private CategoryService categoryService;
   private Category category = new Category();

   @Autowired
   private CartService cartService;
   private Cart cart = new Cart();
   
   @Autowired
   private GoodsService goodsService;
   private Goods goods = new Goods();
 
   Map<String, Object> map = new HashMap();
 
   @RequestMapping({"/main/ctgAddjsp.html"})
   public ModelAndView addjsp()
   {
     return new ModelAndView("main/category/add");
   }
 
   @ResponseBody
   @RequestMapping({"/main/ctgInsert.html"})
   public String insert(String ctg_name, String ctg_img, Integer status, Integer sort) {
     this.map.put("ctg_name", ctg_name);
     this.map.put("ctg_img", ctg_img);
     this.map.put("status", Integer.valueOf(1));
     this.map.put("sort", Integer.valueOf(0));
     return String.valueOf(categoryService.insert(map));
   }
   @ResponseBody
   @RequestMapping({"/main/ctgUpdate.html"})
   public String update(Integer ctg_id, String ctg_name) { this.map.put("ctg_name", ctg_name);
     this.map.put("ctg_id", ctg_id);
     return String.valueOf(this.categoryService.update(this.map)); }
   
   /** 后台控制页面中 分类查看删除产品 */
   @ResponseBody
   @RequestMapping({"/main/ctgUpstatus.html"})
   public String upstatus(Integer ctg_id, Integer status) {
     this.map.put("status", status);
     this.map.put("ctg_id", ctg_id);
     return String.valueOf(this.categoryService.upstatus(this.map));
   }
   @ResponseBody
   @RequestMapping({"/main/ctgSort.html"})
   public String sort(Integer ctg_id, Integer sort) {
     this.map.put("sort", sort);
     this.map.put("ctg_id", ctg_id);
     return String.valueOf(this.categoryService.sort(this.map));
   }
 
   @RequestMapping({"/main/ctgList.html"})
   public ModelAndView list(Integer status) {
     this.category.setStatus(status);
     List list = this.categoryService.list(this.category);
     ModelAndView ml = new ModelAndView();
     ml.addObject("list", list);
     ml.setViewName("main/category/list");
     return ml;
   }
   @RequestMapping({"/main/ctgListById.html"})
   public ModelAndView listById(Integer ctg_id) {
     this.category.setCtg_id(ctg_id);
     List list = this.categoryService.listById(this.category);
     ModelAndView ml = new ModelAndView();
     ml.addObject("list", list);
     ml.setViewName("main/category/info");
     return ml;
   }
   
   /** 处理分类请求 */
   @RequestMapping({"/page/category.html"})
   public ModelAndView ctgList(@RequestParam(defaultValue="0") Integer ctg_id, HttpSession session) {
     this.category.setStatus(Integer.valueOf(1));
     List ctgList = this.categoryService.list(this.category);
     List goodsList = new ArrayList();
     if (ctgList.size() > 0) {
       this.goods.setStatus(Integer.valueOf(1));
       if (ctg_id.intValue() == 0) {
         ctg_id = ((Category)ctgList.get(0)).getCtg_id();
       }
       this.goods.setCtg_id(ctg_id);
       this.goods.setType(Integer.valueOf(1));
       goodsList = this.goodsService.list(this.goods);
     }
     ModelAndView ml = new ModelAndView();
     ml.addObject("ctg_id", ctg_id);
     ml.addObject("ctgList", ctgList);
     ml.addObject("goodsList", goodsList);
     ml.setViewName("page/category");
     String open_id = getOppen_id(session);
     this.cart.setOppen_id(open_id);
     int cart_num = this.cartService.goodstotalnum(this.cart);
     session.setAttribute("cart_num", cart_num);
     return ml;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.controller.CategoryCtrl
 * JD-Core Version:    0.6.2
 */