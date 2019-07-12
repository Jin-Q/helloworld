package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class UpdateGrtGuarContRecordOp extends CMISOperation {
	private final String modelId = "GrtGuarCont";
	private final String modelIdLmt = "RLmtGuarCont";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			KeyedCollection kCollRel = null;
			KeyedCollection kCollRelLmt = null;
			String rel = null;   
//			String relLmt = null;   
			try { 
				kColl = (KeyedCollection)context.getDataElement(modelId);
 			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0) 
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			/**ws添加 判断是否调用业务模块添加业务合同关系表接口------start------------------*/
			try {
				kCollRel = (KeyedCollection)context.getDataElement("GrtLoan");
//				kCollRelLmt = (KeyedCollection)context.getDataElement("RLmtGuar"); 
				rel = (String)kCollRel.getDataValue("rel");    
//				relLmt = (String)kCollRelLmt.getDataValue("rel");     
 			} catch (Exception e) {}
 			//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
 			if(kCollRel.containsKey("modify_rel_serno")&&kCollRel.getDataValue("modify_rel_serno")!=null&&!"".equals(kCollRel.getDataValue("modify_rel_serno"))){
				String modify_rel_serno = (String)kCollRel.getDataValue("modify_rel_serno");
				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
				TableModelDAO daotmp = this.getTableModelDAO(context);
				String conditionStr = "where biz_serno='"+modify_rel_serno+"' and guar_cont_no='"+guar_cont_no+"'";
				IndexedCollection selectIColl = daotmp.queryList("GrtLoanRGurTmp", conditionStr, connection); 
				//guar_amt );
				KeyedCollection kCollRelTmp = new KeyedCollection("GrtLoanRGurTmp");
				kCollRelTmp.put("biz_serno", modify_rel_serno);
				kCollRelTmp.put("guar_cont_no", guar_cont_no);
				kCollRelTmp.put("guar_amt",kCollRel.getDataValue("guar_amt"));
				if(selectIColl!=null&&selectIColl.size()>0){
					daotmp.update(kCollRelTmp, connection);
				}else{
					daotmp.insert(kCollRelTmp, connection);
				}
				context.addDataField("flag","success");
				return "0";
			}
 			//add by YAGNZY 2015-9-8  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 START
			if("ywRel".equals(rel)){
				/**调用业务模块接口*/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				int rst = service.addGrtLoanRGur(kCollRel, context, connection);
				if(rst!=1){
					throw new EMPException("Update Failed! Record Count: " + rst);
				}       
//			}else if("sxRel".equals(relLmt)){
//				/**调用授信模块接口*/
//				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
//				int rst = service.addRLmtAppGuarCont(kCollRelLmt,(String)kColl.getDataValue("guar_cont_type"), context, connection);
//				if(rst!=1){     
//					throw new EMPException("Update Failed! Record Count: " + rst); 
//				}    
			}
			/**-------------------------end-----------------------------------------*/
			//从担保合同表中获取rel，申请类型地段（1--担保合同管理进入，2--授信进入，3--业务进入）
			String relAppType = (String)kColl.getDataValue("rel");
//			if("".equals(relLmt)&&"".equals(rel)){
			if(rel==null||"".equals(rel)){
				if("1".equals(relAppType) || "2".equals(relAppType)){
					//调用授信模块接口
					try{
						int rst = 0;
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
						kCollRelLmt = new KeyedCollection(modelIdLmt);
						if("1".equals(relAppType)){
							kCollRelLmt.put("type", "Y");
						}
						String guar_cont_no=(String) kColl.getDataValue("guar_cont_no");
//						String limit_code=(String) kColl.getDataValue("limit_code");
						String guar_amt=(String) kColl.getDataValue("guar_amt");
						String agr_no=(String) kColl.getDataValue("agr_no");
//						String is_add_guar =(String) kCollRelLmt.getDataValue("is_add_guar");
//						String is_per_gur =(String) kCollRelLmt.getDataValue("is_add_guar");
//						kCollRelLmt.put("limit_code",limit_code);
						kCollRelLmt.put("guar_cont_no", guar_cont_no);
						kCollRelLmt.put("guar_amt", guar_amt);
						if(agr_no!=null&&!"".equals(agr_no)){//担保模式为联保时，新增生成联保授信协议与担保合同关系
							 service.createRLmtGuarContByJoint(guar_cont_no, agr_no, guar_amt, "", "", context, connection);
						}else{
							rst = service.addRLmtAppGuarCont(kCollRelLmt, (String)kColl.getDataValue("guar_cont_type"), context, connection);
						}
					}catch(Exception ee){
						throw new EMPException("生成联保授信协议与担保合同关系失败: " + ee.getMessage());
					}
				}
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}else{
				context.addDataField("flag","success");
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
