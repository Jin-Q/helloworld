<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
		//request = (HttpServletRequest) pageContext.getRequest();
		Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<% String crd_grade=(String)request.getParameter("crd_grade"); %>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusSameOrg._toForm(form);
		CusSameOrgList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusSameOrgGroup.reset();
	};

	//检查客户是否在批量包中
	function doSelect(){
		var data = CusSameOrgList._obj.getSelectedData();
		if(data.length==0){
			alert("至少选中一条记录！");
			return;
		}
		var cusIds = "";
		for(var i=0;i<data.length;i++){
			cusIds = cusIds + data[i].cus_id._getValue() + "$";
		}
   		var handleSuccess = function(o){ 		
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag =="success"){
					alert("引入成功！");				
					window.opener.location.reload();
					window.close();								
				}else{
					var cusArray = flag.split("&");//切割开cus_id
					if(cusArray[0]== "SigLmt"){
						alert("客户["+cusArray[1]+"]存在于单笔授信中，请重新选择！");
					} else{
						alert("客户["+cusArray[1]+"]已经存在于批量包中，请重新选择！");
					}
				}
			}
		};
		var handleFailure = function(o){
			alert("异步回调失败！");	
		};
		var url = '<emp:url action="checkCusInBatchRecord.do"/>?batch_cus_no=${context.batch_cus_no}&cus_id='+cusIds;
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
   	}
   	
	/*--user code begin--*/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="CusSameOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusSameOrg.same_org_no" label="同业机构(行)号" />
			<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" />
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<button id ="conn" onclick="doSelect()">引入</button>
		<emp:table icollName="CusSameOrgList" pageMode="true" url="getcusSameOrgPop4Batch.do?crd_grade=${context.crd_grade}" selectType="2">
		    <emp:text id="cus_id" label="客户码"/>
			<emp:text id="same_org_no" label="同业机构(行)号" />
			<emp:text id="same_org_cnname" label="同业机构(行)名称" />
			<emp:text id="crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE"/>
			<emp:text id="reg_cap_amt" label="注册/开办资金(万元)" dataType="Currency"/>
			<emp:text id="reg_no" label="登记注册号" hidden="true"/>
		</emp:table>
	<button id ="conn" onclick="doSelect()">引入</button>
</body>
</html>
</emp:page>
    