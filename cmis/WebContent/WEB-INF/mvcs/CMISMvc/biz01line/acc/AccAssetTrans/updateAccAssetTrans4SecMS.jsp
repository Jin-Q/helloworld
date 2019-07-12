<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kc = (KeyedCollection)context.getDataElement("AccAssetTrans");
	String asset_type="";
	if(kc.containsKey("asset_type")){
		asset_type =(String)kc.getDataValue("asset_type");
	}  
%>

<%@page import="com.ecc.emp.data.KeyedCollection"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryAccAssetTransList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function load(){
		AccAssetTrans.bill_no._obj.addOneButton("bill_no","查看",getBill);
		AccAssetTrans.cont_no._obj.addOneButton("cont_no","查看",getCont);
	};

	function getCont(){
		var cont_no = AccAssetTrans.cont_no._getValue();
		var asset_type = AccAssetTrans.asset_type._getValue();
		var url;
		if(asset_type == '01'){
			url = '<emp:url action="getCtrAssetTransContViewPage.do"/>?cont_no='+cont_no+"&menuId=ZCLZHTCX&op=view&isHistory=history&flag=notHave";
		}else{
			url = '<emp:url action="getCtrAssetProContViewPage.do"/>?cont_no='+cont_no+"&menuIdTab=dqzczqhxm&op=view&isHistory=history&flag=notHave";
		}
		
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};

	function getBill(){
		var bill_no = AccAssetTrans.bill_no._getValue();
		var url = '<emp:url action="getAccLoanViewPage.do"/>?bill_no='+bill_no+"&isHaveButton=not";
		
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	
	function doSave(){
		var form = document.getElementById("submitForm");
		if(AccAssetTrans._checkAll()){
			AccAssetTrans._toForm(form);
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
						alert("保存成功!");
						window.location.reload();
					}else {
						alert("保存异常!");
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
			var postData = YAHOO.util.Connect.setForm(form);
			var url ='<emp:url action ="updateAccAssetTransRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
		}else {
			return false;
		}
	};
</script>
</head>
<body class="page_content" onload="load();">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	<emp:tab label="台账信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="AccAssetTransGroup" title="资产台账" maxColumn="2">
			<emp:text id="AccAssetTrans.bill_no" label="借据编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="AccAssetTrans.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="AccAssetTrans.ori_bill_no" label="原借据编号" maxlength="40" required="false" readonly="true"/>
			<emp:select id="AccAssetTrans.asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSETTRANS_TYPE" readonly="true"/>
			<emp:date id="AccAssetTrans.rebuy_date" label="赎回日期" required="false" readonly="true"/>
			<emp:select id="AccAssetTrans.acc_status" label="资产状态" required="false" dictname="STD_ZB_ACC_TRANS_STATUS" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" title="登记信息" maxColumn="2">
			<emp:text id="AccAssetTrans.manager_br_id_displayname" label="管理机构" required="false" readonly="true"/>
			<emp:text id="AccAssetTrans.fina_br_id_displayname" label="账务机构" required="false" readonly="true"/>
			<emp:text id="AccAssetTrans.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true" readonly="true"/>
			<emp:text id="AccAssetTrans.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true" readonly="true"/>
		</emp:gridLayout>
		<emp:form id="submitForm" action="#" method="POST">
			<emp:gridLayout id="" title="证券化专管员维护信息" maxColumn="2">
				<emp:select id="AccAssetTrans.is_default_return" label="是否为违约回收" required="true" dictname="STD_ZX_YES_NO"/>
				<emp:select id="AccAssetTrans.is_bad_asset_redem" label="是否为不合格资产赎回" required="true" dictname="STD_ZX_YES_NO"/>
				<emp:select id="AccAssetTrans.is_clearance_redem" label="是否为清仓赎回"  required="true" dictname="STD_ZX_YES_NO"/>
			</emp:gridLayout>
			<div align="center">
			<br>
				<emp:button id="save" label="保存" />
			</div>
		</emp:form>		
	</emp:tab>
	<%if(asset_type.equals("01")){ %>
	<emp:tab label="资产流转合同信息" id="subTab" url="getCtrAssetTransContViewPage.do?cont_no=${context.AccAssetTrans.cont_no}&menuIdTab=ZCLZHTCX&op=view&flag=notHave" initial="false" needFlush="true"/>
	<%}else{ %>
	<emp:tab label="资产证券化项目信息" id="subTab" url="getCtrAssetProContViewPage.do?cont_no=${context.AccAssetTrans.cont_no}&menuIdTab=dqzczqhxm&op=view&flag=notHave" initial="false" needFlush="true"/>
	<%} %>
	<emp:tab label="原台账信息" id="subTab1" url="getAccLoanViewPage.do?bill_no=${context.AccAssetTrans.ori_bill_no}&isHaveButton=not" initial="false" needFlush="true"/> 
	<emp:tab label="新台账信息" id="subTab2" url="getAccLoanViewPage.do?bill_no=${context.AccAssetTrans.bill_no}&isHaveButton=not" initial="false" needFlush="true"/>
    </emp:tabGroup>
    
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
