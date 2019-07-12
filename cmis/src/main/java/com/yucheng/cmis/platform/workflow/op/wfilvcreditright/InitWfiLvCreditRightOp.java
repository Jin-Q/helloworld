package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 
*@author lisj
*@time 2015-3-16
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*			                   初始化授权配置Op
*@version v1.0
*
 */
public class InitWfiLvCreditRightOp extends CMISOperation {
	
	private final String modelId = "WfiLvCreditRight";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			TableModelDAO dao = this.getTableModelDAO(context);

			String organNo =(String)context.getDataValue("organNo");
			String orgId  = (String)context.getDataValue("org_id");//获取配置机构码
			KeyedCollection  OrgInfo = dao.queryDetail("SOrg", orgId, connection);
			if((OrgInfo.getDataValue("org_lvl"))!=null && !"".equals(OrgInfo.getDataValue("org_lvl"))){	
				IndexedCollection WLCR = dao.queryList(modelId, " where org_lvl ='"+OrgInfo.getDataValue("org_lvl").toString()+"' and right_type='01'", connection);
				if(WLCR!=null && WLCR.size()>0){
					//更新初始化数据前，删除原配置信息避免重复
					SqlClient.delete("deleteWLCRByOrgId", orgId , connection);
					for(Iterator<KeyedCollection> iterator  = WLCR.iterator();iterator.hasNext();){
						KeyedCollection temp = (KeyedCollection)iterator.next();
						KeyedCollection kColl = new KeyedCollection();
						kColl.put("org_id", orgId);
						kColl.put("org_lvl", temp.getDataValue("org_lvl"));
						kColl.put("belg_line", temp.getDataValue("belg_line"));
						kColl.put("assure_main", temp.getDataValue("assure_main"));
						kColl.put("right_type", "02");
						kColl.put("new_crd_amt", temp.getDataValue("new_crd_amt"));
						kColl.put("stock_crd_amt", temp.getDataValue("stock_crd_amt"));
						kColl.put("sub_new_crd_amt", temp.getDataValue("new_crd_amt"));
						kColl.put("sub_stock_crd_amt", temp.getDataValue("stock_crd_amt"));
						kColl.setName(modelId);
						dao.insert(kColl, connection);//初始化配置信息
					}
				}
			}
			context.put("flag", PUBConstant.SUCCESS);
			try {
				KeyedCollection  wfiLCRLog = new KeyedCollection();
				String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
				String op_time = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
				wfiLCRLog.put("operation_org_id",organNo);//当前登录机构
				wfiLCRLog.put("operation_staff", currentUserId);
				wfiLCRLog.put("operation_date", op_time);
				wfiLCRLog.put("right_type", "02");
				wfiLCRLog.put("op_type", "init");//操作类型
				wfiLCRLog.setName("WfiLcrLog");
				dao.insert(wfiLCRLog, connection);
			} catch (Exception e) {
				context.put("flag", "writelogEx");
			}
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
