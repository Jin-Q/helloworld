<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>	
<%
	//request = (HttpServletRequest) pageContext.getRequest();
	String biz_type = request.getParameter("biz_type");
	String flg = request.getParameter("flg");
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	context.put("flg",flg);
	String menuId = "";
	if (context.containsKey("menuId")) {
		menuId = (String) context.getDataValue("menuId");
	}
%>  
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doOnLoad(){
	IqpLoanApp.cus_id._obj.addOneButton("cus","选择",getCus);
	IqpLoanApp.cus_id._obj._renderRequired(true); 
	IqpLoanApp.prd_id._obj._renderReadonly(true);
	IqpLoanApp.belong_net._obj.addOneButton("net","查看",getNet); 
	
	//获取业务模式，如果不是普通模式，则展示业务模式和所属网络
	var biz_type = '<%=biz_type%>';
	IqpLoanApp.biz_type._setValue(biz_type);
	if(biz_type!="7" && biz_type!="4" && biz_type!="0" && biz_type!="8"){ 
		IqpLoanApp.biz_type._obj._renderHidden(false);
		IqpLoanApp.belong_net._obj._renderHidden(false);

		IqpLoanApp.biz_type._obj._renderRequired(true);
		IqpLoanApp.belong_net._obj._renderRequired(true);  
	}else if(biz_type=="4" || biz_type=="0"){//如果是国内保理/先票后货
		if(biz_type=="4"){//如果是国内保理
			IqpLoanApp.prd_id._setValue("800021");
			prd_name._setValue("国内保理");

			//HS141110017_保理业务改造
			IqpLoanApp.fin_type._obj._renderHidden(false);
			IqpLoanApp.fin_type._obj._renderRequired(true);
			
		}else if(biz_type=="0"){//如果是先票后货，则产品种类固定为银行承兑汇票
			IqpLoanApp.prd_id._setValue("200024");
			prd_name._setValue("银行承兑汇票");
		}
		IqpLoanApp.biz_type._obj._renderHidden(false);
		IqpLoanApp.belong_net._obj._renderHidden(false); 

		IqpLoanApp.biz_type._obj._renderRequired(true);
		IqpLoanApp.belong_net._obj._renderRequired(true);     
		
	}else if(biz_type=="8"){//银租通业务无需所属网络字段
		IqpLoanApp.biz_type._obj._renderHidden(false);
		IqpLoanApp.belong_net._obj._renderHidden(true);

		IqpLoanApp.biz_type._obj._renderRequired(true);
		IqpLoanApp.belong_net._obj._renderRequired(false); 
	}else{
		IqpLoanApp.biz_type._obj._renderHidden(true);
		IqpLoanApp.belong_net._obj._renderHidden(true);

		IqpLoanApp.biz_type._obj._renderRequired(false);
		IqpLoanApp.belong_net._obj._renderRequired(false);  
	}
	if(biz_type=="7" && '${context.flg}' == 'csgn'){
		IqpLoanApp.prd_id._setValue("100063");
		prd_name._setValue("企业委托贷款");
	}
	/**add by lisj 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
	if(biz_type=="7" && '${context.flg}' == 'trust'){
		IqpLoanApp.prd_id._setValue("100090");
		prd_name._setValue("信托贷款");
	}
	/**add by lisj 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
	if(biz_type=="6"){
		IqpLoanApp.biz_type._obj._renderHidden(false);
		IqpLoanApp.belong_net._obj._renderHidden(false);

		IqpLoanApp.biz_type._obj._renderRequired(true);
		IqpLoanApp.belong_net._obj._renderRequired(false);  
	}
	/**modified by lisj 2015-2-6  需求编号【HS141110017】保理业务改造（信贷应用）begin**/
	if(biz_type =="3" || biz_type =="4"){
		IqpLoanApp.biz_type._obj._renderHidden(false);
		IqpLoanApp.belong_net._obj._renderHidden(false); 
		IqpLoanApp.biz_type._obj._renderRequired(true);	
		IqpLoanApp.belong_net._obj._renderRequired(false); 
  
	}
	/**modified by lisj 2015-2-6  需求编号【HS141110017】保理业务改造（信贷应用）end**/
};
function getCus(){  
   /**如果业务模式不为普通，则客户不能为个人客户*/
   var main_br_id = '${context.organNo}';
    //modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
	/*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 begin*/
	if('<%=biz_type%>' != "7" || '<%=flg%>' == "trust" || '<%=flg%>' == "csgn"){
		var url = '<emp:url action="queryAllCusPop.do"/>?cusTypCondition='+"BELG_LINE!='BL300' and cus_status='20' and cust_mgr = '${context.currentUserId}' and main_br_id='"+main_br_id+"'"
		+'&opt=team&cusTypCondition2='+"BELG_LINE!='BL300' and cus_status='20' and cust_mgr = '${context.currentUserId}' "
		+'&returnMethod=returnCus';
    }else{
    	var url = '<emp:url action="queryAllCusPop.do"/>?cusTypCondition='+"cus_status='20' and cust_mgr = '${context.currentUserId}' and main_br_id='"+main_br_id+"'"
		+'&opt=team&cusTypCondition2='+"cus_status='20' and cust_mgr = '${context.currentUserId}'" 
    	+'&returnMethod=returnCus';
    } 
    /*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 end*/
	//modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
	url = EMPTools.encodeURI(url);  
	var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
	window.open(url,'newWindow',param); 
};

