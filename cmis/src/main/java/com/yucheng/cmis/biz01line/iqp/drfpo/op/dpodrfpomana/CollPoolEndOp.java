package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpodrfpomana;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CollPoolEndOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String drfpo_no = "";
		String porder_no = "";
		KeyedCollection IqpBillDetailInfo = new KeyedCollection("IqpBillDetailInfo");
		KeyedCollection IqpCorreInfo = new KeyedCollection("IqpCorreInfo");
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			drfpo_no = (String) context.getDataValue("drfpo_no");
			porder_no = (String) context.getDataValue("porder_no");
			String porder[] = porder_no.split(",");
			//更改汇票状态为“核销”状态
			IqpBillDetailInfo.addDataField("porder_no","");
			IqpBillDetailInfo.addDataField("status","04");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				IqpBillDetailInfo.setDataValue("porder_no",porder_no);
				dao.update(IqpBillDetailInfo, connection);
			}
			//更改票据池与汇票的关联信息表的状态为“出池”状态
			IqpCorreInfo.addDataField("drfpo_no",drfpo_no);
			IqpCorreInfo.addDataField("porder_no","");
			IqpCorreInfo.addDataField("status","03");
			for(int i=0;i<porder.length;i++){
				porder_no = porder[i];
				IqpCorreInfo.setDataValue("porder_no",porder_no);
				dao.update(IqpCorreInfo, connection);
			}
			//返回异步标志，状态更改成功
			context.addDataField("flag","success");
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
