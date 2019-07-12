package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoApplyComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfoApply;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusGrpInfoApplyModRecordOp extends CMISOperation {

	private final String modelId = "CusGrpInfoApply";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert[" + modelId+ "] cannot be empty!");

			serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ","all", connection, context);			
			kColl.setDataValue("serno", serno);

			// 转换类
			ComponentHelper cHelper = new ComponentHelper();
			CusGrpInfoApply cusGrpInfoApply = new CusGrpInfoApply();
			cusGrpInfoApply = (CusGrpInfoApply) cHelper.kcolTOdomain(cusGrpInfoApply, kColl);
			
			
			// 构件业务处理类
			CusGrpInfoApplyComponent cusGrpInfoApplyComponent = (CusGrpInfoApplyComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CUSGRPINFOAPPLYCOMPONENT, context,
							connection);

			cusGrpInfoApplyComponent.addCusGrpInfoApplyForMod(cusGrpInfoApply,serno);

			context.addDataField("flag", "success");

		} catch (EMPException ee) {
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
