<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	.emp_field_text_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 240px;
	};
</style>
<script type="text/javascript">
//下一步按钮操作事件
function doNext(){
	var form = document.getElementById('submitForm');
	var result = GrtGuarCont._checkAll();
    if(result){
    	var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var msg = jsonstr.msg;
				var guar_cont_no = jsonstr.guar_cont_no;
				var pk_id = jsonstr.pk_id;
				if("success" == msg){
					var flag ="update";
					var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?pk_id='+pk_id+'&op=update&flag=ybWh&menuIdTab=ybCount&guar_cont_no='+guar_cont_no+'&rel=${context.rel}&serno=${context.serno}&limit_code=${context.limit_code}&isCreditChange=${context.isCreditChange}&cont_no=${context.cont_no}&oper='+flag;
					url = EMPTools.encodeURI(url);
					window.location=url; 
				}else{
					alert("新增担保合同失败！");
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增担保合同失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		GrtGuarCont._toForm(form);
		page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
    	var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action,callback,postData);
    }
}
//返回按钮操作事件
function doClose() {
	window.close();
}

/*客户pop返回事件
function returnCus(data){
	GrtGuarCont.cus_id._setValue(data.cus_id._getValue());
	GrtGuarCont.cus_id_displayname._setValue(data.cus_name._getValue());
	var cus_id = data.cus_id._getValue();
	var url = GrtGuarCont.drfpo_no._obj.config.url;
	var url1 = GrtGuarCont.po_no._obj.config.url;
	var url2 = GrtGuarCont.po_no_bl._obj.config.url;
	var urls = '&cus_id='+cus_id;
	GrtGuarCont.drfpo_no._obj.config.url=url+urls;
	GrtGuarCont.po_no._obj.config.url=url1+urls;
	GrtGuarCont.po_no_bl._obj.config.url=url2+urls;
	GrtGuarCont.cus_id._obj._renderReadonly(true);
}*/

//票据池编号pop返回事件
function returnDrfpo(data){
	GrtGuarCont.drfpo_no._setValue(data.drfpo_no._getValue());
	GrtGuarCont.bill_amt._setValue(data.bill_amt._getValue());
}
//应收账款池编号pop返回事件
function returnPoNo(data){
	GrtGuarCont.po_no._setValue(data.po_no._getValue());
	GrtGuarCont.bill_amt._setValue(data.invc_amt._getValue());
	GrtGuarCont.poType._setValue("1");
}
//保理池编号pop返回事件
function returnPoNoBl(data){
	GrtGuarCont.po_no._setValue(data.po_no._getValue());
	GrtGuarCont.po_no_bl._setValue(data.po_no._getValue());
	GrtGuarCont.bill_amt._setValue(data.invc_amt._getValue());
	GrtGuarCont.poType._setValue("2");
}
function doLoad(){
//	doRemove();
	change();
	checkGuarWay();      
	var rel = '${context.rel}';   //关系类型
	if("sxRel"==rel){   //授信进入
		GrtGuarCont.cus_id._obj._renderReadonly(true); 		  //客户码
		GrtGuarCont.guar_model._obj._renderReadonly(true);    //担保模式
		GrtGuarCont.limit_code._obj._renderReadonly(true);    //授信额度编码
		GrtGuarCont.lmt_grt_flag._obj._renderReadonly(true);  //是否授信项下 
		GrtGuarCont.cus_id._obj.addOneButton('viewCusInfo','查看',viewCusInfo);   //客户新增查看按钮
		GrtGuarCont.rel._setValue("2");//授信进入
	}else if("ywRel"==rel){ //业务进入
		GrtGuarCont.lmt_grt_flag._obj._renderHidden(true);//是否授信项下隐藏
		GrtGuarCont.limit_code._obj._renderHidden(true);//是否授信项下隐藏
		GrtGuarCont.lmt_grt_flag._obj._renderRequired(false);
		GrtGuarCont.limit_code._obj._renderRequired(false);        
		GrtGuarCont.lmt_grt_flag._setValue("2");   //值设置为否 
		GrtGuarCont.rel._setValue("3");//业务进入
	}else{
		GrtGuarCont.lmt_grt_flag._obj._renderReadonly(false);  //是否授信项下 
		GrtGuarCont.rel._setValue("1");//担保进入
	}
	//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 start   
	var options = GrtGuarCont.guar_model._obj.element.options;
	for ( var i = options.length - 1; i >= 0; i--) {
		//判断担保模式，若为"保理池"，去掉选项
		if(options[i].value == "03"){
		options.remove(i);
		}
	}
	//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式 end
/*	var cus_id = GrtGuarCont.cus_id._getValue();
	var url = GrtGuarCont.drfpo_no._obj.config.url;
	var url1 = GrtGuarCont.po_no._obj.config.url;
	var url2 = GrtGuarCont.po_no_bl._obj.config.url;
	var urls = '&cus_id='+cus_id;
	GrtGuarCont.drfpo_no._obj.config.url=url+urls;
	GrtGuarCont.po_no._obj.config.url=url1+urls;
	GrtGuarCont.po_no_bl._obj.config.url=url2+urls;
	GrtGuarCont.cus_id._obj._renderReadonly(true);*/
}
/*去除担保模式字典项中的联保字段。
function doRemove(){
	document.getElementById("GrtGuarCont.guar_model").remove("05");
}*/

