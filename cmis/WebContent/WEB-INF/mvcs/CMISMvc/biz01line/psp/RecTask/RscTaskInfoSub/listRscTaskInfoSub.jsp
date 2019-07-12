<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head> 
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
	<style type="text/css"></style>
</head>
<body class="page_content" >
	<!-- 高级搜索区 -->

	
	<form  method="POST" action="#" id="queryForm">
	</form>
			<emp:gridLayout id="RscTaskInfoSubGroup" title="输入查询条件" maxColumn="3">
 				<emp:text id="RscTaskInfoSub.pk_id" label="主键" maxLength="40" />
 				<emp:text id="RscTaskInfoSub.serno" label="风险分类认定编号" maxLength="40" />
				<emp:select id="RscTaskInfoSub.class_adjust_rst" label="分类调整结果  STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT" />
			</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<!-- 工具栏 -->
	<div id="toolBar" style="height:auto">
			<div style="margin-bottom: 5px" align="left">
			
			<!-- 按钮 -->
			<emp:button label="新增" id="addRecord"  op="add" ></emp:button>
			<emp:button label="删除" id="deleteRecord"   op="remove" ></emp:button>
			<emp:button label="修改" id="updateRecord"  op="update" ></emp:button>
			<emp:button label="查看" id="viewRecord"   op="view" ></emp:button>
			<emp:button label="excel导出" id="exportExcel"  op="view" ></emp:button>
			<emp:button label="excel模版导出" id="exportExcelTemplet" op="view" ></emp:button>
			<emp:button label="excel数据导入" id="importExcelData"   op="view" ></emp:button>
		</div>
	</div>
	

				<div class='emp_gridlayout_title'>风险分类认定从表列表</div>
	<!-- 列表信息 -->
	<emp:table icollName="RscTaskInfoSubList" url="getRscTaskInfoSubData.do"    >
 		<emp:text id="pk_id" label="主键"/>
 		<emp:text id="serno" label="风险分类认定编号"/>
 		<emp:text id="identy_duty" label="认定岗位"/>
		<emp:select id="class_adjust_rst" label="分类调整结果  STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT"/>
 		<emp:text id="remark" label="调整理由"/>
		<emp:date id="class_date" label="分类日期" />
	</emp:table>
	<script type="text/javascript">
	/** 打开高级查询窗口 */
	function doOpenQueryDlg() {
		$('#searchPanel').panel('open');
		$('.searchbox').css('display', 'none');
		$('#queryDlgDiv').css('display', 'none');
		$('#quickQueryDlgDiv').css('display', 'inline-block');
	}

	/** 打开快捷查询工具栏 */
	function doQuickQueryDlg() {
		$('#searchPanel').panel('close');
		$('.searchbox').css('display', 'inline-block');
		$('#quickQueryDlgDiv').css('display', 'none');
		$('#queryDlgDiv').css('display', 'inline-block');
	}
	
	//查询
	function doSearch(value,name){
		//构造查询参数
		var params=EMP.searchParams('RscTaskInfoSubQueryMenu',name,value);
		params["searchType"] = "quickquery";
		$('#RscTaskInfoSub').datagrid('load',params);
	} 
	
	//高级查询
	function doQuery(){
		var arry=$('#queryForm').toJsonData();	
		$('#RscTaskInfoSub').datagrid('load',arry);
	}

	function doQuery(){
		var form = document.getElementById('queryForm');
		RscTaskInfoSubGroup._toForm(form);
		RscTaskInfoSubList._obj.ajaxQuery(null,form); 
	};
	
	/** 高级查询--重置 */
	function doReset() {
		$('#queryForm').form('clear');
	}
	
	//新增操作
	function doAddRecord(){
		var url="<emp:url action='getRscTaskInfoSubAddPage.do'/>?op=add";
		url = EMPTools.encodeURI(url);
		var ob={title:'新增风险分类认定从表',url:url,width:1000,height:500,draggable : true,modal : true,maximized : true};
		EMP.createwin(ob);
	}

	//修改操作
	function doUpdateRecord(){ 
		var row = $('#RscTaskInfoSub').datagrid('getSelected');
		if(row){
			var url="<emp:url action='getRscTaskInfoSubUpdatePage.do'/>?op=update&"+ 'pk_id='+row.pk_id
;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert("请先选择一条记录！");
			//$.messager.alert('错误','请选择一条记录！','error');
		}
	}

	//查看操作
	function doViewRecord(){ 
		var row = $('#RscTaskInfoSub').datagrid('getSelected');
		if(row){
			var url="<emp:url action='getRscTaskInfoSubDetailPage.do'/>?op=view&"+ 'pk_id='+row.pk_id
;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert("请先选择一条记录！");
			// $.messager.alert('错误','请选择一条记录！','error');
		}
	}
	//删除操作
	function doDeleteRecord(){
		var row = $('#RscTaskInfoSub').datagrid('getSelected');
		if(row){
			// $.messager.confirm('确认', '您确定需要删除该条记录吗？', function(r){ 
				if(confirm("您确定需要删除该条记录吗？")){ 
					$.ajax({ 
						type: "POST", 
						dataType: "html",
						url: "<emp:url action='deleteRscTaskInfoSubData.do'/>", 
						data:  'RscTaskInfoSub.pk_id='+row.pk_id
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
								alert("删除成功");
								// $.messager.alert('提示',"删除成功",'info');
								doSearch();
							}else{
								alert("删除失败");
								// $.messager.alert('错误',"",'error');
							}
							 
						} 
					});
				}
			}
		}else{
			alert("请先选择一条记录！");
			// $.messager.alert('错误','请选择一条记录！','error');
		}
	}
	
	//导出excel
	function doExportExcel(){
		var url = '<emp:url action="exportRscTaskInfoSubExcelData.do"/>';
		url = EMPTools.encodeURI(url);
		window.open(url);
	}
	
	//导出Excel模版
	function doExportExcelTemplet(){
		var url = '<emp:url action="exportRscTaskInfoSubExcelTemplet.do"/>';
		url = EMPTools.encodeURI(url);
		window.open(url);
	}

	//导入Excel数据--afterUrl为后续excel文件上传后要执行的方法，必填
	function doImportExcelData(){
		var url = "<emp:url action='showExcelImportPage.do'/>?module_id=12345&func_id=123411&data_id=3332221&afterUrl=importRscTaskInfoSubExcelData.do&allow-doc=xls&allow-size=10&allow-num=1&isSelection=false&isMultiple=false&isModule=false&isFunction=false&returnMethod=returnMethod";
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
			alert(message, null, function(r){
				window.parent.doQuery();
				window.parent.EMP.closewin();
			});
		} else {
			var params = new Array();
			params[0] = param;
			alert(message, params);
	  		// .messager;
		}
	}
	
	</script>
	</body>
	</html>
</emp:page>