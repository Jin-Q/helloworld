package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * @author zhaoxp
 * @time 2014年11月26日
 * @description 需求编号：XD140812040, 银行承兑税票改造
 * @version v1.0
 */
public class UpdateCtrLoanContOp extends CMISOperation {

	private final String modelId = "CtrLoanCont";
	private final String modelIdSub = "CtrLoanContSub";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			KeyedCollection kCollSub = null;
			KeyedCollection kCollSubTemp = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				kCollSubTemp = (KeyedCollection) context.getDataElement("CtrLoanCont.CtrLoanContSub");
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");
			String cont_no=(String)kColl.getDataValue("cont_no");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			kCollSub =dao.queryDetail(modelIdSub, cont_no, connection);
			kCollSub.put("actp_status",kCollSubTemp.getDataValue("actp_status"));
			dao.update(kCollSub, connection);
			flag = "保存成功。";
			
		} catch (EMPException ee) {
			flag = "保存失败！";
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag", flag);
		return "0";
	}
}
