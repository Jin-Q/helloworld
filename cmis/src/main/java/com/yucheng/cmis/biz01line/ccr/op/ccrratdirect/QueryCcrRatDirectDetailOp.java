package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCcrRatDirectDetailOp  extends CMISOperation {
	
	private final String modelId = "CcrAppInfo";
	//private final String modelId1 = "CcrAppDetail";

	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

//			CcrComponent ccrComponent = (CcrComponent) CMISComponentFactory
//			.getComponentFactoryInstance().getComponentInstance(
//					CcrPubConstant.CCR_COMPONENT, context, connection);
//			CcrAppAll ccrAppAll = ccrComponent.intuil(serno_value);
//			CcrAppInfo ccrAppInfo = ccrAppAll.getCcrAppInfo();
//			CcrAppDetail ccrAppDetail = ccrAppAll.getCcrAppDetail();
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection ccrAppInfoKcoll = dao.queryFirst("CcrAppInfo", null, " where serno = '"+serno_value+"'", connection);
			KeyedCollection ccrAppDetailKcoll = dao.queryFirst("CcrAppDetail", null, " where serno = '"+serno_value+"'", connection);
			SInfoUtils.addUSerName(ccrAppInfoKcoll, new String[] { "manager_id" ,"input_id"});
			SInfoUtils.addSOrgName(ccrAppInfoKcoll, new String[] { "manager_br_id","input_br_id" });
			this.putDataElement2Context(ccrAppInfoKcoll, context);
			this.putDataElement2Context(ccrAppDetailKcoll, context);
			//从客户表获取相关信息
			CusBaseComponent cusBaseComp = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context, connection);
			String cus_id = (String) ccrAppInfoKcoll.getDataValue("cus_id");
			CusBase cus = cusBaseComp.getCusBase(cus_id);
			context.addDataField("cus_name", cus.getCusName());//客户名称
			context.addDataField("cus_type", cus.getCusType());//客户类型
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
		//	super.getHttpServletRequest(context).setAttribute("smallFlag", kColl.getDataValue("com_scale_ccr"));
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id","input_id" });
            SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
            
            String op = (String)context.getDataValue("op");
            String reason_show = (String)ccrAppDetailKcoll.getDataValue("reason_show");
            if("view".equals(op)){
            	if(reason_show != null && reason_show.trim().length()>0){
            		kColl.addDataField("op",op);
            		kColl.addDataField("check_value", "1");
            		context.addDataField("reportId", "ccr/zrscb1_view.raq");
            	}else{
            		kColl.addDataField("op",op);
            		kColl.addDataField("check_value", "2");
            		context.addDataField("reportId", "ccr/zrscb2_view.raq");
            	}
            }else{
            	if(reason_show != null && reason_show.trim().length()>0){
            		kColl.addDataField("check_value", "1");
            		context.addDataField("reportId", "ccr/zrscb1.raq");
            	}else{
            		kColl.addDataField("check_value", "2");
            		context.addDataField("reportId", "ccr/zrscb2.raq");
            	}
            }
            
			this.putDataElement2Context(kColl, context);
			
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
