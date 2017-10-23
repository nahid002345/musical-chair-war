package com.NahidAppSetting.musicalchairwar;

public class ListModel {
	 private String name;  
	 private String data;  
	 private String date;  
	 private String path;  
	 private String image;
 
	 public ListModel(String name,String date, String dt, String path, String image)  
	 {  
	  this.name = name;  
	  this.data = date;  
	  this.path = path;   
	  this.image = image;  
	    
	 }  
	 public String getName()  
	 {  
	  return name;  
	 }  
	 public String getData()  
	 {  
	  return data;  
	 }  
	 public String getDate()  
	 {  
	  return date;  
	 }  
	 public String getPath()  
	 {  
	  return path;  
	 }  
	 public String getImage() {  
	  return image;  
	 }  
	   
	 public int compareTo(ListModel o) {  
	  if(this.name != null)  
	   return this.name.toLowerCase().compareTo(o.getName().toLowerCase());   
	  else   
	   throw new IllegalArgumentException();  
	 }  
	 
}
