<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isRpsscnt = "";
	if(context.containsKey("isRpsscnt")){
		isRpsscnt = (String)context.getDataValue("isRpsscnt");
	} 
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doViewCtrAssetstrsfCont() {
		var paramStr = CtrAssetstrsfContList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrAssetstrsfContViewPage.do"/>?pvp=pvp&op=view&menuIdTab=queryCtrAssetstrsfHistoryList&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,"viewCtrAssetstrsfCont","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=1,location=no,status=no");
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpAssetstrsf() {
		var paramStr = IqpAssetstrsfList._obj.getParamStr(['serno','asset_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetstrsfViewPage.do"/>?flag=notHave&op=view&menuIdTab=queryIqpAssetstrsfHistoryList&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,"viewIqpAssetstrsf","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=1,location=no,status=no");
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	    <div align="left">
		   <emp:button id="viewCtrAssetstrsfCont" label="查看" op="view"/>
	    </div>
		<emp:table icollName="CtrAssetstrsfContList" pageMode="false" url="">
		   <emp:text id="cont_no" label="合同编号" />
		   <emp:text id="toorg_no" label="交易对手行号" />
		   <emp:text id="toorg_name" label="交易对手行名" />
		   <emp:text id="prd_id" label="产品名称" hidden="true"/>
		   <emp:text id="prd_id_displayname" label="产品名称" />		
		   <emp:text id="takeover_type" label="转让方式" dictname="STD_ZB_TAKEOVER_MODE" />		
		   <emp:text id="asset_total_amt" label="资产总额" dataType="Currency"/>
		   <emp:text id="takeover_total_amt" label="转让金额" dataType="Currency"/>
		   <emp:text id="takeover_date" label="转让日期" />
		   <emp:text id="manager_br_id_displayname" label="管理机构" />
		   <emp:text id="input_id_displayname" label="登记人" />
		   <emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		   <emp:text id="input_br_id" label="登记机构" hidden="true"/>
		   <emp:text id="input_date" label="登记日期" hidden="true"/>
		   <emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
		   <emp:text id="serno" label="业务编号" hidden="true"/>
		   <emp:text id="asset_no" label="资产包编号" hidden="true"/>
		   <emp:text id="acct_curr" label="转让币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
	    </emp:table>
	    <br>
	    <div align="left">
		   <emp:button id="viewIqpAssetstrsf" label="查看" />
	    </div>
	    <emp:table icollName="IqpAssetstrsfList" pageMode="false" url="">
		  <emp:text id="serno" label="业务编号" />
		  <emp:text id="asset_no" label="资产包编号" hidden="true"/>		
		  <emp:text id="toorg_no" label="交易对手行号" />
		  <emp:text id="toorg_name" label="交易对手行名" />
		  <emp:text id="prd_id" label="产品名称" hidden="true"/>
		  <emp:text id="prd_id_displayname" label="产品名称" />
		  <emp:text id="takeover_type" label="转让方式" dictname="STD_ZB_TAKEOVER_MODE" />
		  <emp:text id="asset_total_amt" label="资产总额" dataType="Currency"/>
		  <emp:text id="takeover_total_amt" label="转让金额" dataType="Currency"/>
		  <emp:text id="takeover_date" label="转让日期" />
		  <emp:text id="takeover_int" label="转让利息" hidden="true"/>
		  <emp:text id="manager_br_id_displayname" label="管理机构" />
		  <emp:text id="input_id_displayname" label="登记人" />
	  	  <emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		  <emp:text id="input_date" label="登记日期" />
		  <emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		  <emp:text id="prd_id" label="产品编号"  hidden="true" />
	    </emp:table>
</body>
</html>
</emp:page>
    