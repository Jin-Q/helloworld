package com.yucheng.cmis.pub.sequence;

import com.ecc.emp.core.EMPException;

public class SequenceNotFoundException extends EMPException {
	  /**
	    */
	   public SequenceNotFoundException() 
	   {
	    
	   }
	 
	    
	   public SequenceNotFoundException(String message )
	   {
		   super( message );
	   }

	   public SequenceNotFoundException(String errorCode, String message )
	   {
		   super(errorCode, message );
	   }
	   
	   
	   /**
	    * @param message
	    * @param cause
	    */
	   public SequenceNotFoundException(String message, Throwable cause) 
	   {
		   super(message, cause);
	    
	   }

	   /**
	    * @param message
	    * @param cause
	    */
	   public SequenceNotFoundException(String errorCode, String message, Throwable cause) 
	   {
		   super(errorCode, message, cause);
	    
	   }
	   
	   
	   
	   /**
	    * @param cause
	    */
	   public SequenceNotFoundException(Throwable cause) 
	   {
		   super(cause);
	   }
	
}
