<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<% String cus_id=(String)request.getParameter("cus_id");
   String net_agr_no=(String)request.getParameter("net_agr_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
      //获取成员厂商信息
	function getCusInfo(data){
		var cus_id ='<%=cus_id%>'
		if(data.cus_id._getValue()==cus_id){
			alert("成员客户不能与中心厂商为同一客户！");
		  	IqpMemMana.mem_cus_id._setValue("");
		  	IqpMemMana.mem_cus_id_displayname._setValue("");
		  	return false;
	   	}
	   	   IqpMemMana.mem_cus_id._setValue(data.cus_id._getValue());
	   	   IqpMemMana.mem_cus_id_displayname._setValue(data.cus_name._getValue());
	}
	
	function doReturn(){
		var url = '<emp:url action="queryIqpMemManaList.do"/>?cus_id=${context.cus_id}'+"&net_agr_no=${context.net_agr_no}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	
	function doSub(){
		if(IqpMemMana._checkAll()){
			var form = document.getElementById("submitForm");
			IqpMemMana._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {							
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					var id=jsonstr.id;
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert(msg);
						var term = IqpMemMana.term._getValue();
						var lmt_quota = IqpMemMana.lmt_quota._getValue();
						var status = IqpMemMana.status._getValue();
						var lmt_type = IqpMemMana.lmt_type._getValue();
						var url = '<emp:url action="getIqpMemManaUpdatePage.do"/>?'+id+"&cus_id=${context.cus_id}"+"&menuId=IqpMemMana"
						+"&op=update&term="+term+"&lmt_quota="+lmt_quota+"&status="+status+"&lmt_type="+lmt_type;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag=="fail"){
						alert(msg);
						window.location.reload();
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
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addIqpMemManaRecord.do" method="POST">		
		<emp:gridLayout id="IqpMemManaGroup" title="成员管理" maxColumn="2">	
		    <emp:text id="IqpMemMana.core_cus_id" label="中心厂商编号" maxlength="32" required="true" readonly="true"/>
		    <emp:text id="IqpMemMana.core_cus_id_displayname" label="中心厂商名称"   required="true" readonly="true"/>		
			<emp:pop id="IqpMemMana.mem_cus_id" label="成员厂商编号" url="queryAllCusPop.do?cusTypCondition=BELG_LINE='BL200'&returnMethod=getCusInfo" required="true" />
			<emp:text id="IqpMemMana.mem_cus_id_displayname" label="成员厂商名称"  required="true" readonly="true"/>
			<emp:select id="IqpMemMana.mem_manuf_type" label="成员厂商类别" required="true" dictname="STD_ZB_MANUF_TYPE" />
			<emp:text id="IqpMemMana.term" label="在途期限(日)" maxlength="16" required="true" />
			<emp:text id="IqpMemMana.lmt_quota" label="授信限额" maxlength="18" required="false" dataType="Currency" colSpan="2"/>
			<emp:checkbox id="IqpMemMana.lmt_type" label="授信业务种类" required="true" dictname="STD_BIZ_TYPE" layout="false" delimiter="&nbsp;&nbsp;" colSpan="2"/>
			<emp:select id="IqpMemMana.status" label="状态" required="true" dictname="STD_ZB_MEM_STATUS" defvalue="1" readonly="true"/>
			<emp:select id="IqpMemMana.core_corp_duty" label="核心企业责任" dictname="STD_ZB_CORP_DUTY" required="true" defvalue="02" hidden="true"/>
			<emp:text id="IqpMemMana.input_id" label="登记人" maxlength="32" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="IqpMemMana.input_br_id" label="登记机构" defvalue="$organNo" required="false" hidden="true"/>
			<emp:date id="IqpMemMana.input_date" label="登记日期" defvalue="$OPENDAY" hidden="true"/>
			<emp:text id="IqpMemMana.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.serno" label="业务编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpMemMana.pk1" label="主键" maxlength="32" required="false" hidden="true"/>			
		</emp:gridLayout>	
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>