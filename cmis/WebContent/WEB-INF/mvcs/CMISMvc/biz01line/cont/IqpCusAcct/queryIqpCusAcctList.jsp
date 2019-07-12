<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String serno="";
	String is_agent_disc="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
	if(context.containsKey("is_agent_disc")){
		is_agent_disc =(String)context.getDataValue("is_agent_disc");
	}
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	String modiflg = "";
	String modify_rel_serno = "";
	String wf_flag="";
	String cont_no="";
	if(context.containsKey("modiflg")){
		modiflg = (String)context.getDataValue("modiflg");
	}
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	if(context.containsKey("cont_no")){
		cont_no = (String) context.getDataValue("cont_no");
	}
	if(context.containsKey("op")){
		op = (String) context.getDataValue("op");
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
		IqpCusAcct._toForm(form);
		IqpCusAcctList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCusAcctPage() {
		var paramStr = IqpCusAcctList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCusAcctUpdatePage.do"/>?'+paramStr+'&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCusAcct() {
		var paramStr = IqpCusAcctList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var modiflag='<%=modiflg%>';
			if(modiflag =="yes"){
				var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?menuIdTab=${context.menuIdTab}&'+paramStr+'&prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+'&cont_no='+'<%=cont_no%>';
				url = EMPTools.encodeURI(url);
				window.open(url,'iqpCusAcct','height=500,width=800,top=80,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{
				var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?menuIdTab=${context.menuIdTab}&'+paramStr+'&prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno=${context.serno}'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCusAcctPage() {
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var url = '<emp:url action="getIqpCusAcctAddPage.do"/>?prd_id=${context.prd_id}&is_close_loan=${context.is_close_loan}&is_agent_disc=${context.is_agent_disc}&serno='+'<%=serno%>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+'&cont_no='+'<%=cont_no%>';
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpCusAcct() {
		var paramStr = IqpCusAcctList._obj.getParamStr(['pk_id']);
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
							if(flag == "success"){
								alert("删除成功!");
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteIqpCusAcctRecord.do"/>?'+paramStr+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
					/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpCusAcctGroup.reset();
	};
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	function doCheckIsCanBeDetele(){
		var paramStr = IqpCusAcctList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var acct_no  = IqpCusAcctList._obj.getParamValue(['acct_no']);
			var acct_attr = IqpCusAcctList._obj.getParamValue(['acct_attr']);
			
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
						doDeleteIqpCusAcct();
					}else if(flag == "forbidden"){
						alert("打回业务修改流程不允许删除原账户信息!");
					}else{
						alert("账户信息校验发生异常!");
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
			var url = '<emp:url action="checkIsCanBeDetele.do"/>?&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'+"&acct_no="+acct_no+"&acct_attr="+acct_attr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		}else{
			alert('请先选择一条记录！');
		}
	};
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	
	
</script>
</head>
<body class="page_content">
	
	<div align="left">
	<%if("yes".equals(modiflg) && !"view".equals(op)){ %>
		<emp:button id="getAddIqpCusAcctPage" label="新增" />
		<emp:button id="checkIsCanBeDetele" label="删除" />
		<emp:button id="viewIqpCusAcct" label="查看" />
	<%}else{ %>
		<emp:actButton id="getAddIqpCusAcctPage" label="新增" op="add"/>
		<!--<emp:actButton id="getUpdateIqpCusAcctPage" label="修改" op="update"/>-->
		<emp:actButton id="deleteIqpCusAcct" label="删除" op="remove"/>
		<emp:actButton id="viewIqpCusAcct" label="查看" op="view"/>
	<%} %>
		
	</div>

	<emp:table icollName="IqpCusAcctList" pageMode="true" url="pageIqpCusAcctQuery.do" reqParams="serno=${context.serno}&cont_no=${context.cont_no}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="pk_id" label="pkid" hidden="true"/> 
		<emp:text id="acct_no" label="账户账号" />
		<emp:text id="acct_name" label="账户名称" />
		<emp:text id="acct_attr" label="账户属性" dictname="STD_ZB_BR_ID_ATTR" />
		<emp:text id="is_this_org_acct" label="是否本行账户" dictname="STD_ZX_YES_NO" />
		<emp:text id="opac_org_no" label="开户行行号" hidden="true"/>
		<emp:text id="opan_org_name" label="开户行行名" />
		<emp:text id="pay_amt" label="支付金额" hidden="true"/>		
	</emp:table>
	
</body>
</html>
</emp:page>
    