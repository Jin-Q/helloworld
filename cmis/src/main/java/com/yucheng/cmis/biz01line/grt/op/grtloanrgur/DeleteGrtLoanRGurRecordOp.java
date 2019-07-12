package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.agent.IqpLoanAppAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DeleteGrtLoanRGurRecordOp extends CMISOperation {

	private final String modelIdRel = "GrtLoanRGur";

	private final String pk_id_name = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			int count=1;
			String pk_id_value = null;
			String guar_cont_no = null;
			String guar_cont_state = null; 
			String serno = null;
			String guar_lvl = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
				serno = (String)context.getDataValue("serno");
				guar_lvl = (String)context.getDataValue("guar_lvl");
				guar_cont_no = (String)context.getDataValue("guar_cont_no"); 
				guar_cont_state = (String)context.getDataValue("guar_cont_state");
			} catch (Exception e) {}  
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			
			
			/**删除业务担保合同数据后，下面一条业务担保合同的等级相应-1*/ 
//			if(!"".equals(listPk) && listPk != null){  
//				String[] list = listPk.split(",");
//				for(int i=0;i<list.length;i++){
//					String pk_id = list[i];
//					KeyedCollection kColl = dao.queryDetail(modelIdRel, pk_id, connection);
//					int guar_lvl =  Integer.parseInt(kColl.getDataValue("guar_lvl").toString());
//					guar_lvl = guar_lvl-1; 
//					kColl.setDataValue("guar_lvl", guar_lvl); 
//					dao.update(kColl, connection); 
//				}
//			} 
			Map<String,String> map = new HashMap<String,String>(); 
			map.put("serno", serno);
			map.put("guar_lvl", guar_lvl); 
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
         	int rels = cmisComponent.updateGrtLoanRGurLvlYB(map, connection);
			if(rels < 0){
				throw new EMPJDBCException("update failed!"); 
			}
			
			
			/**如果业务担保合同关系表中数据是一条且授信担保合同关系表中数据为0条，且担保合同状态为登记状态,则可以删除担保合同记录*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			LmtServiceInterface lmtService = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			int num = service.checkGetLoanRGurNum(guar_cont_no, context, connection);
			int lmtNum = lmtService.checkRLmtAppGuarContNum(guar_cont_no, context, connection);
			if(num == 1 && lmtNum ==0 && "00".equals(guar_cont_state)){
				GrtServiceInterface grtService = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				guar_cont_no = "'"+guar_cont_no+"'";
				count = grtService.deleteGuarContInfo(guar_cont_no, connection, context);
			}
			
			int countRel = dao.deleteByPk(modelIdRel, pk_id_value, connection); 
			if(countRel==1 && count==1){
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
