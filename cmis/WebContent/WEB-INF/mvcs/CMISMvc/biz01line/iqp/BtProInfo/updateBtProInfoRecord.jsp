<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
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

<style type="text/css">
.emp_input2{
border:1px solid #b7b7b7;

width:440px;
}
</style> 
<script type="text/javascript">
	
	/*--user code begin--*/
	 function doSave(){
		if(!IqpBtProInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpBtProInfo._toForm(form);
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
					window.location.reload();
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

		var url = '<emp:url action="updateBtProInfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};

	function caculate(){
        var pro_tolinves = IqpBtProInfo.pro_tolinves._getValue();
        var capital = IqpBtProInfo.get_capital._getValue();
        //检查格式
		var reg = /^[\d\,]*\.?\d*$/;
		var checkres = reg.exec(capital);
		if (!checkres) {
			alert("请输入正确的格式");
			return;
		}
        if(pro_tolinves!=""&&capital!=""){
            if(parseFloat(capital)<=parseFloat(pro_tolinves)){ 
            	IqpBtProInfo.get_capital_rate._setValue(parseFloat(capital)/parseFloat(pro_tolinves));
            }else{
                alert("到位资本金需小于项目总投资");
                IqpBtProInfo.get_capital._setValue(""); 
            }
        }
	}					
	/*--user code end--*/    
     	
</script>         
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateBtProInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpBtProInfoGroup" maxColumn="2" title="项目信息">
			<emp:text id="IqpBtProInfo.serno" label="业务编号" maxlength="40" required="false" defvalue="${context.serno}" readonly="true" hidden="true"/> 
			<emp:select id="IqpBtProInfo.pro_cls" label="项目类别" required="true" dictname="STD_ZB_PRO_TYPE"/>
			<emp:text id="IqpBtProInfo.pro_name" label="项目名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_input2"/>
			<emp:text id="IqpBtProInfo.pro_addr" label="项目地点" maxlength="100" required="true" colSpan="2" cssElementClass="emp_input2"/>  
			<emp:text id="IqpBtProInfo.pro_occup_squ" label="项目占地面积（平方米）" maxlength="100" required="false" dataType="Double"/>
			<emp:text id="IqpBtProInfo.pro_arch_squ" label="项目建筑面积（平方米）" maxlength="100" required="false"  dataType="Double"/>
			<emp:text id="IqpBtProInfo.approve_gover_dept" label="批项政府部门" maxlength="80" required="false" /> 
			<emp:text id="IqpBtProInfo.pro_approve_no" label="项目批准文号" maxlength="80" required="false" />
			<emp:text id="IqpBtProInfo.govlanduser_no" label="国有土地使用证编号" maxlength="80" required="false" />
			<emp:text id="IqpBtProInfo.landuser_lic" label="建设用地规划许可证号" maxlength="80" required="false" />
			
			<emp:text id="IqpBtProInfo.pro_invest" label="项目年度计划投资（本年）" maxlength="16" dataType="Currency" required="true" />
			<emp:select id="IqpBtProInfo.capital_get_mode" label="资本金到位方式" dictname="STD_CAPITAL_GET_MODE" required="true" />
			
			<emp:text id="IqpBtProInfo.pro_tolinves" label="项目总投资" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpBtProInfo.get_capital" label="到位资本金" maxlength="18" required="true" dataType="Currency" onchange="caculate()"/>
			<emp:text id="IqpBtProInfo.get_capital_rate" label="到位资本金比例" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="IqpBtProInfo.construct_lic" label="施工许可证" maxlength="80" required="false" />   
			<emp:text id="IqpBtProInfo.pro_main_term" label="项目经营期限"  maxlength="38" required="false" dataType="Int"/>
			<emp:select id="IqpBtProInfo.term_type" label="期限类型"  required="false" dictname="STD_ZB_TERM_TYPE"/>
			<emp:text id="IqpBtProInfo.construct_name" label="施工企业名称" maxlength="80" required="false" />
			<emp:textarea id="IqpBtProInfo.pro_memo" label="项目情况说明" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpBtProInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/> 
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>  
			<emp:actButton id="reset" label="重置" op="cancel"/>  
		</div>
	</emp:form>
</body>
</html>
</emp:page>
