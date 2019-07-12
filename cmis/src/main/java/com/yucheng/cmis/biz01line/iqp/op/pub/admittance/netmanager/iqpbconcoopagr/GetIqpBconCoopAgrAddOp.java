package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpbconcoopagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetIqpBconCoopAgrAddOp extends CMISOperation {

	private final String modelId = "IqpBconCoopAgr";
	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		try{
			connection = this.getConnection(context);
			String net_agr_no=(String)context.getDataValue("net_agr_no");
			String cus_id=(String)context.getDataValue("cus_id");
			String mem_cus_id=(String)context.getDataValue("mem_cus_id");
			String mem_manuf_type=(String)context.getDataValue("mem_manuf_type");//成员厂商类型 01 供应商 02 经销商
			if("02".equals(mem_manuf_type)){//如果成员厂商为经销商,核心企业为厂商
				kColl.addDataField("borrow_cus_id", mem_cus_id);//借款客户
				kColl.addDataField("manuf_cus_id", cus_id);//厂商
			}else{//如果成员厂商为供应商,成员厂商为厂商
				kColl.addDataField("borrow_cus_id", cus_id);//借款客户
				kColl.addDataField("manuf_cus_id", mem_cus_id);//厂商
			}
			String orgId=context.getDataValue("organNo").toString();
			String userId= context.getDataValue("currentUserId").toString();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();	
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			KeyedCollection lmtkColl=lmtservice.getLmtAgrInfoMsg(mem_cus_id, "05", connection);//获取协议信息
			String agr_no="";//获取授信协议编号
			if(lmtkColl.containsKey("agr_no")){
				agr_no=(String)lmtkColl.getDataValue("agr_no");
			}			
			kColl.addDataField("net_agr_no", net_agr_no);//网络协议编号		
			kColl.addDataField("input_id", userId);
			kColl.addDataField("input_br_id", orgId);
			kColl.addDataField("lmt_agr_no", agr_no);
			
			String[] args=new String[] { "borrow_cus_id","manuf_cus_id"};
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
