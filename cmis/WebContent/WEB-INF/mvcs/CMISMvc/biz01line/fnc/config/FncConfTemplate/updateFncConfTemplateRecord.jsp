<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" />
<style type="text/css">
.emp_field_select_select {
border: 1px solid #b7b7b7;
text-align:left;
width:170px;
}
.emp_field_text_input {
border: 1px solid #b7b7b7;
text-align:left;
width:170px;
}
</style>
<script type="text/javascript">

	var page = new EMP.util.Page();
	function doOnLoad() {
		//page.renderEmpObjects();
		doChangeId();
	}
	
	function doUpdateFncConfTemplate() {
		var form = document.getElementById('submitForm');
		var result = FncConfTemplate._checkAll();
		if(result){
			FncConfTemplate._toForm(form)
			form.submit();
		}
	};
		
	function doReset(){
		page.dataGroups.FncConfTemplateGroup.reset();
	};
	function doReturn() {
		var url = '<emp:url action="queryFncConfTemplateList.do"/>';
		url = EMP.util.Tools.encodeURI(url);
		window.location=url;
	};

	function doChangeId(){
		var url = '<emp:url action="queryFncConfStylesListByCon.do"/>';
		var handleSuccess = function(o){ EMPTools.unmask();	
				setCustomer(o);	
		};
		var handleFailure = function(o){ EMPTools.unmask();	
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);	
		
	
	};
		
	function setCustomer(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr define error!"+e.message);
				return;
			}
            var FncConfStylesList=jsonstr.FncConfStylesList;
            
			var bsStyleId = FncConfTemplate.fnc_bs_style_id._obj.element;	
			var plStyleId = FncConfTemplate.fnc_pl_style_id._obj.element;	
			var cfStyleId = FncConfTemplate.fnc_cf_style_id._obj.element;	
			var fiStyleId = FncConfTemplate.fnc_fi_style_id._obj.element;	
			var soeStyleId = FncConfTemplate.fnc_ri_style_id._obj.element;
			var slStyleId = FncConfTemplate.fnc_smp_style_id._obj.element;

			var bsStyleId_value = '${context.fnc_bs_style_id}';	
			var plStyleId_value = '${context.fnc_pl_style_id}';
			var cfStyleId_value = '${context.fnc_cf_style_id}';
			var fiStyleId_value = '${context.fnc_fi_style_id}';
			var soeStyleId_value = '${context.fnc_ri_style_id}';
			var slStyleId_value = '${context.fnc_smp_style_id}';
			
			var bsId = 1;
			var plId = 1;
			var cfId = 1;
			var fiId = 1;
			var soeId = 1;
			var slId = 1;
			for(var i=0;i<FncConfStylesList.length;i++){
			var fncConfTyp=FncConfStylesList[i].fnc_conf_typ;
			var ssTemp = FncConfStylesList[i].style_id;
			if(fncConfTyp=="01"){
				bsStyleId.options[bsId] = new Option();	
				bsStyleId.options[bsId].value = FncConfStylesList[i].style_id;
				bsStyleId.options[bsId].text =  "["+FncConfStylesList[i].style_id+"]"+FncConfStylesList[i].fnc_conf_dis_name;
				if(ssTemp==bsStyleId_value){
					bsStyleId.options[bsId].selected =  true;
				}
				bsId++;
			}else if(fncConfTyp=="02"){
				plStyleId.options[plId] = new Option();
				plStyleId.options[plId].value = FncConfStylesList[i].style_id;
				plStyleId.options[plId].text =  "["+FncConfStylesList[i].style_id+"]"+FncConfStylesList[i].fnc_conf_dis_name;
				if(ssTemp==plStyleId_value){
					plStyleId.options[plId].selected =  true;
				}
				plId++;
			
			}else if(fncConfTyp=="03"){
				cfStyleId.options[cfId] = new Option();
				cfStyleId.options[cfId].value = FncConfStylesList[i].style_id;
				cfStyleId.options[cfId].text =  "["+FncConfStylesList[i].style_id+"]"+FncConfStylesList[i].fnc_conf_dis_name;
				if(ssTemp==cfStyleId_value){
					cfStyleId.options[cfId].selected =  true;
				}
				cfId++;
			
			}else if(fncConfTyp=="04"){
				fiStyleId.options[fiId] = new Option();
				fiStyleId.options[fiId].value = FncConfStylesList[i].style_id;
				fiStyleId.options[fiId].text =  "["+FncConfStylesList[i].style_id+"]"+FncConfStylesList[i].fnc_conf_dis_name;
				if(ssTemp==fiStyleId_value){
					fiStyleId.options[fiId].selected =  true;
				}
				fiId++;
			
			}else if(fncConfTyp=="05"){
				soeStyleId.options[soeId] = new Option();
				soeStyleId.options[soeId].value = FncConfStylesList[i].style_id;
				soeStyleId.options[soeId].text =  "["+FncConfStylesList[i].style_id+"]"+FncConfStylesList[i].fnc_conf_dis_name;
				if(ssTemp==soeStyleId_value){
					soeStyleId.options[soeId].selected =  true;
				}
				soeId++;
			
			}else if(fncConfTyp=="06"){
				slStyleId.options[slId] = new Option();
				slStyleId.options[slId].value = FncConfStylesList[i].style_id;
				slStyleId.options[slId].text = "["+FncConfStylesList[i].style_id+"]"+FncConfStylesList[i].fnc_conf_dis_name;
				if(ssTemp==slStyleId_value){
					slStyleId.options[slId].selected =  true;
				}
				slId++;
			}
		}
	 }
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form id="submitForm" action="<emp:url action='updateFncConfTemplateRecord.do'/>" method="POST">
	</form>
	<div id="FncConfTemplateGroup" class="emp_group_div">
		<emp:gridLayout id="FncConfTemplateGroup" maxColumn="2" title="财务报表列表">
			<emp:text id="FncConfTemplate.fnc_id" label="报表编号" maxlength="6" required="true" readonly="true" />
			<emp:text id="FncConfTemplate.fnc_name" label="财务报表类型" maxlength="200" required="true" />
			<emp:select id="FncConfTemplate.no_ind" label="新旧报表标志"  dictname="STD_ZB_FNC_ON_TYP" hidden="true"/>
			<emp:select id="FncConfTemplate.com_ind" label="企事业报表标志"  dictname="STD_ZB_FNC_COMIND" hidden="true"/>
			<emp:select id="FncConfTemplate.fnc_bs_style_id" label="资产负债表" required="true" />
			<emp:select id="FncConfTemplate.fnc_pl_style_id" label="损益表(收入支出总表)" required="true" />
			<emp:select id="FncConfTemplate.fnc_cf_style_id" label="现金流量(事业支出明细表)" required="true" />
			<emp:select id="FncConfTemplate.fnc_fi_style_id" label="财务指标" required="false" />
			<emp:select id="FncConfTemplate.fnc_ri_style_id" label="所有者权益变动" required="false" />
			<emp:select id="FncConfTemplate.fnc_smp_style_id" label="财务简表(经营支出明细表)" required="false" />
			<emp:text id="FncConfTemplate.fnc_style_id1" label="保留" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncConfTemplate.fnc_style_id2" label="保留" maxlength="6" required="false" hidden="true"/>
		</emp:gridLayout>
		</div>
		
	<div align="center">
		<br>
		<emp:button id="updateFncConfTemplate" label="保存" />
		<emp:button id="reset" label="重置"/>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
