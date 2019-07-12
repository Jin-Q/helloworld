package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpdepotagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetIqpDepotAgrAddOp extends CMISOperation {

	private final String modelId = "IqpDepotAgr";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		try{
			connection = this.getConnection(context);
			String net_agr_no=(String)context.getDataValue("net_agr_no");
			String mem_cus_id=(String)context.getDataValue("mem_cus_id");
			String cus_id=(String)context.getDataValue("cus_id");
			String mem_manuf_type=(String)context.getDataValue("mem_manuf_type");//成员厂商类型 “01”为供应商 “02”为经销商
			if("02".equals(mem_manuf_type)){//如果成员厂商为经销商,成员厂商为借款人
				kColl.addDataField("cus_id", mem_cus_id);
			}else{//如果成员厂商为供应商,核心企业为借款人
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
