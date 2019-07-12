<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
    //加载的时候删除默认请选择提示
	window.onload = function(){
		 fromList._obj.element.options.remove(0);
		 toList._obj.element.options.remove(0);
	}

	//移到配置字段中
	function putToList(){
		var nLen = fromList._obj.element.options.length;
		for(i=0;i<nLen;i++){
	  		if(fromList._obj.element.options(i).selected){
	  			var oOption = fromList._obj.element.options(i);
	  			var varOption = new Option(oOption.text,oOption.value);
	  			toList._obj.element.options.add(varOption);
	  			fromList._obj.element.options.remove(i);
		 		break;
	  		}
		}
	}

	//移回模型字段中
	function putFromList(){
		var nLen = toList._obj.element.options.length;
		for(i=0;i<nLen;i++){
	  		if(toList._obj.element.options(i).selected){
	  			var oOption = toList._obj.element.options(i);
	  			var varOption = new Option(oOption.text,oOption.value);
	  			fromList._obj.element.options.add(varOption);
	  			toList._obj.element.options.remove(i);
		 		break;
	  		}
		}
	}

	//进行下一步操作
	function doSave(){
		var nLen = toList._obj.element.options.length;
		var values = '';
		for(i=0;i<nLen;i++){
			values += toList._obj.element.options(i).value + ',';
		}
		if(values!=null&&values!=''){
			values = values.substring(0,values.length-1);
		}
		CusModifyHistoryCfg.cfg_column._setValue(values);

		var form = document.getElementById("submitForm");
		var result = CusModifyHistoryCfg._checkAll();
		if(result){
			CusModifyHistoryCfg._toForm(form);
			form.submit();
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="updateCusModifyHistoryCfgRecord.do" method="POST">
		<emp:gridLayout id="CusModifyHistoryCfgGroup" maxColumn="2" title="客户修改历史配置">
			<emp:text id="CusModifyHistoryCfg.model_id" label="表名" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusModifyHistoryCfg.input_id_displayname" label="登录人"  required="true" readonly="true"/>
			<emp:text id="CusModifyHistoryCfg.input_br_id_displayname" label="登录机构"  required="true" readonly="true"/>
			<emp:date id="CusModifyHistoryCfg.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			<emp:select id="fromList" label="模型字段" dictname="STD_ZB_TABLEMODEL_COLUMN" size="30" ondblclick="putToList()"></emp:select>
			<emp:select id="toList" label="配置字段" dictname="STD_ZB_CFG_COLUMN" size="30" ondblclick="putFromList()"></emp:select>
			
			<emp:text id="CusModifyHistoryCfg.cfg_column" label="配置列" required="true" hidden="true"/>
			<emp:text id="CusModifyHistoryCfg.input_id" label="登录人" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusModifyHistoryCfg.input_br_id" label="登录机构" maxlength="20" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
