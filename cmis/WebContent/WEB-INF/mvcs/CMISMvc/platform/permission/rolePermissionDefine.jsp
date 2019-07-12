<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c"%>
<emp:page>
<html>
<head>
<title>ECC IDE Jsp file</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CMISMvc/testJavaScript/testRoleMenuOp.mvc" -->
<link href="<emp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp-ext/pageUtil.js'/>" type="text/javascript" language="javascript"></script>
<jsp:include page="/include.jsp" />
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
	#roleList ul {
		margin: 0px;
		padding: 0px;	
	}
	#roleList ul li {
		list-style:none;
		cursor: hand;
	}
	#roleList ul li a{
		display:block;
		width:100px;
		padding:5px 0 5px 10px;
		text-decoration: none;
		background-color:white;
	}
	#roleList ul li a.selected {
		background-color:#dadada;
	}
	.dTreeNode {
		border-width:1px 1px 0;
		border-style:solid;
		border-color:#dadada;
		position:relative;
		
		border-width:0px 0px 1px;
	}
	#opTreeDiv {
		margin-left:20px;
		border-bottom-width:1px;
		border-bottom-style:solid;
		border-bottom-color:#dadada;
		
		border-width:0px;
		
		width:400px;
	}
	
	.opHid{
	   display:none;
	}
	.opShow{
	   display:block;
	}
	.selectDiv {
		border-bottom:1px solid #dadada;
	}
