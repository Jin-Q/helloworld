<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<HTML>
<HEAD>
 
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 <jsp:include page="/include.jsp" flush="true"/>
<!--  <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" /> -->
<script src="<emp:file fileName='scripts/dtree.js'/>"
	type="text/javascript" language="javascript"></script>

<title>关联客户智能搜索</title>
</head>
  <script type="text/javascript">
   function linkTo(id,pid,name,info,cusType,nodeAttribute){
     var url = '';
     var windowName='\''+id+'\'';
     //if(cusType == '20'){
    //    url= '<emp:url action="getCusViewPageNoImage.do"/>&cusId='+id+'&info=tree&cusTreeType='+nodeAttribute;
    	url= '<emp:url action="getCusViewPage.do"/>&cusId='+id+'&info=tree&cusTreeType='+nodeAttribute;
    /* }else{
         if(nodeAttribute=='TreeCusIndiv'){
        	 url = '<emp:url action="getCusIndivTree.do"/>&inner_cus_id='+id;
         }else{
			 url = '<emp:url action="getCusComTree.do"/>&inner_cus_id='+id;
         }
		*/
     //}
     url = EMPTools.encodeURI(url);
	 window.open(url,'','height=480, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
   }
  
  </script>
  
		<emp:ctree  />
	  
</html>
 
    