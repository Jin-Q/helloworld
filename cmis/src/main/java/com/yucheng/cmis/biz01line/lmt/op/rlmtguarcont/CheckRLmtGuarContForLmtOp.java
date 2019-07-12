package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckRLmtGuarContForLmtOp extends CMISOperation {
	
	//operation TableModel
	private final String appmodelId = "RLmtAppGuarCont"; 
	private final String modelId = "RLmtGuarCont"; 
	/**
	 * 用于授信变更中引入最高额担保合同
	 * 逻辑：1、授信和担保合同关系表中没有挂关系的有效担保合同
	 *       或者
	 *       2、该协议下， 授信和担保合同关系表中有挂关系，但授信申请和担保合同关系表中不存在非解除关系的有效担保合同
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guar_cont_no ="";
			String limit_code = "";
			String serno = "";
			try {
				 guar_cont_no = (String)context.getDataValue("guar_cont_no");
				 limit_code = (String)context.getDataValue("limit_code");
				 serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(guar_cont_no == null || guar_cont_no=="")
				throw new EMPJDBCException("The values to ["+guar_cont_no+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context); 
			
			/**查询分项下是否已存在解除状态*/
			String conditionstr = " where guar_cont_no='"+guar_cont_no+"' and serno='"+serno+"' and limit_code = '"+limit_code+"'";
			IndexedCollection iColl = dao.queryList(appmodelId, conditionstr, connection);
			if(iColl.size()>0){
				context.addDataField("flag","error");
				context.addDataField("msg","该担保合同已与当前授信分项关联，请重新选择！");
				return "0";
			}
			
			/**查询此笔业务下是否已引入此担保合同*/
			String condition = "where guar_cont_no='"+guar_cont_no+"' and corre_rel <> '3'";
			IndexedCollection iCollCont =  dao.queryList(modelId, condition, connection);
			if(iCollCont.size()>0){
				String condition2 = " WHERE GUAR_CONT_NO='"+guar_cont_no+"' AND SERNO = '"+serno+"' AND CORRE_REL <> '3'";
				/**剔除与现有条线不一致的授信分项：公司条线业务能引用原小微条线的担保合同    2013-12-14 唐顺岩*/
				condition2 += " AND LIMIT_CODE NOT IN (SELECT ORG_LIMIT_CODE FROM LMT_APP_DETAILS ";
				//condition2 +=" WHERE SERNO = '"+serno+"' AND LMT_TYPE <> (SELECT LMT_TYPE FROM LMT_APPLY WHERE SERNO = '"+serno+"'))";
				condition2 +=" WHERE SERNO = '"+serno+"')";
				/** END */
				IndexedCollection appiColl =  dao.queryList(appmodelId, condition2, connection);
				if(appiColl.size() > 0){
					context.addDataField("flag","error");
					context.addDataField("msg","该担保合同已与其他授信分项关联，不能再次关联！");
				}else{
					context.addDataField("flag","success");
					context.addDataField("msg","");
				}
			}else{
				context.addDataField("flag","success");
				context.addDataField("msg","");
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
