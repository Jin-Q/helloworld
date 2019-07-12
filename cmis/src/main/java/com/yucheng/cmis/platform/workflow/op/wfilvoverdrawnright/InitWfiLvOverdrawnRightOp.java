package com.yucheng.cmis.platform.workflow.op.wfilvoverdrawnright;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 
*@author wangj
*@time 2015-5-14
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class InitWfiLvOverdrawnRightOp extends CMISOperation {
	
	private final String modelId = "WfiLvOverdrawnRight";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			String orgId  = (String)kColl.getDataValue("org_id");//获取配置机构码
			String belg_line = (String)kColl.getDataValue("belg_line");//获取条线
			//String is_inuse  = (String)kColl.getDataValue("is_inuse");//获取是否控制
			IndexedCollection WLOR = dao.queryList(modelId, " where org_id='"+orgId+"' and belg_line='"+belg_line+"'", connection);
			if (WLOR != null && WLOR.size() > 0) {
				KeyedCollection kColl2 =(KeyedCollection) WLOR.get(0);
				kColl2.setDataValue("overdrawn_amt", kColl.getDataValue("overdrawn_amt"));
				kColl2.setDataValue("is_inuse", kColl.getDataValue("is_inuse"));
				kColl2.setDataValue("pledge_amt", kColl.getDataValue("pledge_amt"));
				kColl2.setDataValue("impawn_amt", kColl.getDataValue("impawn_amt"));
				kColl2.setDataValue("fullpledge_amt", kColl.getDataValue("fullpledge_amt"));
				kColl2.setDataValue("riskpledge_amt", kColl.getDataValue("riskpledge_amt"));
				kColl2.setDataValue("guarantee_amt", kColl.getDataValue("guarantee_amt"));
				kColl2.setDataValue("credit_amt", kColl.getDataValue("credit_amt"));
				kColl2.setDataValue("is_ctrl", kColl.getDataValue("is_ctrl"));
				dao.update(kColl2, connection);// 更新配置信息
			}else{
				dao.insert(kColl, connection);// 初始化配置信息
			}
			context.put("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.put("flag", PUBConstant.FAIL);
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
