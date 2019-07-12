package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class SearchIsReplaceOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String cont_no= (String)context.getDataValue("cont_no");
			KeyedCollection kCollCtr = dao.queryDetail("CtrLoanCont", cont_no, connection);
			KeyedCollection kCollCtr2 = dao.queryDetail("CtrRpddscntCont", cont_no, connection);
			if(kCollCtr!=null&&kCollCtr.getDataValue("cont_no")!=null&&!"".equals(kCollCtr.getDataValue("cont_no"))){
				String serno = (String)kCollCtr.getDataValue("serno");
				String prd_id = (String)kCollCtr.getDataValue("prd_id");
				KeyedCollection kColl = new KeyedCollection();
				if(prd_id!=null&&"500024".equals(prd_id)){//出口商业发票融资
					kColl = dao.queryDetail("IqpExportPorderFin", serno, connection);
				}else if(prd_id!=null&&"500025".equals(prd_id)){//出口托收贷款
					kColl = dao.queryDetail("IqpExportCollectCont", serno, connection);
				}else if(prd_id!=null&&"500026".equals(prd_id)){//信用证项下出口押汇
					kColl = dao.queryDetail("IqpCreditExportApp", serno, connection);
				}
				if(kColl.containsKey("is_replace")&&kColl.getDataValue("is_replace")!=null&&!"".equals(kColl.getDataValue("is_replace"))){
					context.put("Is_Replace", kColl.getDataValue("is_replace"));//是否置换（1、是  2、否）
				}else{
					context.put("Is_Replace", "2");//是否置换（1、是  2、否）
				}
			}else{
				if(kCollCtr2.containsKey("rpddscnt_type")&&kCollCtr2.getDataValue("rpddscnt_type")!=null&&!"".equals(kCollCtr2.getDataValue("rpddscnt_type"))
					&&("02".equals(kCollCtr2.getDataValue("rpddscnt_type"))||"04".equals(kCollCtr2.getDataValue("rpddscnt_type")))){
					context.put("Is_Replace", "2");//回购式
				//2014-11-19 Edited by FCL 变更内部转贴现确认书
				}else if (kCollCtr2.containsKey("rpddscnt_type")&&kCollCtr2.getDataValue("rpddscnt_type")!=null&&!"".equals(kCollCtr2.getDataValue("rpddscnt_type"))
						&&("05".equals(kCollCtr2.getDataValue("rpddscnt_type")))&&kCollCtr2.containsKey("prd_id")&&("300022".equals(kCollCtr2.getDataValue("prd_id")))){
					context.put("Is_Replace", "0");//内部转贴现
				}else{
					context.put("Is_Replace", "1");//买断式
					
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
		return "0";
	}

}
