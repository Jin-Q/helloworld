<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppJoinBack._toForm(form);
		LmtAppJoinBackList._obj.ajaxQuery(null,form);
	};
	/*--user code begin--*/
	function doAddAppJoinBack(){
		//进入向导页面 选择 退圈/入圈 
		var url = '<emp:url action="getLmtAppJoinBackAddWizPage.do"/>?process=${context.process}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	function doUpdateAppJoinBack(){
		var sel = LmtAppJoinBackList._obj.getSelectedData();
		if (sel != null && sel != "") {
			var status = sel[0].approve_status._getValue();
			if(status == '000'||status == '991'||status == '992'||status == '993'){ //待发起才可以修改 
				var paramStr = LmtAppJoinBackList._obj.getParamStr(['serno','agr_no','app_flag']);
				var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr + '&process=${context.process}&op=update';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("只有状态为【待发起】、【追回】、【打回】的记录可以修改!");
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	function doViewAppJoinBack(){
		var paramStr = LmtAppJoinBackList._obj.getParamStr(['agr_no','serno','app_flag']);
		if (paramStr != null) {			
			var url = '<emp:url action="getLmtAppJoinBackViewPage.do"/>?'+paramStr + '&process=${context.process}&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}

	//提交流程
	function doSubm(){
		var paramStr = LmtAppJoinBackList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = LmtAppJoinBackList._obj.getParamValue(['approve_status']);
	        if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'&&_status!= '993'){
				alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
				return;
			}
	        var agr_no = LmtAppJoinBackList._obj.getParamValue(['agr_no']);//圈商编号
			var cus_name = LmtAppJoinBackList._obj.getParamValue(['agr_no_displayname']);//圈商名称
	        var app_flag = LmtAppJoinBackList._obj.getParamValue(['app_flag']);//申请标识
	        if(app_flag=='0'){//入圈
	        	WfiJoin.appl_type._setValue("3252");
		    }else{//退圈
		    	WfiJoin.appl_type._setValue("3253");
			}
			WfiJoin.table_name._setValue("LmtAppJoinBack");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.cus_id._setValue(agr_no);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("入圈/退圈申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function doDeleteAppJoinBack(){
		var paramStr = LmtAppJoinBackList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var _status = LmtAppJoinBackList._obj.getParamValue(['approve_status']);
	        if(_status!= '000'){
				alert('该申请所处状态不是【待发起】不能删除！');
				return;
			}
			var url = '<emp:url action="deleteLmtAppJoinBackRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			if(!confirm("确认删除?") )return ;
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
				  try {
					var jsonstr = eval("("+o.responseText+")");
				  } catch(e) {
					alert("数据库操作失败!");
					return;
				  }
				  var flag = jsonstr.flag;
				  if(flag == 'success'){
					 alert("删除成功！");
					 window.location.reload();
					}
				}
			};
		    var handleFailure = function(o){};
		    var callback = {
		    	success:handleSuccess,
		    	failure:handleFailure
		    };
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.LmtAppJoinBackGroup.reset();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppJoinBackGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAppJoinBack.serno" label="业务编号" />
		<emp:text id="LmtAppJoinBack.agr_no" label="圈商编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="addAppJoinBack" label="新增" op="add"/>
		<emp:button id="updateAppJoinBack" label="修改" op="update"/>
		<emp:button id="deleteAppJoinBack" label="删除" op="remove"/>
		<emp:button id="viewAppJoinBack" label="查看" op="view"/>
		<emp:button id="subm" label="提交" op="subm"/>
	</div>

	<emp:table icollName="LmtAppJoinBackList" pageMode="true" url="pageLmtAppJoinBackQuery.do?process=${context.process}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="agr_no" label="圈商编号" />
		<emp:text id="agr_no_displayname" label="圈商名称" />
		<emp:text id="app_flag" label="申请标识" dictname="STD_LMT_APP_FLAG"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
		
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    