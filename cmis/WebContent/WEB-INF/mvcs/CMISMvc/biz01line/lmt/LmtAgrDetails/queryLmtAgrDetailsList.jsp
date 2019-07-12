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
		LmtAgrDetails._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAgrDetailsPage() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	//过时
	function doViewLmtAgrDetails1() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var meunId = '${context.menuId}';
			if(meunId == 'crd_ledger4froze'){
				var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger4froze&op=view&main_menuId=${context.menuId}";
			}else{
				var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger&op=view&main_menuId=${context.menuId}";
			}
			
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var meunId = '${context.menuId}';
			if(meunId == 'crd_ledger4froze'){
				var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger4froze&op=view&main_menuId=${context.menuId}";
			}else{
				var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger&op=view&main_menuId=${context.menuId}";
			}
			
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
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
    //一票否决
    function doNotAgreeLmtAgrDetails(){
    	var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			if(confirm("确定要对该笔台账进行一票否决？")){				
				var lmt_status = LmtAgrDetailsList._obj.getParamValue(['lmt_status']);
				if("30"==lmt_status){
					alert("该额度台账状态已经为[终止]，不能再次否决！");
					return false;
				}
				var url = '<emp:url action="disAgreeAgrDetails.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("否决失败!");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							alert("否决成功！");
							window.location.reload();							
						}else if(flag=="exists"){
							alert("该授信项下已发生业务，不能否决！");
						}else{
							alert('否决失败！');
						}
					}
				};
				var handleFailure = function(o){
					alert("更新台账失败，请联系管理员！");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
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
		<emp:select id="LmtAgrDetails.lmt_status" label="额度状态" dictname="STD_LMT_STATUS" />
		<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号" />
		<emp:select id="LmtAgrDetails.sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:select id="LmtAgrDetails.limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:date id="LmtAgrDetails.forze_date" label="批量冻结日期" />
		<emp:text id="LmtAgrDetails.cus_id" label="客户码" hidden="true"/>
		
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" op="view"/>
		<emp:button id="notAgreeLmtAgrDetails" label="一票否决" op="ypfj"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="true" url="pageLmtAgrDetailsQuery.do?subConndition=${context.subConndition}">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="limit_name" label="额度品种名称" hidden="true" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<!-- added by yangzy 2015/05/08 需求：XD141222087，法人透支需求展示额度剩余额度 begin -->
		<emp:text id="bal_amt" label="授信余额" dataType="Currency"/>
		<!-- added by yangzy 2015/05/08 需求：XD141222087，法人透支需求展示额度剩余额度 end -->	
		<!-- add by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 begin -->
		<emp:text id="core_corp_cus_id" label="核心企业客户号" hidden="true"/>
		<emp:text id="core_corp_cus_id_displayname" label="核心企业客户名称" />
		<!-- add by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 end -->	
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    