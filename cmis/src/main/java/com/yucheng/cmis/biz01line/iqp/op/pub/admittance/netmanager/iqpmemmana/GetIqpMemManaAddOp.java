package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpmemmana;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetIqpMemManaAddOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		String net_agr_no = context.getDataValue("net_agr_no").toString();//网络协议编号
		String core_cus_id = context.getDataValue("cus_id").toString();
		KeyedCollection kColl = new KeyedCollection("IqpMemMana");
		kColl.addDataField("core_cus_id", core_cus_id);
		kColl.addDataField("net_agr_no", net_agr_no);
		String[] args=new String[] { "core_cus_id" };
	    String[] modelIds=new String[]{"CusBase"};
	    String[] modelForeign=new String[]{"cus_id"};
	    String[] fieldName=new String[]{"cus_name"};
		//详细信息翻译时调用			
        SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
        this.putDataElement2Context(kColl, context);
        
        //拼接字典STD_BIZ_TYPE
        IndexedCollection kColl_BizType = setBizType();
		this.putDataElement2Context(kColl_BizType, context);
		
		return "0";
	}

	private IndexedCollection setBizType() throws EMPException{
		IndexedCollection dicIcoll = new IndexedCollection("STD_BIZ_TYPE");
		try{
			KeyedCollection kTmp1 = new KeyedCollection();
			kTmp1.addDataField("enname", "0");
			kTmp1.addDataField("cnname","先票后货");
			dicIcoll.addDataElement(kTmp1);
			
			KeyedCollection kTmp2= new KeyedCollection();
			kTmp2.addDataField("enname", "1");
			kTmp2.addDataField("cnname","保兑仓");
			dicIcoll.addDataElement(kTmp2);
			
			KeyedCollection kTmp3 = new KeyedCollection();
			kTmp3.addDataField("enname", "2");
			kTmp3.addDataField("cnname","阶段性担保+货押");
			dicIcoll.addDataElement(kTmp3);
			
			KeyedCollection kTmp4 = new KeyedCollection();
			kTmp4.addDataField("enname", "3");
			kTmp4.addDataField("cnname","应收账款（池）质押融资");
			dicIcoll.addDataElement(kTmp4);
			
			KeyedCollection kTmp5 = new KeyedCollection();
			kTmp5.addDataField("enname", "4");
			kTmp5.addDataField("cnname","国内保理");
			dicIcoll.addDataElement(kTmp5);
			
			KeyedCollection kTmp6 = new KeyedCollection();
			kTmp6.addDataField("enname", "5");
			kTmp6.addDataField("cnname","存货质押");
			dicIcoll.addDataElement(kTmp6);
			
			KeyedCollection kTmp7 = new KeyedCollection();
			kTmp7.addDataField("enname", "6");
			kTmp7.addDataField("cnname","票据池融资");
			dicIcoll.addDataElement(kTmp7);
			
		}catch(EMPException e){
			throw new EMPException(""+e.getMessage());
		}
		
		return dicIcoll;
	}
}
