<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>待办事项</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

  function doQuery(){
    var form = document.getElementById('queryForm');
    WfiWorklistTodo._toForm(form);
    WfiWorklistTodoList._obj.ajaxQuery(null,form);
  };
  
  function doSel(){
    var selObj = WfiWorklistTodoList._obj.getSelectedData()[0];
    var nodeid=selObj.nodeid._getValue();
    var instanceid=selObj.instanceid._getValue();
    var wfsign=selObj.wfsign._getValue();
    var applType = selObj.appl_type._getValue();
    var url = "<emp:url action='getInstanceInfo.do'/>"+
      "?instanceId="+instanceid+"&nodeId="+nodeid+"&applType="+applType;
    url = EMPTools.encodeURI(url);
    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
  };

  function doMyTrack() {
    if(WfiWorklistTodoList._obj.getSelectedData().length == 0) {
      alert("请选择一条记录。");
      return;
    }
    var form = document.getElementById("form");
    var instanceid=WfiWorklistTodoList._obj.getSelectedData()[0].instanceid._getValue();
    form.instanceid.value = instanceid;
    doTrack(form);
  };
  
  function doReset() {
    page.dataGroups.WfiWorklistTodoGroup.reset();
  }

</script>
</head>
<body class="page_content" >  
  <emp:table icollName="WfiWorklistTodoList" pageMode="true" url="pageToDoWorkQuery.do" >
    <emp:link id="appl_type" label="业务类型" dictname="ZB_BIZ_CATE" operation="sel" hidden="true"/>
    <emp:link id="pk_value" label="业务流水号" operation="sel"/>
    <emp:text id="cus_id" label="客户码" />
    <emp:text id="cus_name" label="客户名称" />
    <emp:text id="prd_name" label="产品名称" />
    <emp:text id="amt" label="申请金额" dataType="Currency"/> 
    <emp:text id="prenodename" label="上一审批岗" hidden="true"/>
    <emp:text id="nodestarttime" label="业务提交时间" />
    <emp:text id="nodestatus" label="节点状态" dictname="WF_NODE_STATUS" hidden="true"/>
	<emp:text id="nodestarttime" label="业务提交时间" />
    <emp:text id="wfname" label="流程名称"  hidden="true"/>
    <emp:text id="wfi_status" label="审批状态" dictname="WF_APP_STATUS" hidden="true"/>
    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
    <emp:text id="prenodeid" label="前一节点ID" hidden="true"/>
    <emp:text id="nodeid" label="节点ID" hidden="true"/>
	<emp:text id="wfsign" label="流程标识" hidden="true"/>
	<emp:text id="currentnodeuser" label="当前办理人" hidden="true"/>
  </emp:table>
  
</body>
</html>
</emp:page>