//客户信息查看
function viewCusInfo(){
	var url = "<emp:url action='getCusViewPage.do'/>&cusId="+GrtGuarCont.cus_id._getValue();
  	url=encodeURI(url); 
  	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
}

function change(){
	if (GrtGuarCont.lmt_grt_flag._obj.element.value == "1") {
		if(GrtGuarCont.guar_model._obj.element.value=="05"){//此时为联保时授信项下，只需要选择联保协议编号
			GrtGuarCont.limit_code._obj._renderRequired(false);
			GrtGuarCont.limit_code._obj._renderReadonly(false);
			GrtGuarCont.limit_code._setValue("");
			GrtGuarCont.limit_code._obj._renderHidden(true);
			GrtGuarCont.agr_no._obj._renderRequired(true);
			GrtGuarCont.agr_no._obj._renderHidden(false);
		}else{
			GrtGuarCont.limit_code._obj._renderRequired(true);
			GrtGuarCont.limit_code._obj._renderHidden(false);
			GrtGuarCont.agr_no._obj._renderRequired(false);
			GrtGuarCont.agr_no._setValue("");
			GrtGuarCont.agr_no._obj._renderHidden(true);
		}
	}else{
		GrtGuarCont.limit_code._obj._renderRequired(false);
		GrtGuarCont.limit_code._obj._renderHidden(true);
		GrtGuarCont.limit_code._setValue("");
		GrtGuarCont.agr_no._obj._renderRequired(false);
		GrtGuarCont.agr_no._obj._renderHidden(true);
		GrtGuarCont.agr_no._setValue("");
	}
}

