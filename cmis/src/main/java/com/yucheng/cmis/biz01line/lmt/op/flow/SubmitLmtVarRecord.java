package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.PkGeneratorSet;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;

public class SubmitLmtVarRecord extends CMISOperation {

	private final String modelId = "WfiBizVarRecord";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			String serno = null;
			String nodeId = null;
//			String nodeName = null;
			String instanceId = null;
			IndexedCollection iColl = null;//变更分项
			KeyedCollection kCollSelf = null;//个人授信自助额度信息
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno+"] cannot be null!");
				
			try {
				nodeId = (String)context.getDataValue("nodeId");
			} catch (Exception e) {}
			if(nodeId == null || nodeId.length() == 0)
				throw new EMPJDBCException("The value of pk["+nodeId+"] cannot be null!");
			
//			try {
//				nodeName = (String)context.getDataValue("nodeName");
//			} catch (Exception e) {}
//			if(nodeName == null || nodeName.length() == 0)
//				throw new EMPJDBCException("The value of pk["+nodeName+"] cannot be null!");
			
			try {
				instanceId = (String)context.getDataValue("instanceId");
			} catch (Exception e) {}
			if(instanceId == null || instanceId.length() == 0)
				throw new EMPJDBCException("The value of pk["+instanceId+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//保存之前先清除当前暂存的历史记录
			String currentUserId = (String)context.getDataValue("currentUserId");//当前登录人
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			String commentid = wfiComponent.getCurrentCommId(instanceId, currentUserId, connection, nodeId); 
			wfiComponent.clearWfiVariable(instanceId, nodeId, currentUserId, commentid);
			
			iColl = (IndexedCollection)context.getDataElement("LmtAppDetailsList");
			PkGeneratorSet pkservice = (PkGeneratorSet) context.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
			UNIDGenerator pk = (UNIDGenerator) pkservice.getGenerator("UNID");
			if(iColl.size()>0){
				for(int i=0;i<iColl.size();i++){//期限
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String term = (String)kColl.getDataValue("term");
					KeyedCollection kCollVar = new KeyedCollection(modelId);
					String varKey = "term@"+serno+"@"+kColl.getDataValue("org_limit_code");
					kCollVar.put("pk1", pk.getUNID());
					kCollVar.put("instanceid", instanceId);
					kCollVar.put("nodeid", nodeId);
//					kCollVar.put("nodename", nodeName);
					kCollVar.put("var_key", varKey);
					kCollVar.put("var_name", "期限");
					kCollVar.put("var_type", "string");
					kCollVar.put("op_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					kCollVar.put("input_id", currentUserId);
					kCollVar.put("input_br_id", context.getDataValue("organNo"));
					kCollVar.put("remark", "0");
					kCollVar.put("commentid", commentid);
					kCollVar.put("var_old_value", kColl.getDataValue("term_old"));
					if(term==null || "".equals(term)){//若调整后值为空，则默认没有修改，取修改前值
						kCollVar.put("var_value", kColl.getDataValue("term_old"));
					}else{
						kCollVar.put("var_value", term);
					}
//						String condition = "WHERE INSTANCEID='"+instanceId+"' AND VAR_KEY='"+varKey+"' ORDER BY OP_TIME";
//						IndexedCollection iCollTmp = dao.queryList(modelId, condition, connection);
//						if(iCollTmp.size()>0){
//							KeyedCollection kCollTmp = (KeyedCollection)iCollTmp.get(0);
//							kCollVar.put("var_old_value", kCollTmp.getDataValue("var_key"));
//						}else{
//							String condStr = "WHERE SERNO='"+serno+"' AND ORG_LIMIT_CODE='"+kColl.getDataValue("org_limit_code")+"'";
//							KeyedCollection kCollTmp = dao.queryFirst("LmtAppDetails", null, condStr, connection);
//							kCollVar.put("var_old_value", kCollTmp.getDataValue("term"));
//						}
					dao.insert(kCollVar, connection);
				}
				
				for(int i=0;i<iColl.size();i++){//期限类型
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String term_type = (String)kColl.getDataValue("term_type");
					KeyedCollection kCollVar = new KeyedCollection(modelId);
					String varKey = "termType@"+serno+"@"+kColl.getDataValue("org_limit_code");
					kCollVar.put("pk1", pk.getUNID());
					kCollVar.put("instanceid", instanceId);
					kCollVar.put("nodeid", nodeId);
//					kCollVar.put("nodename", nodeName);
					kCollVar.put("var_key", varKey);
					kCollVar.put("var_name", "期限类型");
					kCollVar.put("var_type", "string");
					kCollVar.put("op_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					kCollVar.put("input_id", currentUserId);
					kCollVar.put("input_br_id", context.getDataValue("organNo"));
					kCollVar.put("remark", "0");
					kCollVar.put("commentid", commentid);
					kCollVar.put("var_old_value", kColl.getDataValue("term_type_old"));
					if(term_type==null || "".equals(term_type)){//若调整后值为空，则默认没有修改，取修改前值
						kCollVar.put("var_value", kColl.getDataValue("term_type_old"));
					}else{
						kCollVar.put("var_value", term_type);
					}
					dao.insert(kCollVar, connection);
				}
				
				for(int i=0;i<iColl.size();i++){//授信金额
					KeyedCollection kColl = (KeyedCollection)iColl.get(i);
					String crd_amt = (String)kColl.getDataValue("crd_amt");
					KeyedCollection kCollVar = new KeyedCollection(modelId);
					String varKey = "crdAmt@"+serno+"@"+kColl.getDataValue("org_limit_code");
					kCollVar.put("pk1", pk.getUNID());
					kCollVar.put("instanceid", instanceId);
					kCollVar.put("nodeid", nodeId);
//					kCollVar.put("nodename", nodeName);
					kCollVar.put("var_key", varKey);
					kCollVar.put("var_name", "授信金额");
					kCollVar.put("var_type", "string");
					kCollVar.put("op_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					kCollVar.put("input_id", currentUserId);
					kCollVar.put("input_br_id", context.getDataValue("organNo"));
					kCollVar.put("remark", "0");
					kCollVar.put("commentid", commentid);
					kCollVar.put("var_old_value", kColl.getDataValue("crd_amt_old"));
					if(crd_amt==null || "".equals(crd_amt)){//若调整后值为空，则默认没有修改，取修改前值
						kCollVar.put("var_value", kColl.getDataValue("crd_amt_old"));
					}else{
						kCollVar.put("var_value", crd_amt);
					}
					dao.insert(kCollVar, connection);
				}
			}
			
			//自助额度信息
			if(context.containsKey("LmtAppIndivTmp")){
				kCollSelf = (KeyedCollection)context.getDataElement("LmtAppIndivTmp");
				String self_amt = (String)kCollSelf.getDataValue("self_amt");
				if(self_amt!=null&&!"".equals(self_amt)){
					KeyedCollection kCollVar = new KeyedCollection(modelId);
					kCollVar.put("pk1", pk.getUNID());
					kCollVar.put("instanceid", instanceId);
					kCollVar.put("nodeid", nodeId);
//					kCollVar.put("nodename", nodeName);
					kCollVar.put("var_key", "self_amt");
					kCollVar.put("var_name", "自助金额");
					kCollVar.put("var_value", self_amt);
					kCollVar.put("var_type", "string");
					kCollVar.put("commentid", commentid);
					kCollVar.put("op_time", TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					kCollVar.put("input_id", currentUserId);
					kCollVar.put("input_br_id", context.getDataValue("organNo"));
					kCollVar.put("remark", "0");
					kCollVar.put("var_old_value", kCollSelf.getDataValue("self_amt_old"));
					
					dao.insert(kCollVar, connection);
				}
			}
			
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
