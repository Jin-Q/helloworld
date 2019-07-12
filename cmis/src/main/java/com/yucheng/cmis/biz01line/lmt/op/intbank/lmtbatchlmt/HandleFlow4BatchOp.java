package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchlmt;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class HandleFlow4BatchOp extends CMISOperation {

	 private final String modelId = "LmtBatchLmt";
	 private final String modelIdAgr = "LmtBatchLmtAgr";
	 private final String modelIdAcc = "LmtIntbankAcc";
	public String doExecute(Context context) throws EMPException {		
		Connection connection = null;
		String serno = null;
		KeyedCollection  kCollAcc = new KeyedCollection(modelIdAcc);
		KeyedCollection  kCollAgr = new KeyedCollection(modelIdAgr);
		try{
			connection = this.getConnection(context);
			serno = (String)context.getDataValue("serno");
			TableModelDAO dao = getTableModelDAO(context);
			KeyedCollection kColl = null;
			//获取提交客户的数据
			IndexedCollection iColl = dao.queryList(modelId, " WHERE serno = '" + serno + "'", connection);
			//取出iColl里面数据，放入kColl中，进行逐条插入进台帐中
			kColl = (KeyedCollection) iColl.get(0);
			//判断批量包中客户是否进行授信
//			String batch_cus_no=(String)kColl.getDataValue("batch_cus_no");
//			IndexedCollection correiColl=dao.queryList("LmtBatchCorre", "where batch_cus_no='"+batch_cus_no+"'", connection);
			boolean check=true;
//			for(int i=0;i<correiColl.size();i++)
//			{
//				KeyedCollection correkColl=(KeyedCollection)correiColl.get(i);
//				String cus_id=(String)correkColl.getDataValue("cus_id");
//				//查询批量包中客户是否已经进行授信
//				IndexedCollection lmtiColl=dao.queryList("LmtSigLmt", "where cus_id='"+cus_id+"' and app_cls='"+"02'", connection);
//				if(lmtiColl.isEmpty())
//				{
//					context.addDataField("flag", "fail");
//					check=false;
//					break;
//				}
//			}
			if(check){
				//将数据插入台帐表中
				LmtIntbankComponent libc_acc = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
				kCollAcc = libc_acc.insert2kColl4Acc(kColl);
				
				//批量客户进行授信，提交生成一条协议
				LmtIntbankComponent libc_agr = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
				kCollAgr = libc_agr.insert2kColl4Agr(kColl);
				
				kColl.setDataValue("approve_status", "111");
				dao.insert(kCollAgr, connection);
				dao.update(kColl, connection);
				context.addDataField("flag","success");	
			}

	}catch(Exception e){
		throw new EMPException(e);
	} finally {
		if (connection != null)
			this.releaseConnection(context, connection);
	}
	return "0";
}

}
