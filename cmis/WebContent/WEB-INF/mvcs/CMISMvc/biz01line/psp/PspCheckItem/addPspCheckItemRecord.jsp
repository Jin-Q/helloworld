<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

 function checkTagType(){
    var tagType = PspCheckItem.tag_type._getValue();
    if(tagType == "20" || tagType == "30" || tagType =="40"){
 	   PspCheckItem.tag_attr._obj._renderRequired(true);
    }else{
 	   PspCheckItem.tag_attr._obj._renderRequired(false); 
    }

    if(tagType=='30'){
    	PspCheckItem.is_need_event._setValue('2');
    	PspCheckItem.is_need_event._obj._renderReadonly(true);
    	setEvent();
    }else{
    	PspCheckItem.is_need_event._obj._renderReadonly(false);
    }
 }; 

 function checkIsJudge(){
     var isJude = PspCheckItem.is_judge._getValue();
     if(isJude == 1){
  	   PspCheckItem.rule._obj._renderRequired(true);
     }else{
  	   PspCheckItem.rule._obj._renderRequired(false);   
     }  
  };

  function setEvent(){
	var is_need_event = PspCheckItem.is_need_event._getValue();
	if(is_need_event=='1'){
		PspCheckItem.event_type._obj._renderHidden(false);
		PspCheckItem.hpp_cond._obj._renderHidden(false);
		PspCheckItem.imp_item_id._obj._renderHidden(false);
		PspCheckItem.event_type._obj._renderRequired(true);
		PspCheckItem.hpp_cond._obj._renderRequired(true);
		PspCheckItem.imp_item_id._obj._renderRequired(true);
	}else{
		PspCheckItem.event_type._obj._renderHidden(true);
		PspCheckItem.hpp_cond._obj._renderHidden(true);
		PspCheckItem.imp_item_id._obj._renderHidden(true);
		PspCheckItem.event_type._obj._renderRequired(false);
		PspCheckItem.hpp_cond._obj._renderRequired(false);
		PspCheckItem.imp_item_id._obj._renderRequired(false);
		PspCheckItem.event_type._setValue('');
		PspCheckItem.hpp_cond._setValue('');
		PspCheckItem.imp_item_id._setValue('');
	}
  }

  function checkIsHidden(){
	  var is_hidden = PspCheckItem.is_hidden._getValue();
	  if(is_hidden=='1'){
		  PspCheckItem.is_null._setValue('2');
		  PspCheckItem.is_null._obj._renderReadonly(true);
	  }else{
		  PspCheckItem.is_null._obj._renderReadonly(false);
	  }
  }
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addPspCheckItemRecord.do" method="POST">
		<emp:gridLayout id="PspCheckItemGroup" title="检查项目表" maxColumn="2">
			<emp:text id="PspCheckItem.item_id" label="项目编号" maxlength="40" colSpan="2" hidden="true" required="false" />
			<emp:text id="PspCheckItem.item_name" label="项目名称" maxlength="200" required="true" colSpan="2"/>
			<emp:select id="PspCheckItem.tag_type" label="标签类型" dictname="STD_ZB_TAB_TYPE" required="false" onchange="checkTagType()" />
			<emp:text id="PspCheckItem.tag_attr" label="标签属性" maxlength="30" required="true" />
			<emp:text id="PspCheckItem.default_value" label="缺省值" maxlength="40" required="false" />
			<emp:text id="PspCheckItem.msg" label="提示信息" maxlength="200" required="false" />
			<emp:text id="PspCheckItem.url" label="URL" maxlength="300" required="false" />
			<emp:text id="PspCheckItem.url_desc" label="URL说明" maxlength="200" required="false" />
			<emp:select id="PspCheckItem.is_hidden" label="是否隐藏" required="true" dictname="STD_ZX_YES_NO" onchange="checkIsHidden()" defvalue="2"/>
			<emp:select id="PspCheckItem.is_null" label="是否不为空" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspCheckItem.is_judge" label="是否自动判断" required="true" dictname="STD_ZX_YES_NO" onchange="checkIsJudge()"/>
			<emp:pop id="PspCheckItem.rule" label="业务规则" url="rulespop.do?id=PspCheckItem.rule" required="false" />
			
			<emp:select id="PspCheckItem.is_need_event" label="是否增加事件" required="true" dictname="STD_ZX_YES_NO" defvalue="2" onchange="setEvent()" colSpan="2"/>
			<emp:select id="PspCheckItem.event_type" label="事件类型" required="false" dictname="STD_PSP_EVENT_TYPE" hidden="true"/>
			<emp:text id="PspCheckItem.hpp_cond" label="成立条件（多值以|隔开）" required="false" hidden="true"/>
			<emp:text id="PspCheckItem.imp_item_id" label="目标项目编号" required="false" hidden="true"/>
			
			<emp:text id="PspCheckItem.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:date id="PspCheckItem.input_date" label="登记日期" required="false" hidden="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="PspCheckItem.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
			<emp:textarea id="PspCheckItem.memo" label="备注" maxlength="200" required="false" colSpan="2"/> 
		</emp:gridLayout> 
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

