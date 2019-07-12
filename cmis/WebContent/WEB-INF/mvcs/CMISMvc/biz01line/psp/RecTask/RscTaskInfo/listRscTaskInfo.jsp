<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
			<emp:gridLayout id="RscTaskInfoGroup" title="输入查询条件" maxColumn="3">
 				<emp:text id="RscTaskInfo.bill_no" label="借据编号"  />
				<emp:select id="RscTaskInfo.pre_class_rst"  label="上期分类结果 " dictname="STD_ZB_NINE_SORT" />
				<emp:pop id="RscTaskInfo.cus_id" label="客户编号"   url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
				
				
				<emp:text id="RscTaskInfo.cus_id_cname"   label="客户名称"  readonly="true" />
				<emp:text id="RscTaskInfo.duty"   label="岗位" defvalue="mgr" hidden="true" />
			</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left"><!-- 按钮 -->
			<emp:button label="认定" id="verifyRecord"  op="verify" ></emp:button>
			<emp:button label="提交" id="submitRecord"  op="submit"></emp:button>
			<emp:button label="赋认定默认值" id="assignmentRecord"   op="assignment"  ></emp:button>
	 
	</div>
	
 
		
	<!-- 列表信息 -->
	<emp:table  icollName="RscTaskInfoList" pageMode="true"  url="pageRscTaskInfoQuery.do?duty=mgr"  >
 		<emp:text id="serno" label="主键" hidden="true"/>
 		<emp:text id="bill_no" label="借据编号"/>
 		<emp:text id="cus_id" label="客户编号"/>
 		<emp:text id="cus_id_displayname"  label="客户名称" cssTDClass="tdCenter"/>
		<emp:select id="cur_type" label="币种 " dictname="STD_ZX_CUR_TYPE"  cssTDClass="tdCenter"/>
 		<emp:text id="loan_amt" label="贷款金额" dataType='Currency' cssTDClass="tdRight"/>
 		<emp:text id="loan_balance" label="贷款余额" dataType='Currency' cssTDClass="tdRight"/>
		<emp:select id="pre_class_rst"   label="上期分类结果 " dictname="STD_ZB_NINE_SORT"   cssTDClass="tdCenter"/>
		<emp:select id="model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
	    <emp:select id="risk_cls_status" label="风险分类状态 " dictname="STD_ZB_RISK_CLS_ST" cssTDClass="tdCenter"  hidden="true"/>
		<emp:select id="model_eval_mac" label="模型分类结果 " dictname="STD_ZB_NINE_SORT"  cssTDClass="tdCenter"/>
		<emp:text id="model_class_rea" label="机评理由"    cssTDClass="tdCenter" hidden="true"/> 
		
		<emp:select id="class_rst" label="客户经理认定结果 " dictname="STD_ZB_NINE_SORT"   cssTDClass="tdCenter"/> 
		<emp:text id="class_date" label="分类日期"  cssTDClass="tdCenter" />
	</emp:table>
	<script src="<emp:file fileName='scripts/jquery/jquery-1.7.2.min.js'/>"
	type="text/javascript"></script>
	<script type="text/javascript">
 
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		RscTaskInfo.duty._setValue("mgr");
		RscTaskInfo._toForm(form);
		RscTaskInfoList._obj.ajaxQuery(null,form);
	};


	/* 赋默认值操作 */
	function doAssignmentRecord(){
		var serno = '';
		$.ajax({ 
			type: "POST", 
			dataType: "html",
			url: "<emp:url action='updateRscTaskInfoData4kcoll.do'/>", 
			data:  'RscTaskInfo.serno='+serno+'&duty='+'${param.duty}' +'&type=assignment'+'&dutyno=S0403'+'&risk_cls_status='+"01", 
			success: function(data) { 
				try {
					var jsonstr = eval("("+data+")");
				} catch(e) {
					EMP.alertException(data);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
					alert("赋值成功!");
					// $.messager.alert('提示',"赋值成功!",'info');
					doQuery();
				}else if(flag=="out"){
					alert("未完成！");//未完成
				}else{
					alert("赋值失败!");
					// $.messager.alert('错误',"赋值失败!",'error');
				}
				 
			} 
		});
	}
	function doReset(){
		page.dataGroups.RscTaskInfoGroup.reset();
	};
	//新增操作
	function doAddRecord(){
		var url="<emp:url action='getRscTaskInfoAddPage.do'/>?op=add";
		url = EMPTools.encodeURI(url);
		var ob={title:'新增风险分类认定',url:url,width:1000,height:500,draggable : true,modal : true,maximized : true};
		EMP.createwin(ob);
	}

	//编辑操作(认定操作)
	function doVerifyRecord(){  
		var paramStr = RscTaskInfoList._obj.getParamValue(['serno']);
		var duty = '${param.duty}';
		if (paramStr != null) {
			var risk_cls_status = RscTaskInfoList._obj.getSelectedData()[0].risk_cls_status._getValue();
             var serno = RscTaskInfoList._obj.getSelectedData()[0].serno._getValue();
			if(risk_cls_status !="01"){
				alert("分类状态非【未分类】,不能发起认定操作！");
				return false;
				}
			var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?serno="+serno+'&op=update&duty=' +duty+'&dutyno=S0403';
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert('请先选择一条记录！');
			//$.messager.alert('错误','请选择一条记录！','error');
		}
	}

	//查看操作
	function doViewRecord(){ 
		var row = $('#RscTaskInfo').datagrid('getSelected');
		if(row){
			var url="<emp:url action='getRscTaskInfoDetailPage.do'/>?op=view&"+ 'serno='+row.serno;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert("请选择一条记录！");
			// $.messager.alert('错误','请选择一条记录！','error');
		}
	}
	
	function view(value,rowData,index){
		var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=view&"+ 'serno='+rowData.serno;
		url = EMPTools.encodeURI(url);
		return "<a href=\"javascript:window.location='"+url+"'\">"+value+"</a>";
	}
	
	//导出excel
	function doExportExcel(){
		var url = '<emp:url action="exportRscTaskInfoExcelData.do"/>?duty=${param.duty}';
		url = EMPTools.encodeURI(url);
		window.open(url);
	}
	
	//导出Excel模版
	function doExportExcelTemplet(){
		var url = '<emp:url action="exportRscTaskInfoExcelTemplet.do"/>';
		url = EMPTools.encodeURI(url);
		window.open(url);
	}

	//导入Excel数据--afterUrl为后续excel文件上传后要执行的方法，必填
	function doImportExcelData(){
		var url = "<emp:url action='showExcelImportPage.do'/>?module_id=12345&func_id=123411&data_id=3332221&afterUrl=importRscTaskInfoExcelData.do&allow-doc=xls&allow-size=10&allow-num=1&isSelection=false&isMultiple=false&isModule=false&isFunction=false&returnMethod=returnMethod";
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
			alert("操作成功");
				indow.location.reload();
		} else {
		//	var params = new Array();
		//	params[0] = param;
			alert(message);
	  		// .messager;
		}
	}
	//pop 查询回调客户名称函数
	 
	function returnCus(data){
		RscTaskInfo.cus_id._setValue(data.cus_id._getValue());
		RscTaskInfo.cus_id_cname._setValue(data.cus_name._getValue());
    };
	//提交
	function doSubmitRecord(){
			var paramStr = RscTaskInfoList._obj.getParamValue(['serno']);
			var duty = "${param.duty}";
						var handleSuccess = function(o){ 
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("Parse jsonstr define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag=='success'){
								alert("提交成功!");
								 window.location.reload();
							}else{
								alert("提交失败!");
							}
						}; 
						var handleFailure = function(o){
							alert("异步请求出错！");	
						};
						var callback = {
							success:handleSuccess,
							failure:handleFailure
						};
						var url = "<emp:url action='updateRscTaskInfoData4kcoll.do'/>?RscTaskInfo.serno="+ RscTaskInfoList._obj.getSelectedData()[0].serno._getValue()+'&duty='+duty+'&type=submit'+"&risk_cls_status=01"+"&dutyno=S0403", 
						url = EMPTools.encodeURI(url);
						var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	</script>
	</body>
	</html>
</emp:page>