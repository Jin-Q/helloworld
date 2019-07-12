package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryPrdOrgApplyListByPrdPkOp extends CMISOperation {
private final String modelId = "PrdOrgApply";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String prdPk = null;
			IndexedCollection prdOrgApplyIcoll = null;
			try {
				prdPk = (String)context.getDataValue("prd_pk");
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取产品主键失败！");
				throw new EMPJDBCException("获取产品主键失败！");
			}
			if(prdPk == null || "".equals(prdPk)){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "获取产品主键为空！");
				throw new EMPJDBCException("获取产品主键为空！");
			}
			String conditionStr = "where prd_pk = '"+prdPk.trim()+"'";
			int size = 15;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			try{
				prdOrgApplyIcoll = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			}catch(Exception e){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "查询产品适用机构失败！");
				throw new EMPJDBCException("查询产品适用机构失败！");
			}
			
			//prdOrgApplyIcoll.setName("PrdOrgApplyList");
			prdOrgApplyIcoll.setName(prdOrgApplyIcoll.getName()+"List");
			this.putDataElement2Context(prdOrgApplyIcoll, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "查询产品适用机构失败！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "查询产品适用机构失败！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}	
		return null;
	}
}
