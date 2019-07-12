<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String is_ebill = "";
	if(context.containsKey("is_ebill")){
		is_ebill = (String)context.getDataValue("is_ebill");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doOnLoad(){
		//------获取票据总金额、票据数量------
		var num = IqpBillDetailList._getSize();
		var totel=0;
		for(var i=0;i<num;i++){
			var amt = IqpBillDetailList[i].drft_amt._getValue();
			totel = parseFloat(totel)+parseFloat(amt);
		}
		IqpBillDetail.porder_no._setValue(''+totel+'');
		IqpBillDetail.rpay_amt._setValue('${context.rpay_amt}');
		IqpBillDetail.int_amt._setValue('${context.int_amt}');
		IqpBillDetail.batch_no._setValue('${context.batch_no}');
	};
		
	//------------异步获取利息信息----------
	function getRateByPorderNos(){

	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBillDetail._toForm(form);
		IqpBillDetailList._obj.ajaxQuery(null,form);
	};
	function doGetUpdateIqpBillDetailPage() {
		var batchStatus = '${context.status}';
		if(batchStatus == '02'){
			alert("该批次正在审批中，不允许修改！");
			return;
		}
		//列表改为多选  2014-09-26 唐顺岩
		var idx = IqpBillDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length == 1){
			var paramStr = IqpBillDetailList._obj.data[idx[0]]['porder_no']._getValue();  //得到单个值
			var url = '<emp:url action="getIqpBillDetailUpdatePage.do"/>?op=update&menuIdFather=${context.menuId}&subMenuIdFather=${context.subMenuId}&menuId=queryIqpBillDetail&batch_no=${context.batch_no}&serno=${context.serno}&porder_no='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条记录，再操作！');
		}
	};
	function doViewIqpBillDetail() {
		//列表改为多选  2014-09-26 唐顺岩
		var idx = IqpBillDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length == 1){
			//var paramStr = IqpBillDetailList._obj.getParamStr(['porder_no']);
			var paramStr = IqpBillDetailList._obj.data[idx[0]]['porder_no']._getValue();  //得到单个值
			var url = '<emp:url action="getIqpBillDetailViewPage.do"/>?op=view&menuId=queryIqpBillDetail&batch_no=${context.batch_no}&serno=${context.serno}&porder_no='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else { 
			alert('请选择一条记录，再操作！');
		}
	};
	
	function doGetAddIqpBillDetailPage() {
		 //如果没有引入批次，那么不能新增票据。在这里用num判断
        var batch_no = '${context.batch_no}';
        var num = IqpBillDetailList._getSize();
		if(num==0 && (batch_no=="" || batch_no==null)){
            alert("请先引入批次，必须在已有批次里新增票据!");
            return false;
	    }
	    
		var batchStatus = '${context.status}';
		if(batchStatus == '02'){
			alert("该批次正在审批中，不允许修改！");
			return;
		}
		var url = '<emp:url action="getIqpBillDetailAddPage.do"/>?menuIdFather=${context.menuId}&subMenuIdFather=${context.subMenuId}&batch_no=${context.batch_no}&bill_type=${context.bill_type}&biz_type=${context.biz_type}';
		url = EMPTools.encodeURI(url);
		window.location = url; 
	};
	
	function doDeleteIqpBillDetail() {
		var batchStatus = '${context.status}';
		if(batchStatus == '02'){
			alert("该批次正在审批中，不允许修改！");
			return;
		}
		//可以批量剔除票据  2014-09-26 唐顺岩
		var idx = IqpBillDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length >= 1){
			var paramStr = IqpBillDetailList._obj.getParamStr(['porder_no']);
			if(confirm("是否确认要剔除选择明细？该操作不可恢复！")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var msg = jsonstr.msg;
						if(flag == "success"){
							alert("剔除成功！");
							window.location.reload();
						}else {
							alert("剔除失败！"+msg);
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

				var url = '<emp:url action="deleteIqpBillDetailRecord.do"/>?batch_no=${context.batch_no}&serno=${context.serno}&'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			} 
		} else {
			alert('请至少选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBillDetailGroup.reset();
	};
	
	/*--user code begin--*/
	function doImportIqpBillDetail(){
		 //如果没有引入批次，那么不能新增票据。在这里用num判断
        var batch_no = '${context.batch_no}';
        var num = IqpBillDetailList._getSize();
		if(num==0 && (batch_no=="" || batch_no==null)){
            alert("请先引入批次，必须在已有已有批次里新增票据！");
            return false;
	    }
	    
		var url = '<emp:url action="importIqpBillPage.do"/>?serno=${context.serno}&batch_no=${context.batch_no}';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	//------获取导入批次输入页面-----
	function doImportIqpBatch(){
		//alert('${context.prd_id}');
		var url = '<emp:url action="importIqpBatchPage.do"/>?menuId=queryIqpBatchMng&serno=${context.serno}&prd_id=${context.prd_id}';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};	
	//导入EXCEL
    function doImportIqpBillDetailByExcel(){
    	var batch_no = '${context.batch_no}';
        var num = IqpBillDetailList._getSize();
		if(num==0 && (batch_no=="" || batch_no==null)){
            alert("请先引入批次，必须在已有已有批次里导入票据！");
            return false;
	    }
    	var url = '<emp:url action="queryIqpBillDetailImport.do"/>?batch_no=${context.batch_no}&bill_type=${context.bill_type}&biz_type=${context.biz_type}';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }	
    //下载模板
    function doDownLoadIqpBillDetail(){
        var bill_type= '${context.bill_type}';
    	var url = '<emp:url action="downLoadIqpBillDetailTmplate.do"/>?bill_type=${context.bill_type}';
		url = EMPTools.encodeURI(url);
		window.location = url;
    }

    //增加贴现业务合计展示，需求编号：XD140812041  2014-09-23 唐顺岩
    function doShowTotal(){
    	var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRITNT_TXHTHJ.raq&batch_no=${context.batch_no}';
	
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
    }
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBillDetailGroup" title="当前票面金额汇总" maxColumn="2">
			<emp:text id="IqpBillDetail.batch_no" label="批次号"  readonly="true" />
			<emp:text id="IqpBillDetail.porder_no" label="汇票总金额" dataType="Currency"  readonly="true" />
			<emp:text id="IqpBillDetail.rpay_amt" label="实付总金额" dataType="Currency"  readonly="true" />
			<emp:text id="IqpBillDetail.int_amt" label="利息金额" dataType="Currency"  readonly="true" />
	</emp:gridLayout>
	
	<div align="left">
		<%
		if(!is_ebill.equals("1")){
		//电票不显示
		%>
		<emp:actButton id="importIqpBatch" label="引入批次" op="impBatch"/>
		<emp:actButton id="getAddIqpBillDetailPage" label="新增" op="add"/>
		<%}%>
		<emp:actButton id="getUpdateIqpBillDetailPage" label="修改" op="update"/>
		<%
		if(!is_ebill.equals("1")){
		//电票不显示
		%>
		<emp:actButton id="deleteIqpBillDetail" label="剔除" op="remove"/>
		<%}%>
		
		<%
		String biz_type = "";
		if(context.containsKey("biz_type")){
			biz_type = (String)context.getDataValue("biz_type");
		}
		if(biz_type.equals("03")||biz_type.equals("04")||biz_type.equals("05")||biz_type.equals("06")){
		//买入不显示导入按钮
		%><%} %>
		<%
		if(!is_ebill.equals("1")){
		//电票不显示
		%>
		<emp:actButton id="downLoadIqpBillDetail" label="下载模板" op="imp"/>
		<emp:actButton id="importIqpBillDetailByExcel" label="导入" op="imp"/>
		<%}%>
		<emp:actButton id="viewIqpBillDetail" label="查看" op="view"/>
		<emp:button id="showTotal" label="合计" />
		
	</div>
<%
	String bill_type = "";
	if(context.containsKey("bill_type")){
		bill_type = (String)context.getDataValue("bill_type");
	}
	if(bill_type.equals("200")){//商业承兑汇票
%>
	<emp:table icollName="IqpBillDetailList" pageMode="true" selectType="2" url="pageIqpBillDetailQuery.do" reqParams="serno=${context.serno}&batch_no=${context.batch_no}">
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="bill_isse_date" label="票据签发日" />
		<emp:text id="porder_end_date" label="汇票到期日" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency" />
		<emp:text id="isse_name" label="出票/付款人名称" />
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="aaorg_name" label="承兑人开户行名称" />
		<emp:text id="aaorg_no" label="承兑人开户行行号" />
		<emp:text id="aaorg_type" label="承兑人开户行类型" dictname="STD_AORG_ACCTSVCR_TYPE"/>
		<emp:text id="fore_disc_date" label="贴现日期" />
	</emp:table>
<% }else{%>
	<emp:table icollName="IqpBillDetailList" pageMode="true" selectType="2" url="pageIqpBillDetailQuery.do" reqParams="serno=${context.serno}&batch_no=${context.batch_no}">
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="bill_isse_date" label="票据签发日" />
		<emp:text id="porder_end_date" label="汇票到期日" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency" />
		<emp:text id="isse_name" label="出票/付款人名称" />
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="aorg_name" label="承兑行名称" />
		<emp:text id="aorg_no" label="承兑行行号" />
		<emp:text id="aorg_type" label="承兑行类型" dictname="STD_AORG_ACCTSVCR_TYPE"/>
		<emp:text id="fore_disc_date" label="贴现日期" />
	</emp:table>
<% }%>
</body>
</html>
</emp:page>
    