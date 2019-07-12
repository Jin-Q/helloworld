package com.yucheng.cmis.biz01line.iqp.op.iqpassettransapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckAssetInfoForAssetTransOp extends CMISOperation {

	private final String modelId = "IqpAssetTransApp";
	private final String relModelId = "IqpAssetTransList";
	private final String accModelId = "AccLoan";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String serno = (String)context.getDataValue("serno");
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			
			String condition = " where serno = '"+serno+"'";
			IndexedCollection relIcoll = dao.queryList(relModelId, condition, connection);
			
			//没有资产明细
			if(relIcoll.size()==0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "未添加资产清单，无法放入流程！");
				return null;
			}
			
			for(int i=0;i<relIcoll.size();i++){
				KeyedCollection relKColl = (KeyedCollection) relIcoll.get(i);
				String bill_no = (String) relKColl.getDataValue("bill_no");
				KeyedCollection accKColl = dao.queryDetail(accModelId, bill_no, connection);
				String acc_status = (String) accKColl.getDataValue("acc_status");
				if(!acc_status.equals("1")){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "借据号【"+bill_no+"】台账状态已不是【正常】状态，无法放入流程，请检查资产清单！");
					return null;
				}
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "校验失败！");
			throw ee;
		}  catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
