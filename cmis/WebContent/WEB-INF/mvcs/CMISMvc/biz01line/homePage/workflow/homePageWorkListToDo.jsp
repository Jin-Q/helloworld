<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
 
<emp:page>
<html>
<head>
 
<jsp:include page="/include.jsp" flush="true" />
 
</head>
<body >
	
	<div id="workFlowListToDo" align="left">
		正在读取数据...
		
	</div>
	<emp:text id="apply_type" label="字典load" hidden="true" dictname="ZB_BIZ_CATE" />
 
</body>
</html>
</emp:page>

 
<script type="text/javascript">


	function toThePage(){
		var url = '<emp:url action="getToDoWorkListPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.parent.location =url;  
	}

	function doLoadData(){
 		var node = document.getElementById("workFlowListToDo");
 		 
		var dic = page.objectsDefine.dataDics['ZB_BIZ_CATE'];
		 
		var sUrl='<emp:url action="queryHomePageWorkFlowListToDo.do" />';
		var callback =
		{
			success: function(o) {
			 	var data = eval("("+o["responseText"]+")");
					
				var WorkFlowToDoListList= new Array();
				WorkFlowToDoListList=data["WorkFlowToDoList"]    ;
				var i=0;
				var content="";
				for(  i=0;i< WorkFlowToDoListList.length ;i++){
					var type= WorkFlowToDoListList[i]["apply_type"]  ;
				 
					//var type_cnname = WorkFlowToDoListList[i]["apply_type_cnname"]  ;
					var type_cnname=dic[type];
					var count= WorkFlowToDoListList[i]["count"]  ;
					content=content+'<a href="' + '<emp:url action="getDoneWorkListPage.do"/>'+'" onClick="toThePage()"> '+ type_cnname + ' 有：' + count + '笔待办' +'</a> <hr>';  
					}

				node.innerHTML=content;
				
			},
			
			failure: function(o) {alert("12")}, 
			argument: [1, 1, 1]  
		}
		 var transaction = YAHOO.util.Connect.asyncRequest("GET", sUrl, callback, null); 
	 	}
	doLoadData();
 

</script>
 