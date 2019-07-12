package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryCusGrpInfoApplyDetailOp extends CMISOperation {

	private final String modelId = "CusGrpInfoApply";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
			CusBaseComponent cbc = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context,connection);
			String cus_id = kColl.getDataValue("parent_cus_id").toString();
			CusBase cusBase = cbc.getCusBase(cus_id);
            kColl.addDataField("parent_cus_name", cusBase.getCusName());
            kColl.addDataField("parent_org_code", cusBase.getCertCode());
            kColl.addDataField("parent_loan_card", cusBase.getLoanCardId());
		
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id", "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"manager_id", "input_id"});
			this.putDataElement2Context(kColl, context);

			condition=" where serno ='"+serno_value+"'";
			IndexedCollection iColl_CusGrpMemberApply = dao.queryList("CusGrpMemberApply",condition, connection);
			SInfoUtils.addSOrgName(iColl_CusGrpMemberApply, new String[]{"input_br_id"});
			SInfoUtils.addUSerName(iColl_CusGrpMemberApply, new String[]{"input_id"});

			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl_CusGrpMemberApply, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(iColl_CusGrpMemberApply, context);
			
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
