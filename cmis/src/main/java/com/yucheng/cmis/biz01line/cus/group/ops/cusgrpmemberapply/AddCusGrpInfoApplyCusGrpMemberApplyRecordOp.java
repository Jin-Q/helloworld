package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpmemberapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberApplyComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMemberApply;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusGrpInfoApplyCusGrpMemberApplyRecordOp extends CMISOperation {
	 
	private final String modelId = "CusGrpMemberApply";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//根据cusid从cuscom表中查出当前成员所属的主管机构和主管客户经理
			String cusId = (String) kColl.getDataValue("cus_id");
			if(cusId ==null || "".equals(cusId.trim())){
				throw new EMPJDBCException("The values ["+cusId+"] cannot be empty!");
			}

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryFirst("CusBase", null, " where cus_id = '"+cusId+"'", connection);
			
			kColl.setDataValue("manager_br_id", kc.getDataValue("main_br_id"));
			kColl.setDataValue("manager_id", kc.getDataValue("cust_mgr"));

			//转换类
			ComponentHelper cHelper = new ComponentHelper();
			CusGrpMemberApply cusGrpMemberApply = new CusGrpMemberApply();
			cusGrpMemberApply = (CusGrpMemberApply)cHelper.kcolTOdomain(cusGrpMemberApply, kColl);		

			//构件业务处理类
			CusGrpMemberApplyComponent cusGrpMemberApplyComponent = (CusGrpMemberApplyComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPMEMBERAPPLYCOMPONENT,context,connection);			
			cusGrpMemberApplyComponent.addCusGrpMemberApply(cusGrpMemberApply);
			
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
