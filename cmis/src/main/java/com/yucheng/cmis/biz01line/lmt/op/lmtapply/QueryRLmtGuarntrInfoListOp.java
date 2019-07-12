package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRLmtGuarntrInfoListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cus_id = "";

			String limit_code = "";
			if(context.containsKey("limit_code")){
				limit_code= (String)context.getDataValue("limit_code");
			}
			String conditionStr = " WHERE limit_code='"+limit_code+"'";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList("RLmtGuarntrInfo", null,conditionStr,connection);
			//将保证人编号拼装成一个String
			int size = 15; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String GUAR_ID = (String)kColl.getDataValue("guar_id");
				cus_id += "'"+GUAR_ID+"',";
			}
			
			IndexedCollection lmtGuarntrInfo = new IndexedCollection();
			if (cus_id.length() > 0) {
				cus_id = cus_id.substring(0, cus_id.length() - 1);
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				GrtServiceInterface service = (GrtServiceInterface) serviceJndi.getModualServiceById("grtServices", "grt");
				lmtGuarntrInfo = service.getGuarantyInfoList(cus_id, "2", pageInfo, this.getDataSource(context));
				
				String[] args = new String[] { "cus_id" };
				String[] modelIds = new String[] { "CusBase" };
				String[] modelForeign = new String[] { "cus_id" };
				String[] fieldName = new String[] { "cus_name" };
				SystemTransUtils.dealName(lmtGuarntrInfo, args, SystemTransUtils.ADD,context, modelIds, modelForeign, fieldName);
			}
			
			lmtGuarntrInfo.setName("RLmtGuarntrInfoList");
			this.putDataElement2Context(lmtGuarntrInfo, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
