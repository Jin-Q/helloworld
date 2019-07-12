<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String lmt_serno = (String)context.getDataValue("serno");
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CcrAppInfo._toForm(form);
		CcrAppInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCcrAppInfoPage() {
		var paramStr = CcrAppInfoList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			//判断审批状态，如果审批中的，不允许修改或删除
			var data = CcrAppInfoList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status =="111"){
				alert("审批中的评级申请无法修改");
				return;
			}
			var url = '<emp:url action="getCcrAppInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCcrAppInfo() {
		var paramStr = CcrAppInfoList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrAppInfoViewPage.do"/>?'+paramStr+'&restrict_tab=Y&menuIdTab=ccrappinfoXX';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看评级历史
	function doViewCcrAppInfoHis() {
		var paramStr = CcrAppInfoHisList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrAppInfoViewPage.do"/>?'+paramStr+'&restrict_tab=Y&menuIdTab=ccrappinfoXX&openType=pop';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCcrAppInfoPage() {
		var lmt_serno = '<%=lmt_serno%>';
		var url = '<emp:url action="getCcrAppIndivInfoAddPage.do"/>?lmt_serno='+lmt_serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCcrAppInfo() {
		var paramStr = CcrAppInfoList._obj.getParamStr(['serno','cus_id','approve_status']);
		if (paramStr != null) {
			//判断审批状态，如果审批中的，不允许修改或删除
			var data = CcrAppInfoList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			
			if(approve_status == '000' || approve_status == '992'  ){			
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteCcrAppInfoRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						EMPTools.unmask();
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("删除失败!");
								return;
							}
							var flag=jsonstr.flag;	
							var flagInfo=jsonstr.flagInfo;						
							if(flag=="success"){
								alert('删除成功！');
								window.location.reload();					
							}
						}	
					};
					var handleFailure = function(o){ 
						alert("删除失败，请联系管理员");
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}; 
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert('该申请不是待发起或是退回状态，不能删除!');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CcrAppInfoGroup.reset();
	};
	
	/*--user code begin--*/	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div class='emp_gridlayout_title'>个人信用评级&nbsp;</div>
	<div align="left">
		<emp:actButton id="getAddCcrAppInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateCcrAppInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteCcrAppInfo" label="删除" op="remove"/>
		<emp:button id="viewCcrAppInfo" label="查看"/><!-- 复议时导致menuId错乱此次查看按钮一直放开 -->
	</div>

	<emp:table icollName="CcrAppInfoList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_type" label="客户类型"  dictname="STD_ZB_CUS_TYPE" hidden="true"/>
		<emp:text id="app_begin_date" label="申请日期" /> 
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="expiring_date" label="到期日期" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true"/>
	</emp:table>
	<br/>
	<div class='emp_gridlayout_title'>个人信用评级历史&nbsp;</div>
	<div align="left">
		<emp:button id="viewCcrAppInfoHis" label="查看"/>
	</div>
	<emp:table icollName="CcrAppInfoHisList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="expiring_date" label="到期日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:select id="adjusted_grade" label="调整后信用等级" dictname="STD_ZB_CREDIT_GRADE"/>
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    