package com.yucheng.cmis.biz01line.fnc.op.fncstatcommon;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatBsComponent;
import com.yucheng.cmis.biz01line.fnc.master.component.FncStatCommonComponent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.RptItemData;
import com.yucheng.cmis.log.CMISLog;
import com.yucheng.cmis.message.CMISMessageManager;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;

/**
 *@Classname	Delete.java
 *@Version 1.0	
 *@Since   1.0 	Oct 15, 2008 11:03:21 AM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class DeleteFncStatCommonOp  extends CMISOperation{

	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT;
		 Connection connection = null;
		
		try {
		    connection = this.getConnection(context);
			String cusId 		= 	(String) context.getDataValue("cus_id");			//客户ID
			String statPrd		=   (String) context.getDataValue("stat_prd");			//报表期间
			String statPrdStyle =	(String) context.getDataValue("stat_prd_style");	//报表类型
			String tableName	=	(String) context.getDataValue("fnc_name");			//表名
			String styleId		=  	(String) context.getDataValue("style_id");			//报表样式编号
			String statFlag = (String) context.getDataValue("state_flg");				//状态
			
			int fncConfDataColumn	=	Integer.parseInt((String)context.
												getDataValue("fnc_conf_data_column")); //期数数
			
			/*
			 * 创建业务处理组件，并初始化
			 */
			 
			FncStatCommonComponent  fsCommonComponent = (FncStatCommonComponent)CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATCOMMON,context, connection);
			
			
			String statYear = statPrd.substring(0, 4);
			String statMonth = statPrd.substring(4, 6);
			String itemId = null;
			
			
			RptItemData riData = new RptItemData(cusId, tableName, statPrdStyle, statYear,
					statMonth, itemId, fncConfDataColumn);
					
			
			//新增一张报表数据
			flagInfo = fsCommonComponent.removeFncStat(riData);
			
			
			
			
			/*
			 * 设置返回参数
			 */
			
			//构件业务处理类
			FncStatBsComponent fncStatBsComponent = (FncStatBsComponent)CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCSTATBS,context, connection);
			
			
			/**
			 * 获取到财报的对象,将财报对象放入到context中
			 */
			FncStatBase pp = fncStatBsComponent.findOneFncStatBase(cusId, statPrdStyle, statPrd);
			FncConfStyles fc = fncStatBsComponent.findOneFncConfStyles(cusId, statPrdStyle, statPrd, styleId);
			
			context.clear();
			
			context.removeDataElement(CMISConstance.CMIS_RPTSTYLE);
			context.addDataField(CMISConstance.CMIS_RPTSTYLE, fc);
			context.addDataField(CMISConstance.CMIS_FNCSTATBASE, pp);
			
			context.setDataValue("cus_id",cusId);
			context.setDataValue("stat_prd",statPrd);
			context.setDataValue("stat_prd_style",statPrdStyle);
			context.setDataValue("fnc_name",tableName);
			context.setDataValue("style_id",styleId);
			context.setDataValue("fnc_conf_data_column",(String)context.
					getDataValue("fnc_conf_data_column"));
			context.setDataValue("state_flg",statFlag);
			
		}catch(CMISException e){
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			String message = CMISMessageManager.getMessage(e);
			CMISLog.log(context, CMISConstance.CMIS_PERMISSION, CMISLog.ERROR, 0, message);
			throw e;
		}catch(Exception e){
	                throw new EMPException(e);
	        } finally {
	                if (connection != null)
	                        this.releaseConnection(context, connection);
	        }

		return flagInfo;
	}
	
}
