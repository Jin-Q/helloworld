<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>种植情况调查表</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doBack(){
		var serno = TableInfo.serno._getValue();
		var url = '<emp:url action="setReportImportPage.do"/>&serno='+serno;
		url=encodeURI(url);
		window.location = url;
	}

	function doSave(){
		var form = document.getElementById("submitForm");

		TableInfo._toForm(form);
		MainInfoList._toForm(form);
		CostList._toForm(form);
		PredictionList._toForm(form);
			
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("添加数据失败!");
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
					return;
				}else{
					alert("保存失败！");
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
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
</script>
</head>
<body class="page_content" >
<emp:tabGroup mainTab="mainInfo" id="种植表">
  <emp:tab label="种植情况调查表" id="mainInfo" >
	<emp:form id="submitForm" action="savePlantInvestigation.do" method="POST"></emp:form>
	<h3>种植情况调查表</h3>
	
	<emp:gridLayout id="TableInfoGroup" title="表信息" maxColumn="2">
			<emp:text id="TableInfo.category" label="分类编号" readonly="true" hidden="true" colSpan="2"/>
			<emp:text id="TableInfo.table_filler" label="填表人" />
			<emp:text id="TableInfo.filling_date" label="日期"/>
			<emp:text id="TableInfo.serno" label="流水号" hidden="true"/>
			<emp:text id="TableInfo.pk_id" label="id" hidden="true"/>
	</emp:gridLayout>	
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="过去一年种植情况"></emp:label>
	</div>
	<emp:table icollName="MainInfoList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="category" label="分类编号" readonly="true" hidden="true"/>
			<emp:text id="item_name" label="项目" flat="false" />
			<emp:text id="mon_1" label="种类1" flat="false" />
			<emp:text id="mon_2" label="种类2" flat="false" />
			<emp:text id="mon_3" label="种类3" flat="false" />
			<emp:text id="mon_4" label="种类4" flat="false" />
			<emp:text id="mon_5" label="种类5" flat="false" />
			<emp:text id="mon_6" label="种类6" flat="false" />
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="主要成本"></emp:label>
	</div>
	<emp:table icollName="CostList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="category" label="分类编号" readonly="true" hidden="true"/>
			<emp:text id="item_name" label="项目" flat="false" />
			<emp:text id="mon_1" label="种类1" flat="false" />
			<emp:text id="mon_2" label="种类2" flat="false" />
			<emp:text id="mon_3" label="种类3" flat="false" />
			<emp:text id="mon_4" label="种类4" flat="false" />
			<emp:text id="mon_5" label="种类5" flat="false" />
			<emp:text id="mon_6" label="种类6" flat="false" />
	</emp:table>
	
	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="未来一年预计种植收入情况"></emp:label>
	</div>
	<emp:table icollName="PredictionList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" readonly="true" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="category" label="分类编号" readonly="true" hidden="true"/>
			<emp:text id="item_name" label="项目" flat="false" />
			<emp:text id="mon_1" label="种类1" flat="false" />
			<emp:text id="mon_2" label="种类2" flat="false" />
			<emp:text id="mon_3" label="种类3" flat="false" />
			<emp:text id="mon_4" label="种类4" flat="false" />
			<emp:text id="mon_5" label="种类5" flat="false" />
			<emp:text id="mon_6" label="种类6" flat="false" />
	</emp:table>
	
	<div align="center" >
		<emp:button id="save" label="保存"/>
		<emp:button id="back" label="返回"/>
	</div> 	
  </emp:tab>
		<emp:tab id="docInfo" label="调查报告" url="rqReport/reportShowPage.jsp" reqParams="reportId=dcbg_wx_sxzz.raq&serno=$TableInfo.serno;&pageMark=no" initial="true" needFlush="true"/>	
</emp:tabGroup> 
</body>
</html>
</emp:page>
