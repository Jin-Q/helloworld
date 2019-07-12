package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class SearchLmtAmtOp extends CMISOperation {
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String agr_no = "";
			String lmt_type = "";
			
			if(!context.containsKey("agr_no") || null==context.getDataValue("agr_no") || "".equals(context.getDataValue("agr_no"))){
				context.addDataField("flag", "fail");
				context.addDataField("msg", "查询失败，失败原因：协议号不能为空");
				context.addDataField("lmt_amt", 0);
				context.addDataField("grp_lmt_amt", 0);
				return null;
			}
			
			if(!context.containsKey("lmt_type") || null==context.getDataValue("lmt_type") || "".equals(context.getDataValue("lmt_type"))){
				context.addDataField("flag", "fail");
				context.addDataField("msg", "查询失败，失败原因：授信类型不能为空，正确传值：  01-单一法人 02-同业客户 03-合作方");
				context.addDataField("lmt_amt", 0);
				context.addDataField("grp_lmt_amt", 0);
				return null;
			}
			
			agr_no = (String)context.getDataValue("agr_no");
			lmt_type = (String)context.getDataValue("lmt_type");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kColl = service.getAgrUsedInfoByArgNo(agr_no, lmt_type, connection, context);
			
			context.put("flag", "success");
			context.put("msg", "success");
			context.put("lmt_amt", kColl.getDataValue("lmt_amt"));    //授信占用
			context.put("grp_lmt_amt",  kColl.getDataValue("grp_lmt_amt"));  //集团授信占用
			context.put("is_grp",  kColl.getDataValue("is_grp"));  //是否集团客户
		} catch (Exception e) {
			context.put("flag", "fail");
			context.put("msg", "查询失败！"+e.getMessage());
			context.put("lmt_amt", 0);
			context.put("grp_lmt_amt", 0);
			context.put("is_grp",  ""); 
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
