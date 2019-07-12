package com.yucheng.cmis.biz01line.arp.op.arpcolldebtaccre;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpCollDebtAccReListPopOp extends CMISOperation {


	private final String modelId = "ArpCollDebtAccRe";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"";
			if("".equals(conditionStr)){
				conditionStr += " where status = '02'";
			}else{
				conditionStr += " and status = '02'";
			}
			
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			String[] args = new String[] { "serno","guaranty_no","guaranty_no","guaranty_no"};
			String[] modelIds = new String[] {"ArpCollDebtAcc","MortGuarantyBaseInfo","MortGuarantyBaseInfo","MortGuarantyBaseInfo"};
			String[] modelForeign = new String[] {"serno","guaranty_no","guaranty_no","guaranty_no"};
			String[] fieldName = new String[] { "cus_id","guaranty_name","guaranty_type","guaranty_info_status"};
			String[] resultName = new String[] { "cus_id","guaranty_name","guaranty_type","guaranty_info_status"};
			// 详细信息翻译时调用
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			String[] args1 = new String[] { "cus_id"};
			String[] modelIds1 = new String[] {"CusBase"};
			String[] modelForeign1 = new String[] {"cus_id"};
			String[] fieldName1 = new String[] { "cus_name"};
			// 详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args1, SystemTransUtils.ADD, context, modelIds1, modelForeign1, fieldName1);
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
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
