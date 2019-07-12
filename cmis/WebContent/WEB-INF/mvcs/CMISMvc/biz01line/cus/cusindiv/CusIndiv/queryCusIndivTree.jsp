<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>

<link href="<emp:file fileName='styles/default/common.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/dataField.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3_menuframe.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/moneystyle.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/pageUtil.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataType.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataField.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dataGroup.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/relatedTabs.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/ImageViewControl.js'/>" type="text/javascript" language="javascript"></script>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<!-- <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" />-->


<%
	String cus_id=request.getParameter("cus_id");
	String btnflag = request.getParameter("btnflag");
	String flag = request.getParameter("flag");
	if (!"edit".equals(flag)){
		flag="query";
	}else{
		flag="edit";
	}
	
/*	HttpSession ssion = request.getSession();
	ssion.removeAttribute("buttonFlag");
	ssion.setAttribute("buttonFlag",flag);*/
/*	String openTmp = request.getParameter("openTmp");
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
	if(cus_id == null || cus_id.equals("") || cus_id.length()==0){
		cus_id = (String)context.getDataValue("cusId");
	}
%>
<emp:page>
<html>
<head>
<title>个人客户[<%=cus_id%>]综合信息页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	var menuOpTree;
	function createTree(){
		var treename = "menuOpTree";
		var d;
		var flag = "<%=flag%>";
		var btnflag = "<%=btnflag%>";
		var paramStr="cus_id=<%=cus_id%>";
		var paramStrSubSocRel = "CusIndivSocRel.cus_id=<%=cus_id%>&EditFlag="+flag;
		var famLbyParamStrSub = "CusIndivFamLby.cus_id=<%=cus_id%>&EditFlag="+flag;
		var taxParamStrSub = "CusIndivTax.cus_id=<%=cus_id%>&EditFlag="+flag;
		var bondParamStrSub = "CusIndivBond.cus_id=<%=cus_id%>&EditFlag="+flag;
		var insurancesParamStrSub = "CusIndivInsu.cus_id=<%=cus_id%>&EditFlag="+flag;
		var indivInvtEnterpriseParamStrSub = "CusIndivInvtEnt.cus_id=<%=cus_id%>";
		var indivIncomeParamStrSub = "CusIndivIncome.cus_id=<%=cus_id%>&EditFlag="+flag;
		var indivAssParamStrSub = "CusIndivAss.cus_id=<%=cus_id%>&EditFlag="+flag;
		var socialProtectionStrSub = "CusSocialProtection.cus_id=<%=cus_id%>&EditFlag="+flag;
		var GrtURL= '<emp:url action="mortGuarantyBaseByCusId.do"/>&'+"cus_id=<%=cus_id%>";
	
		//var baseURL = '<emp:url action="getCusIndivViewPage.do"/>&'+paramStr+"&info=tree&flag=query";
		<%if("edit".equals(flag)){%>
		var baseURL = '<emp:url action="getCusIndivUpdatePage.do"/>&'+paramStr+"&info=tree&flag="+flag+"&treeflag=infotree&repBtn=no&btnflag="+btnflag;
		<%}else{%>
		var baseURL = '<emp:url action="getCusIndivViewPage.do"/>&'+paramStr+"&info=tree&flag="+flag+"&treeflag=infotree&repBtn=no&btnflag="+btnflag;
		<%}%>
		
		var socRelURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStrSubSocRel;
		var famLbyURL = '<emp:url action="queryCusIndivFamLbyList.do"/>&'+famLbyParamStrSub;
		var taxURL = '<emp:url action="queryCusIndivTaxList.do"/>&'+taxParamStrSub;
		var bondURL = '<emp:url action="queryCusIndivBondList.do"/>&'+bondParamStrSub;
		var insurancesURL = '<emp:url action="queryCusIndivInsurancesList.do"/>&'+insurancesParamStrSub;
		var indivInvtEnterpriseURL = '<emp:url action="queryCusIndivInvtEntList.do"/>&'+indivInvtEnterpriseParamStrSub;
		var indivIncomeURL = '<emp:url action="queryCusIndivIncomeList.do"/>&'+indivIncomeParamStrSub;
		var indivAssURL = '<emp:url action="queryCusIndivAssList.do"/>&'+indivAssParamStrSub;
	
		var socialProtectionURL = '<emp:url action="queryCusSocialProtectionList.do"/>&'+socialProtectionStrSub;
	
		//我行交易信息
		var deptInItemURL = '<emp:url action="queryCusDeptInItemList.do"/>&'+"CusDeptInItem.cus_id=<%=cus_id%>&EditFlag="+flag;
		//他行交易信息信息
		var obisDepositURL = '<emp:url action="queryCusObisDepositList.do"/>&'+"CusObisDeposit.cus_id=<%=cus_id%>&EditFlag="+flag;
		var obisLoanURL = '<emp:url action="queryCusObisLoanList.do"/>&'+"CusObisLoan.cus_id=<%=cus_id%>&EditFlag="+flag;
		var obisAssureURL = '<emp:url action="queryCusObisAssureList.do"/>&'+"CusObisAssure.cus_id=<%=cus_id%>&EditFlag="+flag;
	
		var cusEventURL = '<emp:url action="queryCusEventList.do"/>&'+"CusEvent.cus_id=<%=cus_id%>&EditFlag="+flag;
	
		var holdFundURL = '<emp:url action="queryCusHoldFundList.do"/>&'+"CusHoldFund.cus_id=<%=cus_id%>&EditFlag="+flag;
		var holdChrematisticURL = '<emp:url action="queryCusHoldChremList.do"/>&'+"CusHoldChrem.cus_id=<%=cus_id%>&EditFlag="+flag;
		var cusCertInfoURL = '<emp:url action="queryCusCertInfoList.do"/>&'+"CusCertInfo.cus_id=<%=cus_id%>&EditFlag="+flag;
		
	    //对外投资(和对公一样)
		var relInvestURL = '<emp:url action="queryCusComRelInvestList.do"/>&'+"CusComRelInvest.cus_id="+"<%=cus_id%>&EditFlag="+flag;

		//智能关联搜索
		var realCusURL = '<emp:url action="GetIndivCusRelTreeOp.do"/>&cus_id='+"<%=cus_id%>"+"&cusType=indiv";
		
		var clURL = '<emp:url action="getAccViewByCusId.do"/>&cus_id='+"<%=cus_id%>"+"&cusType=indiv";
		var cusComAccURL = '<emp:url action="queryCusComAccList.do"/>&'+"CusComAcc.cus_id=<%=cus_id%>&EditFlag=<%=flag%>";
		//客户授信存量情况
		var  cusLmtURL= '<emp:url action="queryLmtAgrIndivListForCusTree.do"/>&'+"cus_id=<%=cus_id%>&op=view&type=cusTree";
		//授信否决历史
		var cusOverruleURL = '<emp:url action="queryLmtAppIndivListForCusTree.do"/>&'+"cus_id=<%=cus_id%>&op=view&type=cusTree";
		/*zhoujf 20090603 end*/
	
		try {
		d = new dTree(treename);

         d.add(0,-1,'个人客户综合信息');
		 d.add(1,0,'客户概况');
		 d.add(2,1,'客户基础信息',baseURL,'','rightframe');
		 d.add(4,1,'税费缴纳情况',taxURL,'','rightframe');
		 d.add(5,1,'持有资本证券信息',bondURL,'','rightframe');
		 d.add(6,1,'持有保险信息',insurancesURL,'','rightframe');
		 d.add(9,1,'社会保障信息',socialProtectionURL,'','rightframe');
		 d.add(7,1,'个人收入信息',indivIncomeURL,'','rightframe');
		 d.add(8,1,'客户重大事件',cusEventURL,'','rightframe');
		 d.add(10,1,'持有基金',holdFundURL,'','rightframe');
		 d.add(11,1,'持有理财',holdChrematisticURL,'','rightframe');
		 d.add(12,1,'其他证件信息',cusCertInfoURL,'','rightframe');
		 
		 d.add(100,0,'客户关联信息');
		 d.add(10010,100,'社会关系信息',socRelURL,'','rightframe');
		 
		 d.add(10011,100,'个人对外投资信息',relInvestURL,'','rightframe');
		 d.add(10012,100,'智能客户关联信息',realCusURL,'','rightframe');
		 
		 d.add(200,0,'客户家庭信息');
		 d.add(20020,200,'家庭资产信息',indivAssURL,'','rightframe');
		 d.add(20030,200,'家庭负债信息',famLbyURL,'','rightframe');
		 
		 d.add(300,0,'他行交易信息');
		 d.add(30010,300,'他行存款信息',obisDepositURL,'','rightframe');
		 d.add(30020,300,'他行贷款信息',obisLoanURL,'','rightframe');
		 d.add(30030,300,'他行担保信息',obisAssureURL,'','rightframe');
		 /* add by zhoujf 20090613 客户业务*/
		 d.add(400,0,'我行业务信息');
		 d.add(40010,400,'存款',deptInItemURL,'','rightframe');
		 d.add(40020,400,'存量台账',clURL,'','rightframe')
	     d.add(40030,400,'担保情况',GrtURL,'','rightframe');
		 d.add(40040,400,'客户我行账户登记簿',cusComAccURL,'','rightframe');
		 d.add(40050,400,'存量授信',cusLmtURL,'','rightframe');
		 d.add(40060,400,'授信否决历史',cusOverruleURL,'','rightframe');
		 /* zhoujf 20090613 end*/
	} catch (e) {alert(e)};
		if(window.opener){
			var ctrlLink = "<br/><a href='javascript: "+treename
				+".openAll();'>展开所有</a> | <a href='javascript: "
				+treename+".closeAll();'>关闭所有</a><br/><br/>| "
				+"<a href='javascript:ImageView();'>影像查看</a><br/><br/>";
			document.getElementById("Page_left_detail_page").innerHTML = ctrlLink+d.toString();
			menuOpTree=d;
		}else{
			var ctrlLink = "<br/><a href='javascript: "+treename
				+".openAll();'>展开所有</a> | <a href='javascript: "
				+treename+".closeAll();'>关闭所有</a><br/><br/>";				
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
		data['image_action'] = 'View23'	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
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
<body onload="createTree()" bgcolor="#F8F7F7">
<div id="Page_left_detail_page"></div>
<div id="image" style="display: none;"><span id="ocr" style="HEIGHT: 250; POSITION: absolute; TOP: 300; WIDTH: 162; LEFT: 0;">
	<input type="hidden" name="imageType" id= "imageType"/>
	<OBJECT ID="TreePadControl1" WIDTH=100% HEIGHT=100%
		CLASSID="CLSID:779745CF-F32D-4be9-BE90-E0D32F13000C" VIEWASTEXT>
		<PARAM NAME="Location" VALUE="6, 6">
		<PARAM NAME="Name" VALUE="ControlAxSourcingSite">
		<PARAM NAME="TabIndex" VALUE="0">
	</OBJECT> 
	<script language="javascript" type="text/javascript">
		function TreePadControl1::ComDocumentRequested(comDoc){
			new ImageViewControl();
			DocPadControl1.LoadComDocument(comDoc);
	    }
		function TreePadControl1::ScanButtonClick(){
			var url = "ImageScan.jsp?type="+imageType.value+"&date="+new Date();
			window.showModalDialog(url,"","dialogHeight:10000px;dialogWidth:10000px;");
		}
	</script> </span></div>
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
	<div id=Page_right style="width:79%;">
	<%if("edit".equals(flag)){%>
		<iframe id="rightframe" name="rightframe" src="<emp:url action="getCusIndivUpdatePage.do"/>&cus_id=<%=cus_id%>&btnflag=<%=btnflag%>&info=tree&flag=<%=flag %>" frameborder="0" scrolling="auto" height="538px" width="100%">
		</iframe>
	<%}else{ %>
		<iframe id="rightframe" name="rightframe" src="<emp:url action="getCusIndivViewPage.do"/>&cus_id=<%=cus_id%>&btnflag=<%=btnflag%>&info=tree&flag=<%=flag %>" frameborder="0" scrolling="auto" height="538px" width="100%">
		</iframe>
	<%} %>
	</div>
</body>
</html>
</emp:page>