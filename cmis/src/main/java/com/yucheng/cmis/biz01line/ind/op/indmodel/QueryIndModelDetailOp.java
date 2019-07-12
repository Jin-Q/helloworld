package com.yucheng.cmis.biz01line.ind.op.indmodel;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryIndModelDetailOp extends CMISOperation {
	
	private static final Logger logger = Logger.getLogger(QueryIndModelDetailOp.class);
	
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndModel";
	
	//扄1�7要操作的表模型的主键
	private final String model_no_name = "model_no";
	
	/**
	 * 执行查询详细信息操作
	 */
	public String doExecute(Context context) throws EMPException {
		
	
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			//获得查询霄1�7要的主键信息
			String model_no_value = null;
			try {
				model_no_value = (String)context.getDataValue(model_no_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(model_no_value == null || model_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+model_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, model_no_value, connection);
			String ruleRating=(String) kColl.getDataValue("rating_rules");
			//取得组评分规则翻译信息
			// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
					.getInstance();
			ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices",
							"shuffle");
			String ruleInfo=shuffleService.getRuleInfoDesc(ruleRating);
			
			kColl.addDataField("rating_rules_displayname",ruleInfo);
			this.putDataElement2Context(kColl, context);

			condition=" where model_no='"+model_no_value+"'";
			IndexedCollection iColl_IndModelGroup = dao.queryList("IndModelGroup",condition, connection);
			this.putDataElement2Context(iColl_IndModelGroup, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}	
}