</style>
<!-- <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" />-->
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script>
	var curMenuId = null;
	
	var jsontree;
	
	var dTreeName = "dTreeObj";
	
	var dTreeObj;
	
	//当前的选中的角色编号
	var curRole = null;
	
	//当前所选中的资源编号
    var currentResourceId = null;
    
    //当前所选中的角色、资源下的操作权限是否有修改
	var changeStatus = false;
	
	
	function getResourceActTree(){
		var divObj;
		var callback = {
		  success:handleSuccess,
		  failure:handleFailure,
		  argument: { divObj:divObj }
		}
		
		var url = '<emp:url action="getResourceActTree.do"/>';
		
		var obj1 = window.top.YAHOO.util.Connect.asyncRequest('GET', url, callback);
		//topPage.setMainFrameWait();
		EMPTools.setWait();
	};
	
	/**
	  请求资源操作树的返回处理方法
	*/
	var handleSuccess = function(o){ EMPTools.unmask();
			try {			
				jsontree = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse menu define error!"+e);
				return;
			}
			createTreeTable();
			
			EMPTools.removeWait();
	};	
	
	var handleFailure = function(o){ EMPTools.unmask();
		if(o.responseText !== undefined) {
			alert(o.responseText);
		}
	};
	
	function createTreeTable(){
		
		try {
			dTreeObj = new dTree(dTreeName);
			
			//重写dtree中每个节点所生成的HTML代码
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
					str += '<a href="#" class="node"><span id="'+this.obj+node.id+'_span" onclick="showOperation(\''+node.id+'\')">';
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
			
			//设置资源树的根节点
			dTreeObj.add(dTreeName,-1,"资源定义");
			createSubTree(dTreeName, jsontree, dTreeObj);
		} catch (e) {alert(e)};
	/*
		var ctrlLink = "<br/><a href='javascript: "+dTreeName
				+".openAll();'>open all</a> | <a href='javascript: "
				+dTreeName+".closeAll();'>close all</a><br/><br/>";
				
		var searchbar="<tr><td>编号<input type=\"text\" id=\"resourceid\" name=\"resourceid\" size=\"10\"/>";
			searchbar+="名称<input type=\"text\" id=\"resourcename\" name=\"rescourcename\" size=\"10\">";
			searchbar+="<button onclick=\"search()\">搜索</button></td></tr>";
	*/
		document.getElementById("opTreeDiv").innerHTML = dTreeObj.toString();
	};
	
	/**
	*递归生成子树
	*/
	function createSubTree(pid, pNode, dtree){
		var operationsDiv=document.getElementById("operationsDiv");
		var children = pNode.children;
		for( var i=0; i<children.length; i++) {
			var item = children[i];
			var currentOpdivId=item.id;
			dTreeObj.add(item.id,pid,item.label,null,'',null);
			
			
			//对于每个资源，都生成一个隐藏的div，该div存放着该资源下的所有操作，每个操作是一个checkbox
			var opDiv = document.createElement("DIV");
			opDiv.id=item.id+"_operation_div";
			opDiv.style.display = "none";
			for (var j in item.operations){
				var op=document.createElement("DIV");
				var check = document.createElement("INPUT");
				check.id = item.operations[j].id;
				check.name = item.operations[j].label;
				check.type = "checkbox";
				//每个操作checkbox都有onclick事件：selectCheck方法
				check.onclick = Function("selectCheck(this,'"+item.id+"','"+item.operations[j].id+"')");
				var textNode = document.createTextNode(item.operations[j].label);
				op.appendChild(check);
				op.appendChild(textNode);
				opDiv.appendChild(op);
			}
			operationsDiv.appendChild(opDiv);
			
			if (item.children && item.children.length>0) {
				createSubTree(item.id, item, dTreeObj);
			}
			
		}
	};
	
	/**
	*显示当前所选中的资源下所有的操作(显示存放该资源下所有操作的DIV)
	*/
	function showOperation(itemid){
		var oerationDiv = null;
		if(currentResourceId != null){
			if(changeStatus){
				var msg = "是否保存刚才的修改？";
				if(confirm(msg)){
					//保存对当前的选中的角色、资源下的操作权限的修改
					saveChange();
				}
			}
			var spanObj = document.getElementById(dTreeName+currentResourceId+"_span");
			if(spanObj)
				spanObj.className = "";
			oprationDiv = currentResourceId+"_operation_div";
	    	document.getElementById(operationDiv).style.display = "none";
	    }
	    currentResourceId = itemid;
	    var spanObj = document.getElementById(dTreeName+currentResourceId+"_span");
		if(spanObj)
			spanObj.className = "selectDiv";
	  	operationDiv = currentResourceId+"_operation_div";
	  	document.getElementById(operationDiv).style.display = "block";
	};
	
	/**
	* 修改操作权限(修改复选框的选择所触发的方法)
	*/
	function selectCheck(o,resourceId,actId){
		changeStatus = true;
	};
	
	function clickRole(roleObj){
		if(curRole == null){
			curRole = roleObj.id;
		}else{
			if(curRole == roleObj.id)
				return;
			if(changeStatus){
				var msg = "是否保存刚才的修改？";
				if(confirm(msg)){
					//保存对当前的选中的角色、资源下的操作权限的修改
					saveChange();
				}
			}
			document.getElementById(curRole).className = "";
			curRole = roleObj.id;
		}
		document.getElementById(curRole).className = "selected";
		
		//取得当前选中的role下的操作权限信息
		getRolePermissionInfo(curRole);
	};
		
	function getRolePermissionInfo(roleNo){
		var divObj;
		var callback = {
		  success:handleRolePermissionInfo,
		  failure:handleFailure,
		  argument: { divObj:divObj }
		}
		
		var url = '<emp:url action="getRolePermissionInfo.do"/>&roleNo='+roleNo;
		url = encodeURI(url);
		var obj1 = window.top.YAHOO.util.Connect.asyncRequest('GET', url, callback);
		
		EMPTools.setWait();
	};
	
	/**
	* 获得角色操作权限信息的返回方法(也是提交修改后返回的方法)
	*/
	var handleRolePermissionInfo = function(o){
		try {
			var rolePermissionInfo = eval("("+o.responseText+")");
			var actList = null;
			if(o.argument.resourceId == null){//如果未指定resourceId，则将所有复选框清空
				var operationListDiv = document.getElementById("operationsDiv");
				actList = operationListDiv.getElementsByTagName("input");
			}else{//否则只清空指定resourceId下的复选框
				var operationDivId = o.argument.resourceId + "_operation_div";
				var operationDiv = document.getElementById(operationDivId);
				if(operationDiv == null)
					return;
				actList = operationDiv.getElementsByTagName("input");
			}
			for(var k=0;k<actList.length;k++){
				if(actList[k].type == "checkbox"){
					actList[k].checked = false;
				}
			}
			for(var i=0;i<rolePermissionInfo.length;i++){
				var resourceActInfo = rolePermissionInfo[i];
				var resourceId = resourceActInfo.resourceid;
				var operations = resourceActInfo.operations;
				var operationDivId = resourceId + "_operation_div";
				var operationDiv = document.getElementById(operationDivId);
				if(operationDiv == null)
					continue;
				
				var actList = operationDiv.getElementsByTagName("input");
				for(var k=0;k<actList.length;k++){
					if(actList[k].type == "checkbox"){
						var actId = actList[k].id;
						
						//如果该角色有该操作的权限，则复选框选中
						if(operations != null && operations[actId])
							actList[k].checked = true;
					}
				}
			}			
			EMPTools.removeWait();
			
			if(o.argument.resourceId != null)
				alert("修改已保存");
		} catch(e) {
			alert("Parse menu define error!"+e);
			return;
		}
	};	
	
	
	/**
	*保存当前所选中的角色的权限配置信息的修改
	*/
	function saveChange(){
		if(curRole == null ){
			alert("请先选择所要保存的角色!");
		}else if(currentResourceId == null){
			alert("请先选择所要保存的资源!");
		}else if(changeStatus == false){
			alert("角色、操作权限没有改变!");
		}else{
			var url = '<emp:url action="updateRolePermissionInfo.do"/>&roleNo='+curRole+"&resourceId="+currentResourceId;
			
			var operationDivId = currentResourceId + "_operation_div";
			var operationDiv = document.getElementById(operationDivId);
			
			//查找当前选中的角色、资源下所有被选中的操作
			var selectCount = 0;
			var actList = operationDiv.getElementsByTagName("input");
			for(var k=0;k<actList.length;k++){
				if(actList[k].type == "checkbox"){
					if(actList[k].checked){
						url += "&actList["+selectCount+"].actId="+actList[k].id;
						selectCount++;
					}
				}
			}
			
			url = encodeURI(url);
			
			var callback = {
				success:handleRolePermissionInfo,
		  		failure:handleFailure,
		  		argument: { resourceId:currentResourceId }
			}
			var obj1 = window.top.YAHOO.util.Connect.asyncRequest('GET', url, callback);
			
			EMPTools.setWait();
			
			//将修改状态还原为未修改
			changeStatus = false;
		}
	};
	
	
	function addRole(){
		var url = '<emp:url action="addRole.do"/>';
		window.open (url,'searchPage','height=250,width=330,top=100,left=200,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no') 
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
				showOperation(resourceid);
				return;
			}
		}
		alert("找不到该资源");	
	};
	
	
</script>
</head>
<body onload="getResourceActTree()">


<table id="whole">
<tr height="23" bgcolor='#E8F2FE' valign="middle">
     <td align="center">角色列表</td>
     <td>资源操作树</td>
     <td>操作</td>
</tr>
<tr> 
      <td style="width:120px; border-right-width:1px;">
         <div id="roleList">
         	<UL>
				<c:forEach items="${context['SRoleList']}" var="role">
					<LI>
						<A id="${role.roleno}" onclick="clickRole(this)">${role.rolename}</A>
					</LI>			
				</c:forEach>
			</UL>	
         </div>
      </td>
      <td width="30%">
         <div id="opTreeDiv"></div>
         
      </td>
      <td>
        <div id="operationsDiv">
        	
        </div>
        <br>
        <button id="save_button" onclick="saveChange()">保存</button>
      </td>
</tr>
</table>
<table>
<tr></tr>
</table>
</body>
</html>
</emp:page>