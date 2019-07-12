package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusBaseRelComponent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusRelTree;
import com.yucheng.cmis.biz01line.cus.cusRelTree.util.TreeUtil;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.StringUtil;
/**
 *  @author lisj
 *	@time 2014-11-26
 *  @description 需求编号：【XD141107075】 
 *  			   校验客户关联关系信息Op
 *  @version v1.0
 */
public class CkeckCusRelInfoOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String  cus_id = null;
		CusBaseRelComponent cusBaseRelComponent=null;
		CusBaseRelComponent cusBaseRelComponent4CusIndiv =null;
		CusRelTree cusRelTree=null;
		Map<String,CusRelTree> treeMap=null;
		try {
			if(context.containsKey("cus_id"))
			  cus_id = (String)context.getDataValue("cus_id");
		} catch (Exception e) {e.printStackTrace();}
		if(cus_id==null||cus_id.equals("")) 
			throw new EMPException("客户码为空！");
		try{
			connection = this.getConnection(context); 
			cusBaseRelComponent = (CusBaseRelComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CusComRelComponent,context,connection);
			cusBaseRelComponent4CusIndiv = (CusBaseRelComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
				PUBConstant.CusIndivRelComponent,context,connection);

			CusBaseComponent CusBaseComponent = (CusBaseComponent) cusBaseRelComponent.getComponent(PUBConstant.CUSBASE);
			CusBaseComponent CusBaseComponent4CusIndvi = (CusBaseComponent) cusBaseRelComponent4CusIndiv.getComponent(PUBConstant.CUSBASE);
			//对公客户查询
			CusCom cusCom=CusBaseComponent.getCusCom(cus_id);
			//个人客户查询
			CusIndiv cusIndiv=CusBaseComponent4CusIndvi.getCusIndiv(cus_id);
			if(cusCom !=null && cusCom.getCusId()!=null && !cusCom.getCusId().equals("")){
				
				CusBase cusBase=CusBaseComponent.getCusBase(cus_id);
				cusRelTree=new CusRelTree();
				cusRelTree.setNodeId(cus_id);
				cusRelTree.setNodeAttribute(TreeUtil.cusCom);
				cusRelTree.setNodeLevel("");
				cusRelTree.setNodeName(cusBase.getCusName());
				cusRelTree.setNodeType("TreeSelf");
				cusRelTree.setPNodeId("-1");
				cusRelTree.setNodeInfo("");
				cusRelTree.setNodeCusType(cusBase.getCusType());
				cusRelTree.setNewParentID("-1");
				cusRelTree.setNewID(StringUtil.getRandomId());
				treeMap=cusBaseRelComponent.get3DisplayLayerTree(cusRelTree);

				if(treeMap.size()>1){
					context.addDataField("flag","existence");
				}else{
					context.addDataField("flag","inexistence");
				}
			}else if(cusIndiv !=null && cusIndiv.getCusId()!=null && !cusIndiv.getCusId().equals("")){
				CusBase cusBase=CusBaseComponent.getCusBase(cus_id);
				cusRelTree=new CusRelTree();
				cusRelTree.setNodeId(cus_id);
				cusRelTree.setNodeAttribute(TreeUtil.cusIndiv);
				cusRelTree.setNodeLevel("");
				cusRelTree.setNodeName(cusBase.getCusName());
				cusRelTree.setNodeType("TreeSelf");
				cusRelTree.setPNodeId("-1");
				cusRelTree.setNodeInfo("");
				cusRelTree.setNodeCusType(cusBase.getCusType());
				cusRelTree.setNewParentID("-1");
				cusRelTree.setNewID(StringUtil.getRandomId());
				treeMap=cusBaseRelComponent.get3DisplayLayerTree(cusRelTree);
				
				if(treeMap.size()>1){
					context.addDataField("flag","existence4CusIndvi");
				}else{
					context.addDataField("flag","inexistence");
				}
			}else{
				context.addDataField("flag","inexistence");
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
