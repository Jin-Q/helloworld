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
		WfTaskpool._toForm(form);
		WfTaskpoolList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfTaskpoolPage() {
		var paramStr = WfTaskpoolList._obj.getParamStr(['tpid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfTaskpoolUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfTaskpool() {
		var paramStr = WfTaskpoolList._obj.getParamStr(['tpid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfTaskpoolViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfTaskpoolPage() {
		var url = '<emp:url action="getWfTaskpoolAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfTaskpool() {
		var paramStr = WfTaskpoolList._obj.getParamStr(['tpid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfTaskpoolRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfTaskpoolGroup.reset();
	};
	
	/*--user code begin--*/
		
	function doDutyset() {
		var paramStr = WfTaskpoolList._obj.getParamStr(['tpid']);
		if(paramStr==null || paramStr=='') {
			alert('请选择一条记录！');
			return;
		}
		var url = '<emp:url action="getWfTaskpoolDutyPage.do" />?count=n&'+paramStr;
		url = EMPTools.encodeURI(url);
		var retObj = window.showModalDialog(url,'selectPage','dialogHeight:500px;dialogWidth:350px;help:no;resizable:no;status:no;');
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 1) {
					alert('操作成功！');
				} else {
					alert('操作失败！'+flag);
				}
			}catch(e) {
				alert(o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = null;
		var urlSet = '<emp:url action="setWfTaskpoolUsers.do" />?'+paramStr+'&users='+retObj[1];
		urlSet = EMPTools.encodeURI(urlSet);
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', urlSet, callback,postData);
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfTaskpoolGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfTaskpool.tpid" label="项目池编号" />
		<emp:text id="WfTaskpool.tpname" label="项目池名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfTaskpoolPage" label="新增" />
		<emp:button id="getUpdateWfTaskpoolPage" label="修改" />
		<emp:button id="deleteWfTaskpool" label="删除" />
		<emp:button id="viewWfTaskpool" label="查看" />
		<emp:button id="dutyset" label="关联岗位设置" />
	</div>

	<emp:table icollName="WfTaskpoolList" pageMode="true" url="pageWfTaskpoolQuery.do">
		<emp:text id="tpid" label="项目池编号" />
		<emp:text id="tpname" label="项目池名称" />
		<emp:text id="tpdsc" label="描述" />
		<emp:text id="sysid" label="系统ID" />
	</emp:table>
	
</body>
</html>
</emp:page>
    