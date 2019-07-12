package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappjointcoop_joint;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;

public class CheckNameListGrade implements RiskManageInterface {

	/**
	 * 检查联保小组成员是否都存在有效评级
	 */
	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);

		Map<String,String> outMap=new HashMap<String,String>();
		String openDay = (String)context.getDataValue("OPENDAY");
		String message = "";
		String condition = "WHERE CUS_ID IN (SELECT CUS_ID FROM LMT_APP_NAME_LIST WHERE SERNO='"+serno+"') AND BELG_LINE<>'BL300' AND (CUS_CRD_GRADE IS NULL OR CUS_CRD_GRADE in('00','14','15','16','17','18','19') OR CUS_CRD_DT<'"+openDay+"')";
		IndexedCollection icoll = dao.queryList("CusBase", condition, connection);
		if(icoll.size()>0){
			for(int i=0;i<icoll.size();i++){
				KeyedCollection kCollCus = (KeyedCollection)icoll.get(i);
				String cus_id = (String)kCollCus.getDataValue("cus_id");
				message += "["+cus_id+"]";
			}
			outMap.put("OUT_是否通过", "不通过");
			outMap.put("OUT_提示信息", "联保小组成员"+message+"评级未达到A级(含)或已过期");
		}else{
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "联保小组成员评级检查通过");
		}
		
		return outMap;
	}
}
