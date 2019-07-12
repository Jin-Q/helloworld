package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappdesbuyplan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class IqpAppDesbuyPlanAddOp extends CMISOperation {

	private final String modelId = "IqpAppDesbuyPlan";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		try{
			connection = this.getConnection(context);
			String mem_cus_id="";
			String cus_id="";
			String mem_manuf_type="";
			String serno="";
			try{
				mem_cus_id= (String)context.getDataValue("mem_cus_id");//网络成员编号
				cus_id=(String)context.getDataValue("cus_id");//核心企业编号
				mem_manuf_type=(String)context.getDataValue("mem_manuf_type");//厂商类型 01为供应商  02为经销商
				serno = (String)context.getDataValue("serno");//流水号 
			}catch(Exception e){
				throw new Exception("数据异常，请检查");
			}
			
			//成员厂商为经销商时,供货厂商为核心企业，
			//成员厂商为供应商时,供货厂商为成员厂商，
			if("02".equals(mem_manuf_type)){//
				kColl.addDataField("for_manuf", cus_id);
			}else{
				kColl.addDataField("for_manuf", mem_cus_id);
			}
			//此字段为了查询成员厂商下所挂的订货计划列表用
			kColl.addDataField("cus_id", mem_cus_id);
			String orgId=context.getDataValue("organNo").toString();
			String userId= context.getDataValue("currentUserId").toString();
			
			kColl.addDataField("serno", serno);
			kColl.addDataField("input_id", userId);
			kColl.addDataField("input_br_id", orgId);
			String[] args=new String[] { "cus_id","for_manuf" };
		    String[] modelIds=new String[]{"CusBase","CusBase"};
		    String[] modelForeign=new String[]{"cus_id","cus_id"};
		    String[] fieldName=new String[]{"cus_name","cus_name"};
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
