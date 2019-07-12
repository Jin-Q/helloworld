<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//-------异步保存主表单页面信息-------
	function doSave(){
		var mainid = PrdSubTabAction.main_id._getValue();
		var subid = PrdSubTabAction.sub_id._getValue();
		if(!PrdSubTabAction._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		PrdSubTabAction._toForm(form);
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
					alert("保存成功！");
					var url = '<emp:url action="queryPrdSubTabActionList.do"/>?mainid='+mainid+'&subid='+subid;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="addPrdSubTabActionRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}; 

	function doDelete(){  
		var main_act_id = PrdSubTabAction.main_act_id._getValue();
		var main_id = PrdSubTabAction.main_id._getValue();  
		var sub_id =PrdSubTabAction.sub_id._getValue();
		if(main_act_id!=""){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var subactid = jsonstr.subactid;
				if(flag == "succ"){  
					var options = PrdSubTabAction.sub_act_id._obj.element.options;
					var list = new Array();	
					list = subactid.split(",");
					for(var i=0;i<=list.length-1;i++){ 
						for(var j=options.length-1;j>=0;j--){
				             if(list[i]==options[j].value){  
					             if(list[i]!=""){
					            	 options.remove(j); 
						         }
				             }    
						}	
					}  
				}else {
					alert("请选择相应值");
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

		var url = '<emp:url action="checkPrdSubTabActionRecord.do"/>&main_act_id='+main_act_id+"&main_id="+main_id+"&sub_id="+sub_id;
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		}
    }

   		  				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addPrdSubTabActionRecord.do" method="POST">
		<emp:gridLayout id="PrdSubTabActionGroup" title="从资源操作权限管理" maxColumn="2">
			<emp:text id="PrdSubTabAction.pkid" label="主键" maxlength="40" defvalue="${context.pkid}" colSpan="2" hidden="true" required="true" />
			<emp:text id="PrdSubTabAction.main_id" label="主资源关联模块" maxlength="80" defvalue="${context.mainid}" readonly="true"  required="true" />
			<emp:select id="PrdSubTabAction.main_act_id" label="主资源操作ID" dictname="MAIN_ACTION_TYPE" required="true" onchange="doDelete()"/> 
			<emp:text id="PrdSubTabAction.sub_id" label="从资源关联模块" maxlength="80" defvalue="${context.subid}" readonly="true" required="true" />
			<emp:select id="PrdSubTabAction.sub_act_id" label="从资源操作ID" dictname="SUB_ACTION_TYPE" required="true" />
			<emp:pop id="PrdSubTabAction.rule" label="过滤规则" url="rulespop.do?id=PrdPvRiskItem.item_rules" required="false" buttonLabel="选择规则" />
			<emp:textarea id="PrdSubTabAction.memo" label="备注" maxlength="200" colSpan="2" required="false" />
			<emp:text id="PrdSubTabAction.input_id" label="登记人员" readonly="true" hidden="true" maxlength="40" defvalue="${context.currentUserId}" required="false" />
			<emp:date id="PrdSubTabAction.input_date" label="登记日期" readonly="true" hidden="true" required="false" defvalue="${context.OPENDAY}" />
			<emp:text id="PrdSubTabAction.input_br_id" label="登记机构" readonly="true" hidden="true" maxlength="20" defvalue="${context.organNo}" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

