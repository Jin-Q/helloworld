<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>

<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" />

<script>
	function getCurYear(){
	//	var now=new Date();
	//	var year=now.getFullYear();
		var openDay = '${context.OPENDAY}';
		year = openDay.substring(0,4);
		return year;
	};
	var page = new EMP.util.Page();
	function doOnLoad() {
		//page.renderEmpObjects();
		//原来的做法
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
	}
	
	function doChange(){
		var curYear = getCurYear();
		var yearLimit = parseFloat(curYear)+5;
		var style_value = FncStatBase.stat_prd_style._obj.element.value;
		var select = FncStatBase.stat_prd._obj.element;	
		//var y = FncStatBase.stat_prd_year._obj.element;	
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
			//	select.options[i].value = ""+curYear+"0"+(i+5);
				select.options[i].value = "0"+(i+5);
				select.options[i].text ="上半年";
				}else{
			//	select.options[i].value = ""+curYear+(i+10);
				select.options[i].value = ""+(i+10);
				select.options[i].text ="下半年";
				}
			}
		}else if(style_value=="2"){
			for(var i=1;i<=4;i++){
				select.options[i] = new Option();
				if(i*3<=9){
				//	select.options[i].value = ""+curYear+"0"+(i*3);
					select.options[i].value = "0"+(i*3);
				}else{
				//	select.options[i].value = ""+curYear+(i*3);
					select.options[i].value = ""+(i*3);
				}
				select.options[i].text = "第"+i+"季度";
			}
		}else if(style_value=="1"){
			for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
				//	select.options[i+1].value = ""+curYear+"0"+(i+1);
					select.options[i+1].value = "0"+(i+1);
				}else{
				//	select.options[i+1].value = ""+curYear+(i+1);
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
		var paramStr = FncStatBaseList._obj.getParamStr(['fnc_type','cus_id','stat_prd_style','stat_prd','state_flg','stat_style']);
		if (paramStr != null) {
			var url = '<emp:url action="cusFncStat_view.do"/>&isSmp=isSmp&'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doView2() {
		var paramStr = FncStatBaseList._obj.getParamStr(['fnc_type','cus_id','stat_prd_style','stat_prd','state_flg','stat_style']);
		if (paramStr != null) {
			var state_flg = FncStatBaseList._obj.getParamValue(['state_flg']);//状态
			if(state_flg!=''&&state_flg!=null&&state_flg.substring(8)=='2'){
				alert('完成状态的财务报表不允许修改！');
				return;
			}
			var url = '<emp:url action="cusFncStat.do"/>&isSmp=isSmp&'+paramStr;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncStatBasePage() {
		var url = '<emp:url action="getCusFncStatBaseAddPage.do"/>?&isSmp=isSmp';
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncStatBase() {
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','stat_style','fnc_type']);
		if (paramStr != null) {
			var state_flg = FncStatBaseList._obj.getParamValue(['state_flg']);//状态
			if(state_flg!=''&&state_flg!=null&&state_flg.substring(8)=='2'){
				alert('完成状态的财务报表不允许删除！');
				return;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncStatBaseRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="success"){
							alert("删除成功!");
							var url = '<emp:url action="queryCusFncStatBaseSmpList.do"/>?&isSmp=isSmp';
							url = EMPTools.encodeURI(url);
							window.location = url;
				 	  	}else {
					 		alert(flag);
					 		return;
				   		}
					}
				};
				var handleFailure = function(o){	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};
	function returnCus(data){
		FncStatBase.cus_id._setValue(data.cus_id._getValue());
		FncStatBase.cus_name._setValue(data.cus_name._getValue());
	};
</script>
</head>
	<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="输入查询条件">
		<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="FncStatBase.stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:select id="FncStatBase.stat_prd_year" label="年份" />
		<emp:select id="FncStatBase.stat_prd" label="报表期间"  required="true" />
		<emp:pop id="FncStatBase.cus_name" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
		<emp:text id="FncStatBase.cus_id" label="客户码"  hidden="true" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" />

 <div align="left">
		<emp:button id="getAddFncStatBasePage" label="新增" /> 
		<emp:button id="view2" label="修改" />
		<emp:button id="view" label="查看" /> 
		<emp:button id="deleteFncStatBase" label="删除"/> 
 </div>
	<emp:table icollName="FncStatBaseList" pageMode="true" url="pageFncStatBaseQuery.do?&isSmp=${context.isSmp}">
		<emp:text id="cus_id" label="客户码" hidden="false"/>
	   <emp:text id="cus_id_displayname" label="客户名称" hidden="false"/>
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
