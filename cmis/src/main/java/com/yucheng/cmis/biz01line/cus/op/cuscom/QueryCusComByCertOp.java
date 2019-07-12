package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusComByCertOp  extends CMISOperation {
	private final String modelId = "CusCom";
	
	
	private final String cert_code_name = "cert_code";
	
	private boolean updateCheck = false;
	
	/**
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String cert_code_value = null;
			try {
				cert_code_value = (String)context.getDataValue(cert_code_name);
			} catch (Exception e) {}
			if(cert_code_value == null || cert_code_value.length() == 0)
				throw new EMPJDBCException("The value of ["+cert_code_name+"] cannot be null!");
			
			CusCom cusCom = new CusCom();
			CusBase cusBase = new CusBase();
			
			
			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			cusBase = cusBaseComponent.getCusBaseByCert(cert_code_value, "20");
			
			CusComComponent cusComComponent = (CusComComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM,context,connection);
			cusCom = cusComComponent.getCusCom(cusBase.getCusId());	
			/*KeyedCollection kColl = cHelper.domain2kcol(cusIndiv, modelId);
			System.out.println("name is>>>>>>>>>>>>>>>"+kColl.getName());
			this.putDataElement2Context(kColl, context);*/
			context.addDataField("cus_name", cusBase.getCusName());
			context.addDataField("cus_id", cusBase.getCusId());
			
//			context.addDataField("reg_code", cusBase.getRegCode());
			context.addDataField("loan_card_id", cusBase.getLoanCardId());
			
			
			//注册资本币种
			context.addDataField("reg_cur_type", cusCom.getRegCurType());
			//注册资本（万元）
			context.addDataField("reg_cap_amt", cusCom.getRegCapAmt());
			//主营范围
			context.addDataField("com_main_opt_scp", cusCom.getComMainOptScp());
			//兼营范围
//			context.addDataField("com_part_opt_scp", cusCom.getComPartOptScp());
			
			
			//法定代表人/负责人姓名
//			context.addDataField("legal_name", cusCom.getLegalName());
			//法定代表人证件类型
			//context.addDataField("legal_cert_type", cusCom.getLegalCertType());
			//法定代表人证件号码
			//context.addDataField("legal_cert_code", cusCom.getLegalCertCode());
			//通讯地址
			context.addDataField("post_addr", cusCom.getPostAddr());
			//联系电话
			context.addDataField("phone", cusCom.getLegalPhone());
			//注册登记日期
			context.addDataField("reg_start_date", cusCom.getRegStartDate());
			//到期日期
			context.addDataField("reg_end_date", cusCom.getRegEndDate());
			//注册登记地址
			context.addDataField("reg_addr", cusCom.getRegAddr());
			//国税税务登记代码
			context.addDataField("nat_tax_reg_code", cusCom.getNatTaxRegCode());
			//地税税务登记代码
			context.addDataField("loc_tax_reg_code", cusCom.getLocTaxRegCode());
			//企业规模
			context.addDataField("com_scale", cusCom.getComScale());
			//从业人数
			context.addDataField("com_employee", cusCom.getComEmployee());
			//登记注册 年检到期日
			context.addDataField("reg_audit_end_date", cusCom.getRegAuditEndDate());
			//拥有我行股份金额
//			context.addDataField("com_hold_stk_amt", cusCom.getComHoldStkAmt());
			//客户经理
			context.addDataField("cust_mgr", cusBase.getCustMgr());
			//主管机构
			context.addDataField("main_br_id", cusBase.getMainBrId());
			//客户类型
			context.addDataField("cus_type", cusBase.getCusType());
			
			
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
