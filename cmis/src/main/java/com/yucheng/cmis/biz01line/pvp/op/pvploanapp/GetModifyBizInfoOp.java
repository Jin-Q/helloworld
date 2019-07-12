package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class GetModifyBizInfoOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cont_no= "";
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			if(cont_no == null || cont_no.trim().length() == 0){
				throw new EMPException("获取合同编号异常！");
			}
			//查询是否存在在途业务修改信息
			TableModelDAO dao  = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList("PvpBizModifyRel", "where cont_no ='"+cont_no+"' and approve_status not in('997','998')", connection);
			if(iColl!=null && iColl.size()>0){
				context.addDataField("flag", "update");//直接进行维护操作
				String modify_rel_serno = (String)((KeyedCollection) iColl.get(0)).getDataValue("modify_rel_serno");
				context.addDataField("modify_rel_serno", modify_rel_serno);
			}else{
				//进行新增操作
				context.addDataField("flag", "add");
				context.addDataField("modify_rel_serno", "none");
			}
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}

}
