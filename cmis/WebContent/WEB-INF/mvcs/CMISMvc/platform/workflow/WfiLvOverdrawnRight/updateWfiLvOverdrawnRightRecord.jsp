<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		var url = '<emp:url action="queryWfiLvOverdrawnRightList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}			

	function doUpdateWLORight(){
		 if(!WfiLvOverdrawnRight._checkAll()){
			 return false;
		 }else{
			var form = document.getElementById("submitForm");
			WfiLvOverdrawnRight._toForm(form);
			var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var msg = jsonstr.flag;
				if(msg=='success'){
		            alert('修改成功!');
		            window.location.reload();
				}else{
					alert('修改失败!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("修改失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	  }
	};
	function onLoad(){
		changeCtrl();
		changeIsInuse();
	}

	function changeCtrl(){
		var isCtrl=WfiLvOverdrawnRight.is_ctrl._getValue();
		if("1"==isCtrl){
			WfiLvOverdrawnRight.pledge_amt._obj._renderHidden(false) ;
			WfiLvOverdrawnRight.impawn_amt._obj._renderHidden(false) ;
			WfiLvOverdrawnRight.fullpledge_amt._obj._renderHidden(false) ;
			WfiLvOverdrawnRight.riskpledge_amt._obj._renderHidden(false) ;
			WfiLvOverdrawnRight.guarantee_amt._obj._renderHidden(false) ;
			WfiLvOverdrawnRight.credit_amt._obj._renderHidden(false) ;
			
			WfiLvOverdrawnRight.pledge_amt._obj._renderRequired(true);
			WfiLvOverdrawnRight.impawn_amt._obj._renderRequired(true);
			WfiLvOverdrawnRight.fullpledge_amt._obj._renderRequired(true);
			WfiLvOverdrawnRight.riskpledge_amt._obj._renderRequired(true);
			WfiLvOverdrawnRight.guarantee_amt._obj._renderRequired(true);
			WfiLvOverdrawnRight.credit_amt._obj._renderRequired(true);
		}else if("2"==isCtrl){
			WfiLvOverdrawnRight.pledge_amt._obj._renderHidden(true) ;
			WfiLvOverdrawnRight.impawn_amt._obj._renderHidden(true) ;
			WfiLvOverdrawnRight.fullpledge_amt._obj._renderHidden(true) ;
			WfiLvOverdrawnRight.riskpledge_amt._obj._renderHidden(true) ;
			WfiLvOverdrawnRight.guarantee_amt._obj._renderHidden(true) ;
			WfiLvOverdrawnRight.credit_amt._obj._renderHidden(true) ;
			
			WfiLvOverdrawnRight.pledge_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.impawn_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.fullpledge_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.riskpledge_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.guarantee_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.credit_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.pledge_amt._setValue("");
			WfiLvOverdrawnRight.impawn_amt._setValue("");
			WfiLvOverdrawnRight.fullpledge_amt._setValue("");
			WfiLvOverdrawnRight.riskpledge_amt._setValue("");
			WfiLvOverdrawnRight.guarantee_amt._setValue("");
			WfiLvOverdrawnRight.credit_amt._setValue("");
	
		}
		
	}
	function changeIsInuse(){
		var isInuse=WfiLvOverdrawnRight.is_inuse._getValue();
		if("1"==isInuse){
			WfiLvOverdrawnRight.overdrawn_amt._obj._renderHidden(false) ;
			WfiLvOverdrawnRight.overdrawn_amt._obj._renderRequired(true);
		}else if("2"==isInuse){
			WfiLvOverdrawnRight.overdrawn_amt._obj._renderHidden(true) ;
			WfiLvOverdrawnRight.overdrawn_amt._obj._renderRequired(false);
			WfiLvOverdrawnRight.overdrawn_amt._setValue("");
		}
		
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:form id="submitForm" action="updateWfiLvOverdrawnRightRecord.do" method="POST">
		<emp:gridLayout id="WfiLvOverdrawnRightGroup" maxColumn="2" title="初始化机构透支额度信息">
			<emp:text id="WfiLvOverdrawnRight.pk_id" label="主键"  required="true" readonly="true" hidden="true" colSpan="2" />
			<emp:text id="WfiLvOverdrawnRight.org_id" label="机构码"  readonly="true"  required="false" />
			<emp:text id="WfiLvOverdrawnRight.org_id_displayname" label="机构名称"  readonly="true"  required="false" />
			<emp:select id="WfiLvOverdrawnRight.belg_line" label="所属条线"  required="true" dictname="STD_ZB_BUSILINE" readonly="true" />
			<emp:select id="WfiLvOverdrawnRight.is_inuse" label="是否控制透支额度"  required="true" dictname="STD_ZX_YES_NO" onchange="changeIsInuse()"  />
			<emp:text id="WfiLvOverdrawnRight.overdrawn_amt" label="透支额度金额" maxlength="16" dataType="Currency"  required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="WfiLvOverdrawnRightGroup" title="初始化单户限额信息" maxColumn="2">
			<emp:select id="WfiLvOverdrawnRight.is_ctrl" label="是否控制单户限额"  required="true" dictname="STD_ZX_YES_NO" onchange="changeCtrl()" colSpan="2" />
			<emp:text id="WfiLvOverdrawnRight.pledge_amt" label="质押限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.impawn_amt" label="抵押限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.fullpledge_amt" label="准全额质押限额" maxlength="16" dataType="Currency"  required="true"/>
			<emp:text id="WfiLvOverdrawnRight.riskpledge_amt" label="低风险质押限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.guarantee_amt" label="保证限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.credit_amt" label="信用限额" maxlength="16" dataType="Currency"  required="true"  />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateWLORight" label="修改" op="update"/>
			<emp:button id="return" label="返回列表页面"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
