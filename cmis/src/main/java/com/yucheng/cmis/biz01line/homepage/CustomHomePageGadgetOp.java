package com.yucheng.cmis.biz01line.homepage;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * 用户定制的gadget添加、删除、查询操作，
 * 
 * @author JackYu
 *
 */
public class CustomHomePageGadgetOp extends CMISOperation{

	final String QUERY_GADGET = "query";
	final String MOVE_GADGET = "delete";
	final String ADD_GADGET = "add";
	final String SUCESS_FLAG = "sucess";
	final String ERROR_FLAG = "error";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		try {
			String operator = (String)context.getDataValue("operator");
			
			//查询该用户已定制的gadget
			if(operator!=null && operator.equals(QUERY_GADGET)) queryCustomHomePageGadget(context);
			
			//删除该用户定制的gadget
			else if(operator!=null && operator.equals(MOVE_GADGET)) moveCustomHomePageGadget(context);
			
			//添加该用户定制的gadget
			else if(operator!=null && operator.equals(ADD_GADGET)) addCustomHomePageGadget(context);
			
			
		}catch (EMPException e) {
			e.printStackTrace();
			context.addDataField("flag", ERROR_FLAG);
			throw new EMPException(e.getMessage());
		} 
		catch (Exception e) {
			context.addDataField("flag", ERROR_FLAG);
			e.printStackTrace();
		}
		
		return "0";
	}
	
	/**
	 * 查询用户订制的gadget
	 * @param context
	 * @throws EMPException
	 */
	private void queryCustomHomePageGadget(Context context) throws EMPException{
		HomePageComponet hpcom = new HomePageComponet();
		
		String userId = (String) context.get("currentUserId");
		DataSource dataSource = this.getDataSource(context);
		
		String gadgetStr = hpcom.queryCustomGadget(userId, dataSource);
		
		context.addDataField("gadgetStr", gadgetStr);
		context.addDataField("flag", SUCESS_FLAG);
	}
	
	/**
	 * 删除用户订制的gadget
	 * @param context
	 * @throws EMPException
	 */
	private void moveCustomHomePageGadget(Context context) throws EMPException{
		HomePageComponet hpcom = new HomePageComponet();
		
		String userId = null;
		String gadgetId = null;
		
		try {
			userId = (String) context.get("currentUserId");
			gadgetId = (String) context.get("gadgetId");
		} catch (Exception e) {
			throw new EMPException("gadgetId 为空");
		}
		
		
		DataSource dataSource = this.getDataSource(context);
		
		boolean flag = hpcom.moveCustomGadget(userId, gadgetId, dataSource);
		
		if(flag)
			context.addDataField("flag", SUCESS_FLAG);
	}
	
	/**
	 * 添加Gadget
	 * @param context
	 * @throws EMPException
	 */
	private void addCustomHomePageGadget(Context context) throws EMPException{
		HomePageComponet hpcom = new HomePageComponet();
		
		String userId = null;
		String gadgetId = null;
		
		try {
			userId = (String) context.get("currentUserId");
			gadgetId = (String) context.get("gadgetId");
		} catch (Exception e) {
			throw new EMPException("gadgetId 为空");
		}
		
		
		DataSource dataSource = this.getDataSource(context);
		
		boolean flag = hpcom.addCustomGadget(userId, gadgetId, dataSource);
		
		if(flag)
			context.addDataField("flag", SUCESS_FLAG);
	}

}
