package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPubBailInfo_jointListOp extends CMISOperation {

	private final String modelId = "PubBailInfo";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String serno = null;
		String cont_no = null;
		String cus_id = null;
		String menuId = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
//				serno = (String)context.getDataValue("serno");
//				cont_no = (String)context.getDataValue("cont_no");
				queryData = (KeyedCollection) context.getDataElement(this.modelId);
			} catch (Exception e) {} 
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			if(context.containsKey("agr_no")){
				cont_no = (String)context.getDataValue("agr_no");
			}
			if(context.containsKey("cus_id")){
				cus_id = (String)context.getDataValue("cus_id");
			}
			if(context.containsKey("menuId")){
				menuId = (String)context.getDataValue("menuId");
			}
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg="";
			if(context.containsKey("modiflg")){
				modiflg = (String)context.getDataValue("modiflg");
			}
			String modify_rel_serno = "";
			
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
			}
			context.put("cont_no", cont_no);
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
            if("Agr_Fin_Guar".equals(menuId)){
            	//修改为用授信协议号关联  2014-05-04
            	//conditionStr = " where cus_id ='" + cus_id + "' order by serno desc,cus_id desc,bail_acct_no desc";
            	conditionStr = " where cont_no ='" + cont_no + "' order by serno desc,cus_id desc,bail_acct_no desc";
			}else{
				if(!"".equals(cont_no) && cont_no != null){
					if(!"".equals(modiflg) && "yes".equals(modiflg)){
						if (conditionStr.contains("WHERE")) {
							conditionStr += " and cont_no ='" + cont_no + "' and modify_rel_serno='"+modify_rel_serno+"' order by serno desc,cus_id desc,bail_acct_no desc";
						} else {
							conditionStr += " where cont_no ='" + cont_no + "' and modify_rel_serno='"+modify_rel_serno+"' order by serno desc,cus_id desc,bail_acct_no desc";
						}
					}else{
						if (conditionStr.contains("WHERE")) {
							conditionStr += " and cont_no ='" + cont_no + "' order by serno desc,cus_id desc,bail_acct_no desc";
						} else {
							conditionStr += " where cont_no ='" + cont_no + "' order by serno desc,cus_id desc,bail_acct_no desc";
						}
					}
					
			    }else{
			    	if (conditionStr.contains("WHERE")) {
			    		if(!"".equals(modiflg) && "yes".equals(modiflg)){
							conditionStr += " and serno ='" + serno + "' and modify_rel_serno='"+modify_rel_serno+"' order by serno desc,cus_id desc,bail_acct_no desc";
						}else {
							conditionStr += " and serno ='" + serno + "' order by serno desc,cus_id desc,bail_acct_no desc";
						}
					} else {
						if(!"".equals(modiflg) && "yes".equals(modiflg)){
							conditionStr += " where serno ='" + serno + "' and modify_rel_serno='"+modify_rel_serno+"' order by serno desc,cus_id desc,bail_acct_no desc";
						}else {
							conditionStr += " where serno ='" + serno + "' order by serno desc,cus_id desc,bail_acct_no desc";
						}
						
					}
			    }   
			}
			
			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
			IndexedCollection iColl  = new IndexedCollection();
			if(!"".equals(modiflg) && "yes".equals(modiflg)){
				iColl = dao.queryList("PubBailInfoTmp", null,conditionStr, pageInfo, connection);
				iColl.setName(modelId);
			}else{
				iColl = dao.queryList(modelId, null,conditionStr, pageInfo, connection);
			}
			
			iColl.setName(iColl.getName() + "List");
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			String[] args = new String[] { "cus_id" };
			String[] modelIds = new String[] { "CusBase" };
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "cus_name" };
			// 详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD,context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(iColl, new String[]{"open_org"});
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
