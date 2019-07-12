package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdPreventRiskDetailOp extends CMISOperation {
	private final String modelId = "PrdPreventRisk";
	private final String prevent_id_name = "prevent_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";
			String prevent_id_value = null;
			try {
				prevent_id_value = (String)context.getDataValue(prevent_id_name);
			} catch (Exception e) {}
			if(prevent_id_value == null || prevent_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+prevent_id_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, prevent_id_value, connection);
			this.putDataElement2Context(kColl, context);

			condition=" where prevent_id='"+prevent_id_value+"'";
			IndexedCollection iColl_PrdPvRiskScene = dao.queryList("PrdPvRiskScene",condition, connection);
			this.putDataElement2Context(iColl_PrdPvRiskScene, context);
			/** 查询出关联表信息置于页面之中 */
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			String conditionStr = " where prevent_id='"+prevent_id_value+"'";
			List list = new ArrayList();
			list.add("prevent_id");
			list.add("item_id");
			IndexedCollection iColl = dao.queryList("PrdPvRiskItemRel",list ,conditionStr,pageInfo,connection);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			if(iColl != null && iColl.size() > 0){
				IndexedCollection riskIColl = new IndexedCollection();
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection)iColl.get(i);
					KeyedCollection inKColl = new KeyedCollection();
					String item_id = (String)kc.getDataValue("item_id");
					KeyedCollection itemKColl = dao.queryDetail("PrdPvRiskItem", item_id, connection);
					inKColl.addDataField("item_id", item_id);
					inKColl.addDataField("item_name", itemKColl.getDataValue("item_name"));
					inKColl.addDataField("used_ind", itemKColl.getDataValue("used_ind"));
					riskIColl.add(inKColl);
				}
				riskIColl.setName("PrdPvRiskItemRelList");
				this.putDataElement2Context(riskIColl, context);
			}
			
			
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
