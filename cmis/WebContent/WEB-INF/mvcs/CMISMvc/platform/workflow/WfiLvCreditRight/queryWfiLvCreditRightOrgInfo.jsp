<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String right_type = request.getParameter("right_type");
	context.put("right_type", right_type);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doInitWfiLvCreditRight() {
 if(!WfiLvCreditRight._checkAll()){
		return false;
 }else{
	var org_id = WfiLvCreditRight.org_id._getValue();
	if(confirm("初始化分支行授权配置将会覆盖原配置信息，是否继续？")){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert(e.message);
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						alert('初始化授权配置成功！');
						window.opener.location.reload();
						doClose();
					}else if(msg=='fail'){
						alert('初始化授权配置失败!');
					}else{
						alert('初始化授权配置成功，但写入操作记录异常！');
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("初始化失败，请联系管理员！");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var url = '<emp:url action="initWfiLvCreditRightRecord.do"/>?org_id='+org_id;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
   }
};

function doInitCommBranchCreditRight(){
	if(!WfiLvCreditRight._checkAll()){
		return false;
 	}else{
 		var cb_org_id = WfiLvCreditRight.cb_org_id._getValue();
 		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert(e.message);
					return;
				}
				var flag=jsonstr.flag;	
				if(flag=="success"){
			 		var url = '<emp:url action="getCommBranchCreditRightUpdatePage.do"/>?cb_org_id='+cb_org_id;
					url = EMPTools.encodeURI(url);
					window.location=url;
				}else if(flag=="exist"){
					doReset();
					alert("已存在该社区支行权限配置信息！");
				}else{
					alert("异步校验调用异常！");
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("异步调用校验失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var url = '<emp:url action="checkCommBranchRecord.do"/>?cb_org_id='+cb_org_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
 	}
};

function doClose(){
 window.close();
};

function doReset(){
	page.dataGroups.WfiLvCreditRightGroup.reset();
};
function doOnLoad(){
	if(<%=right_type%> == "03"){
		WfiLvCreditRight.org_id_displayname._obj._renderHidden(true);
		WfiLvCreditRight.org_id_displayname._obj._renderRequired(false);
		WfiLvCreditRight.org_id._obj._renderHidden(true);
		WfiLvCreditRight.org_id._obj._renderRequired(false);
		
		WfiLvCreditRight.cb_org_id_displayname._obj._renderHidden(false);
		WfiLvCreditRight.cb_org_id_displayname._obj._renderRequired(true);
		WfiLvCreditRight.cb_org_id._obj._renderHidden(false);
	}
};

//返回社区支行机构
function getCBName(data){
	WfiLvCreditRight.cb_org_id._setValue(data.comm_branch_id._getValue());
	WfiLvCreditRight.cb_org_id_displayname._setValue(data.comm_branch_name._getValue());
};

//返回主管机构
function getOrganName(data){
	WfiLvCreditRight.org_id._setValue(data.organno._getValue());
	WfiLvCreditRight.org_id_displayname._setValue(data.organname._getValue());
	WfiLvCreditRight.org_lvl._setValue(data.org_lvl._getValue());
};

</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="" method="POST">	
		<emp:gridLayout id="WfiLvCreditRightGroup" title="初始化授信审批权限机构选择" maxColumn="2">
		 	<emp:pop id="WfiLvCreditRight.org_id_displayname" label="机构名称" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" colSpan="2"/>
			<emp:text id="WfiLvCreditRight.org_id" label="机构码" maxlength="16" readonly="true" />
			<emp:text id="WfiLvCreditRight.org_lvl" label="机构等级" hidden="true"/>
			
			<emp:pop id="WfiLvCreditRight.cb_org_id_displayname" label="社区支行机构名称" required="false" url="queryCommBranchPop.do" returnMethod="getCBName" hidden="true" colSpan="2" />
		    <emp:text id="WfiLvCreditRight.cb_org_id" label="社区支行机构码" maxlength="16" readonly="true" required="false" hidden="true"/>
    	</emp:gridLayout>
		<div align="center">
			<br>
			<%if(!"".equals(right_type) && "03".equals(right_type)){ %>
			    <emp:button id="initCommBranchCreditRight" label="确定"/>
			<%}else{ %>
				<emp:button id="initWfiLvCreditRight" label="确定"/>
			<%} %>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
    