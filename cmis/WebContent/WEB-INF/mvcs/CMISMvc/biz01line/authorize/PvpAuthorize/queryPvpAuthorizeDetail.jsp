<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String biz_type="";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	}
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		if('<%=flag%>'=="authorize"){
        	var url = '<emp:url action="queryPvpAuthorizeList.do"/>?menuId=queryPvpAuthorizeList&biz_type='+'<%=biz_type%>';
        }else{
        	var url = '<emp:url action="queryPvpAuthorizeHistoryList.do"/>?menuId=queryPvpAuthorizeHistoryList&biz_type='+'<%=biz_type%>'; 
        }
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function getCusForm(){ 
		var cus_id = PvpAuthorize.cus_id._getValue();
		var prd_id = PvpAuthorize.prd_id._getValue()
		var url = "";
		if(prd_id == "600020"||prd_id == "300022"||prd_id == "300023"||prd_id == "300024"){//同业客户
			url= "<emp:url action='getCusSameOrgViewPage.do'/>&type=cusSame&cus_id="+cus_id+"&op=view&restrict_tab=Y&pvp_auth=Y"; 
		}else{
			url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		}
		url=EMPTools.encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function getCont(){
		var cont_no = PvpAuthorize.cont_no._getValue();
		var prd_id = PvpAuthorize.prd_id._getValue();
		var tran_id = PvpAuthorize.tran_id._getValue();	//交易码，展期授权要先判断交易码
		var url;
		if(tran_id == '0200200001102'){	//展期授权
			url = '<emp:url action="getIqpExtensionAgrViewPage.do"/>?agr_no='+cont_no+"&menuId=queryCtrAssetstrsfHistoryList&hidden_button=true&restrictUsed=false";
		}else{
			if(prd_id == "600020"){//资产转受让业务
				url = '<emp:url action="getCtrAssetstrsfContViewPage.do"/>?cont_no='+cont_no+"&menuId=queryCtrAssetstrsfHistoryList&op=view&pvp=pvp";
			}else if(prd_id == "600021"){
				url = '<emp:url action="getCtrAssetTransContViewPage.do"/>?cont_no='+cont_no+"&menuId=ZCLZHTCX&op=view&isHistory=history&flag=notHave";
			}else if(prd_id == "600022"){
				url = '<emp:url action="getCtrAssetProContViewPage.do"/>?cont_no='+cont_no+"&menuIdTab=dqzczqhxm&op=view&isHistory=history&flag=notHave";
			}else if(prd_id == "300022" || prd_id == "300024" || prd_id == "300023"){//转帖
				url = '<emp:url action="getCtrRpddscntContViewPage.do"/>?cont_no='+cont_no+"&menuId=queryCtrRpddscntContHistoryList&op=view&pvp=pvp";
		    }else if('${context.biz_type}'== "8" && (prd_id == "300021" || prd_id == "300020")){
		    	url='<emp:url action="getCtrLoanContForDiscViewPage.do"/>?cont_no='+cont_no+'&cont=cont&flag=ctrLoanCont&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp';
		    }else if('${context.biz_type}'== "8" && prd_id != "300021" && prd_id != "300020"){
		    	url='<emp:url action="getCtrLoanContViewPage.do"/>?cont_no='+cont_no+'&cont=cont&flag=ctrLoanCont&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp';
		    }else if(prd_id == "300021" || prd_id == "300020"){    
		    	url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&cont_no='+cont_no+"&flag=ctrLoanCont&menuId=queryCtrLoanContHistoryList&pvp=pvp";
		   	}else{
		    	url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&cont_no='+cont_no+"&flag=ctrLoanCont&menuId=queryCtrLoanContHistoryList&pvp=pvp";
			}
		}
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	
	function load(){
		PvpAuthorize.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		PvpAuthorize.cont_no._obj.addOneButton("cont_no","查看",getCont);
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="load();">
	<emp:gridLayout id="PvpAuthorizeGroup" title="授权信息" maxColumn="2">
			<emp:text id="PvpAuthorize.serno" label="出账流水号" maxlength="40" required="true" />
			<emp:text id="PvpAuthorize.tran_serno" label="交易流水号" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.authorize_no" label="授权编号" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="PvpAuthorize.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="PvpAuthorize.cus_name" label="客户名称" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="PvpAuthorize.prd_id" label="产品编号" maxlength="10" required="false" />
			<emp:text id="PvpAuthorize.prd_id_displayname" label="产品名称" required="false" />
			<emp:text id="PvpAuthorize.tran_id" label="交易码" maxlength="10" required="false" />
			<emp:text id="PvpAuthorize.tran_amt" label="交易金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="PvpAuthorize.tran_date" label="交易日期" required="false" />
			<emp:text id="PvpAuthorize.send_times" label="发送次数" maxlength="38" required="false" />
			<emp:text id="PvpAuthorize.return_code" label="返回编码" maxlength="10" required="false" />
			<emp:textarea id="PvpAuthorize.return_desc" label="返回说明" maxlength="2000" required="false" colSpan="2" />
			<emp:text id="PvpAuthorize.manager_br_id_displayname" label="管理机构"  required="false" />
			<emp:text id="PvpAuthorize.in_acct_br_id_displayname" label="入账机构"  required="false" />
			<emp:select id="PvpAuthorize.status" label="状态" dictname="STD_AUTHORIZE_STATUS" required="false" />
			<emp:text id="PvpAuthorize.fldvalue01" label="FLDVALUE01" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue02" label="FLDVALUE02" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue03" label="FLDVALUE03" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue04" label="FLDVALUE04" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue05" label="FLDVALUE05" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue06" label="FLDVALUE06" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue07" label="FLDVALUE07" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue08" label="FLDVALUE08" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue09" label="FLDVALUE09" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue10" label="FLDVALUE10" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue11" label="FLDVALUE11" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue12" label="FLDVALUE12" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue13" label="FLDVALUE13" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue14" label="FLDVALUE14" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue15" label="FLDVALUE15" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue16" label="FLDVALUE16" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue17" label="FLDVALUE17" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue18" label="FLDVALUE18" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue19" label="FLDVALUE19" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue20" label="FLDVALUE20" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue21" label="FLDVALUE21" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue22" label="FLDVALUE22" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue23" label="FLDVALUE23" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue24" label="FLDVALUE24" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue25" label="FLDVALUE25" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue26" label="FLDVALUE26" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue27" label="FLDVALUE27" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue28" label="FLDVALUE28" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue29" label="FLDVALUE29" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue30" label="FLDVALUE30" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue31" label="FLDVALUE31" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue32" label="FLDVALUE32" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue33" label="FLDVALUE33" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue34" label="FLDVALUE34" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue35" label="FLDVALUE35" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue36" label="FLDVALUE36" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue37" label="FLDVALUE37" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue38" label="FLDVALUE38" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue39" label="FLDVALUE39" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PvpAuthorize.fldvalue40" label="FLDVALUE40" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
