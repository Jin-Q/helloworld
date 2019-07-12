package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdBasicinfoDetailOp  extends CMISOperation {
	private final String modelId = "PrdBasicinfo";
	private final String modelIdRepayMode = "PrdRepayMode";
	private final String prdid_name = "prdid";

	/**
	 * 产品配置获得修改页面op
	 * 执行操作：
	 * 1.获取产品配置基本信息内容封装为PrdBasicinfoIColl
	 * 2.获取政策策略配置信息内容封装为PrdSchemeIColl
	 * 3.获取还款方式配置信息内容封装为RePayIColl
	 * 4.获取费用配置信息内容封装为CostsetIColl
	 * .....内容增加做相应的IColl扩展
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/*if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}*/
			String query = "";
			String prdid_value = null;
			try {
				prdid_value = (String)context.getDataValue(prdid_name);
				if(context.containsKey("query")){
					query = (String)context.getDataValue("query");
				}
			} catch (Exception e) {}
			if(prdid_value == null || prdid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+prdid_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, prdid_value, connection);
			
			String[] args=new String[] {"supcatalog","cus_id","repay_type","repay_type" };
			String[] modelIds=new String[]{"PrdCatalog","CusBase","PrdRepayMode","PrdRepayMode"};
			String[]modelForeign=new String[]{"catalogid","cus_id","repay_mode_id","repay_mode_id"}; 
			String[] fieldName=new String[]{"catalogname","belg_line","repay_mode_dec","repay_mode_type"};
			String[] resultName = new String[] { "supcatalog_displayname","belg_line","repay_type_displayname","repay_mode_type"};
		    //详细信息翻译时调用	
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			SInfoUtils.addUSerName(kColl, new String[]{"inputid"});
			SInfoUtils.addSOrgName(kColl, new String[]{"orgid"});
			
			this.putDataElement2Context(kColl, context);
			
			if(context.containsKey("query")){
				context.setDataValue("query", query);
			}else {
				context.addDataField("query", query);
			}
			
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
