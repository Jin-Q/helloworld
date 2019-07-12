<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	String apply_cur_type="";
	String apply_amount="";
	String prd_id="";
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	String modiflg = "";
	String modify_rel_serno = "";
	String wf_flag="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
	if(context.containsKey("apply_cur_type")){
		apply_cur_type =(String)context.getDataValue("apply_cur_type");
	}  
	if(context.containsKey("apply_amount")){
		apply_amount =(String)context.getDataValue("apply_amount");
	}  
	if(context.containsKey("prd_id")){
		prd_id =(String)context.getDataValue("prd_id");
	}
	if(context.containsKey("modiflg")){
	   modiflg = (String)context.getDataValue("modiflg");
	}
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAppendTerms._toForm(form);
		IqpAppendTermsList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppendTermsPage() {
		var paramStr = IqpAppendTermsList._obj.getParamStr(['append_terms_pk','serno']);
		if (paramStr != null) {
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var url = '<emp:url action="getIqpAppendTermsUpdatePage.do"/>?chrg_rate=${context.chrg_rate}&'+paramStr+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppendTerms() {
		var paramStr = IqpAppendTermsList._obj.getParamStr(['append_terms_pk','serno']);
		if (paramStr != null) {
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var wf_flag= '<%=wf_flag%>';
			var url="";
			if(wf_flag=="1"){
				url = '<emp:url action="getIqpAppendTermsViewPage.do"/>?'+paramStr+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+"&op=${context.op}&wf_flag="+wf_flag;
			}else{
				url = '<emp:url action="getIqpAppendTermsViewPage.do"/>?'+paramStr+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+"&op=${context.op}";
			}
			
			/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppendTermsPage() {
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var url = '<emp:url action="getIqpAppendTermsAddPage.do"/>?chrg_rate=${context.chrg_rate}&serno='+'<%=serno%>'+'&apply_cur_type='+'<%=apply_cur_type%>'+'&apply_amount='+'<%=apply_amount%>'+'&prd_id='+'<%=prd_id %>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAppendTerms() {
		var paramStr = IqpAppendTermsList._obj.getParamStr(['append_terms_pk','serno']);
		if (paramStr != null) {
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
						var msg = jsonstr.msg;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}else {
							alert(msg);
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
				/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
				var url = '<emp:url action="deleteIqpAppendTermsRecord.do"/>?'+paramStr+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
				/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppendTermsGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
		<emp:actButton id="getAddIqpAppendTermsPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAppendTermsPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAppendTerms" label="删除" op="remove"/>
		<%if("1".equals(wf_flag)){ %>
			<emp:button id="viewIqpAppendTerms" label="查看" />
		<%}else{ %>
			<emp:button id="viewIqpAppendTerms" label="查看" op="view"/>
		<%} %>
	</div>

	<emp:table icollName="IqpAppendTermsList" pageMode="true" url="pageIqpAppendTermsQuery.do">
		<emp:text id="append_terms_pk" label="主键" hidden="true"/>
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="fee_code" label="费用描述" dictname="STD_ZB_FEE_CODE"/>
		<emp:text id="fee_type" label="费用类型" dictname="STD_ZB_FEE_MODE"/>
		<emp:text id="fee_amt" label="费用总金额" dataType="Currency"/>
		<emp:text id="fee_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="fee_rate" label="费用比率" dataType="Rate"/>
		<emp:text id="is_cycle_chrg" label="是否周期性收费" dictname="STD_ZX_YES_NO" />
		<!-- add by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin-->
		<emp:text id="modify_rel_serno" label="打回业务修改流水号" hidden="true"/>
		<!-- add by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end-->
	</emp:table>
	
</body>
</html>
</emp:page>
    