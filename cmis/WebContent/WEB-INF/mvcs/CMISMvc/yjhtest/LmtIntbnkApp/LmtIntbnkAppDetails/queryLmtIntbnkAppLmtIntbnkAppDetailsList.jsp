<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>瀛愯〃鍒楄〃鏌ヨ椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateLmtIntbnkAppDetailsPage() {
		var paramStr = LmtIntbnkAppDetailsList._obj.getParamStr(['serno','crd_item_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="getLmtIntbnkAppLmtIntbnkAppDetailsUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddLmtIntbnkAppDetailsPage(){
		var serno = window.parent.window.LmtIntbnkApp.serno._getValue();
		var url = '<emp:url action="getLmtIntbnkAppLmtIntbnkAppDetailsAddPage.do"/>?LmtIntbnkAppDetails.serno='+serno;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteLmtIntbnkAppDetails() {
		var paramStr = LmtIntbnkAppDetailsList._obj.getParamStr(['serno','crd_item_id']);
		if (paramStr != null) {
			if(confirm("鏄惁纭瑕佸垹闄わ紵")){
				var url = '<emp:url action="deleteLmtIntbnkAppLmtIntbnkAppDetailsRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	function doViewLmtIntbnkAppDetails() {
		var paramStr = LmtIntbnkAppDetailsList._obj.getParamStr(['serno','crd_item_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryLmtIntbnkAppLmtIntbnkAppDetailsDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">

	<div align="left">
		<emp:button id="getAddLmtIntbnkAppDetailsPage" label="鏂板" op="add_LmtIntbnkAppDetails"/>
		<emp:button id="getUpdateLmtIntbnkAppDetailsPage" label="淇敼" op="update_LmtIntbnkAppDetails"/>
		<emp:button id="deleteLmtIntbnkAppDetails" label="鍒犻櫎" op="remove_LmtIntbnkAppDetails"/>
		<emp:button id="viewLmtIntbnkAppDetails" label="鏌ョ湅" op="view_LmtIntbnkAppDetails"/>
	</div>
							
	<emp:table icollName="LmtIntbnkAppDetailsList" pageMode="false" url="pageLmtIntbnkAppLmtIntbnkAppDetailsQuery.do" reqParams="LmtIntbnkApp.serno=$LmtIntbnkApp.serno;">

		<emp:text id="serno" label="业务流水号" />
		<emp:text id="bank_no" label="同业机构行号" />
		<emp:text id="bank_name" label="同业机构名称" />
		<emp:text id="crd_item_id" label="授信明细" />
		<emp:text id="cur_type" label="币种" />
		<emp:text id="crd_lmt" label="授信额度" />
		<emp:text id="margin_ratio" label="保证金比例" />
		<emp:text id="mini_rate" label="最低利率" />
		<emp:text id="mini_fare" label="最低费率" />
	</emp:table>
				
</body>
</html>
</emp:page>