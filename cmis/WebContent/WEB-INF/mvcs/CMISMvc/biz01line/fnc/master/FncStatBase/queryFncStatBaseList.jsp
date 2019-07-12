<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
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
	
	function doGetUpdateFncStatBasePage() {
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','stat_style']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getFncStatBaseUpdatePage.do"/>&'+paramStr+"&EditFlag="+editFlag;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doView() {
		var paramStr = FncStatBaseList._obj.getParamStr(['fnc_type','cus_id','stat_prd_style','stat_prd','state_flg','stat_style']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="fncStat_view.do"/>&'+paramStr+"&EditFlag="+editFlag;
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
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="fncStat.do"/>&'+paramStr+"&EditFlag="+editFlag;
			url = EMP.util.Tools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncStatBasePage() {
	//	var flag = '${context.flag}';
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getFncStatBaseAddPage.do"/>?cus_id='+"<%=cus_id%>&EditFlag="+editFlag;
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
						if(flag=="success"){//cus_id=5000923677&stat_prd_style=4&stat_prd=200112&stat_style=1
							alert("删除成功!");
							var editFlag = '${context.EditFlag}';
							var cus_id = FncStatBaseList._obj.getSelectedData()[0].cus_id._getValue();
							var url = '<emp:url action="queryFncStatBaseList.do"/>&FncStatBase.cus_id='+cus_id+"&EditFlag="+editFlag;
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
	function doImportReport() {
	    var address = document.getElementsByName("pFile")[0].value;
	   /* var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd',
	                                             	    'stat_bs_style_id','stat_pl_style_id',
	                                             	    'stat_cf_style_id','stat_fi_style_id',
	                                             	    'stat_soe_style_id','stat_sl_style_id'
	                                             	    ,'stat_style']);
	    */
	    var data = FncStatBaseList._obj.getSelectedData()[0];
	    if(data == null){
            alert('请先选择一条记录');
            return;
		}

	    var state_flg = FncStatBaseList._obj.getParamValue(['state_flg']);//状态
		if(state_flg!=''&&state_flg!=null&&state_flg.substring(8)=='2'){
			alert('完成状态的财务报表不允许修改！');
			return;
		}
		
		if(address == "") {
			alert("请选择要导入的EXCEL文件!!!!!");
			return false;
		}else{
			if(confirm("系统中已经存在该类报表，做导入操作会把原数据覆盖 !")){
				
			}else{
				return;
			}
		}
	
		
		FncStatBaseTmp._putKColl(data);
		FncStatBaseTmp.pFile._setValue(address);
		
		var form = document.getElementById('improtForm');
		/*
		FncStatBase.cus_id1._setValue(data.cus_id._getValue());
		FncStatBase.stat_prd_style1._setValue(data.stat_prd_style._getValue());
		FncStatBase.stat_prd1._setValue(data.stat_prd._getValue());
		
		FncStatBase.stat_bs_style_id1._setValue(data.stat_bs_style_id._getValue());
		FncStatBase.stat_pl_style_id1._setValue(data.stat_pl_style_id._getValue());
		FncStatBase.stat_cf_style_id1._setValue(data.stat_cf_style_id._getValue());
		FncStatBase.stat_fi_style_id1._setValue(data.stat_fi_style_id._getValue());
		FncStatBase.stat_soe_style_id1._setValue(data.stat_soe_style_id._getValue());
		FncStatBase.stat_sl_style_id1._setValue(data.stat_sl_style_id._getValue());
		FncStatBase.stat_style1._setValue(data.stat_style._getValue());
		*/
		FncStatBaseTmp._toForm(form);
		improtForm.submit();
	}
	
	function doExportReport() {
		var address = document.getElementsByName("pFile")[0].value;
		
		var paramStr = FncStatBaseList._obj.getParamStr(['cus_id','stat_prd_style','stat_prd','fnc_type',
		                                     			'stat_bs_style_id','stat_pl_style_id','stat_cf_style_id',
		                                     			'stat_fi_style_id','stat_soe_style_id','stat_sl_style_id'
		                                     			,'stat_style']);
		if (paramStr!=null) {
		var url = '<emp:url action="exportReport.do"/>&'+paramStr+"&address=" + address;
			url=encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doDownLoadTmplate(){
		var url = '<emp:url action="queryFncConfTemplatePopList.do"/>';
		url=encodeURI(url);
		window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=yes,status=no');
	}

</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm"></form>
	

	<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="输入查询条件">
		<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:select id="FncStatBase.stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:select id="FncStatBase.stat_prd_year" label="年份" />
		<emp:select id="FncStatBase.stat_prd" label="报表期间"  required="true" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" />

	<form id="improtForm" name="improtForm" action="<emp:url action='importReport.do?EditFlag=${context.EditFlag}'/>"  method="POST" enctype="multipart/form-data">
		<emp:text id="FncStatBaseTmp.cus_id" label="客户码" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_prd_style" label="报表周期类型" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_prd" label="报表期间" hidden="true"/>
		<emp:text id="FncStatBaseTmp.pFile" label="路径" hidden="true" />
		
		<emp:text id="FncStatBaseTmp.stat_style" label="路径" hidden="true"/>
		
		<emp:text id="FncStatBaseTmp.stat_bs_style_id" label="资产样式编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_pl_style_id" label="损益表编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_cf_style_id" label="现金流量表编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_fi_style_id" label="财务指标表编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_soe_style_id" label="所有者权益变动表编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_sl_style_id" label="财务简表编号" hidden="true"/>	
		<emp:text id="FncStatBaseTmp.stat_acc_style_id" label="会计科目余额表编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.stat_de_style_id" label="经济合作社财务收支明细表编号" hidden="true"/>
		<emp:text id="FncStatBaseTmp.fnc_type" label="财务报表类型" hidden="true"/>
	 	<table>
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td>
			<div>
			<!--<input type="text" id="txt" name="txt" size="50">   
            <input type="button" onmousemove="pFile.style.pixelLeft=event.x-60;pFile.style.pixelTop=this.offsetTop;" value="请选择文件"  class="button80" onclick="pFile.click()">
			<input type="file" id="pFile" onchange="txt.value=this.value" style="position:absolute;filter:alpha(opacity=0);" size="1"> -->
			  <input type="file" name="pFile" onChange="">
			</div>
			</td>
			<td>(说明：请使用2003版EXCEL表)</td>
		</tr>
	 </table>
	</form>
 <div align="left">

	<%
//	String flag=(String)request.getSession().getAttribute("buttonFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
	if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddFncStatBasePage" label="新增" /> 
		<!-- emp:button id="getUpdateFncStatBasePage" label="修改"/-->
		<emp:button id="deleteFncStatBase" label="删除" /> 
		<emp:button id="view" label="查看" /> 
		<emp:button id="view2" label="修改" /> 
		<emp:button id="importReport" label="导入EXCEL" locked="false"/>
		<emp:button id="exportReport" label="导出EXCEL" locked="false"/>
		<emp:button id="downLoadTmplate" label="下载模板" locked="false"/>
	<% }else{ %>
		<emp:button id="view" label="查看" /> 
	<% } %>
</div>
	<emp:table icollName="FncStatBaseList" pageMode="true" url="pageFncStatBaseQuery.do?FncStatBase.cus_id=${context.FncStatBase.cus_id}&EditFlag=${context.EditFlag}">
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
