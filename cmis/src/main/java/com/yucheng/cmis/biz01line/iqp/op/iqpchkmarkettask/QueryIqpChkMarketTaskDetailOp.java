package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarkettask;

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

public class QueryIqpChkMarketTaskDetailOp  extends CMISOperation {
	
	private final String adjModelId = "IqpMortValueAdj";
	private final String valModelId = "IqpMortValueMana";
	
	private boolean updateCheck = true;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.adjModelId, context, connection);
			
			String pk_id = null;
			try {
				pk_id = (String)context.getDataValue("pk_id");
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/** 盯市任务确认需走流程， 为适用流程，如果是流程中查看  2014-09-30 唐顺岩  */
			if(context.containsKey("serno")){
				KeyedCollection conf_kColl = (KeyedCollection)dao.queryFirst("IqpChkMarketTaskApp", null, " WHERE SERNO='"+context.getDataValue("serno")+"'", connection);
				if(null!=conf_kColl && null!=conf_kColl.getDataValue("serno") && !"".equals(conf_kColl.getDataValue("serno"))){
					pk_id = conf_kColl.getDataValue("adj_pk").toString();
				}
			}
			/** END */
			
			if(pk_id == null || pk_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id+"] cannot be null!");
			
			KeyedCollection adjKColl = dao.queryDetail(adjModelId, pk_id, connection);
			
			String value_no = (String) adjKColl.getDataValue("value_no");
			KeyedCollection valKColl = dao.queryDetail(valModelId, value_no, connection);
			
			//翻译目录
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", valKColl, "catalog_no", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			
			//翻译产地、销售区域
			Map<String, String> map = new HashMap<String, String>();
			map.put("produce_area", "STD_GB_AREA_ALL");
			map.put("sale_area", "STD_GB_AREA_ALL");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(valKColl, map, service);
			
			//翻译供应商名称
			String[] args=new String[] { "produce_vender" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(valKColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			//翻译登记机构、登记人 
			SInfoUtils.addSOrgName(adjKColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(adjKColl, new String[] { "input_id"});
			
			this.putDataElement2Context(adjKColl, context);
			this.putDataElement2Context(valKColl, context);
			
			
			/**价格调整历史信息*/
			int size = 5;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			ArrayList<String> list = new ArrayList<String>();
			list.add("org_valve");
			list.add("change_valve");
			list.add("info_sour");
			list.add("inure_date");
			IndexedCollection iColl = dao.queryList(adjModelId, list, " WHERE VALUE_NO='"+value_no+"' and pk_id !='"+pk_id+"' order by inure_date desc", pageInfo, connection);
			iColl.setName("IqpMortValueAdjList");
			
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
