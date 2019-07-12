<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
			<emp:gridLayout id="RscTaskInfoGroup" title="输入查询条件" maxColumn="3">
 				<emp:text id="RscTaskInfo.bill_no" label="借据编号"  />
				<emp:date id="RscTaskInfo.class_date"  label="分类日期 "  />
				<emp:pop id="RscTaskInfo.cus_id" label="客户编号"    url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus"/>
				
				
				<emp:text id="RscTaskInfo.cus_id_cname"   label="客户名称"  readonly="true" />
			</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<!-- 高级搜索区 -->
	 	<div class='emp_gridlayout_title'>风险分类认定历史表列表</div>  
	
	<!-- 工具栏 -->
	<div id="toolBar" style="height:auto"> 
			<!-- 按钮 -->
			<emp:button label="查看" id="viewRecord" op="view"></emp:button>
			<emp:button label="excel导出" id="exportExcel" op="view" ></emp:button>
	 
	</div>
	 
		
	<!-- 列表信息 -->
	<emp:table icollName="RscTaskInfoHisList" url="getRscTaskInfoHisData.do"    >
 		<emp:text id="serno" label="主键" hidden="true"/>
 		<emp:text id="bill_no" label="借据编号"/>
 		<emp:text id="cus_id" label="客户编号"/>
 		<emp:text id="cus_id_cname"    label="客户名称" cssTDClass="tdCenter"/>
		<emp:select id="cur_type" label="币种 " dictname="STD_ZX_CUR_TYPE" cssTDClass="tdCenter"/>
 		<emp:text id="loan_amt" label="贷款金额" dataType='Currency' cssTDClass="tdRight"/>
 		<emp:text id="loan_balance" label="贷款余额" dataType='Currency' cssTDClass="tdRight"/>
		<emp:select id="pre_class_rst"    label="上期分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_eval_mac" label="模型分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:date id="class_date" label="分类日期" />
		<emp:text id="model_class_rea" label="机评理由"   cssTDClass="tdCenter" hidden="true"/> 
