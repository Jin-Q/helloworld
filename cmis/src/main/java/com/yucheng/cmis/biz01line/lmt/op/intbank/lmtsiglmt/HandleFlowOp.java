package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class HandleFlowOp extends CMISOperation {
	
	private final String modelId = "LmtSigLmt";
	private final String modelIdAcc = "LmtIntbankAcc";

	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = null;
		String cus_id = null;
		KeyedCollection  kCollAcc = new KeyedCollection(modelIdAcc);
		try{
			connection = this.getConnection(context);
			
			cus_id=(String)context.getDataValue("cus_id");
			serno = (String)context.getDataValue("serno");
			TableModelDAO dao = getTableModelDAO(context);
			KeyedCollection kColl = null;
			//获取提交客户的数据
			IndexedCollection iColl = dao.queryList(modelId, " WHERE serno = '" + serno + "'", connection);
			//取出iColl里面数据，放入kColl中，进行逐条插入进台帐中
			for(int i=0;i<iColl.size();i++)
			{
				kColl = (KeyedCollection) iColl.get(i);
			}
			LmtIntbankComponent libc = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
			kCollAcc = libc.insert2kColl(kColl);
			//提交授信分项数据到授信使用明细
			IndexedCollection subiColl = dao.queryList("LmtSubApp"," WHERE serno = '" + serno + "'", connection);
			for(int j=0;j<subiColl.size();j++)
			{
				KeyedCollection kColldetail=(KeyedCollection)subiColl.get(j);
				kColldetail.remove("serno");
				kColldetail.addDataField("cus_id", cus_id);
				kColldetail.setName("LmtIntbankDetail");
				dao.insert(kColldetail, connection);
			}	
			//更改单笔授信中的申请状态			
			kColl.setDataValue("approve_status", "111");								
			dao.update(kColl, connection);
			context.addDataField("flag","success");
		}catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
