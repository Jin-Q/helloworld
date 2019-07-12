<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<%
	String cus_id=(String)request.getParameter("cus_id");
%>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryIqpMemManaList.do"/>?cus_id=${context.cus_id}'+"&net_agr_no=${context.net_agr_no}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
       //获取成员厂商信息
    function getCusInfo(data){
   	   IqpMemMana.mem_cus_id._setValue(data.cus_id._getValue());
   	   IqpMemMana.mem_cus_id_displayname._setValue(data.cus_name._getValue());
    }
	function doUpdate(){
		if(IqpMemMana._checkAll()){
			var form = document.getElementById("submitForm");
			IqpMemMana._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert(msg);
						window.location.reload();
					}else if(flag=="fail"){
						alert(msg);	
					}else{
						alert("数据操作失败！");
						}
				}
			};
			var callback = {
				success:handleSuccess,
				failure:null
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}
	}

	//查看授信台账信息
	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger&showButton=N";
			url = EMPTools.encodeURI(url);
			window.open(url,"agrInfoPage","height=650,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=1,location=no,status=no");
			//window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//校验授信限额是否大于供应链下总的额度
	function checkTotal(){
		var _value =IqpMemMana.lmt_quota._getValue();
		var total = "${context.total}";
		if((_value-total)>0){
			alert("授信限额["+_value+"]不能超过供应链授信总额["+total+"]。");
			IqpMemMana.lmt_quota._setValue('');
			return false;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content"  onload="checkTotal()">
	 <emp:tabGroup mainTab="base_tab" id="mainTab" >
         <emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
         <emp:form id="submitForm" action="updateIqpMemManaRecord.do" method="POST">
		<emp:gridLayout id="IqpMemManaGroup" maxColumn="2" title="成员管理">
			<emp:text id="IqpMemMana.cus_id" label="中心厂商编号" maxlength="32" required="true" readonly="true"/>
		    <emp:text id="IqpMemMana.cus_id_displayname" label="中心厂商名称"   required="true" readonly="true"/>
			<emp:pop id="IqpMemMana.mem_cus_id" label="成员厂商编号" url="queryAllCusPop.do?cusTypCondition=BELG_LINE='BL200'&returnMethod=getCusInfo" 
			defvalue="${context.mem_cus_id}" required="true" />
			<emp:text id="IqpMemMana.mem_cus_id_displayname" label="成员厂商名称"   required="true" readonly="true"/>
			<emp:select id="IqpMemMana.mem_manuf_type" label="成员厂商类别" required="true" dictname="STD_ZB_MANUF_TYPE" readonly="true" defvalue="${context.mem_manuf_type}"/>
			<emp:text id="IqpMemMana.term" label="在途期限(日)" maxlength="16" required="true" defvalue="${context.term}"/>
			<emp:text id="IqpMemMana.lmt_quota" label="授信限额" maxlength="18" required="true" dataType="Currency" colSpan="2" onchange="checkTotal()" defvalue="${context.lmt_quota}"/>
			<emp:checkbox id="IqpMemMana.lmt_type" label="授信业务种类" required="true" dictname="STD_BIZ_TYPE" layout="false" colSpan="2" delimiter="&nbsp;&nbsp;"/>
			<emp:select id="IqpMemMana.status" label="状态" required="true" dictname="STD_ZB_MEM_STATUS" readonly="true" defvalue="${context.status}"/>
			<emp:text id="IqpMemMana.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
			<emp:date id="IqpMemMana.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.core_corp_duty" label="核心企业责任" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.serno" label="业务编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.pk1" label="主键" maxlength="32" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="update" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	<div class='emp_gridlayout_title'>供应链下授信情况&nbsp;</div>

	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtAgrDetailsList" pageMode="false" url="">
		<emp:text id="limit_code" label="额度编码" />
		<emp:text id="cus_type" label="客户类型" hidden="true"/>
		<emp:text id="core_corp_duty" label="核心企业责任" dictname="STD_ZB_CORP_DUTY"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
	</emp:table>
	</emp:tab>
   <emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
</body>
</html>
</emp:page>
