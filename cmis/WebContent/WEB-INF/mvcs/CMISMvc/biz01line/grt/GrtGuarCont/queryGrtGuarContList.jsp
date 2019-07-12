<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String action = (String)context.getDataValue("action");
String flag = "";
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		GrtGuarCont._toForm(form);
		GrtGuarContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateGrtGuarContPage() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="update";
		var data = GrtGuarContList._obj.getSelectedData();
		if (paramStr != null) {
			var status = data[0].guar_cont_state._getValue();
			var rel = data[0].rel._getValue();
			if(status == '00'&&rel=='1'){
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?op=update&'+paramStr+'&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else if(status != '00'){
				alert('非登记状态的合同不能进行修改操作！');
			}else if(rel!='1'){
				alert('非担保合同管理模块新增的担保合同不能在此进行修改操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doUpdateGrtGuarContLmtRel() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="update";
		var data = GrtGuarContList._obj.getSelectedData();
		if (paramStr != null) {
			var status = data[0].guar_cont_state._getValue();
			var rel = data[0].rel._getValue();
			if(status == '01'){
				var url = '<emp:url action="updateGrtGuarContLmtRelPage.do"/>?menuIdTab=updateGrtGuarContLmtRelPage&op=update&'+paramStr+'&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else if(status != '01'){
				alert('非有效状态的合同不能进行授信关系维护操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&'+paramStr+'&oper='+flag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddGrtGuarContPage() {
		var url = '<emp:url action="getGrtGuarContAddPage.do"/>?op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var data = GrtGuarContList._obj.getSelectedData();
		var status = data[0].guar_cont_state._getValue();
		if (paramStr != null) {
			var status = data[0].guar_cont_state._getValue();
			var rel = data[0].rel._getValue();
			if(status != '00'||rel!='1'){
				if(status != '00'){
				  alert('非登记状态的合同不能进行删除操作！');
				}else if(rel!='1'){
				  alert('非担保合同管理模块新增的担保合同不能在此进行删除操作！');
				}
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText != undefined){
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e.message);
							return;
						}
						var flag = jsonstr.flag;
						var msg = jsonstr.msg;
						if(flag == 'success'){
							alert("已删除！");
							window.location.reload();
						}else if(flag == 'fail'){
							alert(msg);
						}else{
							alert("删除失败！");
						}
					}
				};
				var url = '<emp:url action="deleteGrtGuarContRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var callback = {
						success:handleSuccess,
						failure:function(){alert("删除失败！");}
				};
				var obj1 = YAHOO.util.Connect.asyncRequest('post',url,callback);
			}
		}else {
			alert('请先选择一条记录！');
		}
	};

	function doSignGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var data = GrtGuarContList._obj.getSelectedData();
		var status = data[0].guar_cont_state._getValue();
		var flag ="sign";
		if (paramStr != null) {
			if(status =='00'){
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?op=sign&'+paramStr+'&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else {
				alert('只有登记状态的合同可以进行签订操作！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.GrtGuarContGroup.reset();
	};

	//风险拦截
	function interRisk(){
		var serno = GrtGuarContList._obj.getSelectedData()[0].guar_cont_no._getValue();
		var _applType="";
		var _modelId="GrtGuarCont";
		var _pkVal=serno;
		var _preventIdLst="FFFA276200EC9B3A5B05EC0EFEF287E9";
		var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
	    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
	    if(!_retObj || _retObj == '2' || _retObj == '5'){
			if( _retObj == '5'){
				alert("执行风险拦截有错误，请检查！");
			}
			return false;
		}else{
			return true;
		}
	}
	//注销事件
	function doDestroyGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		/**modified by lisj 2014年12月12日 修改当合同状态为【登记】，该笔担保合同未关联授信、业务信息时可做注销操作，于 2014-12-18上线 begin**/
    	if(paramStr!= null){
    		var data = GrtGuarContList._obj.getSelectedData();
    		var status = data[0].guar_cont_state._getValue();
    		var guarContNo = data[0].guar_cont_no._getValue();
    		if(status !='01' && status !='00'){
    			alert("非有效状态的担保合同不能做注销操作！");
    			return;
    		}
    		if(!confirm("确定要注销该笔合同？")){
				return;
        	}
    		if(!interRisk()){
    			return;
    		}
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					}catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("注销成功！");
						window.location.reload();
					}else if(flag == 'undone'){
						alert("该担保合同存在关联的授信或业务信息，不能注销！")
					}else{
						alert("注销失败！");
						window.location.reload();
					}
				}
			};
			var url = '<emp:url action="destroyGrtGuarContRecord.do"/>?'+paramStr+"&guar_cont_state="+status;
			url = EMPTools.encodeURI(url);
			var callback = {
					success:handleSuccess,
					failure:function(){alert("注销失败！");}
			};
			var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback);
    	}else{
			alert("请选择一条记录！");
        }
    	/**modified by lisj 2014年12月12日 修改当合同状态为【登记】，该笔担保合同未关联授信、业务信息时可做注销操作，于 2014-12-18上线 end**/
	};
	
	function returnCus(data){
		GrtGuarCont.cus_id._setValue(data.cus_id._getValue());
		GrtGuarCont.cus_id_displayname._setValue(data.cus_name._getValue());
    };
    function returnCus2(data){
    	GrtGuarCont.guar_id._setValue(data.cus_id._getValue());
    	GrtGuarCont.guar_id_displayname._setValue(data.cus_name._getValue());
    };
    function doPrintGrtGuarCont(){
    	var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
	    if(paramStr!= null){
	        var state = GrtGuarContList._obj.getParamValue(['guar_cont_state']);
	        if(state == '01'){//担保合同状态为'有效'
	        	<%if(action.equals("zg")){ %>
	        	checkCusBelong();
	    		<%}else{ %>
				alert("一般担保合同暂不提供打印！");
	    		<%} %>
				
	        }else{
	            alert("只有担保合同状态为'有效'的才可以进行【打印】操作！");
	        }
    	}else{
			alert("请选择一条记录！");
        }
	}

	function checkCusBelong(){//校验客户条线
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var guar_way = GrtGuarContList._obj.getParamValue(['guar_way']);
				var guar_cont_no = GrtGuarContList._obj.getParamValue(['guar_cont_no']);
				if(flag == "success"){
					if(guar_way == '00'&& checkCusBelong()){//个人最高额抵押合同
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/gezgedyht.raq&guar_cont_no='+guar_cont_no;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}
				}else if(flag == "fail"){
					if(guar_way == '00'){//抵押
			        	var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/zgedyht.raq&guar_cont_no='+guar_cont_no;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				    }else if(guar_way == '01'){//质押
				    	var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/zgezyht.raq&guar_cont_no='+guar_cont_no;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else if(guar_way == '02'){//保证-单人担保
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/zgebzht.raq&guar_cont_no='+guar_cont_no;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else if(guar_way == '03'){//保证-多人分保
						alert("该担保合同为多人分保,暂不提供打印！");
						//var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/zgebzht.raq&guar_cont_no='+guar_cont_no;
						//url = EMPTools.encodeURI(url);
						//window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else if(guar_way == '04'){//保证-多人联保
						alert("该担保合同为多人联保,暂不提供打印！");
						//var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/zgebzht.raq&guar_cont_no='+guar_cont_no;
						//url = EMPTools.encodeURI(url);
						//window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}
				}else if(flag=="notexists"){
					alert("该担保合同所关联的授信,客户条线未维护！");
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
		var url = '<emp:url action="checkCusBelong4GuarContNo.do"/>?'+paramStr;	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="GrtGuarContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号 " />
			<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文合同编号" />
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
			<emp:pop id="GrtGuarCont.guar_id_displayname" label="担保人名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus2" />
			<emp:pop id="GrtGuarCont.cus_id_displayname" label="借款人名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
			<emp:text id="GrtGuarCont.cus_id" label="借款人编号" hidden="true"/>	
			<emp:text id="GrtGuarCont.guar_id" label="担保人编号" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
	<%if(action.equals("zg")){ %>
		<emp:button id="getAddGrtGuarContPage" label="新增" op="add"/>
		<emp:button id="getUpdateGrtGuarContPage" label="修改" op="update"/>
		<emp:button id="deleteGrtGuarCont" label="删除" op="remove"/>
	<%} %>
		<emp:button id="viewGrtGuarCont" label="查看" op="view"/>
		<emp:button id="signGrtGuarCont" label="签订" op="sign"/>
		<emp:button id="destroyGrtGuarCont" label="注销" op="destroy"/>
		<emp:button id="updateGrtGuarContLmtRel" label="授信关系维护" op="relUpdate"/>
    <%if(action.equals("zg")){ %>
		<emp:button id="printGrtGuarCont" label="打印" op="print"/>
	<%} %>
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="true" url="pageGrtGuarContQuery.do?action=${context.action}">
		<emp:text id="guar_cont_no" label="担保合同编号 " />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="lmt_grt_flag" label="是否授信项下" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_model" label="担保模式" dictname="STD_GUAR_MODEL"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
		<emp:text id="cus_id_displayname" label="借款人名称" hidden="true"/>
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
		<emp:text id="reg_date" label="登记日期" />
		<emp:text id="rel" label="申请类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    