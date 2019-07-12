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
		LmtAgrIndiv._toForm(form);
		LmtAgrIndivList._obj.ajaxQuery(null,form);
	};

	//查看详情
	function doViewLmtAgrIndiv() {
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrIndivViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrIndivGroup.reset();
	};
	
	//发起冻结申请
	function doFrozLmtAgrIndiv(){
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrIndivList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/
		if(confirm("是否确认要进行冻结？")){
			var url = '<emp:url action="checkLmtAgrIndivFrozenApp.do"/>?'+paramStr+"&app_type=03";
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
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
							var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?serno='+serno+"&app_type=03&menuId=lmtFrozeIndiv&updflag="+updflag;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else{
						var url = '<emp:url action="getLmtIndivFrozenUpdateRecord.do"/>?'+paramStr+"&app_type=03&menuId=lmtFrozeIndiv&updflag=update";
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
	}

	//发起解冻申请
	function doUnfrozLmtAgrIndiv(){
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrIndivList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/
		if(confirm("是否确认要进行解冻？")){
			var url = '<emp:url action="checkLmtAgrIndivFrozenApp.do"/>?'+paramStr+"&app_type=04";
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
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
							var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?serno='+serno+"&app_type=04&menuId=lmtFrozeIndiv&updflag="+updflag;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else{
						if(appStatus=="nofrozen"){
							alert('冻结金额为0，不能进行解冻操作！');
						}else{
							var url = '<emp:url action="getLmtIndivFrozenUpdateRecord.do"/>?'+paramStr+"&app_type=04&menuId=lmtFrozeIndiv&updflag=update";
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
	}

	function returnCus(data){
		LmtAgrIndiv.cus_id._setValue(data.cus_id._getValue());
		LmtAgrIndiv.cus_id_displayname._setValue(data.cus_name._getValue());
    };

    function doPrint(){
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		var agr_no = LmtAgrInfoList._obj.getParamValue(['agr_no']);
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/bzdbflyjzlgr.raq&agr_no='+agr_no;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}
    /**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
    /**add by lisj  2015-3-18  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	//个人封面打印
	function doPrintln(){
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
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
						var param = 'height=600, width=1024, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
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
			resetUrl = EMPTools.encodeURI(resetUrl);
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
		<emp:gridLayout id="LmtAgrIndivGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtAgrIndiv.agr_no" label="协议编号" />
			<emp:text id="LmtAgrIndiv.serno" label="业务编号" />
			<emp:pop id="LmtAgrIndiv.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE='BL300'&returnMethod=returnCus" />
			<emp:text id="LmtAgrIndiv.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrIndiv" label="查看" op="view"/>
		<emp:button id="print" label="打印" op="print"/>
		<emp:button id="frozLmtAgrIndiv" label="冻结" op="froze"/>
		<emp:button id="unfrozLmtAgrIndiv" label="解冻" op="unfroze"/>
		<!-- add by lisj 2015-6-12  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin -->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by lisj 2015-6-12  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end -->
	</div>

	<emp:table icollName="LmtAgrIndivList" pageMode="true" url="pageLmtAgrIndivQuery.do">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="self_amt" label="自助金额" dataType="Currency"/>
		<emp:text id="totl_start_date" label="授信起始日" />
		<emp:text id="totl_end_date" label="授信到期日" />	
		<emp:text id="manager_id_displayname" label="责任人" />	
		<emp:text id="manager_br_id_displayname" label="责任机构" />	
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    