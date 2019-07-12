<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>ECC IDE Jsp file</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script src="<emp:file fileName='scripts/pageUtil.js'/>" type="text/javascript" language="javascript"></script>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CMISMvc/testJavaScript/testRoleMenuOp.mvc" -->
<style>
	* {
		font-size:12px;		
	}
	h2 {
		font-size:18px;
	}
	#whole {
		width: 100%;
	}
	#whole td{
		border-width:0px;
		border-style:solid;
		border-color:#6666FF;
	}
	#whole td {
		vertical-align: top;
	}	
	
	.dTreeNode {
		border-width:1px 1px 0;
		border-style:solid;
		border-color:#dadada;
		
		border-width:0px 0px 1px;
	}
	#opTreeDiv {
		margin-left:20px;
		border-bottom-width:1px;
		border-bottom-style:solid;
		border-bottom-color:#dadada;
		
		border-width:0px;
	}
	.opDiv {
		position:absolute;
		top:0px;
		*top:1px;
		left:200px;
		width:50px;
		height:20px;
		border-left-width:1px;
		border-left-style:solid;
		border-left-color:#dadada;
		
		border-width:0px;
	}
	
	.selectDiv {
		border-bottom:1px solid #dadada;
	}
</style>
<!-- <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" /> -->
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script>
	var YAHOO = topPage.YAHOO;
	var curMenuId = null;
	
	var jsontree
	var dTreeName = "dTreeObj";
	var dTreeObj;
	
	function getResourceTree(resourceId){
		var divObj;
		
		var callback = {
		  success:handleSuccess,
		  failure:handleFailure
		}
		if(resourceId != null)
			curMenuId = resourceId;
		
		var url = '<emp:url action="getResourceTree.do"/>'
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	};
	
	/**
	  请求资源树的成功返回方法
	*/
	var handleSuccess = function(o){ EMPTools.unmask();
	
		if(o.responseText !== undefined) {
			try {			
				jsontree = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse menu define error!"+e);
				return;
			}
			createTreeTable();
		}
		
	};	
	
	var handleFailure = function(o){ EMPTools.unmask();
		if(o.responseText !== undefined) {
			alert(o.responseText);
		}
	};
	
	function createTreeTable(){

		try {
			dTreeObj = new dTree(dTreeName);
			
			dTree.prototype.node = function(node, nodeId) {
				var str = '<div id="'+this.obj+node.id+'_div" class="dTreeObjNode">' + this.indent(node, nodeId);
				if (this.config.useIcons) {
					if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc) ? this.icon.folder : this.icon.node);
					if (!node.iconOpen) node.iconOpen = (node._hc) ? this.icon.folderOpen : this.icon.node;
					if (this.root.id == node.pid) {
						node.icon = this.icon.root;
						node.iconOpen = this.icon.root;
					}
					str += '<img id="i' + this.obj + nodeId + '" src="' + ((node._io) ? node.iconOpen : node.icon) + '" alt="" />';
				}
				//将树形中的文本增加span，并提供onclick事件(根节点除外)
				if (this.root.id != node.pid) {
					str += '<a href="#" class="node"><span id="'+this.obj+node.id+'_span" onclick="selectDiv(\''+node.id+'\')">';
					str += node.name;
					str += '</span></a>';
				}else{
					str += node.name;
				}
				str += '</div>';
				if (node._hc) {
					str += '<div id="d' + this.obj + nodeId + '" class="clip" style="display:' + ((this.root.id == node.pid || node._io) ? 'block' : 'none') + ';">';
					str += this.addNode(node);
					str += '</div>';
				}
				this.aIndent.pop();
				return str;
			};
			
			dTreeObj.add(dTreeName,-1,"资源定义");
			createSubTree(dTreeName, jsontree, dTreeObj);
		} catch (e) {alert(e)};
		var ctrlLink = "<br/><a href='javascript: "+dTreeName
				+".openAll();'>open all</a> | <a href='javascript: "
				+dTreeName+".closeAll();'>close all</a><br/><br/>";
				
		var searchbar="<tr><td>编号<input type=\"text\" id=\"resourceid\" name=\"resourceid\" size=\"10\"/>";
			searchbar+="名称<input type=\"text\" id=\"resourcename\" name=\"rescourcename\" size=\"10\">";
			searchbar+="<button onclick=\"search()\">搜索</button></td></tr>";
	
		document.getElementById("opTreeDiv").innerHTML = ctrlLink+searchbar+dTreeObj.toString();
		if(curMenuId != null){
			selectDiv(curMenuId);
		}
	};

	function createSubTree(pid, pNode, dTreeObj){
		var children = pNode.children;
		for( var i=0; i<children.length; i++) {
			var item = children[i];
			dTreeObj.add(item.id,pid,item.label,null,'',null);
			if (item.children && item.children.length>0) {
				createSubTree(item.id, item, dTreeObj);
			}
		}
	};
	
	function selectDiv(itemId){
		if(curMenuId == null){
			curMenuId = itemId;
		}else if(curMenuId != itemId){
			var spanObj = document.getElementById(dTreeName+curMenuId+"_span");
			if(spanObj)
				spanObj.className="";
			curMenuId = itemId;
		}else{
			var spanObj = document.getElementById(dTreeName+curMenuId+"_span");
			if(spanObj)
				spanObj.className="selectDiv";
			return;
		}
		
		spanObj = document.getElementById(dTreeName+itemId+"_span");
		if(spanObj)
			spanObj.className = "selectDiv";
		
	};
	
	
	function search()
	{
		var div=null;
		var resourceid = document.getElementById("resourceid").value;
		var resourcename = document.getElementById("resourcename").value;
		if(resourceid == null || resourceid == ""){
			if(resourcename != null && resourcename != ""){
				resourceid = dTreeObj.returnNodeId(resourcename);
			}else
				return;
		}
		if (resourceid != null && resourceid != "")
		{
			div =document.getElementById(dTreeName + resourceid+"_div");
			if(div != null && div!= ""){
				dTreeObj.openTo(resourceid);
				selectDiv(resourceid);
				return;
			}
		}
		alert("找不到该资源");	
	};
	
	function doSelect(methodName){
		if(curMenuId == null){
			alert("请选择一个资源!");
			return;
		}
		var data = {};
		data.resourceid = curMenuId;
		data.cnname = dTreeObj.returnTitle(curMenuId);
		window.opener[methodName](data);
		window.close();
	}
	
</script>
</head> 
<body onload="getResourceTree()" onselectstart="return false">
<table width="90%"  bgcolor='#E8F2FE'><tr><td bgcolor='#E8F2FE'>

资源定义 MENUID: <emp:text id="menuId" label="menuId" dataName="menuId" /> 

<table width="99%" id="whole" bgcolor="#FFFFFF">

  <tr> 
<td width="50%">
<div id="opTreeDiv"></div></td>

</tr>
<tr><td>

</td></tr>
</table>
<emp:return id="opbutton_select" />

</td>

</tr></table>
</body>
</html>
