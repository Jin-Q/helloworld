package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappjointcoop_joint;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;

public class CheckTerm implements RiskManageInterface {

	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//获取数据库处理dao接口
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);

		Map<String,String> outMap=new HashMap<String,String>();
		String openDay = (String)context.getDataValue("OPENDAY");
		KeyedCollection kColl = dao.queryDetail("LmtAppJointCoop", serno, connection);
		String termType = (String)kColl.getDataValue("term_type");
		String term = (String)kColl.getDataValue("term");
		String end_date = LmtUtils.computeEndDate (openDay,termType,term);
		end_date = end_date.replaceAll("-", "/");
		Date date_one = new Date(end_date);
		boolean flag = true;
		IndexedCollection icoll = dao.queryList("LmtAppDetails", " WHERE SERNO='"+serno+"'", connection);
		if(icoll.size()>0){
			for(int i=0;i<icoll.size();i++){
				KeyedCollection kCollDet = (KeyedCollection)icoll.get(i);
				String end_date_det = LmtUtils.computeEndDate(openDay, (String)kCollDet.getDataValue("term_type"), (String)kCollDet.getDataValue("term"));
				end_date_det = end_date_det.replaceAll("-", "/");
				Date date_two = new Date(end_date_det);
				long compute_value =date_one.getTime() - date_two.getTime();  //得到两个到期日期想减值
				if(compute_value < 0){
					flag = false;
					break;
				}
			}
			if(flag==true){
				outMap.put("OUT_是否通过", "通过");
				outMap.put("OUT_提示信息", "联保小组期限检查通过");
			}else{
				outMap.put("OUT_是否通过", "不通过");
				outMap.put("OUT_提示信息", "联保小组成员额度分项授信期限超过小组额度期限");
			}
		}else{
			outMap.put("OUT_是否通过", "不通过");
			outMap.put("OUT_提示信息", "联保小组成员额度分项为空");
		}
		
		return outMap;
	}

}
