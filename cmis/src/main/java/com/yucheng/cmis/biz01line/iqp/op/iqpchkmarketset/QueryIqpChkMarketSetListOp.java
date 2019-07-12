package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarketset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpChkMarketSetListOp extends CMISOperation {


	private final String modelId = "IqpChkMarketSet";
	private final String valModelId = "IqpMortCatalogMana";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(valModelId, queryData, context, false, false, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.valModelId, conditionStr, context, connection);
			
			int size = 10; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			String sql = "select * from (select a.value_no,"+
							" a.catalog_no,"+
							" b.catalog_name,"+
							" b.catalog_lvl,"+
							" b.attr_type,"+
							" a.unit,"+
							" a.market_value,"+
							" a.input_id,"+
							" a.input_br_id,"+
							" a.input_date"+
							" from iqp_mort_value_mana a, iqp_mort_catalog_mana b"+
							" where a.catalog_no like '%'||b.catalog_no and a.status not in ('0','2')) IqpChkMarketSet" + conditionStr;
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			iColl.setName(modelId+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
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
