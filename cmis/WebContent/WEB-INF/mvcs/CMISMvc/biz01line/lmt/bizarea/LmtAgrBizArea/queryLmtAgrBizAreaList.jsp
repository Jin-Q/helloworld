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
		LmtAgrBizArea._toForm(form);
		LmtAgrBizAreaList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAgrBizArea() {
		var paramStr = LmtAgrBizAreaList._obj.getParamStr(['agr_no','biz_area_type','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryLmtAgrBizAreaAllDetails.do"/>?'+paramStr ;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrBizAreaGroup.reset();
	};
	
	//入圈
	function doAddWiz(){
		var paramStr = LmtAgrBizAreaList._obj.getParamStr(['agr_no','agr_status','single_max_amt','end_date','biz_area_type']);
		if (paramStr != null) {
			var agr_status = LmtAgrBizAreaList._obj.getSelectedData()[0].agr_status._getValue();
			if( agr_status != '002' )  {//无效标志
				alert("无效圈商不能申请");
				return ;
			}
			if(!confirm("是否确认要发起入圈申请？")){
				return;
			}
			paramStr += '&app_flag=0';
			var url = '<emp:url action="addLmtAppJoinBackRecord.do"/>?'+paramStr ;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("数据库操作失败!");
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					if(flag == 'suc'){
						var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr+"&serno="+serno+'&process=no&op=update&menuId=joinBackBizArea';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if( flag == 'existTaskUpd' ){//如果已存在待发起任务则跳转修改
						if(confirm("已存在待发起的入圈申请,确认跳转修改?")){
							var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr+'&app_flag=0&serno='+serno+'&process=no&op=update&menuId=joinBackBizArea';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else if(flag == 'existTaskView'){//如果已经存在提交任务则跳转查看
						var url = '<emp:url action="getLmtAppJoinBackViewPage.do"/>?'+paramStr+'&app_flag=0&serno='+serno+'&process=no&op=view&menuId=joinBackBizArea';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == 'existTask'){//存在在途退圈申请不能发起
						alert('存在在途退圈申请，不能发起新的入圈申请！');
					}
				}
			};
		    var handleFailure = function(o){};
		    var callback = {
		    	success:handleSuccess,
		    	failure:handleFailure
		    };
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}

	//退圈
	function doOutBizArea(){
		var paramStr = LmtAgrBizAreaList._obj.getParamStr(['agr_no','agr_status']);
		if (paramStr != null) {
			var agr_status = LmtAgrBizAreaList._obj.getSelectedData()[0].agr_status._getValue();
			if( agr_status != '002' )  {//无效标志
				alert("无效圈商不能申请");
				return ;
			}
			if(!confirm("是否确认要发起退圈申请？")){
				return;
			}
			paramStr += '&app_flag=1';
			var url = '<emp:url action="addLmtAppJoinBackRecord.do"/>?'+paramStr ;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("数据库操作失败!");
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					if(flag == 'suc'){
						var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr+"&serno="+serno+'&process=no&op=update&menuId=joinBackBizArea';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if( flag == 'existTaskUpd' ){//如果已存在待发起任务则跳转修改
						if(confirm("已存在待发起的退圈申请,确认跳转修改?")){
							var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr+'&app_flag=0&serno='+serno+'&process=no&op=update&menuId=joinBackBizArea';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else if(flag == 'existTaskView'){//如果已经存在提交任务则跳转查看
						var url = '<emp:url action="getLmtAppJoinBackViewPage.do"/>?'+paramStr+'&app_flag=0&serno='+serno+'&process=no&op=view&menuId=joinBackBizArea';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == 'existTask'){//存在在途退圈申请不能发起
						alert('存在在途入圈申请，不能发起新的退圈申请！');
					}else{
						alert("圈内无有效客户!");
					}
				}
			};
		    var handleFailure = function(o){};
		    var callback = {
		    	success:handleSuccess,
		    	failure:handleFailure
		    };
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}

	//提交流程   目前直接存储到 入退圈申请表
/*	function doSubm(){
		var paramStr = LmtAgrBizAreaList._obj.getParamStr(['agr_no','agr_status']);
		if (paramStr != null) {
			if(confirm("是否确认要提交？")){
				var url = '<emp:url action="addLmtAppJoinBackRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				asySubmit(url);				
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	function asySubmit(url){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
			  try {
				var jsonstr = eval("("+o.responseText+")");
			  } catch(e) {
				alert("数据库操作失败!");
				return;
			  }
			  var flag = jsonstr.flag;
			  if(flag == 'suc'){
					 alert("提交成功！");
					 return;
				}else {
					alert("不是入圈/退圈申请状态!");
				}
			}
		};
	    var handleFailure = function(o){
	    };
	    var callback = {
	    	success:handleSuccess,
	    	failure:handleFailure
	    };
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}*/
	
	function doOnload(){
		var type = "${context.type}";
		if( 'select' == type ){
			document.getElementById("divBtns1").style.display = "none";
			document.getElementById("divBtns2").style.display = "";
		}else{
			document.getElementById("divBtns1").style.display = "";
			document.getElementById("divBtns2").style.display = "none";
		}
	}
	function doReturnMethod(methodName){
		var data = LmtAgrBizAreaList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			if (methodName) {
				var parentWin = EMPTools.getWindowOpener();
				eval("parentWin."+methodName+"(data[0])");
				window.close();
			}else{
				alert("未定义返回的函数，请检查弹出按钮的设置!");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		var type = "${context.type}";
		if( 'select' == type ){
			var methodName = '${context.popReturnMethod}';	
			doReturnMethod(methodName);
		}
	}
	
	/**add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	function doPrintln(){	
		var paramStr = LmtAgrBizAreaList._obj.getParamStr(['agr_no']);
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
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=11";
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4AB',param);
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=11";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
</script>
</head>
<body class="page_content" onload="doOnload()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAgrBizAreaGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAgrBizArea.agr_no" label="圈商编号" />
			<emp:text id="LmtAgrBizArea.biz_area_name" label="圈商名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left" id="divBtns1" style="display: none">
		<emp:button id="addWiz" label="入圈申请" op="add"/>
		<emp:button id="outBizArea" label="退圈申请" op="remove"/>
		<emp:button id="viewLmtAgrBizArea" label="查看" op="view"/>
		<!-- add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 -->	
		<emp:button id="println" label="封面打印" op="println"/>
	</div>
	
	<div align="left" id="divBtns2" style="display: none">
		<emp:returnButton id="s1" label="选择返回"/>
	</div>

	<emp:table icollName="LmtAgrBizAreaList" pageMode="true" url="pageLmtAgrBizAreaQuery.do" reqParams="process=${context.process}">
		<emp:text id="agr_no" label="圈商编号" />
		<emp:text id="biz_area_name" label="圈商名称" />
		<emp:text id="biz_area_type" label="圈商类型" dictname="STD_LMT_BIZ_AREA_TYPE" />
		<emp:text id="cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="agr_status" label="圈商状态" dictname="STD_ZB_AGR_STATUS"/>
		
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="share_range" label="共享范围" dictname="STD_SHARED_SCOPE" hidden="true"/>
		<emp:text id="belg_org" label="所属机构" hidden="true"/>
		<emp:text id="guar_type" label="授信合作担保方式" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    