<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String po_no = context.getDataValue("po_no").toString();
	String type = context.getDataValue("type").toString();
%>
<emp:page>

<html>
<head>

<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpActrecbondDetail._toForm(form);
		IqpActrecbondDetailList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpActrecbondDetailPage() {
		var paramStr = IqpActrecbondDetailList._obj.getParamStr(['cont_no','invc_no']);
		if (paramStr != null) {
			var paramStr2 = IqpActrecbondDetailList._obj.getParamStr(['status']);
			if(paramStr2!='status=1'){
				alert("只有登记状态可以修改！");
				return;
				}
			var url = '<emp:url action="getIqpActrecbondDetailUpdatePage.do"/>?' + encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpActrecbondDetail() {
		var paramStr = IqpActrecbondDetailList._obj.getParamStr(['cont_no','invc_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpActrecbondDetailViewPage.do"/>?'+ encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=900, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpActrecbondDetailPage() {
		var po_no = '<%=po_no%>';
		var url = '<emp:url action="getIqpActrecbondDetailAddPage.do"/>?poNo='+po_no;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'',param);
		window.location.reload();
	};
	
	function doDeleteIqpActrecbondDetail(){
		
		var paramStr = IqpActrecbondDetailList._obj.getParamStr(['cont_no','invc_no','po_no']);
		if (paramStr != null) {
			var paramStr2 = IqpActrecbondDetailList._obj.getParamStr(['status']);
			if(paramStr2!='status=1'){
				alert("只有登记状态可以删除！");
				return;
				}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功！");
							var url = '<emp:url action="queryIqpActrecbondDetailList.do"/>?po_no=<%=po_no%>&type=<%=type%>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除失败！");
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
				
				var url = '<emp:url action="deleteIqpActrecbondDetailRecord.do"/>?' + encodeURI(paramStr);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpActrecbondDetailGroup.reset();
	};
	
	/*--user code begin--*/
	//回款登记按钮   2014-10-16 唐顺岩
	function doActrecRepay() {
		var paramStr = IqpActrecbondDetailList._obj.getParamStr(['cont_no','invc_no']);
		if (paramStr != null) {
			var paramStr2 = IqpActrecbondDetailList._obj.getParamStr(['status']);
			if(paramStr2!='status=3'){
				alert("该应收账款还[在池]或[已回款]，请选择[已出池]状态的应收账款进行该操作！");
				return;
			}
			var url = '<emp:url action="getIqpActrecbondDetailUpdatePage.do"/>?target=Y&' + encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=900, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=yes';
			window.open(url,'actrecRepaPage',param);
			//var param = 'dialogHeight:900px;dialogWidth:1200px;center:yes;toolbar:no;menubar:no;scrollbars:yes;resizable:yes;location:no;status:no';
			//window.showModalDialog(url,"actrecRepaPage",param);
			//window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};		
	/*--user code end--*/
	/* add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   begin */
	//导入EXCEL
    function doImportIqpActrecbondDetailByExcel(){
    	var po_no = '<%=po_no%>';
    	var type = '<%=type%>';
    	var url = '<emp:url action="queryIqpActrecbondDetailImport.do"/>?po_no='+po_no+'&type='+type;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }	
    //下载模板
    function doDownLoadIqpActrecbondDetail(){
    	var url = '<emp:url action="downLoadIqpActrecbondDetailTmplate.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
    }
    /* add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   end */
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>

	<div align="left">
		<%if(!type.equals("view")){ %>
			<emp:button id="getAddIqpActrecbondDetailPage" label="新增" op="add"/>
			<emp:button id="getUpdateIqpActrecbondDetailPage" label="修改" op="update"/>
			<emp:button id="deleteIqpActrecbondDetail" label="删除" op="remove"/>
			<emp:button id="actrecRepay" label="回款登记" />
			<!-- add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   begin -->
			<emp:button id="downLoadIqpActrecbondDetail" label="下载模板" />
			<emp:button id="importIqpActrecbondDetailByExcel" label="导入" />
			<!-- add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   end -->
		<% } %>
		<emp:button id="viewIqpActrecbondDetail" label="查看" op="view"/>
	</div>
	
	<emp:table icollName="IqpActrecbondDetailList" pageMode="false" url="pageIqpActrecbondDetailQuery.do">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="sel_cus_name" label="卖方客户名称" />
		<emp:text id="cont_no" label="贸易合同编号" />
		<emp:text id="invc_no" label="权证号" />
		<emp:text id="invc_amt" label="权证金额" dataType="Currency"/>
		<emp:text id="invc_date" label="权证日期" />
		<emp:text id="bond_pay_date" label="付款到期日" />
		<emp:select id="status" label="状态" hidden="false" dictname="STD_ACTREC_INVC_STATUS"/>
		
		<emp:text id="bond_mode" label="债券类型" hidden="true"/>
		<emp:text id="invc_ccy" label="发票币种" hidden="true"/>
		<emp:text id="bond_amt" label="债权金额" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    