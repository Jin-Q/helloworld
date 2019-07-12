package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
//贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2014-12-26 
public class QueryPspPropertyAnalyListForZxOp extends CMISOperation {


	private final String modelId = "PspZxqkLoan";
	private final String analyModelId = "PspZxqkDetail";
	private final String resultModelId = "PspCheckItemResult";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cus_id = (String) context.getDataValue("cus_id");
			if(cus_id==null){
				throw new EMPException("获取不到客户编号！");
			}
			String task_id = (String) context.getDataValue("task_id");
			if(task_id==null){
				throw new EMPException("获取不到任务编号！");
			}
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			//征信记录明细
			KeyedCollection analyKColl = dao.queryDetail(analyModelId, task_id, connection);
			this.putDataElement2Context(analyKColl, context);
			
			//征信情况
			String conditionStr = " where cus_id = '"+ cus_id +
					"' and task_id = '" + task_id + "' and cus_type = '01" +  "' order by task_id desc ";
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
			IndexedCollection grtGuaranteeIColl = new IndexedCollection();//保证人信息
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "iqp");
			
			/**modified by yangzy 2015-7-8 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
			IndexedCollection TaskIColl = new IndexedCollection();//贷后信息
			TaskIColl = dao.queryList("PspCheckTask", "where task_create_date >= '2015-06-05' and task_id ='"+task_id+"'", connection);
			grtGuaranteeIColl = dao.queryList("PspGuarAnalyRel", "where grt_type='3' and task_id ='"+task_id+"'", connection);
			if(TaskIColl ==null || TaskIColl.size()<=0){
				grtGuaranteeIColl = service.getGrtListByCusId(cus_id, null, "3", this.getDataSource(context));
			}
			/**modified by yangzy 2015-7-8 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
			
			grtGuaranteeIColl.setName("GrtGuarantee"+"List");
			for(int i=0;i<grtGuaranteeIColl.size();i++){
				KeyedCollection grtGuaranteeKColl = (KeyedCollection) grtGuaranteeIColl.get(i);

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
