package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-8-24
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class CheckBizModifyProcessOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno = "";//出账流水号
			String cont_no= "";

			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}

			if(serno == null || serno.trim().length() == 0){
				throw new EMPException("获取出账流水号异常！");
			}
			if(cont_no == null || cont_no.trim().length() == 0){
				throw new EMPException("获取合同编号异常！");
			}
			//查询合同/协议备份信息
			TableModelDAO dao  = this.getTableModelDAO(context);
			IndexedCollection temp = dao.queryList("PvpBizModifyRel", "where biz_serno='"+serno
									+"' and cont_no='"+cont_no+"' and approve_status in('000','111','992','993')", connection);
			if(temp!=null && temp.size()>0){
				context.addDataField("flag", "forbidden");
			}else{
				context.addDataField("flag", "success");
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
