<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isShow = "oper"; 
	if(context.containsKey("isShow") && !"".equals(context.getDataValue("isShow"))){
		isShow = context.getDataValue("isShow").toString();
	}
	/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  bein**/
	String printFlag ="yes";
	if(context.containsKey("printFlag") && !"".equals(context.getDataValue("printFlag"))){
		printFlag = context.getDataValue("printFlag").toString();
	}
	/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
%>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAgrInfo._toForm(form);
		LmtAgrInfoList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAgrInfo() {
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = "";
			var isShow = '<%=isShow%>';
			if("oper"!=isShow){  //申请时查看
				url = '<emp:url action="getLmtAgrInfoViewPageNoTab.do"/>?'+paramStr+"&subConndition=${context.subConndition}&cus_id=${context.cus_id}&isShow=${context.isShow}&op=view";
			}else{
				url = '<emp:url action="getLmtAgrInfoViewPage.do"/>?'+paramStr+"&menuId=crd_agr&subConndition=${context.subConndition}&cus_id=${context.cus_id}&isShow=${context.isShow}&op=view";
			}
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrInfoGroup.reset();
	};
	
	function doFroze(){
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrInfoList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		var url = '<emp:url action="checkLmtAgrFrozenApp.do"/>?'+paramStr+"&app_type=03";
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){ 
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("操作失败!");
					return;
				}
				var serno=jsonstr.serno;
				if(serno!=""){
					if(serno=="other"){
						alert("该笔协议存在在途的变更或解冻申请，不可以冻结！");
						return;
					}else{
						var appStatus = jsonstr.appStatus;
						var updflag;
						if(appStatus=='000'){//待发起状态
							updflag = 'update';
						}else{
							updflag = 'query';
						}
						var url = '<emp:url action="getLmtFrozenUpdateRecord.do"/>?serno='+serno+"&menuId=LmtAgrInfoFrozen&app_type=03&updflag="+updflag;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}else{
					var url = '<emp:url action="getLmtAgrInfoFrozenPage.do"/>?'+paramStr+"&menuId=LmtAgrInfoFrozen&app_type=03";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("操作失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	function doUnfroze(){
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrInfoList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/	
		var url = '<emp:url action="checkLmtAgrFrozenApp.do"/>?'+paramStr+"&app_type=04";
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("操作失败!");
					return;
				}
				var serno=jsonstr.serno;
				var appStatus = jsonstr.appStatus;
				if(serno!=""){
					if(serno=="other"){
						alert("该笔协议存在在途的变更或冻结申请，不可以解冻！");
						return;
					}else{
						var updflag;
						if(appStatus=='000'){//待发起状态
							updflag = 'update';
						}else{
							updflag = 'query';
						}
						var url = '<emp:url action="getLmtFrozenUpdateRecord.do"/>?serno='+serno+"&menuId=LmtAgrInfoFrozen&app_type=04&updflag="+updflag;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}else{
					if(appStatus=="nofrozen"){
						alert('冻结金额为0，不能进行解冻操作！');
					}else{
						var url = '<emp:url action="getLmtAgrInfoFrozenPage.do"/>?'+paramStr+"&menuId=LmtAgrInfoFrozen&app_type=04";
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("操作失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	function returnCus(data){
		LmtAgrInfo.cus_id._setValue(data.cus_id._getValue());
		LmtAgrInfo.cus_id_displayname._setValue(data.cus_name._getValue());
    };

	function doPrint(){
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		var agr_no = LmtAgrInfoList._obj.getParamValue(['agr_no']);
		var url = '<emp:url action="getReport2ShowPage.do"/>&reportId=ctrLoanCont/sxht.raq&agr_no='+agr_no;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	 /**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
	/**add by lisj  2015-3-18  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	//单一法人封面打印
	function doPrintln(){
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("异步调用通讯发生异常！");
						return;
					}
					var flag=jsonstr.flag;
					if(flag =="success"){
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=01|04|12&jural_flag=lmtapp";
						url = EMPTools.encodeURI(url);
						var param = 'height=500, width=1024, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
						window.open(url,'newWindow4LAI',param);
					}else{
						alert("清空原表报数据失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("清空原表报数据发生异常，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=01|04|12";
			resetUrl  = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj  2015-3-18  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	 /**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="LmtAgrInfoGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAgrInfo.serno" label="业务编号" />
		<emp:text id="LmtAgrInfo.agr_no" label="协议编号" />
		<emp:pop id="LmtAgrInfo.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
		<emp:text id="LmtAgrInfo.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrInfo" label="查看" op="view"/>
		<% if("oper".equals(isShow)){ %>
		<emp:button id="print" label="打印" op="print"/>
		<emp:button id="froze" label="冻结" op="froze"/>
		<emp:button id="unfroze" label="解冻" op="unfroze"/>
		<!-- add by yangzy 2015-6-15  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  begin -->
		<emp:button id="println" label="封面打印" op="println"/>
		<%} %>
		<!-- add by yangzy 2015-6-15  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  end -->
		<%if("view".equals(isShow) && "yes".equals(printFlag)){ %>
			<emp:button id="println" label="封面打印" />
		<%} %>
	</div>

	<emp:table icollName="LmtAgrInfoList" pageMode="true" url="pageLmtAgrInfoQuery.do" reqParams="subConndition=${context.subConndition}&cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency" />
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency" />
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    