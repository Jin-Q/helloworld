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
		var data = CtrRpddscntContList._obj.getSelectedData();
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
			var url = '<emp:url action="queryIsConfPvpForRpddscnt.do"/>?menuId=PvpRpddscant&cont_no='+cont_no;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
		} else {
			alert('请先选择一条记录！');
		}
	};	

	function doSave(){
		//added by yagnzy 20140829 放款改造  begin
		var buttonObjWait = document.getElementById('button_select');
		buttonObjWait.disabled=true;
		buttonObjWait.innerHTML='请稍等..';
		//added by yagnzy 20140829 放款改造  end
		var data = CtrRpddscntContList._obj.getSelectedData();
		var cont_no = data[0].cont_no._getValue(); 
		var form = document.getElementById("submitForm"); 
		data[0]._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					//added by yangzy 20140829 放款改造  begin
					buttonObjWait.disabled=false;
					buttonObjWait.innerHTML='下一步';
					//added by yangzy 20140829 放款改造  end 
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag == "success"){
					url = '<emp:url action="getPvpRpddscantUpdatePage.do"/>?op=update&serno='+serno+'&cont_no='+cont_no;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					//added by yangzy 20140829 放款改造  begin
					buttonObjWait.disabled=false;
					buttonObjWait.innerHTML='下一步';
					//added by yangzy 20140829 放款改造  end 
					alert("异步请求出错！");
				}
			}
		};
		var handleFailure = function(o){
			//added by yangzy 20140829 放款改造  begin
			buttonObjWait.disabled=false;
		    buttonObjWait.innerHTML='下一步';
			//added by yangzy 20140829 放款改造  end 
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		var url = '<emp:url action="addPvpRpddscantRecord.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<div  class='emp_gridlayout_title'>新增出账  </div> 
	<emp:form id="submitForm" action="" method="POST"></emp:form>
		<emp:table icollName="CtrRpddscntContList" pageMode="true" url="pageAddPvpLoanApp.do">
		   <emp:text id="serno" label="业务编号" hidden="true"/>
           <emp:text id="cont_no" label="合同编号" />
           <emp:text id="toorg_name" label="交易对手行名" />
		   <emp:text id="prd_id_displayname" label="产品名称" />
		   <emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE" />
		   <emp:text id="rpddscnt_type" label="转贴现方式" dictname="STD_ZB_BUSI_TYPE" />
		   <emp:text id="bill_total_amt" label="票据总金额" dataType="Currency"/>
		   <emp:text id="bill_qnt" label="票据数量" />
		   <emp:text id="rpay_amt" label="总实付金额" dataType="Currency"/>
		   <emp:text id="input_id_displayname" label="登记人"/>
		   <emp:text id="input_id" label="登记人" hidden="true"/>
		   <emp:text id="manager_br_id_displayname" label="管理机构" />
		   <emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		   <emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
		   <emp:text id="input_br_id" label="登记机构" maxlength="20" required="false"  hidden="true"/>
		   <emp:date id="input_date" label="登记日期" required="false"  hidden="true"/>
		   <emp:select id="approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" hidden="true"/>
		   <emp:text id="batch_no" label="批次号" hidden="true"/>
		   <emp:text id="prd_id" label="产品编号" hidden="true"/>
		</emp:table>
		<div align="center">
			<br>
			<emp:button id="select" label="下一步" />
		</div>
	
</body>
</html>
</emp:page>

