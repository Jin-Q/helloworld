package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryGrtListByCusIdForPspOp extends CMISOperation {

	private final String resultModelId = "PspCheckItemResult";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cus_id = null;
		String task_id = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			try {
				cus_id = (String) context.getDataValue("cus_id");
			} catch (Exception e) {
            	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "获取不到客户编号", null);
            	throw new EMPException(e);
            }
			try {
				task_id = (String) context.getDataValue("task_id");
			} catch (Exception e) {
            	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "获取不到任务编号", null);
            	throw new EMPException(e);
            }
			
			IndexedCollection grtMortIColl = new IndexedCollection();//担保品信息
			IndexedCollection grtGuaranteeIColl = new IndexedCollection();//保证人信息
			/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
			IndexedCollection TaskIColl = new IndexedCollection();//贷后信息
			TaskIColl = dao.queryList("PspCheckTask", "where task_create_date >= '2015-06-05' and task_id ='"+task_id+"'", connection);
			/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "iqp");
			/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
			grtMortIColl = dao.queryList("PspGuarAnalyRel", "where grt_type='4' and task_id ='"+task_id+"'", connection);
			/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
			if(TaskIColl ==null || TaskIColl.size()<=0){
				grtMortIColl = service.getGrtListByCusId(cus_id, null, "4", this.getDataSource(context));
			}
			/**modified by yangzy 2015-6-26 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
			/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
			String[] args1=new String[] {"cus_id"};
			String[] modelIds1=new String[]{"CusBase"};
			String[]modelForeign1=new String[]{"cus_id"};
			String[] fieldName1=new String[]{"cus_name"};
			SystemTransUtils.dealName(grtMortIColl, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1);
			grtMortIColl.setName("GrtMort"+"List");
			for(int i=0;i<grtMortIColl.size();i++){
				KeyedCollection grtMortKColl = (KeyedCollection) grtMortIColl.get(i);
				String guaranty_no = (String) grtMortKColl.getDataValue("guaranty_no");
				String task_id_gua = task_id + guaranty_no;
				String scheme_id = "FFFA276A01CBD3827D90989AA0C031FD";
				String condition = " where task_id = '"+task_id_gua+"' and scheme_id = '"+scheme_id+"' ";
				IndexedCollection resultIColl = dao.queryList(resultModelId, condition, connection);
				if(resultIColl.size()>0){
					grtMortKColl.addDataField("is_analy", "1");
				}else{
					grtMortKColl.addDataField("is_analy", "2");
				}
			}
			this.putDataElement2Context(grtMortIColl, context);
			/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
			grtGuaranteeIColl = dao.queryList("PspGuarAnalyRel", "where grt_type='3' and task_id ='"+task_id+"'", connection);
			/**modified by yangzy 2015-6-26 需求编号：XXXXXXXXX 贷后管理常规检查任务改造,过滤追加保证 start**/
			if(TaskIColl ==null || TaskIColl.size()<=0){
				grtGuaranteeIColl = service.getGrtListByCusId(cus_id, null, "3", this.getDataSource(context));
			}
			/**modified by yangzy 2015-6-26 需求编号：XXXXXXXXX 贷后管理常规检查任务改造,过滤追加保证 end**/
			/**modified by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
//			String[] args1=new String[] {"cus_id"};
//			String[] modelIds1=new String[]{"CusBase"};
//			String[]modelForeign1=new String[]{"cus_id"};
//			String[] fieldName1=new String[]{"cus_name"};
//			SystemTransUtils.dealName(grtGuaranteeIColl, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1);
			grtGuaranteeIColl.setName("GrtGuarantee"+"List");
			for(int i=0;i<grtGuaranteeIColl.size();i++){
				KeyedCollection grtGuaranteeKColl = (KeyedCollection) grtGuaranteeIColl.get(i);
//				String guar_id = (String) grtGuaranteeKColl.getDataValue("guar_id");
//				String task_id_ee = task_id + guar_id;
				String bleg_line = (String)grtGuaranteeKColl.getDataValue("belg_line");
				String guar_cus_id = (String) grtGuaranteeKColl.getDataValue("cus_id");//保证人客户码
				String task_id_ee = task_id + guar_cus_id;
				String scheme_id = "FFFA276A016098037E87488513680CA2";
				if("BL300".equals(bleg_line)){
					scheme_id = "FFFA2787002D8A2F4F8F9A072E082729";
				}
				String condition = " where task_id = '"+task_id_ee+"' and scheme_id = '"+scheme_id+"' ";
				IndexedCollection resultIColl = dao.queryList(resultModelId, condition, connection);
				if(resultIColl.size()>0){
					grtGuaranteeKColl.addDataField("is_analy", "1");
				}else{
					grtGuaranteeKColl.addDataField("is_analy", "2");
				}
			}
			this.putDataElement2Context(grtGuaranteeIColl, context);
			
			
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
