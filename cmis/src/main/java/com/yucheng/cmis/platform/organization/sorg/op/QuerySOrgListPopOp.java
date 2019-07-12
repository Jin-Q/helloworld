package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySOrgListPopOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SOrg";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false)
                                                                        +"order by organno desc";
			String loginOrgID = null;
			String orgListType = null;
			try {
				loginOrgID = context.getDataValue("organNo").toString();
				orgListType = context.getDataValue("orgListType").toString();
			} catch (Exception e) {}

			String orderStr = " order by organno asc";
			String orgStr = null;
			if(loginOrgID != null && orgListType != null){
				orgStr = buildOrgStr(loginOrgID,orgListType);
				if(orgStr == null || orgStr.trim().length() == 0){
				}else{
					conditionStr = conditionStr + orgStr + orderStr;
				}
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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

	/**
	 * 根据机构编号、展现类型获得查询语句
	 * @param orgID 机构编号
	 * @param orgListType 展现类型（0-展现自己，1-展现自己及下级，2-展现所有)
	 * @return
	 */
	private String buildOrgStr(String orgID,String orgListType){
		String orgStr = null;
		if("0".equals(orgListType))
			orgStr = " organno = '" + orgID + "'";
		else if("1".equals(orgListType))
			orgStr = " organno like '" + orgID.substring(0,3) + "%'";
		return orgStr;
	}
}
