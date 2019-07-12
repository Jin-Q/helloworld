<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
	.emp_field_longtext_input { /****** 长度固定 ******/
		width: 250px;
		border-width: 1px;
		border-color: #b7b7b7;
		border-style: solid;
		text-align: left;
	}
	
	.emp_field_text_readonly {
			border: 1px solid #b7b7b7;
			background-color:#eee;
			text-align: left;
			width: 600px;
			height: 80px;
		};
</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAgrDetails._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger&showButton=N";
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:1000px';
			window.open(url,'',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		LmtAgrDetails.cus_id._setValue(data.cus_id._getValue());
		LmtAgrDetails.cus_id_displayname._setValue(data.cus_name._getValue());
    };

	function doSelect() {
		doAddOrgno();
	};
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = Array();
			data[0] = document.all.limitCode.value;
			if(data[0]==null||data[0]==''){
				alert('请先选择额度信息！');
				return;
			}
			data[1] = document.all.agrNo.value;
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data)");
			window.close();
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	

	function doAddOrgno(){
		var data = LmtAgrDetailsList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var limitCodes=document.all.limitCode.value;
			var limitCode=data[0].limit_code._getValue();
			var agrNos=document.all.agrNo.value;
			var agrNo=data[0].agr_no._getValue();

			if(limitCodes.indexOf(limitCode)!=-1){
				alert("该授信额度已经选择!");
			}else{
				if(limitCodes==''){
					document.all.limitCode.value=limitCode;
					document.all.agrNo.value=agrNo;
				}else{
					document.all.limitCode.value=limitCodes+","+limitCode;
					document.all.agrNo.value=agrNos+","+agrNo;
				}
			}
		}else{
			alert('请选择一条记录！');
		}
	}

	function doClearOrgno(){
		document.all.limitCode.value='';
		document.all.agrNo.value='';
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAgrDetailsGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="LmtAgrDetails.agr_no" label="授信协议编号" />
		<emp:pop id="LmtAgrDetails.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:text id="LmtAgrDetails.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="true" url="pageLmtAgrDetailsPop.do?condition=${context.condition}&flag=${context.flag}&cus_id=${context.cus_id}&openDay=${context.openDay}">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="limit_name" label="额度品种名称" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:select id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<!-- add by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 begin -->
		<emp:text id="core_corp_cus_id" label="核心企业客户号" hidden="true"/>
		<emp:text id="core_corp_cus_id_displayname" label="核心企业客户名称" />
		<!-- add by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 end -->
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期" />
	</emp:table>
	<div align="center"><br>
	<emp:textarea id="limitCode" label=" "  readonly="true" cssElementClass="emp_field_text_readonly"/>
	<emp:textarea id="agrNo" label=" " hidden="true"/><br>
	<emp:button label="加入" id="addOrgno"/>
	<emp:returnButton id="returnMethod" label="选择返回"/>
	<emp:button label="清空" id="clearOrgno"/>
	<br>
	</div>
</body>
</html>
</emp:page>
    