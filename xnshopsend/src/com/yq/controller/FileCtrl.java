 package com.yq.controller;
 
 import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
 
 @Controller
 @RequestMapping({"/main"})
 public class FileCtrl
 {
   @ResponseBody
   @RequestMapping({"/upload.html"})
   public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request)
   {
	   System.out.println("file.toString = " + file.toString());
	   String realpath = request.getSession().getServletContext().getRealPath("");
	   String path = realpath.substring(0, realpath.lastIndexOf("\\")) + "/upload";
 
     String fileName = new Date().getTime() + ".png";
 
     File targetFile = new File(path, fileName);
     if (!targetFile.exists()) {
       targetFile.mkdirs();
     }
     String url = path+"/"+fileName;
     try
     {
       if(file!=null){
    	   url = request.getContextPath() + "/../upload/" + fileName;
    	   file.transferTo(targetFile);
       }
      
     } catch (Exception e) {
       e.printStackTrace();
     }
    
 
     return url;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.controller.FileCtrl
 * JD-Core Version:    0.6.2
 */