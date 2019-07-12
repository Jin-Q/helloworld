package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchlmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtBatchLmtDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtBatchLmt";
	private final String sigModelId = "LmtSigLmt";
	private final String serno_name = "serno";		

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
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
					
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("CusSameOrg");
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition("CusSameOrg", queryData, context, false, false, false);
			
			int size=10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String batch_cus_no = (String)kColl.getDataValue("batch_cus_no");
		//	String single_quota = (String)kColl.getDataValue("single_quota"); 
			IndexedCollection iColl_sig = dao.queryList(sigModelId, "where batch_serno = '"+serno_value+"'", connection);
			//查询该笔批量授信下的
			conditionStr = conditionStr+"where cus_id in (select cus_id from lmt_batch_corre where batch_cus_no ='"+batch_cus_no+"')"+"order by cus_id  desc";
			IndexedCollection iColl_cus = dao.queryList("CusSameOrg",null ,conditionStr,pageInfo,connection);
			for(int i=0;i<iColl_cus.size();i++){
				KeyedCollection kColl_cus = (KeyedCollection)iColl_cus.get(i);
			    //kColl_cus.addDataField("lmt_amt", single_quota);//页面上字段为授信金额，赋值不应为单户限额 2019/5/21
				if(iColl_sig!=null&&iColl_sig.size()>0){
					for(int j=0;j<iColl_sig.size();j++){
						KeyedCollection kColl_sig = (KeyedCollection)iColl_sig.get(j);
						if(((String)kColl_cus.getDataValue("cus_id")).equals((String)kColl_sig.getDataValue("cus_id"))){
							kColl_cus.addDataField("lmt_amt", (String)kColl_sig.getDataValue("lmt_amt"));
						}
					}
				}
			}
			iColl_cus.setName(iColl_cus.getName()+"List");
			//翻译登记人、登记机构、责任人和管理机构信息
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"input_id","manager_id"});
			this.putDataElement2Context(iColl_cus, context);
			this.putDataElement2Context(kColl, context);
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
