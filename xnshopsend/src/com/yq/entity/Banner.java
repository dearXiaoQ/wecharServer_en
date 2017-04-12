 package com.yq.entity;
 
 public class Banner
 {
   Integer ban_id;
   String ban_img;
   Integer sort;
   Integer status;
   String url;
   Integer type;
 
   public Integer getBan_id()
   {
     return this.ban_id;
   }
   public void setBan_id(Integer ban_id) {
     this.ban_id = ban_id;
   }
   public String getBan_img() {
     return this.ban_img;
   }
   public void setBan_img(String ban_img) {
     this.ban_img = ban_img;
   }
   public Integer getSort() {
     return this.sort;
   }
   public void setSort(Integer sort) {
     this.sort = sort;
   }
   public Integer getStatus() {
     return this.status;
   }
   public void setStatus(Integer status) {
     this.status = status;
   }
   public String getUrl() {
     return this.url;
   }
   public void setUrl(String url) {
     this.url = url;
   }
   public Integer getType() {
     return this.type;
   }
   public void setType(Integer type) {
     this.type = type;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.entity.Banner
 * JD-Core Version:    0.6.2
 */