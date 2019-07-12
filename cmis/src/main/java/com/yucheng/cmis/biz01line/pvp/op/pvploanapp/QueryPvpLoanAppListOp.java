package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;
import java.util.Iterator;

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

public class QueryPvpLoanAppListOp extends CMISOperation {
	private final String modelId = "PvpLoanApp";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String conditionStr ="";
		try{  
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			String biz_type = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			/**取得业务模式*/
			if(context.containsKey("biz_type")){
				biz_type = (String)context.getDataValue("biz_type");  
			}
			
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
//			if(conditionStr !=null && !(conditionStr.equals(""))){
//				conditionStr += " and prd_id not in ('300022','300023','300024','600020') and cont_no in (select b.cont_no from ctr_loan_cont b where b.biz_type='"+biz_type+"') order by serno desc,cont_no desc";
//			}else{
//				conditionStr = "where prd_id not in ('300022','300023','300024','600020') and cont_no in (select b.cont_no from ctr_loan_cont b where b.biz_type='"+biz_type+"') order by serno,cont_no desc";
//			}
			conditionStr += " order by input_date desc,serno desc,cont_no desc";
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id","cont_no","prd_id"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","cont_no","prdid"};
			String[] fieldName=new String[]{"cus_name","serno","prdname"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id_displayname"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			/**add by lisj 2015-9-25 需求编号：【XD150303015】放款审查岗打回的业务申请信息修改需求 begin**/
			for(Iterator<KeyedCollection> iterator =iColl.iterator();iterator.hasNext();){
				KeyedCollection temp = (KeyedCollection)iterator.next();
				String approve_status  = (String) temp.getDataValue("approve_status");
				String approve_modify_right = "";
				if(temp.getDataValue("approve_modify_right")==null || "".equals(temp.getDataValue("approve_modify_right"))){
					approve_modify_right ="0";
				}else{
					approve_modify_right = (String) temp.getDataValue("approve_modify_right");
				}
				if("992".equals(approve_status)){//打回状态
					if("1".equals(approve_modify_right)){
						temp.put("status_display", "打回可修改");//审批状态展示字段
					}else{
						temp.put("status_display", approve_status);
					}
				}else{
					temp.put("status_display", approve_status);
				}

			}
			/**add by lisj 2015-9-25 需求编号：【XD150303015】放款审查岗打回的业务申请信息修改需求 end**/
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
