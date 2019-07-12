<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doQuery(){
		var form = document.getElementById('queryForm');
		CommBranchCreditRight._toForm(form);
		CommBranchCreditRightList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CommBranchCreditRightGroup.reset();
	};
	
	function doGetUpdateWfiLvCreditRight4CB() {
		var paramStr = CommBranchCreditRightList._obj.getParamStr(['pk_id']);
		var org_id = CommBranchCreditRightList._obj.getParamValue(['org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCommBranchCreditRightUpdatePage.do"/>?'+paramStr+"&cb_org_id="+org_id;
			url = EMPTools.encodeURI(url);
			window.open(url,'viewCommBranchCRI','height=500,width=800,top=150,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiLvCreditRight4CB() {
		var paramStr = CommBranchCreditRightList._obj.getParamStr(['pk_id']);
		var org_id = CommBranchCreditRightList._obj.getParamValue(['org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCommBranchCreditRightViewPage.do"/>?'+paramStr+"&cb_org_id="+org_id;
			url = EMPTools.encodeURI(url);
			window.open(url,'viewCommBranchCRI','height=500,width=800,top=150,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doDeleteWfiLvCreditRight4CB() {
		var paramStr = CommBranchCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("删除该机构配置将会删除该机构下所有社区支行授信授权配置，是否确认删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("语法错误！");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();
						}else if(msg=='fail'){
							alert('删除失败!');
						}else{
							alert('删除成功，但写入操作记录异常！');
						}
					}	
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var url = '<emp:url action="deleteCommBranchCreditRightRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doAddWfiLvCreditRight4CB() {
		var url = '<emp:url action="getWfiLvCreditRightOrgInfoViewPage.do"/>?right_type=03';
		url = EMPTools.encodeURI(url);
      	window.open(url,'viewOrgInfo','height=400,width=800,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=yes');
	};
	//返回社区支行机构
	function getCBName(data){
		CommBranchCreditRight.org_id._setValue(data.comm_branch_id._getValue());
		CommBranchCreditRight.org_id_displayname._setValue(data.comm_branch_name._getValue());
	};
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CommBranchCreditRightGroup" title="输入查询条件" maxColumn="2">
	    <emp:pop id="CommBranchCreditRight.org_id_displayname" label="社区支行机构名称" required="true" url="queryCommBranchPop.do" returnMethod="getCBName"/>
		<emp:text id="CommBranchCreditRight.org_id" label="社区支行机构码" maxlength="16" readonly="true" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="addWfiLvCreditRight4CB" label="新增" />
		<emp:button id="getUpdateWfiLvCreditRight4CB" label="修改" op="update"/>
		<emp:button id="deleteWfiLvCreditRight4CB" label="删除" />
		<emp:button id="viewWfiLvCreditRight4CB" label="查看" op="view"/>
	</div>

	<emp:table icollName="CommBranchCreditRightList" pageMode="true" url="pageCommBranchCreditRightQuery.do?right_type=03">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="org_id" label="社区支行编码" />
		<emp:text id="cb_org_name" label="社区支行名称" />
		<emp:text id="is_life_loan" label="是否开通生活贷" dictname="STD_ZX_YES_NO" />
		<emp:text id="life_loan_crd_amt" label="生活贷授信审批金额(万元)"  dataType="Currency"/>
		<emp:text id="right_type" label="权限类型"  dictname="STD_ZB_RIGHT_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    