package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPvpLoanAppDetailOp  extends CMISOperation {
	
	private final String modelId = "PvpLoanApp";
	private final String serno_name = "serno";
	private final String modelIdStar = "WfiWorklistTodo";
	private final String modelIdEnd = "WfiWorklistEnd";
		
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);

			String biz_type = null;
			String serno_value = null;
			String tran_serno = "";
			String cont_no = "";
			String flag ="pvp";
			String instanceIdPvp = null;
			String ContmodelId = "CtrLoanCont";
			try {
				if(context.containsKey(serno_name)){  
					serno_value = (String)context.getDataValue(serno_name);
				}
				if(context.containsKey("tran_serno")){
					tran_serno = (String)context.getDataValue("tran_serno");
					flag = (String)context.getDataValue("flag");
					serno_value = tran_serno;
					context.setDataValue("flag", flag); 
				}	
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");	
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/**查询流程实例号
			 * 首先查询待办流程列表，如果无结果则去查询办结流程列表，如果无结果不作处理
			 * */
			String condition = "where pk_value='"+serno_value+"'";
			IndexedCollection icollPvp = dao.queryList(modelIdStar, condition, connection);
			if(icollPvp.size()>0){
				KeyedCollection kColl = (KeyedCollection)icollPvp.get(0);
				instanceIdPvp = (String)kColl.getDataValue("instanceid");
			}else{
				IndexedCollection icollEnd = dao.queryList(modelIdEnd, condition, connection);
				if(icollEnd.size()>0){
					KeyedCollection kColl = (KeyedCollection)icollEnd.get(0);
					instanceIdPvp = (String)kColl.getDataValue("instanceid");
				}
			} 
			context.addDataField("instanceIdPvp", instanceIdPvp);
			
			/** 客户名称，产品名称翻译 */
			String[] args=new String[] { "prd_id"};
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[] fieldName=new String[]{"prdname"};
			String[] modelForeign=new String[]{"prdid"};
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			cont_no = (String)kColl.getDataValue("cont_no");
			String prd_id = (String)kColl.getDataValue("prd_id");
			if("600020".equals(prd_id)){
				ContmodelId = "CtrAssetstrsfCont";
				KeyedCollection contKColl = dao.queryAllDetail(ContmodelId, cont_no, connection);
				kColl.addDataField("toorg_name",(String)contKColl.getDataValue("toorg_name"));
			}else if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){
				ContmodelId = "CtrRpddscntCont";
				KeyedCollection contKColl = dao.queryAllDetail(ContmodelId, cont_no, connection);
				kColl.addDataField("toorg_name",(String)contKColl.getDataValue("toorg_name"));
			}else{
				ContmodelId = "CtrLoanCont";
				KeyedCollection contKColl = dao.queryAllDetail(ContmodelId, cont_no, connection);
				biz_type = (String)contKColl.getDataValue("biz_type");
				kColl.addDataField("cn_cont_no",(String)contKColl.getDataValue("cn_cont_no"));
				String[] argsctr=new String[] { "cus_id"};
				String[] modelIdsctr=new String[]{"CusBase"};
				String[] fieldNamectr=new String[]{"cus_name"};
				String[] modelForeignctr=new String[]{"cus_id"};
				SystemTransUtils.dealName(kColl, argsctr, SystemTransUtils.ADD, context, modelIdsctr,modelForeignctr, fieldNamectr);
			    context.put("biz_type", biz_type);
			    /**add by lisj 2015-9-7 需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			    IndexedCollection iColl4CLCT = dao.queryList("PvpBizModifyRel", "where biz_serno='"+serno_value+"' and approve_status = '997' order by update_time desc ", connection);
			    if(iColl4CLCT!=null && iColl4CLCT.size()>0){
			    	KeyedCollection kColl4CLCT = (KeyedCollection) iColl4CLCT.get(0);
			    	String modify_rel_serno = (String)kColl4CLCT.getDataValue("modify_rel_serno");
			    	context.put("modify_rel_serno", modify_rel_serno);
			    }
			    /**add by lisj 2015-9-7 需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			}
			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			args=new String[] { "cont_no"};
			modelIds=new String[]{"CtrLoanCont"};
			modelForeign=new String[]{"cont_no"};
			fieldName=new String[]{"serno"};
			String[] resultName = new String[] { "fount_serno"};
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			this.putDataElement2Context(kColl, context);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","in_acct_br_id"});
			this.putDataElement2Context(kColl, context);
			
			if(context.containsKey("prd_id")){
				context.setDataValue("prd_id", kColl.getDataValue("prd_id"));
			}else{
				context.addDataField("prd_id", kColl.getDataValue("prd_id"));
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
