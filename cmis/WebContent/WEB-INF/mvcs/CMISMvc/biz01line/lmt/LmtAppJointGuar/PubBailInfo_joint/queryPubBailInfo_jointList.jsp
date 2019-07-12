<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	/**add by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String modiflg = "";
    if(context.containsKey("modiflg")){
    	modiflg = (String)context.getDataValue("modiflg");
    }
    String modify_rel_serno = "";
    if(context.containsKey("modify_rel_serno")){
    	modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
    }
    String cont_no = "";
    if(context.containsKey("cont_no")){
    	cont_no = (String)context.getDataValue("cont_no");
    }
    /**add by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<% 
	String serno = request.getParameter("serno");
	//String cus_id = request.getParameter("cus_id");
%>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PubBailInfo._toForm(form);
		PubBailInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.PubBailInfoGroup.reset();
	};
	
	/*--user code begin--*/
	
	function doUpdate() {
		var paramStr = PubBailInfoList._obj.getParamStr(['serno','cus_id','bail_acct_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getPubBailInfo_jointUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//var param = 'dialogWidth:800px';
			//window.showModalDialog(url,'',param);
			//window.location.reload();
			window.location = url; 
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doView() {
		var paramStr = PubBailInfoList._obj.getParamStr(['serno','cus_id','bail_acct_no']);
		if (paramStr != null) {
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var modiflg = '<%=modiflg%>';
			var modify_rel_serno = '<%=modify_rel_serno%>';
			var url = '<emp:url action="getPubBailInfo_jointViewPage.do"/>?'+paramStr+"&modiflg="+modiflg+"&modify_rel_serno="+modify_rel_serno;
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			url = EMPTools.encodeURI(url);
			//var param = 'dialogWidth:800px';
			//window.showModalDialog(url,'',param);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doAdd() {
		var serno = "${context.serno}";
		var cus_id = "${context.cus_id}";
		var cont_no = "${context.cont_no}";
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var modiflg = '<%=modiflg%>';
		var modify_rel_serno = '<%=modify_rel_serno%>';
		var cont_no ='<%=cont_no%>'
		var url = '<emp:url action="getPubBailInfo_jointAddPage.do"/>?serno=' + serno+'&cus_id='+cus_id+'&cont_no='+cont_no+"&modiflg="+modiflg+"&modify_rel_serno="+modify_rel_serno+"&cont_no="+cont_no;
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		url = EMPTools.encodeURI(url);
		//var param = 'dialogWidth:800px';
		//window.open(url,'',param);
		//window.location.reload();
		window.location = url; 
	};
	
	function doDelete() {
		var paramStr = PubBailInfoList._obj.getParamStr(['serno','bail_acct_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
				var modiflg = '<%=modiflg%>';
				var modify_rel_serno = '<%=modify_rel_serno%>';
				var url = '<emp:url action="deletePubBailInfo_jointRecord.do"/>?'+paramStr+"&modiflg="+modiflg+"&modify_rel_serno="+modify_rel_serno;
				/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
				url = EMPTools.encodeURI(url);
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							window.location.reload();
							alert("删除成功!");
							serno = "${context.serno}";
							var cus_id = "${context.cus_id}";
							var url = '<emp:url action="queryPubBailInfo_jointList.do"/>?serno=' + serno + '&cus_id=' + cus_id;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else if(flag == "modifySuc"){
							alert("删除成功!");
							window.location.reload();
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
		<emp:actButton id="add" label="新增" op="add"/>
		<emp:actButton id="delete" label="删除" op="remove"/>
		<emp:actButton id="view" label="查看" op="view"/>
	</div>

	<emp:table icollName="PubBailInfoList" pageMode="true" url="pagePubBailInfo_jointQuery.do" reqParams="serno=${context.serno}&cont_no=${context.cont_no}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_no" label="合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>   
		<emp:text id="cus_id_displayname" label="客户名称" hidden="true"/>
		<emp:text id="bail_acct_no" label="保证金账户账号" />
		<emp:text id="bail_acct_name" label="保证金账户名称" />
		<emp:text id="bail_type" label="保证金类型" dictname="STD_PUB_BAIL_TYPE"/>  
		<emp:text id="open_org_displayname" label="开户机构" />
		<!-- modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
		<emp:text id="modify_rel_serno" label="打回业务修改流水编号" hidden="true"/> 
		<!-- modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
	</emp:table>  
	
</body>
</html>
</emp:page>
    