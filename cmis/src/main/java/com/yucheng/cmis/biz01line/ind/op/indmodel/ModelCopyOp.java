/*
 * Yangfei's New Files
 *
 * Create on 2009-3-25
 * Copyrigh 2005 Evergreen International Corp.
 */

package com.yucheng.cmis.biz01line.ind.op.indmodel;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;


public class ModelCopyOp extends CMISOperation{	
	
	private static final Logger logger = Logger.getLogger(ModelCopyOp.class);
	
	/**
	 * 组件ID
	 */
	private final static String componentID = IndPubConstant.IND_COMPONENT;
	
	/**
	 * 组件对象
	 */
	private IndComponent indComponent;
	
	/**
	 * 模型编号
	 */
	private final String model_no_name = "model_no";
	
	/**
	 * 
	 * 模型ID
	 * 
	 */
	
	public String doExecute(Context context) throws EMPException {
    
    //获取需要复制的模型编号
    String model_no_value = null;
	try {
		model_no_value = (String)context.getDataValue(model_no_name);
	} catch (Exception e) 
	{
		logger.error(e.getMessage(), e);
	}
	if(model_no_value == null || model_no_value.length() == 0)
		throw new EMPException("The value of pk["+model_no_name+"] cannot be null!");
	
	//获取自动生成的新的模型编号
	String key_model_no="";
	Connection connection = null;
	try {
			connection = this.getConnection(context);
			// 从context中取出sequenceService

			// 调用生成方法
			key_model_no = CMISSequenceService4JXXD.querySequenceFromDB("M", "fromDate", 
	        		connection, context);

			// 对模型进行复制

			// 构建业务处理类
			indComponent = (IndComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							componentID, context,connection);
			indComponent.modelCopy(key_model_no, model_no_value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";

	}

}
