<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	       //获取管理机构信息
			function getOrgID(data){
				LmtIntbankBatchList.manager_br_id._setValue(data.organno._getValue());
				LmtIntbankBatchList.manager_br_id_displayname._setValue(data.organname._getValue());
			}
			//获取责任人信息
			function setconId(data){
				LmtIntbankBatchList.manager_id_displayname._setValue(data.actorname._getValue());
				LmtIntbankBatchList.manager_id._setValue(data.actorno._getValue());
			}
			
			function doReturn(){
				var url = '<emp:url action="queryLmtIntbankBatchListList.do"/>';
				url = EMPTools.encodeURI(url);
				window.location=url;
			}
			/**在进行下一步时，校验E级和不评级客户不能进行授信**/
			function doAdd(){
				if(LmtIntbankBatchList._checkAll()){
					var crd_grade = LmtIntbankBatchList.cdt_lvl._getValue()+'';
					if(crd_grade=='E'||crd_grade=='Z' ){
						alert("批量包信用等级不能为E级和未评级");
						
					}else{
						saveBatch();
					}
				}else{
					alert("请检查必输项！");
				}			
			}
			//通过异步保存批量包
			function saveBatch(){			
				var form = document.getElementById("submitForm");				
				LmtIntbankBatchList._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var message = jsonstr.message;
						var serno = jsonstr.serno;
						if(flag == "success"){
							var url = '<emp:url action="getLmtIntbankBatchListUpdatePage.do"/>?serno='+serno+"&op=add";
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else if(flag=="fail"){
							alert(message);
						}else{
							alert("发生异常！");
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
			}
	
</script>
</head>
<body class="page_content">
	<emp:form  method="POST" action="#" id="queryForm"></emp:form>
	<emp:form id="submitForm" action="getAddLmtIntbankBatchListRecord.do" method="POST">
		<emp:gridLayout id="LmtIntbankBatchListGroup" title="同业客户批量名单维护" maxColumn="2">
			<emp:select id="LmtIntbankBatchList.batch_cus_type" label="批量客户类型" required="true" dictname="STD_ZB_BATCH_CUS_TYPE" defvalue="01"/>
			<emp:select id="LmtIntbankBatchList.cdt_lvl" label="信用等级" required="true" dictname="STD_ZB_FINA_GRADE"/>
			<emp:text id="LmtIntbankBatchList.input_id_displayname" label="登记人" required="true"  readonly="true" defvalue="$currentUserName"/>
			<emp:text id="LmtIntbankBatchList.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			
			<emp:text id="LmtIntbankBatchList.batch_cus_no" label="批量客户编号" maxlength="32" required="false" hidden="true"/>
			<emp:pop id="LmtIntbankBatchList.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" required="false" returnMethod="setconId" 
			         popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"  hidden="true"/>
			<emp:pop id="LmtIntbankBatchList.manager_br_id_displayname" label="管理机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" 
			         popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" hidden="true"/>
			<emp:date id="LmtIntbankBatchList.input_date" label="登记日期" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="LmtIntbankBatchList.input_id" label="登记人" maxlength="20"  hidden="true" defvalue="$currentUserId"/>
			<emp:text id="LmtIntbankBatchList.input_br_id" label="登记机构" maxlength="32" required="true" hidden="true" defvalue="$organNo"/>
			<emp:select id="LmtIntbankBatchList.approve_status" label="审批状态" required="false" hidden="true" defvalue="000"/>
			<emp:text id="LmtIntbankBatchList.serno" label="业务编号" maxlength="32" hidden="true" />
			<emp:date id="LmtIntbankBatchList.start_date" label="生效日期" required="false" hidden="true"/>
			<emp:date id="LmtIntbankBatchList.end_date" label="到期日期" required="false" hidden="true"/>
			<emp:select id="LmtIntbankBatchList.status" label="状态" required="false" dictname="STD_ZB_INTBANK_STATE" defvalue="01" hidden="true"/>
			<emp:pop id="LmtIntbankBatchList.manager_id" label="责任人" url="null" hidden="true"/>
			<emp:pop id="LmtIntbankBatchList.manager_br_id" label="管理机构" url="null" hidden="true" />
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="add" label="下一步" />
			<emp:button id="return" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
