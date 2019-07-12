package com.yucheng.cmis.biz01line.iqp.op.iqpappendterms;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAppendTermsListOp extends CMISOperation {


	private final String modelId = "IqpAppendTerms";
	private final String modelIdIqp = "IqpLoanApp";
	private final String modelIdCsgnLoanInfo = "IqpCsgnLoanInfo";//
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			String conditionStr = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {
				throw new Exception("业务编号获取失败，请联系用户管理员!");
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				conditionStr = "where serno= '"+serno+"'and modify_rel_serno ='"+modify_rel_serno+"' order by fee_code desc";
			}else{
			    conditionStr = "where serno= '"+serno+"' order by fee_code desc";
			}
		
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("append_terms_pk");
			list.add("serno");
			list.add("fee_code");
			list.add("fee_type");
			list.add("fee_amt");
			list.add("fee_cur_type");
			list.add("fee_rate");
			list.add("is_cycle_chrg");
			IndexedCollection iColl = new IndexedCollection();
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				list.add("modify_rel_serno");
				iColl = dao.queryList("IqpAppendTermsTmp",list ,conditionStr,pageInfo,connection);
				iColl.setName("IqpAppendTermsList");
			}else{
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
				iColl.setName(iColl.getName()+"List");
			}		
			this.putDataElement2Context(iColl, context);
			
			//从数据库取业务主表申请金额
			KeyedCollection kColl = dao.queryDetail(modelIdIqp, serno, connection);
			String apply_amount = (String)kColl.getDataValue("apply_amount");
			
			KeyedCollection kCollCsgnLoanInfo = (KeyedCollection)dao.queryDetail(modelIdCsgnLoanInfo, serno, connection);
			String chrg_rate = "";
			if(kCollCsgnLoanInfo != null){
				String sernoCsgnLoanInfo = (String)kCollCsgnLoanInfo.getDataValue("serno");
				if(sernoCsgnLoanInfo != null && !"".equals(sernoCsgnLoanInfo)){
					chrg_rate = (String)kCollCsgnLoanInfo.getDataValue("chrg_rate");
					
				}
			}
			context.put("chrg_rate", chrg_rate);
			context.put("apply_amount", apply_amount); 
			TableModelUtil.parsePageInfo(context, pageInfo);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			
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
