 <%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	request.setAttribute("canwrite", "");
%>
<style type="text/css">
.emp_field_text_input2 {
border:1px solid #CEC7BD;
background-color:#eee;
text-align:left;
width:100px;
}
</style>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function setCheckRate(){
		var check_type = IqpChkStoreTaskRecord.check_type._getValue();
		if(check_type=='2'){
			IqpChkStoreTaskRecord.check_rate._obj._renderReadonly(false);
		}else{
			IqpChkStoreTaskRecord.check_rate._obj._renderReadonly(true);
			IqpChkStoreTaskRecord.check_rate._setValue('');
		}
	}

	function doPrint(){
		var task_id = IqpChkStoreTaskRecord.task_id._getValue();
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=iqp/iqpchkstoretask.raq&task_id='+task_id;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}

	function getStoreId(data){
		IqpChkStoreTaskRecord.store_id._setValue(data.store_id._getValue());
    	IqpChkStoreTaskRecord.store_id_displayname._setValue(data.store_name._getValue());
    };

    function doSave(){
    	var form = document.getElementById("submitForm");
    	if(IqpChkStoreTaskRecord._checkAll()){
    		IqpChkStoreTaskRecord._toForm(form);
    		var handleSuccess = function(o){
    			if(o.responseText !== undefined) {
    				try {
    					var jsonstr = eval("("+o.responseText+")");
    				} catch(e) {
    					alert("Parse jsonstr1 define error!" + e.message);
    					return;
    				}
    				var flag = jsonstr.flag;
    				if(flag == "success"){
    					alert("保存成功!");
    					
    				}else {
    					alert("保存失败!");
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
    		var postData = YAHOO.util.Connect.setForm(form);	
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
    	}else {
    		return;
    	}
    };

    function doLoad(){
    	countQntValue();
    }

    function countQntValue(){
    	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var qnt = jsonstr.qnt;
				var value = jsonstr.value;
				if(flag == "success"){
					document.getElementById("qnt").value = qnt;
					document.getElementById("value").value = value;
				}else{
					document.getElementById("qnt").value = qnt;
					document.getElementById("value").value = value;
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
		var task_id = IqpChkStoreTaskRecord.task_id._getValue();
		var url = '<emp:url action="queryQntValue4TaskId.do"/>?task_id='+task_id;
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
    }
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="updateIqpChkStoreTaskRecordRecord.do" method="POST">
		<table width="100%" align="center" border="1" class="searchTb">
		    <tr height="21">
				<td colspan="7" style="border: none; background-color: #99CCFF"><b>基本信息</b></td>
			</tr>
			<tr height="21">
				<td width="20%" align="center">仓库名称</td>
				<td colspan="2"><emp:pop id="IqpChkStoreTaskRecord.store_id_displayname" label="仓库名称" url="queryIqpOverseeUnderstorePopForChkList.do?oversee_org_id=${context.IqpChkStoreTaskRecord.oversee_org_id}" returnMethod="getStoreId" required="false" buttonLabel="选择" /></td>
				<td width="15%" align="center">监管方</td>
				<td><emp:text id="IqpChkStoreTaskRecord.oversee_org_id_displayname" label="监管机构" required="false" readonly="true"/></td>
				<td width="15%" align="center">操作方式</td>
				<td><emp:text id="IqpChkStoreTaskRecord.opr_mode" label="操作方式" maxlength="32" required="false" /></td>
				<td></td>
			</tr>
			<tr height="21">
				<td align="center">企业名称</td>
				<td colspan="2"><emp:text id="IqpChkStoreTaskRecord.cus_id_displayname" label="客户名称" required="false" readonly="true" /></td>
				<td align="center">授信敞口</td>
				<td><emp:text id="IqpChkStoreTaskRecord.lmt_open_amt" label="授信敞口" maxlength="16" required="false" /></td>
				<td align="center">核定抵/质押率(%)</td>
				<td><emp:text id="IqpChkStoreTaskRecord.pldimn_rate" label="核定抵/质押率(%)" maxlength="16" required="false" dataType="Percent"/></td>
				<td></td>
			</tr>
			<tr height="21">
				<td colspan="7">
					<div class="emp_gridLayout_title">核/巡库押品记录</div>
					<emp:table icollName="IqpChkStoreGageRecordList" pageMode="false" url="">
						<emp:text id="cargo_name" label="货物名称" />
						<emp:text id="model" label="型号" />
						<emp:text id="produce_vender" label="生产厂家" />
						<emp:text id="unit_price" label="单价(元)" />
						<emp:text id="qnt" label="数(重)量" />
						<emp:text id="value" label="价值(元)" />
					</emp:table>
				</td>
			</tr>
			<tr height="21">
			    <td>当前查库数量与价值汇总</td>
			    <td colspan="2">数(重)量:<input name="qnt" value="" type="text" class="input_50" maxlength="100" style="width: 150" disabled="disabled"/></td>
			    <td colspan="2">价值(元):<input name="value" value="" type="text" class="input_50" maxlength="100" style="width: 150" disabled="disabled"/></td>
			    <td>应控货数量或最低价值（动态模式下）</td>
			    <td ><emp:text id="IqpChkStoreTaskRecord.low_value" label="应控货数量或最低价值（动态模式下）" maxlength="16" required="false" /></td>
			</tr>
			<tr height="21" title="注：巡库员必须凭现场核查进行评价，如作出较差或否定评价则应在后面注明实际情况">
				<td colspan="7" style="border: none; background-color: #99CCFF"><b>现场巡查记录</b></td>
			</tr>
			<tr height="21">
				<td align="center">监管方内部控制</td>
             	<td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.oversee_inner_ctrl" label="监管方内部控制" dictname="STD_OVERSEE_INNER_CTRL" required="false"/></td>
             	<td></td>
			</tr>
			<tr height="21">
			   <td rowspan="3" align="center">监管方人员素质</td>
               <td >管理人员综合素质</td>
               <td colspan="5"><emp:radio id="IqpChkStoreTaskRecord.manager_guality" label="管理人员综合素质"  required="false" dictname="STD_MANAGER_GUALITY" layout="1"/></td>
               <td></td>         
			</tr>
			<tr height="21">
               <td> 操作人员行业经验</td>
               <td colspan="5"><emp:radio id="IqpChkStoreTaskRecord.operator_exp" label="操作人员行业经验" required="false" dictname="STD_OPERATOR_EXP" layout="1"/></td>
               <td></td>
			</tr>
			<tr height="21">
               <td>监管方对我行巡库配合</td>
               <td colspan="5"><emp:radio id="IqpChkStoreTaskRecord.oversee_org_coop" label="监管方对我行巡库配合" required="false" dictname="STD_OVERSEE_ORG_COOP" layout="1"/></td>
               <td></td>
			</tr>
			<tr height="21">
			  <td align="center">仓储条件</td>
              <td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.store_cond" label="仓储条件" required="false" dictname="STD_STORE_COND" /></td>
              <td></td>  
			</tr>
			<tr height="21">
			  <td align="center">仓位和标识</td>
              <td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.storage_flag" label="仓位和标识" required="false" dictname="STD_STORAGE_FLAG" /></td>
              <td></td>           
			</tr>
			<tr height="21">
			  <td align="center">货物权属合法性</td>
              <td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.goods_auth" label="货物权属合法性" required="false" dictname="STD_GOODS_AUTH" /></td>
              <td></td>           
			</tr>
			<tr height="21">
			  <td align="center">货物品质鉴定</td>
              <td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.goods_qual" label="货物品质鉴定" required="false" dictname="STD_GOODS_QUAL" /></td>
              <td></td>           
			</tr>
			<tr height="21">
			  <td align="center">出入库操作流程</td>
              <td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.iostore_opr_flow" label="出入库操作流程" required="false" dictname="STD_IOSTORE_OPR_FLOW" /></td>
              <td></td>         
			</tr>
			<tr height="21">
			    <td rowspan="2" align="center">抵/质押货物数量/金额相符性</td>
                <td>检查方式</td>
                <td colspan="2"><emp:radio id="IqpChkStoreTaskRecord.check_type" label="检查方式" required="false" dictname="STD_CHECK_TYPE" onclick="setCheckRate()" layout="1"/></td>
				<td colspan="3">抽查比例:<emp:text id="IqpChkStoreTaskRecord.check_rate" label="抽查比例" required="false" cssElementClass="emp_field_text_input2" dataType="Percent" readonly="true"/></td>
				<td></td>
			</tr>
			<tr height="21">
				<td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.accord_attr" label="抵/质押货物数量/金额相符性" required="false" dictname="STD_ACCORD_ATTR"/></td>
				<td></td>
			</tr>
			<tr>
			  <td align="center">异常情况处理</td>
              <td colspan="6"><emp:checkbox id="IqpChkStoreTaskRecord.exception_disp" label="异常情况处理" required="false" dictname="STD_EXCEPTION_DISP" /></td>
              <td></td>    
			</tr>
			<tr height="21">
				<td align="center">巡库员补充核查说明<br/>（重点对异常情况进行说明）</td>
				<td colspan="6"><emp:textarea id="IqpChkStoreTaskRecord.memo" label="巡库员补充核查说明" maxlength="1000" required="false"/></td>
				<td></td>
			</tr>
			<tr height="21">
				<td align="center">处理意见</td>
				<td colspan="6"><emp:textarea id="IqpChkStoreTaskRecord.disp_advice" label="处理意见" maxlength="1000" required="false" /></td>
				<td></td>
			</tr>
		</table>
		<emp:text id="IqpChkStoreTaskRecord.task_id" label="任务执行编号" maxlength="32" required="true" readonly="true" hidden="true"/>
		<emp:text id="IqpChkStoreTaskRecord.store_id" label="仓库编号" maxlength="32" required="false" hidden="true"/>
		<emp:text id="IqpChkStoreTaskRecord.oversee_org_id" label="监管机构编号" maxlength="32" required="false" hidden="true"/>
		<emp:text id="IqpChkStoreTaskRecord.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
		<div align="center">
			<br>
			<emp:button id="print" label="打印" op="update"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
