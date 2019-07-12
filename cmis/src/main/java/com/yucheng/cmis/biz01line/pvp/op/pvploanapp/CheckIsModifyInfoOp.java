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
public class CheckIsModifyInfoOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String serno = "";//出账流水号
			String cont_no= "";
			String biz_cate="";
			String modify_rel_serno ="";
			String approve_status ="";
			String prd_id ="";
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			if(context.containsKey("biz_cate")){
				biz_cate = (String)context.getDataValue("biz_cate");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			if(context.containsKey("approve_status")){
				approve_status = (String)context.getDataValue("approve_status");
			}
			if(context.containsKey("prd_id")){
				prd_id = (String)context.getDataValue("prd_id");
			}
			if(serno == null || serno.trim().length() == 0){
				throw new EMPException("获取业务流水号异常！");
			}
			if(cont_no == null || cont_no.trim().length() == 0){
				throw new EMPException("获取合同编号异常！");
			}
			if(biz_cate == null || biz_cate.trim().length() == 0){
				throw new EMPException("获取业务类型异常！");
			}
			if(modify_rel_serno == null || modify_rel_serno.trim().length() == 0){
				throw new EMPException("获取modify_rel_serno号异常！");
			}
			if(approve_status == null || approve_status.trim().length() == 0){
				throw new EMPException("获取approve_status号异常！");
			}
			if(prd_id == null || prd_id.trim().length() == 0){
				throw new EMPException("获取prd_id号异常！");
			}
			//查询合同/协议备份信息
			TableModelDAO dao  = this.getTableModelDAO(context);
			IndexedCollection temp = new IndexedCollection();
			if(!"998".equals(approve_status)){
				if("016".equals(biz_cate)){
					temp = dao.queryList("IqpExtensionAgrHis", "where modify_rel_serno='"+modify_rel_serno+"'", connection);
				}else{
					temp = dao.queryList("CtrLoanContHis", "where modify_rel_serno='"+modify_rel_serno+"'", connection);	
				}
				if(temp!=null && temp.size()>0){
					if(!"".equals(prd_id) && ("200024".equals(prd_id) || "400020".equals(prd_id) || "400021".equals(prd_id))){
						KeyedCollection kColl4CLCT = dao.queryDetail("CtrLoanContTmp", modify_rel_serno, connection);
						String risk_open_amt = (String)kColl4CLCT.getDataValue("risk_open_amt");
						if(risk_open_amt==null || "".equals(risk_open_amt)){
							context.addDataField("flag", "riskOANone");
						}else{
							context.addDataField("flag", "success");
						}
						
					}else{
						context.addDataField("flag", "success");
					}
				}else{
					context.addDataField("flag", "limited");
				}
			}else{
				context.addDataField("flag", "forbidden");
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
