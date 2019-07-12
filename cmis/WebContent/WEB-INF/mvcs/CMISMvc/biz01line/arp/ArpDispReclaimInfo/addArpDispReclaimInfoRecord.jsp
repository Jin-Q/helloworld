<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
    String asset_disp_no = request.getParameter("asset_disp_no");
	String guaranty_no = request.getParameter("guaranty_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doLoad(){
		var asset_disp_no='<%=asset_disp_no%>';
		var guaranty_no = '<%=guaranty_no%>';
		ArpDispReclaimInfo.asset_disp_no._setValue(asset_disp_no);
		ArpDispReclaimInfo.guaranty_no._setValue(guaranty_no);
	}
	function doReturn(){
		var asset_disp_no='<%=asset_disp_no%>';
		var guaranty_no = '<%=guaranty_no%>';
		var url = '<emp:url action="queryArpDispReclaimInfoList.do"/>?guaranty_no='+guaranty_no+'&asset_disp_no='+asset_disp_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	function doNext(){
		if(!ArpDispReclaimInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		ArpDispReclaimInfo._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功!");
					doReturn();
				}else {
					alert("保存成功!"); 
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addArpDispReclaimInfoRecord.do" method="POST">
		
		<emp:gridLayout id="ArpDispReclaimInfoGroup" title="处置回收信息" maxColumn="2">
			<emp:text id="ArpDispReclaimInfo.serno" label="业务编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="ArpDispReclaimInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="ArpDispReclaimInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="false" readonly="true"/>
			<emp:select id="ArpDispReclaimInfo.is_cash" label="是否现金" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="ArpDispReclaimInfo.disp_amt" label="处置金额" maxlength="16" required="true" dataType="Currency" />
			<emp:date id="ArpDispReclaimInfo.disp_date" label="处置日期" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="ArpDispReclaimInfoGroup" title="登记信息" maxColumn="2">
			<emp:text id="ArpDispReclaimInfo.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="ArpDispReclaimInfo.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="ArpDispReclaimInfo.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="ArpDispReclaimInfo.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="ArpDispReclaimInfo.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

