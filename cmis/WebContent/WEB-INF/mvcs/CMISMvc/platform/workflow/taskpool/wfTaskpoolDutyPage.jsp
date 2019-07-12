<%@page import="com.yucheng.cmis.platform.organization.domains.SDuty"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@page import="java.util.*"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String count=request.getParameter("count");//"1"单选；"n"多选
List<SDuty> allDutyList = (List<SDuty>)context.getDataValue("AllDutyList");
List<SDuty> hasDutyList = (List<SDuty>)context.getDataValue("HasDutyList");
String sel="";
for(int i=0;i<hasDutyList.size();i++){
	sel+=(String)hasDutyList.get(i).getDutyno()+";";
}
%>
<html>
<head>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath() %>/scripts/workflow/xtree/css/xtree2.css">
<script type="text/javascript"	src="<%=request.getContextPath() %>/scripts/workflow/xtree/js/xtree2.js"></script>
<style type='text/css'>
A{font-size:9pt}
BODY{font-size:9pt}
.button
{
	BORDER-RIGHT:#A7A6AA 1px solid; 
	PADDING-RIGHT:1px;
	BORDER-TOP:#A7A6AA 1px solid;
	PADDING-LEFT:2px;
	FONT-SIZE:12px;
	FILTER:progid:DXImageTransform.Microsoft.Gradient(GradientType=0,StartColorStr=#ffffff, EndColorStr=#F0F0F0);
	BORDER-LEFT: #A7A6AA 1px solid; 
	CURSOR:hand;
	COLOR:#4B4B4B;
	PADDING-TOP:2px; 
    BORDER-BOTTOM: #A7A6AA 1px solid; 
	background:background-attachment
}
</style>
<script type="text/javascript">
function retSuc(){
		var retObj = [];
		retObj[0] = true;
		var dutyno = null;
		var dutyname = null;
		var attribute= document.getElementsByName('checkbox_tree');
	    for (var i = 0; i < attribute.length; i++){
	         if(!attribute[i].checked)
				continue;
	         if(dutyno==null){
				dutyno= attribute[i].value.substring(0,attribute[i].value.indexOf("@"));
				dutyname= attribute[i].value.substring(attribute[i].value.indexOf("@")+1,attribute[i].value.length);
			 }
	         else{
				dutyno+= ";"+attribute[i].value.substring(0,attribute[i].value.indexOf("@"));
				dutyname+= ";"+attribute[i].value.substring(attribute[i].value.indexOf("@")+1,attribute[i].value.length);
			 }
	    }
		retObj[1] = dutyno;
		retObj[2] = dutyname;
		window.returnValue = retObj;
		window.close();
};
	
	function retFail(){
		var retObj = [];
		retObj[0] = false;
		window.returnValue = retObj;
		window.close();
	};
//初始化选择岗位
function initrole(){
	var attribute= document.getElementsByName('checkbox_tree');
	var dutyno = null;
    for (var i = 0; i < attribute.length; i++){
    	dutyno= attribute[i].value.substring(0,attribute[i].value.indexOf("@"));
    	if("<%=sel%>".indexOf(dutyno+";")!=-1){
    		attribute[i].checked=true;
    	}
    }
}
</script>
<body onload="initrole()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr >
<td>
<div id="leftMenuBar" style="height:470px; width:350; overflow: auto; text-align: left;">
		<script type="text/javascript">
	  //xtree
	  var path='<%=request.getContextPath()%>/scripts/workflow/xtree/images/';
      	webFXTreeConfig.rootIcon        = path+"folder.png",
	    webFXTreeConfig.openRootIcon    = path+"openfolder.png",
	    webFXTreeConfig.folderIcon      = path+"folder.png",
	    webFXTreeConfig.openFolderIcon  = path+"openfolder.png",
	    webFXTreeConfig.fileIcon        = path+"dot.png",
	    webFXTreeConfig.iIcon           = path+"I.png",
	    webFXTreeConfig.lIcon           = path+"L.png",
	    webFXTreeConfig.lMinusIcon      = path+"Lminus.png",
	    webFXTreeConfig.lPlusIcon       = path+"Lplus.png",
	    webFXTreeConfig.tIcon           = path+"T.png",
	    webFXTreeConfig.tMinusIcon      = path+"Tminus.png",
	    webFXTreeConfig.tPlusIcon       = path+"Tplus.png",
	    webFXTreeConfig.plusIcon        = path+"plus.png",
	    webFXTreeConfig.minusIcon       = path+"minus.png",
	    webFXTreeConfig.blankIcon       = path+"blank.png",
        webFXTreeConfig.loadingIcon     = path+"loading.gif";
       var tree = new WebFXTree('请选择岗位');
    <% 
       for(int i=0;i<allDutyList.size();i++){
			SDuty duty = allDutyList.get(i);
			out.print("tree.add(new WebFXTreeItem('"+duty.getDutyname()+"',null,null,null,null,'"+duty.getDutyno()+"@"+duty.getDutyname()+"','"+count+"'));");	
        }
    %>
          tree.indentWidth = 19;
          tree.open = true;//节点打开状态，true为打开
          tree._selectedItem = null;
          tree._fireChange = true;
          tree.rendered = false;
          tree.suspendRedraw = false;
          tree.showLines = true;//连线显示状态，true为显示
          tree.showExpandIcons = true;//扩展和收缩图标显示状态，true为显示
          tree.showRootNode = true;//根节点显示状态，true为显示
          tree.showRootLines = true;//根节点连线显示状态，true为显示
          tree.write();
</script>
</div>
</td>
</tr>
</table>
<center>
	<input type="button" class="button" value="确　　定" onclick="retSuc()">&nbsp;&nbsp; 
	<input type="button" class="button" value="取　　消" onclick="retFail()">
</center>
</body>
</html>
