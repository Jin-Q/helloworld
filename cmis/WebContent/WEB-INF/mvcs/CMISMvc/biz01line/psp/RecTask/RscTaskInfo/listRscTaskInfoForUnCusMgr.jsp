<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head>
<title>列表查询页面</title> 
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
</head>
<body class="page_content" onload="doOnLoad()">
	<!-- 高级搜索区 -->
		<form  method="POST" action="#" id="queryForm">
	</form>
			<emp:gridLayout id="RscTaskInfoGroup" title="输入查询条件" maxColumn="3">
 				<emp:text id="RscTaskInfo.bill_no" label="借据编号"  />
				<emp:select id="RscTaskInfo.pre_class_rst"  label="上期分类结果 " dictname="STD_ZB_NINE_SORT" />
				<emp:pop id="RscTaskInfo.cus_id" label="客户编号"   url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
				<emp:text id="RscTaskInfo.cus_id_cname"   label="客户名称"  readonly="true" /> 
				<emp:select id="RscTaskInfo.dutyno" label="审查岗"  dictname="STD_ZB_RSC_DUTY" /> 
		
			</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	
	<emp:panel name="p1" title="" >
		<div id="dlg-buttons" style="text-align: left;">
<%-- 		<emp:button text="EXCEL导出模板" id="exportExcelTemplet" iconCls="icon-print"  op="excelTemplet"   ></emp:button> --%>
			<emp:button label="提交" id="submitRecord"  op="submit"  ></emp:button>
			<emp:button label="赋认定默认值" id="assignmentRecord"  op="assignment"  ></emp:button>
		</div>
		<div style="margin-top:20px;"></div> 
		<emp:gridLayout id="loanGroup" title=" " maxColumn="2">
		<emp:text id="Loan.loanAmt" label="贷款总金额"   dataType="Currency"   readonly="true" />
	   	<emp:text id="Loan.loanBalance" label="不良贷款总金额"  dataType="Currency"   readonly="true" />
		</emp:gridLayout>
	</emp:panel>
	
	<!-- 工具栏 -->
		<div class='emp_gridlayout_title'>有疑义的风险分类列表</div>
	<div id="toolBar" style="height:auto">
		<div style="margin-bottom: 5px" align="left">
			<!-- 按钮 -->
			<emp:button label="EXCEL导出" id="exportExcel"   op="excelExport"  ></emp:button>
			<emp:button label="EXCEL导入" id="ImportExcelData"  op="excelImport"  ></emp:button>
			<emp:button label="认定" id="verifyRecord"   op="verify"  ></emp:button>
			<emp:button label="退回" id="fallBack"   op="fallBack"  ></emp:button>
		</div>
	</div>
		
	<!-- 列表信息 --> 

	<emp:table  icollName="RscTaskInfoList" pageMode="true" url="getRscTaskInfoData.do"  reqParams="RscTaskInfo.doubt=yes&RscTaskInfo.bch_cde=${context.organNo}&RscTaskInfo.usr_cde=${context.currentUserId}"    >
 		<emp:text id="serno" label="主键" hidden="true"/>
 		<emp:link id="bill_no" label="借据编号" operation="viewRecord" />
 		<emp:text id="cus_id" label="客户编号"/>
 		<emp:text id="cus_id_displayname"    label="客户名称" cssTDClass="tdCenter"/>
		<emp:select id="cur_type" label="币种 " dictname="STD_ZX_CUR_TYPE" cssTDClass="tdCenter"/>
 		<emp:text id="loan_amt" label="贷款金额" dataType='Currency' cssTDClass="tdRight" />
 		<emp:text id="loan_balance" label="贷款余额" dataType='Currency' cssTDClass="tdRight"/>
		<emp:select id="pre_class_rst"   label="上期分类结果 "  dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_eval_mac" label="模型分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:text id="model_class_rea" label="机评理由"   cssTDClass="tdCenter" hidden="true"/> 
<%-- 		<emp:select id="class_rst" label="分类认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/> --%>
		<emp:select id="mgr_mgr_class_rst" label="客户经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="subHead_class_rst" label="客户经理主管认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter" />
		<emp:select id="branchHead_class_rst" label="风险管理部审查员认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="headAcc_class_rst" label="风险管理部总经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="finally_class_rst" label="已完成认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>

		<emp:text id="input_id_displayname" label="客户经理" cssTDClass="tdCenter"/>
		<emp:text id="input_br_id_displayname" label="所属机构" cssTDClass="tdCenter"/>
