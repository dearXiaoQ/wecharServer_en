 package com.yq.entity;
 
 public class Order extends Page
 {
   int order_id;
   String goods_id;
   String goods_name;
   String goods_img;
   String goods_spe;
   String goods_price;
   String goods_num;
   Float goods_total;
   Integer goods_total_num;
   String addr_name;
   String receive;
   Integer cps_id;
   String cps_name;
   Float cps_price;
   String oppen_id;
   String add_time;
   String start_time;
   String end_time;
   String ctg_name;
   String realname;
   String note;
   int status;
  
   	
   
   public int getOrder_id()
   {
     return this.order_id;
   }
   public void setOrder_id(int order_id) {
     this.order_id = order_id;
   }
   public String getGoods_id() {
     return this.goods_id;
   }
   public void setGoods_id(String goods_id) {
     this.goods_id = goods_id;
   }
   public String getGoods_name() {
     return this.goods_name;
   }
   public void setGoods_name(String goods_name) {
     this.goods_name = goods_name;
   }
   public String getGoods_img() {
     return this.goods_img;
   }
   public void setGoods_img(String goods_img) {
     this.goods_img = goods_img;
   }
   public String getGoods_price() {
     return this.goods_price;
   }
   public void setGoods_price(String goods_price) {
     this.goods_price = goods_price;
   }
   public String getGoods_num() {
     return this.goods_num;
   }
   public void setGoods_num(String goods_num) {
     this.goods_num = goods_num;
   }
 
   public Integer getCps_id() {
     return this.cps_id;
   }
   public void setCps_id(Integer cps_id) {
     this.cps_id = cps_id;
   }
   public String getCps_name() {
     return this.cps_name;
   }
   public void setCps_name(String cps_name) {
     this.cps_name = cps_name;
   }
   public Float getCps_price() {
     return this.cps_price;
   }
   public void setCps_price(Float cps_price) {
     this.cps_price = cps_price;
   }
   public String getOppen_id() {
     return this.oppen_id;
   }
   public void setOppen_id(String oppen_id) {
     this.oppen_id = oppen_id;
   }
   public Float getGoods_total() {
     return this.goods_total;
   }
   public void setGoods_total(Float goods_total) {
     this.goods_total = goods_total;
   }
   public Integer getGoods_total_num() {
     return this.goods_total_num;
   }
   public void setGoods_total_num(Integer goods_total_num) {
     this.goods_total_num = goods_total_num;
   }
   public String getAddr_name() {
     return this.addr_name;
   }
   public void setAddr_name(String addr_name) {
     this.addr_name = addr_name;
   }
   public String getReceive() {
     return this.receive;
   }
   public void setReceive(String receive) {
     this.receive = receive;
   }
   public String getGoods_spe() {
     return this.goods_spe;
   }
   public void setGoods_spe(String goods_spe) {
     this.goods_spe = goods_spe;
   }
   public String getAdd_time() {
     return this.add_time;
   }
   public void setAdd_time(String add_time) {
     this.add_time = add_time;
   }
   public int getStatus() {
     return this.status;
   }
   public void setStatus(int status) {
     this.status = status;
   }
   public String getRealname() {
     return this.realname;
   }
   public void setRealname(String realname) {
     this.realname = realname;
   }
   public String getStart_time() {
     return this.start_time;
   }
   public void setStart_time(String start_time) {
     this.start_time = start_time;
   }
   public String getEnd_time() {
     return this.end_time;
   }
   public void setEnd_time(String end_time) {
     this.end_time = end_time;
   }
   public String getCtg_name() {
     return this.ctg_name;
   }
   public void setCtg_name(String ctg_name) {
     this.ctg_name = ctg_name;
   }
   public String getNote() {
     return this.note;
   }
   public void setNote(String note) {
     this.note = note;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.entity.Order
 * JD-Core Version:    0.6.2
 */