<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op= "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if("view".equals(op)||"to_storage".equals(op)){
		request.setAttribute("canwrite","");
	}
String guaranty_no = request.getParameter("guaranty_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			function doAdd(){
		var form = document.getElementById('submitForm');
		MortUsufructSea._toForm(form);
		if(!MortUsufructSea._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("保存成功");
						var guaranty_no = '${context.guaranty_no}';
						var collateral_type_cd = '${context.collateral_type_cd}';
						var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no+'&collateral_type_cd='+collateral_type_cd;
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("保存失败!");
				document.getElementById("button_add").disabled="";
				document.getElementById("button_reset").disabled="";
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	function doReset(){
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		location.href(url);
		page.dataGroups.MortUsufructSeaGroup.reset();
	};		

	//两个日期作比较
	function checkInsurStartDate(){
		if(MortUsufructSea.appr_begin_date._obj.element.value!=''){
			var e = MortUsufructSea.appr_end_date._obj.element.value;
			var s = MortUsufructSea.appr_begin_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('批准使用起始日期必须小于或等于当前日期！');
        		MortUsufructSea.appr_begin_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('批准使用起始日期必须小于或等于批准使用终止日期！');
            		MortUsufructSea.appr_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortUsufructSea.appr_end_date._obj.element.value!=''){
			var e = MortUsufructSea.appr_end_date._obj.element.value;
			var s = MortUsufructSea.appr_begin_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('批准使用终止日期必须大于或等于批准使用起始日期！');
            		MortUsufructSea.appr_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortUsufructSeaRecord.do" method="POST">
		
		<emp:gridLayout id="MortUsufructSeaGroup" title="海域使用权" maxColumn="2">
			<emp:text id="MortUsufructSea.sea_id" label="海域编号" required="false" hidden="true"/>
			<emp:text id="MortUsufructSea.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			
			<emp:text id="MortUsufructSea.range" label="范围" maxlength="100" required="true" />
			<emp:select id="MortUsufructSea.use_type_cd" label="用海类型" required="true" dictname="STD_SEA_USE_TYPE" />
			<emp:text id="MortUsufructSea.use_others" label="其他用海" maxlength="100" required="true" />
			<emp:text id="MortUsufructSea.use_area" label="用海面积(公顷)" maxlength="16" required="true" dataType="Double" />
			<emp:date id="MortUsufructSea.appr_begin_date" label="批准使用起始日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortUsufructSea.appr_end_date" label="批准使用终止日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:text id="MortUsufructSea.reg_no" label="登记编号" maxlength="100" required="false" />
			<emp:textarea id="MortUsufructSea.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			<emp:text id="MortUsufructSea.corporation" label="法人代表//" maxlength="100" required="false" hidden="true" />
			<emp:text id="MortUsufructSea.address" label="地址//" maxlength="200" required="false" hidden="true" />
			<emp:date id="MortUsufructSea.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:text id="MortUsufructSea.use_no" label="水域滩涂养殖使用证号" maxlength="100" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("view".equals(op)||"to_storage".equals(op)){%>		
			<%}else{%>
			<emp:button id="add" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<% } %>			
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

