package com.yucheng.cmis.biz01line.iqp.op.iqpassetproapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckAssetInfoForAssetProOp extends CMISOperation {

	private final String modelId = "IqpAssetProApp";
	private final String relModelId = "IqpAssetProList";
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
				/**modified by lisj 需求编号：【XD150303017】关于资产证券化的信贷改造 begin**/
				if((accKColl.getDataValue("acc_status"))!=null && !"".equals(accKColl.getDataValue("acc_status")) && !acc_status.equals("1")){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "借据号【"+bill_no+"】台账状态已不是【正常】状态，无法放入流程，请检查资产清单！");
					return null;
				}
				/**modified by lisj 需求编号：【XD150303017】关于资产证券化的信贷改造 end**/
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
