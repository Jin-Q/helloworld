package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateSOrgRecordOp extends CMISOperation {
	

	private final String modelId = "SOrg";
	

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
			int count=dao.update(kColl, connection);
			if(count!=1){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "Update Failed! Record Count: " + count);
				return null;
			}
			
			String  tmpOrganno=(String)kColl.getDataValue("organno");
			String  tmpOrganName=(String)kColl.getDataValue("organname");
			OrganizationInitializer.addAndUpdateOrgMapInfo(tmpOrganno, tmpOrganName);
			//SInfoUtils.findAllOrgInfo();
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		    /**add by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,调整机构等级后需更新授权配置信息表  begin**/
			IndexedCollection SWFILCR = dao.queryList("WfiLvCreditRight", "where org_id='"+tmpOrganno+"' and right_type='02'", connection);
			if(SWFILCR!=null && SWFILCR.size() >0){
				KeyedCollection temp = (KeyedCollection) SWFILCR.get(0);
				String old_org_lvl = (String) temp.getDataValue("org_lvl");
				String org_lvl  = (String) kColl.getDataValue("org_lvl");
				//机构等级发生变更，需重新初始化授权配置信息
				if(!old_org_lvl.equals(org_lvl)){
					//更新初始化数据前，删除原配置信息避免重复
					SqlClient.delete("deleteWLCRByOrgId", tmpOrganno , connection);
					IndexedCollection WLCR = dao.queryList("WfiLvCreditRight", " where org_lvl ='"+org_lvl+"' and right_type='01'", connection);//查询变更后基础授权配置信息
					for(Iterator<KeyedCollection> iterator  = WLCR.iterator();iterator.hasNext();){
						KeyedCollection temp4WLCR = (KeyedCollection)iterator.next();
						KeyedCollection kColl4WLCR = new KeyedCollection();
						kColl4WLCR.put("org_id", tmpOrganno);
						kColl4WLCR.put("org_lvl", temp4WLCR.getDataValue("org_lvl"));
						kColl4WLCR.put("belg_line", temp4WLCR.getDataValue("belg_line"));
						kColl4WLCR.put("assure_main", temp4WLCR.getDataValue("assure_main"));
						kColl4WLCR.put("right_type", "02");
						kColl4WLCR.put("new_crd_amt", temp4WLCR.getDataValue("new_crd_amt"));
						kColl4WLCR.put("stock_crd_amt", temp4WLCR.getDataValue("stock_crd_amt"));
						kColl4WLCR.put("sub_new_crd_amt", temp4WLCR.getDataValue("new_crd_amt"));
						kColl4WLCR.put("sub_stock_crd_amt", temp4WLCR.getDataValue("stock_crd_amt"));
						kColl4WLCR.setName("WfiLvCreditRight");
						dao.insert(kColl4WLCR, connection);//初始化配置信息
					}
					
					KeyedCollection  wfiLCRLog = new KeyedCollection();
					String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
					String op_time = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
					wfiLCRLog.put("operation_org_id",tmpOrganno);//当前登录机构
					wfiLCRLog.put("operation_staff", currentUserId);
					wfiLCRLog.put("operation_date", op_time);
					wfiLCRLog.put("right_type", "02");
					wfiLCRLog.put("op_type", "OCinit");//操作类型(机构变更操作)
					wfiLCRLog.setName("WfiLcrLog");
					dao.insert(wfiLCRLog, connection);
				}
			}
			/**add by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,调整机构等级后需更新授权配置信息表  end**/
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "删除失败！失败原因："+ee.getMessage());
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
