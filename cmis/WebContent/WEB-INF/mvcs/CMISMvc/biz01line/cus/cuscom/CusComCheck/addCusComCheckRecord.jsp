<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%String cus_id = (String)request.getParameter("cus_id") ;
	System.out.println(cus_id);
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

       function onload()
       {
   		var curYear = getCurYear();
        
    	   CusComCheck.cus_id._setValue("<%=cus_id%>");
    	   var select = CusComCheck.stat_prd._obj.element;	
   			var y = CusComCheck.stat_prd_year._obj.element;	
   			CusComCheck.stat_prd_year._obj.config.hidden=false;
   			CusComCheck.stat_prd_year._obj._renderStatus();
   			CusComCheck.stat_prd_year._obj._renderRequired(true);
			var m=0;
			
			for(var n=1999;n<2015;n++){
				//alert(1111); 
				y.options[m+1] = new Option();
				y.options[m+1].value = n+1;
				y.options[m+1].text =  n+1+"年";
				
				if(curYear==n+1){
					y.options[m+1].selected =  true;
				}
				m++;
				//alert(m);
			}
    	   for(var i=0;i<12;i++){
				select.options[i+1] = new Option();
				if(i<9){
						select.options[i+1].value = ""+curYear+"0"+(i+1);
					}else{
						select.options[i+1].value = ""+curYear+(i+1);
						}
				select.options[i+1].text =  i+1+"月";
			}

  	     var year=new Option("2000","2000");
		 
       }

       function getCurYear(){

   		var now=new Date();
   		var year=now.getFullYear();
   		return year;
   	}
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="addCusComCheckRecord.do" method="POST">
		
		<emp:gridLayout id="CusComCheckGroup" title="客户检查表" maxColumn="2">
			<emp:text id="CusComCheck.cus_id" label="客户码" maxlength="20" required="true" hidden="true"/>
			<emp:select id="CusComCheck.stat_prd_year" label="年份"  required="true"  onchange="doChangey();"/>
			<emp:select id="CusComCheck.stat_prd" label="报表期间"  required="true" onchange="linkChangeStatPrd()"/>			
			<emp:text id="CusComCheck.ele_cons" label="用电量(度)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.water_cons" label="用水量(吨)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.gas_cons" label="用气量(m³)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.per_tax" label="增值税(元)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.income_tax" label="所得税(元)" maxlength="12" required="true" />
			<emp:text id="CusComCheck.check_person" label="检查人" maxlength="20" required="true" />
			<emp:date id="CusComCheck.check_date" label="日期"  required="false" />
			<emp:textarea id="CusComCheck.remarks" label="备注" maxlength="400" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

