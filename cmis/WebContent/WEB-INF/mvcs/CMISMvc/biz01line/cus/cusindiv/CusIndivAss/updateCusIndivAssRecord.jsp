<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	/*--user code begin--*/
	function doUpdateCusIndivAss(){
		var form = document.getElementById("submitForm");
		var result = CusIndivAss._checkAll();
		if(result){
			CusIndivAss._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
	}
	
  	function toSubmitForm(form){
	  var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="修改成功"){
					alert(flag);
					goback();
			    }else {
				 alert(flag);
				 return;
			   }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	
	function goback(){
		var paramStr="CusIndivAss.cus_id="+CusIndivAss.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivAssList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
					
	function doReturn(){
		goback();
	}
	
		/*--user code end--*/
	//输入日期不能大于当前日期
	function CheckDate(obj,errMsg){
		var str_date=obj._getValue();
		var openDay = '${context.OPENDAY}';
		if( str_date==""|| str_date==null){
	        return;
		}
		if(str_date>openDay){
			alert(errMsg);
		    obj._obj.element.value="";
		}
	}
	
	function cheakNum(){
		var num=CusIndivAss.indiv_ass_num._getValue();
		if(num==""||num==null){
			return ;
		}
		num = parseFloat(num);
		if(isNaN(num)){
			alert("数量 输入有误！");
			CusIndivAss.indiv_ass_num._obj.element.value="";
		}
		if(num<0){
			alert("数量 不能为负数！");
			CusIndivAss.indiv_ass_num._obj.element.value="";
		}
	}
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusIndivAssRecord.do" method="POST">
		<emp:gridLayout id="CusIndivAssGroup" maxColumn="2" title="家庭资产信息">
			<emp:text id="CusIndivAss.indiv_ass_id" label="资产编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:select id="CusIndivAss.indiv_ass_type" label="资产类别" required="true" dictname="STD_ZB_INV_ASS_TPY"/>
			<emp:text id="CusIndivAss.indiv_ass_name" label="资产名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusIndivAss.indiv_ass_plr" label="资产单位" maxlength="80" required="true" />
			<emp:text id="CusIndivAss.indiv_ass_num" label="资产数量" maxlength="38" required="true" dataType="Int" onblur="cheakNum()" />
			<emp:date id="CusIndivAss.com_ass_buy_date" label="资产购置日期" required="false" hidden="true" onblur="CheckDate(CusIndivAss.com_ass_buy_date,'资产购置日期不能大于当前日期')"/>
			<emp:text id="CusIndivAss.com_ass_ori_amt" label="资产购置原价(元)" maxlength="18" required="false" hidden="true" dataType="Currency" />
			<emp:text id="CusIndivAss.indiv_ass" label="资产估价(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="CusIndivAss.com_ass_status" label="抵押状况" required="true" dictname="STD_ZB_ASS_COLL_INFO" colSpan="2"/>
			<emp:textarea id="CusIndivAss.indiv_ass_des" label="资产描述" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:textarea id="CusIndivAss.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusIndivAss.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusIndivAss.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="CusIndivAss.input_date" label="登记日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusIndivAss.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusIndivAss.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusIndivAss.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updateCusIndivAss" label="保存"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>