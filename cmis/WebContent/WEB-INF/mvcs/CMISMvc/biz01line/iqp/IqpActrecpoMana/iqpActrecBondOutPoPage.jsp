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
	
	function doOutPo(){
		var paramStr = IqpActrecBondInPoList._obj.getSelectedData();
		var po_no = new Array();
		var invc_no = new Array();
		var cont_no = new Array();
		if (paramStr.length >0) {
			for(var i=0;i<paramStr.length;i++){
				po_no.push(paramStr[i].po_no._getValue());
				invc_no.push(paramStr[i].invc_no._getValue());
				cont_no.push(paramStr[i].cont_no._getValue());
				var status = paramStr[i].status._getValue();
				if(status=="6"){
					alert("已出池记账，不能重复发起出池！");
					return;
				}
			}
			if(confirm("是否确认要出池？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("出池失败！");
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("出池记账成功！");
							window.opener.location.reload();
							window.location.reload();
						/* modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start */
						/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 start */
						}else if(flag == "error1"){
							alert("[在池债权总金额*质押率+保证金金额]不能覆盖池担保占用敞口，不能出池！");
							window.location.reload();
						/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 end */
						}else if(flag == "error2"){
							alert("[在池债权总金额*质押率+保理待处理资金账户金额]不能覆盖池担保占用敞口，不能出池！");
							window.location.reload();
						}
						/* modified by yangzy 2015/05/29 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end */
						else {
							alert("出池失败！");
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
				
				var url = '<emp:url action="iqpActrecBondOutPo.do"/>?po_no=' + encodeURI(po_no)+'&invc_no='+encodeURI(invc_no)+'&cont_no='+encodeURI(cont_no);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doReturn(){
		window.close();
		}
	function doOutPo_p() {
		var num = IqpActrecBondInPoList._obj.getSelectedData().length;
		if (num == 0){
			alert('请先选择一条记录！');
			return;
		}
		if (num == 1) {
			for(var i=0;i<num;i++){
				var guarantyNo = IqpActrecBondInPoList._obj.getSelectedData()[i].po_no._getValue();
				var invc_no = IqpActrecBondInPoList._obj.getSelectedData()[i].invc_no._getValue();
				var cont_no = IqpActrecBondInPoList._obj.getSelectedData()[i].cont_no._getValue();
				var warrantNo = invc_no+'#'+cont_no;
				warrantNo = encodeURIComponent(warrantNo);
				var warrant_state = IqpActrecBondInPoList._obj.getSelectedData()[i].status._getValue();
				if(warrant_state!="6"){
					alert("只有出池记账中的数据可以做出池单打印！");
					return;
				}
			}
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("获取出池申请信息失败！");
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						var serno = jsonstr.serno;
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/ypckd2.raq&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						alert("获取出池申请信息失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("获取出池申请信息失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="printStorageMortGuarantyCertiInfo.do"/>?flag=out&guarantyNo='+guarantyNo+'&warrantNo='+warrantNo+'&warrantType=99';
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
	
	<emp:table icollName="IqpActrecBondInPoList" pageMode="false" url="" selectType="2">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="sel_cus_name" label="卖方客户名称" />
		<emp:text id="cont_no" label="商业合同编号" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency" />
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
		<emp:button id="outPo" label="出池" />
		<emp:button id="outPo_p" label="出池单打印" />
		<emp:button id="return" label="关闭" />
	</div>
	
</body>
</html>
</emp:page>
    