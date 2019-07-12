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
		LmtIndusAgr._toForm(form);
		LmtIndusAgrList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtIndusAgrPage() {
		var paramStr = LmtIndusAgrList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var is_list_mana = LmtIndusAgrList._obj.getParamValue('is_list_mana');
			if(is_list_mana != '2'){
			    alert("只能操作不是“名单制管理”的记录！");
			    return ;
			}			
			paramStr = paramStr + "&op=mana";
			var url = '<emp:url action="getLmtIndusAgrViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtIndusAgr() {
		var paramStr = LmtIndusAgrList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			paramStr = paramStr + "&op=view";
			var url = '<emp:url action="getLmtIndusAgrViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doSubmitRecord(url){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='1'){
		            window.location.reload();
				}else if(operMsg=='2'){
					alert('操作失败!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("操作失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	};
	
	function doReset(){
		page.dataGroups.LmtIndusAgrGroup.reset();
	};
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	/**add by yangzy 2015-6-17 需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = LmtIndusAgrList._obj.getParamStr(['agr_no']);
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
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=10";
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=10";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by yangzy 2015-6-17 需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIndusAgrGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtIndusAgr.serno" label="业务编号" />
			<emp:text id="LmtIndusAgr.agr_no" label="协议编号" />
			<emp:select id="LmtIndusAgr.indus_type" label="行业分类" dictname="STD_ZB_INDUS_TYPE" />
			<emp:select id="LmtIndusAgr.agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
				<emp:button id="viewLmtIndusAgr" label="查看" op="view"/>
		<emp:button id="getUpdateLmtIndusAgrPage" label="名单管理" op="mana"/>
		<!-- add by yangzy 2015-6-17 需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  -->
		<emp:button id="println" label="封面打印" op="println"/>
	</div>

	<emp:table icollName="LmtIndusAgrList" pageMode="true" url="pageLmtIndusAgrQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="indus_type" label="行业分类" dictname="STD_ZB_INDUS_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="indus_amt" label="行业总额"  dataType="Currency" />
		<emp:text id="single_amt" label="单户限额"  dataType="Currency" />
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true" />
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true" />
		<emp:text id="agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS" />
		<emp:text id="is_list_mana" label="是否名单制管理" dictname="STD_ZX_YES_NO"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    