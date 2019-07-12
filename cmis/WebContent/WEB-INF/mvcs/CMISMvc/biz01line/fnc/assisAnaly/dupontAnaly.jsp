<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>

<%
String cus_id=request.getParameter("FncStatBase.cus_id");
%>

<emp:page>
	<html>
	<head>
	<title>列表查询页面</title>
	<jsp:include page="/include.jsp" />

<script>
	function getCurYear(){
		var now=new Date();
		var year=now.getFullYear();
		return year;
	};
	var page = new EMP.util.Page();
	function doOnLoad() {
		EMPTools.addEvent(FncStatBase.stat_prd_style._obj.element, "change", doChange, FncStatBase.stat_prd_style);
		setYearSelect();//初始化年份
	};
	//初始化年份选项
	function setYearSelect(){
		var curYear = getCurYear();
		var yearLimit = parseFloat(curYear)+5;
		var select = FncStatBase.stat_prd_year._obj.element;
		var j=0;
		for(var i=1999;i<yearLimit;i++){
			select.options[j+1] = new Option();
			select.options[j+1].value = i+1;
			select.options[j+1].text =  i+1+"年";
			j++;
		}
	};
	function doChange(){
		var curYear = getCurYear();
		var yearLimit = parseFloat(curYear)+5;
		var style_value = FncStatBase.stat_prd_style._obj.element.value;
		var select = FncStatBase.stat_prd._obj.element;	
		select.length=1;
		if(style_value=='4'){
				FncStatBase.stat_prd_year._obj._renderHidden(true);
				FncStatBase.stat_prd_year._setValue('');
			}else{
				FncStatBase.stat_prd_year._obj._renderHidden(false);
			}
		 if(style_value=="4"){
		  	var j=0;
			for(var i=1999;i<yearLimit;i++){
				select.options[j+1] = new Option();
				select.options[j+1].value = i+1+'12';
				select.options[j+1].text =  i+1+"年";
				j++;
			}
		}else if(style_value=="3"){
			for(var i=1;i<=2;i++){
				select.options[i] = new Option();	
				if("1"==i){
				select.options[i].value = "0"+(i+5);
				select.options[i].text ="上半年";
				}else{
				select.options[i].value = ""+(i+10);
				select.options[i].text ="下半年";
				}
			}
		}else if(style_value=="2"){
			for(var i=1;i<=4;i++){
				select.options[i] = new Option();
				if(i*3<=9){
					select.options[i].value = "0"+(i*3);
				}else{
					select.options[i].value = ""+(i*3);
				}
				select.options[i].text = "第"+i+"季度";
			}
		}else if(style_value=="1"){
			for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
					select.options[i+1].value = "0"+(i+1);
				}else{
					select.options[i+1].value = ""+(i+1);
				}
				select.options[i+1].text =  i+1+"月";
			}
		}
	}
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncStatBase._toForm(form);
		FncStatBaseList._obj.ajaxQuery(null,form);
	};
	
	function doView() {
		var paramStr = FncStatBaseList._obj.getSelectedData();
		if(paramStr.length>1){
			alert("请选择单笔记录!");
			return;
		}else{					
			if (paramStr.length > 0) {
				cus_id = paramStr[0].cus_id._getValue();
				stat_prd_style = paramStr[0].stat_prd_style._getValue();
				stat_prd = paramStr[0].stat_prd._getValue();
				stat_style = paramStr[0].stat_style._getValue();
				state_flg = paramStr[0].state_flg._getValue();	
				fnc_type = paramStr[0].fnc_type._getValue();
				var EditFlag = '${context.EditFlag}';
				paramStr = '?fnc_type='+fnc_type+'&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='+stat_prd
				 +'&stat_style='+stat_style+'&state_flg='+state_flg+'&EditFlag='+EditFlag;
				var url = '<emp:url action="fncStat_view.do"/>'+paramStr;
				url = EMP.util.Tools.encodeURI(url);
				windowName = Math.ceil(Math.random()*50000000);
				EMPTools.openWindow(url,windowName+"",'height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			} else {
				alert('请先选择一条记录！');
			}
		}
	};

	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};
	
	function doDupontAnaly(){
		var paramStr = FncStatBaseList._obj.getSelectedData();
		if(paramStr.length>1){
			alert("请选择单笔记录!");
			return;
		}else{
			if (paramStr.length > 0) {
				cus_id = paramStr[0].cus_id._getValue();
				stat_prd_style = paramStr[0].stat_prd_style._getValue();
				stat_prd = paramStr[0].stat_prd._getValue();
				stat_style = paramStr[0].stat_style._getValue();
				state_flg = paramStr[0].state_flg._getValue();
				if(state_flg.substr(state_flg.length-1,state_flg.length) != '2'){
					alert("请选择完成状态的报表");
					return;
				}
				fnc_type = paramStr[0].fnc_type._getValue();
				if(fnc_type != 'PB0001'){
					alert("财报分析只支持2002版报表");
					return;
				}
				paramStr = '?type=hidden&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='
							+stat_prd+'&stat_style='+stat_style+'&analy_type=21';
				var url = '<emp:url action="getDupontAnaly.do"/>'+paramStr;
				url = EMP.util.Tools.encodeURI(url);
				windowName = Math.ceil(Math.random()*50000000);
				EMPTools.openWindow(url,windowName+"",'height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			} else {
				alert('请先选择一条记录！');
			}
		}
	};
	function doDupontChart(){
		var paramStr = FncStatBaseList._obj.getSelectedData();
		if (paramStr.length > 0) {
			var cus_id = new Array();
			var stat_prd_style = new Array();
			var stat_prd = new Array();
			var stat_style = new Array();
			var state_flg = new Array();
			var fnc_type = new Array();
			for(var i=0;i<paramStr.length;i++){
				cus_id.push(paramStr[i].cus_id._getValue());
				stat_prd_style.push(paramStr[i].stat_prd_style._getValue());
				stat_prd.push(paramStr[i].stat_prd._getValue());
				stat_style.push(paramStr[i].stat_style._getValue());
				state_flg.push(paramStr[i].state_flg._getValue());
				fnc_type.push(paramStr[i].fnc_type._getValue());
			}
			for(i=0;i<state_flg.length;i++){
				if(state_flg[i].substr(state_flg[i].length-1,state_flg[i].length) != '2'){
					alert("请选择完成状态的报表");
					return;
				}
				if(fnc_type[i] != 'PB0001'){
					alert("财报分析只支持2002版报表");
					return;
				}
			}
			paramStr = '?cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='
						+stat_prd+'&stat_style='+stat_style+'&analy_type=22';
			var url = '<emp:url action="getDupontChart.do"/>'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			windowName = Math.ceil(Math.random()*50000000);
			EMPTools.openWindow(url,windowName+"",'height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择记录！');
		}		
	};

</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm"></form>
	<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="输入查询条件">
		<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="FncStatBase.stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:select id="FncStatBase.stat_prd_year" label="年份" />
		<emp:select id="FncStatBase.stat_prd" label="报表期间"   />
		<emp:text id="FncStatBase.cus_id" label="客户码" hidden="true"	readonly="true" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" />
	
 <div align="left">
	<emp:button id="view" label="查看" />
	<emp:button id="dupontAnaly" label="杜邦分析"/>
	<emp:button id="dupontChart" label="综合分析"/>
</div>
	<emp:table icollName="FncStatBaseList" pageMode="true" url="pageFncStatBaseQuery.do?FncStatBase.cus_id=${context.FncStatBase.cus_id}&EditFlag=${context.EditFlag}" selectType="2">
		<emp:text id="cus_id" label="客户码" hidden="false"/>
	    <emp:text id="cus_name" label="客户名称" hidden="true"/>
	    <emp:text id="cert_type" label="证件类型" hidden="false" dictname="STD_ZB_CERT_TYP"/>
	    <emp:text id="cert_code" label="证件号码" hidden="false"/>
	    <emp:select id="fnc_type" label="财务报表类型" dictname="STD_ZB_FIN_REP_TYPE"/>
		<emp:text id="stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="stat_prd" label="报表期间"  onchange="linkChangeStatPrd()"/>
		<emp:text id="stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:text id="stat_bs_style_id" label="资产样式编号" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" hidden="false"/>
		<emp:text id="input_id" label="登记人" hidden="true" />
		<emp:text id="input_br_id" label="登记机构" hidden="true" />
		<emp:text id="state_flg_name" label="状态" />
		<emp:text id="state_flg" label="状态" hidden="true"/>
		<emp:text id="stat_pl_style_id" label="损益表编号" hidden="true"/>
		<emp:text id="stat_cf_style_id" label="现金流量表编号" hidden="true"/>
		<emp:text id="stat_fi_style_id" label="财务指标表编号" hidden="true"/>
		<emp:text id="stat_soe_style_id" label="所有者权益变动表编号" hidden="true"/>
		<emp:text id="stat_sl_style_id" label="财务简表编号" hidden="true"/>	
		<emp:text id="stat_acc_style_id" label="会计科目余额表编号" hidden="true"/>
		<emp:text id="stat_de_style_id" label="经济合作社财务收支明细表编号" hidden="true"/>
	</emp:table>
	</body>
	</html>
</emp:page>