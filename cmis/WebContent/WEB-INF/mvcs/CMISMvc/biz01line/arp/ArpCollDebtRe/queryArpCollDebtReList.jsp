<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno = "";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpCollDebtRe._toForm(form);
		ArpCollDebtReList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpCollDebtRePage() {
		var paramStr = ArpCollDebtReList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDebtReUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpCollDebtRe(){
		var paramStr = ArpCollDebtReList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpCollDebtRePage() {
		var url = '<emp:url action="getArpCollDebtReAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpCollDebtRe() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("记录已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteArpCollDebtReRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpCollDebtReGroup.reset();
	};
	
	function doGetUpdateMortGuarantyBaseInfoPage() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		var status = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_info_status']);
		if (paramStr != null) {
			if(status=='guaranty_info_status=3'){
				alert("押品生效状态的押品不能进行修改！");
			}else{
				var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?op=update&cus_id=${context.cus_id}&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=yes, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortGuarantyBaseInfoPage() {
		//tab=tab用来控制关闭按钮
		//relation=guar用来控制是担保合同下的押品新增（新增的同时需要新增担保关联记录）
		var serno = '<%=serno%>';
		var url = '<emp:url action="getMortGuarantyBaseInfoAddPage.do"/>?rel=rel&serno='+serno+'&tab=tab';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'AddMortGuarantyBaseInfoPage',param);
	};
	
	function doDeleteMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("记录已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteGrtGuarantyReRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//已存在的引入
	function doIntroGuaranty(){//参数arpCD标志引入时的是业务中引用的
		var serno = '<%=serno%>';
		var url = '<emp:url action="queryMortGuarantyPopList.do"/>?arpCD=arpCD&rel=1&serno='+serno+'&returnMethod=getGuaranty';
		url = EMPTools.encodeURI(url);
		var param = "height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
		window.open(url,'newWindow',param);
	};
	//新增的引入
	function doIntroGuarantyNew(){//参数arpCD标志引入时的是业务中引用的
		var serno = '<%=serno%>';
		var url = '<emp:url action="queryMortGuarantyPopList.do"/>?arpCD=arpCD&rel=2&serno='+serno+'&returnMethod=getGuaranty1';
		url = EMPTools.encodeURI(url);
		var param = "height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
		window.open(url,'newWindow',param);
	};
	//引入的引入
	function getGuaranty(data){
		var guaranty_no = data.guaranty_no._getValue();
		var serno = '<%=serno%>';
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert(msg);
					window.location.reload();
				}else{
					alert(msg);
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="introGuarantyArp.do"/>?serno='+serno+'&rel=1&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	//新增的引入
	function getGuaranty1(data){
		var guaranty_no = data.guaranty_no._getValue();
		var serno = '<%=serno%>';
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert(msg);
					window.location.reload();
				}else{
					alert(msg);
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="introGuarantyArp.do"/>?serno='+serno+'&rel=2&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	//重置引入按钮操作事件
	function doDestoryGuaranty(){
		var param = ArpCollDebtReList._obj.getParamStr(['serno','guaranty_no']);
		if(param==null){
			alert("请选择一条记录！");
			return;
		}
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert(msg);
					window.location.reload();
				}else{
					alert(msg);
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="introGuarantyArp.do"/>&flag=destory&'+param;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	// 异步保存客户经理
	function doConfirmSubmit(){
		if(!MortGuarantyBaseInfoList._checkAll()|!ArpCollDebtReList._checkAll()){
			return false;
		}
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("保存成功！");
						window.location.reload();
					}else if(flag == 'fail'){
						alert("没有抵债物需要保存！");
					}
				}
			};
			var callback = {
					success:handleSuccess,
					failure:function(){alert("保存失败！");}
			};
			var form = document.getElementById('submitForm');
	    	ArpCollDebtReList._toForm(form);
	    	MortGuarantyBaseInfoList._toForm(form);
	    	var url = '<emp:url action="addArpCollDebtReRecord.do"/>'; 
			url = EMPTools.encodeURI(url);
			var data = YAHOO.util.Connect.setForm(form);
			var obj2 = YAHOO.util.Connect.asyncRequest('post', url,callback,data);
	}
	 
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div class='emp_gridlayout_title'>引入已使用的抵质押品作为抵债物</div>
	<div align="left">
		<emp:actButton id="introGuaranty" label="引入担保品" op="update"/>
		<emp:actButton id="destoryGuaranty" label="重置引入" op="update"/>
		<emp:actButton id="viewArpCollDebtRe" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpCollDebtReList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="debt_acc_no" label="抵债台账编号" hidden="true" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="guaranty_type_displayname" label="押品类型" />
		<emp:text id="debt_in_amt" label="抵入金额" flat="false" required="true" dataType="Currency" defvalue="0.00"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_DEBT_RE_STATUS" />
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" hidden="true"/>
	</emp:table>
	<div class='emp_gridlayout_title'>新增的抵债物列表 </div>
	<div align="left">
		<emp:actButton id="introGuarantyNew" label="引入担保品" op="update" />
		<emp:actButton id="getAddMortGuarantyBaseInfoPage" label="新增" op="update" />
		<emp:actButton id="getUpdateMortGuarantyBaseInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpCollDebtRe" label="删除" op="update"/>
		<emp:actButton id="viewMortGuarantyBaseInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="debt_acc_no" label="抵债台账编号" hidden="true" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="guaranty_type_displayname" label="押品类型" />
		<emp:text id="debt_in_amt" label="抵入金额" flat="false" required="true" dataType="Currency" defvalue="0.00"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_DEBT_RE_STATUS" />
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" hidden="true"/>
	</emp:table>
	<emp:form id="submitForm" action="" method="POST"></emp:form>
	<div align="center" >
		<br>
			<emp:actButton id="confirmSubmit" label="保存列表" op="save"/>
	</div>
</body>
</html>
</emp:page>
    