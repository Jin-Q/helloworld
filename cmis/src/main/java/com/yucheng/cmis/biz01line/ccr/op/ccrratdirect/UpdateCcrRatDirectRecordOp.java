package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;

public class UpdateCcrRatDirectRecordOp extends CMISOperation {
	

	private final String modelId = "CcrAppInfo";
	private final String modelId1 = "CcrAppDetail";
	String flagInfo = CMISMessage.DEFEAT;//信息编码
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl4Info = null;
			KeyedCollection kColl4Det = null;
			
			try {
				kColl4Info = (KeyedCollection)context.getDataElement(modelId);
				kColl4Det = (KeyedCollection)context.getDataElement(modelId1);
			} catch (Exception e) {}
			if(kColl4Info==null || kColl4Info.size()==0 ||kColl4Det==null || kColl4Det.size()==0)
				throw new EMPJDBCException("The values to update["+modelId+","+modelId1+"] cannot be empty!");
		
			ComponentHelper cHelper =new ComponentHelper();
			CcrAppInfo ccrAppInfo = new CcrAppInfo();
			CcrAppDetail ccrAppDetail = new CcrAppDetail();
			cHelper.kcolTOdomain(ccrAppInfo, kColl4Info);
			cHelper.kcolTOdomain(ccrAppDetail, kColl4Det);
			ccrAppDetail.setSerno(ccrAppInfo.getSerno());
			CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
			//保存主表及明细
			flagInfo = ccrComponent.updateCcrAppAll(ccrAppInfo,ccrAppDetail);
			if(flagInfo == CMISMessage.DEFEAT){
				throw new CMISException(CMISMessage.MESSAGEDEFAULT,"保存失败!"); 
			}
			
			String reason_show = (String)kColl4Det.getDataValue("reason_show");
			if(reason_show != null && reason_show.trim().length()>0){
				kColl4Info.setDataValue("check_value", "1");
				context.addDataField("reportId", "ccr/zrscb1.raq");
			}else{
				kColl4Info.setDataValue("check_value", "2");
				context.addDataField("reportId", "ccr/zrscb2.raq");
			}
			context.addDataField("serno", ccrAppInfo.getSerno());
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag", "success");

		return "0";
	}
}
