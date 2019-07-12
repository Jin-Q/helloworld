<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<jsp:include page="/include.jsp" />
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp/rpt.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
		
	};
		
	function doReset(){
		page.dataGroups.FncConfDefFormatGroup.reset();
	};
	 function doAddFncConfDefFormat(){
        var form = document.getElementById('submitForm');
        var result = FncConfDefFormat._checkAll();
        if(result){
			FncConfDefFormat._toForm(form);
		}
		var url="<emp:url action='addFncConfDefFormatRecord.do'/>";
		var handleSuccess = function(o){ EMPTools.unmask();
				var paramStr = FncConfDefFormat.style_id._obj.element.value;
				var sUrl = "<emp:url action='getFnc.do'/>?style_id="+paramStr;
				alert(sUrl);
				form.action = sUrl;
				form.submit();
		};	
		var handleFailure = function(o){ EMPTools.unmask();
				alert("出现错误！");
				return false;
	    };	
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		}; 
		var postData = YAHOO.util.Connect.setForm(submitForm);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
    }
    
    function updateFnc(styleId,itemId){
    	alert("this here");
    	alert(styleId+"====="+itemId);
    }
    
    function switch_updown() {
   		if (mspan.title=="关闭上栏") {
      		  mspan.title="打开上栏";
      		  document.all("mtop").style.display="none";
   		} else {
       		 mspan.title="关闭上栏";
        	document.all("mtop").style.display="";
  		 }
	}

    function doReturn() {
		var url = '<empext:url url="queryFncConfDefFormatList.do"/>';
		url = EMP.util.Tools.encodeURI(url);
		window.location=url;
	};
</script>
</head>

<body >
	<form id="submitForm" method="POST">
	</form>
	<div id="mtop">
		<div id="FncConfDefFormatGroup" class="emp_group_div">
			<emp:gridLayout id="FncConfDefFormatGroup" maxColumn="3" title="报表配置定义表" >
				<emp:text id="FncConfDefFormat.style_id" label="报表样式编号" maxlength="6" required="true" />
				<emp:text id="FncConfDefFormat.item_id" label="项目编号" maxlength="9" required="true" />
				<emp:text id="FncConfDefFormat.fnc_conf_order" label="顺序编号" maxlength="10" required="false" />
				<emp:text id="FncConfDefFormat.fnc_conf_cotes" label="栏位" maxlength="38" required="false" />
				<emp:select id="FncConfDefFormat.fnc_conf_row_flg" label="行次标识" required="false" dictname="STD_ZB_FNCCONFROW" />
				<emp:text id="FncConfDefFormat.fnc_conf_indent" label="层次" maxlength="38" required="false" />
				<emp:text id="FncConfDefFormat.fnc_conf_prefix" label="前缀" maxlength="20" required="false" colSpan="2" />
				<emp:select id="FncConfDefFormat.fnc_item_edit_typ" label="项目编辑方式" required="false" dictname="STD_ZB_FNCITEMEDT" />
				<emp:text id="FncConfDefFormat.fnc_conf_disp_amt" label="显示数值" maxlength="16" required="false" />
				<emp:text id="FncConfDefFormat.fnc_cnf_app_row" label="追加行数" maxlength="38" required="false" />
				<emp:text id="FncConfDefFormat.fnc_conf_disp_tpy" label="默认现实类型" maxlength="2" required="false" />
				<emp:text id="FncConfDefFormat.fnc_conf_chk_frm" label="检查公式" maxlength="30" required="false" colSpan="2" />
				<emp:text id="FncConfDefFormat.fnc_conf_cal_frm" label="计算公式" maxlength="30" required="false" colSpan="2" />
			</emp:gridLayout>
		</div>
		<div align="center">
			<br>
			<emp:button id="addFncConfDefFormat" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</div>
	<div align="center" style="width:100%; height:1px">
		<table width="101%" height="100%">
			<tr>
    			<td height="3" align="center" bgcolor="#C70505" style="cursor:hand" title="关闭上栏" id="mspan" onClick="switch_updown()"></td>
  			</tr>
		</table>
	</div>
    <div align="center">
		<emp:fnc id="formatList"/>
    </div>

</body>
</html>
</emp:page>
