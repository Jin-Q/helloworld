package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivSocRelComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckSpsIsExistsByCusId extends CMISOperation {
	@Override
	public String doExecute(Context context) throws EMPException {
		String cus_id = null;
		String cus_id_rel = null;
		Connection conn = null;
		String flag = "存在配偶信息，请先删除列表页面的配偶信息后增加！";
		String flag2 = "新增";
		try {
			cus_id = (String) context.getDataValue("cus_id");
			cus_id_rel = (String) context.getDataValue("cus_id_rel");
			conn = this.getConnection(context);
			CusIndivSocRelComponent cusIndivSocRelComponent = (CusIndivSocRelComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIVSOCREL, context, conn);
			
			CusIndivComponent ciComp = (CusIndivComponent)cusIndivSocRelComponent.getComponent(PUBConstant.CUSINDIV);
			CusIndiv cusInd = ciComp.getCusIndiv(cus_id);
			CusIndiv cusRelInd = ciComp.getCusIndiv(cus_id_rel);
			String cus_sex = "";
			String cus_rel_sex = "";
			if(cusInd!=null){
				cus_sex = cusInd.getIndivSex();
			}else {
				throw new Exception("客户["+cus_id+"]信息不存在！");
			}
			if(cusRelInd!=null){
				cus_rel_sex = cusRelInd.getIndivSex();
			}else {
				throw new Exception("客户["+cus_id_rel+"]信息不存在！");
			}
			if(cus_sex.equals(cus_rel_sex)){
				flag = "夫妻性别不能相同！";
			}
			//cus_id 和cus_id_rel均不存在配偶信息
			else if(!cusIndivSocRelComponent.checkExist(cus_id_rel).equals("y") && !cusIndivSocRelComponent.checkExist(cus_id).equals("y")){
				flag = "可以新增配偶信息";
			}else{
				if(cusIndivSocRelComponent.checkExist(cus_id).equals("y")){
					flag = "客户"+cus_id+"存在配偶信息";
					flag2 = "修改";
				}else if(cusIndivSocRelComponent.checkExist(cus_id_rel).equals("y")){
					flag = "客户"+cus_id_rel+"存在配偶信息";
				}				
			}
		} catch (Exception e) {
			flag = "操作失败！";
			throw new EMPException(e);
		} finally {
			if (conn != null) {
				this.releaseConnection(context, conn);
			}
		}
		context.addDataField("flag", flag);
		context.addDataField("flag2", flag2);
		return "0";
	}

}
