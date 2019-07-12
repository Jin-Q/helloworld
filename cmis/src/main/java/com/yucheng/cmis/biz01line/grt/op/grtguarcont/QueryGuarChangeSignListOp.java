package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGuarChangeSignListOp extends CMISOperation {


	private final String modelId = "GrtGuarCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String action = null;
	//	String conditionStr = null;
		try{
			connection = this.getConnection(context);
		    KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if("".equals(conditionStr)){
				conditionStr = "WHERE 1=1 ";
			}
			//modified by yangzy 2015/04/14 需求：XD150325024，集中作业扫描岗权限改造 START
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			String conditionStrT = recordRestrict.judgeQueryRestrict(this.modelId,"", context, connection);
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			/**调用业务模块接口，查询业务担保合同关联表中新增的担保变更*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceRel = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IndexedCollection contiColl = serviceRel.getInsertGuarChange(queryData,pageInfo,conditionStrT, context, connection);
			//modified by yangzy 2015/04/14 需求：XD150325024，集中作业扫描岗权限改造 end
			//把担保合同编号拼装成一个String 
//			String guar_cont_no_str = "";   
//			IndexedCollection contiColl = null;     
//			for(int i=0;i<iCollRel.size();i++){ 
//				KeyedCollection kColl = (KeyedCollection)iCollRel.get(i);
//				String guar_cont_no = (String)kColl.getDataValue("guar_cont_no");
//				guar_cont_no_str += "'"+guar_cont_no+"',";
//			}
//			if(!"".equals(guar_cont_no_str)){
//				guar_cont_no_str = guar_cont_no_str.substring(0, guar_cont_no_str.length()-1);
//			}else{
//				guar_cont_no_str = "''";  
//			}
//			conditionStr +=" and guar_cont_state='00' and guar_cont_no in("+guar_cont_no_str+")"; 
//			TableModelDAO dao = this.getTableModelDAO(context);
//			contiColl = dao.queryList(modelId, null, conditionStr, pageInfo, connection);  
			
//			for(int i=0;i<iCollRel.size();i++){
//				KeyedCollection kColl = (KeyedCollection)iCollRel.get(i);
//				String guar_cont_no_Rel = (String)kColl.getDataValue("guar_cont_no");
//				for(int j=0;j<contiColl.size();j++){
//					KeyedCollection GuarContkColl = (KeyedCollection)contiColl.get(j);
//					String guar_cont_no = (String)GuarContkColl.getDataValue("guar_cont_no");
//					if(guar_cont_no_Rel.equals(guar_cont_no)){ 
//						if(!GuarContkColl.containsKey("cont_no")){
//							GuarContkColl.addDataField("cont_no", kColl.getDataValue("cont_no"));	
//						}  
//					}    
//				}
//			}
			contiColl.setName("GrtGuarContList");
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(contiColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(contiColl, context);

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
