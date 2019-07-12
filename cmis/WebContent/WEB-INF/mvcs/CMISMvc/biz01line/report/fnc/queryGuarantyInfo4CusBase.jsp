<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<emp:page>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cus_id = (String)context.getDataValue("cus_id");
	String manager_id = (String)context.getDataValue("manager_id");
	String cus_properties = (String)context.getDataValue("cus_properties");
%>
<html>
<head>
<title>担保合同信息</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
 function doPrint(){
	 var recordCount = GuarantyInfo4CusBaseList._obj.recordCount;//取总的记录数
	 var count = 0;
	 var guarContNoStr ="";
	 var mgr_id_0 = "";
	 var mgr_br_id_0 = "";
	 var mgr_id = "";
	 var mgr_br_id = "";
	 for(var i=0;i<recordCount;i++){
		 var box = GuarantyInfo4CusBaseList._obj.data[i].box._getValue();
			if(box=="on"){
				var guar_cont_no = GuarantyInfo4CusBaseList._obj.data[i].guar_cont_no._getValue();
				mgr_id = GuarantyInfo4CusBaseList._obj.data[i].manager_id._getValue();
				mgr_br_id = GuarantyInfo4CusBaseList._obj.data[i].manager_br_id._getValue();
				//alert(mgr_id+"::"+mgr_br_id);
				if(count==0){
					mgr_id_0 = mgr_id;
					mgr_br_id_0 = mgr_br_id;
				}else if(mgr_id_0!=mgr_id){
					alert("您所选担保合同的主管客户经理不一致,请重新选择");
					count = 0;
					return false;
				}else if(mgr_br_id_0!=mgr_br_id){
					alert("您所选担保合同的主管机构不一致,请重新选择");
					count = 0;
					return false;
				}
				 count ++;
				 guarContNoStr += guar_cont_no +",";
			}
	}
	if(count>0){
		var cus_id = '<%=cus_id%>';
		var cus_properties = '<%=cus_properties%>';
		var manager_id = '<%=manager_id%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crcusbaseinfo.raq&cus_id='+cus_id+'&manager_id='+manager_id+'&cus_properties='+cus_properties+"&guarContNoStr="+guarContNoStr;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=80,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}else{
		alert("请勾选需打印的担保合同信息！");
	}
 };
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
		纯基础资料封面打印（请勾选下列需打印的担保合同信息）
	<div align="left">
		<emp:button id="print" label="打印" />
	</div>
	
	<emp:table icollName="GuarantyInfo4CusBaseList" pageMode="true" url="pageGuarantyInfo4CusBaseList.do" reqParams="cus_id=${context.cus_id}&manager_id=${context.manager_id}&cus_properties=${context.cus_properties}">
		<emp:checkbox id="box" label="选择" flat="false" />
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_start_date" label="合同起始日" />
		<emp:text id="guar_end_date" label="合同到期日" />
		<emp:text id="guar_cont_state" label="合同状态"  dictname="STD_CONT_STATUS"/>
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>