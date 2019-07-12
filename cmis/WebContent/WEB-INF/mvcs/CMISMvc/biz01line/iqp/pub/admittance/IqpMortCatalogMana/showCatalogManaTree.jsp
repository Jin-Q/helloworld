<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String isMin = "N";
	isMin = request.getParameter("isMin");
	String value = request.getParameter("value");
	String type = request.getParameter("type");
	String close = request.getParameter("close");
	//押品目录准入时，还未生效的目录树的预展示时所需参数
	String serno = null;
	serno = request.getParameter("serno");
%>

<HTML>
<HEAD>
<TITLE>押品类型树</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="${mvcfile}" -->

<script src="<emp:file fileName='scripts/yui/yahoo/yahoo-min.js'/>" type="text/javascript" language="javascript"></script> 
<script src="<emp:file fileName='scripts/yui/event/event-min.js'/>" type="text/javascript" language="javascript"></script> 
<script src="<emp:file fileName='scripts/yui/connection/connection-min.js'/>" type="text/javascript" language="javascript"></script>

<!--  <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" />-->
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/emp/pageUtil.js'/>" type="text/javascript" language="javascript"></script>

<link href="<emp:file fileName='styles/info.css'/>" rel="stylesheet" type="text/css" />

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

<script type="text/javascript">
	var menuOpTree;
	function doOnLoad(){
		var callback = {
			success : createDTree,
			isJSON : true
		};
		var url = "<emp:url action='getCatalogManaJsonTree.do'/>?type=<%=type%>&serno=<%=serno%>&value=<%=value%>";
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
			//menuOpTree.config.useCheckbox = true;  //是否多选
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
			if(jsonObj.children != null){
				createSubTree(jsonObj.id, jsonObj.children, menuOpTree);
			}
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
			//var node = dtree.dynamicAdd(item.id,pid,item.label,item.locate);
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
			var url = "<emp:url action='getCatalogManaJsonTree.do'/>?parentNodeId="+pid;
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
		if("ALL"==curNodeId){
			alert("此节点为根节点不允许选择！");
			return false;
		}
		var nId = curNodeId;
		for (var n=0; n<menuOpTree.aNodes.length; n++) {
			if (menuOpTree.aNodes[n].id == nId) {
				nId = n;
				break;
			}
		}
		var isMin = '<%=isMin%>';
		if("Y"==isMin || "y"==isMin){  //是否要选择到最小节点
			var leaf = menuOpTree.aNodes[nId].jsonObj.leaf;
			if (leaf != "true") {
				alert("请选择到最小分类！");
				return;
			}
		}
		var locate = menuOpTree.aNodes[nId].jsonObj.locate;
		
		var label = menuOpTree.aNodes[nId].jsonObj.label;
		if(locate==undefined){
			locate=menuOpTree.aNodes[nId].jsonObj.id;
        }
		var label = menuOpTree.aNodes[nId].jsonObj.label;		
		var pNode = menuOpTree.jsonObj;
		var list = locate.split(",");
		var showStr = "";
		for(var i=0;i<list.length;i++){
			if(list[i] == null || list[i] == "")
				continue;
			if(pNode.id == list[i]){
				showStr = pNode.label;
				continue;
			}
			for(var j=0;j<pNode.children.length;j++){
				if(pNode.children[j].id == list[i]){
					pNode = pNode.children[j];
					showStr += pNode.label+"->";
					break;
				}
			}
		}
		var data = {};
		//添加返回参数
		data.id=curNodeId;
		data.label=label;
		data.locate = locate;
		data.locate_cn = showStr.substring(0,showStr.length-2);;

		var parentWin = EMPTools.getWindowOpener();
		eval("parentWin."+methodName+"(data)");
		
		window.close();
	};

	function doReturnMethod(_method, obj){
		doSelect(_method);
	}
	function doClose(){
		window.close();
	}
</script>

</HEAD>
<BODY onload="doOnLoad()">
<%if("close".equals(close)){ %>
<emp:button id="close" label="关闭" />
<div id="opTreeDiv"></div>
<emp:button id="close" label="关闭" />
<%}else{ %>
<emp:returnButton id="selNode" label="选取返回" />
<div id="opTreeDiv"></div>
<emp:returnButton id="selNode" label="选取返回" />
<%} %>

</BODY>
</HTML>
