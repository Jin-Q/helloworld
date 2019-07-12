<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAbsProAppList.do"/>&biz_type=${context.biz_type}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doOnLoad(){ 
		IqpAbsProApp.batch_no._obj.addOneButton("batch_no","查看",doViewIqpAbsBatchMng);
		IqpAbsProApp.pack_qnt._obj.addOneButton("pack_qnt","试算",getIqpAbsProCount);
	}
	function doViewIqpAbsBatchMng() {
		var batch_no = IqpAbsProApp.batch_no._getValue();
		var url = "<emp:url action='getIqpAbsBatchMngViewPage.do'/>&flag=view&batch_no="+batch_no;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'cus_window','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function getIqpAbsProCount(){
	 	var pre_package_serno = IqpAbsProApp.pre_package_serno._getValue();
	 	if(pre_package_serno ==null || pre_package_serno ==''){
			return;
		}
		var param = "&pre_package_serno="+pre_package_serno;
		if(pre_package_serno != null && pre_package_serno != ""){
			var url = '<emp:url action="getIqpAbsProCount.do"/>'+param;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					var qnt = jsonstr.qnt;
					var amt = jsonstr.amt;
					if(flag == "success"){
						IqpAbsProApp.pack_qnt._setValue(qnt);
						IqpAbsProApp.pack_amt._setValue(amt); 
					}else {
						alert(msg);
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">
	<emp:gridLayout id="IqpAbsProAppGroup" title="证券化预封包申请表" maxColumn="2">
			<emp:text id="IqpAbsProApp.batch_no" label="批次号" maxlength="18" required="true" colSpan="2"/>
			<emp:text id="IqpAbsProApp.pre_package_serno" label="预封包流水号" maxlength="80" required="true" readonly="true" />
			<emp:text id="IqpAbsProApp.pre_package_name" label="预封包名称" maxlength="50" required="false" />
			<emp:text id="IqpAbsProApp.pre_package_date" label="预封包日期" maxlength="10" required="false" />
			<emp:text id="IqpAbsProApp.pack_qnt" label="总笔数" required="false" defvalue="0" />
			<emp:text id="IqpAbsProApp.pack_amt" label="总金额"  required="false" defvalue="0"/>
			<emp:text id="IqpAbsProApp.input_id" label="操作人员" required="false" />
			<emp:text id="IqpAbsProApp.input_br_id" label="操作机构" required="false" />
			<emp:text id="IqpAbsProApp.update_date" label="修改日期" required="false" />
			<emp:text id="IqpAbsProApp.prc_status" label="处理状态" hidden="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
	</emp:tab>
	<emp:tab label="明细信息" id="proListtab" needFlush="true" url="queryIqpAbsProListList.do?batch_no=${context.IqpAbsProApp.batch_no}&pre_package_serno=${context.IqpAbsProApp.pre_package_serno}&op=view"></emp:tab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
