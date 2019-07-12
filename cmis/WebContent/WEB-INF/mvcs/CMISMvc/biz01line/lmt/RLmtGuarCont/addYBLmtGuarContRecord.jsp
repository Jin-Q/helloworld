<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/     
	function returnCont(data){
		GrtLoanRGur.guar_cont_no._setValue(data.guar_cont_no._getValue());
    };
    function load(){
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
					alert("已引用此担保合同,请重新引用!");
					var url = '<emp:url action="introYbGrtGuarContList.do"/>?limit_code=${context.limit_code}&cus_id=${context.cus_id}&rel=sxRel';
					url=EMPTools.encodeURI(url);  
			      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
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
		var url = '<emp:url action="checkRLmtGuarCont.do"/>&guar_cont_no=${context.guar_cont_no}&limit_code=${context.limit_code}';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
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
						alert("引入成功!");
						 window.opener.location.reload();
					     window.close();    
					}else {
						alert("引入失败!");
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

	function doBack(){ 
		var url = '<emp:url action="introYbGrtGuarContList.do"/>?limit_code=${context.limit_code}&cus_id=${context.cus_id}&rel=sxRel';
		url=EMPTools.encodeURI(url);       
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="load();">   
	
	<emp:form id="submitForm" action="addRLmtGuarContRecord.do" method="POST">
		 
		<emp:gridLayout id="RLmtGuarContGroup" title="授信和担保合同关系表" maxColumn="2">
			<emp:text id="RLmtGuarCont.limit_code" label="授信额度编号" defvalue="${context.limit_code}" maxlength="40" hidden="true" />
			<emp:text id="RLmtGuarCont.agr_no" label="授信协议编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="RLmtGuarCont.guar_cont_no" label="担保合同编号" defvalue="${context.guar_cont_no}" maxlength="40" required="true" />
			<emp:select id="RLmtGuarCont.is_per_gur" label="是否阶段性担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="RLmtGuarCont.guar_amt" label="本次担保金额" maxlength="16" defvalue="${context.guar_amt}" readonly="true" required="true" dataType="Currency" />
			<emp:select id="RLmtGuarCont.is_add_guar" label="是否追加担保" required="true" dictname="STD_ZX_YES_NO" />      
			<emp:select id="RLmtGuarCont.corre_rel" label="关联关系" hidden="true" defvalue="1" dictname="STD_BIZ_CORRE_REL" />
		    <emp:text id="RLmtGuarCont.grt_type" label="担保类型"  defvalue="YB" required="false" hidden="true"/>  
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

