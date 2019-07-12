<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	String serno = request.getParameter("serno");
%>
<emp:page>

<html>
<head>
<title>批次引入页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBatchMng._toForm(form);
		IqpBatchMngList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.IqpBatchMngGroup.reset();
	};
	function doImportIqpBatch(){
		var data = IqpBatchMngList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		//获取选择的批次号
		var batch_no = data[0].batch_no._getValue();
		if(batch_no != null){
			//获取页面调用传过来的业务编号参数
			var serno = '<%=serno%>';
			if(serno != null && serno != ""){
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
						if(flag == "success"){  
							alert("引入成功!");
							window.location.reload();
							window.opener.location.reload();
							window.close();
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
				var url = '<emp:url action="importIqpBatchAction.do"/>?serno=<%=serno%>&batch_no='+batch_no;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)  

			}else{//业务编号为空时，说明是转贴现页面调用了批次引入页面。
				window.opener.IqpRpddscnt.batch_no._setValue(batch_no);
			    window.opener.batchnoChange();  
				window.close();
			}
			
		}else {
			alert('请输入引入批次号！');
		}
	}

	function doSelect(){
		//var methodName="${context.popReturnMethod}";
		doImportIqpBatch();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBatchMngGroup" title="输入引入批次号" maxColumn="2">
			<emp:text id="IqpBatchMng.batch_no" label="批次号" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="importIqpBatch" label="引入" />
		
	</div>
	
	<emp:table icollName="IqpBatchMngList" pageMode="true" url="pageImportIqpBatchMngQuery.do?prd_id=${context.prd_id}&restrictUsed=${context.restrictUsed}">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
		<emp:text id="biz_type" label="暂存用途" dictname="STD_ZB_BUSI_TYPE"/>
		<emp:text id="bill_qnt" label="票据数量" />
		<emp:text id="bill_total_amt" label="票据总金额" dataType="Currency"/>
		<emp:text id="rpay_amt" label="实付金额" dataType="Currency"/>
		<emp:text id="manager_id_displayname" label="责任人" hidden="true"/>
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="status" label="状态" dictname="STD_BATCH_NUM_STATUS"/>
	</emp:table>
</body>
</html>
</emp:page>
    