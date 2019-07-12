package com.yucheng.cmis.biz01line.cont.op.ctrlimitcont;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCtrLimitContHisListOp extends CMISOperation {


	private final String modelId = "CtrLimitCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("cont_no");
			list.add("serno");
			list.add("cont_cn");
			list.add("cus_id");
			list.add("cur_type");
			list.add("cont_amt");
			list.add("start_date");
			list.add("end_date");
			list.add("cont_status");
			list.add("memo");
			list.add("manager_br_id");
			list.add("input_id");
			list.add("input_br_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				String manager_id = "";
				String serno = "";
				if(kColl.containsKey("serno")&&kColl.getDataValue("serno")!=null&&!"".equals(kColl.getDataValue("serno"))){
					serno = (String) kColl.getDataValue("serno");
					KeyedCollection kColl4IQP = dao.queryFirst("CusManager", null, "where is_main_manager='1' and serno='"+serno+"'", connection);
					if(kColl4IQP.containsKey("manager_id")&&kColl4IQP.getDataValue("manager_id")!=null&&!"".equals(kColl4IQP.getDataValue("manager_id"))){
						manager_id = (String) kColl4IQP.getDataValue("manager_id");
					}
				}
				kColl.put("manager_id", manager_id);
			}
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase",};
			String[]modelForeign=new String[]{"cus_id"}; 
			String[] fieldName=new String[]{"cus_name"};    
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","input_br_id"});
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
