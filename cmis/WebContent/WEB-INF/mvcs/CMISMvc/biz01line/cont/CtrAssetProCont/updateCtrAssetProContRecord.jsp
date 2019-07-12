<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(CtrAssetProCont._checkAll()){
			CtrAssetProCont._toForm(form);
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
						alert("签订成功!");
						var url = '<emp:url action="queryCtrAssetProContList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("签订异常!");
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab">
	<emp:tab label="基本信息" id="base_tab"> 
	<emp:form id="submitForm" action="updateCtrAssetProContRecord.do" method="POST">
		<emp:gridLayout id="CtrAssetProContGroup" maxColumn="2" title="资产项目管理">
			<emp:text id="CtrAssetProCont.cont_no" label="项目编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CtrAssetProCont.serno" label="业务编号" maxlength="40" required="false" readonly="true" />
			<emp:text id="CtrAssetProCont.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
			<emp:text id="CtrAssetProCont.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产证券化" readonly="true"/>
			<emp:text id="CtrAssetProCont.pro_name" label="项目名称" maxlength="80" required="false" readonly="true"/>
			<emp:text id="CtrAssetProCont.pro_short_name" label="项目简称" maxlength="80" required="false" readonly="true"/>
			<emp:select id="CtrAssetProCont.pro_type" label="项目类型" required="false" readonly="true" dictname="STD_ZB_ASSET_PRO_TYPE"/>
			<emp:pop id="CtrAssetProCont.pro_org_displayname" label="资产所属机构" required="false" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getProOrgID" readonly="true"/>
			<emp:text id="CtrAssetProCont.pro_short_memo" label="项目简介" maxlength="200" required="false" readonly="true"/>
			<emp:select id="CtrAssetProCont.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="CtrAssetProCont.pro_amt" label="项目金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrAssetProCont.pro_qnt" label="笔数" maxlength="38" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="CtrAssetProCont.ser_date" label="签订日期"  required="true" readonly="true" defvalue="${context.OPENDAY}" />
			<emp:date id="CtrAssetProCont.pack_date" label="封包日期" required="false" />
			<!-- modified by lisj 2015-3-11  需求编号：【XD150303017】关于资产证券化的信贷改造 begin -->
			<emp:text id="CtrAssetProCont.issue_qnt" label="发行总量" maxlength="16" required="false" />
			<!-- modified by lisj 2015-3-11  需求编号：【XD150303017】关于资产证券化的信贷改造 end -->
			<emp:date id="CtrAssetProCont.issue_date" label="计划发行日期" required="false" />
			<emp:date id="CtrAssetProCont.int_start_date" label="起息日" required="false" />
			<emp:date id="CtrAssetProCont.end_date" label="法定到期日" required="true" />
			<emp:date id="CtrAssetProCont.final_date" label="终结日期" required="false" />
			<emp:date id="CtrAssetProCont.approve_date" label="审批通过日期" required="false" readonly="true" />
			<emp:select id="CtrAssetProCont.is_rgt_res" label="是否有追索权" required="true" dictname="STD_ZX_YES_NO" />
			
			<emp:text id="CtrAssetProCont.pro_org" label="资产所属机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:text id="CtrAssetProCont.manager_br_id_displayname" label="管理机构" required="true" readonly="true"/>
			<emp:text id="CtrAssetProCont.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="CtrAssetProCont.cont_status" label="项目状态" maxlength="5" hidden="true" />
		   	<emp:text id="CtrAssetProCont.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="CtrAssetProCont.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="CtrAssetProCont.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="CtrAssetProCont.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="CtrAssetProCont.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="签订" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	</emp:tab>
   	<emp:ExtActTab></emp:ExtActTab>
  	</emp:tabGroup>
</body>
</html>
</emp:page>