<%-- 		<emp:select id="class_rst" label="分类认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/> --%>
		<emp:select id="mgr_mgr_class_rst" label="客户经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="subHead_class_rst" label="客户经理主管认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter" />
		<emp:select id="branchHead_class_rst" label="风险管理部审查员认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="headAcc_class_rst" label="风险管理部总经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="finally_class_rst" label="已完成认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<%-- <emp:select id="subHead_class_rst" label="支行行长认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="branchMgr_class_rst" label="分行售后经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="branchHead_class_rst" label="分行行长认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/> --%>
		<%-- <emp:select id="headAcc_class_rst" label="总行受理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="headApp_class_rst" label="总行审批岗认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/> --%>
		<emp:text id="input_id_displayname" label="客户经理" cssTDClass="tdCenter"/>
		<emp:text id="input_br_id_displayname" label="所属机构" cssTDClass="tdCenter"/>
	</emp:table>
	 
	<script type="text/javascript">
 
	//高级查询
 
	function doQuery(){
		var form = document.getElementById('queryForm');
		RscTaskInfoHis._toForm(form);
		RscTaskInfoHisList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.RscTaskInfoGroup.reset();
	};
	 
	//pop 查询回调客户名称函数
	function returnCus(data){
		RscTaskInfoHis.cus_id._setValue(data.cus_id._getValue());
		RscTaskInfoHis.cus_id_cname._setValue(data.cus_name._getValue());
    };
	
	//新增操作
	function doAddRecord(){
		var url="<emp:url action='getRscTaskInfoHisAddPage.do'/>?op=add";
		url = EMPTools.encodeURI(url);
		var ob={title:'新增风险分类认定历史表',url:url,width:1000,height:500,draggable : true,modal : true,maximized : true};
		EMP.createwin(ob);
	}

	//编辑操作
	function doUpdateRecord(){ 
		var row = $('#RscTaskInfoHis').datagrid('getSelected');
		if(row){
			var url="<emp:url action='getRscTaskInfoHisUpdatePage.do'/>?op=update&"+ 'serno='+row.serno
;
			url = EMPTools.encodeURI(url);
			var ob={title:'更新风险分类认定历史表',url:url,width:1000,height:500,draggable : true,modal : true,maximized : true};
			EMP.createwin(ob); 
		}else{
			EMP.alertMessage("SYS0000007");
			//$.messager.alert('错误','请选择一条记录！','error');
		}
	}

	//查看操作
	function doViewRecord(){ 
		var row = $('#RscTaskInfoHis').datagrid('getSelected');
		if(row){
			var url="<emp:url action='getRscTaskInfoHisDetailPage.do'/>?op=view&"+ 'serno='+row.serno
;
			url = EMPTools.encodeURI(url);
			var ob={title:'查看风险分类认定历史表',url:url,width:1000,height:500,draggable : true,modal : true,maximized : true};
			EMP.createwin(ob); 
		}else{
			EMP.alertMessage("SYS0000007");
			// $.messager.alert('错误','请选择一条记录！','error');
		}
	}
	//删除操作
	function doDeleteRecord(){
		var row = $('#RscTaskInfoHis').datagrid('getSelected');
		if(row){
			// $.messager.confirm('确认', '您确定需要删除该条记录吗？', function(r){
			EMP.confirmMessage('SYS0000008', function(r){
				if (r){
					$.ajax({ 
						type: "POST", 
						dataType: "html",
						url: "<emp:url action='deleteRscTaskInfoHisData.do'/>", 
						data:  'RscTaskInfoHis.serno='+row.serno
 , 
						success: function(data) { 
							try {
								var jsonstr = eval("("+data+")");
							} catch(e) {
								EMP.alertException(data);
								return;
							}
							var flag = jsonstr.flag;
							if(flag=='success'){
								EMP.alertMessage("SYS0000005");
								// $.messager.alert('提示',"删除成功",'info');
								doSearch();
							}else{
								EMP.alertMessage("SYS0000006");
								// $.messager.alert('错误',"删除失败",'error');
							}
							 
						} 
					});
				}
			});
		}else{
			EMP.alertMessage("SYS0000007");
			// $.messager.alert('错误','请选择一条记录！','error');
		}
	}
	
	//导出excel
	function doExportExcel(){
		var cus_id = $('#equals-cus_id').getValue();
		var bill_no = $('#equals-bill_no').getValue();
		var class_date = $('#equals-class_date').getValue();
		var url = '<emp:url action="exportRscTaskInfoHisExcelData.do"/>&equals.cus_id='+cus_id+'&equals.bill_no='+bill_no+'&equals.class_date='+class_date+"&export=yes";
		url = EMPTools.encodeURI(url);
		window.open(url);
	}
	
	//导出Excel模版
	function doExportExcelTemplet(){
		var url = '<emp:url action="exportRscTaskInfoHisExcelTemplet.do"/>';
		url = EMPTools.encodeURI(url);
		window.open(url);
	}

	//导入Excel数据--afterUrl为后续excel文件上传后要执行的方法，必填
	function doImportExcelData(){
		var url = "<emp:url action='showExcelImportPage.do'/>?module_id=12345&func_id=123411&data_id=3332221&afterUrl=importRscTaskInfoHisExcelData.do&allow-doc=xls&allow-size=10&allow-num=1&isSelection=false&isMultiple=false&isModule=false&isFunction=false&returnMethod=returnMethod";
		url = EMPTools.encodeURI(url);
		var ob = {
			title : 'Excel文件上传',
			url : url,
			width : 450,
			height : 400,
			draggable : true,
			modal : true,
			maximized : false
		};
		EMP.createwin(ob);
	}
	
	//导入excle数据时回调函数
	function returnMethod(fs, returnResponses){
		var flag = returnResponses[0].status;
		var message = returnResponses[0].message;
		var param = returnResponses[0].params;
		if(flag == "success"){
			alert(message);
			window.location.reload();
		} else { 
			alert(message);
	  		// .messager;
		}
	}
	
	</script>
	</body>
	</html>
</emp:page>