<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String catalog_id = (String)context.getDataValue("catalog_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doQuery(){
	var form = document.getElementById('queryForm');
	PspCheckItem._toForm(form);
	PspCheckItemList._obj.ajaxQuery(null,form);
};

function doReset(){
	page.dataGroups.PspCheckItemGroup.reset();
};

function doSelect(){
	var data = PspCheckItemList._obj.getSelectedData();
	if (data != null) {
	var item_id = data[0].item_id._getValue();
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
				window.opener["${context.returnMethod}"](data[0]);
				window.close();
			}else {
				alert("已存在！"); 
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

	var url = '<emp:url action="checkHaveItem.do"/>?item_id='+item_id+'&catalog_id='+'<%=catalog_id%>';
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		
	} else {
		alert('请先选择一条记录！');
	}
	
};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspCheckItemGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckItem.item_id" label="项目编号" />
			<emp:text id="PspCheckItem.item_name" label="项目名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选择" />
	</div>

	<emp:table icollName="PspCheckItemList" pageMode="true" url="pagePspCheckItemQuery.do">
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="default_value" label="缺省值" hidden="true"/>
		<emp:text id="tag_type" label="标签类型" />
		<emp:text id="tag_attr" label="标签属性" hidden="true"/>
		<emp:text id="msg" label="提示信息" hidden="true"/>
		<emp:text id="url" label="URL" hidden="true"/>
		<emp:text id="url_desc" label="URL说明" hidden="true"/>
		<emp:text id="is_null" label="是否不为空" dictname="STD_ZX_YES_NO" />
		<emp:text id="is_judge" label="是否自动判断" dictname="STD_ZX_YES_NO" />
		<emp:text id="rule" label="业务规则" hidden="true"/>
		<emp:text id="memo" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_br_id" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    