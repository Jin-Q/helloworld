<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>

<link href="<emp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/dataField.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3_menuframe.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/moneystyle.css'/>" rel="stylesheet" type="text/css" />
<style>
.emp_field .emp_field_textarea_textarea{width:60%;}

</style>
<script src="<emp:file fileName='scripts/pageUtil.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataType.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataField.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataGroup.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/relatedTabs.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/ImageViewControl.js'/>" type="text/javascript" language="javascript"></script>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<!-- <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" /> -->

<%
	String cus_id = request.getParameter("cus_id");
	String oper = request.getParameter("oper");
	String flag = request.getParameter("flag");
	String btnflag = request.getParameter("btnflag");
	if (!"edit".equals(flag)) {
		flag = "query";
	} else {
		flag = "edit";
	}
/*	HttpSession ssion = request.getSession();
	ssion.removeAttribute("buttonFlag");
	ssion.setAttribute("buttonFlag",flag);
	String openTmp = request.getParameter("openTmp");
	if(openTmp!=null&&!"".equals(openTmp)&&"true".equals(openTmp)){
		ssion.removeAttribute("buttonFlag");
		ssion.setAttribute("buttonFlag","edit");
	}else{
		ssion.removeAttribute("buttonFlag");
		ssion.setAttribute("buttonFlag",flag);
	}*/

	//add by loujc for 参数从request中获取不到从context中再取一把
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	if (cus_id == null || cus_id.equals("")|| cus_id.length() == 0) {
		cus_id = (String) context.getDataValue("cus_id");
	}
%>
<emp:page>
<html>
<head>
<title>对公客户[<%=cus_id%>]综合信息页面</title>

