package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarkettask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class ExportIqpChkMarketTaskListOp extends CMISOperation {

	private final String modelId = "IqpChkMarketTask";
	//private final String adjModelId = "IqpMortValueAdj";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition("IqpMortCatalogMana", queryData, context, false, false, false);
			//导出EXCEL时须加记录集权限   2014-05-06 tsy
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			if(conditionStr==null||conditionStr.equals("")){
				conditionStr = " where status is not null";//判断是盯市任务，价格管理生成的则没有状态
			}else{
				conditionStr = conditionStr + " and status is not null";//判断是盯市任务，价格管理生成的则没有状态
			}
			
			//int size = 10; 
			//PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			String sql = "select * from (select a.pk_id," +
							" a.value_no," +
							" b.catalog_no,"+
							" c.catalog_name,"+
							" c.catalog_lvl,"+
							" c.attr_type,"+
							" b.unit,"+
							" b.info_sour,"+
							" a.org_valve,"+
							" a.change_valve,"+
							" a.change_resn,"+
							" a.input_date,"+
							" a.input_id,"+
							" a.input_br_id," +
							" a.status"+
							" from iqp_mort_value_adj a, iqp_mort_value_mana b, iqp_mort_catalog_mana c"+
							" where a.value_no = b.value_no"+
							" and b.catalog_no like '%' || c.catalog_no) IqpChkMarketTask" + conditionStr;
			
			IndexedCollection iColl = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
			iColl.setName(modelId+"List");
			this.putDataElement2Context(iColl, context);
			
			//TableModelUtil.parsePageInfo(context, pageInfo);
			
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
