package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class PrintStorageMortGuarantyCertiInfo extends CMISOperation {
	
	private final String modelId1 = "MortStorExwaDetail";//出入库明细
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = "";
			//押品编号
			String guaranty_no ="";
			//权证编号
			String warrant_no = "";
			//权证类型
			String warrant_type = "";
			
			warrant_type = (String) context.getDataValue("warrantType");
			warrant_no = (String) context.getDataValue("warrantNo");
			
			try {
				warrant_no = java.net.URLDecoder.decode(warrant_no, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			guaranty_no = (String) context.getDataValue("guarantyNo");
			String condition = "";
			if(context.containsKey("flag")&&context.getDataValue("flag")!=null&&"out".equals(context.getDataValue("flag"))){
				condition = " where guaranty_no = '"+guaranty_no+"' and warrant_no = '"+warrant_no+"' and warrant_type = '"+warrant_type+"' and warrant_state = '9' order by serno desc ";
			}else{
				condition = " where guaranty_no = '"+guaranty_no+"' and warrant_no = '"+warrant_no+"' and warrant_type = '"+warrant_type+"' and warrant_state = '2' order by serno desc ";
			}
			KeyedCollection kColl = dao.queryFirst(modelId1, null, condition, connection);
			if(kColl!=null&&kColl.getDataValue("serno")!=null&&!"".equals(kColl.getDataValue("serno"))){
				context.put("flag", "success");
				serno = kColl.getDataValue("serno").toString();
			}else{
				context.put("flag", "error");
			}
			context.put("serno", serno);
			
		}catch (EMPException ee) {
			context.put("flag", "error");
			throw ee;
		}finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
