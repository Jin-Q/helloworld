<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>鍒楄〃鏌ヨ椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddLmtIntbnkAppPage(){
		var url = '<emp:url action="getLmtIntbnkAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtIntbnkApp(){		
		var paramStr = LmtIntbnkAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("确定要删除吗？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert(o.responseText);
							return;
						}
						var flag = jsonstr.flag;
						var msg = jsonstr.msg;
						if(flag == "success"){
							alert("操作成功！");
							doQuery();
						}else {
							alert(o.responseText);
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
				var url = '<emp:url action="deleteLmtIntbnkAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
			}
		} else {
			alert('请选择一条记录！');
		}
	};
	
	function doGetUpdateLmtIntbnkAppPage(){
		var paramStr = LmtIntbnkAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtIntbnkAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	function doViewLmtIntbnkApp(){
		var paramStr = LmtIntbnkAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryLmtIntbnkAppDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIntbnkApp._toForm(form);
		LmtIntbnkAppList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtIntbnkAppGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIntbnkAppGroup" title="查询条件" maxColumn="2">
			<emp:text id="LmtIntbnkApp.serno" label="流水号" />
			<emp:text id="LmtIntbnkApp.bank_name" label="同业机构名称" />
			<emp:text id="LmtIntbnkApp.crd_biz_type" label="授信业务类型" />
			<emp:text id="LmtIntbnkApp.crd_lmt_type" label="授信额度类型" />
			<emp:text id="LmtIntbnkApp.bank_type" label="同业机构类型" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddLmtIntbnkAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtIntbnkAppPage" label="修改" op="update"/>
		<emp:button id="deleteLmtIntbnkApp" label="删除" op="remove"/>
		<emp:button id="viewLmtIntbnkApp" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtIntbnkAppList" pageMode="true" url="pageLmtIntbnkAppQuery.do">
		<emp:text id="serno" label="流水号" />
		<emp:text id="bank_no" label="同业机构行号" />
		<emp:text id="bank_name" label="同业机构名称" />
		<emp:text id="bank_type" label="同业机构类型" />
		<emp:text id="crd_biz_type" label="授信业务类型" />
		<emp:text id="crd_lmt_type" label="授信额度类型" />
		<emp:text id="auto_score" label="机评得分" />
		<emp:text id="auto_grade" label="机评等级" hidden="true"/>
		<emp:text id="crd_lmt_amt" label="授信限额" />
		<emp:text id="crd_totl_amt" label="授信总额" />
		<emp:text id="approve_status" label="审批状态" />
		<emp:text id="cur_type" label="币种" />
		<emp:text id="term_type" label="期限类型" hidden="true"/>
		<emp:text id="term" label="期限" hidden="true"/>
		<emp:text id="apply_date" label="申请日期" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="update_date" label="修改日期" hidden="true"/>
		<emp:text id="input_id" label="申请人" hidden="true"/>
		<emp:text id="cus_manager" label="客户经理" hidden="true"/>
		<emp:text id="input_br_id" label="申请机构" hidden="true"/>
		<emp:text id="mng_br_id" label="主管机构" hidden="true"/>
		<emp:text id="remarks" label="备注" hidden="true"/>
		<emp:text id="lmt_serno" label="授信协议编号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>