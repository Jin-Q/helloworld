package com.yucheng.cmis.biz01line.iqp.op.iqpextensionpvp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class ExtensionAgrNoPop extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {			
            connection = this.getConnection(context);         
            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection)context.getDataElement("ExtensionAgrPop");
            } catch (Exception e) {}
            String conditionStr = TableModelUtil.getQueryCondition("IqpExtensionPvp", queryData, context, false, true, false);
            if(conditionStr.length()==0){
            	conditionStr = "where 1 = 1 ";
            }
            
            /*** 外模块条件拼装 ***/
            String org_id = "";
            String out_condition = "";
            //modified by yangzy 2015/04/24 需求：XD150325024，集中作业扫描岗权限改造 start
			String currentUserId = (String) context.getDataValue("currentUserId");
            if(context.containsKey("orgId")){
            	org_id=context.getDataValue("orgId").toString();
            	out_condition = out_condition + " and manager_id = '"+currentUserId+"' and ( MANAGER_BR_ID = '"+org_id+"' or INPUT_BR_ID = '"+org_id+"' )";
            }
            //modified by yangzy 2015/04/24 需求：XD150325024，集中作业扫描岗权限改造 end
            /*** 取展期协议信息 ***/
			String sql = "select * from iqp_extension_agr "
					+ conditionStr+out_condition+" and status = '200' and agr_no not in(select agr_no from Iqp_Extension_Pvp where approve_status not in('990','998','997')) and agr_no not in (select agr_no from Iqp_Extension_Pvp a,pvp_authorize b where approve_status = '997' and a.fount_bill_no = b.bill_no and b.status!='03')";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);

			/*** 翻译产品 ***/
			SInfoUtils.addUSerName(iColl, new String[] { "input_id","manager_id" });
			SInfoUtils.addSOrgName(iColl, new String[] { "input_br_id","manager_br_id" });
			String[] args=new String[] { "cus_id" ,"fount_bill_no"};
			String[] modelIds=new String[]{"CusBase","AccLoan"};
			String[] modelForeign=new String[]{"cus_id","bill_no"};
			String[] fieldName=new String[]{"cus_name","prd_id"};
			String[] resultName = new String[] { "cus_id_displayname","prd_id"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
		    
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}