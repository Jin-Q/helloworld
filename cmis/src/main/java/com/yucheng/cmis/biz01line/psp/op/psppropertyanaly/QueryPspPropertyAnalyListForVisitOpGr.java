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
import com.yucheng.cmis.pub.util.SystemTransUtils;
/**
 * 
*@author zhaoxp
*@time 2015-01-19
*@description 需求编号：【XD141230092】贷后管理系统需求（首次检查、零售常规检查）
*@version v1.0
*
 */
public class QueryPspPropertyAnalyListForVisitOpGr extends CMISOperation {


	private final String modelId = "PspSitecheckComGr";
	private final String analyModelId = "PspCheckAnaly";
	private final String resultModelId = "PspCheckItemResult";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			String task_id = (String) context.getDataValue("task_id");
			if(task_id==null){
				throw new EMPException("获取不到任务编号！");
			}
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			KeyedCollection KColl = dao.queryDetail("PspCheckTask", task_id, connection);
			String cus_id = (String) KColl.getDataValue("cus_id");
			//检查分析说明
			KeyedCollection analyKColl = dao.queryDetail(analyModelId, task_id, connection);
			this.putDataElement2Context(analyKColl, context);
			
			//现场检查
			String conditionStr = " where task_id = '" + task_id + "' and visit_type in ('01','02')" + " order by task_id desc ";
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
			
			IndexedCollection grtMortIColl = new IndexedCollection();//担保品信息
			IndexedCollection grtGuaranteeIColl = new IndexedCollection();//保证人信息
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			GrtServiceInterface service = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "iqp");
			
			/**modified by yangzy 2015-7-8 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
			IndexedCollection TaskIColl = new IndexedCollection();//贷后信息
			TaskIColl = dao.queryList("PspCheckTask", "where task_create_date >= '2015-06-05' and task_id ='"+task_id+"'", connection);
			grtMortIColl = dao.queryList("PspGuarAnalyRel", "where grt_type='4' and task_id ='"+task_id+"'", connection);
			if(TaskIColl ==null || TaskIColl.size()<=0){
				grtMortIColl = service.getGrtListByCusId(cus_id, null, "4", this.getDataSource(context));
			}
			/**modified by yangzy 2015-7-8 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 end**/
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
			
			/**modified by yangzy 2015-7-8 需求编号：XD150625045 贷后管理常规检查任务改造,过滤追加保证 start**/
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
