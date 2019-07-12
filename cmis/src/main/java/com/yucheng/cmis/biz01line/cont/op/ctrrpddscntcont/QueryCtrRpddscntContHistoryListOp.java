package com.yucheng.cmis.biz01line.cont.op.ctrrpddscntcont;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

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

public class QueryCtrRpddscntContHistoryListOp extends CMISOperation {


	private final String modelId = "CtrRpddscntCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			//添加记录级权限	
			if(conditionStr.indexOf("WHERE") != -1){
				conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
			}else {
				conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr);
			}
			
//			if(conditionStr.equals("")){
//				conditionStr = " where cont_status !='100' order by serno desc,cont_no desc,input_date desc";
//			}else{
//				conditionStr = conditionStr+" and cont_status !='100' order by serno desc,cont_no desc,input_date desc";
//			}
			conditionStr +=" order by serno desc,cont_no desc,input_date desc";
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cont_no");
			list.add("batch_no");
			list.add("prd_id");
			list.add("rpddscnt_type");
			list.add("toorg_no");
			list.add("toorg_name");
			list.add("bill_type");
			list.add("bill_total_amt");
			list.add("bill_qnt");
			list.add("rpay_amt");
			list.add("manager_br_id");
			list.add("cont_status");
			list.add("input_id");
			list.add("ser_date");
			
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

			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[]modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);

			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			
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
