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
	  if(name.startsWith("macro")) //宏SQL直接传递  其中%请用@代替 宏SQL名称以macro打头
		  values=values.replaceAll("@","%"); 
	  reportParam+=name+"="+values+";"  ;
	  name=null;
	  values=null;
	}

	 System.out.println("reportParam=="+reportParam);
	  
	 reportSaveFile=TimeUtil.getDateTime("yyyy-MM-dd.HH.mm.ss")+"-"+Format.generate();
	 if(reportSaveFile.length()>30)
		 reportSaveFile=reportSaveFile.substring(0, 30);
	/**modified by lisj 2014年12月11日 需求:【XD140818051】出账队列导出改造，【打印】、【导出Word】按钮可配置化 begin**/ 
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
      //modified by yangzy 2015/06/11 需求编号：XD150107002 信贷业务纸质档案封面的导出与打印功能 start
      pageMark="no";
      //modified by yangzy 2015/06/11 需求编号：XD150107002 信贷业务纸质档案封面的导出与打印功能 end
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
    /**modified by lisj 2014年12月11日 需求:【XD140818051】出账队列导出改造，【打印】、【导出Word】按钮可配置化 begin**/
    
    String pageWrite="yes";
    pageWrite=(String)request.getParameter("pageWrite");
    System.out.println(" pageMark="+pageMark+"pageScroll="+pageScroll);
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		 

		<title>Report4报表展示</title>

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
				printLabel="<input name='print_btn' type='button' class='button80' value='打 印'>"
				savePrintSetup="no"  
				excelLabel="<input name='exportExcel_btn' type='button' class='button80' value='导出EXCEL'>"
	            submit="<input name='saveToDb' type='button' class='button80' value='保存'>"
				saveAsName="<%=reportSaveFile%>"  
				firstPageLabel="<input name='firstPage_btn' type='button' class='button80' value='首 页'>"
				prevPageLabel="<input name='prevPage_btn' type='button' class='button80' value='上一页'>"
				nextPageLabel="<input name='nextPage_btn' type='button' class='button80' value='下一页'>"
				lastPageLabel="<input name='lastPage_btn' type='button' class='button80' value='尾 页'>"
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
	        	window.Report1_importExcel=function(){alert("禁用此操作！");};
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
			//让正在编辑的输入框失去焦点
			if(document.getElementById("Report1_A14").value == "" || document.getElementById("Report1_A16").value == "" 
				|| document.getElementById("Report1_A18").value == "" || document.getElementById("Report1_A35").value == "") {
				alert("请将用信报告第2，3，4，8项填写完整");
				return false;
		       }
		    }
		    _submitReport(table);
		    return true;
	      }
	  
	</script>
</html>


	<%-- 
	  报表展现实例：
	         var form=document.getElementById('reportForm');
		     form.action="rqReport/reportShowPage.jsp";
		    // id2Form(form,'pageMark','no'); 分页开关
		     // id2Form(form,'pageWrite','no'); 可写报表开关 只对填报报表 控制保存按钮隐藏 当pageWrite=no时隐藏其他均显示
			 id2Form(form,'reportId','lqpLoanCom.raq');//报表模板名称 禁用中文
			 id2Form(form,'macro??',"select * from s_org where organno like '@20@' ");//报表宏SQL @代替% 名称以macro打头
			 
		     id2Form(form,'serno',IqpLoanAppList._obj.getParamValue(['serno']));
		   	 form.submit();
		   	 
          定义一个form专门用于展现报表提交
		   	 <form method="POST" action="" id="reportForm" target="_blank">
             </form>
	
	--%>
	
	
