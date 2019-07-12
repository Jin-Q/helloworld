<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head>
<title>详情页面</title>
<jsp:include page="/include.jsp" flush="true" />
<script type="text/javascript">
   function doOnLoad(){
	   var is_life_loan = WfiOrgLifeloanRel.is_life_loan._getValue();
    	if(is_life_loan!=null && is_life_loan =="1"){
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderHidden(false);
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderRequired(true);
    	}else{
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderHidden(true);
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderRequired(false);
    		WfiOrgLifeloanRel.life_loan_crd_amt._setValue('');
       }
   };

	function select(){
     	var is_life_loan = WfiOrgLifeloanRel.is_life_loan._getValue();
     	if(is_life_loan!=null && is_life_loan =="1"){
     		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderHidden(false);
     		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderRequired(true);
     	}else{
     		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderHidden(true);
     		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderRequired(false);
     		WfiOrgLifeloanRel.life_loan_crd_amt._setValue('');
        }
	};

	function doSave(){
		if(!WfiOrgLifeloanRel._checkAll()){
			return false;
	 	}else{
	 		var form = document.getElementById("submitForm");
	 		WfiOrgLifeloanRel._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert(e.message);
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						alert('保存成功！');
						window.opener.location.reload();
						doClose();
					}else if(flag =="inexistence"){
						alert("您还未录入社区支行授权审批配置信息！");
					}else{
						alert('保存失败!');
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("异步调用异常，请联系管理员！");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	 	}
    };

    function doGetUpdateWfiLvCreditRight4CBPage() {
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvCreditRight4CBUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiLvCreditRight4CB() {
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvCreditRight4CBViewPage.do"/>?'+paramStr+"&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiLvCreditRight4CBPage() {
		var org_id  = WfiOrgLifeloanRel.org_id._getValue();
		var url = '<emp:url action="getWfiLvCreditRight4CBAddPage.do"/>?cb_org_id='+org_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteWfiLvCreditRight4CB(){
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiLvCreditRightRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败！");
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
    
	function doClose(){
		window.close();
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiLvCreditRight._toForm(form);
		WfiLvCreditRightList._obj.ajaxQuery(null,form);
	};
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateCommBranchCreditRightRecord.do" method="POST">
	<div class='emp_gridlayout_title'>社区支行列表</div>
	<div align="left">
		<emp:button id="getAddWfiLvCreditRight4CBPage" label="新增" />
		<emp:button id="getUpdateWfiLvCreditRight4CBPage" label="修改" />
		<emp:button id="deleteWfiLvCreditRight4CB" label="删除" />
		<emp:button id="viewWfiLvCreditRight4CB" label="查看" />
	</div>
	<emp:table icollName="WfiLvCreditRightList" pageMode="true" url="pageWfiLvCreditRight4CBQuery.do" reqParams="cb_org_id=${context.WfiOrgLifeloanRel.org_id}">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="org_id" label="社区支行编码" />
		<emp:text id="cb_org_name" label="社区支行名称" />
		<emp:text id="org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" hidden="true"/>
		<emp:text id="belg_line" label="客户条线" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="new_crd_amt" label="新增授信审批金额（万元）" dataType="Currency" />
		<emp:text id="stock_crd_amt" label="存量授信审批金额（万元）"  dataType="Currency"/>
		<emp:text id="right_type" label="权限类型"  dictname="STD_ZB_RIGHT_TYPE"/>
	</emp:table>
	<br>
	<emp:gridLayout id="WfiOrgLifeloanRelGroup" title="生活贷权限管理" maxColumn="2">
		<emp:text id="WfiOrgLifeloanRel.cb_org_name" label="机构名称" readonly="true"/>
		<emp:select id="WfiOrgLifeloanRel.is_life_loan" label="是否开通生活贷" dictname="STD_ZX_YES_NO" required="true" onchange="select()" colSpan="2"/>
		<emp:text id="WfiOrgLifeloanRel.life_loan_crd_amt" label="生活贷授信审批金额(万元)" dataType="Currency" required="false" hidden="true"/>
		<emp:text id="WfiOrgLifeloanRel.pk_id" label="主键" hidden="true"/>
		<emp:text id="WfiOrgLifeloanRel.org_id" label="机构码" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="save" label="保存" />
		<emp:button id="close" label="关闭" />
	</div>
	</emp:form>
	</body>
	</html>
</emp:page>