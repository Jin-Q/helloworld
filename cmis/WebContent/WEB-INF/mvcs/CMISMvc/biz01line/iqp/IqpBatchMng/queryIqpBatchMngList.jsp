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
		IqpBatchMng._toForm(form);
		IqpBatchMngList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBatchMngPage() {
		var status = IqpBatchMngList._obj.getParamValue(['status']);
		if(status!='01'){
			alert('批次包已被引用，不能进行修改！');
			return;
		}
		
		var paramStr = IqpBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpActTabHelp.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBatchMng() {
		var paramStr = IqpBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBatchMngViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBatchMngPage() {
		var url = '<emp:url action="getIqpBatchMngAddPage.do"/>?op=update';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBatchMng() {
		var status = IqpBatchMngList._obj.getParamValue(['status']);
		if(status!='01'){
			alert('批次包已被引用，不能进行删除！');
			return;
		}
		var paramStr = IqpBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							window.location.reload();
						}else {
							alert("发生异常!"); 
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
				var url = '<emp:url action="deleteIqpBatchMngRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBatchMngGroup.reset();
	};

	/**已换成状态控制
	function doCheckIqpBatchMngForUpdate(){//目前仅校验是否被业务引用
		var data = IqpBatchMngList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		var batch_no = data[0].batch_no._getValue();
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
					doGetUpdateIqpBatchMngPage();//进行修改
				}else {
					alert(msg);
					return;
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

		var url="<emp:url action='checkIqpBatchMng.do'/>?batch_no="+batch_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	};

	function doCheckIqpBatchMngForDel(){//目前仅校验是否被业务引用
		var data = IqpBatchMngList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		var batch_no = data[0].batch_no._getValue();
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
					doDeleteIqpBatchMng();//进行修改
				}else {
					alert(msg);
					return;
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

		var url="<emp:url action='checkIqpBatchMng.do'/>?batch_no="+batch_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	};*/

	function getOrgID(data){
		IqpBatchMng.manager_br_id._setValue(data.organno._getValue());
		IqpBatchMng.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function setconId(data){
		IqpBatchMng.manager_id._setValue(data.actorno._getValue());
		IqpBatchMng.manager_id_displayname._setValue(data.actorname._getValue());
	};

	//-------------对手行行号pop框选择返回函数-----------
    function getOrgNo(data){
    	IqpBatchMng.opp_org_no._setValue(data.bank_no._getValue());
    	IqpBatchMng.opp_org_name._setValue(data.bank_name._getValue());
    };

    /** 电票修改入口 **/
	function doGetUpdateIqpBatchMngIsEbill(){
		var paramStr = IqpBatchMngList._obj.getParamStr(['batch_no']); 
		if (paramStr != null) {
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var checked = jsonstr.checked;
					if("1" == checked){//追加保证金
						var status = IqpBatchMngList._obj.getParamValue(['status']);
						if(status == '02'){
							var biz_type = IqpBatchMngList._obj.getParamValue(['biz_type']);
							if(biz_type == '07'){
								alert("只有转贴现票据才能修改");
							}else{
								var url = '<emp:url action="getIqpBatchMngUpdatePage.do"/>?op=update&updateflag=ebill&'+paramStr;
								url = EMPTools.encodeURI(url);
								window.location = url;
							}
						}else{
							alert("只有状态为'已引用'才能修改");
						}
					}else {
						alert("只有电票业务才能修改");	
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			
			var url = '<emp:url action="checkIqpBatchMngIsEbill.do" />?'+paramStr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBatchMngGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpBatchMng.batch_no" label="批次号" />
			<emp:pop id="IqpBatchMng.opp_org_name" label="对手行行名" url="getPrdBankInfoPopList.do" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
			<emp:select id="IqpBatchMng.bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
			<emp:select id="IqpBatchMng.biz_type" label="暂存用途" dictname="STD_ZB_BUSI_TYPE"/>
			<emp:select id="IqpBatchMng.status" label="状态" dictname="STD_BATCH_NUM_STATUS"/>
			<emp:pop id="IqpBatchMng.manager_br_id_displayname" label="登记机构" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:date id="IqpBatchMng.input_date_begin" label="登记日期从" />
			<emp:date id="IqpBatchMng.input_date_end" label="登记日期到" />
			
			<emp:text id="IqpBatchMng.manager_id" label="责任人" hidden="true"/>
			<emp:text id="IqpBatchMng.manager_br_id" label="责任机构" hidden="true"/>
			<emp:text id="IqpBatchMng.opp_org_no" label="对手行行名" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpBatchMngPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBatchMngPage" label="修改" op="update"/>
		<emp:button id="getUpdateIqpBatchMngIsEbill" label="电票修改" op="e_update"/>
		<emp:button id="deleteIqpBatchMng" label="删除" op="remove"/>
		<emp:button id="viewIqpBatchMng" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpBatchMngList" pageMode="true" url="pageIqpBatchMngQuery.do">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
		<emp:text id="biz_type" label="暂存用途" dictname="STD_ZB_BUSI_TYPE"/>
		<emp:text id="bill_qnt" label="票据数量" />
		<emp:text id="bill_total_amt" label="票据总金额" dataType="Currency"/>
		<emp:text id="opp_org_name" label="对手行行名" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_BATCH_NUM_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    