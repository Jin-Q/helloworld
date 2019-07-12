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
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdPvRiskItemRelListOp extends CMISOperation {
	private static final String relModel = "PrdPvRiskItemRel";
	private static final String riskModel = "PrdPvRiskItem";
	/**
	 * 通过方案编号查询关联表中的方案关联信息，并且查询出关联的风险拦截信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String preventId = (String)context.getDataValue("prevent_id");
			if(preventId == null || preventId.trim().length() == 0){
				throw new EMPException("获取风险拦截方案出错，请检查！");
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			String conditionStr = " where prevent_id='"+preventId+"'";
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List list = new ArrayList();
			list.add("prevent_id");
			list.add("item_id");
			IndexedCollection iColl = dao.queryList(relModel,list ,conditionStr,pageInfo,connection);
			TableModelUtil.parsePageInfo(context, pageInfo);
			IndexedCollection riskIColl = new IndexedCollection();
			if(iColl != null && iColl.size() > 0){
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
			}
			riskIColl.setName("PrdPvRiskItemRelList");
			this.putDataElement2Context(riskIColl, context);
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
