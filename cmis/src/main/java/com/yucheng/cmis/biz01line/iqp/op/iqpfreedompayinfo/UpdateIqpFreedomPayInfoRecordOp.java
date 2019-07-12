package com.yucheng.cmis.biz01line.iqp.op.iqpfreedompayinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdateIqpFreedomPayInfoRecordOp extends CMISOperation {	

	private final String modelId = "IqpFreedomPayInfo";	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			IndexedCollection iColl = (IndexedCollection)context.getDataElement("IqpFreedomPayInfoList");
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			String serno = "";
			if (iColl != null) {
				for (int i = 0; i < iColl.size(); i++) {
					KeyedCollection kc = (KeyedCollection) iColl.get(i);
					if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
						kc.setName("IqpFreedomPayInfoTmp");
						kc.put("modify_rel_serno", modify_rel_serno);
					}else{
						kc.setName(modelId);
					}
					serno = (String) kc.getDataValue("serno");
					int num = dao.update(kc, connection);
					if(num == 0){
						dao.insert(kc, connection);
					}
				}
			}
			
			//保存后得重新设置期号
			if(!"".equals(serno)){
				IndexedCollection payPlanIColl = new IndexedCollection();
				if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
					payPlanIColl = dao.queryList("IqpFreedomPayInfoTmp", " where modify_rel_serno = '"+modify_rel_serno+"' order by pay_date asc ", connection);
					SqlClient.executeUpd("deletePayPlanTmp", modify_rel_serno, null, null, connection);//删除
				}else{
					payPlanIColl = dao.queryList(modelId, " where serno = '"+serno+"' order by pay_date asc ", connection);
					SqlClient.executeUpd("deletePayPlan", serno, null, null, connection);//删除
				}
				
				for(int i=1;i<=payPlanIColl.size();i++){
					KeyedCollection kColl = (KeyedCollection) payPlanIColl.get(i-1);
					kColl.setDataValue("dateno", i);
					dao.insert(kColl, connection);
				}
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			context.addDataField("flag", PUBConstant.SUCCESS);
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