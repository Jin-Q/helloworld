package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortcatalogmana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpMortCatalogManaDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpMortCatalogMana";
	
	private final String catalog_no_name = "catalog_no";
	
	private boolean updateCheck = true;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String catalog_no_value = null;
			try {
				catalog_no_value = (String)context.getDataValue(catalog_no_name);
			} catch (Exception e) {}
			if(catalog_no_value == null || catalog_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalog_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, catalog_no_value, connection);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id"});
			
			//翻译目录
			SInfoUtils.addPrdPopName(modelId, kColl, "catalog_path", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			SInfoUtils.addPrdPopName(modelId, kColl, "sup_catalog_no", "catalog_no", "catalog_name", "", connection, dao); //翻译上级目录
			
			this.putDataElement2Context(kColl, context);
			
			CatalogManaComponent catalogManaComponent = (CatalogManaComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
			Boolean isLeaf = catalogManaComponent.isLeaf(kColl.getDataValue("catalog_no").toString());  //判断是否存在下级目录
			
			context.addDataField("isLeaf", isLeaf);  //将是否存在子目录放到上下文
			context.addDataField("operate", "updateIqpMortCatalogManaRecord.do");  
			
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
