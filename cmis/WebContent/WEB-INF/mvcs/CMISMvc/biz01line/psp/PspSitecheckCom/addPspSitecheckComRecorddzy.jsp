<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_label1 {
	vertical-align: top;
	padding-top: 4px;
	text-align: left;
	width: 400px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var task_id = '<%=task_id%>';
		PspSitecheckCom.task_id._setValue(task_id);
		
		convert_dzyxthzgzk();
		convert_dzybxnlpj();
		convert_dzysdwcfdj();
		convert_dzysdwcfdj();

	}
	    
	

	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspSitecheckCom._checkAll()){
			PspSitecheckCom._toForm(form); 
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
						window.location.reload();
						doReturn();
					}else {
						alert("保存异常!"); 
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};	

	function doReturn(){
		
		window.close();
	}
	
	
	function convert_dzyxthzgzk(){
		if(PspSitecheckCom.dzyxthzgzk._getValue()=="2"){
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderHidden(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(true);
		
		}else{
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderHidden(true);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._setValue("");
		}
	}
	function convert_dzybxnlpj(){
		if(PspSitecheckCom.dzybxnlpj._getValue()=="2"){
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderHidden(false);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(true);
			
		}else{
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderHidden(true);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzybxnlpj_ycsm._setValue("");
		}
	}
	function convert_dzysdwcfdj(){
		if(PspSitecheckCom.dzysdwcfdj._getValue()=="1" || PspSitecheckCom.djjgslcfdj._getValue()=="1"){
			PspSitecheckCom.cfdjsj._obj._renderHidden(false);
			PspSitecheckCom.cfdjsj._obj._renderRequired(true);
			PspSitecheckCom.cfdjyy._obj._renderHidden(false);
			PspSitecheckCom.cfdjyy._obj._renderRequired(true);
			PspSitecheckCom.cfdjsqr._obj._renderHidden(false);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(true);
			//PspSitecheckCom.cfdjsj._setValue("");
			//PspSitecheckCom.cfdjyy._setValue("");
			//PspSitecheckCom.cfdjsqr._setValue("");
		}else{
			PspSitecheckCom.cfdjsj._obj._renderHidden(true);
			PspSitecheckCom.cfdjsj._obj._renderRequired(false);
			PspSitecheckCom.cfdjsj._setValue("");
			PspSitecheckCom.cfdjyy._obj._renderHidden(true);
			PspSitecheckCom.cfdjyy._obj._renderRequired(false);
			PspSitecheckCom.cfdjyy._setValue("");
			PspSitecheckCom.cfdjsqr._obj._renderHidden(true);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(false);
			PspSitecheckCom.cfdjsqr._setValue("");
		}
	}

    
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updatePspSitecheckComRecordFortc.do" method="POST">
			<emp:text id="PspSitecheckCom.cus_id" label="客户号" hidden="true" defvalue="${context.cus_id}" />
			<emp:text id="PspSitecheckCom.pk_id" label="主键" required="false" hidden="true"/>
			<emp:text id="PspSitecheckCom.task_id" label="任务号" hidden="true" defvalue="${context.task_id}" />
		<emp:gridLayout id="PspSitecheckComGroup" title="现场检查" maxColumn="2">
			<emp:date id="PspSitecheckCom.check_time" label="检查时间"  required="true"/>
			<emp:text id="PspSitecheckCom.check_addr" label="检查地点" maxlength="100"   required="true" />
			<emp:text id="PspSitecheckCom.yjry" label="约见人员" maxlength="40" required="true" />
			<emp:select id="PspSitecheckCom.visit_type" label="现场检查类型"  required="true"   dictname="STD_ZB_VISIT_TYPE" defvalue="03" readonly="true"/>	
		</emp:gridLayout>	
		
		<emp:gridLayout id="PspSitecheckComGroup" title="抵质押品明细检查" maxColumn="2">
			<emp:radio id="PspSitecheckCom.dzyxthzgzk" label="抵（质）押物形态和占管状况" required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" onclick="convert_dzyxthzgzk()"/>
			<emp:textarea id="PspSitecheckCom.dzyxthzgzk_ycsm" label="抵（质）押物形态和占管状况异常说明" required="false" hidden="true" colSpan="2" />
			<emp:radio id="PspSitecheckCom.dzybxnlpj" label="抵（质）押物变现能力评价" required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" onclick="convert_dzybxnlpj()"/>
			<emp:textarea id="PspSitecheckCom.dzybxnlpj_ycsm" label="抵（质）押物变现能力异常说明" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.dzysdwcfdj" label="抵（质）押物实地（物）是否被查封、冻结" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_dzysdwcfdj()"/>
			<emp:radio id="PspSitecheckCom.djjgslcfdj" label="登记机关是否已受理查封、冻结" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_dzysdwcfdj()"/>
			<emp:date id="PspSitecheckCom.cfdjsj" label="查封、冻结时间"  required="false" hidden="true"/>
			<emp:textarea id="PspSitecheckCom.cfdjyy" label="原因说明" required="false" hidden="true" colSpan="2"/>	
			<emp:text id="PspSitecheckCom.cfdjsqr" label="查封、冻结申请人" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.dzyrdbyy" label="抵（质）押人的担保意愿是否发生变化" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.dywsfcz" label="抵押物是否出租" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.sfczyxzqsxfxys" label="是否存在影响我行债权的顺利实现的风险因素" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.dzybhyxdbxl" label="抵（质）押物市场价值是否发生不利变化，影响担保效力" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.bgdzydjzmsfqq" label="保管的抵质押登记证明文件是否齐全，账实是否相符" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:textarea id="PspSitecheckCom.dzywcz_qtzk" label="抵质押物存在的其他状况" required="false" colSpan="2"/>
		</emp:gridLayout>
	
		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

