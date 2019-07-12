<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>现金流量表</title>

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
		DataList._toForm(form);
			
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
<emp:tabGroup mainTab="mainInfo" id="现金流量表">
  <emp:tab label="现金流量表" id="mainInfo" >
	<emp:form id="submitForm" action="saveCurrentFlow.do" method="POST">

	<emp:gridLayout id="TableInfoGroup" title="表信息" maxColumn="2">
			<emp:text id="TableInfo.category" label="分类编号" readonly="true" hidden="true" colSpan="2"/>
			<emp:text id="TableInfo.charter" label="制表人" />
			<emp:text id="TableInfo.chart_date" label="日期"/>
			<emp:text id="TableInfo.application" label="申请人"/>
			<emp:text id="TableInfo.apply_date" label="申请日期"/>
			<emp:text id="TableInfo.serno" label="流水号" hidden="true"/>
			<emp:text id="TableInfo.pk_id" label="id" hidden="true"/>
	</emp:gridLayout>	

	<br/>
	<div class='emp_gridlayout_title'>
		<emp:label text="现金流量表"></emp:label>
	</div>
	<emp:table icollName="DataList" pageMode="false" url='' >
			<emp:text id="pk_id" label="id" hidden="true"/>
			<emp:text id="serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="category" label="分类编号" readonly="true" hidden="true"/>
			<emp:text id="category_desc" label="分类名称" flat="false" />
			<emp:text id="mon_pre_5" label="${context.mon_pre_5}" flat="false" />
			<emp:text id="mon_pre_4" label="${context.mon_pre_4}" flat="false" />
			<emp:text id="mon_pre_3" label="${context.mon_pre_3}" flat="false" />
			<emp:text id="mon_pre_2" label="${context.mon_pre_2}" flat="false" />
			<emp:text id="mon_pre_1" label="${context.mon_pre_1}" flat="false" />
			<emp:text id="mon_pre_0" label="${context.mon_pre_0}" flat="false" />
			<emp:text id="mon_aft_0" label="${context.mon_aft_0}" flat="false" />
			<emp:text id="mon_aft_1" label="${context.mon_aft_1}" flat="false" />
			<emp:text id="mon_aft_2" label="${context.mon_aft_2}" flat="false" />
			<emp:text id="mon_aft_3" label="${context.mon_aft_3}" flat="false" />
			<emp:text id="mon_aft_4" label="${context.mon_aft_4}" flat="false" />
			<emp:text id="mon_aft_5" label="${context.mon_aft_5}" flat="false" />
			<emp:text id="mon_aft_6" label="${context.mon_aft_6}" flat="false" />
			<emp:text id="mon_aft_7" label="${context.mon_aft_7}" flat="false" />
			<emp:text id="mon_aft_8" label="${context.mon_aft_8}" flat="false" />
			<emp:text id="mon_aft_9" label="${context.mon_aft_9}" flat="false" />
			<emp:text id="mon_aft_10" label="${context.mon_aft_10}" flat="false" />
			<emp:text id="mon_aft_11" label="${context.mon_aft_11}" flat="false" />
			<emp:text id="mon_aft_12" label="${context.mon_aft_12}" flat="false" />
	</emp:table>
	</emp:form> 
	<div align="center" >
		<emp:button id="save" label="保存"/>
		<emp:button id="back" label="返回"/>
	</div> 	
	</emp:tab>
		<emp:tab id="docInfo" label="调查报告" url="rqReport/reportShowPage.jsp" reqParams="reportId=dcbg_wx_xjllb.raq&serno=$TableInfo.serno;&pageMark=no" initial="true" needFlush="true"/>
		
</emp:tabGroup> 	
</body>
</html>
</emp:page>
