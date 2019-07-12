<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusSubmitInfo._toForm(form);
		CusSubmitInfoList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = CusSubmitInfoList._obj.getSelectedData()[0];
		var cus_id=selObj.cus_id._getValue();
		var url = '<emp:url action="checkCusTypeById.do"/>?cusId='+cus_id;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){ 
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("获取客户信息失败!");
					return;
				}
				var flag=jsonstr.flag;
				var cusUrl = "";
				if(flag=="com"){//对公
					var oper = "infotree";
					cusUrl = '<emp:url action="getCusComViewPage.do"/>?cus_id='+cus_id+"&flag=edit&oper="+oper+"&suppflag=supp&tempflag=type";
				}else{//对私
					cusUrl = '<emp:url action="getCusIndivViewPage.do"/>&cus_id='+cus_id+"&info=tree&flag=edit&treeflag=infotree&repBtn=yes";
				}
				cusUrl = EMPTools.encodeURI(cusUrl);
				window.open(cusUrl,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}
		};
		var handleFailure = function(o){ 
			alert("获取客户信息失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	};

	//查看更多
	function doSelMore(){
		var url = '<emp:url action="queryCusSubmitInfoList.do"/>?';
		url = EMPTools.encodeURI(url);
		//window.location = url;
		window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function doHandOverPop(){
		//选择一个集中录入岗用户
		var selObj = CusSubmitInfoList._obj.getSelectedData()[0];
		var cusId=selObj.cus_id._getValue();
		var url = '<emp:url action="handOverCusSubmitInfoPage.do"/>?' + "cus_id=" + cusId;
	    url = EMPTools.encodeURI(url);
	    window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	//查看详情
	function doViewCusSubmitInfo() {
		var paramStr = CusSubmitInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusSubmitInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		//	window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:table icollName="CusSubmitInfoList" pageMode="false" url="" >
		<emp:text id="serno" label="流水号" hidden="true"/>
		<emp:link id="cus_id" label="客户码" operation="viewCusSubmitInfo"/>
		<emp:link id="cus_id_displayname" label="客户名称" operation="viewCusSubmitInfo"/> 
		<emp:link id="todo" label="补录" operation="sel" defvalue="待补录" hidden="true"/>
		<emp:link id="handover" label="移交" operation="handOverPop" defvalue="移交"/>
	</emp:table>
	
	<emp:link id="serno" label=" " operation=" "  opName="点击查看更多...." onclick="doSelMore()" ></emp:link>

</body>
</html>
</emp:page>
    