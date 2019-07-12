package com.yucheng.cmis.biz01line.fnc.op.fncstatbase;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryFncStatBasePopListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "FncStatBase";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cusId = (String)context.getDataValue("cusId");
			String termType = (String)context.getDataValue("termType");
		
		//获得查询的过滤数据
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			
			TableModelUtil.setCustomizeQueryConditionB(" cus_id = '"+cusId+"' and stat_prd_style = '"+termType+"' and substr(state_flg,9,1)='2'", context);
			
			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+" order by last_upd_id desc";
			
			conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			int size = PUBConstant.MAXLINE;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("cus_id");
			list.add("stat_prd_style");
			list.add("stat_prd");
			list.add("stat_style");
			list.add("stat_bs_style_id");
			list.add("state_flg");
			list.add("cus_name");
			list.add("stat_pl_style_id");
			list.add("stat_cf_style_id");
			list.add("stat_fi_style_id");
			list.add("stat_soe_style_id");
			list.add("stat_sl_style_id");
			list.add("input_id");
			list.add("input_br_id");
//			list.add("style_id1");
//			list.add("style_id2");			
//			list.add("stat_acc_style_id");
//			list.add("stat_de_style_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null){
				this.releaseConnection(context, connection);
			}
		}
		return "0";
	}

}
