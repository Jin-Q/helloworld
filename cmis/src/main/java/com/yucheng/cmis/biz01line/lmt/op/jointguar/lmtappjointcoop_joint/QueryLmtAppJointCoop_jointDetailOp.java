package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappjointcoop_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppJointCoop_jointDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppJointCoop";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
		
			if(serno==null||"".equals(serno)){
				throw new Exception("serno字段值为空！");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			
			//当是商圈下联保：1是    时，将商圈下的  期限，期限类型，到期日期取出来   term   term_type    end_date
//			if("1".equals(kColl.getDataValue("is_biz_area_joint"))){
//				String biz_area_no = (String)kColl.getDataValue("biz_area_no");
//				KeyedCollection kCollTemp = dao.queryFirst("LmtAgrBizArea", null, "where biz_area_no ='"+biz_area_no+"'", connection);
//				String lmt_totl_amt = (String)kCollTemp.getDataValue("lmt_totl_amt");
//				String single_max_amt = (String)kCollTemp.getDataValue("single_max_amt");
//				String end_date = (String)kCollTemp.getDataValue("end_date");
//				context.addDataField("lmt_totl_amt", lmt_totl_amt);
//				context.addDataField("single_max_amt", single_max_amt);
//				context.addDataField("end_date", end_date);
//			}
			//翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(kColl, new String[]{"input_id","manager_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"});
			if("2".equals((String)kColl.getDataValue("share_range"))){
				SystemTransUtils.containCommaORG2CN(kColl,context);
			}
			//添加字段为名单列表传值用
			kColl.addDataField("joint_serno",kColl.getDataValue("serno"));
			this.putDataElement2Context(kColl, context);
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