<%-- 		<emp:date id="class_date" label="分类日期" /> --%>
	</emp:table>
	</panel>
	<div style="margin-top:50px;"></div> 
	
		<div class='emp_gridlayout_title'>无疑义的风险分类列表</div> 
	<!-- 工具栏 -->
	<div id="toolBar1" style="height:auto">
		<div style="margin-bottom: 5px" align="left">
			<!-- 按钮 -->
			<emp:button label="EXCEL导出" id="exportExcelTwo"   op="excelExport" ></emp:button>
			<emp:button label="EXCEL导入" id="ImportExcelDataTwo"   op="excelImport" ></emp:button>
			<emp:button label="认定" id="verifyRecordTow"   op="verify"  ></emp:button>
			<emp:button label="退回" id="fallBackTwo"   op="fallBack"  ></emp:button>
		</div>
	</div>

	<emp:table  icollName="RscTaskInfoTowList" pageMode="true"  url="getRscTaskInfoDataTow.do"   reqParams="RscTaskInfo.doubt=not&RscTaskInfo.bch_cde=${context.organNo}&RscTaskInfo.usr_cde=${context.currentUserId}"  >
 		<emp:text id="serno" label="主键" hidden="true"/>
 		<emp:link id="bill_no" label="借据编号" operation="viewRecordTow" />
 		<emp:text id="cus_id" label="客户编号"/>
 		<emp:text id="cus_id_displayname"   label="客户名称" cssTDClass="tdCenter"/>
		<emp:select id="cur_type" label="币种 " dictname="STD_ZX_CUR_TYPE" cssTDClass="tdCenter"/>
 		<emp:text id="loan_amt" label="贷款金额" dataType='Currency' cssTDClass="tdRight"/>
 		<emp:text id="loan_balance" label="贷款余额" dataType='Currency' cssTDClass="tdRight"/>
		<emp:select id="pre_class_rst"   label="上期分类结果 "  dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_eval_mac" label="模型分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:text id="model_class_rea" label="机评理由 "   cssTDClass="tdCenter" hidden="true"/> 
<%-- 		<emp:select id="class_rst" label="分类认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/> --%>
		<emp:select id="mgr_mgr_class_rst" label="客户经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="subHead_class_rst" label="客户经理主管认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter" />
		<emp:select id="branchHead_class_rst" label="风险管理部审查员认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="headAcc_class_rst" label="风险管理部总经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="finally_class_rst" label="已完成认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<%-- <emp:select id="headApp_class_rst" label="总行审批岗认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/> --%>
		<emp:text id="input_id_displayname" label="客户经理" cssTDClass="tdCenter"/>
		<emp:text id="input_br_id_displayname" label="所属机构" cssTDClass="tdCenter"/>
		<emp:text id="risk_cls_status" label="风险分类状态 "  hidden="true" cssTDClass="tdCenter"/>
	</emp:table>
	</panel>
		<div style="margin-top:50px;"></div> 
		
			<div class='emp_gridlayout_title'>异常风险分类列表</div>  
	<div id="toolBarThr" style="height:auto">
		<div style="margin-bottom: 5px" align="left">
			<!-- 按钮 -->
			<emp:button label="EXCEL导出" id="exportExcelThr"  op="excelExport"  ></emp:button>
			<emp:button label="EXCEL导入" id="ImportExcelDataThr"  op="excelImport"   ></emp:button>
			<emp:button label="认定" id="verifyRecordThr"  op="verify"  ></emp:button>
			<emp:button label="退回" id="fallBackThr"   op="fallBack"  ></emp:button>
		</div>
	</div>

	<!-- 列表信息 -->
	<emp:table icollName="RscTaskInfoThrList" pageMode="true"  url="getRscTaskInfoAnomalyClass.do"    reqParams="RscTaskInfo.doubt=yes&RscTaskInfo.bch_cde=${context.organNo}&RscTaskInfo.usr_cde=${context.currentUserId}">
 		<emp:text id="serno" label="主键" hidden="true"/>
 		<emp:link id="bill_no" label="借据编号" operation="viewRecordThr" />
 		<emp:text id="cus_id" label="客户编号"/>
 		<emp:text id="cus_id_displayname"   label="客户名称" cssTDClass="tdCenter"/>
		<emp:select id="cur_type" label="币种 " dictname="STD_ZX_CUR_TYPE" cssTDClass="tdCenter"/>
 		<emp:text id="loan_amt" label="贷款金额" dataType='Currency' cssTDClass="tdRight" />
 		<emp:text id="loan_balance" label="贷款余额" dataType='Currency' cssTDClass="tdRight"/>
		<emp:select id="pre_class_rst"   label="上期分类结果 "  dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_eval_mac" label="模型分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:text id="model_class_rea" label="机评理由"   cssTDClass="tdCenter" hidden="true"/> 
