<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
<%
	String serno = request.getParameter("serno");
	String cus_id = request.getParameter("cus_id");
	String app_type = request.getParameter("app_type");
	String lrisk_type = request.getParameter("lrisk_type");
	
	String op = request.getParameter("op");
	String subButtonId = request.getParameter("subButtonId");
%>
/*--user code begin--*/

	//下一步方法
	//BelgLine '01':公司、 '02':小微、 '03':供应链
	function doNext(){
		var serno ='<%=serno%>';
		var cus_id ='<%=cus_id%>';
		var prd_id = LmtApply.prd_id._getValue();
		var form = document.getElementById('submitForm');
		var result = LmtApply._checkAll();
		if(result){
			if(prd_id!='05'){//传统授信
				//checkBelgLine();
				if(prd_id=="01"){//所属条线：公司
					var url = '<emp:url action="getLmtAppDetailsAddPage.do"/>&BelgLine=BL100&serno='+ serno+'&cus_id='+cus_id+'&sub_type=01&app_type=<%=app_type%>&op=<%=op%>&subButtonId=<%=subButtonId%>&lrisk_type=<%=lrisk_type%>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{//所属条线：小微
					var url = '<emp:url action="getLmtAppDetailsAddPage.do"/>&BelgLine=BL200&serno='+ serno+'&cus_id='+cus_id+'&sub_type=01&app_type=<%=app_type%>&op=<%=op%>&subButtonId=<%=subButtonId%>&lrisk_type=<%=lrisk_type%>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			}else{//供应链授信
		    	var url = '<emp:url action="getLmtAppDetailsAddPage.do"/>&BelgLine=BL200&serno='+ serno+'&cus_id='+cus_id+'&sub_type=05&app_type=<%=app_type%>&op=<%=op%>&subButtonId=<%=subButtonId%>&lrisk_type=<%=lrisk_type%>';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		}else{
			alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	}

	function checkBelgLine(){
		var cus_id = '<%=cus_id%>';	
		var url = '<emp:url action="getCusBelgLine.do"/>&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("数据获取失败！");
						return;
					}
					var flag=jsonstr.flag;
					var flagInfo=jsonstr.flagInfo;
					var serno = '<%=serno%>';
					var cus_id = '<%=cus_id%>';	
					if(flag=="BL100"){//所属条线：公司
						var url = '<emp:url action="getLmtAppDetailsAddPage.do"/>&BelgLine=BL100&serno='+ serno+'&cus_id='+cus_id+'&sub_type=01&app_type=<%=app_type%>&op=<%=op%>&subButtonId=<%=subButtonId%>&lrisk_type=<%=lrisk_type%>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{//所属条线：小微
						var url = '<emp:url action="getLmtAppDetailsAddPage.do"/>&BelgLine=BL200&serno='+ serno+'&cus_id='+cus_id+'&sub_type=01&app_type=<%=app_type%>&op=<%=op%>&subButtonId=<%=subButtonId%>&lrisk_type=<%=lrisk_type%>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
			}
		};
		var handleFailure = function(o){
			alert("数据获取失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	function doReturn(){
		var serno ='<%=serno%>';
		var cus_id ='<%=cus_id%>';
		var app_type ='<%=app_type%>';
		var op ='<%=op%>';
		var subButtonId ='<%=subButtonId%>';
		var lrisk_type ='<%=lrisk_type%>';
		//var lmt_type ='lmt_type%>';
		//var url = '<emp:url action="queryLmtAppDetailsList.do"/>?serno='+ serno+'&cus_id='+cus_id+"&app_type="+app_type+"&op="+op+"&subButtonId="+subButtonId+"&lrisk_type="+lrisk_type+"&lmt_type="+lmt_type;
		var url = '<emp:url action="queryLmtAppDetailsList.do"/>?serno='+ serno+'&cus_id='+cus_id+"&app_type="+app_type+"&op="+op+"&subButtonId="+subButtonId+"&lrisk_type="+lrisk_type;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function onLoad(){
		//var lmt_type ='lmt_type';
		var options = LmtApply.prd_id._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "02" || options[i].value == "03" || options[i].value == "04" || options[i].value == "09" ){
				options.remove(i);
		
			}
		}
	}
/*--user code end--*/
</script>
</head>
<body class="page_content" style="width:200px" onload="onLoad()">
	<emp:form id="submitForm" method="POST">
		<emp:gridLayout id="LmtApplyGroup" title="授信分项新增向导" maxColumn="1">
			<emp:select id="LmtApply.prd_id" label="授信类别"  dictname="STD_LMT_PROJ_TYPE" required="true" /> 
		</emp:gridLayout> 
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" />
			<emp:button id="reset" label="重置" />
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>