<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		try{
			if(typeof(opener.document)=='object'){
				window.close();
			}
			return;
		} catch(e){

		}
		
		var url = '<emp:url action="queryIndModelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewIndModelGroup() {
		var paramStr = IndModel.IndModelGroup._obj.getParamStr(['model_no','group_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndModelIndModelGroupDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doload(){
		getRepType();
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
						var select = IndModel.fna_rep_type._obj.element;
						select.length=1;
						for(var i=0;i<iColl.length;i++){
							select.options[i+1] = new Option();
							select.options[i+1].value = iColl[i].fnc_id;
							select.options[i+1].text =  iColl[i].fnc_name;
						}
						var startValue = "${context.IndModel.fna_rep_type}";
						IndModel.fna_rep_type._setValue(startValue);
						
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
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
<emp:tabGroup id="IndModel_tabs" mainTab="tab1">
	<emp:tab label="模型信息" id="tab1"  needFlush="true" initial="true">
	<emp:gridLayout id="IndModelGroup" title="模型信息" maxColumn="2">
			<emp:text id="IndModel.model_no" label="模型编号" maxlength="12" required="true" readonly="true"/>
			<emp:text id="IndModel.model_name" label="模型名称" maxlength="60" required="false" />
			<emp:text id="IndModel.rating_rules" label="模型评分规则" required="false" colSpan="2"/>
			<emp:select id="IndModel.biz_belg" label="业务所属" required="true" dictname="STD_ZB_BIZ_BELG"/>
			<emp:textarea id="IndModel.rating_rules_displayname" label="模型评分规则描述" readonly="true" required="false" colSpan="2"/>
			<emp:textarea id="IndModel.remark" label="备注" maxlength="20" required="false" />
			<emp:text id="IndModel.input_id" label="登记人" maxlength="20" defvalue="$currentUserId" hidden="false" />
			<emp:text id="IndModel.input_date" label="登记日期" maxlength="20" defvalue="$OPENDAY" hidden="false" />
			<emp:text id="IndModel.input_br_id" label="登记机构" maxlength="20" defvalue="$organNo" hidden="false" />
			</emp:gridLayout>
	</emp:tab>
	<br>
 <%--
	<emp:tabGroup id="IndModel_tabs" mainTab="IndModelGroup_tab">
 --%>
		<emp:tab id="IndModelGroup_tab" label="模型指标组关联设置">
			<div align="left">
				<emp:button id="viewIndModelGroup" label="查看" op="view_IndModelGroup"/>
			</div>
			<emp:table icollName="IndModel.IndModelGroup" pageMode="false" url="">
		<emp:text id="model_no" label="模型编号" />
		<emp:text id="group_no" label="组别编号" />
		<emp:text id="weight" label="权重" />
		<emp:text id="seqno" label="顺序号" />
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
