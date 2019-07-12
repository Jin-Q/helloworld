package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGetOutPvpListOp extends CMISOperation {
	private final String modelId = "PvpLoanApp";
	private final String modelIdCont = "CtrLoanCont";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String conditionStr ="";
			KeyedCollection queryData = null;
			String currentUserId = (String)context.getDataValue("currentUserId");
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelIdCont);  
			} catch (Exception e) {}  
			
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);  
			
			if(conditionStr !=null && !(conditionStr.equals(""))){
				conditionStr += " and biz_type='7'";
			}else{
				conditionStr = "where  biz_type='7'";
			} 
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelIdCont,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			/**----------实时计算合同的合同余额--start---------------*/
			Double pvp_amt_other =0.00;
			
			Double cont_balance =0.00;
			for(int i=0; i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String cont_no = (String)kColl.getDataValue("cont_no");
				String contBalan = TagUtil.replaceNull4String(kColl.getDataValue("cont_balance"));
				if(contBalan != null && !"".equals(contBalan)){
					cont_balance = Double.valueOf(contBalan);
				}
				//统计审批中的待发起的出账占用的合同额度
				String condition ="where cont_no='"+cont_no+"' and approve_status in ('111','000')";
				IndexedCollection iCollPvp = dao.queryList(modelId, condition, connection);
				Double pvp_amt_all =0.00;//审批中的出账占用合同金额
				for(int j=0;j<iCollPvp.size();j++){
					KeyedCollection kCollPvp = (KeyedCollection)iCollPvp.get(j);
					String pvp_amt = TagUtil.replaceNull4String(kCollPvp.getDataValue("pvp_amt"));
					if(pvp_amt != null && !"".equals(pvp_amt)){
						pvp_amt_other = Double.valueOf(pvp_amt);
					}
					pvp_amt_all += pvp_amt_other;
				}
				cont_balance -=pvp_amt_all;
				kColl.setDataValue("cont_balance", cont_balance);
			}
			/**----------实时计算合同的合同余额----end-------------*/
			
			String[] args=new String[] { "prd_id","cus_id" };
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase"};
			String[]modelForeign=new String[]{"prdid ","cus_id"};
			String[] fieldName=new String[]{"prdname","cus_name"};
			//详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName); 
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			
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
