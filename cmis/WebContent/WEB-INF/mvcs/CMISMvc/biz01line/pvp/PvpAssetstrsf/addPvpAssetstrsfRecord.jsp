<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
	function doSelect(){
		var data = CtrAssetstrsfContList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var cont_no = data[0].cont_no._getValue(); 
			var form = document.getElementById("submitForm"); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var schemeid = jsonstr.schemeid;
					if(flag == "success"){
						doSave();
					}else {
						alert("存在在途的出账申请！");
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

			var url = '<emp:url action="queryIsConfPvpForAssetstrsf.do"/>?menuId=queryPvpAssetstrsf&cont_no='+cont_no;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
		} else {
			alert('请先选择一条记录！');
		}
	};	

	function doSave(){
		var data = CtrAssetstrsfContList._obj.getSelectedData();
		var cont_no = data[0].cont_no._getValue(); 
		var form = document.getElementById("submitForm"); 
		data[0]._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag == "success"){
					url = '<emp:url action="getPvpAssetstrsfUpdatePage.do"/>?op=update&cont_no='+cont_no+'&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("异步请求出错！");
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

		var url = '<emp:url action="addPvpAssetstrsfRecord.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<div  class='emp_gridlayout_title'>新增出账 </div> 
	<emp:form id="submitForm" action="" method="POST"></emp:form>
		<emp:table icollName="CtrAssetstrsfContList" pageMode="true" url="pageAddPvpLoanApp.do">
		   <emp:text id="serno" label="业务编号" hidden="true"/>
           <emp:text id="cont_no" label="合同编号" />
            <emp:text id="toorg_name" label="交易对手行名" />
		   <emp:text id="prd_id_displayname" label="产品名称" /> 
		   <emp:text id="takeover_type" label="转让方式" dictname="STD_ZB_TAKEOVER_MODE" />
		   <emp:text id="asset_total_amt" label="资产总额" dataType="Currency"/>
		   <emp:text id="takeover_total_amt" label="转让金额" dataType="Currency"/>
		   <emp:text id="takeover_date" label="转让日期" /> 
		   <emp:text id="input_id_displayname" label="登记人"/>
		   <emp:text id="input_id" label="登记人" hidden="true"/>
		   <emp:text id="manager_br_id_displayname" label="管理机构" />
		   <emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		   <emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
		   <emp:text id="input_br_id" label="登记机构" maxlength="20" required="false"  hidden="true"/>
		   <emp:date id="input_date" label="登记日期" required="false" hidden="true"/>
		    <emp:text id="asset_no" label="资产包编号" hidden="true"/>
		    <emp:text id="prd_id" label="产品名称" hidden="true"/> 
		</emp:table>
		<div align="center">
			<br>
			<emp:button id="select" label="下一步" />
		</div>
	
</body>
</html>
</emp:page>

