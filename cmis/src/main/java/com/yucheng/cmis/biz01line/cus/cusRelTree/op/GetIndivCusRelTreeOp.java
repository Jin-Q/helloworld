package com.yucheng.cmis.biz01line.cus.cusRelTree.op;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusRelTree.component.CusBaseRelComponent;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusRelTree;
import com.yucheng.cmis.biz01line.cus.cusRelTree.util.TreeUtil;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.StringUtil;

public class GetIndivCusRelTreeOp  extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String  cusId = null;
		CusBaseRelComponent cusBaseRelComponent=null;
		CusRelTree cusRelTree=null;
		Map<String,CusRelTree> treeMap=null;
		try {
			if(context.containsKey("cus_id"))
			  cusId = (String)context.getDataValue("cus_id");
		} catch (Exception e) {e.printStackTrace();}
		if(cusId==null||cusId.equals("")) 
			throw new EMPException("客户码为空！");
		try{
			connection = this.getConnection(context); 
			cusBaseRelComponent = (CusBaseRelComponent) CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CusIndivRelComponent,context,connection);

			CusBaseComponent CusBaseComponent = (CusBaseComponent) cusBaseRelComponent.getComponent(PUBConstant.CUSBASE);
			CusIndiv cusIndiv=CusBaseComponent.getCusIndiv(cusId);
			if(cusIndiv==null||cusIndiv.getCusId()==null||cusIndiv.getCusId().equals(""))
				throw new EMPException("客户信息中不存在该客户！");
			
			CusBase cusBase=CusBaseComponent.getCusBase(cusId);
			cusRelTree=new CusRelTree();
			cusRelTree.setNodeId(cusId);
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
			context.addDataField("TreeMap", treeMap);
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
