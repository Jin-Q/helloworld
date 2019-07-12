<%@page import="com.yucheng.cmis.platform.workflow.WorkFlowConstance"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFINodeVO"%>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
List<WFINodeVO> wfNodeList = (List<WFINodeVO>)context.getDataValue(WorkFlowConstance.WF_NEXT_NODE_LIST);

StringBuffer nodeStr = new StringBuffer() ;
nodeStr.append("[");
if(wfNodeList!=null && wfNodeList.size()>0) {
	String nodekey, nodevalue, taskpool, isMul;
	for(int i=0; i<wfNodeList.size(); i++) {
		WFINodeVO nodeVO = wfNodeList.get(i);
		nodekey = nodeVO.getNodeId();
		nodevalue = nodeVO.getNodeName();
		taskpool = nodeVO.getNodeType();//暂时设置在节点类型字段上标识是否项目池节点
		isMul = (nodeVO.getNodeTransactType().equals("0")||nodeVO.getNodeTransactType().equals("1"))?"1":"n"; //办理人多选标识
		nodeStr.append("{nodekey:\"").append(nodekey).append("\",nodevalue:\"").append(nodevalue).append("\",taskpool:\"").append(taskpool).append("\",isMul:\"").append(isMul).append("\"},");
	}
}
nodeStr.replace(nodeStr.length()-1, nodeStr.length(), "]");
String nodeList = nodeStr.toString();
//System.out.println(nodeList);
%>

<HTML>
<HEAD>
<TITLE>下一步骤、办理人选择</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<link href="<%=request.getContextPath()%>/styles/workflow/default2.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

	var nodejsondata = <%=nodeList%> ;
	var gCount = 1;
	function retSuc(){
		var retObj = [];
		retObj[0] = true;
		var node = document.getElementById("nodeid");
		if(node.value == null||node.value==""){
			alert("请选择下一节点");
			return;
		}
		retObj[1] = node.value;	
		var user= document.getElementById("seluser");
		var ispool= document.getElementById("ispool");
		//是否项目池
		if(ispool.value=="n")
		{
			if(user.value==null||user.value==""){
				alert("请选择下一办理人");
				return;
			}		
			retObj[2] = user.value;	
		}
		window.returnValue = retObj;
		window.close();
	};
	
	function retFail(){
		var retObj = [];
		retObj[0] = false;
		window.returnValue = retObj;
		window.close();
	};
	
	function showNodeDiv(isMul){
		gCount = isMul;
		gCount = 1; //由于目前引擎不支持跳转节点多人办理，所以指定为1
		var list = document.getElementsByName("node");
		var node = document.getElementById("nodeid");
		var divUser = document.getElementById("divuser");
		document.getElementById("ispool").value ="n";
		for(var i=0;i<list.length;i++){
			if(list[i].checked) {
				for(var j=0; j<nodejsondata.length; j++) {
					var nodeObj = nodejsondata[j];
					if(nodeObj.nodekey==list[i].value && nodeObj.taskpool=='T') {
						document.getElementById("ispool").value ="y";
						hiddenDiv();
						break;
					}
				}
				if(document.getElementById("ispool").value=='n') {
					divUser.style.display = "block";
				 	document.getElementById("userid").value="";
				 	node.value = list[i].value;
				 	break;
				}
			 }
		}
	}
	
	//隐藏用户div块
	function hiddenDiv(){
		var list = document.getElementsByName("node");
		var node = document.getElementById("nodeid");
		var divUser = document.getElementById("divuser");
		divUser.style.display = "none";
		document.getElementById("userid").value="";
		for(var i=0;i<list.length;i++){
			if(list[i].checked)
			{
			 	node.value = list[i].value ;
			 	break ;
			 }
		}
		document.getElementById("ispool").value ="y";
	}
	//选择用户
	function selUser(){
		var contextPath="<%=request.getContextPath()%>";
		//打开选择处理人的界面
		var url = contextPath+'/selectAllUser.do?count='+gCount+'&EMP_SID=<%=request.getParameter("EMP_SID")%>&rd='+Math.random();
		var retObj = window.showModalDialog(url,'selectPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;');
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		if(retObj[1] != null){
			if(retObj[1].indexOf(";")!=-1){//返回多值
				var list=retObj[1].split(";");
				var seluser="";
				for(var i=0;i<list.length;i++){
					if(i>0)
						seluser+=";U."+list[i];
					else
						seluser+="U."+list[i];
				}
				document.getElementById("seluser").value =seluser;
			}else{
				document.getElementById("seluser").value = "U."+retObj[1];
			}
		}
		if(retObj[2] != null)
			document.getElementById("userid").value = retObj[2];
	}
	Ext.onReady(querysel);
	function querysel() {
		querynode() ;
	}
	function querynode(){
		var nodevalue = document.getElementById('nodenamekey');
		var nodekey = nodevalue.value ;
		nodekey=nodekey.replace(/\s+/g,"");  
		var innterhtml = "" ;
		//默认输入框为空
		if(nodekey==null||nodekey=="")
		{
			for ( var i = 0; i < nodejsondata.length; i++) {
				var node = nodejsondata[i];
				innterhtml += "<input type=\"radio\" name=\"node\" value=\""+ node.nodekey+ "\" onclick=\"showNodeDiv('"+node.isMul+"')\">"+ node.nodevalue+ "<br/>";
			}
		}
		//输入框有值、执行查询操作
		else{
			for ( var i = 0; i < nodejsondata.length; i++) {
				var node = nodejsondata[i];
				var indexExt = node.nodevalue.indexOf(nodekey);
				if(indexExt!=-1)
				{
					innterhtml += "<input type=\"radio\" name=\"node\" value=\""+ node.nodekey+ "\" onclick=\"showNodeDiv()\">"+ node.nodevalue+ "<br/>";	
				}
			}
		}
		var nodediv = document.getElementById('nodediv');
		if(innterhtml=="")
		{
			innterhtml = "<span style=\"color:red\">没有找到下一节点请输入正确的查询条件</span>"
		}
		nodediv.innerHTML= innterhtml ;
	}
</script>

</HEAD>
<BODY>
<input type="hidden" id="nodeid" value="" />
<input type="hidden" id="seluser" value=""/>
<input type="hidden" id="ispool" value=""/>
<div class="selectNextNodeStyle">
<table border="0" cellspacing="0" cellpadding="0" class="tablemain2" >
	<tr height="auto">
		<td width="40%" class="trtitle">请选择下一节点</td>
		<td width="60%" class="trtitle">请选择下一处理人</td>
	</tr>
	<tr>
		<td>
        <input type="text" id="nodenamekey" value="">&nbsp;&nbsp;<input type="button" id="query" value="查询" onclick="querynode()">
        <div id="nodediv" style="margin:0px;padding:0px;height:240;width=auto;overflow-x:no;overflow-y:auto">
		</div>
		</td>
		<td><div id="divuser" style="display:none"><input type="text" id=userid name="userid" value="" style="width:300px" readonly="readonly"/><a href=# onclick="selUser()">选择</a></div>&nbsp;</td>
	</tr>
</table></div>
<center><input type="button" class="button" value="确  定" onclick="retSuc()"/>&nbsp;&nbsp;<input type="button" class="button" value="取  消" onclick="retFail()"/></center>
</BODY>
</HTML>
