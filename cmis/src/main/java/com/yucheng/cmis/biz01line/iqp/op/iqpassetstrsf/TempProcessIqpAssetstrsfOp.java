package com.yucheng.cmis.biz01line.iqp.op.iqpassetstrsf;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cont.pub.sequence.CMISSequenceService4Cont;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class TempProcessIqpAssetstrsfOp extends CMISOperation {

	private final String modelId = "IqpAssetstrsf";
	private final String ctrModel = "CtrAssetstrsfCont";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String serno = null;
			String prdId = null;
			String asset_no = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			    serno = (String)kColl.getDataValue("serno");
			    prdId = (String)kColl.getDataValue("prd_id");
			    asset_no = (String)kColl.getDataValue("asset_no");
			    
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection modelIdKcoll = dao.queryAllDetail(modelId, serno, connection);
			//1、生成转贴现协议。
			KeyedCollection inColl = new KeyedCollection();
			String cont_no = CMISSequenceService4Cont.querySequenceFromDB("HT", "fromDate", "1", connection, context);
			inColl.addDataField("cont_no", cont_no);
			inColl.addDataField("cont_status", "100");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			KeyedCollection reultKColl = service.insertMsgByKModelFromPrdMap(prdId, "cont", modelIdKcoll, inColl, ctrModel, context, connection);
			String flag = (String)reultKColl.getDataValue("code");
			if(!flag.equals("success")){
				throw new EMPException("生成合同信息失败！");
			}
			
			//2、修改资产申请记录状态
			kColl.setDataValue("approve_status", "997");
			int count = dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
