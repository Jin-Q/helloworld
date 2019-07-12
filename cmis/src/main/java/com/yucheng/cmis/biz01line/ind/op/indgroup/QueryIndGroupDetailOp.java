package com.yucheng.cmis.biz01line.ind.op.indgroup;

import java.sql.Connection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.shuffle.rule.RuleBase;
import com.ecc.shuffle.rule.RuleSet;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIndGroupDetailOp extends CMISOperation {
	
	 private static final Logger logger = Logger.getLogger(QueryIndGroupDetailOp.class);
	 
	//扄1�7要操作的表模垄1�7
	private final String modelId = "IndGroup";
	
	//扄1�7要操作的表模型的主键
	private final String group_no_name = "group_no";
	
	/**
	 * 执行查询详细信息操作
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			//获得查询霄1�7要的主键信息
			String group_no_value = null;
			try {
				group_no_value = (String)context.getDataValue(group_no_name);
			} catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
			if(group_no_value == null || group_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+group_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, group_no_value, connection);
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
			String[] args=new String[] { "trans_id" ,"cus_id"};
			String[] modelIds=new String[]{"SfTrans","CusBase"};
			String[] modelForeign=new String[] { "trans_id" ,"cus_id"};
			String[] fieldName=new String[]{"trans_name","cus_name"};
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			this.putDataElement2Context(kColl, context);
			
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
