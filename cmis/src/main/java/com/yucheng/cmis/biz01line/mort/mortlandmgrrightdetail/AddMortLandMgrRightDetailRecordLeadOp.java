package com.yucheng.cmis.biz01line.mort.mortlandmgrrightdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * @Description:XD150714054
 * @author FChengLiang
 * @time:2015-8-18  下午08:43:07
 */
public class AddMortLandMgrRightDetailRecordLeadOp extends CMISOperation {
	
	private final String modelId = "MortLandMgrRightDetail";
	private final String modelId1 = "MortLandBelongs";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = "";
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			if(context.containsKey("guaranty_no"))
			guaranty_no=(String) context.getDataValue("guaranty_no");
			guaranty_no = guaranty_no.trim();
			TableModelDAO dao = this.getTableModelDAO(context);
			conditionStr = "where guaranty_no ='"+guaranty_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			KeyedCollection kColl = new KeyedCollection(modelId);
			if(iColl.size()!=0){
			   kColl = (KeyedCollection) iColl.get(0);
			   IndexedCollection iColl1 = dao.queryList(modelId1, null,conditionStr,connection);
				iColl1.setName(iColl1.getName()+"List");
				this.putDataElement2Context(iColl1, context);
			}else{
			   kColl.addDataField("guaranty_no", guaranty_no);
			   kColl.addDataField("land_id",""+System.currentTimeMillis());
			}
			this.putDataElement2Context(kColl, context);
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
