<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String guaranty_no = (String)context.getDataValue("guaranty_no");
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
		var data = MortGuarantyCertiInfoList._obj.getSelectedData();
		if (data.length==1) {
			var warrant_no = data[0].warrant_no._getValue();
			//权证编号中文传输会乱码，所以使用编码传输
			warrant_no = encodeURIComponent(warrant_no);
			
			var warrant_type = data[0].warrant_type._getValue();
			var warrant_state = data[0].warrant_state._getValue();
			if(warrant_state!="1"&&warrant_state!="6"&&warrant_state!="7"){
				alert('非登记，核销，出库状态的权证记录不能进行修改操作！');
				return;
			}
			var url = '<emp:url action="getMortGuarantyCertiInfoUpdateIgnorePage.do"/>?warrant_no='+warrant_no+'&warrant_type='+warrant_type;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantyCertiInfo() {
		var data = MortGuarantyCertiInfoList._obj.getSelectedData();
		if (data.length==1) {
			var warrant_no = data[0].warrant_no._getValue();
			//权证编号中文传输会乱码，所以使用编码传输
			warrant_no = encodeURIComponent(warrant_no);
			
			var warrant_type = data[0].warrant_type._getValue();
			var url = '<emp:url action="getMortGuarantyCertiInfoViewIgnorePage.do"/>?warrant_no='+warrant_no+'&warrant_type='+warrant_type;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doGetAddMortGuarantyCertiInfoPage() {
		var url = '<emp:url action="getMortGuarantyCertiInfoAddPage.do"/>?guaranty_no='+'<%=guaranty_no%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortGuarantyCertiInfo() {
		var data = MortGuarantyCertiInfoList._obj.getSelectedData();
		if (data.length==1) {
			var warrant_no = data[0].warrant_no._getValue();
			//权证编号中文传输会乱码，所以使用编码传输
			warrant_no = encodeURIComponent(warrant_no);
			
			var warrant_type = data[0].warrant_type._getValue();
			var is_main_warrant = data[0].is_main_warrant._getValue();
			var warrant_state = data[0].warrant_state._getValue();
			if(is_main_warrant=="1"){
				alert('主权证信息不能被删除！');
				return;
			}
			if(warrant_state!="1"){
				alert('非登记状态的权证记录不能被删除！');
				return;
			}
			if(confirm("是否确认要删除？")){
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
							alert("已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
					alert("删除失败!");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteMortGuarantyCertiInfoRecord.do"/>?warrant_no='+warrant_no+'&warrant_type='+warrant_type;
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
					if("success" == flag){
						alert("已生成授权！");
						//打印入库单据
						window.location.reload();
					}else{
						alert("生成授权失败！");
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
	function doStorageMortGuarantyCertiInfo_p() {
		var num = MortGuarantyCertiInfoList._obj.getSelectedData().length;
		if (num == 1) {
			for(var i=0;i<num;i++){
				var guarantyNo = MortGuarantyCertiInfoList._obj.getSelectedData()[i].guaranty_no._getValue();
				var warrantNo = MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_no._getValue();
				warrantNo = encodeURIComponent(warrantNo);
				var warrantType = MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_type._getValue();
				var warrant_state = MortGuarantyCertiInfoList._obj.getSelectedData()[i].warrant_state._getValue();
				if(warrant_state!="2"){
					alert("只有入库记账中的权证可以做入库单打印！");
					return;
				}
			}
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("获取入库申请信息失败！");
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						var serno = jsonstr.serno;
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/yprkd1.raq&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						alert("获取入库申请信息失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("获取入库申请信息失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="printStorageMortGuarantyCertiInfo.do"/>?guarantyNo='+guarantyNo+'&warrantNo='+warrantNo+'&warrantType='+warrantType;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请选择一笔需要打印入库单的权证记录！');
		}
	}
/** add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin**/
	//下载导入模板
	function doDownLoadMortGuarantyCertiInfoTemplate(){
		var url = '<emp:url action="downLoadMortGuarantyCertiInfoTmplate.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//导入EXCEL
    function doImportMortGuarantyCertiInfoByExcel(){
    	var url = '<emp:url action="queryMortGuarantyCertiInfoImport.do"/>?guaranty_no='+'<%=guaranty_no%>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }
  /**add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end**/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddMortGuarantyCertiInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateMortGuarantyCertiInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteMortGuarantyCertiInfo" label="删除" op="remove"/>
		<emp:actButton id="viewMortGuarantyCertiInfo" label="查看" op="view"/>
		<emp:actButton id="storageMortGuarantyCertiInfo" label="入库" op="to_storage"/>
		<emp:actButton id="storageMortGuarantyCertiInfo_p" label="打印入库单" op="to_storage"/>
		<emp:actButton id="outMortGuarantyCertiInfo" label="出库" op="exwa"/>
		<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin-->
		<emp:actButton id="downLoadMortGuarantyCertiInfoTemplate" label="下载导入模板" op="down"/>
		<emp:actButton id="importMortGuarantyCertiInfoByExcel" label="导入" op="imp"/>
		<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程end-->
	</div>

	<emp:table icollName="MortGuarantyCertiInfoList" pageMode="true" url="pageMortGuarantyCertiInfoQuery.do?guaranty_no=${context.guaranty_no}" selectType="2">
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
		<emp:text id="hx_serno" label="核心流水号"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    