package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo.cusgrpmember;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckLmtApplyAndLmtModApp extends CMISOperation{

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String grp_no_value = null;
			context.put("backMsg","-1");
			try {
				grp_no_value = (String)context.getDataValue("grp_no");
			} catch (Exception e) {}
			/**
			 * 判断该集团是否有正在进行的关联授信或是授信变更操作
			 */
			TableModelDAO dao = this.getTableModelDAO(context);
			//关联变更授信业务
			IndexedCollection indexColl2 = dao.queryList("LmtAppGrp", " where grp_no ='"+grp_no_value+"' and app_type='02' and approve_status in('000','111','992','991')", connection);
			if(indexColl2.size()>0){
				context.put("backMsg", "1");
			}
			//关联授信业务
			IndexedCollection indexColl1 = dao.queryList("LmtAppGrp", " where grp_no ='"+grp_no_value+"' and app_type='01' and approve_status in('000','111','992','991')", connection);
			if(indexColl1.size()>0){
				context.put("backMsg", "0");
			}
			/**
			 * 关联集团客户变更
			 */
			//关联授信业务
			IndexedCollection indexColl3 = dao.queryList("CusGrpInfoApply", " where grp_no ='"+grp_no_value+"' and approve_status in('000','111','992','991')", connection);
			if(indexColl3.size()>0){
				context.put("backMsg", "3");
			}
			/**add by lisj 2015-5-20 需求编号：XD150504034 贷后管理常规检查任务改造,判断关联集团是否存在贷后任务 begin**/
			IndexedCollection indexColl4 = dao.queryList("PspCheckTask", " where grp_no ='"+grp_no_value+"' and approve_status in('000','111','992','991')", connection);
			if(indexColl4.size()>0){
				context.put("backMsg", "4");
			}
			/**add by lisj 2015-5-20 需求编号：XD150504034 贷后管理常规检查任务改造,判断关联集团是否存在贷后任务 end**/
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
