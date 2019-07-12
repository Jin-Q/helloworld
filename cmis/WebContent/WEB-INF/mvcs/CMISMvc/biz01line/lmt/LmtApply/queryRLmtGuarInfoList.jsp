<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<% //String op=(String)request.getParameter("op"); %>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doViewRLmtGuarInfo() {
		var paramStr = RLmtGuarInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?'+paramStr+'&menuId=mort_maintain';
			url = EMPTools.encodeURI(url);
			var param = 'height=600, width=900, top=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddRLmtGuarInfoPage() {
		var cus_id='${context.cus_id}';
		var url = '<emp:url action="queryMortGuarantyPopList.do"/>?cus_id='+cus_id+'&returnMethod=getGuaranty';
		url = EMPTools.encodeURI(url);
		var param = 'height=600, width=800, top=80, left=400, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function getGuaranty(data){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert(msg);
					window.location.reload();
				}else{
					alert(msg);
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var serno='${context.serno}';
		var limit_code='${context.limit_code}';
		var guaranty_no = data.guaranty_no._getValue();
		var url = '<emp:url action="insert2RLmtGuar.do"/>?serno='+serno+'&guaranty_no='+guaranty_no+'&limit_code='+limit_code;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);	
	}
	
	function doDeleteRLmtGuarInfo() {
		var paramStr = RLmtGuarInfoList._obj.getParamStr(['guaranty_no']);
		var limit_code='${context.limit_code}';
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
	    		var handleSuccess = function(o){ 		
						var jsonstr = eval("(" + o.responseText + ")");
						var flag = jsonstr.flag;
						if(flag == "suc" ){
							alert("删除成功！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				var handleFailure = function(o){
					alert("异步回调失败！");	
				};
				var url = '<emp:url action="deleteRLmtGuarInfoRecord.do"/>?'+paramStr+"&limit_code="+limit_code;
				var callback = {
						success:handleSuccess,
						failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);		
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
 <emp:tabGroup mainTab="base_tab" id="mainTab" >
   <emp:tab label="抵质押物" id="base_tab" needFlush="true" initial="true">
   
	<div align="left">
		<emp:actButton id="getAddRLmtGuarInfoPage" label="引入" op="add"/>
		<emp:actButton id="deleteRLmtGuarInfo" label="删除" op="remove"/>
		<emp:actButton id="viewRLmtGuarInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="RLmtGuarInfoList" pageMode="true" url="pageRLmtGuarInfoQuery.do?limit_code=${context.limit_code}">
		<emp:text id="guaranty_no" label="担保品编号" />
		<emp:text id="guaranty_name" label="担保品名称"/>
		<emp:text id="guaranty_cls" label="担保品类别" dictname="STD_GUARANTY_TYPE"/>			
		<emp:text id="cus_id" label="担保人名称" hidden="true"/>
		<emp:text id="cus_id_displayname" label="担保人名称"/>
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_type_displayname" label="担保品类型"/>
		<emp:text id="wrr_amt" label="权利金额(元)" dataType="Currency"/>
		<emp:text id="guar_amt" label="担保金额(元)" dataType="Currency"/>
		<emp:text id="guaranty_info_status" label="担保品状态" dictname="STD_MORT_STATE" />
		<emp:text id="input_date" label="申请时间" />
	</emp:table>
 </emp:tab>
	<emp:tab label="保证人" id="subTab" url="queryRLmtGuarntrInfoList.do?serno=${context.serno}&type=${context.op}&limit_code=${context.limit_code}" initial="false" needFlush="true"/>
 </emp:tabGroup>
</body>
</html>
</emp:page>
    