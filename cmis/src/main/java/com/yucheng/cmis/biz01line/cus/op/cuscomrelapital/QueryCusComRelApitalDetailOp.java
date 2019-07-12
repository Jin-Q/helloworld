package com.yucheng.cmis.biz01line.cus.op.cuscomrelapital;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusComRelApitalDetailOp  extends CMISOperation {
	
	private final String modelId = "CusComRelApital";
	
	private final String cus_id_name = "cus_id";
	private final String cus_id_rel_name = "cus_id_rel";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");

			String cus_id_rel_value = null;
			try {
				cus_id_rel_value = (String)context.getDataValue(cus_id_rel_name);
			} catch (Exception e) {}
			if(cus_id_rel_value == null || cus_id_rel_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_rel_name+"] cannot be null!");

			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("cus_id_rel",cus_id_rel_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			//加载component 如果前面已经有实例从工厂中加载，请使用改实例的getComponent(comId) 如cusBaseComponent.getComponent(comId)，以保证事务一致
			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			
			
			//需要从客户信息中获取的字段mapping关系map
			Map<String,String> colMap=new HashMap<String,String>();
			colMap.put("invt_name", "cus_name");
			colMap.put("cert_typ", "cert_type");
			colMap.put("cert_code", "cert_code");

//			if(cert_typ_value.startsWith("2")){
			//登记注册号
        	// reg_code
//			colMap.put("reg_code", "reg_code");
			
			//贷款卡编号
			// loan_card loan_card_id
			colMap.put("loan_card", "loan_card_id");

        	//出资人主营业务
			//invt_main_fld com_main_opt_scp
//			colMap.put("invt_main_fld", "com_main_opt_scp");
//			}
			
//			colMap.put("invt_phone", "phone");
//			colMap.put("invt_addr", "post_addr");
//			colMap.put("inst_code", "com_ins_code");//组织机构代码
			cusBaseComponent.getKCollCusById(kColl, colMap, null, "cus_id_rel");
			
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
