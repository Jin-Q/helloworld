package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2015-3-16
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*@version v1.0
*
 */
public class QueryWfiLvCreditRightListOp extends CMISOperation {

	private final String modelId = "WfiLvCreditRight";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String right_type = "";//权限类型
			if(context.containsKey("right_type")){
				right_type = (String)context.getDataValue("right_type"); 
			}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			int size = 10;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			if(conditionStr !=null && !("".equals(conditionStr)) && !"".equals(right_type)){
				conditionStr += " and  right_type='"+right_type+"' order by org_lvl desc";
			}else if(!"".equals(right_type)) {
				conditionStr = "where right_type='"+right_type+"' order by org_id desc,org_lvl desc"; 
			}
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr, pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			SInfoUtils.addSOrgName(iColl, new String[]{"org_id"});
			if(!"".equals(right_type) && "02".equals(right_type)){
				return "1";
			}
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
