<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>克隆页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">

	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
	}
	
	function doAddCloneFncConfStyles() {
		var styleId = FncConfStyles.styleId._obj.element.value;
		var style_id = FncConfStyles.style_id._obj.element.value;
		if(styleId==style_id){
			alert("请修改报表样式编号！");
			FncConfStyles.style_id._obj.element.focus();
		}else{
			var form = document.getElementById('submitForm');
			var result = FncConfStyles._checkAll();
			if(result){
				FncConfStyles._toForm(form)
				form.submit();
			}
			}
		
	};
		
	function doResetOne(){
		page.dataGroups.FncConfStylesGroup.reset();
		
	};
	function doReturn() {
		var url = '<emp:url action="queryFncConfStylesList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function selectFncName(){
		var fnc_conf_typ = FncConfStyles.fnc_conf_typ._obj.element.value;
		var tmptId = FncConfStyles.fnc_conf_data_col._obj.element;	

		var options = FncConfStyles.fnc_conf_data_col._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {	
				options.remove(i);
		}
		if(01==fnc_conf_typ){
			FncConfStyles.fnc_name._obj.element.value = "FNC_STAT_BS";			
			tmptId.options[0] = new Option();
			tmptId.options[0].value = "0";
			tmptId.options[0].text =  "-----请选择-----";
			tmptId.options[1] = new Option();
			tmptId.options[1].value = "1";
			tmptId.options[1].text =  "一列";
			tmptId.options[2] = new Option();
			tmptId.options[2].value = "2";
			tmptId.options[2].text =  "二列";
		}else if(02==fnc_conf_typ){
			FncConfStyles.fnc_name._obj.element.value = "FNC_STAT_IS";
			tmptId.options[0] = new Option();
			tmptId.options[0].value = "0";
			tmptId.options[0].text =  "-----请选择-----";
			tmptId.options[1] = new Option();
			tmptId.options[1].value = "1";
			tmptId.options[1].text =  "一列";
			tmptId.options[2] = new Option();
			tmptId.options[2].value = "2";
			tmptId.options[2].text =  "二列";
			
		}else if(03==fnc_conf_typ){
			FncConfStyles.fnc_name._obj.element.value = "FNC_STAT_CFS";
			tmptId.options[0] = new Option();
			tmptId.options[0].value = "0";
			tmptId.options[0].text =  "-----请选择-----";
			tmptId.options[1] = new Option();
			tmptId.options[1].value = "1";
			tmptId.options[1].text =  "一列";
			
			
			
		}else if(04==fnc_conf_typ){
			FncConfStyles.fnc_name._obj.element.value = "FNC_INDEX_RPT";
			tmptId.options[0] = new Option();
			tmptId.options[0].value = "0";
			tmptId.options[0].text =  "-----请选择-----";
			tmptId.options[1] = new Option();
			tmptId.options[1].value = "1";
			tmptId.options[1].text =  "一列";
			
		}else if(05==fnc_conf_typ){
			FncConfStyles.fnc_name._obj.element.value = "FNC_STAT_SOE";
			tmptId.options[0] = new Option();
			tmptId.options[0].value = "0";
			tmptId.options[0].text =  "-----请选择-----";
			tmptId.options[1] = new Option();
			tmptId.options[1].value = "8";
			tmptId.options[1].text =  "七列";
			
		}else if(06==fnc_conf_typ){
			FncConfStyles.fnc_name._obj.element.value = "FNC_STAT_SL";
			tmptId.options[0] = new Option();
			tmptId.options[0].value = "0";
			tmptId.options[0].text =  "-----请选择-----";
			tmptId.options[1] = new Option();
			tmptId.options[1].value = "1";
			tmptId.options[1].text =  "一列";
			tmptId.options[2] = new Option();
			tmptId.options[2].value = "2";
			tmptId.options[2].text =  "二列";
		}
	}

</script>
</head>
<body class="page_content" >
	<emp:form id="submitForm" action="addCloneFncConfStylesRecord.do" method="POST">
	
		<emp:gridLayout id="FncConfStylesGroup" maxColumn="2" title="报表样式列表">
			<emp:text id="FncConfStyles.style_id" label="报表样式编号" maxlength="6" required="true"/>
			<emp:select id="FncConfStyles.fnc_conf_typ" label="所属报表种类" required="true" dictname="STD_ZB_FNC_TYP" readonly="true" onchange="selectFncName()"/>
			<emp:text id="FncConfStyles.fnc_name" label="报表名称" maxlength="200" required="false" readonly="true"/>
			<emp:text id="FncConfStyles.fnc_conf_dis_name" label="显示名称" maxlength="200" required="true" />
			<emp:select id="FncConfStyles.no_ind" label="新旧报表标志"  dictname="STD_ZB_FNC_ON_TYP" hidden="true"/>
			<emp:select id="FncConfStyles.com_ind" label="企事业报表标志"  dictname="STD_ZB_FNC_COMIND" hidden="true"/>
			<emp:select id="FncConfStyles.fnc_conf_data_col" label="数据列数" required="true" dictname="STD_ZB_FNC_COL" readonly="true"/>	
			<emp:select id="FncConfStyles.fnc_conf_cotes" label="栏位" required="true" dictname="STD_ZB_FNC_COTES" readonly="true"/>
				
			<emp:select id="FncConfStyles.data_dec1" label="第一列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec2" label="第二列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec3" label="第三列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec4" label="第四列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec5" label="第五列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec6" label="第六列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec7" label="第七列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:select id="FncConfStyles.data_dec8" label="第八列数据描述"  dictname="STD_ZB_FNC_DCD" hidden="true"/>
			<emp:text id="FncConfStyles.head_left" label="表头左侧描述" maxlength="200"  hidden="true"/>
			<emp:text id="FncConfStyles.head_center" label="表头中部描述" maxlength="200"  hidden="true"/>
			<emp:text id="FncConfStyles.head_right" label="表头右侧描述" maxlength="200"  hidden="true"/>
			<emp:text id="FncConfStyles.food_left" label="表尾左侧描述" maxlength="200"  hidden="true"/>
			<emp:text id="FncConfStyles.food_center" label="表尾中部描述" maxlength="200"  hidden="true"/>
			<emp:text id="FncConfStyles.food_right" label="表尾右侧描述" maxlength="200"  hidden="true"/>
		
			<emp:text id="FncConfStyles.styleId" label="原样式编号"  maxlength="38" hidden="true" required="true"  defvalue="${context.styleId}" readonly="true"/>
		</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="addCloneFncConfStyles" label="保存" op="update"/>
		<emp:button id="reset" label="重置" />
		<emp:button id="return" label="返回"/>
	</div>
	</emp:form>
</body>
</html>
</emp:page>
