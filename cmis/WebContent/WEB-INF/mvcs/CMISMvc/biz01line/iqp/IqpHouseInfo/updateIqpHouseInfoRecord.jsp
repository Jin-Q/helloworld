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
		if("cont".equals(cont)){
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
		if(!IqpHouseInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpHouseInfo._toForm(form);
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

		var url = '<emp:url action="updateIqpHouseInfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}		
	function changeStructure(){
		var structure = IqpHouseInfo.building_structure_cd._getValue();
		if(structure=="04"){
			IqpHouseInfo.durable_years._setValue("50");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}
		else if(structure=="02"){
			IqpHouseInfo.durable_years._setValue("60");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}
		else if(structure=="01"){
			IqpHouseInfo.durable_years._setValue("70");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}
		else if(structure=="09"){
			IqpHouseInfo.durable_years._setValue("40");
			IqpHouseInfo.durable_years._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._setValue("");
		}else if(structure=="05"){
			IqpHouseInfo.durable_years._obj._renderHidden(true);
			IqpHouseInfo.durable_years._setValue("");
			IqpHouseInfo.durable_years1._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._setValue("");
		}
	}
	function doSetValue(){
		IqpHouseInfo.durable_years._setValue(IqpHouseInfo.durable_years1._getValue());
	}
	function doLoad(){
		var structure = IqpHouseInfo.building_structure_cd._getValue();
		if(structure=="05"){
			IqpHouseInfo.durable_years._obj._renderHidden(true);
			IqpHouseInfo.durable_years1._obj._renderHidden(false);
			IqpHouseInfo.durable_years1._setValue(IqpHouseInfo.durable_years._getValue());
		}
	};

	function checkYear(){
		var year = IqpHouseInfo.house_build_year._getValue();
		if(year!=null&&year!=""){
			var   exp=/^\d{4}$/;
	  		var   x=exp.test(year);   
	  		if (!x){
	  			alert("输入年份有误,请输入4位数字");
	  			IqpHouseInfo.house_build_year._setValue("");
	  			return;
	  		}
			if(year-1000<0){
				alert("输入年份过早");
				IqpHouseInfo.house_build_year._setValue("");
				return;
			}
			//var todayDate='${context.OPENDAY}';
			//var todayYear = todayDate.substring(0,4);
			//if(year>todayYear){
            //   alert("房屋建成年份不能大于系统当前年份!");
            //   IqpHouseInfo.house_build_year._setValue("");
			//}
		}
    };
    //校验 首付款比例在0%-100%之间
    function checkFistPerc(value){
        var fst_perc = value;
        if(fst_perc!=null&&fst_perc!=''){
        	if(fst_perc<0 || fst_perc>100){
                alert("首付款比例在0%-100%之间");
                IqpHouseInfo.fst_pyr_perc._setValue("");
             }  
        }
    };
    //购买时间(不使用)
	function checkPurTime(){
		 var pur_time = IqpHouseInfo.pur_time._getValue();
		 if(pur_time){
			 var todayDate='${context.OPENDAY}';
			 var flag = CheckDate1BeforeDate2(pur_time,todayDate);
             if(pur_time==todayDate){
                 return true;
             }
             if(flag){
            	 alert("【购买时间】不能小于当前日期！");
            	 IqpHouseInfo.pur_time._setValue("");
 				 return false;
             }
		 }
	};
    //校验建筑面积,占地面积不能为负数
	function checksqu(){
        var house_squ = IqpHouseInfo.house_squ._getValue();
        var occup_squ = IqpHouseInfo.occup_squ._getValue();
        if(house_squ<0){
            alert("建筑面积不能为负数!");
        	IqpHouseInfo.house_squ._setValue("");
        	return;
        }
        if(occup_squ<0){
        	alert("占地面积不能为负数!");
        	IqpHouseInfo.occup_squ._setValue("");
        	return;
        }
	}
	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateIqpHouseInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpHouseInfoGroup" maxColumn="2" title="厂房信息">
			<emp:text id="IqpHouseInfo.house_addr" label="厂房位置" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_long"/>
			<emp:text id="IqpHouseInfo.pur_amt" label="购买金额" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="IqpHouseInfo.fst_pyr_perc" label="首付款比例" maxlength="10" onchange="checkFistPerc(value)" required="true" dataType="Percent" />
			<emp:date id="IqpHouseInfo.pur_time" label="购买时间" required="true" />
			<emp:text id="IqpHouseInfo.house_build_year" label="房屋建成年份" required="false" onblur="checkYear()" maxlength="4" minlength="4"/>
			<emp:text id="IqpHouseInfo.house_squ" label="建筑面积（平方米）" maxlength="10" required="true" onblur="checksqu()" dataType="Double" />
			<emp:text id="IqpHouseInfo.occup_squ" label="占地面积（平方米）" maxlength="10" required="true" onblur="checksqu()" dataType="Double" />
			<emp:select id="IqpHouseInfo.building_structure_cd" label="建筑结构" required="true" dictname="STD_ARCH_STR" onchange="changeStructure()"/>
			<emp:select id="IqpHouseInfo.fitment_degree" label="装修程度" required="true" dictname="STD_FITMENT_DEGREE" />
			<emp:text id="IqpHouseInfo.durable_years" label="耐用年限" maxlength="10" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpHouseInfo.durable_years1" label="耐用年限" maxlength="10" required="false" dataType="Int" hidden="true" onblur="doSetValue()"/>
			<emp:select id="IqpHouseInfo.street_situation" label="临街状况" required="true" dictname="STD_FRONTAGE_STATUS" />
			<emp:select id="IqpHouseInfo.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
		    
		    <emp:text id="IqpHouseInfo.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" hidden="true" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="修改" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancle"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
