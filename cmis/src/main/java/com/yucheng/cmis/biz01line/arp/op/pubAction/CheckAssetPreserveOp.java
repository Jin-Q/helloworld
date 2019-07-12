package com.yucheng.cmis.biz01line.arp.op.pubAction;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CheckAssetPreserveOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String type = (String)context.getDataValue("type");
			String value = (String)context.getDataValue("value");
			String records = "0";
			
			KeyedCollection kcoll = new KeyedCollection("TransValue");
			kcoll.addDataField("value", value);
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,conn);
			
			/*** type校验类型: checkBadasset(不良移交申请校验)、ArpLawDebtorInfo(债务人申请校验) 
			 * ArpLawLawsuitDetail、ArpLawLawsuitDtmana(诉讼明细校验)
			 * CusidDbtCongniz(呆账认定申请客户校验)、DbtCongnizDetail(呆账认定明细校验)
			 * CusidDbtWriteoff(呆账核销申请客户校验)、DbtWriteoffDetail(呆账核销明细校验)
			 * CusidBondReduc(债权减免申请客户校验)、BondReducDetail(债权减免明细校验)***/
			if(type.equals("checkBadasset") || type.equals("CusidDbtCongniz")||type.equals("DbtCongnizDetail") 
					|| type.equals("CusidDbtWriteoff")||type.equals("DbtWriteoffDetail")|| type.equals("CusidArpCollDebtApp")
					|| type.equals("CusidBondReduc")||type.equals("BondReducDetail")|| type.equals("BusiDebtDetail")){
				kcoll = cmisComponent.delReturnSql(type, kcoll);
				records = kcoll.getDataValue("results").toString();
			}else if(type.equals("ArpLawDebtorInfo")||type.equals("ArpLawDefendantInfo")
					||type.equals("ArpLawLawsuitDetail")||type.equals("ArpLawLawsuitDtmana")
					||type.equals("ArpLawDebtorMana")||type.equals("ArpLawDefendantMana")){
				String serno = (String)context.getDataValue("serno");
				kcoll.addDataField("serno", serno);
				kcoll = cmisComponent.delReturnSql(type, kcoll);
				records = kcoll.getDataValue("results").toString();
			}
			
			if(records.equals("0")){
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "failue");
			}			
		}catch (EMPException ee) {
			context.addDataField("flag", "failue");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "failue");
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}

	/**
	 * kcoll币种计算，调用前最好先判断
	 * curType kcoll中币种字段
	 * transAmt kcoll中金额字段
	 */
	public void  delKcollCurType(KeyedCollection kColl , String curType , String transAmt,Context context) throws EMPException {
		curType = kColl.getDataValue(curType)+"";
		if(!curType.equals("CNY") && !curType.equals("null") && !curType.equals("") ){	//只有非rmb时才转
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,this.getConnection(context));

			KeyedCollection trans_kcoll = new KeyedCollection();
			trans_kcoll.addDataField("curType", curType);
			trans_kcoll.addDataField("transAmt", kColl.getDataValue(transAmt));
			KeyedCollection results_kcoll = cmisComponent.delReturnSql("delCurType", trans_kcoll);
			
			kColl.setDataValue(transAmt, results_kcoll.getDataValue("results"));
		}
	}

	/**
	 * icoll币种计算
	 * curType kcoll中币种字段
	 * transAmt kcoll中金额字段
	 */
	public void  delIcollCurType(IndexedCollection iColl , String curType , String transAmt,Context context) throws EMPException {
		for(int i = 0 ;i < iColl.size();i++){
			delKcollCurType((KeyedCollection) iColl.get(i), curType, transAmt, context);
		}
	}
	
}