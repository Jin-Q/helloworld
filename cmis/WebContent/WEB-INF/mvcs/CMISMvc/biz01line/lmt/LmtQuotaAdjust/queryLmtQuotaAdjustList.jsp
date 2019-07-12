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
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<% 
	String single_quota = request.getParameter("single_quota");
	String agr_no = request.getParameter("agr_no");
	String fin_totl_limit = request.getParameter("fin_totl_limit");
	String fin_totl_spac = request.getParameter("fin_totl_spac");
	String lmt_start_date = request.getParameter("lmt_start_date");
%>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtQuotaAdjust._toForm(form);
		LmtQuotaAdjustList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtQuotaAdjustPage() {
		var approve_status = LmtQuotaAdjustList._obj.getSelectedData()[0].approve_status._getValue();
		var paramStr = LmtQuotaAdjustList._obj.getParamStr(['serno']);
		var agr_no = '<%=agr_no%>';
		var single_quota = '<%=single_quota%>';
		var fin_totl_limit = '<%=fin_totl_limit%>';
		var fin_totl_spac = '<%=fin_totl_spac%>';
		if (paramStr != null) {
			if(approve_status == '111'){
				alert("业务审批中，不能进行修改操作！");
				return;
			}else{
				var end_date = LmtQuotaAdjustList._obj.getSelectedData()[0].end_date._getValue();
				var status = LmtQuotaAdjustList._obj.getSelectedData()[0].status._getValue();
				var openday = '${context.OPENDAY}';
				if(status == "1"){
					if(end_date<openday){
						alert("生效日期小于当前日期，该用信已过期不能调整！");
						return;
					}
				}
				var url = '<emp:url action="getLmtQuotaAdjustUpdatePage.do"/>?'+paramStr+'&agr_no='+agr_no+'&single_quota='+single_quota+'&fin_totl_limit='+fin_totl_limit
				+"&fin_totl_spac="+fin_totl_spac;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetUpdateSingleAdjustPage() {
		var approve_status = LmtQuotaAdjustList._obj.getSelectedData()[0].approve_status._getValue();
		var paramStr = LmtQuotaAdjustList._obj.getParamStr(['serno']);
		var agr_no = '<%=agr_no%>';
		var single_quota = '<%=single_quota%>';
		var fin_totl_limit = '<%=fin_totl_limit%>';
		var fin_totl_spac = '<%=fin_totl_spac%>';
		if (paramStr != null) {
			if(approve_status == '111'){
				alert("业务审批中，不能进行修改操作！");
				return;
			}else{
				var end_date = LmtQuotaAdjustList._obj.getSelectedData()[0].end_date._getValue();
				var status = LmtQuotaAdjustList._obj.getSelectedData()[0].status._getValue();
				var openday = '${context.OPENDAY}';
				if(status == "1"){
					if(end_date<openday){
						alert("生效日期小于当前日期，该用信已过期不能调整！");
						return;
					}
					var url = '<emp:url action="getUpdateSingleAdjustPage.do"/>?'+paramStr+'&agr_no='+agr_no+'&single_quota='+single_quota+'&fin_totl_limit='+fin_totl_limit
					"+&fin_totl_spac="+fin_totl_spac;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert('请先选择一条【生效】的明细记录！');
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtQuotaAdjust() {
		var paramStr = LmtQuotaAdjustList._obj.getParamStr(['serno']);
		var agr_no = '<%=agr_no%>';
		var single_quota = '<%=single_quota%>';
		var fin_totl_limit = '<%=fin_totl_limit%>';
		var fin_totl_spac = '<%=fin_totl_spac%>';
		var op = '<%=op%>';
		
		if (paramStr != null) {
			var url = '<emp:url action="getLmtQuotaAdjustViewPage.do"/>?'+paramStr+'&agr_no='+agr_no+'&single_quota='+single_quota+'&fin_totl_limit='+fin_totl_limit
			+"&fin_totl_spac="+fin_totl_spac;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtQuotaAdjustPage() {
		checkLmtQuotaAdjustApp();
	};
	function checkLmtQuotaAdjustApp() {
		var fin_agr_no = '<%=agr_no%>';
		var url = '<emp:url action="checkLmtQuotaAdjustAppRecord.do"/>?&fin_agr_no='+fin_agr_no;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					var agr_no = '<%=agr_no%>';
					var single_quota = '<%=single_quota%>';
					var fin_totl_limit = '<%=fin_totl_limit%>';
					var fin_totl_spac = '<%=fin_totl_spac%>';
					var fin_agr_no = '<%=agr_no%>';
					var lmt_start_date = '<%=lmt_start_date%>';
					var url = '<emp:url action="getLmtQuotaAdjustAddPage.do"/>?single_quota='+single_quota
					+'&agr_no='+agr_no+'&fin_totl_limit='+fin_totl_limit+'&fin_agr_no='+fin_agr_no+'&lmt_start_date='+lmt_start_date+"&fin_totl_spac="+fin_totl_spac;
					url = EMPTools.encodeURI(url);
					window.location = url;
			   }else {
				 alert(flag);
				 return;
			   }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
	};
	function doDeleteLmtQuotaAdjust() {
		var paramStr = LmtQuotaAdjustList._obj.getParamStr(['serno']);
		var status = LmtQuotaAdjustList._obj.getSelectedData()[0].status._getValue();
		if(status == "2"){
			if (paramStr != null){
					if(confirm("是否确认要删除？")){
						var url = '<emp:url action="deleteLmtQuotaAdjustRecord.do"/>?'+paramStr;
						url = EMPTools.encodeURI(url);
						var handleSuccess = function(o){
							if(o.responseText !== undefined) {
								try {
									var jsonstr = eval("("+o.responseText+")");
								} catch(e) {
									alert("Parse jsonstr define error!"+e);
									return;
								}
								var flag = jsonstr.flag;
								if(flag=="success"){
									alert("删除成功!");
									window.location.reload();
							   }else {
								 alert(flag);
								 return;
							   }
							}
						};
						var handleFailure = function(o){	
						};
						var callback = {
							success:handleSuccess,
							failure:handleFailure
						}
						var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
					}
			}
		}else{
			alert("只有状态为'未生效'的才能进行删除操作！");
		}
	};
	
	function doReset(){
		page.dataGroups.LmtQuotaAdjustGroup.reset();
	};
	
	/*--user code begin--*/
	function doBegin() {
		var paramStr = LmtQuotaAdjustList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = LmtQuotaAdjustList._obj.getSelectedData()[0].status._getValue();
			if(status!=2){
				alert("只状态为'未完成'的才能启用！")
			}else{
				Change4Status();
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function Change4Status() {
		var paramStr = LmtQuotaAdjustList._obj.getParamStr(['serno']);
		var status = LmtQuotaAdjustList._obj.getSelectedData()[0].status._getValue();
		if (paramStr != null){
				if(confirm("是否确认要启用？")){
					var url = '<emp:url action="updateStatus.do"/>?'+paramStr+'&status='+status;
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var flag = jsonstr.flag;
							if(flag=="success"){
								alert("启用成功!");
								window.location.reload();
						   }else {
							 alert(flag);
							 return;
						   }
						}
					};
					var handleFailure = function(o){	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
				}
		}
	};
	function doSubWFBefore() {
		var fin_agr_no = '<%=agr_no%>';
		if(confirm("是否确认要提交？")){
			var url = '<emp:url action="subLmtQuoAdjustWF.do"/>?&fin_agr_no='+fin_agr_no;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					var cus_id = jsonstr.cus_id;
					var cus_id_displayname = jsonstr.cus_id_displayname;
					var fin_totl_limit = jsonstr.fin_totl_limit;
					//alert(serno);
					//alert(cus_id);
					//alert(cus_id_displayname);
					//alert(fin_totl_limit);
					if(flag=="success"){
						WfiJoin.table_name._setValue("LmtAppFinGuar");
						WfiJoin.pk_col._setValue("serno");
						WfiJoin.pk_value._setValue(serno);
						WfiJoin.wfi_status._setValue("000");
						WfiJoin.status_name._setValue("approve_status");
						WfiJoin.appl_type._setValue("375");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
						WfiJoin.cus_id._setValue(cus_id);//客户码
						WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
						WfiJoin.amt._setValue(fin_totl_limit);//金额
						WfiJoin.prd_name._setValue("融资担保公司用信限额调整申请");//产品名称
						initWFSubmit(false);
				   }else {
					 alert(flag);
					 return;
				   }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:actButton id="getAddLmtQuotaAdjustPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtQuotaAdjustPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtQuotaAdjust" label="删除" op="remove"/>
		<emp:actButton id="viewLmtQuotaAdjust" label="查看" op="view"/>
		<emp:actButton id="getUpdateSingleAdjustPage" label="单户限额修改" op="update"/>
		<emp:actButton id="Begin" label="启用待生效" op="update"/>
		<emp:actButton id="subWFBefore" label="提交" op="update"/>
	</div>

	<emp:table icollName="LmtQuotaAdjustList" pageMode="true" url="pageLmtQuotaAdjustQuery.do" reqParams="agr_no=${context.agr_no}&op=${context.op}">
		<emp:text id="fin_totl_limit" label="融资总额" dataType="Currency" />
		<emp:text id="fin_totl_spac" label="融资总敞口" dataType="Currency" />
		<emp:text id="single_quota_his" label="单户限额(存量)" dataType="Currency"/>
		<emp:text id="single_quota_new" label="单户限额(新增)" dataType="Currency"/>
		<emp:text id="inure_date" label="生效日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:select id="status" label="状态" dictname="STD_ZB_STATUS_QUO"/>
		<%if(op.equals("update")){ %>
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<%}%>
		<emp:text id="fin_agr_no" label="融资协议编号" hidden="true" defvalue="<%=agr_no%>"/>
		<emp:text id="serno" label="主键" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>
    