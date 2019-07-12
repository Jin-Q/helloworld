<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
<head>
<jsp:include page="/include.jsp" flush="true" />
<script type="text/javascript">
	function doSave() {
		var idx = CfgRiskClassList._obj.recordCount; //得到记录行号
		if (idx >= 1) {
			for (var i = 0; i < idx; i++) {
				var CfgRiskClass = CfgRiskClassList._obj.data[i];
				if (CfgRiskClass._checkAll()) {
					var form = "ovdue_days="
							+ CfgRiskClass.ovdue_days._getValue() + "&"
							+ "grt100=" + CfgRiskClass.grt100._getValue() + "&"
							+ "grt200=" + CfgRiskClass.grt200._getValue() + "&"
							+ "grt300=" + CfgRiskClass.grt300._getValue() + "&"
							+ "grt400=" + CfgRiskClass.grt400._getValue()
					var handleSuccess = function(o) {
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								if (i >= idx - 1) {
									alert("Parse jsonstr1 define error!"
											+ e.message);
									return;
								}
							}
							var flag = jsonstr.flag;
							if (flag == "success") {
								if (i >= idx - 1) {
									alert("保存成功!");
									window.location.reload();
								}
							} else {
								if (i >= idx - 1) {
									alert("保存异常!");
								}
							}
						}
					};
					var handleFailure = function(o) {
						alert("异步请求出错！");
					};
					var callback = {
						success : handleSuccess,
						failure : handleFailure
					};
					//var postData = YAHOO.util.Connect.setForm(form);
					var url = '<emp:url action ="updateCfgRiskClassRecord.do"/>?'
							+ form;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,
							callback, null)
				}
			}
		}
	};
</script>
</head>
<body>
	<emp:tabGroup id="CfgRiskClassLst_tabs" mainTab="base_tab">
		<emp:tab id="base_tab" label="风险分类配置" needFlush="true" initial="true">
			<emp:button id="save" label="保存" />
			<form id="submitForm" action="#" method="POST">
				<emp:table icollName="CfgRiskClassList"
					url="queryCfgRiskClassList.do" pageMode="true" editable="true"
					needTableTitle="true" selectType="1">
					<emp:select id="ovdue_days" label="担保方式\\\本金逾期天数"
						dictname="STD_ZB_OVDUE_DAYS" readonly="true"></emp:select>
					<emp:select id="grt100" label="抵押" dictname="STD_ZB_NINE_SORT"></emp:select>
					<emp:select id="grt200" label="质押" dictname="STD_ZB_NINE_SORT"></emp:select>
					<emp:select id="grt300" label="保证" dictname="STD_ZB_NINE_SORT"></emp:select>
					<emp:select id="grt400" label="信用" dictname="STD_ZB_NINE_SORT"></emp:select>
				</emp:table>
			</form>
		</emp:tab>
	</emp:tabGroup>
</body>
	</html>
</emp:page>
