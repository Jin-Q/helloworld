<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doAddCusComFinaStock(){
		var form = document.getElementById("submitForm");
		var result = CusComFinaStock._checkAll();
		if(result){
			CusComFinaStock._toForm(form)
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
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusComFinaStock.cus_id="+CusComFinaStock.cus_id._obj.element.value;
						var url = '<emp:url action="getCusComFinaStockAddPage.do"/>&'+paramStr+"&EditFlag=<%=editFlag%>";
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
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
		var paramStr="CusComFinaStock.cus_id="+CusComFinaStock.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusComFinaStockList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};
	
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
	/*--user code begin--*/
	function doReturn(){
		goback();
	}
				
	function cheakStk(amt){
		//当前股本总量(万股)
		var cap_qnt = CusComFinaStock.com_stk_cap_qnt._getValue();
		//当前流通股量(万股)
		var cur_qnt = CusComFinaStock.com_stk_cur_qnt._getValue();
		cap_qnt = parseFloat(cap_qnt);
		cur_qnt = parseFloat(cur_qnt);
		if(cap_qnt<0){
			alert("当前股本总量(万股)不小于零！");
			CusComFinaStock.com_stk_cap_qnt._obj.element.value="";
			return ;
		}
		if(cur_qnt<0){
			alert("当前流通股量(万股)不小于零！");
			CusComFinaStock.com_stk_cur_qnt._obj.element.value="";
			return ;
		}
		if(parseFloat(cap_qnt)<parseFloat(cur_qnt)){
	        alert("当前股本总量 (万股) 应不小于当前流通股量");
	        amt._obj.element.value="";
	 	}
	}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCusComFinaStockRecord.do" method="POST">
		
		<emp:gridLayout id="CusComFinaStockGroup" title="发行股票信息" maxColumn="2">
			
			<emp:text id="CusComFinaStock.com_stk_code" label="股票代码" maxlength="10" required="true" />
			<emp:text id="CusComFinaStock.com_stk_name" label="股票名称" maxlength="60" required="true" />
			<emp:date id="CusComFinaStock.com_stk_mrk_dt" label="上市日期" required="true" onblur="CheckDate(CusComFinaStock.com_stk_mrk_dt,'上市日期不能大于当前日期')"/>
			<emp:select id="CusComFinaStock.com_stk_mrk_place" label="上市地" required="true" dictname="STD_ZX_LISTED" />
			<emp:text id="CusComFinaStock.com_stk_mrk_brs" label="交易所名称" maxlength="80" required="false" colSpan="2"/>
			<emp:text id="CusComFinaStock.com_stk_init_amr" label="首次发行价(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="CusComFinaStock.com_stk_cur_amt" label="股票当前价(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CusComFinaStock.com_stk_eva_amt" label="股票评估价(元)" maxlength="18" required="false" dataType="Currency"  colSpan="2"/>
			<emp:text id="CusComFinaStock.com_stk_cap_qnt" label="当前股本总量(万股)" maxlength="20" required="true" dataType="Double" onblur="cheakStk(CusComFinaStock.com_stk_cap_qnt)"/>
			<emp:text id="CusComFinaStock.com_stk_cur_qnt" label="当前流通股量(万股)" maxlength="20" required="false" dataType="Double" onblur="cheakStk(CusComFinaStock.com_stk_cur_qnt)"/>
			<emp:textarea id="CusComFinaStock.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusComFinaStock.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComFinaStock.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComFinaStock.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComFinaStock.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComFinaStock.last_upd_date" label="更新日期" required="false" hidden="true"/>
			<emp:text id="CusComFinaStock.cus_id" label="客户码" maxlength="30" required="true" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusComFinaStock" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

