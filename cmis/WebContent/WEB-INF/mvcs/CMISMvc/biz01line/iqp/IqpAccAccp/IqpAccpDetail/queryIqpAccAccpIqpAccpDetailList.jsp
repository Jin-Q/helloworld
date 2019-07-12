<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	String is_elec_bill="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}  
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		
	}
	if(context.containsKey("is_elec_bill")){
		is_elec_bill = (String)context.getDataValue("is_elec_bill");
		
	}
%>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateIqpAccpDetailPage() {
		//列表改为多选  2014-09-28 唐顺岩
		var idx = IqpAccpDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length == 1){
		//var paramStr = IqpAccpDetailList._obj.getParamStr(['pk1']);
			var paramStr = IqpAccpDetailList._obj.data[idx[0]]['pk1']._getValue();  //得到单个值
		//if (paramStr!=null) {
			/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var cont = '<%=cont%>';
			var url = "";
			if(cont =="modify"){
				var modify_rel_serno  = IqpAccpDetailList._obj.data[idx[0]]['modify_rel_serno']._getValue();
				url = '<emp:url action="getIqpAccAccpIqpAccpDetailUpdatePage.do"/>?pk1='+paramStr+'&is_elec_bill=${context.is_elec_bill}&cont='+cont+"&modify_rel_serno="+modify_rel_serno;	
			}else{
				url = '<emp:url action="getIqpAccAccpIqpAccpDetailUpdatePage.do"/>?pk1='+paramStr+'&is_elec_bill=${context.is_elec_bill}&cont='+cont;
			}
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param); 
			/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		}else{
			alert('请选择一条记录，再操作！'); 
		}    
	};
	
	function doGetAddIqpAccpDetailPage(){
		var serno = window.parent.window.IqpAccAccp.serno._getValue();
		var url = '<emp:url action="getIqpAccAccpIqpAccpDetailAddPage.do"/>?is_elec_bill=${context.is_elec_bill}&IqpAccpDetail.serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		EMPTools.openWindow(url,'newwindow',param);
	};
	
	function doDeleteIqpAccpDetail() {
		//可以批量剔除票据  2014-09-26 唐顺岩
		var idx = IqpAccpDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length >= 1){
			var paramStr = IqpAccpDetailList._obj.getParamStr(['pk1']);
			var serno  = "${context.IqpAccAccp.serno}";    //流水号从context中获取  2014-09-28 唐顺岩
			if(confirm("是否确认要剔除选择银票明细？该操作不可恢复！")){
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
							window.parent.location.reload();   
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
				var url = '<emp:url action="deleteIqpAccAccpIqpAccpDetailRecord.do"/>?serno='+serno+'&'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAccpDetail() { 
		//列表改为多选  2014-09-28  唐顺岩
		var idx = IqpAccpDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length == 1){
		//var paramStr = IqpAccpDetailList._obj.getParamStr(['pk1']);
			var paramStr = IqpAccpDetailList._obj.data[idx[0]]['pk1']._getValue();  //得到单个值
		//if (paramStr!=null) {
		/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var cont = '<%=cont%>';
		var url = "";
		if(cont =="modify"){
			var modify_rel_serno  = IqpAccpDetailList._obj.data[idx[0]]['modify_rel_serno']._getValue();
			url = '<emp:url action="queryIqpAccAccpIqpAccpDetailDetail.do"/>?pk1='+paramStr+'&cont='+cont+"&modify_rel_serno="+modify_rel_serno;
		}else{
			url = '<emp:url action="queryIqpAccAccpIqpAccpDetailDetail.do"/>?pk1='+paramStr;
		}
		/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		}else {    
			alert('请选择一条记录，再操作！'); 
		}
	};
	/*添加汇总按钮实现   需求编号：XD140812041  author： 王青*/
	function doAddSum(){
		var serno  = "${context.IqpAccAccp.serno}";   //流水号从context中获取  2014-09-18 唐顺岩
		var cont = "<%=cont%>";
		if(cont != "cont"){
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRITNT_CDHPHJ.raq&serno='+serno;
		}else{
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRITNT_CDHPHTHJ.raq&serno='+serno;
		}
		
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		
		}
	/*--user code begin--*/
	function doOnLoad(){
		var num = IqpAccpDetailList._getSize();
		var totel=0;
		for(var i=0;i<num;i++){
			var amt = IqpAccpDetailList[i].drft_amt._getValue();
			totel = parseFloat(totel)+parseFloat(amt);
		}
		window.parent.IqpAccAccp.bill_qty._setValue(num);//汇票数量
		window.parent.IqpAccAccp.bill_qnt._setValue(''+totel+'');//汇票金额
		window.location.refresh(); 
	};
	function doImportIqpAccpDetail(){
		/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var cont = '<%=cont%>';
		var serno = window.parent.window.IqpAccAccp.serno._getValue();
		var url = '<emp:url action="importIqpAccpDetailPage.do"/>&serno='+serno+"&cont="+cont;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
		/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	}
	/*--user code end--*/   
	
</script>
</head>

<body class="page_content">

	<div align="left">
	<%if(!op.equals("view")&&!cont.equals("cont")&&!is_elec_bill.equals("1") && !cont.equals("modify")){%> 
		<emp:button id="getAddIqpAccpDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAccpDetailPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAccpDetail" label="删除" op="remove"/>
		<emp:button id="importIqpAccpDetail" label="导入" /> 
	<%}%>
	<%if("modify".equals(cont) && !"view".equals(op)){ %>
		<emp:button id="getUpdateIqpAccpDetailPage" label="修改" op="update"/>
	<%} %>
		<emp:button id="viewIqpAccpDetail" label="查看" />
	<%if(!"modify".equals(cont)){ %>
		<emp:button id="addSum" label="合计" /> 
	<%} %>
	</div>
							
	<emp:table icollName="IqpAccpDetailList" pageMode="true" selectType="2" url="pageIqpAccAccpIqpAccpDetailQuery.do" reqParams="IqpAccAccp.serno=$IqpAccAccp.serno;">

		<emp:text id="serno" label="业务编号" />
		<emp:text id="clt_person" label="收款人" />
		<emp:text id="clt_acct_no" label="收款人账号" />
		<emp:text id="paorg_no" label="收款人开户行行号" />
		<emp:text id="paorg_name" label="收款人开户行行名" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE" hidden="true"/>
		<emp:text id="term" label="期限" hidden="true"/>
		<emp:text id="pk1" label="承兑汇票申请明细流水号" hidden="true"/>
		<emp:text id="modify_rel_serno" label="打回业务修改流水编号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>