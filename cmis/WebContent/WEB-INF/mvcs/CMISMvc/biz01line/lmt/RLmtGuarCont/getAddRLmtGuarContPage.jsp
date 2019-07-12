<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function returnLmtNo(data){
		RLmtGuarCont.limit_code._setValue(data.limit_code._getValue());
	}
    
    function load(){
        var cus_id = RLmtGuarCont.cus_id._getValue();
        var openDay = '${context.OPENDAY}';
        //modify by jiangcuihua url参数后面带有空格等特殊字符，需要过滤
        var url="<emp:url action='queryLmtAgrDetailsPop.do'/>&flag=4&openDay="+openDay+"&cus_id="+cus_id+"&returnMethod=returnLmtNo";
        RLmtGuarCont.limit_code._obj.config.url = EMPTools.encodeURI(url); 
        //  RLmtGuarCont.limit_code._obj.config.url="<emp:url action='queryLmtAgrDetailsPop.do'/>&condition= AND cus_id="+cus_id+'&returnMethod=returnLmtNo';
    }; 

	function doSub(){
		if(!RLmtGuarCont._checkAll()){
           return;   
		}  
		var form = document.getElementById("submitForm");
		RLmtGuarCont._toForm(form);  
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
					window.opener.location.reload();
				    window.close();
				}else if(flag == "exists"){
					alert('该担保合同已与当前授信分项关联，请重新选择！');
					return;
				}else {
					alert("新增失败!");
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
	};

	function doBack(){ 
      	window.close();
	};      
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="load()">    
	
	<emp:form id="submitForm" action="addRLmtGuarContRecordForNew.do" method="POST">
		
		<emp:gridLayout id="RLmtGuarContGroup" title="授信和担保合同关系表" maxColumn="2">
			<emp:pop id="RLmtGuarCont.limit_code" label="授信额度编号" url="" required="true" buttonLabel="选择"/>
			<emp:text id="RLmtGuarCont.guar_cont_no" label="担保合同编号" defvalue="${context.guar_cont_no}" maxlength="40" required="true" readonly="true"/>
			<emp:text id="RLmtGuarCont.guar_amt" label="本次担保金额" maxlength="16" defvalue="0" readonly="true" dataType="Currency" />
			<emp:select id="RLmtGuarCont.is_per_gur" label="是否阶段性担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="RLmtGuarCont.is_add_guar" label="是否追加担保" required="true" dictname="STD_ZX_YES_NO" />      
		    <emp:text id="RLmtGuarCont.grt_type" label="担保类型"  defvalue="ZGE" required="false" hidden="true"/>
		    <emp:text id="RLmtGuarCont.cus_id" label="客户码"  required="false" hidden="true"/>
		    <emp:text id="RLmtGuarCont.guar_cont_type" label="担保合同类型"  required="false" hidden="true"/>
		</emp:gridLayout>           
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" />     
			<emp:button id="back" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

