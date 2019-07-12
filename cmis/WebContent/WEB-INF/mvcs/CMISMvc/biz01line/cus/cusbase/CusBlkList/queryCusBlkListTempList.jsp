<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBlkListTemp._toForm(form);
		CusBlkListTempList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusBlkListTempPage() {
		var paramStr = CusBlkListTempList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusBlkListTempList._obj.getParamValue('status');
			var source = CusBlkListTempList._obj.getParamValue('source');
			if(source == '1') {
			    alert("导入的数据不能做修改操作!");
			    return ;
			}
			if(status != '001'&&status != '004') {
			//    alert("只有状态为[预登记]或[撤销]的记录才能修改!");
			    alert("只有状态为[预登记]的记录才能修改!");
			    return ;
			}
			var url = '<emp:url action="getCusBlkListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
	    }else {
			alert('请先选择一条记录！');
		}
	}
	
	function doViewCusBlkListTemp() {
		var paramStr = CusBlkListTempList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusBlkListTempViewPage.do"/>?'+paramStr+'&type=query&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusBlkListTempPage() {
		var url = '<emp:url action="getCusBlkListAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusBlkListTemp() {
		var data = CusBlkListTempList._obj.getSelectedData();
		if (data != null) {
			var status = CusBlkListTempList._obj.getParamValue('status');
			var serno = CusBlkListTempList._obj.getParamValue('serno');
			if(status != '001'&&status != '004') {
			//    alert("只有状态为[预登记]或[撤销]的记录才能删除!");
			    alert("只有状态为[预登记]的记录才能删除!");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusBlkListRecord.do"/>?serno='+serno;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						var flagInfo=jsonstr.flagInfo;						
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();								
						}
					}
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
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
	
	function doReset(){
		page.dataGroups.CusBlkListTempGroup.reset();
	};
	//生效
	function doInureCusBlkListTemp(){
		var status = CusBlkListTempList._obj.getParamValue(['status']);
		if (status != null) {
			if(status=='001'||status=='004'){
				var paramStr = CusBlkListTempList._obj.getParamStr(['serno']);
				if (paramStr != null) {
					paramStr += '&status=002';
					doUpdateStatus(paramStr);
				}
			}else{
				alert('请选择状态为[预登记]或[撤销]的记录进行操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	//撤销
	function doCancleCusBlkList(){
		var status = CusBlkListTempList._obj.getParamValue(['status']);
		if (status != null) {
			if(status=='002'){
				var paramStr = CusBlkListTempList._obj.getParamStr(['serno']);
				if (paramStr != null) {
					paramStr += '&status=004';
					doUpdateStatus(paramStr);
				}
			}else{
				alert('请选择状态为[生效]的记录进行操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	/*--user code begin--*/
	function doUpdateStatus(paramStr){
		var url = '<emp:url action="updateCusBlkStatus.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("异步调用失败!");
					return;
				}
				var flag=jsonstr.flag;	
				var flagInfo=jsonstr.flagInfo;						
				if(flag=="success"){
					alert("操作成功！");
					window.location.reload();
				}else if(flag=="exist"){
					setCusInfo();//不存在于本系统中时，提示其是否去开户。
				}else{
					alert("操作失败，请联系管理员");
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("操作失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	function setCusInfo(){
		var certTyp = CusBlkListTempList._obj.getParamValue('cert_type');
		var certCode = CusBlkListTempList._obj.getParamValue('cert_code');
		var cusName = CusBlkListTempList._obj.getParamValue('cus_name');
		if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
		    alert("证件类型证件号码不能为空！");
		    return ;
		}

		//校验组织机构代码
		if(certTyp=="a"){
			if(!CheckOrganFormat(certCode)){
	         	return false;
			}
		}
		if(certTyp=="20"){
			if(!CheckCertCodeFormat(certCode)){
	         	return false;
			}
		}
		if(certTyp=='0' || certTyp=='7'){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if(certCode.length=='15'){
					if(confirm("15位身份证不能开户！")){
						return false;
					}else{
					}
				}
			}else{
				return false;
			}
		}

		//调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名) 
		getCusBlkInfo('returnCus',certTyp,certCode,cusName,'setCusInfo');
	}
	
	//校验统一社会信用代码输入是否正确
	function CheckCertCodeFormat(Code) { 
	　　var patrn = /^[0-9A-Z]+$/;
	 　	//18位校验及大写校验
	　　if ((Code.length != 18) || (patrn.test(Code) == false)) {
	　　　　 alert("不是有效的统一社会信用代码！"); 
			return false;
	　　} else { 
	　　　	var Ancode;//统一社会信用代码的每一个值
	 　　　	var Ancodevalue;//统一社会信用代码每一个值的权重 
	　　　　	var total = 0; 
	　　　　	var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子 
	　　　　	var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
	　　　　	//不用I、O、S、V、Z 
	　　　　	for (var i = 0; i < Code.length - 1; i++) {
	 　　　		Ancode = Code.substring(i, i + 1); 
	　　　　		Ancodevalue = str.indexOf(Ancode); 
	　　　　		total = total + Ancodevalue * weightedfactors[i];
	　　　　		//权重与加权因子相乘之和 
	　　　　	}
	 　　　 	var logiccheckcode = 31 - total % 31;
	　　　　	if (logiccheckcode == 31){
	　　　　　　logiccheckcode = 0;
	　　　　	}
	　　　　	var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
	　　　　	var Array_Str = Str.split(',');
	　　　　	logiccheckcode = Array_Str[logiccheckcode];
	　　　　 var checkcode = Code.substring(17, 18);
	　　　　 if (logiccheckcode != checkcode) {
	　　　　　　	alert("不是有效的统一社会信用代码！"); 
				return false;
	 　　　　}
	 		return true;
	 　　} 
	}
	
	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column
	function returnCus(cusObj){
		if(cusObj==null||cusObj=='undefined'){
			return;
		}
		
		var cus_name=cusObj.cus_name;
		if (cus_name != null) {
		}
		var com_wprm_code = cusObj.reg_code;
		if (com_wprm_code != null) {
		}
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
		}
		var cus_id = CusComRelApital.cus_id._getValue();
		if(cus_id_rel == cus_id){
			alert("出资人不能为自己,获取失败！")
		}
		var com_inv_loan_card = cusObj.loan_card_id;
		
		if (com_inv_loan_card != null) {	
		}
	}
	
	//导入
    function doImportCusBlkListTemp(){
    	var url = '<emp:url action="queryCusBlkListImport.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }
    //下载模板
    function doDownLoadCusBlkListTemp(){
    	var url = '<emp:url action="downLoadCusBlkTmplate.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
    }
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="CusBlkListTempGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusBlkListTemp.cus_name" label="客户名称" />
			<emp:select id="CusBlkListTemp.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
			<emp:text id="CusBlkListTemp.cert_code" label="证件号码" />
			<emp:select id="CusBlkListTemp.status" label="状态" dictname="STD_CUS_BLK_STATUS"/>
			<emp:text id="CusBlkListTemp.legal_name" label="法定代表人" />
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:button id="downLoadCusBlkListTemp" label="下载模板" op="downLoad"/>
		<emp:button id="importCusBlkListTemp" label="导入" op="add"/>
		<emp:button id="getAddCusBlkListTempPage" label="新增" op="add"/>
		<emp:button id="viewCusBlkListTemp" label="查看" op="view"/>
		<emp:button id="getUpdateCusBlkListTempPage" label="修改" op="update"/>
		<emp:button id="deleteCusBlkListTemp" label="删除" op="remove"/>
		<emp:button id="inureCusBlkListTemp" label="生效" op="inure"/>
		<!--  <emp:button id="cancleCusBlkList" label="撤销" op="cancl"/>-->
		
		
	</div>

	<emp:table icollName="CusBlkListTempList" pageMode="true" url="pageCusBlkListTempQuery.do">
		<emp:text id="black_date" label="列入日期" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="serno" label="登记流水号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" hidden="false"/>
		<emp:text id="legal_name" label="法定代表人" hidden="false"/>
		<emp:text id="black_level" label="不宜贷款户级别" dictname="STD_ZB_BLACKLIST_TYP" hidden="true"/>
		<emp:text id="data_source" label="数据来源" dictname="STD_ZB_DATA_SOURCE"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" hidden="false"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:date id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="status" label="状态" dictname="STD_CUS_BLK_STATUS"/>
		<emp:text id="source" label="来源" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    