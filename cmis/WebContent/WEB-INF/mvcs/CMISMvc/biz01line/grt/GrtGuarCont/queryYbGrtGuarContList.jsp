<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
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
			if(paramStr != null) {
				var flag ="update";
				var data = GrtGuarContList._obj.getSelectedData();
				var status = data[0].guar_cont_state._getValue();
				if(status == '00'){
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?op=update&flag=ybWh&menuId=ybCount&'+paramStr+'&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.location = url;
				}else{
				  alert('非登记状态的合同不能进行修改操作！');
				}
			}else{
				alert('请先选择一条记录！');
			}
	};
	
	function doViewGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuId=ybCount&flag=ybWh&'+paramStr+'&oper='+flag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddGrtGuarContPage() {
		var url = '<emp:url action="getGrtYBContAddPage.do"/>?op=add&cus_id=${context.cus_id}&menuId=ybCount';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		if (paramStr != null) {
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
						if(flag == 'success'){
							alert("已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
							window.location.reload();
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
		} else {
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
					var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?'+paramStr+'&op=sign&flag=ybWh&menuId=ybCount&oper='+flag;
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
	//注销事件
	function doDestroyGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
    	if(form){
    		GrtGuarContList._toForm(form);
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("注销成功！");
						window.location.reload();
					}else{
						alert("注销失败！");
						window.location.reload();
					}
				}
			};

			var url = '<emp:url action="destroyGrtGuarContRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var callback = {
					success:handleSuccess,
					failure:function(){alert("注销失败！");}
			};
			var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback);
    	}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="GrtGuarContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号 " />
			<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文合同编号" />
			<emp:text id="GrtGuarCont.cus_id_displayname" label="借款人名称" />
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddGrtGuarContPage" label="新增" />
		<emp:button id="getUpdateGrtGuarContPage" label="修改"/>
		<emp:button id="deleteGrtGuarCont" label="删除"/>
		<emp:button id="viewGrtGuarCont" label="查看"/>
		<emp:button id="signGrtGuarCont" label="签订" />
		<emp:button id="destroyGrtGuarCont" label="注销" />
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="true" url="pageYbGrtGuarCont.do?cus_id=${context.cus_id}">
		<emp:text id="guar_cont_no" label="担保合同编号 " />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="lmt_grt_flag" label="是否授信项下" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
		<emp:text id="cus_id_displayname" label="借款人名称" />
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    