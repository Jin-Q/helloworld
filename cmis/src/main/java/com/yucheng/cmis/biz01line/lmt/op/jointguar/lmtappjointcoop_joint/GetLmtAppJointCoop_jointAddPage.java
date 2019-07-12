package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappjointcoop_joint;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetLmtAppJointCoop_jointAddPage  extends CMISOperation {
	
	private final String modelId = "LmtAppJointCoop";
	private final String modelIdJB = "LmtAppJoinBack";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			String app_serno = null;  //圈商 的申请编号
			String serno = null;  //联保小组申请  号
			KeyedCollection tempK = null;
			TableModelDAO dao = getTableModelDAO(context);
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			try {
				app_serno = (String)context.getDataValue("app_serno");
			} catch (Exception e) {}
			
			if(kColl != null){
				serno = (String)kColl.getDataValue("serno");
				context.addDataField("serno", serno);
			}else{
				kColl = new KeyedCollection(modelId);
				kColl.addDataField("coop_type", "010");  //设置类别为 联保小组
				kColl.addDataField("is_biz_area_joint", "1");  //设置为商圈下新增
				kColl.addDataField("share_range", "1");  //设置共享范围
				String biz_area_no = null;//baSerno 获得
				List<String> l = new ArrayList<String>();
				l.add("serno");
				l.add("biz_area_no");
				tempK = dao.queryFirst(modelIdJB, l, " where serno='" + app_serno + "'", connection);
				biz_area_no = (String)tempK.getDataValue("biz_area_no");
				kColl.addDataField("biz_area_no", biz_area_no); //商圈编号
				//将商圈的end_date 授信总额  单户限额 传递到jsp
				KeyedCollection kCollTemp = dao.queryDetail("LmtAgrBizArea", biz_area_no, connection);
				context.addDataField("lmt_totl_amt",(String)kCollTemp.getDataValue("lmt_totl_amt"));
				context.addDataField("single_max_amt",(String)kCollTemp.getDataValue("single_max_amt"));
				context.addDataField("end_date",(String)kCollTemp.getDataValue("end_date"));
				if(context.containsKey("app_serno")){
					context.setDataValue("app_serno", app_serno);
				}else{
					context.addDataField("app_serno", app_serno);
				}
			}
			
			kColl.addDataField("input_id", context.getDataValue(PUBConstant.currentUserId));
			kColl.addDataField("input_br_id", context.getDataValue(PUBConstant.organNo));
			
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[] {"input_br_id"});
			
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
