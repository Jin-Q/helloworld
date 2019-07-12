package com.yucheng.cmis.platform.riskmanage.op.risklist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;

public class RiskCtrLimitRelCheck implements RiskManageInterface {

	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		IndexedCollection ic = dao.queryList("CtrLimitLmtRel", " where limit_serno = '"+serno+"'", connection);
		if(ic != null && ic.size() > 0){
			map.put("OUT_是否通过", "通过");
			map.put("OUT_提示信息", "合同占用授信明细信息已录入");
		}else {
			map.put("OUT_是否通过", "不通过");
			map.put("OUT_提示信息", "合同占用授信明细信息未录入");
		}
		return map;
	}

}
