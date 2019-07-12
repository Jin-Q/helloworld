package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.component.IqpAppendTermsComponent;
import com.yucheng.cmis.biz01line.cont.pub.ContConstant;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class RemoveCtrLoanContRecordOp extends CMISOperation {
	private final String modelId = "CtrLoanCont";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	private final String cont_no_name = "cont_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			String guar_cont_no_str = "";
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			DataSource datasource =(DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			
	 			String condition = "where cont_no='"+cont_no_value+"'";
				IndexedCollection iColl = dao.queryList(modelIdGrtLoan, condition, connection);
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
					guar_cont_no_str += "'"+guar_cont_no+"',";
				}
				if(guar_cont_no_str.length()>1){
					guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
					/**调用担保模块接口,跟新担保合同状态*/ 
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
				    service.updateGrtGuarCont4RemoveCont(guar_cont_no_str, datasource, connection);
				} 
				
	            //更新业务担保合同状态
				IqpAppendTermsComponent cmisComponent = (IqpAppendTermsComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(ContConstant.IQPAPPENDTREMSCCMPONTENT, context, connection);
				cmisComponent.updateGrtLoanRGur(cont_no_value,datasource, connection);
				
				//跟新合同状态
	            KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
				kColl.setDataValue("cont_status", "800");//撤销操作把合同状态改为作废
				dao.update(kColl, connection);
				
				//增加贴现处理
				String prd_id = (String) kColl.getDataValue("prd_id");
				String serno = (String) kColl.getDataValue("serno");
				KeyedCollection kCollForBatch = null;
				if(prd_id.equals("300021") || prd_id.equals("300020")){//判断是否贴现业务
					String conditionstr = " where serno='"+serno+"'";
					kCollForBatch=dao.queryFirst("IqpBatchMng", null, conditionstr, connection);
					if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
						//kCollForBatch.setDataValue("cont_no", "");//清掉批次与合同关系
						//kCollForBatch.setDataValue("serno", "");//清掉批次与申请关系
						kCollForBatch.setDataValue("status", "04");//改为【作废】状态
						dao.update(kCollForBatch, connection);
					}
				}
				
				context.addDataField("flag", "success");
	      
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
