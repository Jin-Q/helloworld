package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 
*@author lisj
*@time 2015-8-19
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求（展期业务）
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class UpdateBizModifyApp4IEARecordOp extends CMISOperation {

	private final String modelId = "IqpExtensionAgrTmp";
	private final String modelHisId = "IqpExtensionAgrHis";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//保存前先备份IqpExtensionAgrHis
			String modify_rel_serno  = (String) context.getDataValue("modify_rel_serno");
			KeyedCollection kColl4His = dao.queryDetail(modelId, modify_rel_serno, connection);
			kColl4His.setName(modelHisId);

			IndexedCollection checkIEAH = dao.queryList(modelHisId, "where modify_rel_serno ='"+modify_rel_serno+"'", connection);
			//查询是否已存在备份信息
			if(checkIEAH ==null || checkIEAH.size()<=0){
				try {				
					dao.insert(kColl4His, connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//执行打回业务信息修改操作，仅更新可修改的字段信息
			try {
				int count = dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("执行打回业务信息修改操作出错，失败行数:  " + count);
				}
			} catch (Exception e) { e.printStackTrace();
			}
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
