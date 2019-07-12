package com.yucheng.cmis.biz01line.cus.op.cussameorg;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusSameOrgDetail4HeadOp  extends CMISOperation {
	
	private final String modelId = "CusSameOrg";
	
//	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String head_org_no = null;
			String cus_id = null;
			try {
				head_org_no = (String)context.getDataValue("head_org_no");
			} catch (Exception e) {}
			if(head_org_no == null || head_org_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+head_org_no+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, "where same_org_no='"+head_org_no+"'", connection);
			for(int i=0 ; i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(0);
				cus_id = (String)kColl.getDataValue("cus_id");
			}
			
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
