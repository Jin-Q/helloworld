package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class CheckCusidApplyedOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);

			String cus_id = (String) context.getDataValue("cus_id");
			String menuId = (String) context.getDataValue("menuId");
			String modelId = "";
			String conditionStr = "where cus_id='"+ cus_id + "'" ;
			
			if(menuId.equals("cusCognizApply")){//客户认定申请
				modelId="CusCognizApply";
				conditionStr = "where cus_id='"+ cus_id + "' and approve_status in ('000','111','990','991','992','993')" ;
			}else if(menuId.equals("scaleCognizApp")){//条线认定申请
				modelId="CusScaleApply";
				conditionStr = "where cus_id='"+ cus_id + "' and approve_status in ('000','111','990','991','992','993')" ;
			}else if(menuId.equals("evalOrgCognizApp")){//评估机构认定申请
				modelId="CusOrgApp";
				conditionStr = "where cus_id='"+ cus_id + "' and approve_status in ('000','111','990','991','992','993')" ;
			}else if(menuId.equals("grpCognizApp")||menuId.equals("grpCognizChg")){//集团客户名称校验
				String type = "";
				if(context.containsKey("type"))type = context.getDataValue("type").toString();				
				if(type.equals("singleSubmit")){	//集团单一客户提交校验
					modelId="CusGrpMemberApply";
					conditionStr = "where serno ='"+ cus_id +"' " ;
				}else{	//集团名称校验
					modelId="GrpNameCheck";
					KeyedCollection grp_kcoll = (KeyedCollection)context.getDataElement("CusGrpInfoApply");
					String grp_name = grp_kcoll.getDataValue("grp_name").toString();
					String serno = grp_kcoll.getDataValue("serno").toString();
					
					/**
					 * 1、不与cus_grp_info中集团名称重复，并排除本集团变更的情况。 
					 * 2、不等于Cus_Grp_Info_Apply中所有在途申请的名称，并排本笔记录。
					 */
					conditionStr = "select grp_name from (select grp_name from cus_grp_info " 
							+ " where grp_no not in (select grp_no from Cus_Grp_Info_Apply "
							+ " where  serno = '"+serno+"') union  select grp_name from Cus_Grp_Info_Apply "
							+ " where approve_status not in ('990','997','998') and serno != '"+serno+"') "
							+ " where grp_name = '"+grp_name+"'";
				}
			}else if(menuId.equals("goverFinTerInfo")){//政府融资平台信息
				modelId="CusGoverFinTer";
				if(context.containsKey("bill_no")){
					modelId="CusGoverFinTerBill";
					String bill_no = (String) context.getDataValue("bill_no");
					conditionStr = "where cus_id ='"+ cus_id +"' and bill_no = '"+bill_no+"'" ;
				}
			}
			
			IndexedCollection iColl ;
			if(modelId.equals("GrpNameCheck")){
				iColl = TableModelUtil.buildPageData(null, this.getDataSource(context), conditionStr);
			}else{
				iColl = dao.queryList(modelId, conditionStr , connection);
			}
			
			if(modelId.equals("CusGrpMemberApply")&&context.containsKey("type")){
				if (iColl.size() < 2) {
					context.addDataField("flag", "false");
				} else {
					context.addDataField("flag", "true");
				}
			}else{
				if (iColl.size() > 0) {
					context.addDataField("flag", "false");
				} else {
					context.addDataField("flag", "true");
				}
			}

		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}
}