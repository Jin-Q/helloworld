<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	String openType = request.getParameter("openType");
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		var url = '<emp:url action="queryMortStorExwaInfoList.do"/>?menuId=queryMortStorExwaInfoList';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function doClose(){
		window.close();
	}
	
	function doNext(){
		if(!MortStorExwaInfo._checkAll()){
			return;
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("修改失败！");
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){	
					alert("修改成功");
					window.location.reload();
				}else{
					alert("修改失败");
				}   
			}	
		};
		var handleFailure = function(o) {
			alert("修改失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		MortStorExwaInfo._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	//归还时间校验
	function checkDt(){
		var occur_date = MortStorExwaInfo.back_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(!CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('归还时间不能小于当前日期！');
	    		MortStorExwaInfo.back_date._obj.element.value="";
	    		return;
	    	}
    	}
	}	
	function setconId(data){
		MortStorExwaInfo.manager_id_displayname._setValue(data.actorname._getValue());
		MortStorExwaInfo.manager_id._setValue(data.actorno._getValue());
		MortStorExwaInfo.manager_br_id._setValue(data.orgid._getValue());
		MortStorExwaInfo.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		MortStorExwaInfo.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					MortStorExwaInfo.manager_br_id._setValue(jsonstr.org);
					MortStorExwaInfo.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					MortStorExwaInfo.manager_br_id._setValue("");
					MortStorExwaInfo.manager_br_id_displayname._setValue("");
					MortStorExwaInfo.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = MortStorExwaInfo.manager_id._getValue();
					MortStorExwaInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					MortStorExwaInfo.manager_br_id._setValue("");
					MortStorExwaInfo.manager_br_id_displayname._setValue("");
					MortStorExwaInfo.manager_br_id_displayname._obj._renderReadonly(false);
					MortStorExwaInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = MortStorExwaInfo.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		MortStorExwaInfo.manager_br_id._setValue(data.organno._getValue());
		MortStorExwaInfo.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	//根据出入库方式判断是否需要隐藏归还时间字段
	function doChange(){
		var stor_exwa_mode=MortStorExwaInfo.stor_exwa_mode._getValue();
		if(stor_exwa_mode=="02"){//借出的时候需要显示归还时间
			MortStorExwaInfo.back_date._obj._renderHidden(false);
			MortStorExwaInfo.back_date._obj._renderRequired(true);
		}else{//除借出外
			MortStorExwaInfo.back_date._obj._renderHidden(true);
			MortStorExwaInfo.back_date._obj._renderRequired(false);
			MortStorExwaInfo.back_date._setValue('');
		}
	}
	function doLoad(){
		var options = MortStorExwaInfo.stor_exwa_mode._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			//判断出入库方式，若为"入库"，去掉选项
			if(options[i].value == "04"||options[i].value == "05"){
			options.remove(i);
			}
		}  
		doChange();
	}
</script>
</head>
<body class="page_content" onload="doLoad()">
	
<emp:tabGroup id="MortStorExwaInfoTab" mainTab="appinf">
   <emp:tab label="申请信息" id="appinf"  needFlush="true" initial="true" >
	<emp:form id="submitForm" action="updateMortStorExwaInfoRecord.do" method="POST">
		<emp:gridLayout id="MortStorExwaInfoGroup" maxColumn="2" title="押品出入库申请">
			<emp:text id="MortStorExwaInfo.serno" label="业务编号" maxlength="60" required="false" readonly="true" colSpan="2"/>
			<emp:select id="MortStorExwaInfo.stor_exwa_mode" label="出入库方式" required="true" dictname="STD_STOR_EXWA_MODEL" onchange="doChange()"/>
			<emp:date id="MortStorExwaInfo.back_date" label="预计归还时间" required="true" onblur="checkDt()"/>
			<emp:textarea id="MortStorExwaInfo.exwa_memo" label="出库说明" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortStorExwaInfoGroup" maxColumn="2" title="登记信息">	
		    <emp:pop id="MortStorExwaInfo.manager_id_displayname" label="主管客户经理" required="true" readonly="false" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="MortStorExwaInfo.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" readonly="true"/>
			<emp:text id="MortStorExwaInfo.input_id_displayname" label="登记人" required="false" readonly="true" hidden="false" defvalue="$currentUserName"/>
			<emp:text id="MortStorExwaInfo.input_br_id_displayname" label="登记机构" required="false" readonly="true" hidden="false" defvalue="$organName"/>
			<emp:text id="MortStorExwaInfo.input_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:select id="MortStorExwaInfo.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="false"/>
			<emp:text id="MortStorExwaInfo.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="MortStorExwaInfo.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="MortStorExwaInfo.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="MortStorExwaInfo.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="修改"/>
		<%if(openType!=null&&"Y".equals(openType)){ %>	
			<emp:button id="close" label="关闭"/>
		<%}else{ %>
			<emp:button id="return" label="返回"/>
		<%} %>	
		</div>
	</emp:form>
	</emp:tab>
    <emp:tab id="warrant_tab" label="权证详情" url="queryMortGuarantyCertiInfoAddList.do" reqParams="serno=${context.serno}&stor_exwa_mode=${context.MortStorExwaInfo.stor_exwa_mode}" initial="false" needFlush="true"/>
</emp:tabGroup>
</body>
</html>
</emp:page>
