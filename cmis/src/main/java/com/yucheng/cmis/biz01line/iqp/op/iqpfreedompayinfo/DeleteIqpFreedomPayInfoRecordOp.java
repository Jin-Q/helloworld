package com.yucheng.cmis.biz01line.iqp.op.iqpfreedompayinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteIqpFreedomPayInfoRecordOp extends CMISOperation {

	private final String modelId = "IqpFreedomPayInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String flag = PUBConstant.SUCCESS;
			String dateno = (String)context.getDataValue("dateno");
			String serno = (String)context.getDataValue("serno");
			
			Map<String, String> param = new HashMap();
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			param.put("dateno", dateno);
			param.put("serno", serno);
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				int count = dao.deleteByPks("IqpFreedomPayInfoTmp", param, connection);
				if (count != 1) {
					throw new EMPException("Remove Failed! Records :" + count);
				}
			}else{
				int count = dao.deleteByPks(modelId, param, connection);
				if (count != 1) {
					throw new EMPException("Remove Failed! Records :" + count);
				}
			}
				
			//删除后得重新设置期号
			IndexedCollection iColl = new IndexedCollection();
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				iColl = dao.queryList("IqpFreedomPayInfoTmp", " where modify_rel_serno = '"+modify_rel_serno+"' order by pay_date asc ", connection);
				SqlClient.executeUpd("deletePayPlanTmp", modify_rel_serno, null, null, connection);//删除
			}else{
				iColl = dao.queryList(modelId, " where serno = '"+serno+"' order by pay_date asc ", connection);
				SqlClient.executeUpd("deletePayPlan", serno, null, null, connection);//删除
			}

			for(int i=1;i<=iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i-1);
				kColl.setDataValue("dateno", i);
				dao.insert(kColl, connection);
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			context.addDataField("flag", flag);
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
