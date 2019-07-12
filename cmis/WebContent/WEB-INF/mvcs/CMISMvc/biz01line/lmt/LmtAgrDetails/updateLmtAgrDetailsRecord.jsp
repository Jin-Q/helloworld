<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
<%
	String serno = request.getParameter("serno");
	String cus_id = request.getParameter("cus_id");
%>
	/*--user code begin--*/
	function doModagr(){
		if(!LmtAgrDetails._checkAll()){
			return false;
		}
	    var handleSuccess = function(o){
	    	if(o.responseText !== undefined) {
	            try {
					var jsonstr = eval("("+o.responseText+")");
	            } catch(e) {
					alert(jsonstr.msg);
	              	return;
	            }
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("保存成功！");
					doReturn();
					//var url = '<emp:url action="queryLmtAppDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}';
					//url = EMPTools.encodeURI(url);
					//window.location = url;
	            }else {
	            	alert("保存失败！"+jsonstr.msg);
			    }
	         }
		}
		var handleFailure = function(o){
		}
		var callback = {
            success:handleSuccess,
            failure:handleFailure
        }
        
        var form = document.getElementById("submitForm");
		LmtAgrDetails._toForm(form)
        var postData = YAHOO.util.Connect.setForm(form);
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	}

	//设置产品返回 
	function setProds(data){
		LmtAgrDetails.prd_id._setValue(data[0]);
		LmtAgrDetails.prd_id_displayname._setValue(data[1]);
	}

	//返回方法
	function doReturn(){
		var url = "";
		var ogr = '${context.ogr}';
		if(null!=ogr && "" != ogr && "indiv"==ogr){   //个人额度
			url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}';
		}else{
			url = '<emp:url action="queryLmtAppDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}';
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//是否调整期限值改变事件
	function adjTerm(_value){
		if("1"==_value){
			LmtAgrDetails.term_type._obj._renderHidden(false);
			LmtAgrDetails.term._obj._renderHidden(false);
		}else{
			LmtAgrDetails.term_type._obj._renderHidden(true);
			LmtAgrDetails.term._obj._renderHidden(true);
		}
	}
	/*--user code end--*/
	
	window.onload=function(){  
		var sub_type = LmtAgrDetails.sub_type._getValue();
		if("01"==sub_type){//一般授信
			LmtAgrDetails.core_corp_cus_id._obj._renderHidden(true);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderHidden(true);
			LmtAgrDetails.core_corp_duty._obj._renderHidden(true);
		}else{   //供应链授信
			LmtAgrDetails.core_corp_cus_id._obj._renderHidden(false);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderHidden(false);

			LmtAgrDetails.core_corp_cus_id._obj._renderRequired(true);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderRequired(true);
			
			LmtAgrDetails.core_corp_duty._obj._renderHidden(false);
		}
	}
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateLmtAgrDetailsRecord.do?belg_line=${context.belg_line}" method="POST">
		<emp:gridLayout id="LmtAgrDetailsGroup" maxColumn="2" title="原授信分项调整">
			<emp:text id="LmtAgrDetails.agr_no" label="授信协议编号" maxlength="40" required="true" colSpan="2" readonly="true"/>
			<emp:pop id="LmtAgrDetails.core_corp_cus_id" label="核心企业客户码 " required="false" url="searchCoreConPop.do" returnMethod="returnCoreCus" popParam="width=700px,height=650px"  hidden="true"/>
			<emp:select id="LmtAgrDetails.core_corp_duty" label="核心企业责任" required="false" dictname="STD_ZB_CORP_DUTY" />
			<emp:text id="LmtAgrDetails.core_corp_cus_id_displayname" label="核心企业客户名称" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly" hidden="true"/>
			
			<emp:select id="LmtAgrDetails.limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" required="true"/>
			<emp:select id="LmtAgrDetails.sub_type" label="分项类别" readonly="true" required="true" dictname="STD_LMT_PROJ_TYPE"/>
			<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号"  required="true" readonly="true"/>
			<emp:text id="LmtAgrDetails.limit_name" label="额度品种名称" maxlength="60" required="true" />
			<emp:pop id="LmtAgrDetails.prd_id" label="适用产品" url='showPrdCheckTreeDetails.do?bizline=${context.belg_line}' returnMethod="setProds" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:textarea id="LmtAgrDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAgrDetails.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAgrDetails.crd_amt" label="授信金额" maxlength="18" dataType="Currency" required="true"/>
			<emp:select id="LmtAgrDetails.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true"/>
			<emp:select id="LmtAgrDetails.is_pre_crd" label="是否预授信" dictname="STD_ZX_YES_NO" required="true"/>
			<emp:select id="LmtAgrDetails.is_adj_term" label="是否调整期限" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" onchange="adjTerm(this.value)"/>
			<emp:select id="LmtAgrDetails.term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" required="true" hidden="true"/>
			<emp:text id="LmtAgrDetails.term" label="授信期限" maxlength="2" required="true" hidden="true"/>
			<emp:date id="LmtAgrDetails.start_date" label="授信起始日" required="true" readonly="true"/>
			<emp:date id="LmtAgrDetails.end_date" label="授信到期日" required="true" readonly="true"/>
			
			<emp:text id="LmtAgrDetails.enable_amt" label="启用金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAgrDetails.cus_id" label="客户码" maxlength="32" required="false" hidden="true" defvalue="<%=cus_id%>"/>
			<emp:text id="LmtAgrDetails.lmt_status" label="额度状态" maxlength="2" required="false" hidden="true"/>
			<emp:text id="LmtAgrDetails.cus_type" label="客户类别" maxlength="3" required="false" hidden="true"/>
			<emp:text id="LmtAgrDetails.serno" label="申请流水号" maxlength="40" required="true" hidden="true" defvalue="<%=serno%>"/>
			<emp:text id="LmtAgrDetails.bail_rate" label="保证金缴存比例" maxlength="16" required="false" hidden="true"/>			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="modagr" label="修改" op="update"/>
			<emp:button id="reset" label="重置" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
