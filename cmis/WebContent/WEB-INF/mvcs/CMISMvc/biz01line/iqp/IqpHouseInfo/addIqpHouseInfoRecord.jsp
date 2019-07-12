<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function changeStructure(){
		var structure = IqpHouseInfo.building_structure_cd._getValue();
		if(structure=="04"){
			IqpHouseInfo.durable_years._setValue("50");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}
		else if(structure=="02"){
			IqpHouseInfo.durable_years._setValue("60");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}
		else if(structure=="01"){
			IqpHouseInfo.durable_years._setValue("70");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}
		else if(structure=="09"){
			IqpHouseInfo.durable_years._setValue("40");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}else if(structure=="05"){
			IqpHouseInfo.durable_years._obj._renderHidden(true);
			IqpHouseInfo.durable_years._setValue("");
			IqpHouseInfo.durable_years1._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._setValue("");
		}
	}
	function doSetValue(){
		IqpHouseInfo.durable_years._setValue(IqpHouseInfo.durable_years1._getValue());
	}
	function doLoad(){
		var structure = IqpHouseInfo.building_structure_cd._getValue();
		if(structure=="05"){
			IqpHouseInfo.durable_years._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._setValue(MortHotelOffice.durable_years._getValue());
		}
	}
	
	 //校验 首付款比例在0%-100%之间
    function checkFistPerc(value){
        var fst_perc = value;
        if(fst_perc!=null&&fst_perc!=''){
        	if(fst_perc<0 || fst_perc>100){
                alert("首付款比例在0%-100%之间");
                IqpHouseInfo.fst_pyr_perc._setValue("");
             } 
        }
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addIqpHouseInfoRecord.do" method="POST">
		
		<emp:gridLayout id="IqpHouseInfoGroup" title="厂房信息" maxColumn="2">
			<emp:text id="IqpHouseInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpHouseInfo.house_addr" label="厂房位置" maxlength="100" required="false" />
			<emp:text id="IqpHouseInfo.pur_amt" label="购买金额" maxlength="16" required="false" dataType="Currency" />
			<emp:date id="IqpHouseInfo.pur_time" label="购买时间" required="false" />
			<emp:text id="IqpHouseInfo.fst_pyr_perc" label="首付款比例"  onchange="checkFistPerc(value)" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="IqpHouseInfo.house_squ" label="建筑面积" maxlength="10" required="false" dataType="Double" />
			<emp:text id="IqpHouseInfo.occup_squ" label="占地面积" maxlength="10" required="false" dataType="Double" />
			<emp:select id="IqpHouseInfo.building_structure_cd" label="建筑结构" required="false" dictname="STD_ARCH_STR" onchange="changeStructure()"/>
			<emp:select id="IqpHouseInfo.fitment_degree" label="装修程度" required="false" dictname="STD_FITMENT_DEGREE" />
			<emp:date id="IqpHouseInfo.house_build_year" label="房屋建成年份" required="false" />
			<emp:text id="IqpHouseInfo.durable_years" label="耐用年限" maxlength="10" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpHouseInfo.durable_years1" label="耐用年限" maxlength="10" required="false" dataType="Int" hidden="true" onblur="doSetValue()"/>
			<emp:select id="IqpHouseInfo.street_situation" label="临街状况" required="false" dictname="STD_FRONTAGE_STATUS" />
			<emp:select id="IqpHouseInfo.use_status" label="使用状态" required="false" dictname="STD_USE_STATUS" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

