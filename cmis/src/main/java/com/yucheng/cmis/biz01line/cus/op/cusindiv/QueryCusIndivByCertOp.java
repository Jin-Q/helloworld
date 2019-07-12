package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusIndivByCertOp  extends CMISOperation {
	private final String modelId = "CusIndiv";
	
	private final String cert_typ_name = "certTyp";
	
	private final String cert_code_name = "certCode";
	
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
			
			String cert_typ_value = null;
			try {
				cert_typ_value = (String)context.getDataValue(cert_typ_name);
			} catch (Exception e) {}
			if(cert_typ_value == null || cert_typ_value.length() == 0)
				throw new EMPJDBCException("The value of ["+cert_typ_name+"] cannot be null!");

			String cert_code_value = null;
			try {
				cert_code_value = (String)context.getDataValue(cert_code_name);
			} catch (Exception e) {}
			if(cert_code_value == null || cert_code_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cert_code_name+"] cannot be null!");
			
			
			CusBase cusBase = new CusBase();
			CusIndiv cusIndiv = new CusIndiv();
			
			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			CusIndivComponent cusIndivComponent = (CusIndivComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIV,context,connection);
			
			cusBase = cusBaseComponent.getCusBaseByCert(cert_code_value, cert_typ_value);
			if(cusBase!= null){
				String cusId = cusBase.getCusId();
				cusIndiv = cusIndivComponent.getCusIndiv(cusId);	
			}
			
			//姓名
			context.addDataField("cus_name", cusBase.getCusName());
			context.addDataField("cus_id", cusBase.getCusId());
			//性别
			context.addDataField("indiv_sex", cusIndiv.getIndivSex());
			//职业
			context.addDataField("indiv_occ", cusIndiv.getIndivOcc());
			//年收入
			context.addDataField("indiv_ann_incm", cusIndiv.getIndivAnnIncm());
			//出生年月日
			context.addDataField("indiv_dt_of_birth", cusIndiv.getIndivDtOfBirth());
			//职务
			context.addDataField("indiv_com_job_ttl", cusIndiv.getIndivComJobTtl());
			//职称
			context.addDataField("indiv_crtfctn", cusIndiv.getIndivCrtfctn());
			//最高学位
			context.addDataField("indiv_dgr", cusIndiv.getIndivDgr());
			//最高学历
			context.addDataField("indiv_edt", cusIndiv.getIndivEdt());
			//爱好
			context.addDataField("indiv_hobby", cusIndiv.getIndivHobby());
			//联系电话
			context.addDataField("phone", cusIndiv.getPhone());
			//手机号码
			context.addDataField("mobile", cusIndiv.getMobile());
			//居住地址
			context.addDataField("indiv_rsd_addr", cusIndiv.getIndivRsdAddr());
			//工作单位
			context.addDataField("indiv_com_name", cusIndiv.getIndivComName());
			//拥有我行股份金额
			context.addDataField("com_hold_stk_amt", cusIndiv.getComHoldStkAmt());
			//客户经理
			context.addDataField("cust_mgr", cusBase.getCustMgr());
			//主管机构
			context.addDataField("main_br_id", cusBase.getMainBrId());
			context.addDataField("work_resume", cusIndiv.getWorkResume());
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
