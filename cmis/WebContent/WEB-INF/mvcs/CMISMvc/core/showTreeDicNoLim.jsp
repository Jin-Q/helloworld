<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<HTML>
<HEAD>
<TITLE>请选择...</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="${mvcfile}" -->

<script src="<emp:file fileName='scripts/yui/yahoo/yahoo-min.js'/>" type="text/javascript" language="javascript"></script> 
<script src="<emp:file fileName='scripts/yui/event/event-min.js'/>" type="text/javascript" language="javascript"></script> 
<script src="<emp:file fileName='scripts/yui/connection/connection-min.js'/>" type="text/javascript" language="javascript"></script>

<link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" /> 
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/emp/pageUtil.js'/>" type="text/javascript" language="javascript"></script>

<style>
.dTreeNode {
	border-width: 1px 1px 0;
	border-style: solid;
	border-color: #dadada;
	border-width: 0px 0px 1px;
}

#opTreeDiv {
	margin-left: 20px;
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: #dadada;
	border-width: 0px;
}

.opDiv {
	position: absolute;
	top: 0px; *
	top: 1px;
	left: 200px;
	width: 50px;
	height: 20px;
	border-left-width: 1px;
	border-left-style: solid;
	border-left-color: #dadada;
	border-width: 0px;
}

.selectDiv {
	border-bottom:1px solid #dadada;
}
</style>
<%
	String dicTreeTypeId = request.getParameter("dicTreeTypeId");
