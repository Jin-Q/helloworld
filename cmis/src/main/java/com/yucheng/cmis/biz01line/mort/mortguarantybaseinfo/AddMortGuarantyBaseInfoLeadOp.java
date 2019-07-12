package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddMortGuarantyBaseInfoLeadOp extends CMISOperation {
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			//押品基本信息容器（kColl）传值用
			KeyedCollection kColl = new KeyedCollection("MortGuarantyBaseInfo");	
			kColl.addDataField("input_id",context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id",context.getDataValue("organNo"));
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			kColl.addDataField("input_date", context.getDataValue("OPENDAY"));
			if(context.containsKey("cus_id")){//押品为担保合同做担保时，根据客户码过滤。
			/*	String cus_id = (String) context.getDataValue("cus_id");
				//调用客户模块接口
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cus = service.getCusBaseByCusId(cus_id, context, this.getConnection(context));
				kColl.addDataField("cus_id", cus.getCusId());//客户码
				kColl.addDataField("cus_id_displayname", cus.getCusName());//客户名称
				kColl.addDataField("cert_type", cus.getCertType());//证件类型
				kColl.addDataField("cert_code", cus.getCertCode());//证件号码
				*/
			}
			if(context.containsKey("agr_type")&&context.containsKey("agr_no")){
				//监管协议类型
				String agr_type = (String) context.getDataValue("agr_type");
				//协议编号
				String agr_no = (String) context.getDataValue("agr_no");
				kColl.addDataField("agr_type",agr_type);
				kColl.addDataField("agr_no",agr_no);
			}
			this.putDataElement2Context(kColl, context);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
