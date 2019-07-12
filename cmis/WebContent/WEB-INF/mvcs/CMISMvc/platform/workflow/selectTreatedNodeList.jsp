<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIUserVO"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.WorkFlowConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFINodeVO"%>
<!--/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/ -->
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFICallBackDisc"%>
<!--/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/ -->
<%

	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	List<WFINodeVO> wfNodeList = null;
	/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
	List<WFICallBackDisc> wfiCBNodeList = null;
	String isShowCBD="";
	try {
		wfNodeList = (List<WFINodeVO>) context.getDataValue(WorkFlowConstance.WF_NEXT_NODE_LIST);
		isShowCBD=(String)context.getDataValue("isShowCBD");
		if("true".equals(isShowCBD))
			wfiCBNodeList = (List<WFICallBackDisc>) context.getDataValue("WfiCallBackDiscList");
	/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/ 
	} catch(Exception e) {
		e.printStackTrace();
		out.println("<center>打回出错，请联系管理员！错误信息："+e.getMessage()+"</center>");
		return;
	}
	/**  added yangzy 2015/09/18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求  begin*/
	String is_back="no";
	if(context.containsKey("is_back")){
		is_back =(String)context.getDataValue("is_back");
	} 
	/**  added yangzy 2015/09/18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求  end*/
%>

<html>
<head>
<TITLE>退回步骤选择</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/styles/workflow/default.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
function retSuc(){
	var retObj = [];
	retObj[0] = true;
	var node = null;
	var user = null ;
	//取退回模式
	var callBackModel = "";
	var callBack = document.getElementsByName("callBackModel");
	for(var i=0; i<callBack.length; i++){
		if(callBack[i].checked)
			callBackModel = callBack[i].value;
	}
	if(callBackModel==""){
		alert("请选择退回模式");
		return;
	}
	
	var list = document.getElementsByName("nodeidInput");
	for(var i=0;i<list.length;i++){
		if(list[i].checked){
			if(node==null)
			node = list[i].value;
			break ;
		}
	}
	if(node == null){
		alert("请选择要退回的步骤");
		return;
	}
	retObj[1] = node;
	var userlist = document.getElementsByName("userid"+node);
	for(var i=0;i<userlist.length;i++){
		if(userlist[i].checked){
			if(user==null)
			user = userlist[i].value;
			break ;
		}
	}
	if(user==null){
		alert("请选择要退回的人员");
		return ;
	}
	retObj[2] = user;	
	retObj[3] = callBackModel;	
	/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/
	var isShowCBD='<%=isShowCBD%>';
	if("true"==isShowCBD){
		var callBackNodes="";
		var callBacklist = document.getElementsByName("callBackNode");
		for(var i=0;i<callBacklist.length;i++){
			if(callBacklist[i].checked){
				callBackNodes =callBackNodes+","+callBacklist[i].value;
			}
		}
		if(callBackNodes.length>0){
			callBackNodes=callBackNodes.substring(1,callBackNodes.length);
		}
		if(callBackNodes.length==0){
			alert("请选择打回原因");
			return ;
		}
		retObj[4] = callBackNodes;
		
		/**add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求begin**/
		var approveOpModel ="";//审批退回修改权限
		var approveOp = document.getElementsByName("approveOpModel");
		for(var i=0; i<approveOp.length; i++){
			if(approveOp[i].checked)
				approveOpModel = approveOp[i].value;
		}
		if(approveOpModel==""){
			alert("请选择审批退回修改权限");
			return;
		}
		retObj[5] = approveOpModel;
		/**add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求end**/
	}
	/** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/
	window.returnValue = retObj;
	window.close();
};

function retFail(){
	var retObj = [];
	retObj[0] = false;
	window.returnValue = retObj;
	window.close();
};

//点击节点出人员
function showNodeDiv(obj){
	var selNodeid = obj.value;
	var userList = document.getElementsByName("nodeidInput");
	for(var j=0;j<userList.length;j++){
		var divid = "UsersDiv" + userList[j].value;
		var divObj = document.getElementById(divid);
		if(divObj == null)
			continue;
		if(selNodeid == userList[j].value) {
			divObj.style.display='block';
		} else {
			divObj.style.display='none';
		}
	}
	/**add by lisj 2015-8-5 【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求begin**/
	var is_back='<%=is_back%>';
	if((selNodeid != "90_a3" && selNodeid != "2_a3")||is_back=='no'){
		var approveOp = document.getElementsByName("approveOpModel");
		for(var i=0; i<approveOp.length; i++){
				approveOp[i].disabled = true;
				approveOp[1].checked = true;
		}
	}else{
		var approveOp = document.getElementsByName("approveOpModel");
		for(var i=0; i<approveOp.length; i++){
				approveOp[i].disabled = false;
		}
	}
	/**add by lisj 2015-8-5 【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求end**/
}

</script>


