package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class SubmitLmtCoopApplyOp  extends CMISOperation {
	
	private final String modelId = "LmtAppJointCoop";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			String openDate = context.getDataValue("OPENDAY").toString();
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl_agr = dao.queryDetail(modelId, serno_value, connection);
			
			kColl_agr.setDataValue("over_date", openDate);
			kColl_agr.setDataValue("approve_status", "997");
			//修改申请表的办结日期、申请状态
			dao.update(kColl_agr, connection);
			
			String lmt_agr_no = "";
			
			String cus_id = kColl_agr.getDataValue("cus_id").toString();
			
			//根据合作方类型、客户号、判断是否存在有效的合作方授信协议
			List<String> list = new ArrayList<String>();
			list.add("agr_no");
			StringBuffer condition = new StringBuffer(" WHERE CUS_ID='"+cus_id+"' AND COOP_TYPE='"+kColl_agr.getDataValue("coop_type")+"'");
			condition.append(" AND ADD_MONTHS(TO_DATE(END_DATE,'YYYY-MM-DD'),6) >= TO_DATE('"+openDate+"','YYYY-MM-DD')");  //授信到期顺延6个月
			KeyedCollection kColl = dao.queryFirst("LmtAgrJointCoop", list, condition.toString(), connection);
			
			String end_date = LmtUtils.computeEndDate(openDate, kColl_agr.getDataValue("term_type").toString(), kColl_agr.getDataValue("term").toString());
			
			kColl_agr.addDataField("end_date", end_date);  //设置到期日期
			
			kColl_agr.removeDataElement("over_date");  //清除到期日期
			kColl_agr.setName("LmtAgrJointCoop");  //将申请的kColl转换为协议
			
			kColl_agr.addDataField("agr_status","002");
			
			if(null != kColl && (null==kColl.getDataValue("agr_no") || "".equals(kColl.getDataValue("agr_no")))){  //如果协议号不存在说明是新增
				//生成授信协议编号
				lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				kColl_agr.addDataField("agr_no", lmt_agr_no);
				kColl_agr.addDataField("start_date", openDate);
				
				dao.insert(kColl_agr, connection); 
			}else{   //变更
				kColl_agr.addDataField("agr_no", kColl.getDataValue("agr_no"));
				dao.update(kColl_agr, connection);
			}
			
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
