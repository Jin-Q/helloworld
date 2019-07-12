<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>集团成员设置额度引导页</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
<%
String group_serno = request.getParameter("group_serno");
String serno = request.getParameter("serno");
String cus_id = request.getParameter("cus_id");
String lmt_type = request.getParameter("lmt_type");
%>
/*--user code begin--*/
	//异步修改授信类别
    function doUpdateLine(){
   	 	var handleSuccess = function(o) {
  			if (o.responseText !== undefined) {
  				try {
  					var fistTest = eval("(" + o.responseText + ")");
  				} catch(e){
  					alert("异步执行不成功：" + e.message);
  					return;
  				}
  				var result = fistTest.flag;
  				if("success" == result){
  	  				alert("设置授信类别成功！");

	  	  			var serno ='<%=serno%>';
	  		    	var cus_id ='<%=cus_id%>';
	  		    	var lmt_type = LmtApply.lmt_type._getValue();
	  		    	var url = '<emp:url action="getLmtApplyUpdatePage.do"/>?&menuId=corp_crd_apply&op=update&isShow=N&type=surp&serno='+serno+"&cus_id="+cus_id+"&lmt_type="+lmt_type;   //控制 isShow=Y协议中不显示冻结解冻按钮
	  				url = EMPTools.encodeURI(url);
	  				window.location = url;
				}else{
					alert(fistTest.msg);
				}
  			}
   		};
  		var handleFailure = function(o) {
  			alert("与服务器交互失败，请联系管理员！");
  		};
  		var callback = {
  			success :handleSuccess,
  			failure :handleFailure
  		};
  		
  		var form = document.getElementById('submitForm');
  		var result = LmtApply._checkAll();
	    if(result){
	    	var url = '<emp:url action="updateLine4GrpMemberApp.do"/>?serno='+LmtApply.serno._getValue()+"&lmt_type="+LmtApply.lmt_type._getValue()+"&rd="+new Date();
	  		url = EMPTools.encodeURI(url);
	  		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	    }else {
		    alert("请检查各标签页面中的必填信息是否遗漏！");
		}
	}
	
	//返回
    function doReturn() {
		var serno ='<%=group_serno%>';
		url = '<emp:url action="queryLmtMemberAppList.do"/>?serno='+serno+'&menuId=grp_crd_apply&subMenuId=LmtMemberApp&op=update';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//初始化加载
	function onLoad(){
		var options = LmtApply.lmt_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "01" ||options[i].value == "03" || options[i].value == "04"){
				options.remove(i);
			}
		}

		var cus_id ='<%=cus_id%>';
		var serno ='<%=serno%>';
		var lmt_type ='<%=lmt_type%>';
		LmtApply.cus_id._setValue(cus_id);
		LmtApply.serno._setValue(serno);
		LmtApply.lmt_type._setValue(lmt_type);
	}
/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="getLmtApplyUpdatePage.do?op=update" method="POST">
		<emp:gridLayout id="LmtApplyGroup" title="设置集团成员授信类别" maxColumn="1">
			<emp:select id="LmtApply.lrisk_type" label="低风险业务类型" required="true" readonly="true" dictname="STD_ZB_LRISK_TYPE" defvalue="20"/>
			<emp:select id="LmtApply.lmt_type" label="授信类别" required="true" dictname="STD_ZX_LMT_PRD"/> 
			<emp:text id="LmtApply.serno" label="流水号" required="true" readonly="true" hidden="true"/> 
			<emp:text id="LmtApply.cus_id" label="客户码" required="true" readonly="true" hidden="true"/> 
		</emp:gridLayout> 
		<div align="center">
			<br>
			<emp:button id="updateLine" label="确定" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

