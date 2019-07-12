<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
String biz_area_type = (String)request.getParameter("biz_area_type"); 
%>
<script type="text/javascript">

	/*--user code begin--*/
	function doOnload(){
		changeSharedScope();
		var serno = LmtAppBizArea.serno._getValue();
		var biz_area_type ="<%=biz_area_type%>" ; 
		var url_add = '<emp:url action="addLmtAppBizAreaRecord.do"/>?biz_area_type=' + biz_area_type;
		url_add = EMPTools.encodeURI(url_add);
		document.getElementById("submitForm").action = url_add;
	}

	function changeSharedScope(){
		var share_range = LmtAppBizArea.share_range._getValue();
		
		if( share_range == '' || share_range == '1' ){
			LmtAppBizArea.belg_org._obj._renderHidden(true);
			LmtAppBizArea.belg_org._obj._renderRequired(false);
			LmtAppBizArea.belg_org_displayname._obj._renderHidden(true);
			LmtAppBizArea.belg_org_displayname._obj._renderRequired(false);
		}else{
			LmtAppBizArea.belg_org_displayname._obj._renderHidden(false);
			LmtAppBizArea.belg_org_displayname._obj._renderRequired(true);

			LmtAppBizArea.belg_org._obj._renderHidden(false);
			LmtAppBizArea.belg_org._obj._renderRequired(true);
		}
	}
	
	//返回主管机构
	function getOrganName(data){
		LmtAppBizArea.belg_org._setValue(data[0]);
		LmtAppBizArea.belg_org_displayname._setValue(data[1]);
	}

	//保存全部
	function doSaveAll(){
		var type = "<%=biz_area_type%>";
		var form = document.getElementById("submitForm");
		LmtAppBizArea._toForm(form);
		var callback = {
			success : "doReturnMethod",
			isJSON : true
		};
		if( type== 0 && LmtAppBizArea._checkAll() && LmtAppBizAreaComn._checkAll()){
			LmtAppBizAreaComn._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);		
			EMPTools.ajaxRequest('POST',form.action, callback,postData);
		}else if(type == 1 && LmtAppBizArea._checkAll() && LmtAppBizAreaCore._checkAll()){
			LmtAppBizAreaCore._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);		
			EMPTools.ajaxRequest('POST',form.action, callback,postData);
		}else if(type == 2 && LmtAppBizArea._checkAll()){
			var recrdCount = LmtAppBizAreaSupmkList._obj.recordCount;
			if(recrdCount==0){
				alert('目标客户群信息不能为空!');
				return;
			}
			LmtAppBizAreaSupmkList._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);		
			EMPTools.ajaxRequest('POST',form.action, callback,postData);
		}
	}
	
    function doReturnMethod(json, callback){
    	var type = "<%=biz_area_type%>";
    	if('success'==json.flag){
    		alert("保存成功！");
    		var serno = json.serno;
			var url = '<emp:url action="getLmtAppBizAreaUpdatePage.do"/>?serno='+serno+"&biz_area_type="+type ;
			url = EMPTools.encodeURI(url);
			window.location = url;
    	}else{
            alert("保存失败！");
        }
    }
	
	function setManagerId(data){
		LmtAppBizArea.manager_id._setValue(data.actorno._getValue());
		LmtAppBizArea.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppBizArea.manager_br_id._setValue(data.orgid._getValue());
		LmtAppBizArea.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppBizArea.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppBizArea.manager_br_id._setValue(jsonstr.org);
					LmtAppBizArea.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppBizArea.manager_br_id._setValue("");
					LmtAppBizArea.manager_br_id_displayname._setValue("");
					LmtAppBizArea.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppBizArea.manager_id._getValue();
					LmtAppBizArea.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppBizArea.manager_br_id._setValue("");
					LmtAppBizArea.manager_br_id_displayname._setValue("");
					LmtAppBizArea.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppBizArea.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppBizArea.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName2(data){
		LmtAppBizArea.manager_br_id._setValue(data.organno._getValue());
		LmtAppBizArea.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	function doReturn() {
		var url = '<emp:url action="queryLmtAppBizAreaList.do"/>?process=no';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//新增超市百货类
	function doGetAddLmtAppBizAreaSupmkPage(){
	/*	var serno = "${context.serno}";
		if(serno==null||serno==""||serno=="null"){
			alert("请先保存额度申请信息再新增目标客户群信息！");
			return;
		}*/
		LmtAppBizAreaSupmkList._obj._addRow();
		LmtAppBizAreaSupmkList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = LmtAppBizAreaSupmkList._obj.recordCount;//取总记录数
		LmtAppBizAreaSupmkList._obj.data[recordCount-1].serno._setValue(serno);//设置业务编号的值
	}

	//删除超市百货类
	function doDeleteLmtAppBizAreaSupmk(){
		var dataRow =  LmtAppBizAreaSupmkList._obj.getSelectedData()[0];  
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var idx = LmtAppBizAreaSupmkList._obj.getSelectedIdx();  //得到选中行的下标
				LmtAppBizAreaSupmkList._obj._deleteRow(idx);   //删除选中行
				LmtAppBizAreaSupmkList._obj.recordCount -=1; 	//减少总记录数
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	//校验金额
	function checkAmt(obj){
		var amt = parseFloat(LmtAppBizArea.lmt_totl_amt._getValue());
		var per = parseFloat(LmtAppBizArea.single_max_amt._getValue());
		if(per > amt){
			alert("单户限额要小于等于授信总额!");
		//	LmtAppBizArea.single_max_amt._setValue(0);
			obj._setValue('');
		}
		if(per > 5000000){
			alert("单户限额不能超过500万!");
			LmtAppBizArea.single_max_amt._setValue('');
		}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="addLmtAppBizAreaRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppBizAreaGroup" title="圈商准入额度申请" maxColumn="2">
			<emp:text id="LmtAppBizArea.serno" label="业务流水号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:select id="LmtAppBizArea.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="changeSharedScope()"/>
			<emp:pop id="LmtAppBizArea.belg_org" label="所属机构" colSpan="2" url="queryMultiSOrgPop.do" returnMethod="getOrganName" required="true" />
			<emp:textarea id="LmtAppBizArea.belg_org_displayname"  label=" " colSpan="2" readonly="true" />
			<emp:text id="LmtAppBizArea.biz_area_name" label="圈商名称" maxlength="40" required="true" />
			<emp:select id="LmtAppBizArea.biz_area_type" label="圈商类型" defvalue="<%=biz_area_type%>" dictname="STD_LMT_BIZ_AREA_TYPE" readonly="true"/>
			<emp:select id="LmtAppBizArea.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" colSpan="2" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppBizArea.lmt_totl_amt" label="授信总额度(元)" maxlength="18" required="true" dataType="Currency" onblur="checkAmt(LmtAppBizArea.lmt_totl_amt)"/>
			<emp:text id="LmtAppBizArea.single_max_amt" label="单户限额(元)" maxlength="18" required="true" dataType="Currency" onblur="checkAmt(LmtAppBizArea.single_max_amt)"/>
			<emp:checkbox id="LmtAppBizArea.guar_type" label="授信合作担保方式" dictname="STD_LMT_CRD_COOP_LN_TYPE" required="true" colSpan="2" layout="false"/>
			<emp:select id="LmtAppBizArea.term_type" label="授信期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppBizArea.term" label="授信期限" maxlength="8" required="true" dataType="Int"/>
			<emp:date id="LmtAppBizArea.app_date" label="申请日期" required="false" dataType="Date" defvalue="$OPENDAY" readonly="true"/>
			<emp:date id="LmtAppBizArea.over_date" label="办结日期" required="false" dataType="Date" hidden="true"/>
		</emp:gridLayout>
	<%
	if("0".equals(biz_area_type)){%>		
		<emp:gridLayout id="LmtAppBizAreaComnGroup" maxColumn="2" title="目标客户群（一般圈商）">
			<emp:text id="LmtAppBizAreaComn.serno" label="业务编号" maxlength="40" required="false"  hidden="true"/>
			<emp:select id="LmtAppBizAreaComn.shop_type" label="商户类型" required="true" dictname="STD_LMT_BIZ_TYPE" />
			<emp:textarea id="LmtAppBizAreaComn.main_prd" label="主要产品" maxlength="200" required="true" colSpan="2" />
			<emp:select id="LmtAppBizAreaComn.oper_model" label="经营规模" required="true" dictname="STD_LMT_BIZ_SIZE" />
			<emp:textarea id="LmtAppBizAreaComn.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
	<%}else if("1".equals(biz_area_type)){ %>
		<emp:gridLayout id="LmtAppBizAreaCoreGroup" maxColumn="2" title="目标客户群（核心企业）">
			<emp:text id="LmtAppBizAreaCore.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:select id="LmtAppBizAreaCore.core_con_type" label="核心企业类型" required="true" dictname="STD_LMT_BIZ_TYPE" />
			<emp:text id="LmtAppBizAreaCore.year_sale_amt" label="年供货销售额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="LmtAppBizAreaCore.fore_debt_bal" label="平均应收(付)账款余额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="LmtAppBizAreaCore.coop_year" label="合作年限" maxlength="6" required="true" dataType="Int"/>
			<emp:textarea id="LmtAppBizAreaCore.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
	<%}else{ %>
		<div  class='emp_gridlayout_title' id="supmkGuests">目标客户群（超市百货类）</div>
		<div id="tempButton" align="left" >
			<emp:button id="getAddLmtAppBizAreaSupmkPage" label="新增" op="add"/>
			<emp:button id="deleteLmtAppBizAreaSupmk" label="删除" op="remove"/>
		</div>
		<emp:table icollName="LmtAppBizAreaSupmkList" editable="true" pageMode="false" url="">
			<emp:text id="supmk_serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:select id="oper_trade" label="经营行业" dictname="STD_LMT_BIZ_INDUS" />
			<emp:select id="oper_model" label="经营规模" dictname="STD_LMT_BIZ_SIZE" />
			<emp:text id="trade_rank" label="行业排名" dataType="Int"/>
			<emp:text id="provid_year" label="供货年限" dataType="Int"/>
			<emp:text id="net_asset" label=" 净资产" dataType="Currency" />
			<emp:text id="other_cond" label="其他准入条件" />
		</emp:table>
	<%} %>
		<emp:gridLayout id="LmtAppBizAreaGroup" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppBizArea.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true" />
			<emp:text id="LmtAppBizArea.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppBizArea.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="LmtAppBizArea.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
			<emp:pop id="LmtAppBizArea.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
			<emp:pop id="LmtAppBizArea.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName2" />
			<emp:text id="LmtAppBizArea.input_id_displayname" label="登记人" required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="LmtAppBizArea.input_br_id_displayname" label="登记机构" required="false" readonly="true" defvalue="$organName"/>
			<emp:date id="LmtAppBizArea.input_date" label="登记日期" required="false" dataType="Date" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="LmtAppBizArea.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true"/>
		</emp:gridLayout>
		
	</emp:form>
		<div align="center">
			<br>
			<emp:button id="saveAll" label="保存" op="add"/>
			<%//<emp:button id="reset" label="重置"/>%>
			<emp:button id="return" label="返回"/>
		</div>	
</body>
</html>
</emp:page>