<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
/******本页的textarea ******/
.emp_field_textarea_textarea {
	width:90%;
	height:50px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
/******gridlayout 宽800 ******/
.emp_gridLayout_table {
	width:800px;
}
/******一列的长text ******/
.emp_field_text_input_long {
	width:90%;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

.emp_field_disabled .emp_field_text_input_long {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

.emp_field_readonly .emp_field_text_input_long {
	border-color: #b7b7b7;
}
/******一列的普通text ******/
.emp_field_text_input {
	width:30%;
	
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
/******两列的text******/
.emp_field_text_t {
	width:200px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.emp_field_disabled .emp_field_text_t {
	border-color: #b7b7b7;
	color: #CEC7BD;
}
.emp_field_readonly .emp_field_text_t {
	border-color: #b7b7b7;
}

</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		//var url = '<emp:url action="querySOrgList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		window.close();
	};
	function retSupOrg(data){
		SOrg.suporganno._setValue(data.organno._getValue());
		}
	function getValue(data){
		SOrg.distno._setValue(data.id);
		SOrg.distname._setValue(data.label);
	};

	function doSave(){
		var form = document.getElementById("submitForm");
		if(SOrg._checkAll()){
			SOrg._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert("保存成功!");
						/**add by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,调整机构等级后需更新授权配置信息表  begin**/
						window.opener.location.reload();
						window.close();
						/**add by lisj 2015-4-22 需求编号：【XD150407025】分支机构授信审批权限配置 ,调整机构等级后需更新授权配置信息表  end**/
					}else {
						alert(msg);
						return;
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
		}else {
			return false;
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSOrgRecord.do" method="POST">
		<emp:gridLayout id="SOrgGroup" maxColumn="2" title="机构表">
			<emp:text id="SOrg.organno" label="机构码" maxlength="16" required="true" colSpan="2" readonly="true" />
			<emp:pop id="SOrg.suporganno" label="上级机构码"  required="true" cssElementClass="emp_field_text_t" colSpan="2" url="querySOrgPop.do?restrictUsed=false" returnMethod="retSupOrg"/>
			<emp:text id="SOrg.arti_organno" label="所属法人机构码" maxlength="20" required="true" colSpan="2" />
			<emp:text id="SOrg.locate" label="位置属性" cssElementClass="emp_field_text_input_long" maxlength="100" required="true" colSpan="2" />
			<emp:text id="SOrg.organname" label="机构名称" maxlength="40" required="true" colSpan="2" />
			<emp:text id="SOrg.organshortform" label="机构简称" maxlength="40" required="false" colSpan="2" />
			<emp:text id="SOrg.enname" label="英文名" maxlength="40" required="false" colSpan="2" />
			<emp:text id="SOrg.orderno" label="序号" maxlength="38" required="false" colSpan="2" hidden="true"/>
			<emp:pop id="SOrg.distno" label="地区编号" required="false" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="getValue" colSpan="2" cssElementClass="emp_field_text_t" />			
			<emp:text id="SOrg.distname" label="地区名称" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_input_long" readonly="true" />
			<emp:date id="SOrg.launchdate" label="开办日期" required="false" colSpan="2" cssElementClass="emp_field_text_t" />
			<emp:text id="SOrg.bank_srrno" label="大额行号" maxlength="21" required="false" cssElementClass="emp_field_text_t" />
			<emp:text id="SOrg.fincode" label="金融代码" maxlength="21" required="false" cssElementClass="emp_field_text_t" />
			<emp:text id="SOrg.bsins_lic" label="营业执照" maxlength="32" required="false" cssElementClass="emp_field_text_t" />
			<emp:text id="SOrg.organchief" label="机构负责人" maxlength="32" required="false" cssElementClass="emp_field_text_t" />
			<emp:select id="SOrg.organlevel" label="机构级别" required="false" dictname="STD_ZB_ORG_LEVEL" cssElementClass="emp_field_text_t" />
			<emp:select id="SOrg.org_lvl" label="机构等级" required="true" dictname="STD_ZB_ORG_LVL" cssElementClass="emp_field_text_t" />
			<emp:text id="SOrg.telnum" label="联系电话" maxlength="20" required="false" dataType="Phone" cssElementClass="emp_field_text_t" />
			<emp:text id="SOrg.fax" label="传真" maxlength="20" required="false" dataType="Phone" cssElementClass="emp_field_text_t" />
			<emp:textarea id="SOrg.address" label="地址" maxlength="200" required="false" colSpan="2"/>
			<emp:text id="SOrg.postcode" label="邮编" maxlength="10" required="false" colSpan="2" /> 
			<emp:text id="SOrg.control" label="控制字" maxlength="10" required="false" hidden="true" colSpan="2" />
			<emp:select id="SOrg.state" label="状态" required="true" dictname="STD_ZB_ORG_STATUS" cssElementClass="emp_field_text_t"  />	
			<emp:select id="SOrg.area_dev_cate_type" label="地区发展分类" required="false" dictname="ZB_CFG_AREA_DEV"  cssElementClass="emp_field_text_t" />		
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
