<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	//String serno = (String)request.getParameter("serno");
	String serno = "";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
%>   
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	  //获取成员厂商信息
	function getCusInfo(data){
		IqpAppMemMana.mem_cus_id._setValue(data.cus_id._getValue());
		IqpAppMemMana.mem_cus_id_displayname._setValue(data.cus_name._getValue());
		checkCusExistNet(data.cus_id._getValue());
	};	
	//返回	
	function doReturn(){
		var url = '<emp:url action="queryIqpAppMemManaList.do"/>?&serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//判断客户是否已经在本行有网络
	function checkCusExistNet(cus_id){
		var handleSuccess = function(o){ 		
		var jsonstr = eval("(" + o.responseText + ")");
		var flag = jsonstr.flag;
			if(flag == "error" ){
				alert("该成员已存在网络中!");
				IqpAppMemMana.mem_cus_id._setValue("");
				IqpAppMemMana.mem_cus_id_displayname._setValue("");
			}
		}
		var handleFailure = function(o){
		        alert("异步回调失败！");	
		};
		var url = '<emp:url action="checkHaveMem.do"/>?cus_id='+cus_id+'&serno='+'<%=serno%>';
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};

	function doSub(){
		if(!IqpAppMemMana._checkAll()){
           return;
		}
		var mem_cus_id = IqpAppMemMana.mem_cus_id._getValue();
			var form = document.getElementById("submitForm");
			IqpAppMemMana._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {							
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功!");
						var url = '<emp:url action="getIqpAppMemManaUpdatePage.do"/>?mem_cus_id='+mem_cus_id+'&serno='+'<%=serno%>'+'&op=update';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag=="fail"){
						alert("保存失败!");
					}
				}
			};
			var callback = {
				success:handleSuccess,
				failure:null
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	};

	function selectLmtType(){
		var mem_manuf_type = IqpAppMemMana.mem_manuf_type._getValue();
		var list = new Array();
        if(mem_manuf_type == "02"){
            var dealer_lmt_type = "${context.dealer_lmt_type}";
            list = dealer_lmt_type.split(",");
        	IqpAppMemMana.lmt_type._setValue(list);
        }else if(mem_manuf_type == "01"){
        	var provider_lmt_type = "${context.provider_lmt_type}";
        	list = provider_lmt_type.split(",");
        	IqpAppMemMana.lmt_type._setValue(list);
        }
    };

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="addIqpAppMemManaRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAppMemManaGroup" title="网络成员信息" maxColumn="2">
			<emp:pop id="IqpAppMemMana.mem_cus_id" label="成员企业客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=getCusInfo"  required="true" />
			<emp:text id="IqpAppMemMana.mem_cus_id_displayname" label="成员厂商名称"  required="true" readonly="true"/>
			<emp:select id="IqpAppMemMana.mem_manuf_type" label="成员企业类别" required="true" dictname="STD_ZB_MANUF_TYPE" onchange="selectLmtType()"/>
			
			<emp:text id="IqpAppMemMana.term" label="在途期限（天）" maxlength="10" required="false" />
			<emp:select id="IqpAppMemMana.status" label="成员变更状态" dictname="STD_ZB_MEN_TYPE" defvalue="1" readonly="true" required="false" />
			<emp:text id="IqpAppMemMana.lmt_quota" label="授信限额（元）" maxlength="16" required="true" dataType="Currency" />
		    <emp:checkbox id="IqpAppMemMana.lmt_type" label="授信业务种类" dictname="STD_BIZ_TYPE" layout="false" colSpan="2" required="false" disabled="true"/>
		    <emp:text id="IqpAppMemMana.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="true" hidden="true"/> 
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

