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
	function doOnLoad() {
		//showCertTyp(CusComManager.com_mrg_cert_typ, 'indiv');
	};
	
	function doGetAddCusComManagerPage(){
		var cus_id  ='${context.CusComManager.cus_id}';
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComManager.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComManagerAddPage.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComManager(){
		var paramStr = CusComManagerList._obj.getParamStr(['cus_id','cus_id_rel','com_mrg_typ']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComManagerRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="删除成功"){
							alert("删除成功!");
							var editFlag = '${context.EditFlag}';
						    var cus_id  ='${context.CusComManager.cus_id}';
							var paramStr="CusComManager.cus_id="+cus_id+"&EditFlag="+editFlag;
							var url = '<emp:url action="queryCusComManagerList.do"/>&'+paramStr;
							url = EMPTools.encodeURI(url);
							window.location = url;
					   }else {
						 alert(flag);
						 return;
					   }
					}
				};
				var handleFailure = function(o){	
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
	};
	
	function doUpdateCusComManager(){
		var paramStr = CusComManagerList._obj.getParamStr(['cus_id','cus_id_rel','com_mrg_typ']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComManagerUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComManager._toForm(form);
		CusComManagerList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusComManagerGroup.reset();
	};
	
	function doCopy(){
		var com_mrg_typ = CusComManagerList._obj.getParamValue(['com_mrg_typ']);
		if (com_mrg_typ != null) {
			if("02"==com_mrg_typ){
				checkManager();//校验列表页面是否存在"实际控制人"
			}else{
				alert("该高管类型不是法人代表");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function copyFarenToKongzhiren(){
		var cus_id = CusComManagerList._obj.getParamValue(['cus_id']);
		var paramStr = CusComManagerList._obj.getParamStr(['cus_id','cus_id_rel','com_mrg_typ']);
		var url = '<emp:url action="copyFarenToKongzhiren.do"/>?'+ paramStr;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText != undefined){
				try{
					var jsonstr = eval( "(" + o.responseText + ")" );
				}catch (e) {
	 				alert("复制失败!");
	 			}
				var Msg = jsonstr.backMsg;
				if(Msg == "0"){
					alert("该客户已经是实际控制人，不能新增！");
					return ;
				}else if(Msg == "1"){
					alert("法人复制为实际控制人成功！");
					var editFlag = '${context.EditFlag}';
					var paramStr = "CusComManager.cus_id="+ cus_id+"&EditFlag="+editFlag;
					var stockURL = '<emp:url action="queryCusComManagerList.do"/>&' + paramStr;
					stockURL = EMPTools.encodeURI(stockURL);
					window.location = stockURL;
				}
			}else{
				alert("复制失败!");
			}
		};
		var handleFailure = function(o){
			alert("复制失败!");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}
		YAHOO.util.Connect.asyncRequest('GET',url,callback);
	}

	//校验列表页面是否存在"实际控制人"
	function checkManager(){
		var paramStr = CusComManagerList._obj.getParamStr(['cus_id','cus_id_rel','com_mrg_typ']);
		var url = '<emp:url action="checkComMrgTyp.do"/>?'+ paramStr;
		url = EMPTools.encodeURI(url);
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
					copyFarenToKongzhiren();//复制法人为实际控制人
				}else{
					alert("已存在实际控制人，不能复制！");
					return;
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
		YAHOO.util.Connect.asyncRequest('GET',url,callback);
	}
	
	//查看
	function doViewCusComManager(){
		var paramStr = CusComManagerList._obj.getParamStr(['cus_id','cus_id_rel','com_mrg_typ']);
		if (paramStr != null) {
			var url = '<emp:url action="queryCusComManagerDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	<div align="left">
	<%
//	String flag=(String)request.getSession().getAttribute("buttonFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
	if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusComManagerPage" label="新增" />
		<emp:button id="viewCusComManager" label="查看" />
		<emp:button id="updateCusComManager" label="修改" />
		<emp:button id="deleteCusComManager" label="删除" />
		<input type="button" onclick="doCopy()" class="button120" value="法人复制为实际控制人">
	<%
	}else{
	%>
		<emp:button id="viewCusComManager" label="查看" />
	<%}%>
	</div>
	<emp:table icollName="CusComManagerList" pageMode="true" url="pageCusComManagerQuery.do" reqParams="CusComManager.cus_id=${context.CusComManager.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="com_mrg_name" label="高管人姓名" />
		<emp:text id="cus_country" label="国别" dictname="STD_GB_2659-2000"/>
		<emp:text id="com_mrg_typ" label="高管类别" dictname="STD_ZB_MANAGER_TYPE"/>
		<emp:text id="com_mrg_cert_typ" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="com_mrg_cert_code" label="证件号码" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_rel" label="关联客户码"  hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>