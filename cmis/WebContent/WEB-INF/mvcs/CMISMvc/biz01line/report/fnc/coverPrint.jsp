<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String print_type = (String)context.getDataValue("print_type");
	String agr_no = "";
	String guaranty_no = "";
	String cont_no = "";
	String cus_id = "";
	String task_id = "";
	String agr_no1 = "";
	String cont_no1 = "";
	String cont_no2 = "";
	String prd_id = "";
	String serno="";
	String jural_flag="";
	/**add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
	String po_no="";
	/**add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doPrint0(){
		var print_type = '<%=print_type%>';	
		<%if(context.containsKey("guaranty_no")&& !"".equals(context.getDataValue("guaranty_no"))){
			 guaranty_no = (String)context.getDataValue("guaranty_no");}%>
		var guaranty_no = '<%=guaranty_no%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crlimitsinfo.raq&guaranty_no='+guaranty_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	//法律要件资料分为：单一法人/个人授信、集团授信、联保授信
	function doPrint1(){
		var recordCount = lmtJuralInfoList._obj.recordCount;//取总的记录数
		var print_type = '<%=print_type%>';
		<%if(context.containsKey("agr_no")&& !"".equals(context.getDataValue("agr_no"))){
				agr_no = (String)context.getDataValue("agr_no");}%>
		<%if(context.containsKey("jural_flag")&& !"".equals(context.getDataValue("jural_flag"))){
			jural_flag = (String)context.getDataValue("jural_flag");}%>
		var agr_no = '<%=agr_no%>';
		var jural_flag = '<%=jural_flag%>';
		var count = 0;
		var guarContNo ="";//担保合同编号字符串
		var limitCode ="";//授信额度编号字符串
		var guarId ="";//担保编号字符串
		for(var i=0;i<recordCount;i++){
			var box01 = lmtJuralInfoList._obj.data[i].box01._getValue();
			if(box01=="on"){
				var guar_cont_no = lmtJuralInfoList._obj.data[i].guar_cont_no._getValue();
				var limit_code = lmtJuralInfoList._obj.data[i].limit_code._getValue();
				var guar_id = lmtJuralInfoList._obj.data[i].guar_id._getValue();
				 count ++;
				 guarContNo += guar_cont_no +",";
				 limitCode += limit_code +",";
				 guarId += guar_id +",";
			}
		}
		if(count>0){                                                                                          
			if(jural_flag =="lmtapp"){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crjuralinfo4lmtapp.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}'+"&guar_cont_no="+guarContNo+"&limit_code="+limitCode+"&guar_id="+guarId;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else if(jural_flag == "lmtgrp"){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crjuralinfo4lmtapp.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}'+"&guar_cont_no="+guarContNo+"&limit_code="+limitCode+"&guar_id="+guarId;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else if(jural_flag == "lmtjoint"){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crjuralinfo4lmtjonit.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}'+"&guar_cont_no="+guarContNo+"&limit_code="+limitCode+"&guar_id="+guarId;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}
		}else{
			alert("请勾选需打印的保证担保法律资料信息！");
		}
	}

	function doPrint2(){
		<%if(context.containsKey("cont_no")&& !"".equals(context.getDataValue("cont_no"))){
			 cont_no = (String)context.getDataValue("cont_no");}%>
		var cont_no = '<%=cont_no%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crprdindivinfo.raq&cont_no='+cont_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	
	function doPrint3(){
		<%if(context.containsKey("cus_id")&& !"".equals(context.getDataValue("cus_id"))){
			 cus_id = (String)context.getDataValue("cus_id");}%>
		var cus_id = '<%=cus_id%>';
		var comm_cus_box = comm_cus._getValue();
		var guar_cus_box = guar_cus._getValue();
		if(comm_cus_box =="on" || guar_cus_box =="on"){
			if((comm_cus_box =="on" && guar_cus_box !="on") ||(comm_cus_box !="on" && guar_cus_box =="on")){
				var cus_properties ="";
				var cus_pro_name="";
				if(comm_cus_box =="on"){
					cus_properties ="1";
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crcusbaseinfo.raq&cus_id='+cus_id+'&manager_id=${context.currentUserId}&cus_properties='+cus_properties;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{
					cus_properties ="2";
					var url = '<emp:url action="getGuarantyInfo4CusBaseList.do"/>?cus_id='+cus_id+'&manager_id=${context.currentUserId}&cus_properties='+cus_properties;
					url = EMPTools.encodeURI(url);
					window.open(url,'cus_window','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}
			}else{
				alert("客户性质只允许勾选一个选项！");
			}
		}else{
			alert("请勾选该客户性质！");
		}	
	}
	
	function doPrint4(){	
		var recordCount = LmtAgrInfoList._obj.recordCount;//取总的记录数
		<%if(context.containsKey("agr_no")&& !"".equals(context.getDataValue("agr_no"))){
			 agr_no1 = (String)context.getDataValue("agr_no");}%>
		var agr_no = '<%=agr_no1%>';
		var count = 0;
		var limitCode ="";//合同编号字符串
		for(var i=0;i<recordCount;i++){
			var box04 = LmtAgrInfoList._obj.data[i].box04._getValue();
			if(box04=="on"){
				var limit_code = LmtAgrInfoList._obj.data[i].limit_code._getValue();
				 count ++;
				 limitCode += limit_code +",";
			}
		}
		if(count>0){                                                                                          
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crcreditinfo.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}'+"&limit_code="+limitCode;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
			alert("请勾选需打印的额度分项信息！");
		}
	}
	
	function doPrint5(){
		<%if(context.containsKey("cont_no")&& !"".equals(context.getDataValue("cont_no"))){
			 cont_no1 = (String)context.getDataValue("cont_no");}%>
		var cont_no = '<%=cont_no1%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/cragrinfo.raq&cont_no='+cont_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	
	function doPrint6(){		
		<%if(context.containsKey("task_id")&& !"".equals(context.getDataValue("task_id"))){
			  task_id = (String)context.getDataValue("task_id");}%>
		var task_id = '<%=task_id%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crpspinfo.raq&task_id='+task_id+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		
	}
	
	function doPrint7(){	
		<%if(context.containsKey("cont_no")&& !"".equals(context.getDataValue("cont_no"))){
			 cont_no2 = (String)context.getDataValue("cont_no");}%>
		var cont_no = '<%=cont_no2%>';
			<%prd_id = (String)context.getDataValue("prd_id");%>
		<%if(context.containsKey("serno")&& !"".equals(context.getDataValue("serno"))){
				serno = (String)context.getDataValue("serno");}%>
		var prd_id = '<%=prd_id%>';
		var serno = '<%=serno%>';
		if(prd_id=="200024"){
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/craccinfo.raq&cont_no='+cont_no+"&serno="+serno+"&manager_id=${context.currentUserId}";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
				alert('该产品不属于【银行承兑汇票】！');
		}	
	}

	function doPrint8(){
		<%if(context.containsKey("cus_id")&& !"".equals(context.getDataValue("cus_id"))){
			cus_id = (String)context.getDataValue("cus_id");}%>
		var cus_id = '<%=cus_id%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crprdindivinfo4cus.raq&cus_id='+cus_id+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}

	function doPrint9(){
		<%if(context.containsKey("agr_no")&& !"".equals(context.getDataValue("agr_no"))){
			agr_no = (String)context.getDataValue("agr_no");}%>
		var agr_no = '<%=agr_no%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crcreditinfo4coop.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	function doPrint10(){
		<%if(context.containsKey("agr_no")&& !"".equals(context.getDataValue("agr_no"))){
			agr_no = (String)context.getDataValue("agr_no");}%>
		var agr_no = '<%=agr_no%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crcreditinfo4indus.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}
	/**add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
	function doPrint11(){
		<%if(context.containsKey("agr_no")&& !"".equals(context.getDataValue("agr_no"))){
			agr_no = (String)context.getDataValue("agr_no");}%>
		var agr_no = '<%=agr_no%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crcreditinfo4bizarea.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}

	function doPrint12(){
		<%if(context.containsKey("agr_no")&& !"".equals(context.getDataValue("agr_no"))){
			agr_no = (String)context.getDataValue("agr_no");}%>
		var agr_no = '<%=agr_no%>';
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crprdindivinfo4lmt.raq&agr_no='+agr_no+'&manager_id=${context.currentUserId}';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}

	//该功能仅针对于业务关联的一半担保/最高保额担保合同封面打印
	function doPrint13(){
		var recordCount = lmtJuralInfo4ClcList._obj.recordCount;//取总的记录数
		var print_type = '<%=print_type%>';
		<%if(context.containsKey("cont_no")&& !"".equals(context.getDataValue("cont_no"))){
			cont_no = (String)context.getDataValue("cont_no");}%>

		var cont_no = '<%=cont_no%>';
		var count = 0;
		var guarContNo ="";//担保合同编号字符串
		var guarId ="";//担保编号字符串
		for(var i=0;i<recordCount;i++){
			var box13 = lmtJuralInfo4ClcList._obj.data[i].box13._getValue();
			if(box13=="on"){
				var guar_cont_no = lmtJuralInfo4ClcList._obj.data[i].guar_cont_no._getValue();
				var guar_id = lmtJuralInfo4ClcList._obj.data[i].guar_id._getValue();
				 count ++;
				 guarContNo += guar_cont_no +",";
				 guarId += guar_id +",";
			}
		}
		if(count>0){                                                                                          
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crjuralinfo4clc.raq&cont_no='+cont_no+'&manager_id=${context.currentUserId}'+"&guar_cont_no="+guarContNo+"&guar_id="+guarId;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
			alert("请勾选需打印的保证担保法律资料信息！");
		}
	}

	function doPrint14(){
		var recordCount = iqpPoolManaInfoList._obj.recordCount;//取总的记录数
		var print_type = '<%=print_type%>';
		<%if(context.containsKey("po_no")&& !"".equals(context.getDataValue("po_no"))){
			po_no = (String)context.getDataValue("po_no");}%>

		var po_no = '<%=po_no%>';
		var count = 0;
		var guarContNo ="";//担保合同编号字符串
		for(var i=0;i<recordCount;i++){
			var box14 = iqpPoolManaInfoList._obj.data[i].box14._getValue();
			if(box14=="on"){
				var guar_cont_no = iqpPoolManaInfoList._obj.data[i].guar_cont_no._getValue();
				 count ++;
				 guarContNo += guar_cont_no +",";

			}
		}
		//if(count>0){
			if(count ==0 || count ==1){                                                                                      
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=coverReport/crpoolmanainfo.raq&po_no='+po_no+'&manager_id=${context.currentUserId}'+"&guar_cont_no="+guarContNo;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert("担保合同不允许多选！");
			}
		//}else{
		//	alert("请勾选需打印的保理池/应收账款池/票据池资料信息！");
		//}
	}
	/**add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
</script>
</head>
<body class="page_content" >	
		<div class='emp_gridlayout_title'>信贷业务纸质档案案卷封面打印</div>
		<div  id='CoverReportPrintButtonGroup' class='emp_group_div'>
			<%if(print_type.indexOf("00")>=0){ %>
			<emp:button id="print0" label="打印" />
			 抵（质）押物资料封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("01")>=0){ %>
			<emp:button id="print1" label="打印" />
			保证担保法律要件资料封面打印（注意：根据实际的纸质档案内容进行勾选，对应的担保合同编号关联的保证信息将一并打印）
			<br/>
			<emp:table icollName="lmtJuralInfoList" pageMode="false" url=""> 
		      <emp:checkbox id="box01" label="选择" flat="false" />
		      <emp:text id="limit_code" label="授信额度编号" />
		      <emp:text id="guar_cont_no" label="担保合同编号" />
		      <emp:text id="guar_cont_cn_no" label="中文合同编号" />
		      <emp:text id="guar_id" label="担保编号" hidden="true"/>
		      <emp:text id="cus_name" label="保证人名称" />
		      <emp:text id="guar_amt" label="担保金额" dataType="Currency" />
		      <emp:text id="guar_start_date" label="担保起始日" hidden="true"/>
		      <emp:text id="guar_end_date" label="担保终止日" hidden="true"/>
	        </emp:table>
			    <%} %>
			<%if(print_type.indexOf("02")>=0){ 
			%>			
			<emp:button id="print2" label="打印" />
			品种个性化资料封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("03")>=0){ %>
			请选择该客户性质：
			<br/>
			<emp:checkbox id="comm_cus" label="普通客户" flat="false"/>普通客户
			<emp:checkbox id="guar_cus" label="担保客户" flat="false"/>担保客户
			<br/>
			<emp:button id="print3" label="打印" />
			纯基础资料封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("04")>=0){ %>
			<emp:button id="print4" label="打印" />
			授信资料封面打印（请勾选下列需打印的额度分项信息）
			<br/>
			<emp:table icollName="LmtAgrInfoList" pageMode="false" url=""> 
		      <emp:checkbox id="box04" label="选择" flat="false" />
		      <emp:text id="limit_code" label="授信额度编号" />
		      <emp:text id="cus_name" label="客户名称" />
		      <emp:text id="crd_amt" label="授信金额" dataType="Currency" />
		      <emp:text id="prd_name" label="额度品种名称" />
		      <emp:text id="guar_type" label="担保方式" />
	        </emp:table>
				<%} %>
			<%if(print_type.indexOf("05")>=0){ %>
			<emp:button id="print5" label="打印" />
			用信资料封面打印
			<br/>
				<%} %>
		    <%if(print_type.indexOf("06")>=0){ %>
			<emp:button id="print6" label="打印" />
			贷后资料封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("07")>=0){ %>
			<emp:button id="print7" label="打印" />
			发票资料封面打印
			<br/>
				<%} %>	
			<%if(print_type.indexOf("08")>=0){ %>
			<emp:button id="print8" label="打印" />
			品种个性化资料（客户信息）封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("09")>=0){ %>
			<emp:button id="print9" label="打印" />
			授信资料（合作方）封面打印
			<br/>
				<%} %>		
			<%if(print_type.indexOf("10")>=0){ %>
			<emp:button id="print10" label="打印" />
			授信资料（行业）封面打印
			<br/>
				<%} %>
			<!-- add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin -->	
			<%if(print_type.indexOf("11")>=0){ %>
			<emp:button id="print11" label="打印" />
			授信资料（圈商）封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("12")>=0){ %>
			<emp:button id="print12" label="打印" />
			品种个性化资料（协议管理）封面打印
			<br/>
				<%} %>
			<%if(print_type.indexOf("13")>=0){ %>
			<emp:button id="print13" label="打印" />
			保证担保法律要件资料（担保合同）封面打印（注意：根据实际的纸质档案内容进行勾选，该功能针对业务关联的一般担保/最高保额担保合同打印）
			<br/>
			<emp:table icollName="lmtJuralInfo4ClcList" pageMode="false" url=""> 
		      <emp:checkbox id="box13" label="选择" flat="false" />
		      <emp:text id="guar_cont_no" label="担保合同编号" />
		      <emp:text id="guar_cont_cn_no" label="中文合同编号" />
		      <emp:text id="guar_id" label="保证编号" />
		      <emp:text id="cus_name" label="保证人名称" />
		      <emp:text id="guar_amt" label="担保金额" dataType="Currency" />
		      <emp:text id="guar_start_date" label="担保起始日" hidden="true"/>
		      <emp:text id="guar_end_date" label="担保终止日" hidden="true"/>
	        </emp:table>
				<%} %>
				
			<%if(print_type.indexOf("14")>=0){ %>
			<emp:button id="print14" label="打印" />
			保理池/应收账款池/票据池资料封面打印（注意：根据实际的纸质档案内容进行勾选,对担保合同的操作为可选或者单选）
			<br/>
			<emp:table icollName="iqpPoolManaInfoList" pageMode="false" url=""> 
		      <emp:checkbox id="box14" label="选择" flat="false" />
		      <emp:text id="guar_cont_no" label="担保合同编号" />
		      <emp:text id="guar_cont_cn_no" label="中文合同编号" />
		      <emp:text id="amt" label="债权总金额/在池票面总金额" dataType="Currency" />
		      <emp:text id="guar_start_date" label="担保起始日" hidden="false"/>
		      <emp:text id="guar_end_date" label="担保终止日" hidden="false"/>
	        </emp:table>
				<%} %>
			<!-- add by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end -->		
		</div>
		
</body>
</html>
</emp:page>
