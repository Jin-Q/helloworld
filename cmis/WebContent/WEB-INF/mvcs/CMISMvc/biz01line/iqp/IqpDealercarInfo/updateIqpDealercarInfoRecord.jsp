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
		if(!IqpDealercarInfo._checkAll()){
			return;
		}
		IqpDealercarInfo._toForm(form); 
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
						alert("修改成功!");
						var url = '<emp:url action="queryIqpDealercarInfoList.do"/>?serno=${context.serno}'; 
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("请重新选择客户!"); 
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
	};				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpDealercarInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpDealercarInfoGroup" maxColumn="2" title="经销商汽车信息">
			<emp:text id="IqpDealercarInfo.serno" label="业务编号" maxlength="40" hidden="true" />
			<emp:text id="IqpDealercarInfo.car_serno" label="车辆信息编号" maxlength="40" hidden="true"/>
			<emp:text id="IqpDealercarInfo.car_name" label="车辆名称" maxlength="100" required="true" /> 
			<emp:text id="IqpDealercarInfo.car_num" label="数量（辆）" maxlength="38" required="true" dataType="Int"/> 
			<emp:text id="IqpDealercarInfo.car_amt" label="单价" maxlength="16" required="true" dataType="Currency" />
			<emp:select id="IqpDealercarInfo.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
		    <emp:text id="IqpDealercarInfo.car_sign" label="车辆品牌" maxlength="40" required="true" />
		    <emp:select id="IqpDealercarInfo.car_type" label="车辆类型" required="true" dictname="STD_ZB_CAR_TYPE" />
		</emp:gridLayout> 
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
