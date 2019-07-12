package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteGrtLoanRGurZGEOp extends CMISOperation {

	private final String modelIdRel = "GrtLoanRGur";

	private final String pk_id_name = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String pk_id_value = null;
			String serno = null;
			String guar_lvl = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
				serno = (String)context.getDataValue("serno");
				guar_lvl = (String)context.getDataValue("guar_lvl");
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0 || serno == null || serno.length() == 0 || guar_lvl == null || guar_lvl.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			/**删除业务担保合同数据后，下面一条业务担保合同的等级相应-1*/ 
//			if(!"".equals(listPk) && listPk != null){ 
//				String[] list = listPk.split(",");
//				for(int i=0;i<list.length;i++){
//					String pk_id = list[i];
//					KeyedCollection kColl = dao.queryDetail(modelIdRel, pk_id, connection);
//					int guar_lvl =  Integer.parseInt(kColl.getDataValue("guar_lvl").toString());
//					guar_lvl = guar_lvl-1; 
//					kColl.setDataValue("guar_lvl", guar_lvl); 
//					dao.update(kColl, connection);
//				}  
//			}
			Map<String,String> map = new HashMap<String,String>(); 
			map.put("serno", serno);
			map.put("guar_lvl", guar_lvl); 
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
         	int rels = cmisComponent.updateGrtLoanRGurLvlZGE(map, connection); 
			if(rels < 0){
				throw new EMPJDBCException("update failed!");  
			} 
			
			int countRel = dao.deleteByPk(modelIdRel, pk_id_value, connection); 
			if(countRel==1){
				context.addDataField("flag", "success");
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