function returnCus(data){
	IqpLoanApp.cus_id._setValue(data.cus_id._getValue());
	IqpLoanApp.manager_br_id._setValue(data.main_br_id._getValue());
	cus_name._setValue(data.cus_name._getValue());
	var cusId = data.cus_id._getValue();
	getBizLineByCusId(cusId);
};
//通过客户ID异步查询客户所属条线
function getBizLineByCusId(cusId){
	var prd_id = IqpLoanApp.prd_id._getValue();
	var biz_type =  IqpLoanApp.biz_type._getValue();
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var msg = jsonstr.msg;
			var bizline = jsonstr.bizline;
			if(flag == "success"){
				//IqpLoanApp.prd_id._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&returnMethod=returnCus&bizline='+bizline;
				//IqpLoanApp.prd_id._obj.addOneButton("prd","选择",getPrd);
				/**modified by lisj 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
				if('<%=biz_type%>' == "7" || '<%=biz_type%>' == "8"){//普通贷款，银租通
					IqpLoanApp.prd_id._obj._renderReadonly(false);
					IqpLoanApp.prd_id._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline='+bizline;
					if('${context.flg}' == 'csgn'){//定向资管委托贷款（业务入口）
						IqpLoanApp.prd_id._obj._renderReadonly(true);
					}else if('${context.flg}' == 'csgnClaimInvest'){//委托债权投资（业务入口）
                        //判断客户条线，如果是公司小微的则为企业委托贷款
                        //如果是个人，则为个人委托贷款
                        if(bizline == "BL100" || bizline == "BL200"){
                        	IqpLoanApp.prd_id._setValue("100063");
                    		prd_name._setValue("企业委托贷款");
                    		IqpLoanApp.prd_id._obj._renderReadonly(true);
                        }else if(bizline == "BL300"){
                        	IqpLoanApp.prd_id._setValue("100065");
                    		prd_name._setValue("个人委托贷款");
                    		IqpLoanApp.prd_id._obj._renderReadonly(true);
                        }
					}else if ('${context.flg}' == 'trust'){//信托贷款（业务入口）
						IqpLoanApp.prd_id._setValue("100090");
                		prd_name._setValue("信托贷款");
                		IqpLoanApp.prd_id._obj._renderReadonly(true);	
					}
					/**modified by lisj 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
				}else if('<%=biz_type%>' != "0" && '<%=biz_type%>' != "4"){ //不是国内保理业务 先票后货
					IqpLoanApp.prd_id._obj._renderReadonly(false);
					IqpLoanApp.prd_id._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline=BL500';
				}
				//给所属网络pop框的url赋值
				IqpLoanApp.belong_net._obj.config.url='<emp:url action="queryIqpNetAgrNo.do"/>&returnMethod=getNetMagInfo&restrictUsed=false&biz_type='+biz_type+'&cus_id='+cusId;
			}else {
				alert(msg);
			}
		}
	};
	var handleFailure = function(o){
		alert("异步回调失败！");	
	};
	var callback = {
		success:handleSuccess,
		failure:handleFailure
	};
	var url = '<emp:url action="getBizLineByCusId.do"/>&cusid='+cusId;
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
}

	/*--user code begin--*/ 
	function doNext(){
		if(!IqpLoanApp._checkAll()){
           return;
		}
		var prdid = IqpLoanApp.prd_id._getValue();
		var form = document.getElementById("submitForm");
		IqpLoanApp._toForm(form);
		form.submit();
    };   
	
	
	function returnPrdId(data){
		IqpLoanApp.prd_id._setValue(data.id);
		prd_name._setValue(data.label);

		//HS141110017_保理业务改造
		if(data.id=='800021'){
			IqpLoanApp.fin_type._obj._renderHidden(false);
			IqpLoanApp.fin_type._obj._renderRequired(true);
		}else if(data.id=='100051'){
			alert("法人账户透支有且只能有一笔生效的合同！");
		}else if(data.id=='100088'){
			alert("小微自助循环贷款有且只能有一笔生效的合同！");
		}
		/**add by lisj 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
		var biz_type = '<%=biz_type%>';
		var prd_id = IqpLoanApp.prd_id._getValue();
		if(biz_type=="7" && '${context.flg}' == 'nomal' && prd_id =="100090"){
			alert("一般业务不允许发起【信托贷款】业务！");
			IqpLoanApp.prd_id._setValue("");
			prd_name._setValue("");
			IqpLoanApp.cus_id._setValue("");
			cus_name._setValue("");
		}else if(biz_type =="8"){
			alert("银租通不允许发起【信托贷款】业务！");
			IqpLoanApp.prd_id._setValue("");
			prd_name._setValue("");
			IqpLoanApp.cus_id._setValue("");
			cus_name._setValue("");
		}
		/**add by lisj 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
	};

	function getNetMagInfo(data){
		IqpLoanApp.belong_net._setValue(data.net_agr_no._getValue()); 
    };     

    function getNet(){
        var net = IqpLoanApp.belong_net._getValue();        
        if(net != "" && net != null){
        	var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?net_agr_no='+net+'&op=view&menuId=netmanager';  
        	url = EMPTools.encodeURI(url);
        	var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
        	window.open(url,'newWindow',param);     
        }else{
            alert("请选择所属网络!");  
        }
    };   
    //重置功能
    function doClean(){
    	var biz_type = '<%=biz_type%>';
    	IqpLoanApp.cus_id._setValue("");
    	cus_name._setValue("");
    	if(biz_type!="4" && biz_type!="0"){
    		IqpLoanApp.prd_id._setValue("");
        	prd_name._setValue("");
        }
    	IqpLoanApp.belong_net._setValue("");
    };

    function checkFinType(){
        var fin_type = IqpLoanApp.fin_type._getValue();
        if(fin_type=='2'){
            alert("目前表外保理功能未开放！");
            IqpLoanApp.fin_type._setValue('');
        }
    }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="addIqpLoanAppRecodePage.do?flg=${context.flg}"  method="POST">
		<emp:gridLayout id="IqpLoanAppGroup" title="业务申请" maxColumn="2">
			<emp:text id="IqpLoanApp.cus_id" label="客户码" required="false" />
			<emp:text id="cus_name" label="客户名称" maxlength="80" readonly="true" required="false" />
			<emp:pop id="IqpLoanApp.prd_id" label="产品编号" url="" returnMethod="returnPrdId" required="true" buttonLabel="选择" />
			<emp:text id="prd_name" label="产品名称" maxlength="80" readonly="true" required="false" />
			
			<emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" required="false" readonly="true" />
			<emp:pop id="IqpLoanApp.belong_net" label="所属网络" url="queryIqpNetAgrNo.do?cus_id=''&biz_type=''&restrictUsed=false&returnMethod=getNetMagInfo" required="false" />  
			
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true" required="false" defvalue="000"/>
			<emp:text id="IqpLoanApp.manager_br_id" label="管理机构" hidden="true" required="false" />
			<emp:date id="IqpLoanApp.input_date" label="登记日期" required="false" hidden="true" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:text id="IqpLoanApp.input_id" label="登记人" maxlength="20" required="false" defvalue="${context.currentUserId}" hidden="true" readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true" readonly="true"/>
			
			<!--HS141110017_保理业务改造 -->
			<emp:select id="IqpLoanApp.fin_type" label="融资类型" hidden="true" required="false" dictname="STD_ZB_FIN_TYPE" onblur="checkFinType()"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" op="add"/>
			<emp:button id="clean" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

