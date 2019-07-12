<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
/*--user code begin--*/
	function doOnload(){
		LmtAppNameList.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		showOrHiddenDiv();
	}
	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppNameList.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	//确定保存
	function doSure(){
		if(!LmtAppNameList._checkAll()){
			return ;
		}
		var handSuc = function(o){
			if(o.responseText !== undefined) {
				try { var jsonstr = eval("("+o.responseText+")"); } 
					catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "suc"){
					alert("保存成功!");
					doReturn();
				}else{
					alert(flag);
				}
			}
		};
	    var handFail = function(o){};
	    var callback = {
	    	success:handSuc,
	    	failure:handFail
	    };
	    var form = document.getElementById("submitForm");
		LmtAppNameList._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action, callback,postData);
	}
	//返回
	function doReturn(){
		var url = '<emp:url action="queryLmtNameList_jointList.do"/>?serno=${context.serno}&cus_id=${context.cus_id_zz}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//新增分项信息
	function doGetAddLmtAppDetailsPage(){
	/*	var is_limit_set = LmtAppNameList.is_limit_set._getValue();
		if(is_limit_set!='1'){
			alert('是否进行额度设置选项选择是才可以新增额度信息！');
			return;
		}*/
		var count = LmtAppDetailsList._obj.recordCount;
		if(count==0){
			var cus_id = LmtAppNameList.cus_id._getValue();
			var url = '<emp:url action="getLmtAppDetailsJointAddPage.do"/>?serno=${context.serno}&belg_line=${context.belg_line}&type=03&cus_id='+cus_id;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}else{
			alert('每个联保小组成员只能新增一笔额度分项！');
		}
	}
	//修改分项信息
	function doGetUpdLmtAppDetailsPage() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppDetailsJointUpdatePage.do"/>?'+paramStr+"&belg_line=${context.belg_line}";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//删除分项信息
	function doDeleteLmtAppDetails(){
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteLmtAppDetailsJointRecord.do"/>&'+ paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//查看分项信息
	function doViewLmtAppDetails() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code','sub_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppDetailsJointViewPage.do"/>?'+paramStr;//+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//控制div的显示隐藏
	function showOrHiddenDiv(){
		var is_limit_set = LmtAppNameList.is_limit_set._getValue();
		if(is_limit_set=='1'){
			document.getElementById("appDetails_div").style.display = "";
		}else{
			document.getElementById("appDetails_div").style.display = "none";
		}
	}
/*--user code end--*/

</script>
</head>
<body class="page_content" onload="doOnload()">

<emp:form id="submitForm" action="updateLmtAppNameListRecord.do" method="POST">
	
	<emp:gridLayout id="LmtAppNameListGroup" title="联保小组成员信息修改" maxColumn="2">
		<emp:text id="LmtAppNameList.cus_id" label="客户码" readonly="true" colSpan="2" required="true" />
		<emp:text id="LmtAppNameList.cus_id_displayname" label="客户名称" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		<emp:text id="LmtAppNameList.bail_rate" label="保证金比例" maxlength="16" required="true" dataType="Percent" colSpan="2"/>
		<emp:select id="LmtAppNameList.is_limit_set" label="是否进行额度设置" hidden="true" required="true" dictname="STD_ZX_YES_NO" defvalue="1" onchange="showOrHiddenDiv()"/>
		<emp:textarea id="LmtAppNameList.memo" label="备注" maxlength="400" required="false" colSpan="2" />
		<emp:text id="LmtAppNameList.serno" label="流水号" maxlength="40" required="true" hidden="true"/>
		<emp:select id="LmtAppNameList.sub_type" label="分项类别" required="false" dictname="STD_LMT_PROJ_TYPE" defvalue="03" colSpan="2" readonly="true" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="sure" label="保存"/>
		<emp:button id="reset" label="重置"/>
		<emp:button id="return" label="返回"/>
	</div>
</emp:form>

	<div align="left" id="appDetails_div">
	<div class='emp_gridlayout_title'>联保小组成员额度分项&nbsp;</div>
	<div align="left">
		<emp:actButton id="getAddLmtAppDetailsPage" label="新增" op="add"/>
		<emp:actButton id="getUpdLmtAppDetailsPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtAppDetails" label="删除" op="remove"/>
		<emp:actButton id="viewLmtAppDetails" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAppDetailsList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="limit_name" label="额度品种名称" hidden="true"/>
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="term" label="授信期限" />
	</emp:table>
	</div>
</body>
</html>
</emp:page>
