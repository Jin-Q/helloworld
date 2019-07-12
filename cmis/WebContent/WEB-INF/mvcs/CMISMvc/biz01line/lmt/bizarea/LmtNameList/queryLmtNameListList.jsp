<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<%
	//add by yangzy  2015-6-15   需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isShow = "oper"; 
	if(context.containsKey("isShow") && !"".equals(context.getDataValue("isShow"))){
		isShow = context.getDataValue("isShow").toString();
	}
	//add by yangzy  2015-6-15   需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtNameList._toForm(form);
		LmtNameListList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtNameList() {
		var paramStr = LmtNameListList._obj.getParamStr(['agr_no','cus_id']);
		if (paramStr != null) {
		//	var joint_agr_no = LmtNameListList._obj.getSelectedData()[0].joint_agr_no._getValue();
		//	var is_limit_set = LmtNameListList._obj.getSelectedData()[0].is_limit_set._getValue();//是否进行额度设置
			var url = '<emp:url action="getLmtNameListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		//	window.open(url,'newwindow1','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+', top=120, left=200, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtNameListGroup.reset();
	};
	/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
	/**add by lisj/yangzy  2015-6-15   需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	//封面打印
	function doPrintln(){
		var paramStr = LmtNameListList._obj.getParamStr(['agr_no','cus_id']);
		if (paramStr != null) {
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("异步调用通讯发生异常！");
						return;
					}
					var flag=jsonstr.flag;
					if(flag =="success"){
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=01|04|12&jural_flag=lmtjoint";
						url = EMPTools.encodeURI(url);
						var param = 'height=450, width=700, top=80, left=80, toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no, status=no';
						window.open(url,'newWindow4LAI',param);
					}else{
						alert("清空原表报数据失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("清空原表报数据发生异常，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=01|04|12";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj/yangzy 2015-6-15   需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="LmtNameListGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtNameList.agr_no" label="协议编号" hidden="true"/>
		<emp:text id="LmtNameList.cus_id" label="客户码" />
	</emp:gridLayout>
		
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left" id="divBtns1" >
		<emp:button id="viewLmtNameList" label="查看" op="view"/>
		<!-- add by lisj/yangzy 2015-6-15  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  begin -->
		<%if("view".equals(isShow)) {%>
		<emp:button id="println" label="封面打印" />
		<%} %>
		<!-- add by lisj/yangzy 2015-6-15  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  end -->
	</div>

	<emp:table icollName="LmtNameListList" pageMode="true" url="pageLmtNameListQuery.do" reqParams="agr_no=${context.agr_no}">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cus_status" label="客户状态" dictname="STD_LMT_CUS_STATUS" />
		<emp:text id="is_limit_set" label="是否进行额度设置" dictname="STD_ZX_YES_NO" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    