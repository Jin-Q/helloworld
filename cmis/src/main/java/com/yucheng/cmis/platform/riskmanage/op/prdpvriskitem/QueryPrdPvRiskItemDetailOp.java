package com.yucheng.cmis.platform.riskmanage.op.prdpvriskitem;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryPrdPvRiskItemDetailOp  extends CMISOperation {
	
	private final String modelId = "PrdPvRiskItem";
	

	private final String item_id_name = "item_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String item_id_value = null;
			try {
				item_id_value = (String)context.getDataValue(item_id_name);
			} catch (Exception e) {}
			if(item_id_value == null || item_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+item_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, item_id_value, connection);
			String ruleRating=(String) kColl.getDataValue("item_rules");
			if(ruleRating != null && ruleRating.trim().length() > 0){
				//取得组评分规则翻译信息
				// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
						.getInstance();
				ShuffleServiceInterface shuffleService = (ShuffleServiceInterface) serviceJndi
						.getModualServiceById("shuffleServices",
								"shuffle");
				String ruleInfo=shuffleService.getRuleInfoDesc(ruleRating);
				
				kColl.addDataField("item_rules_displayname",ruleInfo);
			}
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
