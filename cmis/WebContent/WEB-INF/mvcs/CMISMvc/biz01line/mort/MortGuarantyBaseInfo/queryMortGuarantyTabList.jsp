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
		MortGuarantyBaseInfo._toForm(form);
		MortGuarantyBaseInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortGuarantyBaseInfoPage() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		var status = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_info_status']);
		if (paramStr != null) {
			if(status=='guaranty_info_status=3'){
				alert("押品生效状态的押品不能进行修改！");
			}else{
				/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
				//lrisk_type =10 判断授信申请风险类型，用于控制【押品所有权是否为非关联方第三人】字段
				var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?op=update&cus_id=${context.cus_id}&'+paramStr+'&menuIdTab=mort_maintain&tab=tab&lrisk_type=${context.lrisk_type}';
				/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=yes, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
			//lrisk_type =10 判断授信申请风险类型，用于控制【押品所有权是否为非关联方第三人】字段
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab&lrisk_type=${context.lrisk_type}';
			/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortGuarantyBaseInfoPage() {
		//tab=tab用来控制关闭按钮
		//relation=guar用来控制是担保合同下的押品新增（新增的同时需要新增担保关联记录）
		/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 begin**/
		//lrisk_type =10 判断授信申请风险类型，用于控制【押品所有权是否为非关联方第三人】字段
		var url = '<emp:url action="getMortGuarantyBaseInfoAddPage.do"/>?relation=guar&cus_id=${context.cus_id}&guar_cont_no=${context.guar_cont_no}&tab=tab&lrisk_type=${context.lrisk_type}';
		/**add by lisj 2015-10-26 需求编号：XD150710052 新增对【押品所有权是否为非关联方第三人】录入信息控制 end**/
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
	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};
	function doIntroGuaranty(){//参数Intro标志引入时的是已完成和出库核销状态的押品
		var url = '<emp:url action="queryMortGuarantyPopList.do"/>?Intro=intro&returnMethod=getGuaranty';
		url = EMPTools.encodeURI(url);
		var param = "height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
		window.open(url,'newWindow',param);
	};
	function getGuaranty(data){
		var guaranty_no = data.guaranty_no._getValue();
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
		var url = '<emp:url action="introGuaranty.do"/>?guar_cont_no=${context.guar_cont_no}&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		
	}
	//入库按钮
	function doStorageMortGuarantyBaseInfo(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=to_storage&menuIdTab=mort_maintain&stay=stay_storage&'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = "height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no";
			window.open(url,'storage',param);
		} else {
			alert('请先选择一条记录！');
		}
		
    }

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:actButton id="introGuaranty" label="引入担保品" op="intro" />
		<emp:actButton id="getAddMortGuarantyBaseInfoPage" label="新增" op="add" />
		<emp:actButton id="getUpdateMortGuarantyBaseInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteMortGuarantyBaseInfo" label="删除" op="remove"/>
		<emp:actButton id="viewMortGuarantyBaseInfo" label="查看" op="view"/>
		<emp:actButton id="storageMortGuarantyBaseInfo" label="入库" op="storage"/>
	</div>

	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="true" url="pageMortGuarantyBaseInfoQuery.do">
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="cus_id_displayname" label="抵质押人名称" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="wrr_amt" label="权利金额（元）" dataType="Currency"/>
		<emp:text id="guar_amt" label="担保金额（元）" dataType="Currency"/>
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    