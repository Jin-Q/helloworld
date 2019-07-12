<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doReset(){
	var agr_type = "";
	var agr_no = "";
	var data = new Array();
	data.push(agr_type,agr_no);
	window.returnValue = data;
	window.close();
}
//窗口关闭事件
function window.onbeforeunload(){        
  //用户点击浏览器右上角关闭按钮
  if((event.clientX > (document.body.clientWidth-15) && event.clientY<0)||event.altKey){   
	  doReset();
  }
  //用户点击任务栏，右键关闭
  else if(event.clientY > document.body.clientHeight||event.altKey){
	  doReset();
  }
} 

function doNext(){
	var result = IqpCargoOverseeRe._checkAll();
	if(result){
		var agr_type = IqpCargoOverseeRe.agr_type._getValue();
		var agr_no = IqpCargoOverseeRe.agr_no._getValue();
		var data = new Array();
		data.push(agr_type,agr_no);
		window.returnValue = data;
		window.close();
    }else {
	    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
	}
}
function doChange(){
	var type = IqpCargoOverseeRe.agr_type._getValue();
	if(type=="00"){//保兑仓
		IqpCargoOverseeRe.agr_no._setValue("");
		IqpCargoOverseeRe.agr_no._obj._renderReadonly(false);
	    IqpCargoOverseeRe.agr_no._obj.config.url='<emp:url action="queryIqpDepotAgrPop.do"/>';
	    IqpCargoOverseeRe.agr_no._obj.config.returnMethod="";
	}else if(type=="01"){//银企合作
		IqpCargoOverseeRe.agr_no._setValue("");
		IqpCargoOverseeRe.agr_no._obj._renderReadonly(false);
		IqpCargoOverseeRe.agr_no._obj.config.url='<emp:url action="queryIqpBconCoopAgrPop.do"/>';
	}else if(type=="02"){//监管协议
		IqpCargoOverseeRe.agr_no._setValue("");
		IqpCargoOverseeRe.agr_no._obj._renderReadonly(false);
		IqpCargoOverseeRe.agr_no._obj.config.url='<emp:url action="queryIqpOverseeAgrPop.do"/>';
	}else if(type==""){
		IqpCargoOverseeRe.agr_no._setValue("");
		IqpCargoOverseeRe.agr_no._obj._renderReadonly(true);
	}
}
//协议编号返回按钮方法
function returnAgrNo(data){
	var type = IqpCargoOverseeRe.agr_type._getValue();
	if(type=="00"){
		IqpCargoOverseeRe.agr_no._setValue(data.depot_agr_no._getValue());
	}else if(type=="01"){
		IqpCargoOverseeRe.agr_no._setValue(data.coop_agr_no._getValue());
	}else if(type=="02"){
		IqpCargoOverseeRe.agr_no._setValue(data.oversee_agr_no._getValue());
	}
}
function doLoad(){
	doChange();
}
</script>
</head>
<body class="page_content" onload="doLoad()">
		<emp:gridLayout id="IqpCargoOverseeReGroup" title="请选择协议类型和协议编号：" maxColumn="1">
			<emp:select id="IqpCargoOverseeRe.agr_type" label="协议类型" required="true" dictname="STD_ZB_AGR_TYPE" onchange="doChange();"/>
			<emp:pop id="IqpCargoOverseeRe.agr_no" label="协议编号" required="true" url="" returnMethod="returnAgrNo"/>
			<emp:text id="IqpCargoOverseeRe.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="下一步"/>
			<emp:button id="reset" label="取消"/>
		</div>
</body>
</html>
</emp:page>

