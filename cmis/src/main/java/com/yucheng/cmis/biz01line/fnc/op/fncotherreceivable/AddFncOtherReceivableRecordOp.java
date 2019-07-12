package com.yucheng.cmis.biz01line.fnc.op.fncotherreceivable;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.detail.component.FncOthRecComponent;
import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOthReceive;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

/**
 * 
 *@Classname	AddFncOtherReceivableRecordOp.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 2:26:36 PM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class AddFncOtherReceivableRecordOp extends CMISOperation {
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		String flagInfo = CMISMessage.DEFEAT;  //错误编码信息
		Connection connection = null;
		try{
			FncOthReceive domain = new FncOthReceive();//数据容器
			connection = this.getConnection(context);

			//构件业务处理类
			FncOthRecComponent thisComponent = (FncOthRecComponent)CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance("FncOthRec",context, connection);

			
			/*
			 * 从context取出数据对象
			 */
			KeyedCollection kColl = (KeyedCollection)context.getDataElement(PUBConstant.FNCOTHERRECEIVABLE);
			ComponentHelper componetHelper = new ComponentHelper();
			domain = (FncOthReceive)componetHelper.kcolTOdomain(domain, kColl);

			//新增一条记录信息
			flagInfo = thisComponent.addFncDetOthRec(domain);
			
			//失败抛出错误提示信息
			if(flagInfo.equals(CMISMessage.DEFEAT)){
				throw new CMISException(CMISMessage.MESSAGEDEFAULT,"新增信息失败，请重新操作!");
			}
			
		}catch(CMISException e){
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			String message = CMISMessageManager.getMessage(e);
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
			throw e;
		} catch(Exception e){
	                throw new EMPException(e);
	        } finally {
	                if (connection != null)
	                        this.releaseConnection(context, connection);
	        }
		return flagInfo;
	}
	
}
