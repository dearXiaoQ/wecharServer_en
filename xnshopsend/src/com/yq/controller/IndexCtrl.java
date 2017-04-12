 package com.yq.controller;
 
 import com.yq.entity.Area;
import com.yq.entity.Banner;
import com.yq.entity.Cart;
import com.yq.entity.Category;
import com.yq.entity.Goods;
import com.yq.entity.Search;
import com.yq.entity.User;
import com.yq.service.AreaService;
import com.yq.service.BannerService;
import com.yq.service.CartService;
import com.yq.service.CategoryService;
import com.yq.service.GoodsService;
import com.yq.service.SearchService;
import com.yq.service.UserService;
import com.yq.util.StringUtil;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
 
 @Controller
 @RequestMapping({"/"})
 public class IndexCtrl extends StringUtil
 {
	 //https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc1c62269461a335d&redirect_uri=http://gmri.mastergroup.com.cn/xnshop/page/userInsert.html?url=http://gmri.mastergroup.com.cn/xnshop/page/index.html?code=001B6ag105UlRD1dbSf10lh6g10B6ags&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1&uin=MTIyMjAxNDYxMw%3D%3D&key=65f928c75b96cd024462f4d9715c033905f7aab1b11ceed506594a64b304349cdacdd3817b9c2d5826d4521636730baf&pass_ticket=DJl4g8sobnzXNWgXX3pKBKKUmSvC9PIWiY7rdMq4He7PEKfGN9r4Amyrmr8WbSLG3zmcJQiKeDVpUM+zWZrJ1Q==
   @Autowired
   private UserService userService;
   private User user = new User();
   
   @Autowired
   private SearchService searchService;
   private Search search = new Search();
   
   @Autowired
   private AreaService areaService;
   private Area area = new Area();
 
   @Autowired
   private CartService cartService;
   private Cart cart = new Cart();
 
   @Autowired
   private GoodsService goodsService;
   private Goods goods = new Goods();
 
   @Autowired
   private BannerService bannerService;
   private Banner banner = new Banner();
 
   @Autowired
   private CategoryService categoryService;
   private Category category = new Category();
   Map<String, Object> map = new HashMap();
 
   @RequestMapping({"/main/main.html"})
   public ModelAndView mainindex() {
	   System.out.println("进入 indexCterl.mainIndex()!" + System.currentTimeMillis());
     return new ModelAndView("main/index");
   }
 
   @RequestMapping({"/page/index.html"})
   public ModelAndView index(HttpSession session) {
	   System.out.println("进入 IndexCtrl.index方法! " + System.currentTimeMillis());
	   System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> here" );
     ModelAndView ml = new ModelAndView();
 
     this.goods.setType(Integer.valueOf(1));
     this.goods.setStatus(Integer.valueOf(1));
 
     this.banner.setType(Integer.valueOf(1));
     this.banner.setStatus(Integer.valueOf(1));
 
     this.goods.setIs_recommend(Integer.valueOf(1));
     List banList = this.bannerService.list(this.banner);
     this.banner.setType(Integer.valueOf(2));
     List advList = this.bannerService.list(this.banner);
     this.goods.setCtg_id(Integer.valueOf(0));
     List hotGoodsList = this.goodsService.list(this.goods);
     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + hotGoodsList.size());
     this.category.setStatus(Integer.valueOf(1));
     List ctgList = this.categoryService.list(this.category);
 
     for (int i = 0; i < ctgList.size(); i++) {
       this.goods.setIs_recommend(Integer.valueOf(0));
       this.goods.setCtg_id(((Category)ctgList.get(i)).getCtg_id());
       List goodsList = this.goodsService.list(this.goods);
       this.map.put("goodsList" + i, goodsList);
     }
 
     ml.addObject("map", this.map);
     ml.addObject("ctgList", ctgList);
     ml.addObject("banList", banList);
     ml.addObject("advList", advList);
     ml.addObject("hotGoodsList", hotGoodsList);
     String oppen_id = getOppen_id(session);
     this.cart.setOppen_id(oppen_id);
     int cart_num = this.cartService.goodstotalnum(this.cart);
     session.setAttribute("cart_num", Integer.valueOf(cart_num));
 
     ml.setViewName("page/index");
 
     return ml;
   }
   
   
/*   @RequestMapping({"page/se.html"})
   public ModelAndView searchlist(@RequestParam(defaultValue="1") Integer status) {
     this.search.setStatus(status);
     List list = this.searchService.list(this.search);
     ModelAndView ml = new ModelAndView();
     ml.addObject("list", list);
     ml.setViewName("page/search");
     return ml;
   }*/
   
 }

/* Location:           
 * Qualified Name:     com.yq.controller.IndexCtrl
 * JD-Core Version:    0.6.2
 */