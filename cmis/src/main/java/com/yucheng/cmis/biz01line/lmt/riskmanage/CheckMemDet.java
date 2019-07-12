package com.yucheng.cmis.biz01line.lmt.riskmanage;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
/**
 * 检查集团授信成员授信分项是否完整
 * @author QZCB
 *
 */
public class CheckMemDet implements RiskManageInterface {

	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		Map<String,String> outMap=new HashMap<String,String>();
		boolean flag = true;
		String strTmp = "";
		String condition = " where grp_serno='"+serno+"'";
		IndexedCollection iColl = dao.queryList("LmtApply", condition, connection);
		for(int i=0;i<iColl.size();i++){
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);
			String app_serno = (String)kColl.getDataValue("serno");
			String cus_id = (String)kColl.getDataValue("cus_id");
			/**根据申请条线 查询是否存在分项信息  2013-12-19 唐顺岩 */
			String conditionDet = "WHERE SERNO ='"+app_serno+"'";
			IndexedCollection iCollDet = dao.queryList("LmtAppDetails", conditionDet, connection);
			if(iCollDet.size()==0){
				flag = false;
				strTmp += "["+cus_id+"]";
			}
		}
		
		if(flag==true){
			outMap.put("OUT_是否通过", "通过");
			outMap.put("OUT_提示信息", "集团成员授信分项检查通过");
		}else{
			outMap.put("OUT_是否通过", "不通过");
			outMap.put("OUT_提示信息", "集团成员"+strTmp+"授信分项未录入");
		}
		
		return outMap;
	}

}
