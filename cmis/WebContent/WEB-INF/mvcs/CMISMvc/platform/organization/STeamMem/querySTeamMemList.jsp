<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String team_no = (String)context.getDataValue("team_no");
	String act = "";
	if(context.containsKey("act")){
		act = (String)context.getDataValue("act");
	}
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		STeamInfo._toForm(form);
		STeamInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSTeamMemPage() {
		var data = STeamMemList._obj.getSelectedData();
		if (data.length==1) {
			var mem_no = data[0].mem_no._getValue();
			var url = '<emp:url action="getSTeamMemUpdatePage.do"/>?mem_no='+mem_no;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'IntroSTeamMem',url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSTeamMem() {
		var data = STeamMemList._obj.getSelectedData();
		if (data.length==1) {
			var mem_no = data[0].mem_no._getValue();
			var url = '<emp:url action="getSTeamMemViewPage.do"/>?mem_no='+mem_no;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'IntroSTeamMem',url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSTeamMemPage() {
		var url = '<emp:url action="getSTeamMemAddPage.do"/>?team_no='+'<%=team_no%>';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'IntroSTeamMem',url);
	};
	function doIntroSTeamMem() {
		var url = '<emp:url action="querySUserIntroList.do"/>&returnMethod=getTeamMem&team_no='+'<%=team_no%>';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'IntroSTeamMem',url);
	};
	function getTeamMem(data){
		var mem_no = new Array();
		for(var i=0;i<data.length;i++){
			mem_no.push(data[i].actorno._getValue());
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert(msg);
					var url = '<emp:url action="querySTeamMemList.do"/>?team_no='+'<%=team_no%>';
					url = EMPTools.encodeURI(url);
					window.location=url;
				}else{
					alert(msg);
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="introTeamMem.do"/>?mem_no='+mem_no+'&team_no='+'<%=team_no%>';
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		
	}
	
	function doDeleteSTeamMem() {
		var paramStr = STeamMemList._obj.getSelectedData();
		var mem_no = new Array();
		for(var i=0;i<paramStr.length;i++){
			mem_no.push(paramStr[i].mem_no._getValue());
		}
		if (paramStr.length != 0) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已成功删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteSTeamMemRecord.do"/>?mem_no='+mem_no;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<div align="left">
	  <%if (!act.equals("view")){ %>
		<emp:button id="IntroSTeamMem" label="引入" op="add"/>
		<emp:button id="getUpdateSTeamMemPage" label="修改" op="update"/>
		<emp:button id="deleteSTeamMem" label="删除" op="remove"/>
		<emp:button id="viewSTeamMem" label="查看" op="view"/>
		<%}else{ %>
		<emp:button id="viewSTeamMem" label="查看" op="view"/>
		<%} %>
	</div>
		<emp:table icollName="STeamMemList" pageMode="true" url="pageSTeamMemQuery.do?team_no=${context.team_no}" selectType="2">
			<emp:text id="team_no" label="团队编号" maxlength="20" required="false" />
			<emp:text id="mem_no" label="成员编号" maxlength="20" required="false" />
			<emp:text id="mem_no_displayname" label="成员名称" required="false" />
			<emp:text id="team_role" label="团队角色" maxlength="2" required="false" dictname="STD_TEAM_ROLE" />
		</emp:table>

	
</body>
</html>
</emp:page>

