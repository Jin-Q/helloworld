<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	String flag = request.getParameter("op");
%>
<script type="text/javascript">
	
	function checkAmt(obj){	    	
		var amt = obj.value;
		var total_amt = '${context.lmt_amt}';
		if(total_amt == null ||total_amt == 'null' || total_amt ==''){
			alert("先保存授信，再进行分项操作！");
			obj.value='';
			return;
		}
		if( parseFloat(amt) > parseFloat(total_amt)){
			alert("单笔授信额度不能大于总额度!");
			obj.value='';			
		}
	}
	
	function doSave(){
		var form = document.getElementById('submitForm');
    	if(form){
    		LmtSubAppList._toForm(form);
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功！");
					}
				}
			};			
			var callback = {
				success:handleSuccess,
				failure:function(){alert("保存失败！");}
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
    	}
	}
</script>
</head>

<body class="page_content">		
<emp:form id="submitform" action='saveLmtSubApp.do' method="POST">	
	<emp:table icollName="LmtSubAppList" pageMode="false" url="pageLmtSigLmtLmtSubAppQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="业务编号" defvalue="${context.serno}"/>
		<emp:text id="variet_no" label="品种编号" />
		<emp:text id="variet_name" label="品种名称" />
		<emp:text id="lmt_amt" label="授信额度" flat="true" dataType="Currency" onblur="checkAmt(this)"  defvalue='${context.lmt_amt}'/>
	</emp:table>
		<div align="center">
		<%if("view".equals(flag)){%>
		<%}else{ %>
			<emp:button id="save" label="保存"/>
		<%} %>
		</div>	

</emp:form>			
</body>
</html>
</emp:page>