<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>合作方授信协议 列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAgrJointCoop._toForm(form);
		LmtAgrCoopList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAgrCoop() {
		var paramStr = LmtAgrCoopList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrCoopViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrGroup.reset();
	};

	function returnCus(data){
		LmtAgrJointCoop.cus_id._setValue(data.cus_id._getValue());
		LmtAgrJointCoop.cus_id_displayname._setValue(data.cus_name._getValue());
    };

    //冻结
    function doFreezeAgrCoop(){
    	var paramStr = LmtAgrCoopList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrCoopList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/
		var url = '<emp:url action="checkLmtCoopFrozenApp.do"/>?'+paramStr+"&app_type=03";
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("操作失败!");
					return;
				}
				var flag=jsonstr.flag;
				if(flag=="success"){
					var url = '<emp:url action="getLmtCoopFrozenPage.do"/>?'+paramStr+"&app_type=03";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag=="same"){
					alert("该笔协议存在在途的冻结申请，不可以重复发起！");
				}else if(flag=="noSame"){
					alert("该笔协议存在在途的解冻申请，不可以发起冻结！");
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

    //解冻
    function doUnfreezeAgrCoop(){
    	var paramStr = LmtAgrCoopList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrCoopList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/
		var url = '<emp:url action="checkLmtCoopFrozenApp.do"/>?'+paramStr+"&app_type=04";
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("操作失败!");
					return;
				}
				var flag=jsonstr.flag;
				if(flag=="success"){
					var url = '<emp:url action="getLmtCoopFrozenPage.do"/>?'+paramStr+"&app_type=04";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag=="same"){
					alert("该笔协议存在在途的解冻申请，不可以重复发起！");
				}else if(flag=="noSame"){
					alert("该笔协议存在在途的冻结申请，不可以发起解冻！");
				}else if(flag=="nofrozen"){
					alert("该笔协议的冻结金额为0，不能发起解冻申请！");
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
    /**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
    /**add by lisj 2015-6-15 需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = LmtAgrCoopList._obj.getParamStr(['agr_no']);
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
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=09|12";
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4CC',param);
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=09|12";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2015-6-15 需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="LmtAgrGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAgrJointCoop.agr_no" label="授信协议编号"/>
		<emp:pop id="LmtAgrJointCoop.cus_id_displayname" label="合作方客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
		<emp:text id="LmtAgrJointCoop.cus_id" label="合作方客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrCoop" label="查看" op="view"/>
		<emp:button id="freezeAgrCoop" label="冻结" op="froze"/>
		<emp:button id="unfreezeAgrCoop" label="解冻" op="unfroze"/>
		<!-- add by lisj 2015-6-15 需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  -->
		<emp:button id="println" label="封面打印" op="println"/>
	</div>

	<emp:table icollName="LmtAgrCoopList" url="pageLmtAgrCoopQuery.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="cus_id" label="合作方客户码" />
		<emp:text id="cus_id_displayname" label="合作方客户名称" />
		<emp:text id="coop_type" label="合作方类型" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS" />
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    