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
		var paramStr = IqpActrecBondOutPoList._obj.getSelectedData();
		if (paramStr.length==1) {
			var invc_no = paramStr[0].invc_no._getValue();
			var cont_no = paramStr[0].cont_no._getValue();
			var status = paramStr[0].status._getValue();
			if(status!='1'){
				alert("只有登记状态可以修改！");
				return;
			}
			var url = '<emp:url action="getIqpActrecbondDetailUpdatePage.do"/>?invc_no='+encodeURI(invc_no)+'&cont_no='+encodeURI(cont_no);
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'UpdateIqp',param);
			//window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpActrecbondDetail() {
		var paramStr = IqpActrecBondOutPoList._obj.getSelectedData();
		var paramStr1 = IqpActrecBondInPoList._obj.getSelectedData();
		if (paramStr.length==1) {
			var invc_no = paramStr[0].invc_no._getValue();
			var cont_no = paramStr[0].cont_no._getValue();
			var url = '<emp:url action="getIqpActrecbondDetailViewPage.do"/>?invc_no='+encodeURI(invc_no)+'&cont_no='+encodeURI(cont_no);
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'View',param);
			window.location.reload();
		} else if(paramStr1.length==1){
			var invc_no1 = paramStr1[0].invc_no._getValue();
			var cont_no1 = paramStr1[0].cont_no._getValue();
			var url = '<emp:url action="getIqpActrecbondDetailViewPage.do"/>?invc_no='+encodeURI(invc_no1)+'&cont_no='+encodeURI(cont_no1);
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'ViewIq');
			window.location.reload();
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpActrecbondDetailPage() {
		var po_no = '<%=po_no%>';
		var url = '<emp:url action="getIqpActrecbondDetailAddPage.do"/>?poNo='+po_no;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'AddIqpActrecbondDetail',param);
		window.location.reload();
	};
	
	function doDeleteIqpActrecbondDetail(){
		var paramStr = IqpActrecBondOutPoList._obj.getSelectedData();
		
		if (paramStr.length==1) {
			var po_no = paramStr[0].po_no._getValue();
			var invc_no = paramStr[0].invc_no._getValue();
			var cont_no = paramStr[0].cont_no._getValue();
			var paramStr2 = paramStr[0].status._getValue();
			if(paramStr2!='1'){
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
							window.location.reload();
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
				
				var url = '<emp:url action="deleteIqpActrecbondDetailRecord.do"/>?po_no=' + encodeURI(po_no)+'&invc_no='+encodeURI(invc_no)+'&cont_no='+encodeURI(cont_no);
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
	
	function doInPo(){
		var paramStr = IqpActrecBondOutPoList._obj.getSelectedData();
		var po_no = new Array();
		var invc_no = new Array();
		var cont_no = new Array();
		if (paramStr.length >0) {
			for(var i=0;i<paramStr.length;i++){
				po_no.push(paramStr[i].po_no._getValue());
				invc_no.push(paramStr[i].invc_no._getValue());
				cont_no.push(paramStr[i].cont_no._getValue());
				var warrant_state = paramStr[i].status._getValue();
				if(warrant_state=="5"){
					alert("已入池记账，不能重复发起入池！");
					return;
				}
			}
			if(confirm("是否确认要入池？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("入池失败！");
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("入池记账成功！");
							window.opener.location.reload();
							window.location.reload();
						}else {
							alert("入池失败！");
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
				
				var url = '<emp:url action="iqpActrecBondInPo.do"/>?po_no=' + encodeURI(po_no)+'&invc_no='+encodeURI(invc_no)+'&cont_no='+encodeURI(cont_no);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doInPo_p() {
		var num = IqpActrecBondOutPoList._obj.getSelectedData().length;
		if (num == 0){
			alert('请先选择一条记录！');
			return;
		}
		if (num == 1) {
			for(var i=0;i<num;i++){
				var guarantyNo = IqpActrecBondOutPoList._obj.getSelectedData()[i].po_no._getValue();
				var invc_no = IqpActrecBondOutPoList._obj.getSelectedData()[i].invc_no._getValue();
				var cont_no = IqpActrecBondOutPoList._obj.getSelectedData()[i].cont_no._getValue();
				var warrantNo = invc_no+'#'+cont_no;
				warrantNo = encodeURIComponent(warrantNo);
				var warrant_state = IqpActrecBondOutPoList._obj.getSelectedData()[i].status._getValue();
				if(warrant_state!="5"){
					alert("只有入池记账中的数据可以做入池单打印！");
					return;
				}
			}
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("获取入池申请信息失败！");
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						var serno = jsonstr.serno;
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/yprkd2.raq&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						alert("获取入库申请信息失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("获取入池申请信息失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="printStorageMortGuarantyCertiInfo.do"/>?guarantyNo='+guarantyNo+'&warrantNo='+warrantNo+'&warrantType=99';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('不能选取多条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div class='emp_gridlayout_title'>在池发票列表&nbsp;</div>
	
	<emp:table icollName="IqpActrecBondInPoList" pageMode="false" url="">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="sel_cus_name" label="卖方客户名称" />
		<emp:text id="cont_no" label="贸易合同编号" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="invc_date" label="开票日期" />
		<emp:text id="bond_pay_date" label="付款到期日" />
		<emp:text id="status" label="状态" dictname="STD_ACTREC_INVC_STATUS"/>
			
		<emp:text id="bond_mode" label="债券类型" hidden="true"/>
		<emp:text id="invc_ccy" label="发票币种" hidden="true"/>
		<emp:text id="bond_amt" label="债券金额" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
	<div class='emp_gridlayout_title'>待入池发票列表&nbsp;</div>
	
	<emp:table icollName="IqpActrecBondOutPoList" pageMode="false" url="" selectType="2">
		<emp:text id="po_no" label="池编号" />
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="sel_cus_name" label="卖方客户名称" />
		<emp:text id="cont_no" label="贸易合同编号" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="invc_date" label="开票日期" />
		<emp:text id="bond_pay_date" label="付款到期日" />
		<emp:text id="status" label="状态" hidden="false" dictname="STD_ACTREC_INVC_STATUS"/>
		
		<emp:text id="bond_mode" label="债券类型" hidden="true"/>
		<emp:text id="invc_ccy" label="发票币种" hidden="true"/>
		<emp:text id="bond_amt" label="债券金额" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
	<div align="left">
		<emp:button id="getAddIqpActrecbondDetailPage" label="新增" />
		<emp:button id="getUpdateIqpActrecbondDetailPage" label="修改" />
		<emp:button id="deleteIqpActrecbondDetail" label="删除" />
		<emp:button id="viewIqpActrecbondDetail" label="查看" />
		<emp:button id="inPo" label="确认入池" />
		<emp:button id="inPo_p" label="打印入池单" />
	</div>
	
</body>
</html>
</emp:page>
    