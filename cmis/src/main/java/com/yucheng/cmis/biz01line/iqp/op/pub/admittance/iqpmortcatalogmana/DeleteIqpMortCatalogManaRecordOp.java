package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortcatalogmana;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteIqpMortCatalogManaRecordOp extends CMISOperation {

	private final String modelId = "IqpMortCatalogMana";
	
	private final String catalog_no_name = "catalog_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String catalog_no_value = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			try {
				catalog_no_value = (String)context.getDataValue(catalog_no_name);
			} catch (Exception e) {}
			if(catalog_no_value == null || catalog_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalog_no_name+"] cannot be null!");
			
			CatalogManaComponent catalogManaComponent = (CatalogManaComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
			
			
			if(catalogManaComponent.isLeaf(catalog_no_value)){  //判断目录下是否存在有效的子目录
				context.addDataField("msg", "押品目录["+catalog_no_value+"]包含有效的子目录，不能做删除处理！");
				context.addDataField("flag","N");
				return "0";
			}
			if(catalogManaComponent.searchValueManaByCatalogNo(catalog_no_value)){  //校验是否存在有效价格信息
				context.addDataField("msg", "押品目录["+catalog_no_value+"]包含价格信息，不能做删除处理！");
				context.addDataField("flag","N");
				return "0";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			//根据表模型和键值对的过滤条件删除同一目录下的供应商
			Map<String,String> conditionFields = new HashMap<String,String>();
			conditionFields.put("mort_catalog_no", catalog_no_value);
			catalogManaComponent.deleteIqpCommoProvider("IqpCommoProvider", conditionFields);

			int count = dao.deleteByPk(modelId, catalog_no_value, connection);
			if (count != 1) {
				context.addDataField("flag", "N");
				context.addDataField("msg", "根据目录编号["+catalog_no_value+"]删除记录条数为 "+count);
				return "0";
			}
			
			//如果删的目录层级为一级目录
			if(context.containsKey("catalog_lvl") && "1".equals(context.getDataValue("catalog_lvl"))){
				//同步删除树形字典中对应的货物质押数据
				catalogManaComponent.deleteTreeDicByCatalogNo(catalog_no_value);
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag", "N");
			context.addDataField("msg", "根据目录编号["+catalog_no_value+"]删除目录错误，错误原因： "+e.getMessage());
			e.printStackTrace();
			return "0";
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
