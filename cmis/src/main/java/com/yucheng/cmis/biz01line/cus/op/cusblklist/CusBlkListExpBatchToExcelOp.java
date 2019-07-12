package com.yucheng.cmis.biz01line.cus.op.cusblklist;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

   /**
    * 不宜贷款户生成Excel
    * */
public class CusBlkListExpBatchToExcelOp extends CMISOperation {
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String conditionStr = " where 1=1 ";
			KeyedCollection queryKcoll = (KeyedCollection)context.getDataElement("CusBlkList");
			String cus_name =(String) queryKcoll.getDataValue("cus_name");
			String cert_type =(String) queryKcoll.getDataValue("cert_type");
			String cert_code =(String) queryKcoll.getDataValue("cert_code");
			String manager_br_id =(String) queryKcoll.getDataValue("manager_br_id");
			String status =(String) queryKcoll.getDataValue("status");
			if(!cus_name.equals("")){
				conditionStr = conditionStr + " and cus_name like '%"+cus_name+"%'";
			}
			if(!cert_type.equals("")){
				conditionStr = conditionStr + " and cert_type = '"+cert_type+"'";
			}
			if(!cert_code.equals("")){
				conditionStr = conditionStr + " and cert_code = '"+cert_code+"'";
			}
			if(!manager_br_id.equals("")){
				conditionStr = conditionStr + " and manager_br_id = '"+manager_br_id+"'";
			}
			if(!status.equals("")){
				conditionStr = conditionStr + " and status = '"+status+"'";
			}
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict("CusBlkList", conditionStr, context, connection);
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "conditionStr=" + conditionStr, null);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList("CusBlkList", null, conditionStr, connection);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("legal_addr", "STD_GB_AREA_ALL");//行政区划名称
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
			
			String street = "";
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				kColl.addDataField("xuhao", i+1);
				
				//存在街道时 将街道加到地址后面
				if(kColl.containsKey("street") && null!=kColl.getDataValue("street") && !"".equals(kColl.getDataValue("street"))){
					street = kColl.getDataValue("street").toString();
				}
				kColl.put("legal_addr_displayname", kColl.getDataValue("legal_addr_displayname") + " " + street);
			}
			
			iColl.setName("CusBlkListList");
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id" });
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
			this.putDataElement2Context(iColl, context);
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