//动态更改担保方式的字典项
function dataDic(){
	//清空担保方式下拉选项
	document.getElementById('GrtGuarCont.guar_way').innerHTML="";
    var itms1 = document.getElementById('GrtGuarCont.guar_way').options;//更改担保方式的字典项
	var op1 = document.createElement("OPTION");
	var op3 = document.createElement("OPTION");
	document.getElementById('GrtGuarCont.guar_way').options.add(op1);
	document.getElementById('GrtGuarCont.guar_way').options.add(op3);
	op1.innerText="-----请选择-----";
	op3.innerText="质押";
	op1.value="";
	op3.value="01";
}
//非池类型担保模式时，池相关字段全部都隐藏
function noPo(){
	GrtGuarCont.drfpo_no._obj._renderHidden(true);
	GrtGuarCont.drfpo_no._obj._renderRequired(false);
	GrtGuarCont.drfpo_no._setValue("");
	GrtGuarCont.po_no._obj._renderRequired(false);
	GrtGuarCont.po_no._obj._renderHidden(true);
	GrtGuarCont.po_no._setValue("");
	GrtGuarCont.po_no_bl._obj._renderRequired(false);
	GrtGuarCont.po_no_bl._obj._renderHidden(true);
}
//根据担保模式的不同过滤字典项
function checkGuarWay(){
	var flag = GrtGuarCont.guar_model._getValue();
	//担保模式为票据池，应收账款池，或保理池时
	 if("01"==flag){//票据池时
		GrtGuarCont.drfpo_no._obj._renderHidden(false);
		GrtGuarCont.drfpo_no._obj._renderRequired(true);
		GrtGuarCont.drfpo_no._setValue("");
		GrtGuarCont.po_no._obj._renderHidden(true);
		GrtGuarCont.po_no._obj._renderRequired(false);
		GrtGuarCont.po_no._setValue("");
		GrtGuarCont.po_no_bl._obj._renderHidden(true);
		GrtGuarCont.po_no_bl._obj._renderRequired(false);
		GrtGuarCont.po_no_bl._setValue("");
		GrtGuarCont.agr_no._obj._renderHidden(true);
		GrtGuarCont.agr_no._obj._renderRequired(false);
		GrtGuarCont.agr_no._setValue("");
		dataDic();
	}else if("02"==flag){//应收账款池
		GrtGuarCont.drfpo_no._obj._renderHidden(true);
		GrtGuarCont.drfpo_no._obj._renderRequired(false);
		GrtGuarCont.drfpo_no._setValue("");
		GrtGuarCont.po_no._obj._renderHidden(false);
		GrtGuarCont.po_no._obj._renderRequired(true);
		GrtGuarCont.po_no._setValue("");
		GrtGuarCont.po_no_bl._obj._renderHidden(true);
		GrtGuarCont.po_no_bl._obj._renderRequired(false);
		GrtGuarCont.po_no_bl._setValue("");
		GrtGuarCont.agr_no._obj._renderHidden(true);
		GrtGuarCont.agr_no._obj._renderRequired(false);
		GrtGuarCont.agr_no._setValue("");
		dataDic();
	}else if("03"==flag){//保理池
		GrtGuarCont.drfpo_no._obj._renderHidden(true);
		GrtGuarCont.drfpo_no._obj._renderRequired(false);
		GrtGuarCont.drfpo_no._setValue("");
		GrtGuarCont.po_no._obj._renderHidden(true);
		GrtGuarCont.po_no._obj._renderRequired(false);
		GrtGuarCont.po_no._setValue("");
		GrtGuarCont.po_no_bl._obj._renderHidden(false);
		GrtGuarCont.po_no_bl._obj._renderRequired(true);
		GrtGuarCont.po_no_bl._setValue("");
		dataDic();
		GrtGuarCont.agr_no._obj._renderHidden(true);
		GrtGuarCont.agr_no._obj._renderRequired(false);
		GrtGuarCont.agr_no._setValue("");
	}else if("00"==flag){   //普通
	//	GrtGuarCont.limit_code._obj._renderHidden(true);
		GrtGuarCont.agr_no._obj._renderHidden(true);
		noPo();
		changeOptions();
	}else if(""==flag){
		noPo();
		GrtGuarCont.agr_no._obj._renderHidden(true);
		GrtGuarCont.agr_no._obj._renderRequired(false);
		GrtGuarCont.agr_no._setValue("");
		changeOptions();
	}
}

