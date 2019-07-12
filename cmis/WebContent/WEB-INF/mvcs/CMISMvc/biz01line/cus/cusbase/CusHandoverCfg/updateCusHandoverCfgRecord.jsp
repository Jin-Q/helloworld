<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshCusHandoverDetail() {
		CusHandoverCfg_tabs.tabs.CusHandoverDetail_tab.refresh();
	};

	/*--user code begin--*/
	function cheakScope(){
			var table_scope = CusHandoverCfg.table_scope._obj.element.value;
			if(table_scope=="4"||table_scope=="5"){
				CusHandoverCfg.table_mode._setValue("2");
				CusHandoverCfg.table_mode._obj._renderReadonly(true);
		   }else if(table_scope == '6'){//如果移交范围是 6 客户合并，那么移交方式是 客户资料移交
				
				CusHandoverCfg.table_mode._setValue("1");
				CusHandoverCfg.table_mode._obj._renderReadonly(true);
		   }else{
				CusHandoverCfg.table_mode._setValue("");
				CusHandoverCfg.table_mode._obj._renderReadonly(false);
		    }
	};
	
	window.onload = function(){
		//cheakScope();
		/**页面加载时，调用移交方式值改变事件       需求编号：HS140903009   2014-10-10 唐顺岩 **/
		var table_mode = CusHandoverCfg.table_mode._getValue();
		if(table_mode=="3"){ //如果移交方式为“3-客户与业务移交-机构撤并” 则将移交范围固定为 “2-客户经理所有客户与业务” 
			CusHandoverCfg.table_scope._setValue("2");
			CusHandoverCfg.table_scope._obj._renderReadonly(true);
		}
   }

	//移交方式值改变事件   需求编号：HS140903009   2014-10-10 唐顺岩 
	function changeMode(mode_value){
		if(mode_value=="3"){ //如果移交方式为“3-客户与业务移交-机构撤并” 则将移交范围固定为 “2-客户经理所有客户与业务” 
			CusHandoverCfg.table_scope._setValue("2");
			CusHandoverCfg.table_scope._obj._renderReadonly(true);
		}else{
			CusHandoverCfg.table_scope._setValue("");
			CusHandoverCfg.table_scope._obj._renderReadonly(false);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusHandoverCfgRecord.do" method="POST">
		<emp:gridLayout id="CusHandoverCfgGroup" title="客户移交配置主表" maxColumn="2">
			<emp:select id="CusHandoverCfg.table_mode" label="移交方式" required="true" dictname="STD_ZB_HAND_TYPE" onchange="changeMode(this.value)"/>
			<emp:select id="CusHandoverCfg.table_scope" label="移交范围" required="true" dictname="STD_ZB_HAND_SCOPE" />
			<emp:textarea id="CusHandoverCfg.ext_class" label="扩展处理" maxlength="200" required="false" colSpan="2"/>
			<emp:textarea id="CusHandoverCfg.memo" label="备注" maxlength="300" required="false" />
			<emp:text id="CusHandoverCfg.serno" label="序列号" maxlength="40" required="true" readonly="true" hidden="true"/>
		</emp:gridLayout>
	 	<fieldSet style="font-weight: 800"><legend>扩展处理使用说明：</legend> 
	 	<blockquote>根据需要实现此扩展处理类</blockquote>
		<blockquote>扩展处理类必须继承接口 com.yucheng.cmis.biz01line.cus.cushand.extInterface.HandoverInterface</blockquote>
		<blockquote>此接口包含beforAction()和afterAction()2个方法。beforAction()移交前的处理，afterAction()移交后的处理</blockquote>
		</fieldSet>

		<div align=center>
			<emp:button id="submit" label="修改"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	<emp:tabGroup id="CusHandoverCfg_tabs" mainTab="CusHandoverDetail_tab">
		<emp:tab id="CusHandoverDetail_tab" label="客户移交配置信息表" url="queryCusHandoverCfgCusHandoverDetailList.do" reqParams="CusHandoverCfg.serno=$CusHandoverCfg.serno;" initial="true"/>
				
	</emp:tabGroup>
</body>
</html>
</emp:page>
