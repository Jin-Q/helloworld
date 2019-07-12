<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}  
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	}  
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	 function doSave(){
		if(!IqpHousePurInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpHousePurInfo._toForm(form);
		//var serno = IqpBksyndic._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
				}else {
					alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		var url = '<emp:url action="updateHousePurInfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	function checkYear(){
		var year = IqpHousePurInfo.house_build_year._getValue();
		if(year!=null&&year!=""){
			var   exp=/^\d{4}$/;  
	  		var   x=exp.test(year);   
	  		if (!x)
	  		{
	  			alert("输入年份有误,请输入4位数字");
	  			IqpHousePurInfo.house_build_year._setValue("");
	  			return;
	  		}
			if(year-1000<0)
			{
				alert("输入年份过早");
				IqpHousePurInfo.house_build_year._setValue("");
				return;
			}
		}
	};
	
	//校验 首付款比例在0%-100%之间
    function checkFistPerc(value){
        var fst_perc = value;
        if(fst_perc!=null&&fst_perc!=''){
        	if(fst_perc<0 || fst_perc>100){
                alert("首付款比例在0%-100%之间");
                IqpHousePurInfo.fst_pyr_perc._setValue("");
             } 
        }
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content"> 
	
	<emp:form id="submitForm" action="updateHousePurInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpHousePurInfoGroup" maxColumn="2" title="房产信息">
		<emp:text id="IqpHousePurInfo.serno" label="业务编号" maxlength="40"  defvalue="${context.serno}" required="false" readonly="true" hidden="true"/> 
			<emp:text id="IqpHousePurInfo.house_addr" label="房屋位置" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_long"/>
			<emp:text id="IqpHousePurInfo.pur_amt" label="购买金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpHousePurInfo.loan_perc" label="贷款比例" maxlength="10" required="false" dataType="Percent" hidden="true"/> 
			<emp:text id="IqpHousePurInfo.invc_no" label="发票号码" maxlength="20" required="false" /> 
			<emp:text id="IqpHousePurInfo.fst_pyr_perc" label="首付款比例" maxlength="10" required="true" onchange="checkFistPerc(value)" dataType="Percent"/>			
			<emp:date id="IqpHousePurInfo.pur_time" label="购买日期" required="true"/>
			<emp:text id="IqpHousePurInfo.house_build_year" label="房屋建成年份" required="false" onblur="checkYear()" maxlength="4" minlength="4"/>
			<emp:select id="IqpHousePurInfo.house_type" label="房屋类型" required="true" dictname="STD_ZB_HOUSE_INFO"/>
			<emp:text id="IqpHousePurInfo.house_name" label="房屋名称" required="false" maxlength="80"/>
			<emp:select id="IqpHousePurInfo.get_houproper_get_type" label="一手/二手" required="true" dictname="STD_ZB_HOUSE_GETTYPE"/>
			<emp:text id="IqpHousePurInfo.house_squ" label="房屋面积（平方米）" maxlength="10" required="true" dataType="Double" />
			<emp:text id="IqpHousePurInfo.house_buildprice" label="房屋购置单价" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpHousePurInfo.house_eval_value" label="房屋评估单价" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpHousePurInfo.is_fst_pur_house" label="是否首购房" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpHousePurInfo.houproper_status" label="房产状态" required="true" dictname="STD_ZB_HOUSE_STATUS"/>
			<emp:select id="IqpHousePurInfo.dispute_mode" label="纠纷解决方式" required="false" dictname="STD_ZB_DISPUTE_TYPE"/>
			<emp:select id="IqpHousePurInfo.house_qnt" label="住房套数" required="false" dictname="STD_ZB_HOUSE_NUM"/>
			<emp:text id="IqpHousePurInfo.hourec_no" label="商品房买卖合同网上备案登记号" required="false" maxlength="20"/>
		</emp:gridLayout>
		 
		<div align="center"> 
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:button id="reset" label="重置" />

		</div>
	</emp:form>
</body>
</html>
</emp:page>
