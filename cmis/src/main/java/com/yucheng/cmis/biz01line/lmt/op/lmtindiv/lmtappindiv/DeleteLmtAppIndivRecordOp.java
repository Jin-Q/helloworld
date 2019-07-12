package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.ccr.msi.CcrServiceInterface;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteLmtAppIndivRecordOp extends CMISOperation {

	private final String modelId = "LmtAppIndiv";
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			//判断分项下的担保合同是否可以删除
			String guar_cont_no = "";
//			int i = 0 ;
			IndexedCollection iColl_RLmtAppGuarCont = dao.queryList("RLmtAppGuarCont", " WHERE LIMIT_CODE IN(SELECT LIMIT_CODE FROM LMT_APP_DETAILS WHERE SERNO='"+serno_value+"')", connection);
			for (Iterator iterator = iColl_RLmtAppGuarCont.iterator(); iterator.hasNext();) {
				KeyedCollection obj = (KeyedCollection) iterator.next();
				//新增状态明为可以删除担保合同
				if("1".equals(obj.getDataValue("corre_rel")) || "2".equals(obj.getDataValue("corre_rel"))){
					guar_cont_no += "'"+(String)obj.getDataValue("guar_cont_no")+ "',";
				}
//				if(i < iColl_RLmtAppGuarCont.size()-1){
//					guar_cont_no += ",";
//				}
//				i++;
			}
			/**调用担保模块接口*/
			if(!"".equals(guar_cont_no)){  //担保合同编号不为空时说明有新增的担保合同
				guar_cont_no = guar_cont_no.lastIndexOf(",")>0?guar_cont_no.substring(0,guar_cont_no.length()-1):guar_cont_no;
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				//删除担保合同及担保合同与押品关系
				service.deleteGuarContInfo(guar_cont_no, connection, context);
			}
			
			//删除担保合同分项及分项与担保合同关系
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			lmtComponent.deleteLmtApplyDetailsBySerno(serno_value);
			
			//调用接口删除对应评级申请信息
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CcrServiceInterface service = (CcrServiceInterface)serviceJndi.getModualServiceById("ccrServices", "ccr");
			service.deleteCcrAppByLmtSerno(serno_value, context, connection);
			
			//调用流程接口删除流程实例
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance()
			.getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID); 
			wfi.wfDelInstance(serno_value, modelId, connection);

			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", ee.getMessage());
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", e.getMessage());
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
