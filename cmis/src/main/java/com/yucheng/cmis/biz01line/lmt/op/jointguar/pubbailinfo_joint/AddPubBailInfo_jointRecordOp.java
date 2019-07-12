package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddPubBailInfo_jointRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PubBailInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			kColl.put("bail_acct_no", kColl.getDataValue("bail_acct_no").toString().trim());
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			String cont_no="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(context.containsKey("cont_no")){
				cont_no = (String) context.getDataValue("cont_no");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				IndexedCollection iColl = dao.queryList("PubBailInfoTmp", "where modify_rel_serno='"+modify_rel_serno+"'", connection);
				IndexedCollection PBIH = dao.queryList("PubBailInfoHis", "where modify_rel_serno ='"+modify_rel_serno+"'", connection);
				if(PBIH ==null || PBIH.size()<=0){
					if(iColl!=null && iColl.size()>0){					
						for(int i=0;i<=iColl.size();i++){
							KeyedCollection temp = (KeyedCollection) iColl.get(i);
							temp.put("modify_rel_serno", modify_rel_serno);
							temp.put("cont_no", cont_no);
							temp.setName("PubBailInfoHis");
							dao.insert(temp, connection);
						}
					}
				}
				kColl.put("modify_rel_serno", modify_rel_serno);
				kColl.setName("PubBailInfoTmp");
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			dao.insert(kColl, connection);
			context.addDataField("flag", "suc");
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