<jsp:include page="/include.jsp" flush="true" />
<script type="text/javascript">
	var menuOpTree;
	function createTree(){
		var treename = "menuOpTree";
		var d;
		var oper = "<%=oper%>";
		var flag = "<%=flag%>";
		var btnflag = "<%=btnflag%>";
		//基本信息
		<%if("query".equals(flag)){%>
		var baseURL = '<emp:url action="getCusComViewPage.do"/>&cus_id='+"<%=cus_id%>"+"&info=tree&flag="+flag+"&btnflag="+btnflag;
		<%}else{%>
		var baseURL = '<emp:url action="getCusComUpdatePage.do"/>&cus_id='+"<%=cus_id%>"+"&info=tree&flag="+flag+"&btnflag="+btnflag;
		<%}%>
		var managerURL = '<emp:url action="queryCusComManagerList.do"/>&'+"CusComManager.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var aptitudeURL = '<emp:url action="queryCusComAptitudeList.do"/>&'+"CusComAptitude.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var stockURL = '<emp:url action="queryCusComFinaStockList.do"/>&'+"CusComFinaStock.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var bondURL = '<emp:url action="queryCusComFinaBondList.do"/>&'+"CusComFinaBond.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var contURL = '<emp:url action="queryCusComContList.do"/>&'+"CusComCont.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var mngFamilyURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+"cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var ressetURL = '<emp:url action="queryCusComRessetList.do"/>&'+"CusComResset.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		//客户的存量业务
		var clURL = '<emp:url action="getAccViewByCusId.do"/>&cus_id='+"<%=cus_id%>"+"&cusType=com";
	    //客户关系信息
		var relInvestURL = '<emp:url action="queryCusComRelInvestList.do"/>&'+"CusComRelInvest.cus_id="+"<%=cus_id%>&EditFlag=<%=flag%>";
		var relApitalURL = '<emp:url action="queryCusComRelApitalList.do"/>&'+"CusComRelApital.cus_id="+"<%=cus_id%>&EditFlag=<%=flag%>";
		//我行交易信息
		var deptInItemURL = '<emp:url action="queryCusDeptInItemList.do"/>&'+"CusDeptInItem.cus_id=<%=cus_id%>";
		//他行交易信息信息
		var obisDepositURL = '<emp:url action="queryCusObisDepositList.do"/>&'+"CusObisDeposit.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var obisLoanURL = '<emp:url action="queryCusObisLoanList.do"/>&'+"CusObisLoan.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var obisAssureURL = '<emp:url action="queryCusObisAssureList.do"/>&'+"CusObisAssure.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		//重大事件
		var cusEventURL = '<emp:url action="queryCusEventList.do"/>&'+"CusEvent.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
	
		//财务信息
		var fncStatBaseURL = '<emp:url action="queryFncStatBaseList.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var fncStatBaseSmpURL = '<emp:url action="queryFncStatBaseSmpList.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>&EditFlag=<%=flag%>&isSmp=isSmp";
		
//		var fncCusReportDetailURL = '<emp:url action="queryFncDetailBaseList.do"/>&'+"FncDetailBase.cus_id=<%=cus_id%>";
		//财务分析
		var fncCusAnlyURL = '<emp:url action="getCusFnaAnly.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>";
		
		//关联客户
		var cusComRelURL = '<emp:url action="GetCusRelTreeOp.do"/>&cus_id='+"<%=cus_id%>";
	    //所在集团信息
	    var relGroupInfoURL = '<emp:url action="queryCusGrpInfoListForRel.do"/>&'+"CusGrpInfo.cus_id=<%=cus_id%>";
	  	//我行业务 add by zhoujf 2009.07.02
		var cusComAccURL = '<emp:url action="queryCusComAccList.do"/>&'+"CusComAcc.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";

		/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  begin**/
		//客户授信存量情况
		var  cusLmtURL= '<emp:url action="queryLmtAgrInfoList.do"/>&'+"cus_id=<%=cus_id%>&menuId=crd_agr&isShow=view&op=view&printFlag=no";
		/**modified by lisj 2015-7-17  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end**/
		
		//授信否决历史
		var cusOverruleURL = '<emp:url action="queryLmtApplyList.do"/>&'+"cus_id=<%=cus_id%>&type=cusHis&menuId=corp_crd_query";
       /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
		//集团（关联）授信审批历史
		var cusLmtGrpApplyURl='<emp:url action="queryLmtGrpApplyList.do"/>&'+"&cus_id=<%=cus_id%>&type=cusHis&menuId=grp_crd_query";
		//联保授信审批历史
		var cusLmtAppJointCoopURL='<emp:url action="queryLmtAppJointCoop_jointList.do"/>&'+"&cus_id=<%=cus_id%>&type=cusHis&menuId=unit_team_crd_query";
		/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
		//国业授信情况
		var GrtURL= '<emp:url action="mortGuarantyBaseByCusId.do"/>&'+"cus_id=<%=cus_id%>";
		
		//辅助分析
		var subjectAnaly = '<emp:url action="subjectAnaly.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var dupontAnaly = '<emp:url action="dupontAnaly.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var finaRateAnaly = '<emp:url action="finaRateAnaly.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		var struAnaly = '<emp:url action="struAnaly.do"/>&'+"FncStatBase.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
        
		//关联企业客户信息
		var relClientURL = '<emp:url action="queryCusCliRelList.do"/>&'+"CusCliRel.cus_id="+"<%=cus_id%>&EditFlag=<%=flag%>";

		try {
			d = new dTree(treename);
	         d.add(0,-1,'对公客户综合信息');
			 d.add(1,0,'客户概况');
			 d.add(2,1,'客户基础信息',baseURL,'','rightframe');
			 
			 d.add(4,1,'客户资质信息',aptitudeURL,'','rightframe');
			 d.add(5,1,'发行股票信息',stockURL,'','rightframe');
			 d.add(6,1,'发行债券信息',bondURL,'','rightframe');
			 d.add(7,1,'客户联系信息',contURL,'','rightframe');
			 d.add(8,1,'固定资产信息',ressetURL,'','rightframe');
			 d.add(9,1,'客户重大事件',cusEventURL,'','rightframe');
			
			 d.add(10,0,'客户关联信息');
			 d.add(11,10,'高管人员信息',managerURL,'','rightframe');
			 d.add(12,10,'法人家庭成员',mngFamilyURL,'','rightframe');
			 d.add(14,10,'对外投资信息',relInvestURL,'','rightframe');
			 d.add(15,10,'资本构成信息',relApitalURL,'','rightframe');
			 d.add(17,10,'关联企业客户信息',relClientURL,'','rightframe');
			 
			 d.add(16,10,'所在关联集团信息',relGroupInfoURL,'','rightframe');
			 
			 d.add(19,10,'关联关系智能搜索',cusComRelURL,'','rightframe');
			 
			 d.add(20,0,'客户财务信息');
				d.add(21,20,'财务报表信息',fncStatBaseURL,'','rightframe');
				d.add(22,20,'财务简表信息',fncStatBaseSmpURL,'','rightframe');
			 d.add(28,0,'辅助分析');
			 	d.add(281,28,'财务指标分析',subjectAnaly,'','rightframe');
			 	d.add(282,28,'杜邦财务分析',dupontAnaly,'','rightframe');
			 	d.add(283,28,'财务比率分析',finaRateAnaly,'','rightframe');
			 	d.add(284,28,'结构分析',struAnaly,'','rightframe');
			 	
			 //d.add(22,20,'检查情况表',fncCusComCheckURL,'','rightframe');
			 //d.add(22,20,'客户报表明细',fncCusReportDetailURL,'','rightframe');
			 //d.add(23,20,'财务报表分析',fncCusAnlyURL,'','rightframe');
			 
			 /*add by zhoujf 20090612 start*/
			 d.add(24,0,'我行业务信息');
			 	d.add("lmtAgr",24,'存量授信',cusLmtURL,'','rightframe');
			 	d.add("lmtHis",24,'授信否决历史',cusOverruleURL,'','rightframe');
				/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
				d.add("lmtAppJointCoopCusHis",24,'联保授信审批历史',cusLmtAppJointCoopURL,'','rightframe');
			 	d.add("lmtGrpCusHis",24,'集团（关联）授信审批历史',cusLmtGrpApplyURl,'','rightframe');
			 	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
				d.add(25,24,'我行存款',deptInItemURL,'','rightframe');
			 	d.add(26,24,'存量台账',clURL,'','rightframe')
		    	d.add(29,24,'担保情况',GrtURL,'','rightframe');
		     
			 d.add(30,24,'客户我行账户登记簿',cusComAccURL,'','rightframe');
			 
			 /*zhoujf 20090612 end*/
			 d.add(70,0,'他行交易信息');
				 d.add(71,70,'他行存款信息',obisDepositURL,'','rightframe');
				 d.add(72,70,'他行贷款余额信息',obisLoanURL,'','rightframe');
				 d.add(73,70,'他行担保信息',obisAssureURL,'','rightframe');
		} catch (e) {alert(e)};
			if(!window.opener)
			{
				var ctrlLink = "<br/><a href='javascript: "+treename
					+".openAll();'>展开所有</a> | <a href='javascript: "
					+treename+".closeAll();'>关闭所有</a><br/><br/>";
				document.getElementById("Page_left_detail_page").innerHTML = ctrlLink+d.toString();
				menuOpTree=d;
			}else{
				var ctrlLink = "<br/><a href='javascript: "+treename
					+".openAll();'>展开所有</a> | <a href='javascript: "
					+treename+".closeAll();'>关闭所有</a><br/><br/>| "
					+"<a href='javascript:ImageView();'>影像查看</a><br/><br/>";
				document.getElementById("Page_left_detail_page").innerHTML = ctrlLink+d.toString();
				menuOpTree=d;
			}
	 };

		function ImageView(){	//影像查看
			var data = new Array();
			data['serno'] = "<%=cus_id%>";	//客户资料的业务编号就填cus_id
			data['cus_id'] = "<%=cus_id%>";	//客户编号
			data['prd_id'] = 'BASIC';	//业务品种
			data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
			doPubImageAction(data);
		};
		
	function moveLeftFrame(cssleft,cssright,yqti,slide,alt,obj) {
		var yq = document.getElementById(yqti);
		var sl = document.getElementById(slide);
		var alrt = document.getElementById(alt);
		
		if(yq.style.display=='none'){
			yq.style.display='inline';
			alrt.alt='点击隐藏';
			sl.className=cssleft;		
			obj.style.width="79%";		
		} else {
			obj.style.width="98.7%";
			yq.style.display='none';
			alrt.alt='点击显示';
			sl.className=cssright;
		}
	};

