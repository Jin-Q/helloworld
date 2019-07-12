<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAbsProList._toForm(form);
		IqpAbsProListList._obj.ajaxQuery(null,form);
	};
	
	
	function doGetAddIqpAbsProListPage(){
		var url = '<emp:url action="queryAccLoanForAbsListPop.do"/>?pre_package_serno=${context.pre_package_serno}&batch_no=${context.batch_no}';
	    url = EMPTools.encodeURI(url);
	    var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		modifyWindow = window.open(url,'newWindow',param);
	};
	function doDeleteIqpAbsProList() {
		var data = IqpAbsProListList._obj.getSelectedData();
		var count = 0;
		if(data.length == 0){
			alert("请先选择删除项");
		}else {

			var subBillNoStr="";//编号字符串，以","间隔
			var num = IqpAbsProListList._obj.getSelectedData().length;
			if (num != 0) {
				for(var i=0;i<num;i++){
					count++;
					var deal_status = IqpAbsProListList._obj.getSelectedData()[i].deal_status._getValue();
					if(deal_status == "02"){
						alert("第"+count+"记录，【成功】状态的资产不能删除！");
						return;
				 	}
					subBillNoStr = subBillNoStr+IqpAbsProListList._obj.getSelectedData()[i].bill_no._getValue()+",";
				}
			}
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
						alert("删除成功!");
						window.location.reload();
					}else {
						alert("删除失败!");
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
			var url = '<emp:url action="deleteIqpAbsProListRecord.do"/>?pre_package_serno=${context.pre_package_serno}&batch_no=${context.batch_no}&subBillNoStr='+subBillNoStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
		
	};
	
	function doReset(){
		page.dataGroups.IqpAbsProListGroup.reset();
	};

	function doImportAbsProDetail(){
		var url = '<emp:url action="importAbsProDetailPage.do"/>&pre_package_serno=${context.pre_package_serno}&batch_no=${context.batch_no}';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpAbsProListGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAbsProList.bill_no" label="贷款台账账号" />
			<emp:text id="IqpAbsProList.five_class" label="五级分类" />
			<emp:text id="IqpAbsProList.acc_status" label="台账状态" />
			<emp:text id="IqpAbsProList.deal_status" label="明细状态" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
    <%if(!"view".equals(op)){ %>
	<div align="left">
		<emp:button id="getAddIqpAbsProListPage" label="明细筛选" />
		<emp:button id="deleteIqpAbsProList" label="删除" />
		<emp:button id="importAbsProDetail" label="导入" />
	</div>
    <%} %>
	<emp:table icollName="IqpAbsProListList" pageMode="true" selectType="2" url="pageIqpAbsProListQuery.do" reqParams="pre_package_serno=${context.pre_package_serno}&batch_no=${context.batch_no}">
		<emp:text id="batch_no" label="批次号" hidden="true"/>
		<emp:text id="pre_package_serno" label="预封包流水号" hidden="true"/>
		<emp:text id="cont_type" label="合同类型" />
		<emp:text id="guar_type" label="担保方式" />
		<emp:text id="bill_no" label="贷款台账账号"/>
		<emp:text id="cus_id" label="客户编号" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="bill_balance" label="借据余额" />
		<emp:text id="bill_amt" label="借据金额" />
		<emp:text id="repay_type" label="还款方式" hidden="true"/>
		<emp:text id="loan_start_date" label="贷款起始日" />
		<emp:text id="loan_end_date" label="贷款到期日" />
		<emp:text id="five_class" label="五级分类" />
		<emp:text id="manager_br_id" label="管理机构" />
		<emp:text id="fina_br_id" label="账务机构" hidden="true"/>
		<emp:text id="acc_status" label="台账状态" />
		<emp:text id="deal_status" label="明细状态" dictname="STD_ABS_DEAL_STATUS"/>
		<emp:text id="error_msg" label="错误信息" />
	</emp:table>
	
</body>
</html>
</emp:page>
    