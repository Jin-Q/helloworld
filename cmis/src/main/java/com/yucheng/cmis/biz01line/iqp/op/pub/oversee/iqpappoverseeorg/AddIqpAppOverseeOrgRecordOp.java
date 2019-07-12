package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpAppOverseeOrgRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAppOverseeOrg";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			String serno = (String)kColl.getDataValue("serno");
			
			IndexedCollection iCollUnderstore = dao.queryList("IqpOverseeUnderstore", "where serno='"+serno+"'", connection);//下属仓库
			IndexedCollection iCollCusinfo = dao.queryList("IqpOverseeCusinfo", "where serno='"+serno+"'", connection);//前五大客户信息
			IndexedCollection iCollManager = dao.queryList("IqpOverseeManager", "where serno='"+serno+"'", connection);//主要管理人员
			IndexedCollection iCollAssent = dao.queryList("IqpOverseeAssent", "where serno='"+serno+"'", connection);//资产
			
			String main_br_id = context.getDataValue("IqpAppOverseeOrg.manager_br_id").toString();
			String serno_value =CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", main_br_id, connection, context);
			kColl.setDataValue("serno", serno_value);
			dao.insert(kColl, connection);
			//插入下属仓库表
			for(int i=0;i<iCollUnderstore.size();i++){
				//自动生成仓库编号
				String store_id = CMISSequenceService4JXXD.querySequenceFromDB("SD", "all", connection, context);
				KeyedCollection kCollUnderstore = (KeyedCollection) iCollUnderstore.get(i);
				kCollUnderstore.setDataValue("store_id", store_id);
				kCollUnderstore.setDataValue("serno", serno_value);
				dao.insert(kCollUnderstore, connection);
			}
			//插入前五大客户信息表
			for(int i=0;i<iCollCusinfo.size();i++){
				//自动生成客户信息编号
				String cusinfo_id = CMISSequenceService4JXXD.querySequenceFromDB("SD", "all", connection, context);
				KeyedCollection kCollCusinfo = (KeyedCollection) iCollCusinfo.get(i);
				kCollCusinfo.setDataValue("cusinfo_id", cusinfo_id);
				kCollCusinfo.setDataValue("serno", serno_value);
				dao.insert(kCollCusinfo, connection);
			}
			//插入主要管理人员表
			for(int i=0;i<iCollManager.size();i++){
				//自动生成主要管理人员信息编号
				String manager_id = CMISSequenceService4JXXD.querySequenceFromDB("SD", "all", connection, context);
				KeyedCollection kCollManager = (KeyedCollection) iCollManager.get(i);
				kCollManager.setDataValue("manager_id", manager_id);
				kCollManager.setDataValue("serno", serno_value);
				dao.insert(kCollManager, connection);
			}
			//插入资产表
			for(int i=0;i<iCollAssent.size();i++){
				//自动生成资产编号
				String assent_id = CMISSequenceService4JXXD.querySequenceFromDB("SD", "all", connection, context);
				KeyedCollection kCollAssent = (KeyedCollection) iCollAssent.get(i);
				kCollAssent.setDataValue("assent_id", assent_id);
				kCollAssent.setDataValue("serno", serno_value);
				dao.insert(kCollAssent, connection);
			}
			
			context.addDataField("serno", serno_value);
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
