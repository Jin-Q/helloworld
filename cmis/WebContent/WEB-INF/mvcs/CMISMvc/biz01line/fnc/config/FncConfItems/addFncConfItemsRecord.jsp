<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryFncConfItemsList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doAddFncConfItems() {
		var form = document.getElementById("submitForm");
		if(FncConfItems.item_id._getValue().length!=9){
			return alert("项目编号必须为9位！");
		}
		var result = FncConfItems._checkAll();
		if(result){
			FncConfItems._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	};

	function toSubmitForm(form){
		  var handleSuccess = function(o){ EMPTools.unmask();
				if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var flag = jsonstr.flag;
							var item_id = jsonstr.item_id;

							if(flag=="update"){
								if(confirm("该项目编号已存在！点【确定】将维护该项目信息！")){
									var paramStr="item_id="+item_id;
									var url = '<emp:url action="getFncConfItemsUpdatePage.do"/>&'+paramStr;
									url = EMPTools.encodeURI(url);
									window.location = url;
							     }else goback();
						    }else if(flag=="new"){
									var url = '<emp:url action="queryFncConfItemsList.do"/>';
									url = EMPTools.encodeURI(url);
									window.location = url;

							}
				}
			};
			var handleFailure = function(o){ EMPTools.unmask();	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData); 
		  }

	function killErrors() 
	{ 
	return true; 
	}   

	function clearSp() {
		var itemName = FncConfItems.item_name._obj.element.value;
		itemName = itemName.replace(/(^\s*)|(\s*$)/g,"");
		FncConfItems.item_name._obj.element.value = itemName;
	}
	window.onerror = killErrors;
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addFncConfItemsRecord.do" method="POST">
		
		<emp:gridLayout id="FncConfItemsGroup" title="报表配置项目列表" maxColumn="2">
			<emp:text id="FncConfItems.item_id" label="项目编号" maxlength="9" required="true" dataType="CodeNo"/>
			<emp:text id="FncConfItems.item_name" label="项目名称" maxlength="200" required="true" onblur="clearSp()"/>
			<emp:select id="FncConfItems.fnc_conf_typ" label="所属报表种类" required="true" dictname="STD_ZB_FNC_TYP" />
			<emp:select id="FncConfItems.fnc_no_flg" label="新旧报表标志" required="false" dictname="STD_ZB_FNC_ON_TYP" />
			<emp:text id="FncConfItems.item_unit" label="单位" maxlength="60" colSpan="2"/>
			<emp:select id="FncConfItems.def_item_id" label="映射财报id" dictname="STD_ZB_FNC_CHG" colSpan="2" hidden="true" help="一套财报一个id这个id是对应的公共财报条目id"/>
			<emp:textarea id="FncConfItems.formula" label="指标公式" maxlength="500" colSpan="2"/>
			<emp:textarea id="FncConfItems.remark" label="备注"></emp:textarea>
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addFncConfItems" label="保存" op="add"/>
			<!--<emp:button id="reset" label="重置"/>
			--><emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

