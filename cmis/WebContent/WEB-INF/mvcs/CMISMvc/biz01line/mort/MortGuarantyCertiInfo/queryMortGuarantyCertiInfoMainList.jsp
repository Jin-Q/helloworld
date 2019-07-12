<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String guaranty_no = (String)context.getDataValue("guaranty_no");
String act = ""; 
if(context.containsKey("act")){
    act = (String)context.getDataValue("act");
}else{
	act = "";
}
String menuId = "";
if(context.containsKey("menuId")){
	menuId =(String) context.getDataValue("menuId");
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
	
	function doViewMortGuarantyCertiInfo() {
		var num = MortGuarantyCertiInfoList._obj.getSelectedData().length;
		if (num == 1) {
			var warrant_no = MortGuarantyCertiInfoList._obj.getSelectedData()[0].warrant_no._obj.value;
			var warrant_type = MortGuarantyCertiInfoList._obj.getSelectedData()[0].warrant_type._obj.value;
			warrant_no = encodeURIComponent(warrant_no);
			warrant_type = encodeURIComponent(warrant_type);
			var menuId = '<%=menuId%>';
			var act = '<%=act%>';
			var paramStr = encodeURI(paramStr);//权证编号转码
			var url = '<emp:url action="getMortGuarantyCertiInfoViewPage.do"/>?act='+act+'&menuId='+menuId+'&warrant_no='+warrant_no+'&warrant_type='+warrant_type;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortGuarantyCertiInfoGroup.reset();
	};
	function doOutStorageMortGuarantyCertiInfo(){
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','guaranty_no','warrant_type']);
		if (paramStr != null) {
			var url = '<emp:url action="outStorageMortGuarantyCertiInfo.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=800, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newwndow',param);
			
		} else {
			alert('请先选择一条记录！');
		}
		
	};
	function doReturnstorageMortGuarantyCertiInfo() {
		var warrantNoStr="";
		var guarantyNoStr="";
		var warrantTypeStr="";
		var warrant_state = "";
		var warrantStateStr = "";
		var num = MortGuarantyCertiInfoList._obj.getSelectedData().length;
		if (num != 0) {
			for(var i=0;i<num;i++){
				warrantNoStr = warrantNoStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_no._getValue()+",";
				guarantyNoStr = guarantyNoStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].guaranty_no._getValue()+",";
				warrantTypeStr = warrantTypeStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_type._getValue()+",";
				warrantStateStr = warrantStateStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue()+",";
				
				warrant_state = MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue();
				if(warrant_state!="4"){
					alert("只有借出状态的权证可以做归还入库操作！");
					return;
				}
			}

			//权证编号中文传输会乱码，所以使用编码传输
			warrantNoStr = encodeURIComponent(warrantNoStr);
			if(confirm("是否确认要归还入库？")){
				var handleSuccess = function(o) {
					EMPTools.unmask();
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("授权失败！");
							return;
						}
						var flag = jsonstr.flag;
						var msg = jsonstr.msg;
						if("success" == flag){
							alert("已生成授权！");
							window.location.reload();
						}else{
							alert("生成授权失败！错误信息："+msg);
						}
					}
				};
				var handleFailure = function(o) {
					alert("生成授权失败！");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				
				var url = '<emp:url action="storageMortGuarantyCertiInfoRecord.do"/>?stor=storage&warrantStateStr='+warrantStateStr+'&warrantNoStr='+warrantNoStr+'&guarantyNoStr='+guarantyNoStr+'&warrantTypeNoStr='+warrantTypeStr+'&guaranty_no='+'<%=guaranty_no%>';
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请选择需要做归还入库操作的权证记录！');
		}
	};
	
	//入库操作
	function doStorageMortGuarantyCertiInfo() {
		var warrantNoStr="";
		var guarantyNoStr="";
		var warrantTypeStr="";
		var warrant_state = "";
		var warrantStateStr = "";
		var num = MortGuarantyCertiInfoList._obj.getSelectedData().length;
		if (num != 0) {
			for(var i=0;i<num;i++){
				warrantNoStr = warrantNoStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_no._getValue()+",";
				guarantyNoStr = guarantyNoStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].guaranty_no._getValue()+",";
				warrantTypeStr = warrantTypeStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_type._getValue()+",";
				warrantStateStr = warrantStateStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue()+",";
				
				warrant_state = MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue();
				if(warrant_state!="1"&&warrant_state!="6"&&warrant_state!="7"){
					alert("只有登记，核销，出库状态的权证可以做入库操作！");
					return;
				}
			}

			//权证编号中文传输会乱码，所以使用编码传输
			warrantNoStr = encodeURIComponent(warrantNoStr);
			
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("授权失败！");
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
						alert("已生成授权！");
						//打印入库单据
						window.location.reload();
					}else{
						alert("生成授权失败！错误信息："+msg);
					}
				}
			};
			var handleFailure = function(o) {
				alert("生成授权失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			
			var url = '<emp:url action="storageMortGuarantyCertiInfoRecord.do"/>?stor=storage&warrantStateStr='+warrantStateStr+'&warrantNoStr='+warrantNoStr+'&guarantyNoStr='+guarantyNoStr+'&warrantTypeNoStr='+warrantTypeStr+'&guaranty_no='+'<%=guaranty_no%>';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请选择需要做入库操作的权证记录！');
		}
	};
	
	//出库
	function doOutMortGuarantyCertiInfo(){
		var warrantNoStr="";
		var guarantyNoStr="";
		var warrantTypeStr="";
		var warrantStateStr="";
		var warrant_state = "";
		var num = MortGuarantyCertiInfoList._obj.getSelectedData().length;
		if (num != 0) {
			for(var i=0;i<num;i++){
				warrantNoStr = warrantNoStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_no._getValue()+",";
				
				guarantyNoStr = guarantyNoStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].guaranty_no._getValue()+",";
				warrantTypeStr = warrantTypeStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_type._getValue()+",";
				warrantStateStr = warrantStateStr+MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue()+",";
				warrant_state = MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue();
				if(warrant_state!="3"){
					alert("只有在库状态的权证可以做出库操作！");
					return;
				}
			}
			//权证编号中文传输会乱码，所以使用编码传输
			warrantNoStr = encodeURIComponent(warrantNoStr);

			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("出库失败！");
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						var serno = jsonstr.serno;
						var url = '<emp:url action="getMortStorExwaInfoUpdatePage.do"/>?serno='+serno+'&restrict_tab=Y&openType=Y';
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
						window.location.reload();
					}else{
						alert("出库失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("出库失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			
			var url = '<emp:url action="storageMortGuarantyCertiInfoRecord.do"/>?stor=exwa&warrantStateStr='+warrantStateStr+'&warrantNoStr='+warrantNoStr+'&guarantyNoStr='+guarantyNoStr+'&warrantTypeNoStr='+warrantTypeStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请选择需要做出库操作的权证记录！');
		}
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortGuarantyCertiInfoGroup" title="输入查询条件" maxColumn="3">
		<emp:select id="MortGuarantyCertiInfo.warrant_cls" label="权证类别" dictname="STD_WARRANT_TYPE" />
		<emp:text id="MortGuarantyCertiInfo.warrant_no" label="权证编号" />
		<emp:text id="MortGuarantyCertiInfo.guaranty_no" label="押品编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	
	<div align="left">
		<emp:button id="viewMortGuarantyCertiInfo" label="查看" op="view"/>
		<emp:button id="outMortGuarantyCertiInfo" label="出库" op="exwa"/>
		<emp:button id="storageMortGuarantyCertiInfo" label="入库" op="to_storage"/>
		<emp:button id="returnstorageMortGuarantyCertiInfo" label="归还入库" op="return_storage"/>
	</div>

	<emp:table icollName="MortGuarantyCertiInfoList" pageMode="true" url="pageMortGuarantyCertiInfoQuery.do?act=${context.act}" selectType="2">
		
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
		<!-- modified by yangzy 2015/05/07 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="客户经理" hidden="true"/>
		<!-- modified by yangzy 2015/05/07 需求：XD150325024，集中作业扫描岗权限改造 end -->
		<emp:text id="hx_serno" label="核心流水号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    