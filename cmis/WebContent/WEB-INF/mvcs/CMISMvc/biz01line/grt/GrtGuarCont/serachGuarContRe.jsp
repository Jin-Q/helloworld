<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>

<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String op = (String)context.getDataValue("op");
String guar_cont_no = (String)context.getDataValue("guar_cont_no");
String guar_model = "";
String rel = "";
if(context.containsKey("guar_model")){
	guar_model = (String)context.getDataValue("guar_model");
}
if(context.containsKey("rel")){
	rel = (String)context.getDataValue("rel");
}
System.out.println(guar_model);
%>
<emp:page>
<head>
<title>授信使用情况</title>
<jsp:include page="/include.jsp" />
<script type="text/javascript">
	function doGetUpdateLmtGuarCont(){
		var lmtCode = LmtGuarContReList._obj.getParamValue(['limit_code']);
		if (lmtCode != null) {
			var contNo = LmtGuarContReList._obj.getParamValue(['guar_cont_no']);
			var serno = LmtGuarContReList._obj.getParamValue(['serno']);
			var guar_model = '<%=guar_model%>';
			var rel = '<%=rel%>';
			var url = '<emp:url action="getRLmtGuarContUpdatePage.do"/>?lmtCode='+lmtCode+"&contNo="+contNo+"&serno="+serno+"&guar_model="+guar_model+"&rel="+rel;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doGetAddLmtGuarCont(){//新增
		var url = '<emp:url action="getAddRLmtGuarContPage.do"/>?guar_cont_no=<%=guar_cont_no%>';
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function doGetDeleteLmtGuarCont(){//删除
		var lmtCode = LmtGuarContReList._obj.getParamValue(['limit_code']);
		if (lmtCode != null) {
			if(!confirm("确定要删除该笔数据？")){
				return;
			}
			var contNo = LmtGuarContReList._obj.getParamValue(['guar_cont_no']);
			var guarLvl = LmtGuarContReList._obj.getParamValue(['guar_lvl']);
			var url = '<emp:url action="delRLmtGuarContRecordForNew.do"/>?limit_code='+lmtCode+"&guar_cont_no="+contNo+"&guar_lvl="+guarLvl;
			url = EMPTools.encodeURI(url);
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
						alert("删除成功!");
						window.location.reload();
					}else {
						alert("删除失败!");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doSub(){
		if(!RLmtGuarCont._checkAll()){
           return;   
		}  
		var form = document.getElementById("submitForm");
		RLmtGuarCont._toForm(form);  
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
					alert("新增成功!");
					window.opener.location.reload();
				    window.close();
				}else if(flag == "exists"){
					alert('该担保合同已与当前授信分项关联，请重新选择！');
					return;
				}else {
					alert("引入失败!");
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
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	function doViewLmtAgrDetails() {
		var limit_code = LmtGuarContReList._obj.getParamValue(['limit_code']);
		if (limit_code != null) {
			var url = "<emp:url action='viewLmtAgrInfo.do'/>?op=view&type=surp&agr_no="+limit_code;
			url=EMPTools.encodeURI(url);  
			window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};	
	
	function doViewIqpDetails() {
		var serno = IqpGuarContReList._obj.getParamValue(['serno']);
		if (serno != null) {
			var cont_no = IqpGuarContReList._obj.getParamValue(['cont_no']);
			if(cont_no == null || cont_no ==  ""){
				var url = "<emp:url action='getAllCtrLoanContView.do'/>?op=view&isShowButton=no&pvp=pvp&serno="+serno;
			}else{
				var url = "<emp:url action='getAllCtrLoanContView.do'/>?op=view&isShowButton=no&pvp=pvp&serno="+serno+"&cont_no="+cont_no;
			}
			url=EMPTools.encodeURI(url);
			window.open(url,'openIqpOrCtr','height=538,width=1024,top=70,left=0,toolbar=no,menubar=yes,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};	
</script>
</head>

<body >
	<div class='emp_gridlayout_title'>担保合同关联授信信息</div>
	<%if("update".equals(op)){ %>
	<div align="left">
		<emp:button id="getUpdateLmtGuarCont" label="修改" op=""/>
		<%if(guar_model!=null&&!"".equals(guar_model)&&!"05".equals(guar_model)&&!"2".equals(rel)){ %>
		<emp:button id="getAddLmtGuarCont" label="新增" op=""/>
		<emp:button id="getDeleteLmtGuarCont" label="删除" op=""/>
		<%} %>
	</div>
	<%} %>
	<emp:table icollName="LmtGuarContReList" pageMode="false" url="">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:link id="limit_code" label="授信额度编号" operation="viewLmtAgrDetails"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_amt" label="本次担保金额" dataType="Currency"/>
		<emp:text id="corre_rel" label="关联关系" dictname="STD_BIZ_CORRE_REL"/>
		<emp:text id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_lvl" label="担保等级" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="guar_cont_no" label="担保合同编号" hidden="true"/>
	</emp:table>
	<br>
	<br>
	<div class='emp_gridlayout_title'>担保合同关联业务信息</div>
	<emp:table icollName="IqpGuarContReList" pageMode="false" url="">
		<emp:link id="serno" label="业务编号" operation="viewIqpDetails"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_amt" label="本次担保金额" dataType="Currency"/>
		<emp:text id="corre_rel" label="关联关系" dictname="STD_BIZ_CORRE_REL"/>
		<emp:text id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_lvl" label="担保等级" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_no" label="合同编号" hidden="true"/>
	</emp:table>
</body> 
</emp:page>
