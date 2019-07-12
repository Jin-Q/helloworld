<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>

<html>
<head>
<title>还款方式列表页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdRepayMode._toForm(form);
		PrdRepayModeList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.PrdRepayModeGroup.reset();
	};

	function doSelect(){
		doReturnMethod();
	}
	
	function doReturnMethod(){
		var data = PrdRepayModeList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择引入项");
		}else {
			var repay_mode_id = data[0].repay_mode_id._getValue();
			var url = '<emp:url action="importPrdRepaymodeRel.do"/>?prdid=${context.prdid}&repay_mode_id='+repay_mode_id;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("引入成功!");
						window.location.reload();
						window.opener.queryList();
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdRepayModeGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="PrdRepayMode.repay_mode_type" dictname="STD_ZB_REPAY_MODE" label="还款方式种类" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:returnButton id="s1" label="引入"/>
	</div>

	<emp:table icollName="PrdRepayModeList" pageMode="true" url="pagePrdRepayModePop.do" reqParams="prdid=${context.prdid}">
		<emp:text id="repay_mode_id" label="还款方式代码" />
		<emp:text id="repay_mode_type" label="还款方式种类" dictname="STD_ZB_REPAY_MODE" />  
		<emp:text id="min_term" label="支持最小期限(月)" />
		<emp:text id="max_term" label="支持最大期限(月)" />
		<emp:text id="repay_interval" label="还款间隔" hidden="true" />
		<emp:text id="firstpay_perc" label="首付比例" dataType="Rate"/>
		<emp:text id="lastpay_perc" label="尾付比例" dataType="Rate"/>
		<emp:text id="is_instm" label="是否期供类" dictname="STD_ZX_YES_NO" />
		<emp:text id="repay_mode_dec" label="还款方式描述" />
	</emp:table>
</body>
</html>
</emp:page>
    