%>
<script type="text/javascript"><!--
	
	var dicTreeTypeId = "<%=dicTreeTypeId%>";
	
	var menuOpTree;
	
	function doOnLoad(){
		
		var callback = {
			success : createDTree,
			isJSON : true
		};
		
		var url = '<emp:url action="getDicJSONTree.do"/>?dicTreeTypeId='+dicTreeTypeId;
		url = EMPTools.encodeURI(url);
		EMPTools.ajaxRequest('GET', url, callback);
		EMPTools.setWait();
	}
	
	/**
	 * 生成一个dtree
	 * 其中jsonObj是根节点＋第一级节点的内容
	 */
	function createDTree(jsonObj, callback){
		
		var treename = "menuOpTree";
		try {
			//创建menuOpTree树，并修改该dtree的处理方式
			menuOpTree = new dTree(treename);
			menuOpTree.config.useCookies = false;
			
			//重载dtree中展开一个父节点的实现方法
			menuOpTree.o = function(id){
				var cn = this.aNodes[id];
				
				//判断子节点是否已经添加。如果未添加，则动态添加子节点
				if(!cn.childrenAppended)
					dynamicAppendNode(cn);
				
				this.nodeStatus(!cn._io, id, cn._ls);
				cn._io = !cn._io;
				if (this.config.closeSameLevel) this.closeLevel(cn);
				if (this.config.useCookies) this.updateCookie();
			};
			
			//获得第一级节点的信息
			var root_children = [];
			root_children[0] = jsonObj;
			menuOpTree.jsonObj = jsonObj;//将后台返回的json对象设置为树中的一个属性
			
			//处理根节点
			createSubTree(-1, root_children, menuOpTree);
			//处理一级节点
			createSubTree(jsonObj.id, jsonObj.children, menuOpTree);
			
			document.getElementById("opTreeDiv").innerHTML = menuOpTree.toString();
			
			//给树中的每个节点生成特定的DIV结构，用于处理选中、双击等事件
			createOpDivForSubTree(root_children, menuOpTree);
			
		} catch (e) {alert(e)};
		
		EMPTools.removeWait();
	};
	
	/**
	 * 往dtree的pid节点下面添加子节点
	 */
	function createSubTree(pid, children, dtree){
		for( var i=0; i<children.length; i++) {
			var item = children[i];
			var node = dtree.dynamicAdd(item.id,pid,item.label);
			node.jsonObj = item;//为了更好的遍历树，将与当前节点相关的json对象赋予节点
			
			if(item.leaf == "false"){
				node._hc = true;
			}
		}
	};
	
	/**
	 * 展开父节点后，动态的添加子节点
	 */
	function dynamicAppendNode(pNode){
		pNode.childrenAppended = true;
		var pid = pNode.jsonObj.id;
		var dynamic = pNode.jsonObj.dynamic;
		
		var handleChildren = function(children, callback){
			var pNode = callback.pNode;
			var pid = pNode.jsonObj.id;
			pNode.jsonObj.children = children;
			
			createSubTree(pid, children, menuOpTree);
			menuOpTree.dynamicAddNode(pid);
			createOpDivForSubTree(children, menuOpTree);
			
			EMPTools.removeWait();
		};
		
		EMPTools.setWait();
		
		//判断是异步从后台取值，还是数据已经保存在前台
		if(dynamic == "false"){
			var children = pNode.jsonObj.children;
			var callback = {
				pNode : pNode
			};
			handleChildren(children, callback);
		}else{
			var callback = {
				success : handleChildren,
				isJSON : true,
				pNode : pNode
			};
			
			var url = '<emp:url action="getDicJSONTree.do"/>?dicTreeTypeId='+dicTreeTypeId+"&parentNodeId="+pid;
			url = EMPTools.encodeURI(url);
			EMPTools.ajaxRequest('GET', url, callback);
		}
	};
	
	function createOpDivForSubTree(children, dtree){
		for( var i=0; i<children.length; i++) {
			var item = children[i];
			var div = document.getElementById("menuOpTree"+item.id+"_div");
			
			if (div != null) {
				if (item.children && item.children.length>0) {
					createOpDivForSubTree(item.children, dtree);
				}
				div.onclick = Function("selectDiv"+"('"+item.id+"')");
				
				//对叶子节点添加双击事件
				if(item.leaf == "true"){
					div.ondblclick = function(){
						var returnBtn = document.getElementById("button_selNode");
						returnBtn.click();
					};
				}
			}else
				break;
		}
	};
	
	var curNodeId;
	function selectDiv(nodeId){
		if(curNodeId != null){
			var div = document.getElementById("menuOpTree"+curNodeId+"_div");
			if(div)
				div.className = "dTreeNode";
		}
		var div = document.getElementById("menuOpTree"+nodeId+"_div");
		if(div)
			div.className = "selectDiv";
		curNodeId = nodeId;
	};
	
	function doSelect(methodName){
		if(curNodeId == null){
			window.close();
		}
		var nId = curNodeId;
		for (var n=0; n<menuOpTree.aNodes.length; n++) {
			if (menuOpTree.aNodes[n].id == nId) {
				nId = n;
				break;
			}
		}
		var leaf = menuOpTree.aNodes[nId].jsonObj.leaf;
		if (leaf != "true") {
			//alert("请选择到最小分类!");
			//return;
		}
		var locate = menuOpTree.aNodes[nId].jsonObj.locate;
		var pNode = menuOpTree.jsonObj;
		var list = locate.split(",");
		var showStr = "";
		for(var i=0;i<list.length;i++){
			if(list[i] == null || list[i] == "")
				continue;
			if(pNode.id == list[i])
				continue;
			for(var j=0;j<pNode.children.length;j++){
				if(pNode.children[j].id == list[i]){
					pNode = pNode.children[j];
					showStr += pNode.label+"->";
					break;
				}
			}
		}
		var data = {};
		data.id=curNodeId;
		data.label=showStr.substring(0,showStr.length-2);
		data.Two=curNodeId.substring(0,2);
		data.Three=curNodeId.substring(0,3);
		data.Fourth=curNodeId.substring(0,4);
		data.One=getOneFormTwo(data.Two);
		
		var parentWin = EMPTools.getWindowOpener();
		eval("parentWin."+methodName+"(data)");
		
		window.close();
	};
	function getOneFormTwo(key){
                if(Number(key)>=1&&Number(key)<=5) return "A";
                if(Number(key)>=6&&Number(key)<=11) return "B";
                if(Number(key)>=13&&Number(key)<=43) return "C";
                if(Number(key)>=44&&Number(key)<=46) return "D";
                if(Number(key)>=47&&Number(key)<=50) return "E";
                if(Number(key)>=51&&Number(key)<=59) return "F";
                if(Number(key)>=60&&Number(key)<=62) return "G";
                if(Number(key)>=63&&Number(key)<=65) return "H";
                if(Number(key)>=66&&Number(key)<=67) return "I";
                if(Number(key)>=68&&Number(key)<=71) return "J";
                if(Number(key)>=72&&Number(key)<=72) return "K";
                if(Number(key)>=73&&Number(key)<=74) return "L";
                if(Number(key)>=75&&Number(key)<=78) return "M";
                if(Number(key)>=79&&Number(key)<=81) return "N";
                if(Number(key)>=82&&Number(key)<=83) return "O";
                if(Number(key)>=84&&Number(key)<=84) return "P";
                if(Number(key)>=85&&Number(key)<=87) return "Q";
                if(Number(key)>=88&&Number(key)<=92) return "R";
                if(Number(key)>=93&&Number(key)<=97) return "S";
                if(Number(key)>=98&&Number(key)<=98) return "T";
	};

	function doReturnMethod(_method, obj){
		doSelect(_method);
	}
	
--></script>

</HEAD>
<BODY onload="doOnLoad()">
<div id="opTreeDiv"></div>
<emp:returnButton id="selNode" label="选定" />
</BODY>
</HTML>
