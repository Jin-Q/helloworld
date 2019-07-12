<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	} 
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function onload(){
		IqpTogetherRqstr.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
    };
    function getCusForm(){
		var cus_id = IqpTogetherRqstr.cus_id._getValue();
		if(cus_id == "" || cus_id == null){
           alert("请先选择客户!");
           return; 
		}
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	function returnCus(data){
		var cus_id = data.cus_id._getValue();
		var cus_name = data.cus_name._getValue();
		var cert_type = data.cert_type._getValue();
        if(cert_type == "210"){
        	var cert_type ="组织机构代码";
        }else if(cert_type == "100"){
        	var cert_type ="身份证";
        }else if(cert_type == "140"){
        	var cert_type ="护照";
        }else if(cert_type == "112"){
        	var cert_type ="军官证";
        }else if(cert_type == "161"){
        	var cert_type ="港澳居民来往内地通行证";
        }else if(cert_type == "162"){
        	var cert_type ="台湾同胞来往内地通行证";
        }else if(cert_type == "130"){
        	var cert_type ="户口簿";
        }else if(cert_type == "111"){
        	var cert_type ="士兵证";
        }else if(cert_type == "101"){
        	var cert_type ="临时身份证";
        }else if(cert_type == "181"){
        	var cert_type ="外国人居留证";
        }else if(cert_type == "122"){
        	var cert_type ="警官证";
        }else if(cert_type == "00B"){
        	var cert_type ="登记注册号";
        }
		
		var cert_code = data.cert_code._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "error"){   
					alert("客户已存在");
				}else{
					IqpTogetherRqstr.cus_id._setValue(cus_id);
					IqpTogetherRqstr.cus_name._setValue(cus_name);
					IqpTogetherRqstr.cert_type._setValue(cert_type);
					IqpTogetherRqstr.cert_code._setValue(cert_code);   
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
		var url = '<emp:url action="checkCus.do"/>?cus_id='+cus_id+'&serno='+'<%=serno%>';    
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	};	

	function doSub(){
		var form = document.getElementById("submitForm");
		IqpTogetherRqstr._checkAll();
		if(IqpTogetherRqstr._checkAll()){
			IqpTogetherRqstr._toForm(form); 
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
						alert("新增成功!");
						var url = '<emp:url action="queryTogetherRqstrList.do"/>?op=update&serno='+'<%=serno%>'; 
						url = EMPTools.encodeURI(url);
						window.location = url; 
					}else {
						alert("发生异常!");
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

	function doBack(){
        window.history.go(-1);

	};
/*--user code end--*/  
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="addTogetherRqstrRecord.do" method="POST">
		
		<emp:gridLayout id="IqpTogetherRqstrGroup" title="共同申请人基本信息" maxColumn="2"> 
			<emp:pop id="IqpTogetherRqstr.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=belg_line='BL300'&returnMethod=returnCus" required="true" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" buttonLabel="选择"/>
			<emp:text id="IqpTogetherRqstr.cert_type" label="证件类型" maxlength="40" required="false" readonly="true" dictname="STD_ZB_CERT_TYP" />  
			<emp:text id="IqpTogetherRqstr.cert_code" label="证件号码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpTogetherRqstr.cus_name" label="姓名" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpTogetherRqstr.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true"/>
			<emp:text id="IqpTogetherRqstr.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

