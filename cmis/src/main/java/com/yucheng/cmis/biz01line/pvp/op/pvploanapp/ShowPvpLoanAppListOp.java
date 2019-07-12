package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class ShowPvpLoanAppListOp extends CMISOperation {


	private final String modelId = "PvpLoanApp";
	private final String contModelId = "CtrLoanCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String currentUserId = (String)context.getDataValue("currentUserId");
		String openDay = (String)context.getDataValue("OPENDAY");
		
		try{
			connection = this.getConnection(context);
			String biz_type = null;
			if(context.containsKey("biz_type")){
				biz_type = (String)context.getDataValue("biz_type");
			}
			//String conditionStr ="where cont_status='200' and cont_no not in(select a.cont_no from pvp_loan_app a where a.approve_status not in('998')) and biz_type='"+biz_type+"' and ((cont_start_date<='"+openDay+"' and cont_end_date>='"+openDay+"') or cont_start_date is null) ";
			String conditionStr = "";
			/** 记录集权限 */ 
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			IndexedCollection iColl = dao.queryList(contModelId, null,conditionStr,pageInfo,connection);
			
			
			
			iColl.setName(iColl.getName()+"List");
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
