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
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<% String flag=(String)request.getParameter("flag"); %>

<script type="text/javascript">
		function refreshLmtBatchCorre() {
			LmtIntbankBatchList_tabs.tabs.LmtBatchCorre_tab.refresh();
		};
		//引入操作
		function doImport(){	
			var batch_cus_no =LmtIntbankBatchList.batch_cus_no._getValue();
			var crd_grade = LmtIntbankBatchList.cdt_lvl._getValue();
			var url = '<emp:url action="queryCusSameOrgPop4Batch.do"/>?crd_grade=' + crd_grade + '&batch_cus_no='+batch_cus_no;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'importSameOrg'," width=900px,height=650px,resizable=1");
		};
		
		function doDelete() {
			var paramStr = LmtBatchCorreList._obj.getParamStr(['batch_cus_no','cus_id']);
			if (paramStr != null) {
				if(confirm("是否确认要删除？")){
					doDeleteAjax();
				}
			} else {
				alert('请先选择一条记录！');
			}
		};
			
		//进行异步删除
		function doDeleteAjax(){
			var paramStr = LmtBatchCorreList._obj.getParamStr(['batch_cus_no','cus_id']);
			var handleSuccess = function(o){ 		
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("删除成功！");
						window.location.reload();
					}else{
						window.close();
					}
				}
			};
			var handleFailure = function(o){
				alert("异步回调失败！");	
			};
			var url = '<emp:url action="deleteLmtIntbankBatchListLmtBatchCorreRecord.do"/>?'+paramStr;
			var callback = {
					success:handleSuccess,
					failure:handleFailure
			};
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		}
		
		function doView() {
			var paramStr = LmtBatchCorreList._obj.getParamStr(['cus_id']);
			if (paramStr!=null) {
				var url = '<emp:url action="getCusSameOrgViewPage.do"/>?'+paramStr+'&type=cusSame&restrict_tab=Y&op=view';
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=850, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				EMPTools.openWindow(url,'newwindow',param);
			}else {
				alert('请先选择一条记录！');
			}
		};
		
		//从EXCEL中导入数据到批量包中
		function doIntro(){
			var batch_cus_no = LmtIntbankBatchList.batch_cus_no._getValue();
			var cdt_lvl = LmtIntbankBatchList.cdt_lvl._getValue();
			var serno = LmtIntbankBatchList.serno._getValue();
			var url = '<emp:url action="queryLmtBatchCorreList.do"/>?batch_cus_no='+batch_cus_no+'&cdt_lvl='+cdt_lvl+'&serno='+serno;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow'); 
		}
		
		function doReturn(){
			var op='${context.op}'
		    if(op=='add')
		    {
		    	var url = '<emp:url action="queryLmtIntbankBatchListList.do"/>';
				url = EMPTools.encodeURI(url);
				window.location = url;
		    	return false;
			}
			window.history.go(-1);
		}
 
</script>
</head>
<body class="page_content">	
	<emp:form id="submitForm" action="updateLmtIntbankBatchListRecord.do" method="POST">
	<emp:tab label="11" id="11">
		<emp:gridLayout id="LmtIntbankBatchListGroup" title="同业客户批量名单维护" maxColumn="2">		
			<emp:text id="LmtIntbankBatchList.batch_cus_no" label="批量客户编号" maxlength="32" required="true" readonly="true"/>
			<emp:select id="LmtIntbankBatchList.batch_cus_type" label="批量客户类型" required="true" readonly="true" dictname="STD_ZB_BATCH_CUS_TYPE"/>
			<emp:text id="LmtIntbankBatchList.cdt_lvl" label="信用等级" required="true" dictname="STD_ZB_FINA_GRADE" readonly="true"/>		
			<emp:text id="LmtIntbankBatchList.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="LmtIntbankBatchList.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:select id="LmtIntbankBatchList.status" label="状态" required="true" readonly="true" dictname="STD_ZB_INTBANK_STATE"/>
			<emp:text id="LmtIntbankBatchList.input_id" label="登记人" maxlength="20"  hidden="true"/>
			<emp:text id="LmtIntbankBatchList.input_br_id" label="登记机构" maxlength="32" hidden="true"/>
			<emp:date id="LmtIntbankBatchList.start_date" label="生效日期"  hidden="true"/>
			<emp:date id="LmtIntbankBatchList.end_date" label="到期日期"  hidden="true"/>
			<emp:text id="LmtIntbankBatchList.serno" label="业务编号" maxlength="32" hidden="true"/>						
			<emp:text id="LmtIntbankBatchList.manager_id" label="责任人" required="false" hidden="true"/>
			<emp:text id="LmtIntbankBatchList.manager_br_id" label="管理机构"  required="false" hidden="true"/>
			<emp:select id="LmtIntbankBatchList.approve_status" label="审批状态"  hidden="true"/>		
		</emp:gridLayout>
		</emp:tab>
	</emp:form>
		<% if("view".equals(flag)){%>
		  <div align="left">	
				<emp:button id="view" label="查看" />
	    </div>
	    <%}else {%>         
		<div align="left">	
				<emp:button id="import" label="引入" />		
				<emp:button id="delete" label="删除" />
				<emp:button id="intro" label="导入" />
				<emp:button id="view" label="查看" />
	    </div>
	    <% }%>		    
		<emp:table icollName="LmtBatchCorreList"  pageMode="true" url="pageLmtIntbankBatchListLmtBatchCorreQuery.do?batch_cus_no=${context.LmtIntbankBatchList.batch_cus_no}">
			<emp:text id="batch_cus_no" label="批量客户编号" />
			<emp:text id="cus_id" label="客户码" />
			<emp:text id="same_org_cnname" label="同业机构(行)名称" />
			<emp:select id="same_org_type" label="同业机构类型" dictname="STD_ZB_INTER_BANK_ORG"/>
			<emp:select id="cust_level" label="监管评级" dictname="STD_ZB_CUSTD_RATE"/>
			<emp:text id="assets" label="总资产(万元)" dataType="Currency"/>
			<emp:text id="paid_cap_amt" label="实收资本(万元)" dataType="Currency"/>
			<emp:text id="input_id_displayname" label="登记人" />
			<emp:text id="input_br_id_displayname" label="登记机构" />
			<emp:text id="input_date" label="登记日期"/>
			<emp:text id="input_id" label="登记人" hidden="true"/>
			<emp:text id="input_br_id" label="登记机构" hidden="true"/>		
	   </emp:table>
	   <br>
	   <div align="center">
	<emp:button id="return" label="返回列表"/>
	</div>
	
</body>
</html>
</emp:page>
