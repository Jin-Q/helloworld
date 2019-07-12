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
		CusBlkList._toForm(form);
		CusBlkListList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusBlkListPage() {
		var paramStr = CusBlkListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusBlkListList._obj.getParamValue('status');
			if(status != '001'&&status != '004') {
			    alert("只有状态为[预登记]或[撤销]的记录才能修改!");
			    return ;
			}
			var url = '<emp:url action="getCusBlkListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
	    }else {
			alert('请先选择一条记录！');
		}
	}
	
	function doViewCusBlkList() {
		var paramStr = CusBlkListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusBlkListViewPage.do"/>?'+paramStr+'&type=query';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusBlkListPage() {
		var url = '<emp:url action="getCusBlkListAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusBlkList() {
		var data = CusBlkListList._obj.getSelectedData();
		if (data != null) {
			var status = CusBlkListList._obj.getParamValue('status');
			var serno = CusBlkListList._obj.getParamValue('serno');
			if(status != '001'&&status != '004') {
			    alert("只有状态为[预登记]或[撤销]的记录才能删除!");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusBlkListRecord.do"/>?serno='+serno;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						var flagInfo=jsonstr.flagInfo;						
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();								
						}
					}
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.CusBlkListGroup.reset();
	};
	//生效
	function doInureCusBlkList(){
		var status = CusBlkListList._obj.getParamValue(['status']);
		if (status != null) {
			if(status=='001'||status=='004'){
				var paramStr = CusBlkListList._obj.getParamStr(['serno']);
				if (paramStr != null) {
					paramStr += '&status=002';
					doUpdateStatus(paramStr);
				}
			}else{
				alert('请选择状态为[预登记]或[撤销]的记录进行操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	//撤销
	function doCancleCusBlkList(){
		var status = CusBlkListList._obj.getParamValue(['status']);
		if (status != null) {
			if(status=='002'){
				var paramStr = CusBlkListList._obj.getParamStr(['serno']);
				if (paramStr != null) {
					paramStr += '&status=004';
					doUpdateStatus(paramStr);
				}
			}else{
				alert('请选择状态为[生效]的记录进行操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	/*--user code begin--*/
	function doUpdateStatus(paramStr){
		var url = '<emp:url action="updateCusBlkStatus.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("主担保金额校验失败!");
					return;
				}
				var flag=jsonstr.flag;	
				var flagInfo=jsonstr.flagInfo;						
				if(flag=="success"){
					alert("操作成功！");
					window.location.reload();
				}else{
					alert("操作失败，请联系管理员");
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("操作失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	//导入
    function doImportCusBlkList(){
    	var url = '<emp:url action="queryCusBlkListImport.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }
    //下载模板
    function doDownLoadCusBlkList(){
    	var url = '<emp:url action="downLoadCusBlkTmplate.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
    }
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="CusBlkListGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusBlkList.cus_name" label="客户名称" />
			<emp:select id="CusBlkList.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="CusBlkList.cert_code" label="证件号码" />
			<emp:select id="CusBlkList.status" label="状态" dictname="STD_CUS_BLK_STATUS"/>
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:button id="downLoadCusBlkList" label="下载模板" op="add"/>
		<emp:button id="importCusBlkList" label="导入" op="add"/>
		<emp:button id="getAddCusBlkListPage" label="新增" op="add"/>
		<emp:button id="viewCusBlkList" label="查看" op="view"/>
		<emp:button id="getUpdateCusBlkListPage" label="修改" op="update"/>
		<emp:button id="deleteCusBlkList" label="删除" op="remove"/>
		<emp:button id="inureCusBlkList" label="生效" op="inure"/>
		<!--  <emp:button id="cancleCusBlkList" label="撤销" op="cancl"/>-->
		
		
	</div>

	<emp:table icollName="CusBlkListList" pageMode="true" url="pageCusBlkListQuery.do">
		<emp:text id="black_date" label="列入日期" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" hidden="false"/>
		<emp:text id="legal_name" label="法定代表人" hidden="false"/>
		<emp:text id="black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
		<emp:text id="data_source" label="数据来源" dictname="STD_ZB_DATA_SOURCE"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" hidden="false"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:date id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="status" label="状态" dictname="STD_CUS_BLK_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    