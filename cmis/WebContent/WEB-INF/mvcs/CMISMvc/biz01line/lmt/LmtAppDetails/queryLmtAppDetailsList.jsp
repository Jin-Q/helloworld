<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
<% 
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = context.getDataValue("app_type").toString();
%>

	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppDetails._toForm(form);
		LmtAppDetailsList._obj.ajaxQuery(null,form);
	};

	//分项修改
	function doGetUpdateLmtAppDetailsPage() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code','sub_type']);
		if (paramStr != null) {
			//判断分项是否可修改
			if(!checkLmtDetailsPurview()){
				return false;
			}
			
			var serno = '${context.serno}';
			//var url = '<emp:url action="getLmtAppDetailsUpdatePage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=update&menuId=LmtAppDetails&grtOp=update&lrisk_type=${context.lrisk_type}&lmt_type=${context.lmt_type}';
			var url = '<emp:url action="getLmtAppDetailsUpdatePage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=update&menuId=LmtAppDetails&grtOp=update&lrisk_type=${context.lrisk_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//检查授信分项是否可以做删除或修改
	function checkLmtDetailsPurview(){
		//var lmt_type = LmtAppDetailsList._obj.getParamValue(['lmt_type']);
		//var lmt_type_app = '${context.lmt_type}';
		//if(lmt_type != lmt_type_app){
		//	alert("选择分项的授信类别与申请授信类别不一致，不能做[删除]或[修改]！");
		//	return false;
		//}
		var lmt_type = LmtAppDetailsList._obj.getParamValue(['lmt_type']);
		var update_flag = LmtAppDetailsList._obj.getParamValue(['update_flag']);
		if(lmt_type=='02'&&update_flag=='02'){//一次性额度不能做变更
			alert("一次性额度不能变更！");
			return false;
		}
		var froze_amt = LmtAppDetailsList._obj.getParamValue(['froze_amt']);
		if(froze_amt>0){
			alert("该授信分项已做冻结处理，不能做[删除]或[修改]！");
			return false;
		}
		return true; 
	}  
	
	//调整对比
	function doAdjustDistinction(){
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null){
			var update_flag = LmtAppDetailsList._obj.getSelectedData()[0].update_flag._getValue();
			var serno =  '${context.serno}';
			if(update_flag == "02"){
				//var url = '<emp:url action="getAdjustDistinctionPage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}&lmt_type=${context.lmt_type}';
				var url = '<emp:url action="getAdjustDistinctionPage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("请选择变更类型为【变更】的数据！");
			}
		}else{
			alert("请先选择一条记录");
		}
	}

	//查看
	function doViewLmtAppDetails() {
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code','sub_type']);
		var serno =  '${context.serno}';
		if (paramStr != null) {
			//var url = '<emp:url action="getLmtAppDetailsViewPage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&ogrMenuId=${context.menuId}&menuId=LmtAppDetails&grtOp=view&lrisk_type=${context.lrisk_type}&lmt_type=${context.lmt_type}';
			var url = '<emp:url action="getLmtAppDetailsViewPage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&ogrMenuId=${context.menuId}&menuId=LmtAppDetails&grtOp=view&lrisk_type=${context.lrisk_type}';
			url = EMPTools.encodeURI(url);
 			window.location = url; 
		} else {
			alert('请先选择一条记录！'); 
		}
	};
	
	//删除
	function doDeleteLmtAppDetails(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("删除成功！");
					window.location.reload();
					//window.parent.location.reload();
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		//var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code','serno','org_limit_code','lmt_type','lrisk_type']);
		var paramStr = LmtAppDetailsList._obj.getParamStr(['limit_code','serno','org_limit_code','lrisk_type']);
		if (paramStr != null) {
			var update_flag = LmtAppDetailsList._obj.getParamValue(['update_flag']);
			if("02"==update_flag){
				alert("变更分项信息不允许删除！");
				return false;
			}
			//判断分项是否可删除
			if(!checkLmtDetailsPurview()){
				return false;
			}
			if(confirm("是否确认要删除此分项信息？将关联删除项下新建的担保合同。")){  
				var url = '<emp:url action="deleteLmtAppDetailsRecord.do"/>&'+paramStr+"&BelgLine=BL100";
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	//查看台账
	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			//var url = '<emp:url action="getLmtAppDetailsHisViewPage.do"/>?'+paramStr+"&type=app&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}&lmt_type=${context.lmt_type}";
			var url = '<emp:url action="getLmtAppDetailsHisViewPage.do"/>?'+paramStr+"&type=app&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//对台账做变更
	function doGetUpdateLmtAgrDetailsPage() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		var serno =  '${context.serno}';
		if (paramStr != null) {
			//var url = '<emp:url action="getLmtAgrDetailsUpdatePage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}&lmt_type=${context.lmt_type}';
			var url = '<emp:url action="getLmtAgrDetailsUpdatePage.do"/>?'+paramStr+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//新增额度分项
	function doGetAddLmtAppDetailsPage() {
		var serno = '${context.serno}';
		var cus_id = '${context.cus_id}';
		var lrisk_type = '${context.lrisk_type}';
		//var url = '<emp:url action="getLmtAppPage.do"/>&serno='+serno+'&cus_id='+cus_id+'&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type='+lrisk_type+'&lmt_type=${context.lmt_type}';
		var url = '<emp:url action="getLmtAppPage.do"/>&serno='+serno+'&cus_id='+cus_id+'&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type='+lrisk_type;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>

	<div class='emp_gridlayout_title'>原有额度分项&nbsp;</div>
	<div align="left">
		<!-- <emp:actButton id="getUpdateLmtAgrDetailsPage" label="修改" op="update"/>  -->
		<emp:actButton id="viewLmtAgrDetails" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="false" url="">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="授信额度编号"/>
		<emp:text id="limit_name" label="额度品种名称" hidden="true" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="lmt_type" label="授信类别" dictname="STD_ZX_LMT_PRD" hidden="true"/>
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" hidden="true" />
		<emp:text id="term" label="授信期限" hidden="true" />		
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	<br>
	<div class='emp_gridlayout_title'>${context.div_name}&nbsp;</div>
	<div align="left">
		<emp:actButton id="getAddLmtAppDetailsPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtAppDetailsPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtAppDetails" label="删除" op="remove"/>
		<emp:actButton id="viewLmtAppDetails" label="查看" op="view"/>
		<emp:actButton id="adjustDistinction" label="调整对比" op="update"/>
	</div>

	<emp:table icollName="LmtAppDetailsList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="limit_code" label="授信额度编号" hidden="true"/>
		<emp:text id="org_limit_code" label="授信额度编号"/>
		<emp:text id="limit_name" label="额度品种名称" hidden="true" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="lmt_type" label="授信类别" dictname="STD_ZX_LMT_PRD" hidden="true"/>
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="term" label="授信期限" />
		<emp:text id="update_flag" label="变更类型" dictname="STD_ZB_APP_TYPE"/>
		<emp:text id="froze_amt" label="冻结金额" dataType="Currency" hidden="true"/>
		<emp:text id="lrisk_type" label="低风险类型"  hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    