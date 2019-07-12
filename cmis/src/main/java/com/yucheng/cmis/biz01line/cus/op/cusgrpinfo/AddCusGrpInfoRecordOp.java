package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusBaseRelComponent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusComRelComponent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusRelTree;
import com.yucheng.cmis.biz01line.cus.cusRelTree.util.TreeUtil;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpInfoComponent;
import com.yucheng.cmis.biz01line.cus.group.component.CusGrpMemberComponent;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfo;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpMember;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;

public class AddCusGrpInfoRecordOp extends CMISOperation {
	

	private final String modelId = "CusGrpInfo";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		String parent_cus_id = null;
		String grp_no_value = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			SInfoUtils.addSOrgName(kColl, new String[]{ "manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{ "manager_id"});
			parent_cus_id = (String) kColl.getDataValue("parent_cus_id");
			grp_no_value = (String) kColl.getDataValue("grp_no");
			
			//转换类
			ComponentHelper ccHelper = new ComponentHelper();
			CusGrpInfo cusGrpInfo = new CusGrpInfo();
			cusGrpInfo = (CusGrpInfo) ccHelper.kcolTOdomain(cusGrpInfo, kColl);	
		    //构件业务处理类
			CusGrpInfoComponent cusGrpInfoComponent = (CusGrpInfoComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSGRPINFO,context,connection);
			String cusId = cusGrpInfo.getParentCusId();
			String str = cusGrpInfoComponent.CheakParCusId(cusId);
			if(str.equals("canInsert")){
				cusGrpInfoComponent.addCusGrpInfo(cusGrpInfo);		
				flag = "success";
			}else{
				flag = "have";
			}
			
			// 获得关联客户信息
			CusBaseRelComponent cusRelTreeComponent = null;
			CusRelTree cusRelTree = null;
			Map<String, CusRelTree> treeMap = null;

			cusRelTreeComponent = (CusComRelComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CusComRelComponent, context,connection);

			CusComComponent CusComComponent = (CusComComponent) cusRelTreeComponent.getComponent(PUBConstant.CUSCOM);
			CusCom cusCom = CusComComponent.getCusCom(parent_cus_id);
			CusComComponent.deleteGrpMemberByGrpNo(grp_no_value);
			if (cusCom == null || cusCom.getCusId() == null|| cusCom.getCusId().equals(""))
				throw new EMPException("对公客户信息中不存在该企业！");

			CusBaseComponent CusBaseComponent = (CusBaseComponent) cusRelTreeComponent.getComponent(PUBConstant.CUSBASE);
			CusBase cusBase = CusBaseComponent.getCusBase(parent_cus_id);
			cusRelTree = new CusRelTree();
			cusRelTree.setNodeId(parent_cus_id);
			cusRelTree.setNodeAttribute(TreeUtil.cusCom);
			cusRelTree.setNodeLevel("");
			cusRelTree.setNodeName(cusBase.getCusName());
			cusRelTree.setNodeType("TreeSelf");
			cusRelTree.setPNodeId("-1");
			cusRelTree.setNodeInfo("");
			cusRelTree.setNodeCusType(cusBase.getCusType());
			cusRelTree.setNewParentID("-1");
			cusRelTree.setNewID(StringUtil.getRandomId());
			

			treeMap=cusRelTreeComponent.get3DisplayLayerTree(cusRelTree);
			
			Iterator<?> it = treeMap.entrySet().iterator();
			Map.Entry<?,?> entry = null;
			
			if (it != null) {
				while (it.hasNext()) {
					entry = (Map.Entry<?,?>) it.next();
					String cusType = ((CusRelTree) entry.getValue()).getNodeAttribute();
					if (cusType.trim().equals("TreeCusCom")|| cusType.trim() == "TreeCusCom") {
						cusBase = CusBaseComponent.getCusBase(((CusRelTree) entry.getValue()).getNodeId());
						CusGrpMember cusGrpMember = new CusGrpMember();
						cusGrpMember.setGrpNo(grp_no_value);
						cusGrpMember.setCusId(((CusRelTree) entry.getValue()).getNodeId());
//						cusGrpMember.setCusName(((CusRelTree) entry.getValue()).getNodeName());
						cusGrpMember.setGrpCorreType("4");
						cusGrpMember.setGrpCorreDetail(" ");
						cusGrpMember.setGenType("1");
//						cusGrpMember.setCusType(cusBase.getCusType());
//						if (cusBase.getMainBrId() != null) {
//							cusGrpMember.setMainBrId(cusBase.getMainBrId());
//						} else {
//							cusGrpMember.setMainBrId(" ");
//						}
//						if (cusBase.getCustMgr() != null) {
//							cusGrpMember.setCusManager(cusBase.getCustMgr());
//						} else {
//							cusGrpMember.setCusManager(" ");
//						}
						if (cusBase.getInputBrId() != null) {
							cusGrpMember.setInputBrId(cusBase.getInputBrId());
						} else {
							cusGrpMember.setInputBrId(" ");
						}
						if (cusBase.getInputDate() != null) {
							cusGrpMember.setInputDate(cusBase.getInputDate());
						} else {
							cusGrpMember.setInputDate(" ");
						}
//						if (cusBase.getInputId() != null) {
//							cusGrpMember.setInputUserId(cusBase.getInputId());
//						} else {
//							cusGrpMember.setInputUserId(" ");
//						}
						// 构件业务处理类
						CusGrpMemberComponent cusGrpMemberComponent = (CusGrpMemberComponent) CMISComponentFactory
								.getComponentFactoryInstance()
								.getComponentInstance(PUBConstant.CUSGRPMEMBER,
										context, connection);
						CusGrpMember cgmCheak = new CusGrpMember();
						// 查询该客户是否已存在集团之中
						cgmCheak = cusGrpMemberComponent
								.cheakCusGrpMember(cusGrpMember.getCusId());
						/**
						 * 判断该客户是否有正在进行的一般授信或是一般授信变更操作
						 */
						TableModelDAO dao = this.getTableModelDAO(context);
						//一般授信业务
						IndexedCollection indexColl1 = dao.queryList("LmtApply", " where cus_id ='"+cusGrpMember.getCusId()+"' and approve_status in('000','111','992')", connection);
						if(indexColl1.size()>0){
							throw new CMISException("<"+cusGrpMember.getCusId()+">客户有正在做的一般授信业务，请先完成该客户的一般授信业务");
						}
						//一般变更授信业务
						IndexedCollection indexColl2 = dao.queryList("LmtModApp", " where cus_id ='"+cusGrpMember.getCusId()+"' and approve_status in('000','111','992')", connection);
						if(indexColl2.size()>0){
							throw new CMISException("<"+cusGrpMember.getCusId()+">客户有正在做的一般授信变更业务，请先完成该客户的一般授信变更业务");
						}
						if (cgmCheak.getGrpNo() == null) {
							cusGrpMemberComponent.addCusGrpMember(cusGrpMember);
						}
					}
				}
			}
			
			context.addDataField("flag",flag);
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
