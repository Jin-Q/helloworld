package com.ecc.emp.ext.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.ext.tag.EMPExtTagSupport;
import com.yucheng.cmis.biz01line.cus.cusRelTree.domain.CusRelTree;
/*
 * desp:标签<exp:ctree /> 将当前TreeMap展现 090719gaozh
 * 
 * 
 * */
public class EMPExtTreeTag extends EMPExtTagSupport {

 public int doStartTag(){
	 StringBuffer sb =null;
	 HashMap<String,CusRelTree> treeMap=null;
	 CusRelTree cusRelTree=null;
		
		try{
			sb= new StringBuffer();
			//取得当前context
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
			treeMap=(HashMap<String, CusRelTree>) context.getDataValue("TreeMap");
           // String emp_sid=(String)context.getDataValue("EMP_SID");	
            sb.append("\n");
			sb.append("<script language='javascript'> \n");
			sb.append("var tree=new dTree('tree'); \n");
			
			 //输出
			 Iterator it = treeMap.entrySet().iterator();
             Map.Entry entry = null;
             if (it != null) {
            	String nodeId;
            	String pNodeId;
            	String nodeName;
            	String nodeInfo;
            	String nodeCusType;
            	String pID;
            	String cID;
            	String nodeAttribute;
				while (it.hasNext()) {
					entry = (Map.Entry) it.next();
					cusRelTree=(CusRelTree) entry.getValue();
					nodeId=cusRelTree.getNodeId();
					pNodeId=cusRelTree.getPNodeId();
					nodeName=cusRelTree.getNodeName();
					nodeInfo=cusRelTree.getNodeInfo();
					nodeCusType=cusRelTree.getNodeCusType();
					pID=cusRelTree.getNewParentID();
					cID=cusRelTree.getNewID();
					nodeAttribute=cusRelTree.getNodeAttribute();
				    sb.append("tree.add('"+cID+"','"+pID+"','"
				    		              +nodeName+nodeInfo+"',\"javascript:linkTo('"+nodeId+"','"+pNodeId+"','"+nodeName+"','"+nodeInfo+"','"+nodeCusType+"','"+nodeAttribute+"');\",''); \n");
				}
			}
             
             sb.append("document.write(tree) \n");
			sb.append("</script> \n");	
			System.out.println(sb.toString());
			outputContent(sb.toString());
			 
		}catch(Exception e){
			e.printStackTrace();
		}
	 return EVAL_BODY_INCLUDE;
 }
 
}
