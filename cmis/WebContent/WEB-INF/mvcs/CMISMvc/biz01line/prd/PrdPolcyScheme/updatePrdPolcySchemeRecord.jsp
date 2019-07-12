<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
button.table_btn {
		color:#000;
		border-width:0;
		width:60px;
		height:18px;
		line-height:25px;
		cursor:pointer;
		font-size: 12px;
		background-color: #D4E3ED; 
		background-image: url(../images/center_bg.gif);
		background-repeat: repeat-x;
	}

</style>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doConn(){
		var schemeId = PrdPolcyScheme.schemeid._getValue();
		var data = PrdPlocyList._obj.getSelectedData();
		var schemecodeArr = "";
		//组装多记录选择返回参数
		for(var i=0;i<data.length;i++){
			schemecodeArr += data[i].schemecode._getValue()+",";
		}
		var url="<emp:url action='doConnPrdPlocyAndPrdScheme.do'/>?schemeId="+schemeId+"&schemecodeArr="+schemecodeArr;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, "",null)

		window.location.reload();
	}

	function doConnPlocy(){
		var url = '<emp:url action="queryPrdPlocyListPop.do"/>?schemeId=${context.PrdPolcyScheme.schemeid}&returnMethod=setOrgName';
		url = EMPTools.encodeURI(url);
		var popparam = 'height=500, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',popparam);
	}

	function doDelPlocy(){
		var schemeId = PrdPolcyScheme.schemeid._getValue();
		var data = PrdPlocyList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择删除政策资料！");
			return false;
		}else {
			if(confirm("是否确认要删除？")){
				var schemecodeArr = "";
				//组装多记录选择返回参数
				for(var i=0;i<data.length;i++){
					schemecodeArr += data[i].schemecode._getValue()+",";
				}
				var url="<emp:url action='delConnPlocyWithScheme.do'/>?schemeId="+schemeId+"&schemecodeArr="+schemecodeArr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, "",null)
				alert("删除成功！");
				window.location.reload();
			}
		}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePrdPolcySchemeRecord.do" method="POST">
		<emp:tabGroup mainTab="base_tab" id="main_tabs">
			<emp:tab label="政策资料方案配置" id="base_tab" needFlush="true" initial="true" >
				<emp:gridLayout id="PrdPolcySchemeGroup" maxColumn="2" title="政策资料设置方案">
					<emp:text id="PrdPolcyScheme.schemeid" label="方案编号" maxlength="30" required="true" readonly="true" />
					<emp:text id="PrdPolcyScheme.schemename" label="方案名称" maxlength="40" required="false" readonly="true" />
					<emp:select id="PrdPolcyScheme.effectived" label="是否启用" dictname="STD_ZX_YES_NO" required="true" />
					<emp:textarea id="PrdPolcyScheme.comments" label="备注" colSpan="2"  maxlength="200" required="false" />
					<emp:text id="PrdPolcyScheme.inputid" label="登记人员" maxlength="20" required="false" readonly="true" hidden="true" />
					<emp:text id="PrdPolcyScheme.inputdate" label="登记日期" maxlength="10" required="false" readonly="true" hidden="true"/>
					<emp:text id="PrdPolcyScheme.orgid" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				</emp:gridLayout>
				<div  class='emp_gridlayout_title'>政策资料关联配置</div>
				<div id="tempButton" style="display:${param.optype}" >
				  	<emp:button id="connPlocy" label="引入政策资料"  op="update"/>
				  	<emp:button id="delPlocy" label="移除政策资料关联"/>
				</div>
				<emp:table icollName="PrdPlocyList" pageMode="true" selectType="2" url="pagePrdPlocyQuery.do">
					<emp:text id="schemecode" label="政策资料代码" />
					<emp:text id="schemedesc" label="政策资料描述" />
					<emp:text id="ifwarrant" label="是否权证类" dictname="STD_ZX_YES_NO"/>
					<emp:text id="schemetype" label="政策资料类型" dictname="STD_ZB_INFO_TYPE"/>
					<emp:text id="inputid" label="登记人员" />
					<emp:text id="inputdate" label="登记日期" />
					<emp:text id="orgid" label="登记机构" />
				</emp:table>
			</emp:tab>
			<emp:tab label="政策资料关联场景配置" id="plocy_base" needFlush="true" url="getSpaceApplyListBySchemeId.do?schemeId=${context.PrdPolcyScheme.schemeid}" >
			</emp:tab>
		</emp:tabGroup>
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
