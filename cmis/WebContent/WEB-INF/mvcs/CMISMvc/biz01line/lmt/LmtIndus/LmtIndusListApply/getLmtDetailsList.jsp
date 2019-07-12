<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_getAddLmtAppDetailsPage').style.display = 'none';
			document.getElementById('button_getUpdLmtAppDetailsPage').style.display = 'none';
			document.getElementById('button_deleteLmtAppDetails').style.display = 'none';
		}
	}
	//新增分项信息
	function doGetAddLmtAppDetailsPage(){
		var url = '<emp:url action="getLmtAppDetailsJointAddPage.do"/>?serno=${context.serno}&belg_line=&cus_id=${context.cus_id}&type=04';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	//修改分项信息
	function doGetUpdLmtAppDetailsPage() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppDetailsJointUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
		//	window.location = url;
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//删除分项信息
	function doDeleteLmtAppDetails(){
		if(confirm("是否确认要删除？")){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("删除成功！");
						window.location.reload();
					}else {
						alert("删除失败！");
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
			var limit_code = LmtAppDetailsList._obj.getSelectedData()[0].limit_code._getValue();
			var url = '<emp:url action="deleteLmtAppDetailsJointRecord.do"/>&limit_code='+ limit_code;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
	};
	//查看分项信息
	function doViewLmtAppDetails() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code','sub_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppDetailsJointViewPage.do"/>?'+paramStr;//+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()" >

	<div align="left">
		<emp:button id="getAddLmtAppDetailsPage" label="新增"/>
		<emp:button id="getUpdLmtAppDetailsPage" label="修改" />
		<emp:button id="deleteLmtAppDetails" label="删除" />
		<emp:button id="viewLmtAppDetails" label="查看" />
	</div>

	<emp:table icollName="LmtAppDetailsList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额(元)" dataType="Currency"/>
		<emp:text id="term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="term" label="授信期限" />
	</emp:table>
	
</body>
</html>
</emp:page>