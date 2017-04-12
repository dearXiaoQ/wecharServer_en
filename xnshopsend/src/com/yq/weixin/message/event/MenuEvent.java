 package com.yq.weixin.message.event;
 
 public class MenuEvent extends BaseEvent
 {
   private String EventKey;
 
   public String getEventKey()
   {
     return this.EventKey;
   }
 
   public void setEventKey(String eventKey) {
     this.EventKey = eventKey;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.message.event.MenuEvent
 * JD-Core Version:    0.6.2
 */