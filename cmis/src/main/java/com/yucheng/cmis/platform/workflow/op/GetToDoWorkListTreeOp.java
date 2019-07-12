package com.yucheng.cmis.platform.workflow.op;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class GetToDoWorkListTreeOp extends CMISOperation {

	private static String OPT_TYPE = "ZB_BIZ_CATE";
	public String doExecute(Context context) throws EMPException {
		String curUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		Map<String, String> dicMap = new HashMap<String, String>();
		KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
		if(dictColl!=null) {
			IndexedCollection icoll = (IndexedCollection) dictColl.getDataElement(OPT_TYPE);
			if(icoll != null) {
				for(int i=0; i<icoll.size(); i++) {
					KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
					String enname = (String)kcoll.getDataValue(CMISDataDicService.ATTR_ENNAME);
					KeyedCollection paramKcoll = new KeyedCollection();
					paramKcoll.put("appl_type", enname);
					paramKcoll.put("currentnodeuser", "%"+curUserId+"%");
					String count = "0";
					try {
						count = String.valueOf(SqlClient.queryFirst("getToDoWorkListCount", paramKcoll, null, this.getConnection(context)));
					} catch (SQLException e) {
						e.printStackTrace();
						throw new EMPException(e);
					}
					if(!"0".equals(count))
						dicMap.put((String)kcoll.getDataValue(CMISDataDicService.ATTR_ENNAME), (String)kcoll.getDataValue(CMISDataDicService.ATTR_CNNAME)+"["+count+"笔]");
				}
			}
		}
		context.put("applTypeMap", dicMap);
		
		return null;
	}

}
