package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortvaluemana;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpMortValueManaPopListOp extends CMISOperation {

	private final String modelId = "IqpMortValueMana";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if("".equals(conditionStr)){
				conditionStr += "where status='1'";
			}else{
				conditionStr += "and status='1'";
			}
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			//翻译目录
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",iColl, "catalog_no", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
    		//SInfoUtils.addPrdPopName(modelId, iColl, "sup_catalog_no", "catalog_no", "catalog_name", "", connection, dao); //翻译上级目录
			//翻译产地、销售区域
			Map<String, String> map = new HashMap<String, String>();
			map.put("produce_area", "STD_GB_AREA_ALL");
			map.put("sale_area", "STD_GB_AREA_ALL");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(iColl, new String[] { "input_id"});
			
			String[] args=new String[] { "catalog_no" };
			String[] modelIds=new String[]{"IqpMortCatalogMana"};
			String[] modelForeign=new String[]{"catalog_path"};
			String[] fieldName=new String[]{"catalog_name"};
			String[] resultName = new String[] { "catalog_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);

			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
