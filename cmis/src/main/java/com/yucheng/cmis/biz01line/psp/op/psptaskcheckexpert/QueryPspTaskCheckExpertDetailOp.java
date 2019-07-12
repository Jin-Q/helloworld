package com.yucheng.cmis.biz01line.psp.op.psptaskcheckexpert;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPspTaskCheckExpertDetailOp  extends CMISOperation {
	
	private final String modelId = "PspTaskCheckExpert";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				context.put("menuId", "expert_task");
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			//圈商
			String biz_circle_no = (String) kColl.getDataValue("biz_circle_no");
			//合作方
			String agr_no= (String) kColl.getDataValue("agr_no");
			//业务品种
			String biz_type= (String) kColl.getDataValue("biz_type");
			//行业分类
			String indus_type = (String) kColl.getDataValue("indus_type");
			//圈商接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			if(biz_circle_no!=null){
				KeyedCollection kc = lmtServiceInterface.getLmtAgrBizArea(biz_circle_no,connection);
				String biz_area_name = (String) kc.getDataValue("biz_area_name");
				String biz_area_type = (String) kc.getDataValue("biz_area_type");
				kColl.put("biz_area_name", biz_area_name);
				kColl.put("biz_area_type", biz_area_type);
			}
			if(agr_no!=null){
				String coop_type= (String) kColl.getDataValue("coop_type");
				if("011".equals(coop_type)){
					
				}else{
					KeyedCollection kc = lmtServiceInterface.getAgrCoopInfo(agr_no,connection);
					coop_type = (String) kc.getDataValue("coop_type");
				}
				
			//	kColl.addDataField("coop_type", coop_type);
			}
			if(indus_type!=null){
				//行业分类树形字典翻译
				Map<String,String> map = new HashMap<String, String>();
				map.put("indus_type", "STD_GB_4754-2011");
				CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
				SInfoUtils.addPopName(kColl, map, service);
			}
			
			if(biz_type!=null){
				KeyedCollection prdkColl = dao.queryDetail("PrdBasicinfo", biz_type, connection);
				kColl.put("biz_type_displayname", prdkColl.getDataValue("prdname"));
			}
			//产品翻译
			
			//客户名称翻译
			String[] args=new String[] { "cus_id","agr_no","grp_no"};
			String[] modelIds=new String[]{"CusBase","CusBase","CusGrpInfo"};
			String[] modelForeign=new String[]{"cus_id","cus_id","grp_no"};
			String[] fieldName=new String[]{"cus_name","cus_name","grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "task_exe_br_id","task_divis_br_id","belg_branch" });
			SInfoUtils.addUSerName(kColl, new String[] { "task_exe_id" ,"task_divis_id","belg_manager_id"});
			this.putDataElement2Context(kColl,context);
			
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
