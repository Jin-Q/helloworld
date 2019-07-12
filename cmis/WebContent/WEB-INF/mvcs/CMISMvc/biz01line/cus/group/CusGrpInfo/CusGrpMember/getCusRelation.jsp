<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="java.util.*"%>
<link href="<emp:file fileName='styles/default/common.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/dataField.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3_menuframe.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/moneystyle.css'/>"
	rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/pageUtil.js'/>"
	type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataType.js'/>"
	type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataField.js'/>"
	type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataGroup.js'/>"
	type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/relatedTabs.js'/>"
	type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dtree.js'/>"
	type="text/javascript" language="javascript"></script>
<!-- <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" /> -->
<jsp:include page="/include.jsp" flush="true" />
<%
	Context context = (Context) request
			.getAttribute(EMPConstance.ATTR_CONTEXT);
	IndexedCollection iColl = (IndexedCollection) context
			.getDataElement("CusRelationList");

	Iterator iter = iColl.iterator();
	StringBuffer node = new StringBuffer();
	String fCus = "61021024-0";
	node.append("<script type='text/javascript' >");
	node.append("var cusTree;var d= new dTree('cusTree');");
	node.append(" d.add('" + fCus + "',-1,'个人客户综合信息');");

	String cusId = "";
	String cusName = "";
	String abovename = "";
	String locate = "";
	String opttype = "";
	String memo = "";
	String level = "";
	String flag = "";
	String orderid = "";

	while (iter.hasNext()) {

		KeyedCollection tmp = (KeyedCollection) iter.next();
		cusId = (String) tmp.getDataValue("enname");
		cusName = (String) tmp.getDataValue("cnname");
		abovename = (String) tmp.getDataValue("abovename");
		locate = (String) tmp.getDataValue("locate");
		opttype = (String) tmp.getDataValue("opttype");
		memo = (String) tmp.getDataValue("memo");
		level = (String) tmp.getDataValue("level");
		flag = (String) tmp.getDataValue("flag");
		orderid = (String) tmp.getDataValue("orderid");
		if (abovename == null || abovename.equals(""))
			abovename = fCus;
		if (cusId == abovename || abovename.equals(cusId))
			continue;
		String url = "<emp:url action='" + ("queryCusComTree.do")
				+ "/>?cus_id" + cusId;
		node.append("var url ='<emp:url action = "
				+ ("queryCusComTree.do") + "/>?cus_id=" + cusId + "';");
		node.append("url = EMPTools.encodeURI(url);");
		node.append("d.add('" + cusId + "','" + abovename + "','"
				+ cusName + "',url,'','_blank')");

	}

	System.err.println(node.toString());
	out.println(node.toString());
%>

document.write(d); cusTree=d;
</script>
<emp:page>
	<html>
	<head>
	<title>关联客户智能搜索页面</title>

	<script type="text/javascript">
	
</script>
	</head>
	<body onload="" bgcolor="#F8F7F7">

	</body>
	</html>
</emp:page>