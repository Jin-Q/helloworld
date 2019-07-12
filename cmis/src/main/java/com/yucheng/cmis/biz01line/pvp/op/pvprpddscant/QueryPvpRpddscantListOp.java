package com.yucheng.cmis.biz01line.pvp.op.pvprpddscant;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.RestrictUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPvpRpddscantListOp extends CMISOperation {
	private final String modelId = "PvpLoanApp";
	private final String modelIdCont = "CtrRpddscntCont";  

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String conditionStr ="";
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			//添加记录级权限	
			if(conditionStr.indexOf("WHERE") != -1){
				conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
			}else {
				conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr);
			}
			
			if(conditionStr.equals("")){
				conditionStr = "where approve_status not in('997','111') and prd_id in ('300024','300023','300022')  order by input_date desc,serno desc";
			}else{
				conditionStr = conditionStr +" and approve_status not in('997','111') and prd_id in ('300024','300023','300022')  order by input_date desc,serno desc";
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			String[] args=new String[] { "prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] fieldName=new String[]{"prdname"};
			String[] modelForeign=new String[]{"prdid"};
			//详细信息翻译时调用	
			for(int i=0;i<iColl.size();i++){
				KeyedCollection pvpKColl = (KeyedCollection)iColl.get(i);
				String contno = (String)pvpKColl.getDataValue("cont_no");
				KeyedCollection contKColl = dao.queryDetail(modelIdCont, contno, connection);
				pvpKColl.addDataField("toorg_name", (String)contKColl.getDataValue("toorg_name"));
				SystemTransUtils.dealName(pvpKColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			}
			
			args=new String[] {"cont_no"};
			modelIds=new String[]{modelIdCont};
			modelForeign=new String[]{"cont_no"};
			fieldName=new String[]{"serno"};
			String[] resultName = new String[] { "fount_serno"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","in_acct_br_id"});
			
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
