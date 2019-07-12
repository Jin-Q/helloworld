package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtGuar.LmtGuarComponent;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteRLmtGuarContRecordOp extends CMISOperation {

	private final String modelId = "RLmtAppGuarCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String limit_code = null;
			int count = 1;
			String guar_cont_no = null;
			String guar_lvl = null;
			String guar_cont_state = null;
			String serno = null;
			try { 
				limit_code = (String)context.getDataValue("limit_code");
				serno = (String)context.getDataValue("serno");  //新加流水号 做组合主键
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
				guar_lvl = (String)context.getDataValue("guar_lvl"); 
				guar_cont_state = (String)context.getDataValue("guar_cont_state");   
			} catch (Exception e) {} 
			if(limit_code == null || limit_code.length() == 0 || guar_cont_no == null || guar_cont_no.length() == 0)
				throw new EMPJDBCException("The value of pk cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			/**删除授信担保合同数据后，下面一条授信担保合同的等级相应-1*/  
			Map<String,String> updateMap = new HashMap<String,String>(); 
			updateMap.put("LIMIT_CODE", limit_code);
			updateMap.put("GUAR_LVL", guar_lvl); 
			LmtGuarComponent cmisComponent = (LmtGuarComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtRGuar", context, connection);
         	int rels = cmisComponent.updateRLmtGuarLvlYB(updateMap, connection);
			if(rels < 0){ 
				throw new EMPJDBCException("update failed!");  
			}   
			
			/**如果授信担保合同关系表中数据是一条，且担保合同状态为登记状态,则可以删除担保合同记录*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IqpServiceInterface iqpService = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			int num = service.checkRLmtAppGuarContNum(guar_cont_no, context, connection);
			int iqpNum = iqpService.checkGetLoanRGurNum(guar_cont_no, context, connection);
			if(num == 1 && iqpNum == 0 && guar_cont_state.equals("00")){
				GrtServiceInterface grtService = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				count = grtService.deleteGuarContInfo("'"+guar_cont_no+"'", connection, context); 
			}
			
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("limit_code", limit_code);
			pkMap.put("serno", serno);   //新加流水号 做组合主键
			pkMap.put("guar_cont_no", guar_cont_no);
			int countRel=dao.deleteByPks(modelId, pkMap, connection);
			if(countRel == 1 && count == 1){ 
				context.addDataField("flag", "success"); 
			}      
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error"); 
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
