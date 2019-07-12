package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpobilldetail;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.drfpo.component.DpoDrfpoComponent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetPorderMsgByPorderNoDrfpoOp extends CMISOperation {
	/** 处理逻辑分析：判断票据是否存在
	 *  1.不存在则返回，可以直接添加
	 *  2.票据存在
	 *  	2.1票据存在关联表中则不可以添加
	 *  	2.2票据不存在关联表中，并且票据的状态为允许添加的状态则返回票据信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String porder_no = "";
			String drfpo_no="";
			if(context.containsKey("porder_no")){
				porder_no = (String)context.getDataValue("porder_no");
			}
			if(context.containsKey("drfpo_no")){
				drfpo_no = (String)context.getDataValue("drfpo_no");
			}
			//中文转码
			drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
			porder_no = URLDecoder.decode(porder_no,"UTF-8");
			/** 通过汇票号码取得系统中汇票信息 */
			DpoDrfpoComponent cmisComponent = (DpoDrfpoComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(DpoConstant.DPODRFPOCOMPONENT, context, connection);
			KeyedCollection kcResult = cmisComponent.getPorderMsgByPorderNoDrfpo(porder_no,drfpo_no);
			if(kcResult.getDataValue("code").equals("9999")){
				context.addDataField("flag", "9999");
				context.addDataField("msg", kcResult.getDataValue("msg"));
				context.setDataValue("porder_no", "");
				context.addDataField("porder_curr", "");
				context.addDataField("drft_amt", "");
				context.addDataField("porder_addr", "");
				context.addDataField("is_ebill", "");
				context.addDataField("bill_type", "");
				context.addDataField("bill_isse_date", "");
				context.addDataField("porder_end_date", "");
				context.addDataField("utakeover_sign", "");
				context.addDataField("tcont_no", "");
				context.addDataField("tcont_amt", "");
				context.addDataField("tcont_content", "");
				context.addDataField("drwr_org_code", "");
				context.addDataField("isse_name", "");
				context.addDataField("daorg_no", "");
				context.addDataField("daorg_name", "");
				context.addDataField("daorg_acct", "");
				context.addDataField("pyee_name", "");
				context.addDataField("paorg_no", "");
				context.addDataField("paorg_name", "");
				context.addDataField("paorg_acct_no", "");
				context.addDataField("aaorg_type", "");
				context.addDataField("aaorg_no", "");
				context.addDataField("aaorg_name", "");
				context.addDataField("accptr_cmon_code", "");
				context.addDataField("aaorg_acct_no", "");
				context.addDataField("aorg_type", "");
				context.addDataField("aorg_no", "");
				context.addDataField("aorg_name", "");
				context.addDataField("status", "");
			}else if(kcResult.getDataValue("code").equals("0001")){
				//存在于其他的票据池中，不能在此票据池中新增
				context.addDataField("flag", "0001");
				context.addDataField("msg", kcResult.getDataValue("msg"));
			}else if(kcResult.getDataValue("code").equals("0000")){
				//已录入并可以在此票据池中进行新增
				context.addDataField("flag", "0000");
				context.addDataField("status",kcResult.getDataValue("status"));
			}
			insertMsg2KColl(kcResult, context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 将需要转换的KColl放入上下文中
	 * @param kColl
	 * @param context
	 * @throws Exception
	 */
	public void insertMsg2KColl(KeyedCollection kColl,Context context) throws Exception{
		for(int i=0;i<kColl.size();i++){
			DataElement element = (DataElement) kColl.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				if(context.containsKey(aField.getName())){
				}else {
					String value = "";
					if(aField.getValue() == null){
						value = "";
					}else {
						value = aField.getValue().toString();
					}
					context.addDataField(aField.getName(), value);
				}
				
			}
		}
	}
}
