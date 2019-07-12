package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtagrbizareasupmk;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryLmtAgrBizAreaSupmkListOp extends CMISOperation {


	private final String modelId = "LmtAgrBizAreaSupmk";
	private final String modelIdAgr = "LmtAgrBizArea";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			String biz_area_no = null;
			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			KeyedCollection kColl = null;
			String conditionStr = "where serno='" + serno + "'";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			//根据serno 查询 biz_area_no
			List<String> l = new ArrayList<String>();
			l.add("biz_area_no");
			IndexedCollection iColl = dao.queryList(modelIdAgr, l,conditionStr,connection);
			kColl = (KeyedCollection) iColl.get(0);
			biz_area_no = (String)kColl.getDataValue("biz_area_no");
			
			conditionStr = " where biz_area_no ='" + biz_area_no + "'";
			iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName("LmtAgrBizAreaSupmkList");
			this.putDataElement2Context(iColl, context);
			
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
