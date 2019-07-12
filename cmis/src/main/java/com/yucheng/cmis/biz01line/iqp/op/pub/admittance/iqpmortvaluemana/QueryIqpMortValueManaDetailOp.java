package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortvaluemana;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpMortValueManaDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpMortValueMana";
	
	private final String value_no_name = "value_no";
	
	private boolean updateCheck = true;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnStr = "updatePage";
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String value_no_value = null;
			try {
				value_no_value = (String)context.getDataValue(value_no_name);
			} catch (Exception e) {}
			if(value_no_value == null || value_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+value_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, value_no_value, connection);
			
			//翻译目录
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", kColl, "catalog_no", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			
			//翻译产地、销售区域
			Map<String, String> map = new HashMap<String, String>();
			map.put("produce_area", "STD_GB_AREA_ALL");
			map.put("sale_area", "STD_GB_AREA_ALL");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
			//翻译供应商名称
			String[] args=new String[] { "produce_vender" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			
			//翻译登记机构、登记人 
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id"});
			
			context.addDataField("operate", "updateIqpMortValueManaRecord.do");  //更新的.do
			
			if(context.containsKey("isAdj") && !"".equals(context.getDataValue("isAdj"))){
				/**价格调整历史信息*/
				int size = 5;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
				
				ArrayList<String> list = new ArrayList<String>();
				list.add("org_valve");
				list.add("change_valve");
				list.add("info_sour");
				list.add("inure_date");
				IndexedCollection iColl = dao.queryList("IqpMortValueAdj", list, " WHERE VALUE_NO='"+kColl.getDataValue("value_no")+"' and (status !='1' or status is null)", pageInfo, connection);
				iColl.setName("IqpMortValueAdjList");
				
				//如果为价格调整
				if(!(context.containsKey("op") && "view".equalsIgnoreCase(context.getDataValue("op").toString()))){
					kColl.addDataField("org_value",kColl.getDataValue("market_value"));  //将原有商品核准价格放到上次核准价格中
					kColl.setDataValue("market_value", null);  //将商品核准价格清空
				}
				
				this.putDataElement2Context(iColl, context);
				TableModelUtil.parsePageInfo(context, pageInfo);
				
				returnStr = "isAdjPage";
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
		return returnStr;
	}
}
