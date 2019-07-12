<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//返回主管机构
function getOrganName(data){
	WfiLvOverdrawnRight.org_id._setValue(data.organno._getValue());
	WfiLvOverdrawnRight.org_id_displayname._setValue(data.organname._getValue());
};

function doInitCommBranchOverdrawnRight() {
	
 if(!WfiLvOverdrawnRight._checkAll()){
	 return false;
 }else{
	var form = document.getElementById("submitForm");
	WfiLvOverdrawnRight._toForm(form);
	if(confirm("初始化分支行透支额度配置可能覆盖原配置信息，是否继续？")){
		var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert(e.message);
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						alert('初始化透支配置成功！');
						window.opener.location.reload();
						doClose();
					}else{
						alert('初始化透支配置失败!');
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("初始化失败，请联系管理员！");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
			
		}
   }
};

function doClose(){
	 window.close();
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
<body class="page_content" onload="onLoad()" >
	
	<emp:form id="submitForm" action="initWfiLvOverdrawnRightRecord.do" method="POST">
		
		<emp:gridLayout id="WfiLvOverdrawnRightGroup" title="初始化机构透支额度信息" maxColumn="2">
			<emp:text id="WfiLvOverdrawnRight.pk_id" label="主键" maxlength="36" readonly="flase" required="false" hidden="true" colSpan="2"/>
			<emp:text id="WfiLvOverdrawnRight.org_id" label="机构码" maxlength="20" required="true" readonly="true" />
			<emp:text id="WfiLvOverdrawnRight.org_id_displayname" label="机构名称" required="true" readonly="true"  />
			<emp:select id="WfiLvOverdrawnRight.belg_line" label="所属条线"  required="true" dictname="STD_ZB_BUSILINE" readonly="true" />
			<emp:select id="WfiLvOverdrawnRight.is_inuse" label="是否控制透支额度"  required="true" dictname="STD_ZX_YES_NO" onchange="changeIsInuse()" />
			<emp:text id="WfiLvOverdrawnRight.overdrawn_amt" label="透支额度金额" maxlength="16" dataType="Currency"  required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="WfiLvOverdrawnRightGroup" title="初始化单户限额信息" maxColumn="2">
			<emp:select id="WfiLvOverdrawnRight.is_ctrl" label="是否控制单户限额"  required="true" dictname="STD_ZX_YES_NO" colSpan="2" onchange="changeCtrl()" />
			<emp:text id="WfiLvOverdrawnRight.pledge_amt" label="质押限额" maxlength="16" dataType="Currency"  required="true"/>
			<emp:text id="WfiLvOverdrawnRight.impawn_amt" label="抵押限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.fullpledge_amt" label="准全额质押限额" maxlength="16" dataType="Currency"  required="true"/>
			<emp:text id="WfiLvOverdrawnRight.riskpledge_amt" label="低风险质押限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.guarantee_amt" label="保证限额" maxlength="16" dataType="Currency"  required="true"  />
			<emp:text id="WfiLvOverdrawnRight.credit_amt" label="信用限额" maxlength="16" dataType="Currency"  required="true"  />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="initCommBranchOverdrawnRight" label="确定" op="init"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

