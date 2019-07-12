<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.yucheng.cmis.pub.util.TimeUtil" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doHandOver(){
		if(CusSubmitInfo._checkAll()){
		//	var receiverId = CusSubmitInfo.rcver_id._getValue();
		//	var cusId = CusSubmitInfo.cus_id._getValue();
			CusSubmitInfo.opr_type._setValue("3");  //移交
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("数据库操作失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag==""){
						alert("移交失败");							
					}else{
						alert("移交成功");	
						window.close();
						window.opener.location.reload();
					} 
				}
			};
			var handleFailure = function(o){ 
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			//设置form
			var form = document.getElementById("submitForm");
			CusSubmitInfo._toForm(form)
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusSubmitInfoRecord.do" method="POST">
		<emp:gridLayout id="CusSubmitInfoGroup" title="集中作业移交操作" maxColumn="2">
			<emp:text id="CusSubmitInfo.cus_id" label="客户码" maxlength="21" required="true"  readonly="true" />
			<emp:text id="CusSubmitInfo.cus_name" label="客户名称" maxlength="60" required="false" readonly="true"/>
			<emp:text id="CusSubmitInfo.rcv_id" label="提交人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:textarea id="CusSubmitInfo.handover_memo" label="移交说明" maxlength="400" required="true" colSpan="2" />
			<emp:date id="CusSubmitInfo.handover_date" label="移交日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusSubmitInfo.serno" label="流水号" maxlength="32" readonly="true" hidden="true"/>
			<emp:text id="CusSubmitInfo.end_flag" label="完成标志(0.完成 1.未完成)" maxlength="1"  defvalue="1" hidden="true" />
			<emp:text id="CusSubmitInfo.opr_time" label="具体时间" maxlength="20" readonly="false" hidden="true" />
			<emp:text id="CusSubmitInfo.opr_type" label="操作类型(1.提交 2.打回 3.移交)" maxlength="10" readonly="false" hidden="true" defvalue="3"/>
			<emp:text id="CusSubmitInfo.twoSubFlag" label="再次提交" maxlength="10" readonly="false" hidden="true"/>
		</emp:gridLayout>		
		<emp:gridLayout id="ActorsGroup" title="选择集中作业人员" maxColumn="2">
				<emp:radio id="CusSubmitInfo.handover_id" label="选择接收人" dictname="actors" delimiter="&nbsp;&nbsp;&nbsp;" layout="fasle" required="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="handOver" label="移交"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
