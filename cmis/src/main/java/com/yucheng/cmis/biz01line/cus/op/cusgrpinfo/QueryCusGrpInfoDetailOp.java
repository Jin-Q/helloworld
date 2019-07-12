package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

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

public class QueryCusGrpInfoDetailOp extends CMISOperation {

	private final String modelId = "CusGrpInfo";

	private final String grp_no_name = "grp_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			String grp_no_value = null;
			try {
				grp_no_value = (String)context.getDataValue(grp_no_name);
			} catch (Exception e) {}
			if(grp_no_value == null || grp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+grp_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			condition=" where grp_no='"+grp_no_value+"'";
			IndexedCollection iCollGrp = dao.queryList(modelId, condition, connection);
			if(iCollGrp!=null&&iCollGrp.size()>0){
				KeyedCollection kColl = (KeyedCollection)iCollGrp.get(0);
//				KeyedCollection kColl = dao.queryAllDetail(modelId, grp_no_value, connection);
				SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id"});
				SInfoUtils.addUSerName(kColl, new String[]{"manager_id"});
				
				SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
				SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
				String cus_id = kColl.getDataValue("parent_cus_id").toString();
				CusBaseComponent cbc = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE, context,connection);
				CusBase cusBase = cbc.getCusBase(cus_id);
				kColl.addDataField("parent_cus_name", cusBase.getCusName());
				kColl.addDataField("parent_org_code", cusBase.getCertCode());
				kColl.addDataField("parent_loan_card", cusBase.getLoanCardId());
				this.putDataElement2Context(kColl, context);
				
				IndexedCollection iColl_CusGrpMember = dao.queryList("CusGrpMember",condition, connection);
				/**翻译客户名称、登记人、登记机构*/
				String[] args=new String[] { "cus_id","cus_id","cus_id" };
				String[] modelIds=new String[]{"CusBase","CusBase","CusBase"};
				String[] modelForeign=new String[]{"cus_id","cus_id","cus_id"};
				String[] fieldName=new String[]{"cus_name","main_br_id","cust_mgr"};
				String[] resultName=new String[]{"cus_name","manager_br_id","manager_id"};
				SystemTransUtils.dealPointName(iColl_CusGrpMember, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				SInfoUtils.addSOrgName(iColl_CusGrpMember, new String[] {"manager_br_id" });
				SInfoUtils.addUSerName(iColl_CusGrpMember, new String[] {"manager_id" });
				this.putDataElement2Context(iColl_CusGrpMember, context);
			}else{
				context.put("existFlag", "none");//集团客户已注销
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
