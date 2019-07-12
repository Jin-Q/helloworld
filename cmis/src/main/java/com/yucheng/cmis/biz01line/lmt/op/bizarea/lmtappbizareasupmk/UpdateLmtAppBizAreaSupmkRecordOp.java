package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizareasupmk;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class UpdateLmtAppBizAreaSupmkRecordOp extends CMISOperation {

	private final String modelIdL = "LmtAppBizAreaSupmkList";
	private final String modelId = "LmtAppBizAreaSupmk";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = null;
			try {
				iColl = (IndexedCollection)context.getDataElement(modelIdL);
			} catch (Exception e) {}
			
			if(iColl==null||iColl.size()==0){
				throw new Exception("目标客户群（超市百货类）列表为空！");
			}
			
			//先删掉原有分项信息再插入新的信息
			KeyedCollection kColl = (KeyedCollection)iColl.get(0);
			String serno = (String)kColl.getDataValue("serno");
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			Map<String,String> map = new HashMap<String, String>();
			map.put("serno", serno);
			lmtComponent.deleteByField(modelId, map);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int i=0; i<iColl.size(); i++){
				kColl = (KeyedCollection)iColl.get(i);
				kColl.setId(modelId);
				String supkm_serno = CMISSequenceService4JXXD.querySequenceFromDB("bizAreaComnSerno", "fromDate", null, connection, context);
				kColl.setDataValue("supmk_serno", supkm_serno);
				dao.insert(kColl, connection);
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
