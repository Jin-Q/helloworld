<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateCusHandoverDetailPage() {
		var paramStr = CusHandoverDetailList._obj.getParamStr(['sub_serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getCusHandoverCfgCusHandoverDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};
	
	function doGetAddCusHandoverDetailPage(){
		var serno = window.parent.window.CusHandoverCfg.serno._getValue();
		var url = '<emp:url action="getCusHandoverCfgCusHandoverDetailAddPage.do"/>?CusHandoverDetail.serno='+serno;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	function doDeleteCusHandoverDetail() {
		var paramStr = CusHandoverDetailList._obj.getParamStr(['sub_serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusHandoverCfgCusHandoverDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusHandoverDetail() {
		var paramStr = CusHandoverDetailList._obj.getParamStr(['sub_serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusHandoverCfgCusHandoverDetailDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">

	<div align="left">
		<emp:button id="getAddCusHandoverDetailPage" label="新增" />
		<emp:button id="getUpdateCusHandoverDetailPage" label="修改" />
		<emp:button id="deleteCusHandoverDetail" label="删除" />
		<emp:button id="viewCusHandoverDetail" label="查看" />
	</div>
							
	<emp:table icollName="CusHandoverDetailList" pageMode="true" url="pageCusHandoverCfgCusHandoverDetailQuery.do" reqParams="CusHandoverCfg.serno=$CusHandoverCfg.serno;">

		<emp:text id="sub_serno" label="主键" hidden="true"/>
		<emp:text id="serno" label="序列号" hidden="true"/>
		<emp:text id="table_code" label="表编码" />
		<emp:text id="table_name" label="表名称" />
		<emp:text id="ext_sql" label="执行语句" />
		<emp:text id="memo" label="备注" />
		<emp:text id="sort" label="执行顺序" />
	</emp:table>
				
</body>
</html>
</emp:page>