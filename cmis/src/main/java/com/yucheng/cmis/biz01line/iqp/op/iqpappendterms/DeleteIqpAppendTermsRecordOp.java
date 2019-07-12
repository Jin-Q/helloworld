package com.yucheng.cmis.biz01line.iqp.op.iqpappendterms;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteIqpAppendTermsRecordOp extends CMISOperation {

	private final String modelId = "IqpAppendTerms";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String append_terms_pk = null;
			try {
				append_terms_pk = (String)context.getDataValue("append_terms_pk");
			} catch (Exception e) {}
			if(append_terms_pk == null || append_terms_pk.length() == 0)
				throw new EMPJDBCException("The value of pk["+append_terms_pk+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				IndexedCollection iColl = dao.queryList("IqpAppendTermsTmp", "where modify_rel_serno='"+modify_rel_serno+"'", connection);//附加条款费用列表数据
				IndexedCollection IATH = dao.queryList("IqpAppendTermsHis", "where modify_rel_serno ='"+modify_rel_serno+"'", connection);
				if(IATH ==null || IATH.size()<=0){
					if(iColl!=null && iColl.size()>0){					
						for(int i=0;i<iColl.size();i++){
							KeyedCollection temp = (KeyedCollection) iColl.get(i);
							temp.put("modify_rel_serno", modify_rel_serno);
							temp.setName("IqpAppendTermsHis");
							dao.insert(temp, connection);
						}
					}
				}
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				Map<String,String> pkMap = new HashMap<String,String>();
				pkMap.put("modify_rel_serno",modify_rel_serno);
				pkMap.put("append_terms_pk",append_terms_pk);
				int count= SqlClient.delete("deleteIqpAppendTermsTmpByMP", pkMap, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}
			}else{
				int count=dao.deleteByPk(modelId, append_terms_pk, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}	
			}
			context.addDataField("flag", "success");
			context.addDataField("msg","");
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg","保存失败，原因:"+ee.getMessage());
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
