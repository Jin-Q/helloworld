package com.yucheng.cmis.biz01line.iqp.op.iqpextensionpvp;

import java.sql.Connection;
import java.util.Iterator;
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

public class QueryIqpExtensionPvpListOp extends CMISOperation {


	private final String modelId = "IqpExtensionPvp";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false)
									+"order by input_date desc,serno desc";
			
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/**modified by lisj 2015-9-25 需求编号：【XD150303015】放款审查岗打回的业务申请信息修改需求 begin**/
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("agr_no");
			list.add("fount_bill_no");
			list.add("fount_cont_no");
			list.add("cus_id");
			list.add("fount_cur_type");
			list.add("extension_amt");
			list.add("extension_rate");
			list.add("extension_date");
			list.add("manager_id");
			list.add("manager_br_id");
			list.add("input_date");
			list.add("approve_status");
			list.add("approve_modify_right");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
			
			String[] args=new String[] { "cus_id","agr_no","fount_bill_no","prd_id"};
			String[] modelIds=new String[]{"CusBase","IqpExtensionAgr","AccLoan","PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","agr_no","bill_no","prdid"};
			String[] fieldName=new String[]{"cus_name","serno","prd_id","prdname"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id","prd_name"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
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
			/**modified by lisj 2015-9-25 需求编号：【XD150303015】放款审查岗打回的业务申请信息修改需求 end**/
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			
		}catch (EMPException ee) {
			ee.printStackTrace();
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
