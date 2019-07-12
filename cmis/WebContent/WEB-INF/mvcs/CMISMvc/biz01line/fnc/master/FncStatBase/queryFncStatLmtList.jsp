<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%
	String cus_id = request.getParameter("cus_id");
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
		//page.renderEmpObjects();
		//原来的做法
		EMPTools.addEvent(FncStatBase.stat_prd_style._obj.element, "change", doChange, FncStatBase.stat_prd_style);
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncStatBase._toForm(form);
		FncStatBaseList._obj.ajaxQuery(null,form);
	};
	
	function doView() {
		var data = FncStatBaseList._obj.getSelectedData();
		if(data != null && data != ""){
			var fnc_type = data[0].fnc_type._getValue();
			var cus_id = data[0].cus_id._getValue();
			var stat_prd_style = data[0].stat_prd_style._getValue();
			var stat_prd = data[0].stat_prd._getValue();
			var state_flg = data[0].state_flg._getValue();
			var stat_style = data[0].stat_style._getValue();
			var url = '<emp:url action="fncStat_view.do"/>?fnc_type='+fnc_type+'&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+
			'&stat_prd='+stat_prd+'&state_flg='+state_flg+'&stat_style='+stat_style+"&type=fnc";
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		} 
	};
	
	function doChange(){
		var curYear = getCurYear();
		var style_value = FncStatBase.stat_prd_style._obj.element.value;

		var select = FncStatBase.stat_prd._obj.element;	
		//var y = FncStatBase.stat_prd_year._obj.element;	
			select.length=1;
			
//			if(style_value=="4"){
				//FncStatBase.stat_prd_year._obj.config.hidden=true;
				//FncStatBase.stat_prd_year._obj._renderStatus();
//			}else{
				//FncStatBase.stat_prd_year._obj.config.hidden=false;
				//FncStatBase.stat_prd_year._obj._renderStatus();
//				var m=0;
//				for(var n=1999;n<2015;n++){ 
//					y.options[m+1] = new Option();
//					y.options[m+1].value = n+1;
//					y.options[m+1].text =  n+1+"年";
//					
//					//alert(curYear+"---"+(n+1));
//					if(curYear==n+1){
//						y.options[m+1].selected =  true;
//					}	
//					m++;
//				}
//			}		
			
		 if(style_value=="4"){
		  	var j=0;
			for(var i=1999;i<2015;i++){ 
				select.options[j+1] = new Option();
				select.options[j+1].value = i+1+'12';
				select.options[j+1].text =  i+1+"年";
				j++;
			}
		}else if(style_value=="3"){
			for(var i=1;i<=2;i++){
				select.options[i] = new Option();	
				if("1"==i){
				select.options[i].value = ""+curYear+"0"+(i+5);
				select.options[i].text ="上半年";
				}else{
				select.options[i].value = ""+curYear+(i+10);
				select.options[i].text ="下半年";
				}
			}
		}else if(style_value=="2"){
			for(var i=1;i<=4;i++){
				select.options[i] = new Option();
				if(i*3<=9){
					select.options[i].value = ""+curYear+"0"+(i*3);
				}else{
					select.options[i].value = ""+curYear+(i*3);
				}
				select.options[i].text = "第"+i+"季度";
			}
		}else if(style_value=="1"){
			for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
						select.options[i+1].value = ""+curYear+"0"+(i+1);
					}else{
						select.options[i+1].value = ""+curYear+(i+1);
						}
				select.options[i+1].text =  i+1+"月";
			}
		}
	}
	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};

	//财务比较    
	function doFncCompare(){
		var data = FncStatBaseList._obj.getSelectedData();
		if(data.length< 2){
			alert("至少选择两条数据进行比较！");
			return;
		}else if(data.length > 3){
			alert("最多只能选择三条数据！");
			return;
		}else if(data.length == 2){
			var year = data[0].stat_prd._getValue().substring(0,4);
			var month = data[0].stat_prd._getValue().substring(4,7);
			var style =  data[0].stat_prd_style._getValue();
			var cus_id = FncStatBaseList._obj.getSelectedData()[0].cus_id._getValue();
			var year1 = data[1].stat_prd._getValue().substring(0,4);
			var month1 = data[1].stat_prd._getValue().substring(4,7);
			var style1 =  data[1].stat_prd_style._getValue();
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=fnc/fnc_compare.raq&cus_id='+cus_id+'&year='+year+'&month='+month+'&style='+style
			+'&year1='+year1+'&month1='+month1+'&style1='+style1+'&year2='+"";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else if(data.length == 3){
			var year = data[0].stat_prd._getValue().substring(0,4);
			var month = data[0].stat_prd._getValue().substring(4,7);
			var style =  data[0].stat_prd_style._getValue();
			var cus_id = FncStatBaseList._obj.getSelectedData()[0].cus_id._getValue();
			var year1 = data[1].stat_prd._getValue().substring(0,4);
			var month1 = data[1].stat_prd._getValue().substring(4,7);
			var style1 =  data[1].stat_prd_style._getValue();
			var year2 = data[2].stat_prd._getValue().substring(0,4);
			var month2 = data[2].stat_prd._getValue().substring(4,7);
			var style2 =  data[2].stat_prd_style._getValue();
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=fnc/fnc_compare.raq&cus_id='+cus_id+'&year='+year+'&month='+month+'&style='+style
			+'&year1='+year1+'&month1='+month1+'&style1='+style1+'&year2='+year2+'&month2='+month2+'&style2='+style2;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}
	}

	//财务报表分析（）
	function doFncAnalyse(){
		var paramStr = FncStatBaseList._obj.getParamStr(['fnc_type','cus_id','stat_prd_style','stat_prd','state_flg','stat_style']);
		var data = FncStatBaseList._obj.getSelectedData();
		if (paramStr != null) {
			if(data.length > 1){
				alert("最多只能选择一条数据！");
			}else{
				var cus_id = FncStatBaseList._obj.getSelectedData()[0].cus_id._getValue();
				var year = FncStatBaseList._obj.getSelectedData()[0].stat_prd._getValue().substring(0,4);
				var month = FncStatBaseList._obj.getSelectedData()[0].stat_prd._getValue().substring(4,7);
				var style = FncStatBaseList._obj.getSelectedData()[0].stat_prd_style._getValue();
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=fnc/fnc_analy.raq&cus_id='+cus_id+'&year='+year+'&month='+month+'&style='+style;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm"></form>
	<div id="FncStatBaseGroup" class="emp_group_div">
	<emp:gridLayout id="FncStatBaseGroup" maxColumn="3" title="输入查询条件">
		<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="FncStatBase.stat_prd" label="报表期间"  required="true" onchange="linkChangeStatPrd()"/>
		<emp:select id="FncStatBase.stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
	</emp:gridLayout></div>
	<jsp:include page="/queryInclude.jsp" />
<div align="left">
	<emp:button id="view" label="查看" /> 
	<emp:button id="fncCompare" label="财务比较" /> 
	<emp:button label="财务报表分析" id="fncAnalyse" mouseoutCss="button100" mousedownCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
</div>
	<emp:table icollName="FncStatBaseList" pageMode="true" url="pageFncStatLmtQuery.do?cus_id=${context.cus_id}" selectType="2">
		<emp:text id="cus_id" label="客户码" hidden="false"/>
		<emp:text id="cus_name" label="客户名称" hidden="true"/>
		<emp:text id="cert_type" label="证件类型" hidden="false" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" hidden="false"/>
	    <emp:select id="fnc_type" label="财务报表类型" dictname="STD_ZB_FIN_REP_TYPE"/>
		<emp:text id="stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="stat_prd" label="报表期间"  onchange="linkChangeStatPrd()"/>
		<emp:text id="stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:text id="stat_bs_style_id" label="资产样式编号" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="false"/>
		<emp:text id="state_flg_name" label="状态" />
		<emp:text id="state_flg" label="状态" hidden="true"/>
	</emp:table>
	</body>
	</html>
</emp:page>