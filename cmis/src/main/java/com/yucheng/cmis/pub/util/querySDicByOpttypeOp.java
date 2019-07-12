package com.yucheng.cmis.pub.util;

import java.sql.Connection;
import java.util.List;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * 
 *@author wangj
 *@time 2015-06-25
 *@description 需求编号：【XD150609042】关于无间贷申请流程变更
 *@version v1.0
 * 
 */
public class querySDicByOpttypeOp extends CMISOperation {

	private final String sqlId = "querySDicByOpttype";
	public String doExecute(Context context) throws EMPException {
		Connection connection=null;
		try {
			String opttype = "";
			opttype = (String) context.getDataValue("opttype");
			if (opttype==null||"".equals(opttype)) {
				context.addDataField("flag", "failed");
				context.addDataField("msg", "未录入opttype信息！");
				return "0";
			}
			connection = this.getConnection(context);
			List<KeyedCollection> list = (List<KeyedCollection>)SqlClient.queryList(sqlId,opttype, connection);
			if(list==null||list.size()==0){
				context.addDataField("flag", "error");
				context.addDataField("msg", "获取opttype["+opttype+"]代码列表失败！");
				return "0";
			}
			String ennameStr="";
			String cnnameStr="";
			for(int i = 0; i < list.size(); i++){
				String enname=(String) list.get(i).getDataValue("enname");
				String cnname=(String) list.get(i).getDataValue("cnname");
				if(i==list.size()-1){
					ennameStr+=enname;
					cnnameStr+=cnname;
				}else{
					ennameStr+=enname+",";
					cnnameStr+=cnname+",";
				}
				
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
			context.addDataField("ennameStr", ennameStr);
			context.addDataField("cnnameStr", cnnameStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

}
