package com.yucheng.cmis.biz01line.cus.op.cussubmitinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CusPubConstant;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusSubmitInfoAndUsersOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusSubmitInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			context.addDataField("flag", "0");

			KeyedCollection kColl = new KeyedCollection(modelId);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String cusId = (String) context.getDataValue("cus_id");
			String receiverIdOld = (String) context.getDataValue(PUBConstant.currentUserId); //当前登陆用户
			
			String condition = " WHERE cus_id='" + cusId + "' and rcv_id='" + receiverIdOld + "' and end_flag = '1' ";

			kColl = (KeyedCollection) dao.queryList(modelId, condition, connection).get(0);
			
			//查询s_user表 将与当前用于相同机构下的用户塞进kColl(过滤掉自己)
			String condition2 = " where actorno in (select actorno from s_roleuser where roleno='"+CusPubConstant.ROLE_JZZY+"' and actorno<>'"+receiverIdOld+"')" +
			//不能移交给休假登记中生效的用户     2014-07-08   唐顺岩
			" AND ACTORNO NOT IN(SELECT ACTORNO FROM S_HOLIDAY_REGISTER WHERE (STATUS='01' AND PLAN_END_DATE>=(SELECT OPENDAY FROM PUB_SYS_INFO))) ";
			List<String>  l = new ArrayList<String> ();
			l.add("actorno");
			l.add("actorname");
			IndexedCollection iColl = dao.queryList("SUser", l, condition2, connection);
			KeyedCollection rk = null;
			IndexedCollection ic = new IndexedCollection("actors");
			for( int i=0; i < iColl.size(); i++){
				rk = (KeyedCollection) iColl.get(i);
				KeyedCollection stateKcoll= new KeyedCollection();
				stateKcoll.put("enname",(String)rk.getDataValue("actorno"));
				stateKcoll.put("cnname", (String)rk.getDataValue("actorname"));
				ic.add(stateKcoll);
			}
			//ic.addDataElement(kColl);
//			kColl.remove("memo");
//			String[] args=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"CusBase"};
//			String[] modelForeign=new String[]{"cus_id"};
//			String[] fieldName=new String[]{"cus_name"};
//			//详细信息翻译时调用			
//			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(ic, context);
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
