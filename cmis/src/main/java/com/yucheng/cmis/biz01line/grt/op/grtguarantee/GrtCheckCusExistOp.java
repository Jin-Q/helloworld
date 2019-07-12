package com.yucheng.cmis.biz01line.grt.op.grtguarantee;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 异步查询客户信息是否在明细表中存在OP类
 * @author Administrator
 *
 */
public class GrtCheckCusExistOp extends CMISOperation {

	private final String modelIdTee = "GrtGuarantee";//保证人表模型
	private final String modelIdCont = "GrtGuarCont";//担保合同类型

	private final String serno_name = "cus_id";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cusId = null;
			String guarContNo = null;
			try {
				cusId = (String)context.getDataValue("cus_id");
				guarContNo = (String) context.getDataValue("guar_cont_no");
			} catch (Exception e) {
				//EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "Kcoll["+modelIdCont+"] can't find in context");				
			}
			if(cusId == null || cusId.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelIdTee, "where guar_id in (select guaranty_id from grt_guaranty_re where guar_cont_no='"+guarContNo+"')", connection);
			KeyedCollection guarKc = dao.queryDetail(modelIdCont, guarContNo, connection);
		//	String cus_id = (String) guarKc.getDataValue("cus_id");//担保合同借款人编号
			String rel = (String) guarKc.getDataValue("rel");
			if("2".equals(rel)){//授信
				
			}else if("3".equals(rel)){//业务
				
			}else if("1".equals(rel)){//担保
				
			}
			if((iColl.size()>0)){   
				int count = 0;
				KeyedCollection kc = null;
				for(int i=0;i<iColl.size();i++){
					kc=(KeyedCollection) iColl.get(i);
					if(cusId.equals(kc.getDataValue("cus_id"))){
						context.addDataField("ccr_result", "false");
						context.addDataField("msg","此保证人信息已经存在，不能重复录入！");
						break;
					}else{
						count++;
					}
				}
				if(count==iColl.size()){
					context.addDataField("ccr_result","true");
					context.addDataField("msg","");
				}
			}else{
				context.addDataField("ccr_result","true");
				context.addDataField("msg","");
			}
		//	if(cus_id.equals(cusId)){
		//		context.setDataValue("ccr_result", "false");
		//		context.setDataValue("msg","所录入客户与借款人客户相同，请重新录入！");
		//	}	
			return null;
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

	}

}