</head>
<body>
<div class="selectNextNodeStyle">
<table border="0" cellspacing="0" cellpadding="0" class="tablemain2">
	<tr>
		<td class="trtitle">请选择已办理过的步骤</td>
	</tr>
	<tr>
	   <table border="0" width="95%" cellspacing="1" cellpadding="0" bgcolor="000000">
        <tr>
	     <td width="50%" class="tdtitle">处理过的节点</td>
	     <td width="50%" class="tdtitle">处理过的人员</td>
        </tr>
		<tr>
			<td class="td">
			<%
				if(wfNodeList!=null && wfNodeList.size()>0) {
					for(int i=0; i<wfNodeList.size(); i++) {
						WFINodeVO nodeVo = wfNodeList.get(i);
						out.print("<input type=\"radio\" name=\"nodeidInput\" value=\""+ nodeVo.getNodeId() + "\" onclick='showNodeDiv(this)' />"  + nodeVo.getNodeName() + "<br/>");
					}
				} else {
					out.print("<font color=red>查询未找到可以退回的节点或当前流程设置不允许打回</font>");
				}
			%>
			</td>
			<td class="td">
			<%
				if(wfNodeList!=null && wfNodeList.size()>0) {
					for(int i=0; i<wfNodeList.size(); i++) {
						WFINodeVO nodeVo = wfNodeList.get(i);
						out.print("<div id=\"UsersDiv"+ nodeVo.getNodeId()+"\" style=\"display:none\">");
						List<WFIUserVO> userList = nodeVo.getUserList();
						String nodeTran = nodeVo.getNodeTransactType();
						boolean isMul = (nodeTran==null||"0".equals(nodeTran)||"1".equals(nodeTran))?false : true; //是否多人办理。true是，false否
						if(userList!=null && userList.size()>0) {
							for(int j=0; j<userList.size(); j++) {
								WFIUserVO userVo = userList.get(j);
								if(isMul) {
									out.print("<input type=\"checkbox\" name=\"userid"+nodeVo.getNodeId()+"\" value=\"" + userVo.getUserId() + "\" />" + userVo.getUserName()+"<br/>");
								} else {
									out.print("<input type=\"radio\" name=\"userid"+nodeVo.getNodeId()+"\" value=\"" + userVo.getUserId() + "\" />" + userVo.getUserName()+"<br/>");
								}
							}
						} else {
							out.print("<font color=red>查询未找到可以退回的人员或请联系系统管理员</font>");
						}
						out.print("</div>");
					}
				} else {
					out.print("&nbsp;");
				}
			%>
			</td>
	    </tr>
	   </table>
	</tr>
	<tr>
		<td class="trtitle">退回后提交方式</td>
	</tr>
	<tr>
		<td style="font-size:9pt">
			<input type="radio" name="callBackModel" value="1">逐级提交
			<input type="radio" name="callBackModel" value="0">提交给退回发起人
		</td>
	</tr>
	<!-- /** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  begin*/ -->
	<%if("true".equals(isShowCBD)){ %>
	<tr>
	   <table border="0" width="95%" cellspacing="1" cellpadding="0" bgcolor="000000">
        <tr>
	     <td width="100%" class="tdtitle">打回原因</td>
        </tr>
		<tr>
			<td class="td">
			<%
				if(wfiCBNodeList!=null && wfiCBNodeList.size()>0) {
					for(int i=0; i<wfiCBNodeList.size(); i++) {
						WFICallBackDisc nodeCBDisc = wfiCBNodeList.get(i);
						out.print("<input type=\"checkbox\" name=\"callBackNode\" value=\"" + nodeCBDisc.getCbEnname() +"\" />" + nodeCBDisc.getCbCnname()+" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;("+nodeCBDisc.getCbMemo()+")<br/>");
					} 
				}else {
					out.print("&nbsp;");
				}
			%>
			</td>
	    </tr>
	   </table>
	</tr>
	<!-- add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求  begin -->
	<tr>
		<td class="trtitle">审批退回修改权限</td>
	</tr>
	<tr>
		<td style="font-size:9pt">
			<input  type="radio" name="approveOpModel" value="1">退回可修改
			<input  type="radio" name="approveOpModel" value="0">退回不可修改
		</td>
		<td>(注：审批退回修改权限仅退回给业务实际发起人才允许发起业务信息修改)</td>
	</tr>
	<!-- add by lisj 2015-8-5 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
	<%} %>
	<!-- /** modified wangj 2015/07/30 需求编号：XD150303016_关于放款审查岗增加打回原因选择框的需求  end*/ -->
</table>
<br></div>
<center>
<input type="button" class="button" value="&nbsp;&nbsp;确&nbsp;&nbsp;&nbsp;&nbsp;定&nbsp;&nbsp;" onclick="retSuc()">&nbsp;&nbsp;
<input type="button" class="button" value="&nbsp;&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;&nbsp;" onclick="retFail()">
</center>
</body>
</html>
