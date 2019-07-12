package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappoverseeagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class IqpAppOverseeAgrAddOp extends CMISOperation {

	private final String modelId = "IqpAppOverseeAgr";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		try{
			connection = this.getConnection(context);
			String serno ="";
			String cus_id ="";
			String mem_cus_id ="";
			String mem_manuf_type ="";
			try{
				serno = (String)context.getDataValue("serno");//业务流水号
				cus_id = (String)context.getDataValue("cus_id");//核心厂商
				mem_cus_id=(String)context.getDataValue("mem_cus_id");//成员厂商
				mem_manuf_type=(String)context.getDataValue("mem_manuf_type");//厂商类别 01为供应商  02为经销商
			}catch(Exception e){
				throw new Exception("数据异常，请检查!");
			}
			//当成员厂商为供应商时，核心厂商为买方作为出质人存在
			if("01".equals(mem_manuf_type)){
				kColl.addDataField("mortgagor_id", cus_id);//核心厂商作为出质人
				kColl.addDataField("serno", serno);//业务流水号
			}else{
			   kColl.addDataField("mortgagor_id", mem_cus_id);//成员厂商作为出质人
			   kColl.addDataField("serno", serno);//业务流水号
			}			
			String orgId=context.getDataValue("organNo").toString();
			String userId= context.getDataValue("currentUserId").toString();
			kColl.addDataField("input_id", userId);
			kColl.addDataField("input_br_id", orgId);
			String[] args=new String[] { "oversee_con_id","mortgagor_id"};
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
