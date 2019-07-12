<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
    function doQuery(){
	    var form = document.getElementById('queryForm');
	    PrdBankInfo._toForm(form);
	    PrdBankInfoList._obj.ajaxQuery(null,form);
    };
	
    function doReset(){
		page.dataGroups.PrdBankInfoGroup.reset();
	};
	
	/*--user code begin--*/
	
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = PrdBankInfoList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdBankInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdBankInfo.bank_no" label="行号" />
			<emp:text id="PrdBankInfo.bank_name" label="行名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
	<emp:table icollName="PrdBankInfoList" pageMode="true" url="popPagePrdBankInfoQuery.do?flag=${context.flag}&status=1">
	<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
		<emp:text id="bank_no" label="行号" />
		<emp:text id="bank_name" label="行名" />
		<emp:text id="area_code" label="地区代码" />
		<emp:text id="phone" label="联系电话" />
		<emp:text id="pcode" label="邮政编码" />
		<emp:text id="addr" label="地址" />
		<emp:text id="last_bank_no" label="上级行号" />
	</emp:table>
	<div align="left"><br>
	  <emp:returnButton label="引入"/> <br>
	</div>
</body>
</html>
</emp:page>
    