/*	function goBackList(){
		var url = '<emp:url action="queryCusComList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}*/

/*	window.onbeforeunload = function() {    
		var flag = "<%=flag%>";
		if(flag == "edit"){
			 if(window.opener){
			 	// window.opener.location.reload();
			 }else{
				// window.parent.location.reload();
			 }
		 } else{   
		 	//alert("是刷新而非关闭");    
		 }   
	} */
</script>
	</head>
	<body onload="createTree()" bgcolor="#F8F7F7" style="width:1024px;overflow:auto;">

	<div id="Page_left_detail_page"></div>
	<div id="image" style="display: none;"></div>
	<div id="Page_middle">
	<table width="2px" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td height="100%" valign="top">
			<div id="slide" class="slide_l"
				onclick="moveLeftFrame('slide_l','slide_r','Page_left_detail_page','slide','alt_slide',getElementById('Page_right'))">
			<img id="alt_slide" src="images/default/space.gif" width="10"
				height="100" alt="点击隐藏" /></div>
			</td>
		</tr>
	</table>
	</div>
	<div id="Page_right" style="width: 79%;">
	<%if("query".equals(flag)){%>
	<iframe id="rightframe" name="rightframe" src="<emp:url action="getCusComViewPage.do"/>&flag=<%=flag%>&cus_id=<%=cus_id%>&info=tree&oper=infotree&btnflag=<%=btnflag%>"
		 frameborder="0" scrolling="auto" height="538px" width="100%">
	</iframe>
	<%}else{ %>
	<iframe id="rightframe" name="rightframe" src="<emp:url action="getCusComUpdatePage.do"/>&flag=<%=flag%>&cus_id=<%=cus_id%>&info=tree&oper=infotree&btnflag=<%=btnflag%>"
		 frameborder="0" scrolling="auto" height="538px" width="100%">
	</iframe>
	<%} %>
	</div>
	</body>
	</html>
</emp:page>