<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateIndGroupIndexPage() {
		var paramStr = IndGroupIndexList._obj.getParamStr(['group_no','index_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="getIndGroupIndGroupIndexUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddIndGroupIndexPage(){
		var group_no = window.parent.window.IndGroup.group_no._getValue();
		var url = '<emp:url action="getIndGroupIndGroupIndexAddPage.do"/>?IndGroupIndex.group_no='+group_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteIndGroupIndex() {
		var paramStr = IndGroupIndexList._obj.getParamStr(['group_no','index_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndGroupIndGroupIndexRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndGroupIndex() {
		var paramStr = IndGroupIndexList._obj.getParamStr(['group_no','index_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndGroupIndGroupIndexDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
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
		<emp:button id="getAddIndGroupIndexPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndGroupIndexPage" label="修改" op="update"/>
		<emp:button id="deleteIndGroupIndex" label="删除" op="remove"/>
		<emp:button id="viewIndGroupIndex" label="查看" op="view"/>
	</div>
							
	<emp:table icollName="IndGroupIndexList" pageMode="true" url="pageIndGroupIndGroupIndexQuery.do" reqParams="IndGroup.group_no=$IndGroup.group_no;">

		<emp:text id="group_no" label="组别编号" />
		<emp:text id="index_no" label="指标编号" />
		<emp:text id="index_name" label="指标名称" />
		<emp:text id="ind_std_score" label="标准分" />
		<emp:text id="weight" label="权重" />
		<emp:text id="rule_classpath" label="指标评分规则" /> 
		<emp:select id="dis_property" label="显示属性" dictname="STD_ZB_DISPLAY_PROP"/>
		<emp:select id="ind_dis_type" label="指标显示类型" dictname="STD_ZB_PARA_DISP_TYP"/>
		<emp:text id="seq_no" label="顺序号" />
	</emp:table>
				
</body>
</html>
</emp:page>