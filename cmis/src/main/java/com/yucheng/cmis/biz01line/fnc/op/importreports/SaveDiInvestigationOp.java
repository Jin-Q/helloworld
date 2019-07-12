package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class SaveDiInvestigationOp extends CMISOperation {

	/**
	 * 保存抵好贷调查表
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection mainInfo = null;
			IndexedCollection mtgList = null;
			IndexedCollection partyList = null;
			
			try {
				mainInfo = (KeyedCollection)context.getDataElement("MainInfo");
				mtgList = (IndexedCollection)context.getDataElement("MtgList");
				partyList = (IndexedCollection)context.getDataElement("PartyList");
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection tempKcoll = null;
			
			if(mainInfo != null){
				mainInfo.setName("IqpMeFncDi");
				dao.update(mainInfo, connection);
			}
			if(mtgList != null){
				for(int i=0;i<mtgList.size();++i){
					tempKcoll = (KeyedCollection)mtgList.get(i);
					tempKcoll.setName("IqpMeFncDiMtg");
					dao.update(tempKcoll, connection);
				}
			}
			if(partyList != null){
				for(int i=0;i<partyList.size();++i){
					tempKcoll = (KeyedCollection)partyList.get(i);
					tempKcoll.setName("IqpMeFncDiParty");
					dao.update(tempKcoll, connection);
				}
			}
			context.put("flag", "success");
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "保存抵好贷调查表失败！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "保存抵好贷调查表失败！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
