package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpapppsalecont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class IqpAppPsaleContAddOp extends CMISOperation {

	private final String modelId = "IqpAppPsaleCont";
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
				mem_cus_id= (String)context.getDataValue("mem_cus_id");//成员厂商
				cus_id=(String)context.getDataValue("cus_id");//核心企业编号
				mem_manuf_type=(String)context.getDataValue("mem_manuf_type");//厂商类型 01为供应商  02为经销商
				serno = (String)context.getDataValue("serno");//流水号 
			}catch(Exception e){
				throw new Exception("数据异常，请检查");
			}
			//核心厂商为买方客户，成员厂商为卖方客户
			if("01".equals(mem_manuf_type)){
				kColl.addDataField("barg_cus_id", mem_cus_id);
				kColl.addDataField("buyer_cus_id", cus_id);
			}else{
				kColl.addDataField("barg_cus_id", cus_id);
				kColl.addDataField("buyer_cus_id", mem_cus_id);
			}
			kColl.addDataField("mem_cus_id", mem_cus_id);
			String orgId=context.getDataValue("organNo").toString();
			String userId= context.getDataValue("currentUserId").toString();
			kColl.addDataField("serno", serno);
			kColl.addDataField("input_id", userId);
			kColl.addDataField("input_br_id", orgId);
			//翻译出卖方和买方中文名称
			String[] args=new String[] { "barg_cus_id","buyer_cus_id" };
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
