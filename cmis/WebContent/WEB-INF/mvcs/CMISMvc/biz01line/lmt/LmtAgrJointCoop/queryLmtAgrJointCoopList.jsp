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
		LmtAgrJointCoop._toForm(form);
		LmtAgrJointCoopList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAgrJointCoop() {
		var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrJointCoopViewPage.do"/>?'+paramStr + "&view=yes&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrJointCoopGroup.reset();
	};
	
	function returnCus(data){
		LmtAgrJointCoop.cus_id._setValue(data.cus_id._getValue());
		LmtAgrJointCoop.cus_id_displayname._setValue(data.cus_name._getValue());
    };
    /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
    function returnCusMember(data){
    	LmtAgrJointCoop.cus_member_id._setValue(data.cus_id._getValue());
    	LmtAgrJointCoop.cus_member_name._setValue(data.cus_name._getValue());
    };
    /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
    //解散联保小组
    function doDisBandJointCoop(){
    	var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr == null) {
			alert('请先选择一条记录！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var manager_id = LmtAgrJointCoopList._obj.getParamValue('manager_id');
		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
		var agr_status = LmtAgrJointCoopList._obj.getParamValue(['agr_status']);
		if(agr_status!='002'){
			alert('只有状态为[生效]的协议才能做解散操作！');
			return;
		}
		if(confirm('确定要解散该联保小组？')){
			var url = '<emp:url action="disBandJointCoop.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("解散联保小组失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						alert("联保小组解散成功！");
						window.location.reload();
					}else if(flag=="exists"){
						alert("该联保协议关联担保合同关系未全部解除，不能解散！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("解散联保小组失败，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
    }

    //冻结
    function doFreezeJointCoop(){
    	var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr == null) {
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrJointCoopList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/	
		var agr_status = LmtAgrJointCoopList._obj.getParamValue(['agr_status']);
		if(agr_status!='002'){
			alert('只有状态为[生效]的协议才能做冻结操作！');
			return;
		}
		if(confirm('确定要对该联保协议进行冻结操作？')){
			var url = '<emp:url action="freezeOrUnfreezeJointCoop.do"/>?'+paramStr+"&app_type=03&menuId=lmtAppJointFrozen";
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("联保协议解冻失败!");
						return;
					}
					var flag=jsonstr.flag;
					var serno=jsonstr.serno;
					if(flag=="success"){
						var urlTmp = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>?serno='+serno+"&op=view&type=frozen";
						urlTmp = EMPTools.encodeURI(urlTmp);
						window.location = urlTmp;
					}else if(flag=="exists"){
						alert("该联保协议存在在途的冻结申请，不能重复发起！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("联保协议冻结失败，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
    }

    //解冻
    function doUnfreezeJointCoop(){
    	var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr == null) {
			alert('请先选择一条记录！');
			return;
		}
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 begin**/
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var manager_id = LmtAgrJointCoopList._obj.getParamValue('manager_id');
		if(((rolesList.indexOf("1028")<0) && (rolesList.indexOf("1044")<0)) && manager_id!=null && manager_id!='' && currentUserId != manager_id){
			alert('非当前客户主管客户经理，操作失败！');
			return;
		}
		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
		/**modified by lisj 2015-6-10 同业业务员/授信冻结岗具有全行冻结解冻权限，于2015-6-11上线 end**/
		var agr_status = LmtAgrJointCoopList._obj.getParamValue(['agr_status']);
		if(agr_status!='004'){
			alert('只有状态为[冻结]的协议才能做解冻操作！');
			return;
		}
		if(confirm('确定要对该联保协议进行解冻操作？')){
			var url = '<emp:url action="freezeOrUnfreezeJointCoop.do"/>?'+paramStr+"&app_type=04&menuId=lmtAppJointFrozen";
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("联保协议解冻失败!");
						return;
					}
					var flag=jsonstr.flag;
					var serno=jsonstr.serno;
					if(flag=="success"){
						var urlTmp = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>?serno='+serno+"&op=view&type=frozen";
						urlTmp = EMPTools.encodeURI(urlTmp);
						window.location = urlTmp;
					}else if(flag=="exists"){
						alert("该联保协议存在在途的解冻申请，不能重复发起！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("联保协议解冻失败，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
    }

    function doUpdBail(){
    	var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var agr_status = LmtAgrJointCoopList._obj.getParamValue(['agr_status']);
			if(agr_status!='002'){
				alert('只有状态为[生效]的协议才能做此操作！');
				return;
			}
			var url = '<emp:url action="getLmtAgrJointCoopViewPage.do"/>?'+paramStr + "&view=yes&op=updBail";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
    }

    /**add by lisj/yangzy  2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	//联保小组授信封面打印
	function doPrintln(){
		var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrJointCoopViewPage.do"/>?'+paramStr + "&isShow=view&view=yes&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj/yangzy  2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="LmtAgrJointCoopGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="LmtAgrJointCoop.cus_id_displayname" label="组长客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:text id="LmtAgrJointCoop.agr_no" label="协议号" />
		<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */-->
		<emp:pop id="LmtAgrJointCoop.cus_member_name" label="联保成员名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCusMember" />
		<emp:text id="LmtAgrJointCoop.cus_id" label="组长客户码" hidden="true"/>
		<emp:text id="LmtAgrJointCoop.cus_member_id" label="组长客户码" hidden="true"/>
		<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */-->
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrJointCoop" label="查看" op="view"/>
		<emp:button id="disBandJointCoop" label="解散联保" op="disband"/>
		<emp:button id="freezeJointCoop" label="冻结" op="froze"/>
		<emp:button id="unfreezeJointCoop" label="解冻" op="unfroze"/>
		<emp:button id="updBail" label="保证金维护" op="updBail"/>
		<emp:button id="println" label="封面打印" op="println"/>
	</div>

	<emp:table icollName="LmtAgrJointCoopList" pageMode="true" url="pageLmtAgrJointCoopQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="agr_no" label="协议号" />
		<emp:text id="cus_id" label="组长客户码" />
		<emp:text id="cus_id_displayname" label="组长客户名称" />
		<emp:text id="coop_type" label="合作方类型 " dictname="STD_ZB_COOP_TYPE" hidden="true"/>
		<emp:text id="share_range" label="共享范围" dictname="STD_SHARED_SCOPE" hidden="true"/>
		<emp:text id="belg_org" label="所属机构" hidden="true"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任人机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    