<%@page language="java" contentType="text/html; charset=GBK" import="java.util.*" %>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.yucheng.cmis.base.CMISException"%>
<%@page import="com.yucheng.cmis.pub.util.Format"%>
<%@page import="com.yucheng.cmis.pub.util.TimeUtil" %>
<% 
  String reportId = null;
  String startDate = null;
  String endDate = null;
  String reportParam=null;
  String reportSaveFile=null;
  String op=null;
  
	try{
		op=(String)request.getParameter("op");
	}catch(Exception e){}
	
	try{
		reportId=(String)request.getParameter("reportId");	
	}catch(Exception e){}
	
	try{
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String type = "";
		reportId=(String)context.getDataValue("reportId");
	}catch(Exception e){}
	
	try{
		startDate=(String)request.getParameter("startDate");	
	}catch(Exception e){}
	
	try{
		endDate=(String)request.getParameter("endDate");	
	}catch(Exception e){}
	
	
	if(reportId!=null&&reportId.indexOf(".raq")==-1)
	   reportId+=".raq";	
	reportParam=""; 
	
	 
	Enumeration paramN=request.getParameterNames();
	while(paramN!=null&&paramN.hasMoreElements()){
	   String name=(String)paramN.nextElement();
	   String[] value=request.getParameterValues(name);
	   String values="";
	     for(int i=0;i<value.length;i++){
	      if(values.equals("")||values==null)
	        values=value[i];
	      else
	        values=values+","+value[i];
	     }
	  if(name==null)continue;
	  if(values==null)continue;
	  if(name.startsWith("macro")) //��SQLֱ�Ӵ���  ����%����@���� ��SQL������macro��ͷ
		  values=values.replaceAll("@","%"); 
	  reportParam+=name+"="+values+";"  ;
	  name=null;
	  values=null;
	}

	 System.out.println("reportParam=="+reportParam);
	  
	 reportSaveFile=TimeUtil.getDateTime("yyyy-MM-dd.HH.mm.ss")+"-"+Format.generate();
	 if(reportSaveFile.length()>30)
		 reportSaveFile=reportSaveFile.substring(0, 30);
	 
    String pageMark="yes";
    String pageScroll="yes";
    pageMark=(String)request.getParameter("pageMark");
    if(pageMark!=null&&pageMark.equals("no")){
       pageMark="no";
       pageScroll="yes";
    }
    else {
      pageMark="yes";
      pageScroll="no";
    }
    
    String pageWrite="yes";
    pageWrite=(String)request.getParameter("pageWrite");
    System.out.println(" pageMark="+pageMark+"pageScroll="+pageScroll);
 %>

<html>
	<head>
		 

		<title>Report4����չʾ</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		 
		<link href="<emp:file fileName='styles/flowApprove.css'/>" rel="stylesheet" type="text/css" />
	</head>
<body topmargin=0 leftmargin=0 rightmargin=0 bottomMargin=0>

<table id=rpt align=center><tr><td>
<table align=center>
	<tr><td>
		<report:html 
			name="report1" 
			reportFileName="<%=reportId%>"
			width="-1" 
			height="-1" 
			funcBarLocation="bottom"
			srcType="file"
			params="<%=reportParam%>" scale="1.0" 
			needScroll="<%=pageScroll %>" 
			needPageMark="<%=pageMark %>" 
			scrollWidth="100%" 
			scrollHeight="100%"
			backAndRefresh="no"
			separator=" " needSaveAsExcel="yes" 
			needSaveAsPdf="no" needSaveAsWord="yes"    
			needPrint="yes" 
			printLabel="<input name='print_btn' type='button' class='button80' value='�� ӡ'>"
			savePrintSetup="no"  
			excelLabel="<input name='exportExcel_btn' type='button' class='button80' value='����EXCEL'>"
			wordLabel="<input name='exportWrod_btn' type='button' class='button80' value='����Word'>"
			submit="<input name='saveToDb' type='button' class='button80' value='����'>"
			saveAsName="<%=reportSaveFile%>"  
			firstPageLabel="<input name='firstPage_btn' type='button' class='button80' value='�� ҳ'>"
			prevPageLabel="<input name='prevPage_btn' type='button' class='button80' value='��һҳ'>"
			nextPageLabel="<input name='nextPage_btn' type='button' class='button80' value='��һҳ'>"
			lastPageLabel="<input name='lastPage_btn' type='button' class='button80' value='β ҳ'>"
			displayNoLinkPageMark="yes" timeout="5"/>
	</td></tr>
</table>
</body>
	<script type="text/javascript">
	  window.onload=function(){
        var pageWrite="<%=pageWrite%>";
        try{
	        var iEBtn=document.getElementsByName("importExcel");
	        iEBtn[0].disabled=true;
	        iEBtn[0].display="none";
	        if(window.Report1_importExcel){
	        	window.Report1_importExcel=function(){alert("���ô˲�����");};
	        }
        }catch(e){}
        if(pageWrite=="no") {
            var saveBtn=document.getElementsByName("saveToDb");
                saveBtn[0].disabled=true;
            var submitSpan=document.getElementById("runqian_submit");            
                submitSpan.onclick="";
        }
		var op = '<%=op%>';
		if(op=="view"){
			var doSave=document.getElementsByName("saveToDb");
			doSave[0].style.display="none";
		}
	  }

	function _submitTable(table) {
		if("lqpLoanIndiv.raq" == "<%=reportId%>") {
			//�����ڱ༭�������ʧȥ����
			if(document.getElementById("Report1_A14").value == "" || document.getElementById("Report1_A16").value == "" 
				|| document.getElementById("Report1_A18").value == "" || document.getElementById("Report1_A35").value == "") {
				alert("�뽫���ű����2��3��4��8����д����");
				return false;
		       }
		    }
		    _submitReport(table);
		    return true;
	      }
	  
	</script>
</html>
	
