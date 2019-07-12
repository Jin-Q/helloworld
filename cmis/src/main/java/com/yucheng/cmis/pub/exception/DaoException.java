package com.yucheng.cmis.pub.exception;

public class DaoException extends ComponentException{
	  public DaoException(String message){
		   super(message);
	   }	
	   public DaoException(String id, String message){
		   super("业务组件[" + id + "] " + message);
	   }
	   public DaoException(String id, String message, Exception ex){
		   super("业务组件[" + id + "] " + message,ex);   
	   }
}
