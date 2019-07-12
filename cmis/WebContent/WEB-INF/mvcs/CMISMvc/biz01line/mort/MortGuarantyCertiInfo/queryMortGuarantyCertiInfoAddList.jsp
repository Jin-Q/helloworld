<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op=(String)context.getDataValue("op");
	}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantyCertiInfo._toForm(form);
		MortGuarantyCertiInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortGuarantyCertiInfoPage() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyCertiInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantyCertiInfo() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type','warrant_state']);
		if (paramStr != null) {
			paramStr = encodeURI(paramStr);
			var url = '<emp:url action="getMortGuarantyCertiInfoViewPage.do"/>?exwa=exwa&'+paramStr+'&op=view';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=yes, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doStorageMortGuarantyCertiInfo() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		if (paramStr != null) {
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						alert("已成功入库");
						//var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?status=finish&menuId=stay_storage';
						//url = EMPTools.encodeURI(url);
						window.location.reload();
					}else{
						alert("入库失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("入库失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			
			var url = '<emp:url action="storageMortGuarantyCertiInfoRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doAddMortGuarantyCertiInfo() {
		var stor_exwa_mode = '${context.stor_exwa_mode}';
		var act = "";
		if("04"==stor_exwa_mode){//出入库方式为入库时，添加是选择的记录为登记或者借出状态的记录
			act='register';
		}else{//出库时，只能选择到在库状态的权证记录
			act='act';
		}
		var url = '<emp:url action="queryMortGuarantyCertiInfoList.do"/>?act='+act+'&returnMethod=getGuaranty&restrictUsed=false';
		url = EMPTools.encodeURI(url);
		var param = 'height=800, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newwndow',param);
	};
	//权证添加返回方法
	function getGuaranty(data){
		var warrantNoStr="";
		var guarantyNoStr="";
		var warrantTypeStr="";
		var warrantStateStr="";
		var warrant_state = "";
		var num = data.length;
		var serno = '${context.serno}';
		var stor_exwa_mode = '${context.stor_exwa_mode}';//出入库方式
		for(var i=0;i<num;i++){
			warrantNoStr = warrantNoStr+data[i].warrant_no._getValue()+",";
			guarantyNoStr = guarantyNoStr+data[i].guaranty_no._getValue()+",";
			warrantTypeStr = warrantTypeStr+data[i].warrant_type._getValue()+",";
			warrantStateStr = warrantStateStr+data[i].warrant_state._getValue()+",";
			warrant_state = data[i].warrant_state._getValue();
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					window.location.reload();
				}else{
					alert("添加失败！");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var stor = "";//区别出入库时所传参数。
		if("04"==stor_exwa_mode){//出入库方式为入库时，
			stor="storage";
		}else{
			stor="exwa";
		}
		var parm = encodeURI('warrantNoStr='+warrantNoStr);//权证编号转码
		var url = '<emp:url action="storageMortGuarantyCertiInfoRecord.do"/>?stor='+stor+'&serno='+serno+'&'+parm+'&warrantStateStr='+warrantStateStr+'&guarantyNoStr='+guarantyNoStr+'&warrantTypeNoStr='+warrantTypeStr;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function doRemoveMortGuarantyCertiInfo() {
		var serno = '${context.serno}';
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		if (paramStr != null) {
			if(confirm("是否确认要剔除？")){
				var handleSuccess = function(o) {
					EMPTools.unmask();
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已剔除！");
							window.location.reload();
						}else{
							alert("剔除失败！");
						}
					}
				};
				var handleFailure = function(o) {
					alert("剔除失败!");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var paramStr = encodeURI(paramStr);//权证编号转码
				var url = '<emp:url action="deleteMortStorExwaDetailRecord.do"/>?'+paramStr+'&serno='+serno;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortGuarantyCertiInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
	
		<emp:button id="viewMortGuarantyCertiInfo" label="查看"/>
		<%if(!"view".equals(op)){ %>
		<!--<emp:button id="addMortGuarantyCertiInfo" label="添加"/>-->
		<emp:button id="removeMortGuarantyCertiInfo" label="剔除"/>
		<%} %>
	</div>

	<emp:table icollName="MortGuarantyCertiInfoList" pageMode="false" url="">
		<emp:text id="warrant_cls" label="权证类别" dictname="STD_WARRANT_TYPE" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="warrant_type" label="权证类型" hidden="true"/>
		<emp:text id="warrant_no" label="权证编号" />
		<emp:text id="warrant_name" label="权证名称" />
		<emp:text id="is_main_warrant" label="是否主权证" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_no_displayname" label="押品名称" hidden="true"/>
		<emp:text id="keep_org_no" label="保管机构" hidden="true"/>
		<emp:text id="hand_org_no" label="经办机构" hidden="true"/>
		<emp:text id="keep_org_no_displayname" label="保管机构" />
		<emp:text id="hand_org_no_displayname" label="经办机构" hidden="true"/>
		<emp:text id="warrant_state" label="权证状态" dictname="STD_WARRANT_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    