<emp:select id="mgr_mgr_class_rst" label="客户经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="subHead_class_rst" label="客户经理主管认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter" />
		<emp:select id="branchHead_class_rst" label="风险管理部审查员认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="headAcc_class_rst" label="风险管理部总经理认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		<emp:select id="finally_class_rst" label="已完成认定结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
		
	 
		<emp:text id="input_id_displayname" label="客户经理" cssTDClass="tdCenter"/>
		<emp:text id="input_br_id_displayname" label="所属机构" cssTDClass="tdCenter"/>
	</emp:table>
	</panel>
	<script type="scripts/jquery/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">
	var reset = "";
	function changeDutyOpts(elementObj,removeStr ){
		  var options = elementObj._obj.element.options;
		  for ( var i = options.length - 1; i >= 0; i--) {
			    if(options[i].value!=''){//保留 '请选择'
				    var optI=options[i].value;
					if(removeStr.indexOf(optI)!=-1){			
					    if(optI=="D0103"){
					    	var option1 = new Option('已完成','Q0000');
					    	options.add(option1);
					    }
					}else{
						 options.remove(i);
					}
			    }		
			} 
		} 

	function doOnLoad(){
		//处理岗位下拉框
	//	document.getElementById("base_tab").href="javascript:reLoad();";
		/**过滤掉"客户条线"中不需要的数据     2014-08-04  邓亚辉*/
	//	removeOpts(IqpAssetProApp.belg_line,'BL_ALL','');
	    changeDutyOpts(RscTaskInfo.dutyno,'${context.dutyNoList}');
	
	}

	//高级查询--为了Excel导入后界面的执行;-包括有无疑义列表及不良贷款金额重新赋值
	function doQuery(){
		var arry = "";
		var arryTwo = "";
		var dutyNo = RscTaskInfo.dutyno._getValue();
	 
		if(dutyNo=="Q0000"){ 
			document.getElementById('button_fallBack').style.display = 'none';
			document.getElementById('button_fallBackTwo').style.display = 'none';
			document.getElementById('button_fallBackThr').style.display = 'none';
			document.getElementById('button_verifyRecord').style.display = 'none';
			document.getElementById('button_verifyRecordTow').style.display = 'none';
			document.getElementById('button_verifyRecordThr').style.display = 'none';
		}else{
			document.getElementById('button_fallBack').style.display = 'inline';
			document.getElementById('button_fallBackTwo').style.display = 'inline';
			document.getElementById('button_fallBackThr').style.display =  'inline';
			document.getElementById('button_verifyRecord').style.display = 'inline';
			document.getElementById('button_verifyRecordTow').style.display = 'inline';
			document.getElementById('button_verifyRecordThr').style.display = 'inline';
		}
		if(dutyNo!=null&&dutyNo!=""){
			 $.ajax({    
				type: "POST", 
				dataType: "html",
				url: "<emp:url action='calSumOfAmt.do'/>", 
				data:  'doubt=yes'+'&duty=${param.duty}'+'&dutyno='+dutyNo+"&bch_cde=${context.organNo}&usr_cde=${context.currentUserId}", 
				success: function(data) { 
					try {
						var jsonstr = eval("("+data+")");
					} catch(e) {
						alert(data);
						return;
					}
					Loan.loanAmt._setValue(jsonstr.loanAmt);
					Loan.loanBalance._setValue(jsonstr.loanBalance);
				} 
			}); 
			 var form = document.getElementById('queryForm');
				RscTaskInfo._toForm(form);
				RscTaskInfoList._obj.ajaxQuery(null,form);
				RscTaskInfoTowList._obj.ajaxQuery(null,form);
				RscTaskInfoThrList._obj.ajaxQuery(null,form);
		}else{
			Loan.loanAmt._setValue("");
			Loan.loanBalance._setValue("");
			alert("请先选择审查岗位");
			return;
			 
		}
		 
		
		
		
	}
	
	function doReset(){
		page.dataGroups.RscTaskInfoGroup.reset();
	};
	
	//新增操作
	function doAddRecord(){
		var url="<emp:url action='getRscTaskInfoAddPage.do'/>?op=add";
		url = EMPTools.encodeURI(url);
		var ob={title:'新增风险分类认定',url:url,width:1000,height:500,draggable : true,modal : true,maximized : true};
		window.location = url;
	}
	/* 赋默认值操作 */
	function doAssignmentRecord(){
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var duty = '${param.duty}'; 
		var risk_cls_status = "";
		if(dutyNo=='S0404'){//客户经理
			risk_cls_status = "02";
		}else if(dutyNo=='D0102'){//客户经理主管
			risk_cls_status = "03";
		}else if(dutyNo=='D0103'){//风险管理部审核员
			risk_cls_status = "04";
		}else if(dutyNo=='Q0000'){//风险管理部总经理
			risk_cls_status = "05";
		}else if(dutyNo==null&&dutyNo==""){
		    alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
		    return;
		}
		var serno = '';
		$.ajax({ 
			type: "POST", 
			dataType: "html",
			url: "<emp:url action='updateRscTaskInfoData4kcoll.do'/>", 
			data:  'RscTaskInfo.serno='+serno+'&duty='+duty +'&type=assignment'+'&dutyno='+dutyNo+'&risk_cls_status='+risk_cls_status, 
			success: function(data) { 
				try {
					var jsonstr = eval("("+data+")");
				} catch(e) {
					alert(data);
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

	//编辑操作(认定操作)
	function doVerifyRecord(){ 
		var paramStr = RscTaskInfoList._obj.getParamValue(['serno']);
		var duty = '${param.duty}';
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if (paramStr != null) {
             var serno = RscTaskInfoList._obj.getSelectedData()[0].serno._getValue();
             var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=update&"+ 'serno='+serno +'&duty=' +duty+'&dutyno='+dutyNo;
 			url = EMPTools.encodeURI(url);
 			window.location = url;
		}else{
			alert('请先选择一条记录！');
			//$.messager.alert('错误','请选择一条记录！','error');
		}
	
	}


	//编辑操作(认定操作)
	function doVerifyRecordTow(){ 
	 
		var paramStr = RscTaskInfoTowList._obj.getParamValue(['serno']);
		var duty = '${param.duty}';
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if (paramStr != null) {
             var serno = RscTaskInfoTowList._obj.getSelectedData()[0].serno._getValue();
             var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=update&"+ 'serno='+serno +'&duty=' +duty+'&dutyno='+dutyNo;
 			url = EMPTools.encodeURI(url);
 			window.location = url;
		}else{
			alert('请先选择一条记录！');
			//$.messager.alert('错误','请选择一条记录！','error');
		}
	}

	//编辑操作(认定操作)
	function doVerifyRecordThr(){ 
		var paramStr = RscTaskInfoThrList._obj.getParamValue(['serno']);
		var duty = '${param.duty}';
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if (paramStr != null) {
             var serno = RscTaskInfoThrList._obj.getSelectedData()[0].serno._getValue();
             var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=update&"+ 'serno='+serno +'&duty=' +duty+'&dutyno='+dutyNo;
 			url = EMPTools.encodeURI(url);
 			window.location = url;
		}else{
			alert('请先选择一条记录！');
			//$.messager.alert('错误','请选择一条记录！','error');
		}
	}



	//编辑操作(回退操作)
	function doFallBack(){ 
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var risk_cls_status = "";
		if(dutyNo=='S0404'){//客户经理
			risk_cls_status = "02";
		}else if(dutyNo=='D0102'){//客户经理主管
			risk_cls_status = "03";
		}else if(dutyNo=='D0103'){//风险管理部审核员
			risk_cls_status = "04";
		}else if(dutyNo=='Q0000'){//风险管理部总经理
			risk_cls_status = "05";
		}else if(dutyNo==null&&dutyNo==""){
		    alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
		    return;
		}
		var duty = '${param.duty}';
		var selObj = RscTaskInfoList._obj.getSelectedData()[0];
		if(selObj!=null){
			var cus_id=selObj.cus_id._getValue();
			rollBack(duty,dutyNo,risk_cls_status, cus_id);
		}else{
			alert("请先选择一条记录！");
		}
	}


	//编辑操作(回退操作)
	function doFallBackTwo(){ 
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var risk_cls_status = "";
		if(dutyNo=='S0404'){//客户经理
			risk_cls_status = "02";
		}else if(dutyNo=='D0102'){//客户经理主管
			risk_cls_status = "03";
		}else if(dutyNo=='D0103'){//风险管理部审核员fallBackTwo
			risk_cls_status = "04";
		}else if(dutyNo=='Q0000'){//风险管理部总经理
			risk_cls_status = "05";
		}else if(dutyNo==null&&dutyNo==""){
		    alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
		    return;
		}
		var duty = '${param.duty}';
		var selObj = RscTaskInfoTowList._obj.getSelectedData()[0];
		if(selObj!=null){
			var cus_id=selObj.cus_id._getValue();
			rollBack(duty,dutyNo,risk_cls_status, cus_id);
		}else{
			alert("请先选择一条记录！");
		}
	}

	//编辑操作(回退操作)
	function doFallBackThr(){ 
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var risk_cls_status = "";
		if(dutyNo=='S0404'){//客户经理
			risk_cls_status = "02";
		}else if(dutyNo=='D0102'){//客户经理主管
			risk_cls_status = "03";
		}else if(dutyNo=='D0103'){//风险管理部审核员
			risk_cls_status = "04";
		}else if(dutyNo=='Q0000'){//风险管理部总经理
			risk_cls_status = "05";
		}else if(dutyNo==null&&dutyNo==""){
		    alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
		    return;
		}
		 
		var duty = '${param.duty}';
		var selObj = RscTaskInfoThrList._obj.getSelectedData()[0];
		
		if(selObj!=null){
			var cus_id=selObj.cus_id._getValue();
			rollBack(duty,dutyNo,risk_cls_status, cus_id);
		}else{
			alert("请先选择一条记录！");
		}
		
		
	}

	/*回退  */
	function rollBack(duty,dutyNo,risk_cls_status,cus_id){
		//alert(duty+"=="+dutyNo+"=="+risk_cls_status+"=="+cus_id);
		var serno = '';
		$.ajax({ 
			type: "POST", 
			dataType: "html",
			url: "<emp:url action='updateRscTaskInfoData4kcoll.do'/>", 
			data:  'RscTaskInfo.serno='+serno+'&duty='+duty +'&type=fallBack'+'&dutyno='+dutyNo+'&risk_cls_status='+risk_cls_status+'&cus_id='+cus_id, 
			success: function(data) { 
				try {
					var jsonstr = eval("("+data+")");
				} catch(e) {
					alert(data);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
					alert("回退成功!");
					// $.messager.alert('提示',"回退成功!",'info');
					doQuery();
				}else if(flag=="out"){
					alert("未完成");//未完成
				}else{
					alert("回退失败!");
					// $.messager.alert('错误',"回退失败!",'error');
				}
				 
			} 
		});
	}
	

	//查看操作
	function doViewRecord(){ 
		var selObj = RscTaskInfoList._obj.getSelectedData()[0];
		var serno=selObj.serno._getValue();
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=view&"+ 'serno='+serno+'&dutyNo='+dutyNo;
		url = EMPTools.encodeURI(url);
		window.location = url;
		 
	}

	function doViewRecordTow(){ 
		var selObj = RscTaskInfoTowList._obj.getSelectedData()[0];
		var serno=selObj.serno._getValue();
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=view&"+ 'serno='+serno+'&dutyNo='+dutyNo;
		url = EMPTools.encodeURI(url);
		window.location = url;
		 
	}


	function doViewRecordThr(){ 
		var selObj = RscTaskInfoThrList._obj.getSelectedData()[0];
		var serno=selObj.serno._getValue();
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=view&"+ 'serno='+serno+'&dutyNo='+dutyNo;
		url = EMPTools.encodeURI(url);
		window.location = url;
		 
	}

	//导出excel
	function doExportExcel(){
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if(dutyNo==""||dutyNo==null){
			alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
			return;     
		}
		/* alert('${param.duty}'); */
		var url = '<emp:url action="exportRscTaskInfoExcelData.do"/>?RscTaskInfo.duty=${param.duty}&RscTaskInfo.doubt=yes&RscTaskInfo.dutyno='+dutyNo
				+"&RscTaskInfo.bch_cde=${context.organNo}&RscTaskInfo.usr_cde=${context.currentUserId}"+"&bs=export";
		url = EMPTools.encodeURI(url);
		window.open(url);
	}

	//导出excel
	function doExportExcelTwo(){
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if(dutyNo==""||dutyNo==null){
			alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
			return;     
		}
		/* alert('${param.duty}'); */
		var url = '<emp:url action="exportRscTaskInfoExcelData.do"/>?RscTaskInfo.duty=${param.duty}&RscTaskInfo.doubt=not&RscTaskInfo.dutyno='+dutyNo
				+"&RscTaskInfo.bch_cde=${context.organNo}&RscTaskInfo.usr_cde=${context.currentUserId}"+"&bs=export";
		url = EMPTools.encodeURI(url);
		window.open(url);
	}

	//导出excel
	function doExportExcelThr(){
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if(dutyNo==""||dutyNo==null){
			alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
			return;     
		}
		/* alert('${param.duty}'); */
		var url = '<emp:url action="exportRscTaskInfoAnomalyClassExcelData.do"/>?RscTaskInfo.duty=${param.duty}&doubt=not&RscTaskInfo.dutyno='+dutyNo
				+"&RscTaskInfo.doubt=yes&RscTaskInfo.bch_cde=${context.organNo}&RscTaskInfo.usr_cde=${context.currentUserId}"+"&bs=export";
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
		var dutyNo = RscTaskInfo.dutyno._getValue();
		if(dutyNo==null||dutyNo==""){
			alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
			return;     
		}
		var url = "<emp:url action='showExcelImportPage.do'/>?content="+dutyNo+",One";
		url = EMPTools.encodeURI(url);
		window.open(url);
	}

	//导入Excel数据--afterUrl为后续excel文件上传后要执行的方法，必填
	function doImportExcelDataTwo(){
		var dutyNo =  RscTaskInfo.dutyno._getValue();
		if(dutyNo==null&&dutyNo==""){
			alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
			return;     
		}
		var url = "<emp:url action='showExcelImportPage.do'/>?content="+dutyNo+",Two";
		url = EMPTools.encodeURI(url);
		window.open(url);
	}
	
	function doImportExcelDataThr(){

		var dutyno =  RscTaskInfo.dutyno._getValue();
		if(dutyno==""||dutyno==null){
			alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
			return; 
		}
		var url = "<emp:url action='showExcelImportPage.do'/>?content="+dutyNo+",Thr";
		url = EMPTools.encodeURI(url);
		window.open(url);
		
    }
	
	
	//导入excle数据时回调函数
	function returnMethod(fs, returnResponses){
		var flag = returnResponses[0].status;
		var message = returnResponses[0].message;
		var param = returnResponses[0].params;
		//alert(flag+'=='+message+'=='+param);
		if(flag == "success"){
			 
				window.location.reload(); 
		} else {
			var params = new Array();
			params[0] = param;
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
		var duty = "${param.duty}";
		var serno = "";
		var dutyNo = RscTaskInfo.dutyno._getValue();
		var risk_cls_status = "";
 

		if(dutyNo=='S0404'){//客户经理
			risk_cls_status = "02";
		}else if(dutyNo=='D0102'){//客户经理主管
			risk_cls_status = "03";
		}else if(dutyNo=='D0103'){//风险管理部审核员
			risk_cls_status = "04";
		}else if(dutyNo=='Q0000'){//风险管理部总经理
			risk_cls_status = "05";
		}else if(dutyNo==null&&dutyNo==""){
		    alert("请先选择审查岗,并点击查询操作!");//请先选择审查岗,并点击查询操作!
		    return;
		}
		
		$.ajax({ 
			type: "POST", 
			dataType: "html",
			url: "<emp:url action='updateRscTaskInfoData4kcoll.do'/>", 
			data:  'RscTaskInfo.serno='+serno+'&duty='+duty +'&type=submit'+'&dutyno='+dutyNo+'&risk_cls_status='+risk_cls_status, 
			success: function(data) { 
				try {
					var jsonstr = eval("("+data+")");
				} catch(e) {
					EMP.alertException(data);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
					alert("提交成功!");
					// $.messager.alert('提示',"提交成功!",'info');
					doQuery();
				}else if(flag=="out"){
					alert("删除失败");//请先做认定操作!
					// $.messager.alert('错误',"删除失败",'error');
				}else{
					alert("提交失败!");//提交失败!
				}
				 
			} 
		});
	}



	</script>
	</body>
	</html>
</emp:page>