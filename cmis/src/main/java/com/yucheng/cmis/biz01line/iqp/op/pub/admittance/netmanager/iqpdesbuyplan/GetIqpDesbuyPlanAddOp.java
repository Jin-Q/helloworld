package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpdesbuyplan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetIqpDesbuyPlanAddOp extends CMISOperation {

	private final String modelId = "IqpDesbuyPlan";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		try{
			connection = this.getConnection(context);
			String mem_cus_id= (String)context.getDataValue("mem_cus_id");//网络成员编号
			String cus_id=(String)context.getDataValue("cus_id");//核心企业编号
			String  mem_manuf_type=(String)context.getDataValue("mem_manuf_type");//厂商类型 01为供应商  02为经销商
			String net_agr_no = (String)context.getDataValue("net_agr_no");//网络协议编号
			//订货计划为买方的订货计划,就是经销商的订货计划
			if("02".equals(mem_manuf_type)){//成员厂商为经销商是,成员厂商为订货方
				kColl.addDataField("cus_id", mem_cus_id);
			}else{//成员厂商为供应商时,核心企业为订货方
				kColl.addDataField("cus_id", cus_id);
			}
			String orgId=context.getDataValue("organNo").toString();
			String userId= context.getDataValue("currentUserId").toString();
			
			kColl.addDataField("net_agr_no", net_agr_no);
			kColl.addDataField("input_id", userId);
			kColl.addDataField("input_br_id", orgId);
			String[] args=new String[] { "cus_id" };
		    String[] modelIds=new String[]{"CusBase"};
		    String[] modelForeign=new String[]{"cus_id"};
		    String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
            SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
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
