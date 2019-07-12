<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<HTML>
<HEAD>
<TITLE>ECC IDE Jsp file</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="${mvcfile}" -->
<link href="<emp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/dataField.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/pageUtil.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataType.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataField.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataGroup.js'/>" type="text/javascript" language="javascript"></script>

<script type="text/javascript">

    var YAHOO = topPage.YAHOO;
    
	var page = new EMP.util.Page();
	
	function doOnLoad() {
		window.top.checkPermissions('<emp:text dataName="menuId" />', document);
		page.renderEmpTags(document.body);
	}
	
	function CheckroleNoAndSubmit()
	{
	   var roleno;
	   var mark=0;
	   roleno=cmistaggroup_s_role.fields.s_role__roleno.getValue();	
	   
	   var conditionStr = "roleno ='" + roleno + "'";
	   var url = "<emp:url action='getroleId__s_role.do'/>&conditionStr="+conditionStr;
	   url=encodeURI(url);
	   var handleSuccess = function(o){ EMPTools.unmask();	
			if(o.responseText != undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e.message);
					return false;
				}
				if(jsonstr.s_role__resultset.length==0){
					var detailform=document.getElementById('detailform');
	            	if(cmistaggroup_s_role.toForm(detailform))
	            		detailform.submit();
				}else 
					alert('角色码已存在，请重新输入！');		
				}
		};
		
		var handleFailure = function(o){ EMPTools.unmask();
			alert('failure');
			return false;
		};	
		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	}	
	function doReturnBack(){
		window.close();
	};
	 
</script>

</HEAD>
<BODY onload="doOnLoad()" onbeforeunload="window.top.setMainFrameWait();">
<emp:group id="s_role" maxColumn="1" css="cmisgroup">
	<emp:text id="s_role__roleno" label="角色编号" cnname="角色编号" enname="s_role__roleno" 
		css="cmistag_text"  required="true" maxlength="4" />
	<emp:text id="s_role__rolename"  label="角色名称" cnname="角色名称" enname="s_role__rolename" 
		css="cmistag_text" help="" mustInput="true" maxlen="40" />
	<emp:text id="s_role__orderno"  label="排序号" cnname="排序号" enname="s_role__orderno" 
		css="cmistag_text" help="" mustInput="false" maxlen="10" dataType="number"/>
	<emp:text id="s_role__roletype"  label="角色类别" cnname="角色类别" enname="s_role__roletype" 
		css="cmistag_text" help="" mustInput="true" maxlen="10" hidden="true" defvalue="0"/>
	<emp:text id="s_role__memo"  label="备注" cnname="备注" enname="s_role__memo"  css="cmistag_text"
		help="" mustInput="false" maxlen="60" />
</emp:group>

<form id="detailform" action='<emp:url action="addRolePermission.do" />' method="POST"
	onsubmit="return cmistaggroup_s_role.toForm(this)">
<button onclick="CheckroleNoAndSubmit();">提交</button>&nbsp;
<input type="submit" style="display:none" id="submitButton" />
<button onclick="doReturnBack()">返回</button>
</form>

</BODY></HTML>