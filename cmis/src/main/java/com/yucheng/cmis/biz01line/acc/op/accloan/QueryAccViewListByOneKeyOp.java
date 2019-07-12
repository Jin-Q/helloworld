package com.yucheng.cmis.biz01line.acc.op.accloan;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * @author lisj
 * @time 2014年11月25日
 * @description 需求编号：【XD141107075】 一键查询未结清台账信息Op
 * @version v1.0
 */
public class QueryAccViewListByOneKeyOp extends CMISOperation {

	private final String modelId = "AccView";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cus_id = null;
		String table_model =null;
		KeyedCollection queryData = null;
		try{
			connection = this.getConnection(context);
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				throw new Exception("客户码获取失败,请联系后台管理员!");
			}
			try {
				table_model = (String)context.getDataValue("table_model");
			} catch (Exception e) {
				throw new Exception("获取table失败，table_model不能为null！");
			}
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			if(conditionStr !=null && !("".equals(conditionStr))){
				conditionStr += "and cus_id='"+cus_id+"'"+"and table_model='"+table_model+"' and status in ('1','6')";
			}else{
				conditionStr += "where cus_id='"+cus_id+"'"+"and table_model='"+table_model+"' and status in ('1','6')";
			}
			conditionStr += "order by start_date desc";
			int size = 10; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName("AccViewList");

			String[] args=new String[] {"cus_id","prd_id"};
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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