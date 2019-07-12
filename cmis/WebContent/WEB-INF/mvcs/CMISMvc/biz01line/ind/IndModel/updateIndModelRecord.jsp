<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_field_pop_input {
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshIndModelGroup() {
		IndModel_tabs.tabs.IndModelGroup_tab.refresh();
	};

	/*--user code begin--*/
		function doReturnList(){ 
			var url="<emp:url action='queryIndModelList.do'></emp:url>"; 
			window.location=url;
		}
		function doReturnOrgno(data){
			IndModel.use_branchs._setValue(data);  
		}	
		function doload(){
		//	getRepType();
		}
		//财务报表类型
		function getRepType(){
			
		  var url = '<emp:url action="queryFncConfTemplateListForCus.do"/>';
		  url = EMPTools.encodeURI(url); 
			  var handleSuccess = function(o){ EMPTools.unmask();
				if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var iColl= jsonstr.FncConfTemplateList;
			//				var select = IndModel.fna_rep_type._obj.element;
			//				select.length=1;
							for(var i=0;i<iColl.length;i++){
								select.options[i+1] = new Option();
								select.options[i+1].value = iColl[i].fnc_id;
								select.options[i+1].text =  iColl[i].fnc_name;
							}
			//				var startValue = "${context.IndModel.fna_rep_type}";
			//				IndModel.fna_rep_type._setValue(startValue);
							
				}
			};
			var handleFailure = function(o){ EMPTools.unmask();	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
		  }		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
<emp:tabGroup id="IndModel_tabs" mainTab="tab1">
  <emp:tab label="模型信息" id="tab1"  needFlush="true" initial="true">
	<emp:form id="submitForm" action="updateIndModelRecord.do" method="POST">
		<emp:gridLayout id="IndModelGroup" title="模型信息" maxColumn="2">
			<emp:text id="IndModel.model_no" label="模型编号" maxlength="12" required="true" readonly="true"/>
			<emp:text id="IndModel.model_name" label="模型名称" maxlength="60" required="true" />
			<emp:pop id="IndModel.rating_rules" label="模型评分规则" required="true" url="rulespop.do?id=IndModel.rating_rules" returnMethod="returnMod" colSpan="2"/>
			<emp:radio id="IndModel.biz_belg" label="业务所属" required="true" dictname="STD_ZB_BIZ_BELG" layout="false"/>
			<emp:textarea id="IndModel.rating_rules_displayname" label="模型评分规则描述" readonly="true" colSpan="2"/>
			<emp:textarea id="IndModel.remark" label="备注" maxlength="20" required="false" />
		</emp:gridLayout>
	

		<div align=center>
			<emp:button id="submit" label="保存" op="update"/>
			<emp:button id="returnList" label="返回"/>
		</div>
	</emp:form>	
	</emp:tab>
	<%--
	  /*<emp:tabGroup id="IndModel_tabs" mainTab="IndModelGroup_tab">*/
	--%>
		<emp:tab id="IndModelGroup_tab" label="模型指标组关联设置" url="queryIndModelIndModelGroupList.do" reqParams="IndModel.model_no=$IndModel.model_no;" initial="true"/>
		
	</emp:tabGroup>
</body>
</html>
</emp:page>
