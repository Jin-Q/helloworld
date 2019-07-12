package com.yucheng.cmis.biz01line.cus.op.cussameorg;

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
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusSameOrgDetailOp  extends CMISOperation {
	
	private final String modelId = "CusSameOrg";
	
//	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
//			if(this.updateCheck){
			//记录集权限控制
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			
			String cus_id = null;
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id+"] cannot be null!");
			if(context.containsKey("pvp_auth")&&context.getDataValue("pvp_auth")!=null&&"Y".equals(context.getDataValue("pvp_auth"))){
				cus_id = (String)SqlClient.queryFirst("queryCusSameInfo", cus_id, null, connection);
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			this.putDataElement2Context(kColl, context);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("address", "STD_GB_AREA_ALL");//地址
			map.put("same_org_type", "STD_ZB_INTER_BANK_ORG");//同业机构类型
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtService = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			String lmtFlag = lmtService.searchIsExistLmt(cus_id, "2");
			kColl.addDataField("lmt_flag", lmtFlag);
			
			SInfoUtils.addUSerName(kColl, new String[] { "input_id"});
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			this.putDataElement2Context(kColl, context);
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
