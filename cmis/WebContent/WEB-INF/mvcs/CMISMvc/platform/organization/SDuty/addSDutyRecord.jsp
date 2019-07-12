<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAddSDuty() {
		var form = document.getElementById("submitForm");
		var result = SDuty._checkAll();
		if(result){
			SDuty._toForm(form);
			toSubmitForm(form);
		}else alert("请输入必填项！");
	};
	function toSubmitForm(form){
	
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					var dutyno = jsonstr.dutyno;
					if(flag == "exist"){
						alert("该岗位【"+dutyno+"】已存在，不能重复添加");
						return;
					}else if(flag == "sucess") {
                        alert("添加岗位成功...");
                        window.close();
                        return;
                       }else{
						alert("操作失败");
						return ;
                    }
		                        
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
		  }
	
	function doReturn() {
		//var url = '<emp:url action="querySOrgList.do"/>';
		//url = EMPTools.encodeURI(url);
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSDutyRecord.do" method="POST">
		
		<emp:gridLayout id="SDutyGroup" title="岗位表" maxColumn="2">
			<emp:text id="SDuty.dutyno" label="岗位码" maxlength="20" required="true"/>
			<emp:text id="SDuty.dutyname" label="岗位名称" maxlength="40" required="true" />
			<emp:text id="SDuty.organno" label="机构码" maxlength="16" required="false" />
			<emp:text id="SDuty.depno" label="部门码" maxlength="16" required="false" />
			<emp:text id="SDuty.orderno" label="排序字段" hidden="true" maxlength="38" required="false" />
			<emp:text id="SDuty.orgid" label="组织号" hidden="true" maxlength="16" required="false" />
			<emp:text id="SDuty.type" label="类型" maxlength="38" required="false" hidden="true"/>
			<emp:textarea id="SDuty.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addSDuty" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

