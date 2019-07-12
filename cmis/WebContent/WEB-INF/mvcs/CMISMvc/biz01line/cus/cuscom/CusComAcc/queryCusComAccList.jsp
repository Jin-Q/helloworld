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
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComAcc._toForm(form);
		CusComAccList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComAccPage() {
		var paramStr = CusComAccList._obj.getParamStr(['cus_id','acc_no']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComAccUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComAcc() {
		var paramStr = CusComAccList._obj.getParamStr(['cus_id','acc_no']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComAccViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComAccPage() {
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusComAccAddPage.do"/>&CusComAcc.cus_id=${context.CusComAcc.cus_id}'+"&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComAcc(){
        var paramStr = CusComAccList._obj.getParamStr(['cus_id','acc_no']);
        if (paramStr != null) {
            if(confirm("是否确认要删除？")){
                var url = '<emp:url action="deleteCusComAccRecord.do"/>?'+paramStr;
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
                             var cus_id  ='${context.CusComAcc.cus_id}';
                             var editFlag = '${context.EditFlag}';
                             var paramStr="CusComAcc.cus_id="+cus_id+"&EditFlag="+editFlag;
                             var url = '<emp:url action="queryCusComAccList.do"/>&'+paramStr;
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
	}
	
	function doReset(){
		page.dataGroups.CusComAccGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
	<%
	//	String flag=(String)request.getSession().getAttribute("buttonFlag");
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusComAccPage" label="新增"/>
		<div style="display:none">
		  <emp:button id="getUpdateCusComAccPage" label="修改"/>
		</div>
		<emp:button id="deleteCusComAcc" label="删除"/>
	<%}%>
		<emp:button id="viewCusComAcc" label="查看"/>
	</div>
	<emp:table icollName="CusComAccList" pageMode="true" url="pageCusComAccQuery.do" reqParams="CusComAcc.cus_id=${context.CusComAcc.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" hidden="true" />
		<emp:text id="acc_no" label="结算账户帐号" />
		<emp:text id="acc_date" label="账户开户日期" />
		<emp:text id="acc_name" label="账户名称" />
		<emp:text id="acc_type" label="账号类型" dictname="STD_ZB_CUS_ACC_TYPE"/>
		<emp:text id="acc_open_orgname" label="开户机构名称" hidden="true"/>
		<emp:text id="acc_orgname" label="核算机构名称" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>