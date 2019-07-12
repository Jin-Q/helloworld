<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
	<head>
	<title>支持多选机构POP页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<style type="text/css">
		/*************** 输入框(input)普通状态下的样式 ********************/
		.emp_field_longtext_input { /****** 长度固定 ******/
			width: 250px;
			border-width: 1px;
			border-color: #b7b7b7;
			border-style: solid;
			text-align: left;
		}
		
		.emp_field_text_readonly {
				border: 1px solid #b7b7b7;
				background-color:#eee;
				text-align: left;
				width: 600px;
				height: 80px;
			};
	</style>
	<script type="text/javascript">
	function doQuery() {
		var form = document.getElementById('queryForm');
		SOrg._toForm(form);
		SOrgList._obj.ajaxQuery(null, form);
	};

	function doReset() {
		page.dataGroups.SOrgGroup.reset();
	};

	/*--user code begin--*/

	function doSelect() {
		doAddOrgno();
	};

	function doReturnMethod(methodName){
		if (methodName) {
			var data = Array();
			data[0] = document.all.orgnos.value;
			data[1] = document.all.orgnoNames.value;
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data)");
			window.close();
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	

	function doAddOrgno(){
		var data = SOrgList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var orgnos=document.all.orgnos.value;
			var orgno=data[0].organno._getValue();
			var orgnoNames=document.all.orgnoNames.value;
			
			if(orgnoNames!=null&&orgnoNames!=''){			
				orgnoNames = orgnoNames.substring(0,orgnoNames.indexOf("("));
			}
			var orgnoName=data[0].organname._getValue();
			if(orgnos.indexOf(orgno)!=-1){
				alert("机构号:"+orgno+" 机构名称："+orgnoName+" 已经选择。");
			}else{
				if(orgnos==''){
					document.all.orgnos.value=data[0].organno._getValue();
				//	document.all.orgnoNames.value=data[0].organname._getValue();
					orgnoNames=data[0].organname._getValue();
				}else{
					document.all.orgnos.value=document.all.orgnos.value+","+data[0].organno._getValue();
				//	document.all.orgnoNames.value=document.all.orgnoNames.value+","+data[0].organname._getValue();
					orgnoNames=orgnoNames+","+data[0].organname._getValue();
				}
			}
			var orgnoNamess = orgnoNames.split(",");
			orgnoNames=orgnoNames+"(共选择："+orgnoNamess.length+"个机构)";
			document.all.orgnoNames.value=orgnoNames;
		}
	}
	/*--user code end--*/
</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="SOrg.organno" label="机构码" />
		<emp:text id="SOrg.organname" label="机构名称" cssElementClass="emp_field_longtext_input" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:table icollName="SOrgList" pageMode="true" url="pagesSOrgQuery.do?restrictUsed=false">
		<emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" />
		<emp:text id="organname" label="机构名称" />
		<emp:text id="distno" label="地区编号" />
		<emp:text id="fincode" label="金融代码" />
	</emp:table>

	<div align="center"><br>
	<emp:textarea id="orgnoNames" label=" "  readonly="true" cssElementClass="emp_field_text_readonly"/>
	<emp:textarea id="orgnos" label=" " hidden="true"/><br>
	<emp:button label="加入" id="addOrgno"></emp:button>
	<emp:returnButton label="选择返回"/>
	<br>
	</div>
	</body>
	</html>
</emp:page>
