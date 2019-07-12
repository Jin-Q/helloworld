package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
   /**
    * 检查客户是否被引用
    * */
 public class CheckCusInBatchRecord extends CMISOperation {
	
	/**
	 * 在批量包中添加客户，检查客户是否已经在批量包中或者是否已经存在在途的授信申请
	 * */

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cusIds  = "";
			String batch_cus_no = "";
			try {
				cusIds = (String)context.getDataValue("cus_id");
				batch_cus_no = (String)context.getDataValue("batch_cus_no");
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
				String cusArray[] = cusIds.split("\\$");//切割开cus_id
				context.addDataField("flag", PUBConstant.SUCCESS);
				for (int i=0; i<cusArray.length;i++){
					String cusId = cusArray[i];
					
					//调用客户模块接口，获取同业客户信息
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					KeyedCollection cusSameOrgKColl= service.getCusSameOrgKcoll(cusId, context, connection);
					
						String conditionStr = "where cus_id='"+cusId+"' and batch_cus_no='"+batch_cus_no+"'";
						IndexedCollection iColl = dao.queryList("LmtBatchCorre", conditionStr, connection);
						if(iColl !=null && iColl.size()>0){
							context.setDataValue("flag", "Corre&"+cusId + " " + cusSameOrgKColl.getDataValue("same_org_cnname"));//批量包中存在
							break;
						}
						Map<String,String> map = new HashMap<String,String>();
						map.put("cus_id", cusId);	
						map.put("batch_cus_no", batch_cus_no);	
						LmtIntbankComponent libc = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
						libc.insertcus2batchlist(map);
				}
			
		}catch (EMPException ee) {
			throw new EMPException(ee);
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}