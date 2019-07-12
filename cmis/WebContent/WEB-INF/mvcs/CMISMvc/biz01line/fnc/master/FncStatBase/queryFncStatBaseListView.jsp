<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String cus_id=request.getParameter("FncStatBase.cus_id");
%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>
	<jsp:include page="/include.jsp" />


	<script>

	//var page = new EMP.util.Page();
	//function doOnLoad() {
	//	page.renderEmpObjects();
	//}
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
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncStatBase._toForm(form);
		FncStatBaseList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncStatBasePage() {
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncStatBaseUpdatePage.do"/>&'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doView() {
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','stat_style']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncStatBaseViewPage.do"/>&'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doView2() {
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','state_flg','stat_style']);
		if (paramStr != null) {
			//alert("${context.yq}")
			var url = '<emp:url action="fncStat_view.do"/>&'+paramStr+'&yq=${context.yq}';
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncStatBasePage() {
		var url = '<emp:url action="getFncStatBaseAddPage.do"/>&' + "FncStatBase.cus_id=<%=cus_id%>";
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncStatBase() {
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd']);
		
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncStatBaseRecord.do"/>&'+paramStr;
				url = EMP.util.Tools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};
	function doImportReport() {
	    var address = document.getElementsByName("pFile")[0].value;
		if(address == "") {
			alert("请选择要导入的EXCEL文件");
		}
		if(address != "") {
			
			var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','stat_bs_style_id','stat_pl_style_id','stat_cf_style_id','stat_fi_style_id','stat_soe_style_id','stat_sl_style_id']);
			if (paramStr!=null) {
			var url = '<emp:url action="importReport.do"/>&'+paramStr+"&address=" + address;;
				url=encodeURI(url);
				window.location = url;
				//window.open (url,'addPage','height=600,width=950,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,location=no, status=no');
			}
		}
	}
	function doExportReport() {
		var address = document.getElementsByName("pFile")[0].value;
		if(address == "") {
			alert("请选择要导出的EXCEL文件");
		}
		if(address != "") {
		
			var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','stat_bs_style_id','stat_pl_style_id','stat_cf_style_id','stat_fi_style_id','stat_soe_style_id','stat_sl_style_id']);
			if (paramStr!=null) {
			var url = '<emp:url action="exportReport.do"/>&'+paramStr+"&address=" + address;;
				url=encodeURI(url);
				window.location = url;
			}
		}
	}
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm"></form>
	

	<div id="FncStatBaseGroup" class="emp_group_div">
	<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="输入查询条件">
		<emp:text id="FncStatBase.cus_id" label="客户码" hidden="true"/>
		<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="FncStatBase.stat_prd" label="报表期间" onchange=""/>
		<emp:select id="FncStatBase.stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
	</emp:gridLayout></div>
	<jsp:include page="/queryInclude.jsp" />


 	<div align="left">
 	<!--<emp:button id="view" label="查看" /> -->
	<emp:button id="view2" label="查看" locked="false"/> 
	</div>

	<emp:table icollName="FncStatBaseList" pageMode="true" url="pageFncStatBaseQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:text id="stat_prd" label="报表期间" />
		<emp:text id="stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:text id="stat_bs_style_id" label="资产样式编号" hidden="true"/>
		<emp:text id="state_flg_name" label="状态" />
		<emp:text id="state_flg" label="状态" hidden="true"/>
		<emp:text id="stat_pl_style_id" label="损益表编号" hidden="true"/>
		<emp:text id="stat_cf_style_id" label="现金流量表编号" hidden="true"/>
		<emp:text id="stat_fi_style_id" label="财务指标表编号" hidden="true"/>
		<emp:text id="stat_soe_style_id" label="所有者权益变动表编号" hidden="true"/>
		<emp:text id="stat_sl_style_id" label="财务简表编号" hidden="true"/>
	</emp:table>

	</body>
	</html>
</emp:page>
