<%@page language="java" contentType="text/html; charset=GBK" import="java.util.*" %>
<%@taglib uri="/WEB-INF/runqianReport4.tld" prefix="report"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.yucheng.cmis.pub.util.TimeUtil"%>
<%@page import="com.yucheng.cmis.pub.util.Format"%>
<%@page import="com.yucheng.cmis.base.CMISException"%>
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
	/**modified by lisj 2014��12��11�� ����:��XD140818051�����˶��е������죬����ӡ����������Word����ť�����û� begin**/ 
    String pageMark="yes";
    String pageScroll="yes";
    String needPrint ="yes";
    String needSaveAsWord ="yes";
    pageMark=(String)request.getParameter("pageMark");
    needPrint =(String)request.getParameter("needPrint");
    needSaveAsWord =(String)request.getParameter("needSaveAsWord");
    if(pageMark!=null&&pageMark.equals("no")){
       pageMark="no";
       pageScroll="yes";
    }
    else {
      //modified by yangzy 2015/06/11 �����ţ�XD150107002 �Ŵ�ҵ��ֽ�ʵ�������ĵ������ӡ���� start
      pageMark="no";
      //modified by yangzy 2015/06/11 �����ţ�XD150107002 �Ŵ�ҵ��ֽ�ʵ�������ĵ������ӡ���� end
      pageScroll="no";
    }
    if(needPrint!=null && needPrint.equals("no")){
    	needPrint = "no";
    }else{
    	needPrint = "yes";
    }
    if(needSaveAsWord!=null && needSaveAsWord.equals("no")){
    	needSaveAsWord = "no";
    }else{
    	needSaveAsWord = "yes";
    }
    /**modified by lisj 2014��12��11�� ����:��XD140818051�����˶��е������죬����ӡ����������Word����ť�����û� begin**/
    
    String pageWrite="yes";
    pageWrite=(String)request.getParameter("pageWrite");
    System.out.println(" pageMark="+pageMark+"pageScroll="+pageScroll);
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
	<body bgColor="#FBFDFF">
		<table width="100%"   border="0" cellspacing="0"
			cellpadding="0"  align="center" id="haha">
		<tr align="center">
		<td width="100%" height="100%" align="center" valign="middle"> 
			<report:html name="Report1" 
				reportFileName="<%=reportId%>" srcType="file"
				params="<%=reportParam%>" scale="1.2" 
				needScroll="<%=pageScroll %>" needPageMark="<%=pageMark %>" scrollWidth="100%" scrollHeight="100%"
				width="-1" height="-1" funcBarLocation="bottom"
				backAndRefresh="no"
				separator=" " needSaveAsExcel="yes" 
				needSaveAsPdf="no" needSaveAsWord="no"    
				needPrint="<%=needPrint %>" 
				printLabel="<input name='print_btn' type='button' class='button80' value='�� ӡ'>"
				savePrintSetup="no"  
				excelLabel="<input name='exportExcel_btn' type='button' class='button80' value='����EXCEL'>"
	            submit="<input name='saveToDb' type='button' class='button80' value='����'>"
				saveAsName="<%=reportSaveFile%>"  
				firstPageLabel="<input name='firstPage_btn' type='button' class='button80' value='�� ҳ'>"
				prevPageLabel="<input name='prevPage_btn' type='button' class='button80' value='��һҳ'>"
				nextPageLabel="<input name='nextPage_btn' type='button' class='button80' value='��һҳ'>"
				lastPageLabel="<input name='lastPage_btn' type='button' class='button80' value='β ҳ'>"
				displayNoLinkPageMark="yes" timeout="5"/>
		</td>
	</tr>	
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


	<%-- 
	  ����չ��ʵ����
	         var form=document.getElementById('reportForm');
		     form.action="rqReport/reportShowPage.jsp";
		    // id2Form(form,'pageMark','no'); ��ҳ����
		     // id2Form(form,'pageWrite','no'); ��д������ ֻ������� ���Ʊ��水ť���� ��pageWrite=noʱ������������ʾ
			 id2Form(form,'reportId','lqpLoanCom.raq');//����ģ������ ��������
			 id2Form(form,'macro??',"select * from s_org where organno like '@20@' ");//�����SQL @����% ������macro��ͷ
			 
		     id2Form(form,'serno',IqpLoanAppList._obj.getParamValue(['serno']));
		   	 form.submit();
		   	 
          ����һ��formר������չ�ֱ����ύ
		   	 <form method="POST" action="" id="reportForm" target="_blank">
             </form>
	
	--%>
	
	
