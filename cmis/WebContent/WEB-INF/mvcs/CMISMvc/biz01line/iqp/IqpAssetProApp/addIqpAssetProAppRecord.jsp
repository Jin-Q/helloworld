<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
	function doOnLoad(){
		/**过滤掉"客户条线"中不需要的数据     2014-08-04  邓亚辉*/
		removeOpts(IqpAssetProApp.belg_line,'BL_ALL','');
	}
	
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetProApp._checkAll()){
			IqpAssetProApp._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
<!--					alert("hhhhh"+o.responseText);-->
<!--					alert("kkkk"+IqpAssetProApp.belg_line._getValue());-->
<!--					alert("kkkk"+IqpAssetProApp.end_date._getValue());-->
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					if(flag == "success"){
						alert("新增成功!");
						var url = '<emp:url action="getIqpAssetProAppUpdatePage.do"/>?op=update&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("新增异常!"); 
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
			return false;
		}
	};

	function getOrgID(data){
		IqpAssetProApp.manager_br_id._setValue(data.organno._getValue());
		IqpAssetProApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function getProOrgID(data){
		IqpAssetProApp.pro_org._setValue(data.organno._getValue());
		IqpAssetProApp.pro_org_name._setValue(data.organname._getValue());
	};

	function doReturn() {
		history.go(-1);
	};
	/*--user code end--*/
	/**将"比数"放在"币种"之前   新增"贷款余额"字段    新增"客户条线"、"到期日期"字段   2014-08-04 邓亚辉*/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addIqpAssetProAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetProAppGroup" title="资产证券化项目申请" maxColumn="2">
			<emp:text id="IqpAssetProApp.prd_id" label="产品编码" maxlength="6" required="false" defvalue="600022" readonly="true"/>
			<emp:text id="IqpAssetProApp.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产证券化" readonly="true"/>
			<emp:text id="IqpAssetProApp.pro_name" label="项目名称" maxlength="80" required="true" />
			<emp:text id="IqpAssetProApp.pro_short_name" label="项目简称" maxlength="80" required="false" />
			<emp:select id="IqpAssetProApp.pro_type" label="项目类型" required="false" dictname="STD_ZB_ASSET_PRO_TYPE" defvalue="02" readonly="true"/>
			<emp:pop id="IqpAssetProApp.pro_org_name" label="资产所属机构" required="false" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getProOrgID" />
			<emp:text id="IqpAssetProApp.pro_qnt" label="笔数" maxlength="38" required="false" readonly="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAssetProApp.cur_type" label="币种" required="false" defvalue="CNY" readonly="true" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="IqpAssetProApp.pro_amt" label="项目金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<!--修改页面显示   2014-08-20  王青	 start-->
			<emp:date id="IqpAssetProApp.end_date" label="到期日期" required="false" dataType="Date" readonly="true"/>
			<emp:text id="IqpAssetProApp.pro_balance" label="贷款余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true"/>
			<emp:select id="IqpAssetProApp.belg_line" label="客户条线" required="false" dictname="STD_ZB_BUSILINE"  hidden="true"/>
			<emp:select id="IqpAssetProApp.is_rgt_res" label="是否有追索权" required="true" dictname="STD_ZX_YES_NO" />
			<!--修改页面显示   2014-08-20  王青	 end-->		
			<emp:textarea id="IqpAssetProApp.pro_short_memo" label="项目简介" maxlength="200" required="false" colSpan="2"/>
			
			<emp:text id="IqpAssetProApp.pro_org" label="资产所属机构" hidden="true"/>
			<emp:text id="IqpAssetProApp.pro_status" label="项目状态" maxlength="5" required="false" hidden="true"/>
			<emp:text id="IqpAssetProApp.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:pop id="IqpAssetProApp.manager_br_id_displayname" label="管理机构" required="true"  buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:select id="IqpAssetProApp.flow_type" label="流程类型"  required="false" defvalue="01" dictname="STD_ZB_FLOW_TYPE" readonly="true"/>
		    <emp:select id="IqpAssetProApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" required="false" hidden="true" defvalue="000"/>
		   	<emp:text id="IqpAssetProApp.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="IqpAssetProApp.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="IqpAssetProApp.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="IqpAssetProApp.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="IqpAssetProApp.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="IqpAssetProApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