function changeOptions(){
	//清空担保方式下拉选项
	document.getElementById('GrtGuarCont.guar_way').innerHTML="";
    var itms1 = document.getElementById('GrtGuarCont.guar_way').options;//更改担保方式的字典项
	var op1 = document.createElement("OPTION");
	var op2 = document.createElement("OPTION");
	var op3 = document.createElement("OPTION");
	var op4 = document.createElement("OPTION");
	var op5 = document.createElement("OPTION");
	var op6 = document.createElement("OPTION");
	document.getElementById('GrtGuarCont.guar_way').options.add(op1);
	document.getElementById('GrtGuarCont.guar_way').options.add(op2);
	document.getElementById('GrtGuarCont.guar_way').options.add(op3);
	document.getElementById('GrtGuarCont.guar_way').options.add(op4);
	document.getElementById('GrtGuarCont.guar_way').options.add(op5);
	document.getElementById('GrtGuarCont.guar_way').options.add(op6);
	op1.innerText="-----请选择-----";
	op2.innerText="抵押";
	op3.innerText="质押";
	op4.innerText="保证-单人担保";
	op5.innerText="保证-多人分保";
	op6.innerText="保证-多人联保";
	op1.value="";
	op2.value="00";
	op3.value="01";
	op4.value="02";
	op5.value="03";
	op6.value="04";
}
function returnLmtNo(data){
	GrtGuarCont.limit_code._setValue(data.agr_no._getValue());
}
function returnAgrNo(data){//担保协议编号只是一个辅助字段，真正存到数据库里的数据是授信额度编号
	GrtGuarCont.agr_no._setValue(data.agr_no._getValue());
	GrtGuarCont.limit_code._setValue(data.agr_no._getValue());
}
</script>
</head>
<body class="page_content" onload="doLoad()">   
	
	<emp:form id="submitForm" action="addGrtGuarContRecord.do?flag=ybWh&op=update&menuId=ybCount&rel=${context.rel}&serno=${context.serno}&limit_code=${context.limit_code}&isCreditChange=${context.isCreditChange}&cont_no=${context.cont_no}" method="POST">
		    
		<emp:gridLayout id="GrtGuarContGroup" title="一般担保合同新增页面" maxColumn="2">
			<emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号" maxlength="40" required="false" hidden="true"/>    
			<emp:pop id="GrtGuarCont.cus_id" label="借款人客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="false" buttonLabel="选择" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarCont.cus_id_displayname" label="借款人客户名称" required="false" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2" hidden="true"/>
		    <emp:select id="GrtGuarCont.guar_cont_type" label="担保合同类型" required="false" dictname="STD_GUAR_CONT_TYPE" defvalue="00" readonly="true" colSpan="2"/>
			<emp:select id="GrtGuarCont.guar_model" label="担保模式" required="true" dictname="STD_GUAR_MODEL" onchange="checkGuarWay()" defvalue="00"/>
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" required="true" dictname="STD_GUAR_TYPE" />
			<emp:pop id="GrtGuarCont.drfpo_no" label="票据池编号" url="getIqpDrfpoManaPopList.do?returnMethod=returnDrfpo" required="true" buttonLabel="引入" colSpan="2"/>
			<emp:pop id="GrtGuarCont.po_no" label="应收账款池编号" url="queryIqpActrecpoManaPopList.do?PO_TYPE=1&returnMethod=returnPoNo" required="true" buttonLabel="引入" colSpan="2"/>
			<emp:pop id="GrtGuarCont.po_no_bl" label="保理池编号" url="queryIqpActrecpoManaPopList.do?PO_TYPE=2&returnMethod=returnPoNoBl" required="true" buttonLabel="引入" colSpan="2"/>
			<emp:text id="GrtGuarCont.bill_amt" label="在池票据总额" maxlength="18" required="false" hidden="true" dataType="Currency" colSpan="2"/>
			<emp:select id="GrtGuarCont.lmt_grt_flag" label="是否授信项下" required="true" dictname="STD_ZX_YES_NO" defvalue="1" onchange="change()" readonly="false"/>
			<emp:pop id="GrtGuarCont.limit_code" label="授信额度编号" url="queryLmtAgrDetailsPop.do?flag=3&returnMethod=returnLmtNo" required="false" buttonLabel="选择"/>
			<emp:pop id="GrtGuarCont.agr_no" label="联保协议编号" url="queryLmtAgrJointPop.do?returnMethod=returnAgrNo&flag=1" required="false" buttonLabel="选择"/>
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" required="false" dictname="STD_CONT_STATUS" defvalue="00" hidden="true"/>
			<emp:text id="GrtGuarCont.poType" label="标志位（1--应收账款，2--保理池）" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.rel" label="申请类型" maxlength="40" required="false" hidden="true" colSpan="2"/>
			
			<emp:select id="GrtGuarCont.is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO" required="true" />
			<emp:select id="GrtGuarCont.is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO" required="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="下一步"/>   
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

