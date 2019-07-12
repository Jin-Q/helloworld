<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doBreakLmtPage(){
			var url = '<emp:url action="BreakLmtPage.do"/>';
			url= EMPTools.encodeURI(url);
			window.location = url;
	};
	
	function doUnfrozeLmtIntbankAcc(){		
		var paramStr = LmtIntbankAccList._obj.getParamStr(['serno','agr_no','cus_id']);
		if (paramStr != null) {
			var froze_amt = LmtIntbankAccList._obj.getSelectedData()[0].froze_amt._getValue();
			if(froze_amt !=''){
				var url = '<emp:url action="getUnfrozePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("该台帐没有冻结金额！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateLmtIntbankAccPage(){
		var paramStr = LmtIntbankAccList._obj.getParamStr(['serno','agr_no','cus_id']);
		if (paramStr != null) {
			var status=LmtIntbankAccList._obj.getSelectedData()[0].lmt_status._getValue();
			var odd_amt = LmtIntbankAccList._obj.getSelectedData()[0].odd_amt._getValue();
			if(status=='10'&&eval(odd_amt)>0){
				var url = '<emp:url action="getLmtIntbankAccUpdatePage.do"/>?'+paramStr+"&odd_amt="+odd_amt;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("只有额度状态为正常并且有可用额度的台帐才能进行冻结！");
				}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtIntbankAcc(){
		var paramStr = LmtIntbankAccList._obj.getParamStr(['serno','cus_id','agr_no']);
		if (paramStr != null) {
			var obj = LmtIntbankAccList._obj.getSelectedData();
			var odd_amt = obj[0].odd_amt._getValue();
			var url = '<emp:url action="queryLmtIntbankAccDetail.do"/>?'+paramStr+"&odd_amt="+odd_amt+"&flag=LmtIntbankView";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIntbankAcc._toForm(form);
		LmtIntbankAccList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtIntbankAccGroup.reset();
	};
	
	function returnCusId(data){
		LmtIntbankAcc.cus_id._setValue(data.cus_id._getValue());
		LmtIntbankAcc.cus_id_displayname._setValue(data.same_org_cnname._getValue());
	}	

	/**add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 begin**/
	//下载导入模板
	function doDownLoadLmtIntBankAccTemplate(){
		var url = '<emp:url action="downLoadLmtIntbankAccTmplate.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//导入EXCEL
    function doImportLmtIntBankAccByExcel(){
    	var url = '<emp:url action="queryLmtIntbankAccImport.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }
    /**add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 end**/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIntbankAccGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtIntbankAcc.agr_no" label="协议编号" />
			<emp:text id="LmtIntbankAcc.cus_id" label="客户码" />
			<emp:pop id="LmtIntbankAcc.cus_id_displayname" label="客户名称" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:date id="LmtIntbankAcc.start_date" label="授信起始日期" />
			<emp:select id="LmtIntbankAcc.lmt_status" label="额度状态" dictname="STD_LMT_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
	    <emp:button id="viewLmtIntbankAcc" label="查看" op="view"/>
		<emp:button id="getUpdateLmtIntbankAccPage" label="冻结" op="froze"/>
		<emp:button id="unfrozeLmtIntbankAcc" label="解冻" op="unfroze"/>
		<emp:button id="breakLmtPage" label="终止授信" op="breakLmt"/>
		<!-- add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 begin -->
		<emp:button id="downLoadLmtIntBankAccTemplate" label="下载导入模板" op="downLoad"/>
		<emp:button id="importLmtIntBankAccByExcel" label="导入" op="imp"/>
		<!-- add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 end -->
		
	</div>
	<emp:table icollName="LmtIntbankAccList" pageMode="true" url="pageLmtIntbankAccQuery.do">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_cnname" label="同业机构(行)名称"/>
		<emp:text id="crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE"/>
		<emp:text id="lmt_amt" label="授信总额(元)" dataType="Currency" />
		<emp:text id="froze_amt" label="冻结金额(元)" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日期"/>
		<emp:text id="end_date" label="授信到期日期"/>
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
		<emp:text id="user_amt" label="已用额度(元)" dataType="Currency" hidden="true"/>
		<emp:text id="odd_amt" label="剩余额度(元)" dataType="Currency" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>