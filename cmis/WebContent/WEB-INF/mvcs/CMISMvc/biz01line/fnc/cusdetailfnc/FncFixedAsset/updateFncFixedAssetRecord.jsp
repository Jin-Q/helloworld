<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryFncFixedAssetList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateFncFixedAssetRecord.do" method="POST">
		<emp:gridLayout id="FncFixedAssetGroup" maxColumn="2" title="主要固定资产明细表">
			<emp:pop id="FncFixedAsset.cus_id" label="客户码" url="selectCusInfoPop.do?type=com" returnMethod="returnCus" readonly="true" required="true" />
			<emp:text id="FncFixedAsset.cus_name" label="客户姓名" maxlength="80" required="true" readonly="true"/>
					<emp:text id="FncFixedAsset.fnc_ym" label="年月" maxlength="6" required="true" onchange="checkZero(this.value)"/>
			
			<emp:text id="FncFixedAsset.fnc_asset_name" label="名称" maxlength="60" required="true" />
			<emp:text id="FncFixedAsset.fnc_asset_amt" label="数量" maxlength="38" required="false" />
			<emp:text id="FncFixedAsset.fnc_asset_place" label="位置"  required="true"  />
			<emp:text id="FncFixedAsset.fnc_asset_wrr_id" label="权利证书" maxlength="60" required="false" />
						<emp:text id="FncFixedAsset.fnc_asset_unit" label="单位" maxlength="2" required="false" />
			<emp:text id="FncFixedAsset.fnc_asset_net_val" label="净值" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="FncFixedAsset.fnc_asset_ori_val" label="原值" maxlength="18" required="false" dataType="Currency"/>
		    <emp:select id="FncFixedAsset.bb_type" label="报表周期类型" dictname="STD_ZB_FNC_STAT" required="false" />
			<emp:select id="FncFixedAsset.fnc_asset_pld_desc" label="抵质押情况" dictname="STD_ZB_GUAR_ST" required="false" />
				<emp:select id="FncFixedAsset.fnc_asset_obt_mth" label="占用方式" dictname="STD_ZB_ASSETOM" required="false" />

			<emp:textarea id="FncFixedAsset.remark" label="备注" maxlength="100" required="false" colSpan="2" />
			
		    	<emp:text id="FncFixedAsset.input_id_displayname" label="登记人"  required="false" defvalue="$actorname" readonly="true" />
			
			<emp:text id="FncFixedAsset.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="FncFixedAsset.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
				<!-- 下面是真实值字段 -->	
			<emp:text id="FncFixedAsset.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="FncFixedAsset.last_upd_date" label="更新日期" maxlength="10" required="false" hidden="true"/>
			
				<emp:text id="FncFixedAsset.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="FncFixedAsset.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" hidden="true"/>
			<emp:text id="FncFixedAsset.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
				<emp:text id="FncFixedAsset.pk_id" label="主键" maxlength="32" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
