<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

<html>
<head>
<title>支持多选机构POP页面</title>
<jsp:include page="/include.jsp" flush="true" />
<style type="text/css">
.emp_field_text_readonly {
	border: 1px solid #b7b7b7;
	background-color: white;
	text-align: left;
	width: 600px;
	height:100px;
};
.emp_field_label_xp {
	text-align: right;
	width: 25%;
};
</style>
<script type="text/javascript">
	var hidden_flag = false;
	
	/*** 多form下重置按钮有错，要重命名一个 ***/
	function doQueryQry() {
		var form = document.getElementById('queryForm');
		CusBase._toForm(form);
		resultSet._obj.ajaxQuery(null, form);
	};
	function doResetQry(){
		page.dataGroups.CusBaseGroup.reset();
	};
	/*--user code begin--*/
	function doAddList(){
		cus_ids = Return.cus_ids._getValue();
		if(cus_ids == ""){
			alert("请先选择客户");
		}else{
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var success_amount = jsonstr.success_amount;
					var fail_cusId = jsonstr.fail_cusId;
					var repeat_cusId = jsonstr.repeat_cusId;

					hidden_flag = true;
					result_str="";
					doResultList();
					if(flag == "success"){
						result_str = success_amount+"\n"+fail_cusId+"\n"+repeat_cusId;
					}else if(flag == "fail"){
						result_str = success_amount+"\n"+fail_cusId+"\n"+repeat_cusId;
					}else{
						result_str = success_amount+"\n"+fail_cusId+"\n"+repeat_cusId;
					}
					Return.result._setValue(result_str);
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};

			var form = document.getElementById("submitForm");
			Return._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}		
	};
	function doLoad(){
		serno = "${context.serno}";
		doResultList();
	};
	function doCloseList(){
		window.close();
		window.opener.location.reload();
	};
	function doResultList(){
		hidden_flag = !hidden_flag;
		Return.result._obj._renderHidden(hidden_flag);
	};
	function getOrgID(data){
		CusBase.main_br_id._setValue(data.organno._getValue());
		CusBase.main_br_id_displayname._setValue(data.organname._getValue());
	};
	function setconId(data){
		CusBase.cust_mgr_displayname._setValue(data.actorname._getValue());
		CusBase.cust_mgr._setValue(data.actorno._getValue());
	};

	/*** 选择等按钮begin ***/
	function doAddByExcel(){
		var url = '<emp:url action="importLmtIndusListByExcel.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow','height=300, width=600, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
	};
	function doAddMember(){
		doSelect();
	};
	function doSelectAllCusid(){
		resultSet._obj.selectAll()
		var paramStr = resultSet._obj.getSelectedData();
		resultSet._obj.clearAll()
		var select_cusId="",result_cusId,cus_id = new Array() ;
		for(var i=0;i<paramStr.length;i++){
			cus_id.push(paramStr[i].cus_id._getValue());
			result_cusId = addMembers(cus_id[i]);
			if( result_cusId!= ""){
				select_cusId = select_cusId+result_cusId+",";
			}
		}
		if(select_cusId != ""){
			alert("客户码:"+select_cusId+" 已经选择。");
		}
	};
	function doSelect() {
		var data = resultSet._obj.getSelectedData();
		var select_cusId;
		if (data != null && data.length !=0) {
			select_cusId = data[0].cus_id._getValue();
			select_cusId = addMembers(select_cusId);
			if(select_cusId != ""){
				alert("客户码:"+select_cusId+" 已经选择。");
			}
		}
	};
	function addMembers(cus_id){
		cus_ids = Return.cus_ids._getValue();
		if(cus_ids.indexOf(cus_id)!=-1){
			return cus_id;
		}else{
			cus_ids = cus_ids+cus_id+",";
			Return.cus_ids._setValue(cus_ids);
			return "";
		}
	};
	/*** 选择等按钮end ***/
	
	function getImportList(data,flag){
		Return.cus_ids._setValue(data);
		hidden_flag = true;
		Return.result._setValue("从Excel共计导入了"+flag+"个客户码。下一步请点击[新增到名单]按钮，完成保存！");
		doResultList();
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	
	<emp:gridLayout id="CusBaseGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusBase.cus_id" label="客户码" />
		<emp:text id="CusBase.cus_name" label="客户名称" />
		<emp:select id="CusBase.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="CusBase.cert_code" label="证件号码" />
		<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
		<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
		<emp:text id="CusBase.cust_mgr" label="主管客户经理" hidden="true"  />
		<emp:text id="CusBase.main_br_id" label="主管机构" hidden="true" />
	</emp:gridLayout>
		<table width="100%"  align="center"  class="searchTb">
		<tr>
			<td colspan="4">
			<div align="center">
				<emp:button id="queryQry" label="查询"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<emp:button id="resetQry" label="重置"/>
			</div>
			</td>
		</tr>
	</table>	
	</form>

	<emp:button label="选择" id="addMember"></emp:button>
	<emp:button label="全选" id="selectAllCusid"></emp:button>
	<emp:button label="Excel导入" id="addByExcel"></emp:button>
	<emp:table icollName="resultSet" pageMode="true" url="pageLmtIndusListPop.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cust_mgr" label="主管客户经理" hidden="true"/>
		<emp:text id="cust_mgr_displayname" label="主管客户经理" />
		<emp:text id="main_br_id" label="主管机构" hidden="true"/>
		<emp:text id="main_br_id_displayname" label="主管机构" />
		<emp:text id="belg_line" label="条线" hidden="false" dictname="STD_ZB_BUSILINE"/>
	</emp:table>

	<br>
	<emp:form id="submitForm" action="importLmtIndusListApplyRecords.do" method="POST">
		<emp:gridLayout id="ReturnGroup"  maxColumn="2">
			<emp:text id="Return.serno" label="业务编号" hidden="true"  defvalue="${context.serno}" />
			<emp:textarea id="Return.cus_ids" label="客户码（可手动粘贴多个客户码，以英文逗号隔开）" 
			cssLabelClass="emp_field_label_xp"  cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:textarea id="Return.result" label="处理结果信息"  colSpan="2" readonly="true"
			cssLabelClass="emp_field_label_xp"  cssElementClass="emp_field_text_readonly"/>
		</emp:gridLayout>

		<div align="center">
			<emp:button label="新增到名单" id="addList" ></emp:button>
			<emp:button label="控制结果框" id="resultList" ></emp:button>
			<emp:button label="关闭" id="closeList" ></emp:button>
		</div>
	</emp:form>
</body>
</html>
</emp:page>