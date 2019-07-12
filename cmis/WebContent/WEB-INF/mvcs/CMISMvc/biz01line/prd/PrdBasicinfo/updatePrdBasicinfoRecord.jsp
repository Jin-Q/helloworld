<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea1 { /****** 长度、高度固定 ******/
	width: 600px;
	height: 100px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

.emp_input{
	width: 600px;
	height: 50px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>



<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad(){
		PrdBasicinfo.contmapping._obj.addTheButton("contmapping","配置映射",getContMap);
		PrdBasicinfo.pvpmapping._obj.addTheButton("pvpmapping","配置映射",getpPvpMap);
		PrdBasicinfo.loanform._obj.addOneButton("loanform","选择",getAppForm);
		PrdBasicinfo.contform._obj.addOneButton("contform","选择",getContForm);
		PrdBasicinfo.pvpform._obj.addOneButton("pvpform","选择",getPvpForm);
		PrdBasicinfo.preventtactics._obj.addOneButton("preventtactics","选择",getPreventForm);
		getPrdRepayModeList4Prd();
	};

	function getPreventForm(){
		var url = '<emp:url action="getPreventTacticsPop.do"/>&returnMethod=getPreventTactics';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function getContMap(){
		var loanform = PrdBasicinfo.loanform._getValue();
		var contform = PrdBasicinfo.contform._getValue();
		var contmapping = PrdBasicinfo.contmapping._getValue();
		var prdid = PrdBasicinfo.prdid._getValue();
		
		if(loanform == null || loanform == ""){
			alert("请先选择申请表单！");
			return false;
		}
		if(contform == null || contform == ""){
			alert("请先选择合同表单！");
			return false;
		}
		//var url = '<emp:url action="prdCtrMapPop.do"/>&returnMethod=getCtrMapXml&loanform='+loanform+'&contform='+contform+'&contmapping='+contmapping+'&prdid='+prdid;
		var url = '<emp:url action="prdCtrMapPop.do"/>&returnMethod=getCtrMapXml&loanform='+loanform+'&contform='+contform+'&prdid='+prdid;
		
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function getpPvpMap(){
		var loanform = PrdBasicinfo.loanform._getValue();
		var pvpform = PrdBasicinfo.pvpform._getValue();
		var prdid = PrdBasicinfo.prdid._getValue();
		if(loanform == null || loanform == ""){
			alert("请先选择申请表单！");
			return false;
		}
		if(pvpform == null || pvpform == ""){
			alert("请先选择出账表单！");
			return false;
		}
		var url = '<emp:url action="prdPvpMapPop.do"/>&returnMethod=getPvpMapXml&loanform='+loanform+'&pvpform='+pvpform+'&prdid='+prdid;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};

	//获取产品配置拦截策略
	function getPreventTactics(data){
		PrdBasicinfo.preventtactics._setValue(data.prevent_id._getValue());
	}
	//获取合同映射配置页面
	function getCtrMapXml(data){
		PrdBasicinfo.contmapping._setValue(data);
	}
	//获取出账映射配置页面
	function getPvpMapXml(data){
		PrdBasicinfo.pvpmapping._setValue(data);
	}
	
	/*--异步保存修改--*/
	function doSaveUpdate(){
		var prdid = PrdBasicinfo.prdid._getValue();
		var form = document.getElementById('submitForm');
		var result = PrdBasicinfo._checkAll();
		if(!result){
			return;
		}
		PrdBasicinfo._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var schemeid = jsonstr.schemeid;
				if(flag == "success"){
					alert("修改成功！");
					var url = '<emp:url action="getPrdBasicinfoUpdatePage.do"/>?prdid='+prdid;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("修改失败！");
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

		var url = '<emp:url action="updatePrdBasicinfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};	

	/*--申请表单pop--*/
	function getAppForm(){
		var url = '<emp:url action="getAllFormForSelectPop.do"/>&returnMethod=getAppFormPop&flag=Iqp';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	/*--合同表单pop--*/
	function getContForm(){
		var url = '<emp:url action="getAllFormForSelectPop.do"/>&returnMethod=getContFormPop&flag=Ctr';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	/*--出账表单pop--*/
	function getPvpForm(){
		var url = '<emp:url action="getAllFormForSelectPop.do"/>&returnMethod=getPvpFormPop&flag=Pvp';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	function getAppFormPop(data){
		PrdBasicinfo.loanform._setValue(data.modelId._getValue());
		PrdBasicinfo.contmapping._setValue("");
		PrdBasicinfo.pvpmapping._setValue("");
	};
	function getContFormPop(data){
		PrdBasicinfo.contform._setValue(data.modelId._getValue());
		PrdBasicinfo.contmapping._setValue("");
	};
	function getPvpFormPop(data){
		PrdBasicinfo.pvpform._setValue(data.modelId._getValue());
		PrdBasicinfo.pvpmapping._setValue("");
	};

	function returnMethod(data){
		PrdBasicinfo.policytactics._setValue(data.schemeid._getValue());
		
	};
	function doBack(){
		var url = '<emp:url action="queryPrdBasicinfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function getPrdRepayModeList4Prd(){
		var prdid = PrdBasicinfo.prdid._getValue();
		if(prdid==""){
			return false;
		}
		var url = '<emp:url action="getPrdRepayModeList4Prd.do"/>?prdid='+prdid;
		url = EMPTools.encodeURI(url);
		PLoanTypMtdList._obj.ajaxQuery(url,null);
	};
	function queryList(){
		var form = document.getElementById('queryForm');
		a._toForm(form);
		PLoanTypMtdList._obj.ajaxQuery(null,form);
	};
	//还款方式新增
	function doAddPrdRepayMode(){
		var prdid = PrdBasicinfo.prdid._getValue();
		var url = '<emp:url action="queryPrdRepayModeListPop.do"/>?prdid='+prdid;
        url = EMPTools.encodeURI(url);
		var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		modifyWindow = window.open(url,'newWindow',param);
	};
	//还款方式删除
	function doDelPrdRepayMode(){
		var prdid = PrdBasicinfo.prdid._getValue();
		var data = PLoanTypMtdList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择删除还款方式项！");
			return false;
		}else {
			if(confirm("是否确认要移除该还款方式项!")){
				var repay_mode_id = PLoanTypMtdList._obj.getParamStr(['repay_mode_id']);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							var form = document.getElementById('queryForm');
							PLoanTypMtdList._toForm(form);
							PLoanTypMtdList._obj.ajaxQuery(null,form);
						}else {
							alert("删除失败!");
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
				var url="<emp:url action='delPrdRepayModeRel.do'/>?prdid="+prdid+"&"+repay_mode_id;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		}
	};

	function checkDate(){
		var startdate = PrdBasicinfo.startdate._getValue();
		var enddate = PrdBasicinfo.enddate._getValue();
		if(startdate!=null&&startdate!=''&&enddate!=null&&enddate!=''){
			if(startdate>enddate){
				alert('生效日期不能大于到期日期！');
				PrdBasicinfo.enddate._setValue('');
			}
		}
	};
	function returnMsg(data){
		PrdBasicinfo.supcatalog._setValue(data.id);
		PrdBasicinfo.supcatalog_displayname._setValue(data.label);
	
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()" >
	<emp:form id="submitForm" action="updatePrdBasicinfoRecord.do" method="POST">
		<emp:tabGroup mainTab="base_tab" id="mainTab" >
			<emp:tab label="产品基本信息" id="base_tab" needFlush="true" initial="true" >
				<emp:gridLayout id="" maxColumn="2" title="产品基本信息">
					<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="40" required="false" readonly="true" />
					<emp:text id="PrdBasicinfo.prdid" label="产品编号" maxlength="6" required="true" readonly="true" />
					<emp:pop id="PrdBasicinfo.supcatalog_displayname" label="上级目录" url="showDicTree_PRD.do" returnMethod="returnMsg"  required="true" buttonLabel="选择"/>
					<emp:text id="PrdBasicinfo.prdmanager" label="产品经理" maxlength="30" required="false" readonly="true" />
					<emp:select id="PrdBasicinfo.prdstatus" label="产品状态" dictname="STD_PRD_STATE" defvalue="0" readonly="false" />
					<emp:date id="PrdBasicinfo.startdate" label="生效日期" required="true" onblur="checkDate()"/>
					<emp:date id="PrdBasicinfo.enddate" label="到期日期" required="true" onblur="checkDate()"/>
					<emp:text id="PrdBasicinfo.prdversion" label="产品版本号"  maxlength="10"  required="false" />
					<emp:text id="PrdBasicinfo.inputid_displayname" label="登记人员"  readonly="true" required="false" />
					<emp:text id="PrdBasicinfo.inputdate" label="登记日期" maxlength="10" required="false" readonly="true" />
					<emp:text id="PrdBasicinfo.orgid_displayname" label="登记机构"  required="false" readonly="true"/>
					<emp:checkbox id="PrdBasicinfo.prdowner" label="产品归属" required="false" dictname="STD_ZB_PRDLINE" layout="false" colSpan="2" />
					<emp:textarea id="PrdBasicinfo.prddescribe" label="产品描述" maxlength="200" required="false" />
				   
				    <emp:text id="PrdBasicinfo.inputid" label="登记人员" maxlength="30" readonly="true" required="false" hidden="true"/>
				    <emp:text id="PrdBasicinfo.orgid" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				    <emp:text id="PrdBasicinfo.supcatalog" label="上级目录" maxlength="100" required="false" readonly="true" hidden="true"/>
				</emp:gridLayout>
				
				<emp:gridLayout id="" maxColumn="2" title="产品配置表">
					<emp:checkbox id="PrdBasicinfo.currency" label="可用币种" dictname="STD_ZX_CUR_TYPE" layout="false" colSpan="2" required="true" />
					<emp:checkbox id="PrdBasicinfo.guarway" label="可用担保方式" dictname="STD_ZB_ASSURE_MEANS" layout="false" colSpan="2" required="true" />
					<emp:checkbox id="PrdBasicinfo.canFeeCode" label="可用费用代码" dictname="STD_ZB_FEE_CODE" layout="false" colSpan="2" required="false" />
					<emp:checkbox id="PrdBasicinfo.needFeeCode" label="必需费用代码" dictname="STD_ZB_FEE_CODE" layout="false" colSpan="2" required="false" />
					<emp:text id="PrdBasicinfo.preventtactics" label="拦截策略" required="false" />
					<emp:text id="PrdBasicinfo.loanform" label="申请表单"  required="false" />
					<emp:text id="PrdBasicinfo.contform" label="合同表单" required="false" />
					<emp:text id="PrdBasicinfo.pvpform" label="出账表单" required="false" />
					<emp:select id="PrdBasicinfo.loanflow" label="申请流程" dictname="ZB_BIZ_CATE"  required="false" />
					<emp:select id="PrdBasicinfo.pvpway" label="出账方式" required="false" dictname="STD_ZB_PUTOUT_TYPE" />
					<emp:select id="PrdBasicinfo.payflow" label="放款流程" dictname="ZB_BIZ_CATE" required="false" />
					<emp:pop id="PrdBasicinfo.policytactics" label="政策策略" flat="flase" url="queryPrdPolcySchemeListPop.do?returnMethod=returnMethod" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" buttonLabel="选择"/>
					<emp:pop id="PrdBasicinfo.businessrule" label="业务规则" url="rulespop.do?id=PrdBasicinfo.businessrule" required="false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" buttonLabel="选择"/>
					<emp:textarea id="PrdBasicinfo.businessrule_displayname" label="检查规则描述" readonly="true" colSpan="2" />
					<emp:text id="PrdBasicinfo.costset" label="费用设置" maxlength="20" hidden="true" required="false" />
					<emp:text id="PrdBasicinfo.repayway" label="还款方式设置" hidden="true" maxlength="20" required="false" />
					<emp:text id="PrdBasicinfo.datacollection" label="资料收集" maxlength="40" required="false" />
					<emp:textarea id="PrdBasicinfo.contmapping" label="合同映射"  required="false" colSpan="2" cssElementClass="emp_field_textarea_textarea1"/>
					<emp:textarea id="PrdBasicinfo.pvpmapping" label="出账映射" required="false" colSpan="2" cssElementClass="emp_field_textarea_textarea1"/>
					<emp:textarea id="PrdBasicinfo.comments" label="备注" maxlength="200" required="false" cssElementClass="emp_input"/> 
				</emp:gridLayout>
				
				<div  class='emp_gridlayout_title'>还款方式设置</div>
				<div id="tempButton" style="display:${param.optype}" >
				  	<emp:button id="addPrdRepayMode" label="新增" />
					<emp:button id="delPrdRepayMode" label="删除" locked="false"/>
				</div>
				<emp:table icollName="PLoanTypMtdList" pageMode="true" url="getPrdRepayModeList4Prd.do">
					<emp:text id="repay_mode_id" label="还款方式代码" />
		            <emp:text id="repay_mode_type" label="还款方式种类" dictname="STD_ZB_REPAY_MODE" />
					<emp:text id="min_term" label="支持最小期限(月)" />
					<emp:text id="max_term" label="支持最大期限(月)" />
					<emp:text id="repay_interval" label="还款间隔" hidden="true" />
					<emp:text id="firstpay_perc" label="首付比例" dataType="Rate"/>
					<emp:text id="lastpay_perc" label="尾付比例" dataType="Rate"/>
					<emp:text id="is_instm" label="是否期供类" dictname="STD_ZX_YES_NO" />
					<emp:text id="repay_mode_dec" label="还款方式描述" />
				</emp:table>
			</emp:tab>
			<emp:tab label="产品适用机构配置" url="getPrdOrgApplyListByPrdPk.do?prdId=${context.PrdBasicinfo.prdid}"  id="org_tab" initial="false" needFlush="true">
			</emp:tab>
		</emp:tabGroup>
	<form  method="POST" action="#" id="queryForm">
	     <emp:text id="a.prdid" label="产品" defvalue="${context.prdid}" hidden="true"/>
	</form>
		<div align="center">
			<br>
			<emp:button id="saveUpdate" label="修改" op="update"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
