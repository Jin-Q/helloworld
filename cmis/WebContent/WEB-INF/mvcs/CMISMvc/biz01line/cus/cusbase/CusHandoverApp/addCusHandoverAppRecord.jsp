<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
/**
 * 客户移交整体做法：
 * 跨机构移交只能移交 客户资料 不能移交客户业务
 * 移交客户资料  不需要监交人，点击提交直接提交给 接收人。
 * bsbcmis
 */
	//var userUrl = '<emp:url action="getValueQuerySUserPopListOp.do?restrictUsed=false"/>';
	var userUrl = '<emp:url action="getAllSUserPopListOp.do?restrictUsed=false"/>';
	userUrl = EMPTools.encodeURI(userUrl);
	var orgUrl = '<emp:url action="getValuequerySOrgList.do?restrictUsed=false"/>';
	orgUrl = EMPTools.encodeURI(orgUrl);
	window.onload = function(){
		//先把接收人设置为只读形式，当选择接受机构后，再去掉只读控制，这样式为了实现在接受机构中选择客户经理。
		CusHandoverApp.receiver_id_displayname._obj._renderReadonly(true);	
		//接收机构
		CusHandoverApp.receiver_br_id_displayname._obj._renderReadonly(true);
	};
	
	//接收机构与移出机构关系 的控制
	function CheakOrgType(){
		var orgType = CusHandoverApp.org_type._obj.element.value;
		var organno  ='${context.organNo}';
		var organname  =CusHandoverApp.handover_br_id_displayname._obj.element.value;
		var userId  ='${context.currentUserId}';
		
		if(orgType!=null && orgType!=""){
			//机构内移交
			if(orgType=='10'){
				//接收机构的处理
				CusHandoverApp.receiver_br_id._obj.element.value=organno;
				CusHandoverApp.receiver_br_id_displayname._obj.element.value=organname;
				CusHandoverApp.receiver_br_id_displayname._obj._renderReadonly(true);
				//接收人的处理
				CusHandoverApp.receiver_id_displayname._obj._renderReadonly(false);
				var orgtype = CusHandoverApp.org_type._getValue();
			//	CusHandoverApp.receiver_id_displayname._obj.config.url = userUrl+"&SUser.orgid="+organno+"&fromType=transfer"+"&org_type="+orgtype;
				//CusHandoverApp.receiver_id_displayname._obj.config.url = userUrl+"&fromType=transfer"+"&org_type="+orgtype;
				var url="<emp:url action='getAllSUserPopListOp.do'/>&restrictUsed=false&flag=receive&receiveOrg="+organno+"&fromType=transfer"+"&org_type="+orgType;
				CusHandoverApp.receiver_id_displayname._obj.config.url = EMPTools.encodeURI(url);
				//机构内移交可以移交业务
                CusHandoverApp.handover_mode._obj._renderReadonly(false);
				
				return;
            //同一联社内跨机构移交
			}else if(orgType=='21'){
				//接收机构的处理
				CusHandoverApp.receiver_br_id_displayname._obj._renderReadonly(false);
				CusHandoverApp.receiver_br_id._obj.element.value="";
				CusHandoverApp.receiver_br_id_displayname._obj.element.value="";
				//接收人
				CusHandoverApp.receiver_id._obj.element.value="";
				CusHandoverApp.receiver_id_displayname._obj.element.value="";
				var url="<emp:url action='getAllSUserPopListOp.do'/>&restrictUsed=false&flag=receive&fromType=transfer"+"&org_type="+orgType;
				CusHandoverApp.receiver_id_displayname._obj.config.url = EMPTools.encodeURI(url);
				//跨机构移交只能移交客户资料
                //CusHandoverApp.handover_mode._setValue("1");//仅客户资料移交
                //CusHandoverApp.handover_mode._obj._renderReadonly(true);
			}
		}
    }
    
	
    //返回操作
	function doReturn(){
		var url = '<emp:url action="queryCusHandoverAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	
	function orgRupervise(data){
		CusHandoverApp.supervise_br_id._setValue(data.organno._getValue());
		CusHandoverApp.supervise_br_id_displayname._setValue(data.organname._getValue());
		//去掉接受人的只读控制
        //CusHandoverApp.supervise_id_displayname._obj._renderReadonly(false);
        //接受人值设置为空
        CusHandoverApp.supervise_id._setValue("");
        //CusHandoverApp.supervise_id_displayname._setValue("");
	}
    //客户经理的POP
	function idHandover(data){
		CusHandoverApp.handover_id._setValue(data.actorno._getValue());
		CusHandoverApp.handover_id_displayname._setValue(data.actorname._getValue());
	}
	function orgHandover(data){
		CusHandoverApp.handover_br_id._setValue(data.organno._getValue());
		CusHandoverApp.handover_br_id_displayname._setValue(data.organname._getValue());
	}
	function idReceiver(data){
		var managerId= data.actorno._getValue();
		var managerIdName= data.actorname._getValue();
		var handoverId = CusHandoverApp.handover_id._obj.element.value;
		var handoverIdName = CusHandoverApp.handover_id_displayname._obj.element.value;
		if(managerId==handoverId){
            alert("移出人和接收人不能是同一人!");
            return;
		}else{
			CusHandoverApp.receiver_id._setValue(managerId);
			CusHandoverApp.receiver_id_displayname._setValue(managerIdName);
		}
	}
	function orgReceiver(data){
        var retBrId = data.organno._getValue();
        var retBrIdName = data.organname._getValue();
		var orgType = CusHandoverApp.org_type._obj.element.value;
		if(orgType=='21'){
			var hanBrId = CusHandoverApp.handover_br_id._obj.element.value;//移出机构
			var hanBrIdName = CusHandoverApp.handover_br_id_displayname._obj.element.value;//移出机构
			if(hanBrId==retBrId){
				alert("不是机构内移交\n[移出机构]和[接收机构]不能 是同一个！");
				return;
			}
		}
		CusHandoverApp.receiver_br_id._setValue(retBrId);
		CusHandoverApp.receiver_br_id_displayname._setValue(retBrIdName);
		//去掉接受人的只读控制
		CusHandoverApp.receiver_id_displayname._obj._renderReadonly(false);
		//接受人值设置为空
		CusHandoverApp.receiver_id._setValue("");
		CusHandoverApp.receiver_id_displayname._setValue("");
	//	CusHandoverApp.receiver_id_displayname._obj.config.url = userUrl+"&SUser.orgid="+retBrId+"&fromType=transfer"+"&org_type="+orgType;
		//CusHandoverApp.receiver_id_displayname._obj.config.url = userUrl+"&fromType=transfer"+"&org_type="+orgType;
		var url="<emp:url action='getAllSUserPopListOp.do'/>&restrictUsed=false&flag=receive&receiveOrg="+retBrId+"&fromType=transfer"+"&org_type="+orgType;
		CusHandoverApp.receiver_id_displayname._obj.config.url = EMPTools.encodeURI(url);
	}

	//返回主管客户经理	
	function setconId(data){
		CusHandoverApp.manager_id._setValue(data.actorno._getValue());
		CusHandoverApp.manager_id_displayname._setValue(data.actorname._getValue());
		CusHandoverApp.manager_br_id._setValue(data.orgid._getValue());
		CusHandoverApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusHandoverApp.manager_br_id_displayname._obj._renderReadonly(true);
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
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusHandoverApp.manager_br_id._setValue(jsonstr.org);
					CusHandoverApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusHandoverApp.manager_br_id._setValue("");
					CusHandoverApp.manager_br_id_displayname._setValue("");
					CusHandoverApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusHandoverApp.manager_id._getValue();
					CusHandoverApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusHandoverApp.manager_br_id._setValue("");
					CusHandoverApp.manager_br_id_displayname._setValue("");
					CusHandoverApp.manager_br_id_displayname._obj._renderReadonly(false);
					CusHandoverApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusHandoverApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		CusHandoverApp.manager_br_id._setValue(data.organno._getValue());
		CusHandoverApp.manager_br_id_displayname._setValue(data.organname._getValue());
	}


	//移交方式值改变事件   需求编号：HS140903009   2014-10-14  唐顺岩 
	function changeMode(mode_value){
		if(mode_value=="3"){ //如果移交方式为“3-客户与业务移交-机构撤并” 则将移交范围固定为 “2-客户经理所有客户与业务” 
			CusHandoverApp.handover_scope._setValue("2");
			CusHandoverApp.handover_scope._obj._renderReadonly(true);
		}else{
			CusHandoverApp.handover_scope._setValue("");
			CusHandoverApp.handover_scope._obj._renderReadonly(false);
		}
	}

</script>
</head>
<body class="page_content" >
	<emp:form id="submitForm" action="addCusHandoverAppRecord.do" method="POST">
		<emp:gridLayout id="CusHandoverAppGroup" title="客户移交申请" maxColumn="2">
			<emp:text id="CusHandoverApp.serno" label="申请流水号" maxlength="40" required="false" colSpan="2" readonly="true" hidden="true"/>
			<emp:select id="CusHandoverApp.org_type" label="接收机构与移出机构关系" required="true" dictname="STD_ZB_ORG_TYPE" onclick="CheakOrgType()" colSpan="2" />
			<emp:select id="CusHandoverApp.handover_mode" label="移交方式" required="true" dictname="STD_ZB_HAND_TYPE" colSpan="2" onchange="changeMode(this.value)"/>
			<emp:select id="CusHandoverApp.handover_scope" label="移交范围" required="true" dictname="STD_ZB_HAND_SCOPE" colSpan="2"/>
			<emp:pop id="CusHandoverApp.handover_br_id_displayname" label="移出机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgHandover" required="true" readonly="true" defvalue="$organName"/>
			<emp:pop id="CusHandoverApp.handover_id_displayname" label="移出人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idHandover" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:pop id="CusHandoverApp.receiver_br_id_displayname" label="接收机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver" required="true" readonly="true" />
		<%//	<emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" required="true" readonly="true" /> %>
			<emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="getAllSUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" required="true" readonly="true" />
			<emp:textarea id="CusHandoverApp.handover_detail" label="移交说明" maxlength="250" required="true" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusHandoverAppGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusHandoverApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="CusHandoverApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="CusHandoverApp.manager_id" label="责任人" required="true" hidden="true" />
			<emp:text id="CusHandoverApp.manager_br_id" label="责任机构" required="true" hidden="true" />
			<emp:text id="CusHandoverApp.input_id_displayname" label="登记人"  required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="CusHandoverApp.input_br_id_displayname" label="登记机构"  required="true" readonly="true" defvalue="$organName"/>
			<emp:text id="CusHandoverApp.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY"/>
			<emp:text id="CusHandoverApp.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusHandoverApp.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			
			<emp:select id="CusHandoverApp.approve_status" label="状态" required="true" dictname="WF_APP_STATUS" hidden="true" readonly="true" defvalue="000"/>
			<emp:date id="CusHandoverApp.supervise_date" label="审批日期" required="false" hidden="true"/>
			<emp:date id="CusHandoverApp.receive_date" label="接收日期" required="false" hidden="true"/>
			<emp:pop id="CusHandoverApp.handover_br_id" label="移出机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgHandover" required="true" defvalue="$organNo" hidden="true"/>
			<emp:pop id="CusHandoverApp.handover_id" label="移出人" url="getValueQuerySUserPopListOp.do?queryCondition" returnMethod="idHandover" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:pop id="CusHandoverApp.receiver_br_id" label="接收机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver" required="true" hidden="true" />
            <emp:pop id="CusHandoverApp.receiver_id" label="接收人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" required="true" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>