<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String serno = request.getParameter("serno");//申请编号
	String cus_id_zz = request.getParameter("cus_id_zz");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//选择客户码
	function returnCus(data){
		var cus_crd_grade = data.cus_crd_grade._getValue();
		var belg_line = data.belg_line._getValue();
		if("BL300"==belg_line){
			LmtAppNameList.cus_id_displayname._setValue(data.cus_name._getValue());
			LmtAppNameList.cus_id._setValue( data.cus_id._getValue() );
			if(data.cus_id._getValue()=='<%=cus_id_zz%>'){
				alert('该客户为组长不能再新增！');
				LmtAppNameList.cus_id_displayname._setValue('');
				LmtAppNameList.cus_id._setValue('');
			}
			return;
		}
		if(cus_crd_grade=='11'||cus_crd_grade=='12'||cus_crd_grade=='13'){
			LmtAppNameList.cus_id_displayname._setValue(data.cus_name._getValue());
			LmtAppNameList.cus_id._setValue( data.cus_id._getValue() );
			if(data.cus_id._getValue()=='<%=cus_id_zz%>'){
				alert('该客户为组长不能再新增！');
				LmtAppNameList.cus_id_displayname._setValue('');
				LmtAppNameList.cus_id._setValue('');
			}
		}else{
			alert('联保小组成员评级应达到我行信用等级A级（含）以上!');
		}
	}

	//确定保存
	function doSave(){
		var handSuc = function(o){
			if(o.responseText !== undefined) {
				try { var jsonstr = eval("("+o.responseText+")"); }
					catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "suc"){
					alert("保存成功!");
					var serno = LmtAppNameList.serno._getValue();
					var cus_id = LmtAppNameList.cus_id._getValue();
					var url = '<emp:url action="getLmtNameList_jointUpdatePage.do"/>?serno='+serno+"&cus_id="+cus_id+"&cus_id_zz=<%=cus_id_zz%>";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert(flag);
				}
			}
		};
	    var handFail = function(o){};
	    var callback = {
	    	success:handSuc,
	    	failure:handFail
	    };
	    var form = document.getElementById("submitForm");
		LmtAppNameList._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action, callback,postData);
	}

	/**保存前先校验:1.是否已存在该申请中
				   2.是否存在有效联保小组名单中
				   3.是否存在在途的联保申请中*/
	function doCheckBeforeSave(){
		if(!LmtAppNameList._checkAll()){
			return ;
		}
		var cus_id = LmtAppNameList.cus_id._getValue();
		var url = '<emp:url action="chkBeforeAddJointCoopNameRecord.do"/>&cus_id='+cus_id+"&serno=<%=serno%>";
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("联保新增校验失败!");
					return;
				}
				var flag=jsonstr.flag;
				var returnMsg = jsonstr.returnMsg;
				if(flag=="success"){
					doSave();
				}else if(flag=="existThis"){//该客户已存在该笔申请中
					alert(returnMsg);
				}else{
					if(confirm(returnMsg)){
						doSave();
					}
				}
			}	
		};
		var handleFailure = function(o){
			alert("联保新增校验失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	
	//返回
	function doReturn(){
		var url = '<emp:url action="queryLmtNameList_jointList.do"/>?serno=<%=serno%>&cus_id=<%=cus_id_zz%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
		<emp:form id="submitForm" action="addLmtNameList_jointRecord.do" method="POST">
			
			<emp:gridLayout id="LmtAppNameListGroup" title="联保小组成员信息新增" maxColumn="2">
				<emp:pop id="LmtAppNameList.cus_id" label="客户码" readonly="false" colSpan="2" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cus_type!='A2'&returnMethod=returnCus" required="true" />
				<emp:text id="LmtAppNameList.cus_id_displayname" label="客户名称" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				<emp:text id="LmtAppNameList.bail_rate" label="保证金比例" maxlength="16" required="true" dataType="Percent" colSpan="2"/>
				<emp:select id="LmtAppNameList.is_limit_set" label="是否进行额度设置" required="true" dictname="STD_ZX_YES_NO" defvalue="1" hidden="true"/>
				<emp:textarea id="LmtAppNameList.memo" label="备注" maxlength="400" required="false" colSpan="2" />
				<emp:text id="LmtAppNameList.serno" label="业务编号" maxlength="40" required="false" hidden="true" defvalue="<%=serno%>"/>
				<emp:select id="LmtAppNameList.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE" defvalue="03" colSpan="2" readonly="true" hidden="true"/>
			</emp:gridLayout>
			
			<div align="center">
				<br>
				<emp:button id="checkBeforeSave" label="保存"/>
				<emp:button id="reset" label="重置"/>
				<emp:button id="return" label="返回"/>
			</div>
		</emp:form>
</body>
</html>
</emp:page>

