<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String orgid = (String)context.getDataValue(CMISConstance.ATTR_ORGID);
%>
<emp:page>
<html>
<head>
<title>修改</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript" src="<emp:file fileName='scripts/workflow/newTime.js'/>"></script>
<script type="text/javascript">
	
	/*--user code begin--*/
	var oldVicar, oldVicarname;
	window.onload = function() {
		oldVicar = WfHumanstates.vicar._getValue();
		oldVicarname = WfHumanstates.vicarname._getValue();
		WfHumanstates.vicar._obj.addOneButton('selectButton','选取',selectVicar);
		changeType();
	}
	
	function selectVicar() {
		var contextPath="<%=request.getContextPath()%>";
		var orgid = "<%=orgid%>";
		//打开选择处理人的界面
		var url = contextPath+"/selectAllUser.do?count=1&orgIdTmp="+orgid+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
		var retObj = window.showModalDialog(url,'selectPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;');
			
		//返回数组:[true/fasle,userids,usernames]
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;
		var actorno = retObj[1];
		var userid = WfHumanstates.userid._getValue();
		if(actorno == userid) {
			alert("对不起，委托人与被委托人不能是同一人！");
			WfHumanstates.vicar._setValue('');
			WfHumanstates.vicarname._setValue('');
			return;
		}
		WfHumanstates.vicar._setValue(retObj[1]);
		WfHumanstates.vicarname._setValue(retObj[2]);
	}
	
	function setUserName(data) {
		var actorno = data.actorno._getValue();
		var userid = WfHumanstates.userid._getValue();
		if(actorno == userid) {
			alert("对不起，委托人与被委托人不能是同一人！");
			WfHumanstates.vicar._setValue(oldVicar);
			WfHumanstates.vicarname._setValue(oldVicarname);
			return;
		}
		WfHumanstates.vicar._setValue(data.actorno._getValue());
		WfHumanstates.vicarname._setValue(data.actorname._getValue());
	}	
		
	function checkDate(){
		
		var begTime = WfHumanstates.begintime._getValue();
		var openDay = '${context.OPENDAY}';
		if(begTime!='' && begTime < openDay) {
			alert("委托开始时间不能早于当前营业时间，请调整！");
			WfHumanstates.begintime._setValue('');
			return false;
		}
		
		var endTime = WfHumanstates.endtime._getValue();
		if(begTime!='' && endTime!=''){
			if(endTime < begTime) {
				alert("委托结束时间不能早于开始时间，请调整！");
				WfHumanstates.endtime._setValue('');
				return false;
			}
			
			
		}
		return true;
	}	

	function doSubmit1() {
		if(WfHumanstates._checkAll()) {
			if(checkDate()) {
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert(e);
							return;
						}
						var retKcoll = jsonstr.retKcoll;
						var result = retKcoll.result;
						if(result==1) {
							var begintime = retKcoll.begintime;
							var endtime = retKcoll.endtime;
							alert('您已存在重叠的委托期限['+begintime+']-['+endtime+']，请调整！');
							return;
						} else {
							submitForm();
						}
					}
				};
				var handleFailure = function(o){	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var form = document.getElementById("dateForm");
				WfHumanstates._toForm(form);
				optype._toForm(form);
				var url = form.action;
				var postData = YAHOO.util.Connect.setForm(form);	 
				var o = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) ;
			}
		}
	}

	function submitForm() {
		var form = document.getElementById("submitForm");
		WfHumanstates._toForm(form);
		form.submit();
	}
	
	function changeType() {
		var type = WfHumanstates.vicarioustype._getValue();
		if(type == '0') {
			WfHumanstates.appid._setValue('');
			WfHumanstates.appid._obj._renderRequired(false);
			WfHumanstates.appid._obj._renderHidden(true);
			//WfHumanstates.appname._setValue('');
			//WfHumanstates.appname._obj._renderRequired(false);
			//WfHumanstates.appname._obj._renderHidden(true);
		} else {
			WfHumanstates.appid._obj._renderRequired(true);
			WfHumanstates.appid._obj._renderHidden(false);
			//WfHumanstates.appname._obj._renderRequired(true);
			//WfHumanstates.appname._obj._renderHidden(false);
		}
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="dateForm" action="queryWfHumanstatsByTime.do" method="post">
		<emp:text id="optype" label="操作类型" hidden="true" defvalue="update"/>
	</emp:form>
	<emp:form id="submitForm" action="updateWfHumanstatesRecord.do" method="POST">
		<emp:gridLayout id="WfHumanstatesGroup" maxColumn="2" title="工作委托设置">

			<emp:text id="WfHumanstates.userid" label="委托人用户名" required="true" defvalue="${context.currentUserId}" readonly="true"/>
			<emp:text id="WfHumanstates.username" label="委托人姓名" required="true" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="WfHumanstates.vicar" label="被委托人用户名" required="true" readonly="true"/>
			<emp:text id="WfHumanstates.vicarname" label="被委托人姓名" maxlength="32" required="true" readonly="true"/>
			<emp:text id="WfHumanstates.begintime" label="开始时间" required="true" onclick="setday(this)" readonly="true"/>
			<emp:text id="WfHumanstates.endtime" label="结束时间" required="true" onclick="setday(this)" readonly="true"/>
			<emp:text id="WfHumanstates.onoff" label="是否启用" maxlength="1" required="false" hidden="true" defvalue="0"/>
			<emp:radio id="WfHumanstates.vicarioustype" label="委托类型" required="true" defvalue="0" dictname="WF_VICARIOUSTYPE" onclick="changeType()" colSpan="2"/>
			<emp:select id="WfHumanstates.appid" label="申请类型" required="false" hidden="true" dictname="ZB_BIZ_CATE"/>
			<emp:text id="WfHumanstates.appname" label="APPNAME" maxlength="50" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.wfid" label="WFID" maxlength="32" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.wfname" label="WFNAME" maxlength="50" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.orgid" label="ORGID" maxlength="32" required="false" hidden="true"/>
			<emp:text id="WfHumanstates.pkey" label="流水号" maxlength="32" hidden="true"/>
			<emp:text id="sysid" label="SYSID" hidden="true" defvalue="${context.sysId }"/>
			<emp:textarea id="WfHumanstates.vicarmemo" label="说明" maxlength="500" required="false" colSpan="2"/>
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit1" label="修 改" op="update"/>
			<emp:button id="reset" label="取 消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
