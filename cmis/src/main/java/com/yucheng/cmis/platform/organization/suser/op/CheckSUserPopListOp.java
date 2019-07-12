package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class CheckSUserPopListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "STeamMem";
//	private final String modelIdA = "SDeptuser";
	private final String modelIdB = "SOrg";//机构表
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
//		String team_no = "";
		try{
			connection = this.getConnection(context);
			TableModelDAO  dao = this.getTableModelDAO(context);
			String manager_id = (String) context.getDataValue("manager_id");
			IndexedCollection icteam = dao.queryList(modelId, null, "where mem_no = '"+manager_id+"'", connection);
			if(icteam.size()==0){//不属于某个团队
//				context.addDataField("flag","noteam");
//				IndexedCollection icmodelIdA = dao.queryList(modelIdA, null, "where actorno = '"+manager_id+"'", connection);
				String condition = " where organno in (select organno from s_deptuser where actorno='"+manager_id+"')";
				IndexedCollection iCollOrgs = dao.queryList(modelIdB, condition, connection);
				if(iCollOrgs.size()>1){//属于多个机构
					context.put("flag","more");
					context.put("org","");
					context.put("orgName", "");
				}else if(iCollOrgs.size()==1){//只属于一个机构
					/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
					KeyedCollection kColl4STI = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, connection);
					//判断是否存在团队		
					if(kColl4STI!=null&&kColl4STI.getDataValue("team_no")!=null&&!"".equals(kColl4STI.getDataValue("team_no"))){
						String team_no = (String) kColl4STI.getDataValue("team_no");
						context.put("flag","belg2team");
						context.put("org","");
						context.put("orgName", "");
						context.put("teamNo", team_no);
					}else{
						KeyedCollection kCollOrg = (KeyedCollection)iCollOrgs.get(0);
						context.put("flag","one");
						context.put("org",kCollOrg.getDataValue("organno"));
						context.put("orgName", kCollOrg.getDataValue("organname"));	
					}
					/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
				}else{
					context.put("flag", "no");
					context.put("org", "");
					context.put("orgName", "");
					//throw new EMPException("用户["+manager_id+"]没有配置所属机构！");
				}
			}else{
				context.put("flag","yteam");
				context.put("org","");
				context.put("orgName", "");
			}
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
