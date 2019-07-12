package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrRatDirect;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
/**
 *@Classname	StartCcrDirectAppOp.java
 *@Version 1.0	
 *@Since   1.0 	Mar 1, 2010 
 *@Copyright 	yuchengtech
 *@Author 		eric
 *@Description：评级直接认定提交校验类
 *@Lastmodified 
 *@Author	    
 */
public class StartCcrDirectAppOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno = context.getDataValue("serno").toString();
			CcrComponent ccrComponent = (CcrComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					CcrPubConstant.CCR_COMPONENT, context, connection);
			//		取评级直接认定申请信息
			CcrRatDirect crd = ccrComponent.getCcrRatDirectAppInfoBySerno(serno);
			context.addDataField("serNo", serno);
			context.addDataField("cusId", crd.getCusId());
			context.addDataField("cusName", crd.getCusName());
			context.addDataField("comScaleCcr", crd.getComScaleCcr());
			context.addDataField("grade", crd.getGrade());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally{
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
