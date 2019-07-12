<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c"%>
<emp:page>
<html>
<head>
<title>风险预警</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_label{
	color:#228B22;
	font-family: "宋体","Times New Roman";
	font-size: 13px;
	font-weight: bold;
}

#left{
	
	float:left;
	
}

#right{
	margin:16px;
	float:right;
	 
	width:400px;
	height:200px;
}

}


</style>
<script type="text/javascript">
	function toThePage(e){
		window.parent.location = e.href;
	}
	
</script>
</head>
<body class="page_content">

<div id="center">
<span class="emp_label">最新通知</span>

<marquee id="noticeM"  onmouseover="this.stop()" onmouseout="this.start()" 
            scrollamount=2 direction=up  >
通知读取中....
  
 
 
</marquee>
<br>

</div>

<script>
//控制只显示发布日期已经过的。未到发布日期不处理
	var sUrl='<emp:url action="pageAstSysMessageQuery.do"/>&AstSysMessage.senddate_end=${context.OPENDAY}';
	var callback =
	{
		success: function(res) {   
		
		 	var data = eval("("+res["responseText"]+")");
				
			
			var AstSysMessageList= new Array();
			AstSysMessageList=data["AstSysMessageList"]    ;
			var i=0;
			var content="";
			for(  i=0;i< AstSysMessageList.length ;i++){
				var id= AstSysMessageList[i]["mes_id"]  ;
				var title= AstSysMessageList[i]["title"]  ;
				var senddate= AstSysMessageList[i]["senddate"]  ;
				content=content+'<a href="'+'<emp:url action="getAstSysMessageViewPage.do?menuId=tongzhi"/>'+'&mes_id='+id+'" onClick="toThePage(this)" >'+(i+1)+".&nbsp;&nbsp;"+title+"("+senddate+')</a><br>';
			}
			if(content == ""){
				content = '暂无通知';
			}
			//alert(content);
			noticeM.innerHTML=content;
			
		},  
        failure: function(res) {/*failure handler code*/},  
        argument: [1, 1, 1]  
    };
  var transaction = YAHOO.util.Connect.asyncRequest("GET", sUrl, callback, null); 

</script>

</body>
</html>
</emp:page>
