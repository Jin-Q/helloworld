package com.yucheng.cmis.biz01line.acc.op.accloan;

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

public class QueryAccLoanForAssetListOp extends CMISOperation {


	private final String modelId = "AccLoan";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false) ;
			
			//添加记录级权限	
			if(conditionStr.indexOf("WHERE") != -1){
				conditionStr = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+" and "+conditionStr.substring(6, conditionStr.length()));
			}else {
				conditionStr  = (RestrictUtil.getNewRestrictSelf(this.modelId, connection, context)+conditionStr);
			}
			
			//台账状态为正常的才能进行资产转让
			String openDay = (String)context.getDataValue("OPENDAY");
			if(conditionStr.equals("")){
				conditionStr = " where ACC_STATUS = '1' and end_date>'"+openDay+"'";
			}else{
				conditionStr = conditionStr + " and ACC_STATUS = '1' and end_date>'"+openDay+"'"; 
			}
			
			conditionStr = conditionStr+"order by bill_no desc";  
			
			int size = 10; 
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			TableModelUtil.parsePageInfo(context, pageInfo);

			for(int i=0;i<iColl.size();i++){
				KeyedCollection kc = (KeyedCollection) iColl.get(i);
				String cont_no = (String) kc.getDataValue("cont_no");
				
				KeyedCollection contsubKColl = dao.queryDetail("CtrLoanContSub", cont_no, connection);
				String repay_type = (String) contsubKColl.getDataValue("repay_type");
				String interest_term = (String) contsubKColl.getDataValue("interest_term");
				kc.addDataField("repay_type", repay_type);
				kc.addDataField("interest_term", interest_term);
				
				String ir_accord_type = (String) contsubKColl.getDataValue("ir_accord_type");
				String ir_type = (String) contsubKColl.getDataValue("ir_type");
				//String ruling_ir = (String) contsubKColl.getDataValue("ruling_ir");
				String pad_rate_y = (String) contsubKColl.getDataValue("pad_rate_y");
				String ir_adjust_type = (String) contsubKColl.getDataValue("ir_adjust_type");
				String ir_next_adjust_term = (String) contsubKColl.getDataValue("ir_next_adjust_term");
				String ir_next_adjust_unit = (String) contsubKColl.getDataValue("ir_next_adjust_unit");
				String fir_adjust_day = (String) contsubKColl.getDataValue("fir_adjust_day");
				String ir_float_type = (String) contsubKColl.getDataValue("ir_float_type");
				String ir_float_rate = (String) contsubKColl.getDataValue("ir_float_rate");
				String ir_float_point = (String) contsubKColl.getDataValue("ir_float_point");
				String reality_ir_y = (String) contsubKColl.getDataValue("reality_ir_y");
				String overdue_float_type = (String) contsubKColl.getDataValue("overdue_float_type");
				String overdue_rate = (String) contsubKColl.getDataValue("overdue_rate");
				String overdue_point = (String) contsubKColl.getDataValue("overdue_point");
				String overdue_rate_y = (String) contsubKColl.getDataValue("overdue_rate_y");
				String default_float_type = (String) contsubKColl.getDataValue("default_float_type");
				String default_rate = (String) contsubKColl.getDataValue("default_rate");
				String default_point = (String) contsubKColl.getDataValue("default_point");
				String default_rate_y = (String) contsubKColl.getDataValue("default_rate_y");
				kc.put("ir_accord_type", ir_accord_type);
				kc.put("ir_type", ir_type);
				//kc.addDataField("ruling_ir", ruling_ir);
				kc.put("pad_rate_y", pad_rate_y);
				kc.put("ir_adjust_type", ir_adjust_type);
				kc.put("ir_next_adjust_term", ir_next_adjust_term);
				kc.put("ir_next_adjust_unit", ir_next_adjust_unit);
				kc.put("fir_adjust_day", fir_adjust_day);
				kc.put("ir_float_type", ir_float_type);
				//kc.addDataField("ir_float_rate", ir_float_rate);
				//kc.addDataField("ir_float_point", ir_float_point);
				//kc.addDataField("reality_ir_y", reality_ir_y);
				kc.put("overdue_float_type", overdue_float_type);
				kc.put("overdue_rate", overdue_rate);
				kc.put("overdue_point", overdue_point);
				kc.put("overdue_rate_y", overdue_rate_y);
				kc.put("default_float_type", default_float_type);
				kc.put("default_rate", default_rate);
				kc.put("default_point", default_point);
				kc.put("default_rate_y", default_rate_y);
				
				KeyedCollection contKColl = dao.queryDetail("CtrLoanCont", cont_no, connection);
				String assure_main = (String) contKColl.getDataValue("assure_main");
				kc.put("assure_main", assure_main);
				
				String serno = (String) contKColl.getDataValue("serno");
				KeyedCollection iqpsubKColl = dao.queryDetail("IqpLoanAppSub", serno, connection);
				String repay_term = (String) iqpsubKColl.getDataValue("repay_term");
				String repay_space = (String) iqpsubKColl.getDataValue("repay_space");
				kc.put("repay_term", repay_term);
				kc.put("repay_space", repay_space);
				
			}
			String[] args=new String[] {"cus_id","prd_id","repay_type" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo","PrdRepayMode"};
			String[]modelForeign=new String[]{"cus_id","prdid","repay_mode_id"};
			String[] fieldName=new String[]{"cus_name","prdname","repay_mode_dec"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[]{"fina_br_id"});   
			this.putDataElement2Context(iColl, context);
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
