<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
//	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_label1 {
	vertical-align: top;
	padding-top: 4px;
	text-align: left;
	width: 400px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		changeVisitType();
		convert_worker1();
		convert_scxts1();
		convert_jqsb1();
		convert_chsl1();
		convert_dzyxthzgzk1();
		convert_dzybxnlpj1();
		convert_dzysdwcfdj1();

		var options = PspSitecheckCom.visit_type._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
	   	    if(options[i].value=='02'||options[i].value=='03'){
	  			options.remove(i);
	   	    }
	    }
	}
	function convert_worker1(){
		if(PspSitecheckCom.worker_zjqk._getValue()=="03"){
			PspSitecheckCom.worker_jssm._obj._renderHidden(false);
			PspSitecheckCom.worker_jssm._obj._renderRequired(true);
		}else{
			PspSitecheckCom.worker_jssm._obj._renderHidden(true);
			PspSitecheckCom.worker_jssm._obj._renderRequired(false);
		}
	}
	function convert_scxts1(){
		if(PspSitecheckCom.scx_zjqk._getValue()=="03"){
			PspSitecheckCom.scx_jsyy._obj._renderHidden(false);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(true);
		}else{
			PspSitecheckCom.scx_jsyy._obj._renderHidden(true);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(false);
		}
	}
	function convert_jqsb1(){
		if(PspSitecheckCom.jqsb_zjqk._getValue()=="03"){
			PspSitecheckCom.jqsb_jsyy._obj._renderHidden(false);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(true);
		}else{
			PspSitecheckCom.jqsb_jsyy._obj._renderHidden(true);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(false);
		}
	}
	function convert_chsl1(){
		if(PspSitecheckCom.chsl_zjqk._getValue()=="03"){
			PspSitecheckCom.chsl_jsyy._obj._renderHidden(false);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(true);
		}else{
			PspSitecheckCom.chsl_jsyy._obj._renderHidden(true);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(false);
		}
	}
	function convert_dzyxthzgzk1(){
		if(PspSitecheckCom.dzyxthzgzk._getValue()=="2"){
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderHidden(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(true);
		}else{
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderHidden(true);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(false);
		}
	}
	function convert_dzybxnlpj1(){
		if(PspSitecheckCom.dzybxnlpj._getValue()=="2"){
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderHidden(false);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(true);
		}else{
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderHidden(true);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(false);
		}
	}
	function convert_dzysdwcfdj1(){
		if(PspSitecheckCom.dzysdwcfdj._getValue()=="1" || PspSitecheckCom.djjgslcfdj._getValue()=="1"){
			PspSitecheckCom.cfdjsj._obj._renderHidden(false);
			PspSitecheckCom.cfdjsj._obj._renderRequired(true);
			PspSitecheckCom.cfdjyy._obj._renderHidden(false);
			PspSitecheckCom.cfdjyy._obj._renderRequired(true);
			PspSitecheckCom.cfdjsqr._obj._renderHidden(false);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(true);
		}else{
			PspSitecheckCom.cfdjsj._obj._renderHidden(true);
			PspSitecheckCom.cfdjsj._obj._renderRequired(false);
			PspSitecheckCom.cfdjyy._obj._renderHidden(true);
			PspSitecheckCom.cfdjyy._obj._renderRequired(false);
			PspSitecheckCom.cfdjsqr._obj._renderHidden(true);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(false);
		}
	}
	function convert_worker(){
		if(PspSitecheckCom.worker_zjqk._getValue()=="03"){
			PspSitecheckCom.worker_jssm._obj._renderHidden(false);
			PspSitecheckCom.worker_jssm._obj._renderRequired(true);
			PspSitecheckCom.worker_jssm._setValue("");
		}else{
			PspSitecheckCom.worker_jssm._obj._renderHidden(true);
			PspSitecheckCom.worker_jssm._obj._renderRequired(false);
			PspSitecheckCom.worker_jssm._setValue("");
		}
	}
	function convert_scxts(){
		if(PspSitecheckCom.scx_zjqk._getValue()=="03"){
			PspSitecheckCom.scx_jsyy._obj._renderHidden(false);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(true);
			PspSitecheckCom.scx_jsyy._setValue("");
		}else{
			PspSitecheckCom.scx_jsyy._obj._renderHidden(true);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(false);
			PspSitecheckCom.scx_jsyy._setValue("");
		}
	}
	function convert_jqsb(){
		if(PspSitecheckCom.jqsb_zjqk._getValue()=="03"){
			PspSitecheckCom.jqsb_jsyy._obj._renderHidden(false);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(true);
			PspSitecheckCom.jqsb_jsyy._setValue("");
		}else{
			PspSitecheckCom.jqsb_jsyy._obj._renderHidden(true);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(false);
			PspSitecheckCom.jqsb_jsyy._setValue("");
		}
	}
	function convert_chsl(){
		if(PspSitecheckCom.chsl_zjqk._getValue()=="03"){
			PspSitecheckCom.chsl_jsyy._obj._renderHidden(false);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(true);
			PspSitecheckCom.chsl_jsyy._setValue("");
		}else{
			PspSitecheckCom.chsl_jsyy._obj._renderHidden(true);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(false);
			PspSitecheckCom.chsl_jsyy._setValue("");
		}
	}
	function convert_dzyxthzgzk(){
		if(PspSitecheckCom.dzyxthzgzk._getValue()=="2"){
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderHidden(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(true);
			PspSitecheckCom.dzyxthzgzk_ycsm._setValue("");
		}else{
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderHidden(true);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._setValue("");
		}
	}
	function convert_dzybxnlpj(){
		if(PspSitecheckCom.dzybxnlpj._getValue()=="2"){
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderHidden(false);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(true);
			PspSitecheckCom.dzybxnlpj_ycsm._setValue("");
		}else{
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderHidden(true);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzybxnlpj_ycsm._setValue("");
		}
	}
	function convert_dzysdwcfdj(){
		if(PspSitecheckCom.dzysdwcfdj._getValue()=="1" || PspSitecheckCom.djjgslcfdj._getValue()=="1"){
			PspSitecheckCom.cfdjsj._obj._renderHidden(false);
			PspSitecheckCom.cfdjsj._obj._renderRequired(true);
			PspSitecheckCom.cfdjyy._obj._renderHidden(false);
			PspSitecheckCom.cfdjyy._obj._renderRequired(true);
			PspSitecheckCom.cfdjsqr._obj._renderHidden(false);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(true);
			//PspSitecheckCom.cfdjsj._setValue("");
			//PspSitecheckCom.cfdjyy._setValue("");
			//PspSitecheckCom.cfdjsqr._setValue("");
		}else{
			PspSitecheckCom.cfdjsj._obj._renderHidden(true);
			PspSitecheckCom.cfdjsj._obj._renderRequired(false);
			PspSitecheckCom.cfdjsj._setValue("");
			PspSitecheckCom.cfdjyy._obj._renderHidden(true);
			PspSitecheckCom.cfdjyy._obj._renderRequired(false);
			PspSitecheckCom.cfdjyy._setValue("");
			PspSitecheckCom.cfdjsqr._obj._renderHidden(true);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(false);
			PspSitecheckCom.cfdjsqr._setValue("");
		}
	}
	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspSitecheckCom._checkAll()){
			PspSitecheckCom._toForm(form); 
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
						alert("修改成功!");
						doReturn();
					}else {
						alert("新增异常!"); 
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
		}
	};	

	function doReturn(){
		var dataFrom = '<%=dataFrom%>';
		var task_id = PspSitecheckCom.task_id._getValue();
		var url = '<emp:url action="queryPspPropertyAnalyListForVisit.do"/>?task_id='+task_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}


	function changeVisitType(){
		var visit_type = PspSitecheckCom.visit_type._getValue();
		if(visit_type=="01" ||visit_type=="02"){
			document.getElementById('scjyzk').style.display="";
			document.getElementById('chxc').style.display="";
			document.getElementById('dzypmxjc').style.display="none";
			document.getElementById('tzqk').style.display="none";
			document.getElementById('xmjdqk').style.display="none";
			document.getElementById('jcnr').style.display="none";

			PspSitecheckCom.task_id._obj._renderRequired(true);
			PspSitecheckCom.check_time._obj._renderRequired(true);
			PspSitecheckCom.check_addr._obj._renderRequired(true);
			PspSitecheckCom.yjry._obj._renderRequired(true);
			PspSitecheckCom.visit_type._obj._renderRequired(true);
			PspSitecheckCom.worker_num._obj._renderRequired(true);
			PspSitecheckCom.worker_zjqk._obj._renderRequired(true);
			PspSitecheckCom.worker_jssm._obj._renderRequired(true);
			PspSitecheckCom.scxts._obj._renderRequired(true);
			PspSitecheckCom.scxts_kgl._obj._renderRequired(true);
			PspSitecheckCom.scx_zjqk._obj._renderRequired(true);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(true);
			PspSitecheckCom.jqsb_sum._obj._renderRequired(true);
			PspSitecheckCom.jqsb_kgl._obj._renderRequired(true);
			PspSitecheckCom.jqsb_zjqk._obj._renderRequired(true);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(true);
			PspSitecheckCom.bgry_sum._obj._renderRequired(true);
			PspSitecheckCom.bgcs._obj._renderRequired(true);
			PspSitecheckCom.chsl._obj._renderRequired(true);
			PspSitecheckCom.chsl_zjqk._obj._renderRequired(true);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(true);
			PspSitecheckCom.other_sm._obj._renderRequired(false);
			PspSitecheckCom.dzyxthzgzk._obj._renderRequired(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzybxnlpj._obj._renderRequired(false);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzysdwcfdj._obj._renderRequired(false);
			PspSitecheckCom.djjgslcfdj._obj._renderRequired(false);
			PspSitecheckCom.cfdjsj._obj._renderRequired(false);
			PspSitecheckCom.cfdjyy._obj._renderRequired(false);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(false);
			PspSitecheckCom.dzyrdbyy._obj._renderRequired(false);
			PspSitecheckCom.dywsfcz._obj._renderRequired(false);
			PspSitecheckCom.sfczyxzqsxfxys._obj._renderRequired(false);
			PspSitecheckCom.dzybhyxdbxl._obj._renderRequired(false);
			PspSitecheckCom.bgdzydjzmsfqq._obj._renderRequired(false);
			PspSitecheckCom.dzywcz_qtzk._obj._renderRequired(false);
			PspSitecheckCom.xmjhztz._obj._renderRequired(false);
			PspSitecheckCom.xmjhsjtr._obj._renderRequired(false);
			PspSitecheckCom.xmjhtrbl._obj._renderRequired(false);
			PspSitecheckCom.zbjjhtre._obj._renderRequired(false);
			PspSitecheckCom.ydwzbj._obj._renderRequired(false);
			PspSitecheckCom.zbjdwbl._obj._renderRequired(false);
			PspSitecheckCom.jhyhzrze._obj._renderRequired(false);
			PspSitecheckCom.jhyhdwje._obj._renderRequired(false);
			PspSitecheckCom.jhyhdwbl._obj._renderRequired(false);
			PspSitecheckCom.whrze._obj._renderRequired(false);
			PspSitecheckCom.whrzdwje._obj._renderRequired(false);
			PspSitecheckCom.whrzdwbl._obj._renderRequired(false);
			PspSitecheckCom.qtzjjhtre._obj._renderRequired(false);
			PspSitecheckCom.qtzjdwje._obj._renderRequired(false);
			PspSitecheckCom.qtzjdwbl._obj._renderRequired(false);
			PspSitecheckCom.xmjd._obj._renderRequired(false);
			PspSitecheckCom.xmyjwgsj._obj._renderRequired(false);
			PspSitecheckCom.jhgcjd._obj._renderRequired(false);
			PspSitecheckCom.sjgcjd._obj._renderRequired(false);
			PspSitecheckCom.zbjdwsyqk._obj._renderRequired(false);
			PspSitecheckCom.qtczzjsfdw._obj._renderRequired(false);
			PspSitecheckCom.dksyhxmjdsfpp._obj._renderRequired(false);
			PspSitecheckCom.gcjsjzsfsl._obj._renderRequired(false);
			PspSitecheckCom.xmsjtrcgs._obj._renderRequired(false);
			PspSitecheckCom.xmdcjdsfzc._obj._renderRequired(false);
			PspSitecheckCom.xmdcsfdyqxy._obj._renderRequired(false);
			PspSitecheckCom.scjssfddyq._obj._renderRequired(false);
			PspSitecheckCom.jsglryszsfdyq._obj._renderRequired(false);
			PspSitecheckCom.xswlhxsqdsfsc._obj._renderRequired(false);
			
			
		}else if (visit_type=="03"){
			document.getElementById('dzypmxjc').style.display="";
			document.getElementById('scjyzk').style.display="none";
			document.getElementById('chxc').style.display="none";
			document.getElementById('tzqk').style.display="none";
			document.getElementById('xmjdqk').style.display="none";
			document.getElementById('jcnr').style.display="none";

			PspSitecheckCom.dzyxthzgzk._obj._renderRequired(true);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(true);
			PspSitecheckCom.dzybxnlpj._obj._renderRequired(true);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(true);
			PspSitecheckCom.dzysdwcfdj._obj._renderRequired(true);
			PspSitecheckCom.djjgslcfdj._obj._renderRequired(true);
			PspSitecheckCom.cfdjsj._obj._renderRequired(true);
			PspSitecheckCom.cfdjyy._obj._renderRequired(true);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(true);
			PspSitecheckCom.dzyrdbyy._obj._renderRequired(true);
			PspSitecheckCom.dywsfcz._obj._renderRequired(true);
			PspSitecheckCom.sfczyxzqsxfxys._obj._renderRequired(true);
			PspSitecheckCom.dzybhyxdbxl._obj._renderRequired(true);
			PspSitecheckCom.bgdzydjzmsfqq._obj._renderRequired(true);
			PspSitecheckCom.dzywcz_qtzk._obj._renderRequired(true);


			        
			PspSitecheckCom.worker_num._obj._renderRequired(false);          
			PspSitecheckCom.worker_zjqk._obj._renderRequired(false);          
			PspSitecheckCom.worker_jssm._obj._renderRequired(false);          
			PspSitecheckCom.scxts._obj._renderRequired(false);          
			PspSitecheckCom.scxts_kgl._obj._renderRequired(false);          
			PspSitecheckCom.scx_zjqk._obj._renderRequired(false);          
			PspSitecheckCom.scx_jsyy._obj._renderRequired(false);          
			PspSitecheckCom.jqsb_sum._obj._renderRequired(false);          
			PspSitecheckCom.jqsb_kgl._obj._renderRequired(false);          
			PspSitecheckCom.jqsb_zjqk._obj._renderRequired(false);          
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(false);          
			PspSitecheckCom.bgry_sum._obj._renderRequired(false);          
			PspSitecheckCom.bgcs._obj._renderRequired(false);          
			PspSitecheckCom.chsl._obj._renderRequired(false);          
			PspSitecheckCom.chsl_zjqk._obj._renderRequired(false);          
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(false);          
			PspSitecheckCom.other_sm._obj._renderRequired(false); 	         
			PspSitecheckCom.xmjhztz._obj._renderRequired(false);          
			PspSitecheckCom.xmjhsjtr._obj._renderRequired(false);          
			PspSitecheckCom.xmjhtrbl._obj._renderRequired(false);          
			PspSitecheckCom.zbjjhtre._obj._renderRequired(false);          
			PspSitecheckCom.ydwzbj._obj._renderRequired(false);          
			PspSitecheckCom.zbjdwbl._obj._renderRequired(false);          
			PspSitecheckCom.jhyhzrze._obj._renderRequired(false);          
			PspSitecheckCom.jhyhdwje._obj._renderRequired(false);          
			PspSitecheckCom.jhyhdwbl._obj._renderRequired(false);          
			PspSitecheckCom.whrze._obj._renderRequired(false);          
			PspSitecheckCom.whrzdwje._obj._renderRequired(false);          
			PspSitecheckCom.whrzdwbl._obj._renderRequired(false);          
			PspSitecheckCom.qtzjjhtre._obj._renderRequired(false);          
			PspSitecheckCom.qtzjdwje._obj._renderRequired(false);          
			PspSitecheckCom.qtzjdwbl._obj._renderRequired(false);          
			PspSitecheckCom.xmjd._obj._renderRequired(false);          
			PspSitecheckCom.xmyjwgsj._obj._renderRequired(false);          
			PspSitecheckCom.jhgcjd._obj._renderRequired(false);          
			PspSitecheckCom.sjgcjd._obj._renderRequired(false);          
			PspSitecheckCom.zbjdwsyqk._obj._renderRequired(false);          
			PspSitecheckCom.qtczzjsfdw._obj._renderRequired(false);          
			PspSitecheckCom.dksyhxmjdsfpp._obj._renderRequired(false);          
			PspSitecheckCom.gcjsjzsfsl._obj._renderRequired(false);          
			PspSitecheckCom.xmsjtrcgs._obj._renderRequired(false);          
			PspSitecheckCom.xmdcjdsfzc._obj._renderRequired(false);          
			PspSitecheckCom.xmdcsfdyqxy._obj._renderRequired(false);          
			PspSitecheckCom.scjssfddyq._obj._renderRequired(false);          
			PspSitecheckCom.jsglryszsfdyq._obj._renderRequired(false);          
			PspSitecheckCom.xswlhxsqdsfsc._obj._renderRequired(false); 
			
			
		}else if (visit_type=="04"){
			document.getElementById('tzqk').style.display="";
			document.getElementById('xmjdqk').style.display="";
			document.getElementById('jcnr').style.display="";
			document.getElementById('dzypmxjc').style.display="none";
			document.getElementById('scjyzk').style.display="none";
			document.getElementById('chxc').style.display="none";



			PspSitecheckCom.xmjhztz._obj._renderRequired(true);
			PspSitecheckCom.xmjhsjtr._obj._renderRequired(true);
			PspSitecheckCom.xmjhtrbl._obj._renderRequired(true);
			PspSitecheckCom.zbjjhtre._obj._renderRequired(true);
			PspSitecheckCom.ydwzbj._obj._renderRequired(true);
			PspSitecheckCom.zbjdwbl._obj._renderRequired(true);
			PspSitecheckCom.jhyhzrze._obj._renderRequired(true);
			PspSitecheckCom.jhyhdwje._obj._renderRequired(true);
			PspSitecheckCom.jhyhdwbl._obj._renderRequired(true);
			PspSitecheckCom.whrze._obj._renderRequired(true);
			PspSitecheckCom.whrzdwje._obj._renderRequired(true);
			PspSitecheckCom.whrzdwbl._obj._renderRequired(true);
			PspSitecheckCom.qtzjjhtre._obj._renderRequired(true);
			PspSitecheckCom.qtzjdwje._obj._renderRequired(true);
			PspSitecheckCom.qtzjdwbl._obj._renderRequired(true);
			PspSitecheckCom.xmjd._obj._renderRequired(true);
			PspSitecheckCom.xmyjwgsj._obj._renderRequired(true);
			PspSitecheckCom.jhgcjd._obj._renderRequired(true);
			PspSitecheckCom.sjgcjd._obj._renderRequired(true);
			PspSitecheckCom.zbjdwsyqk._obj._renderRequired(true);
			PspSitecheckCom.qtczzjsfdw._obj._renderRequired(true);
			PspSitecheckCom.dksyhxmjdsfpp._obj._renderRequired(true);
			PspSitecheckCom.gcjsjzsfsl._obj._renderRequired(true);
			PspSitecheckCom.xmsjtrcgs._obj._renderRequired(true);
			PspSitecheckCom.xmdcjdsfzc._obj._renderRequired(true);
			PspSitecheckCom.xmdcsfdyqxy._obj._renderRequired(true);
			PspSitecheckCom.scjssfddyq._obj._renderRequired(true);
			PspSitecheckCom.jsglryszsfdyq._obj._renderRequired(true);
			PspSitecheckCom.xswlhxsqdsfsc._obj._renderRequired(true);
			
			
			PspSitecheckCom.worker_num._obj._renderRequired(false);
			PspSitecheckCom.worker_zjqk._obj._renderRequired(false);
			PspSitecheckCom.worker_jssm._obj._renderRequired(false);
			PspSitecheckCom.scxts._obj._renderRequired(false);
			PspSitecheckCom.scxts_kgl._obj._renderRequired(false);
			PspSitecheckCom.scx_zjqk._obj._renderRequired(false);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(false);
			PspSitecheckCom.jqsb_sum._obj._renderRequired(false);
			PspSitecheckCom.jqsb_kgl._obj._renderRequired(false);
			PspSitecheckCom.jqsb_zjqk._obj._renderRequired(false);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(false);
			PspSitecheckCom.bgry_sum._obj._renderRequired(false);
			PspSitecheckCom.bgcs._obj._renderRequired(false);
			PspSitecheckCom.chsl._obj._renderRequired(false);
			PspSitecheckCom.chsl_zjqk._obj._renderRequired(false);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(false);
			PspSitecheckCom.other_sm._obj._renderRequired(false);
			PspSitecheckCom.dzyxthzgzk._obj._renderRequired(false);
			PspSitecheckCom.dzyxthzgzk_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzybxnlpj._obj._renderRequired(false);
			PspSitecheckCom.dzybxnlpj_ycsm._obj._renderRequired(false);
			PspSitecheckCom.dzysdwcfdj._obj._renderRequired(false);
			PspSitecheckCom.djjgslcfdj._obj._renderRequired(false);
			PspSitecheckCom.cfdjsj._obj._renderRequired(false);
			PspSitecheckCom.cfdjyy._obj._renderRequired(false);
			PspSitecheckCom.cfdjsqr._obj._renderRequired(false);
			PspSitecheckCom.dzyrdbyy._obj._renderRequired(false);
			PspSitecheckCom.dywsfcz._obj._renderRequired(false);
			PspSitecheckCom.sfczyxzqsxfxys._obj._renderRequired(false);
			PspSitecheckCom.dzybhyxdbxl._obj._renderRequired(false);
			PspSitecheckCom.bgdzydjzmsfqq._obj._renderRequired(false);
			PspSitecheckCom.dzywcz_qtzk._obj._renderRequired(false);

			
		}else {
			document.getElementById('dzypmxjc').style.display="none";
			document.getElementById('scjyzk').style.display="none";
			document.getElementById('chxc').style.display="none";
			document.getElementById('tzqk').style.display="none";
			document.getElementById('xmjdqk').style.display="none";
			document.getElementById('jcnr').style.display="none";
		}
	}

	function changeVisit(){
		
		PspSitecheckCom.worker_num._setValue("");
		PspSitecheckCom.worker_zjqk._setValue("");
		PspSitecheckCom.worker_jssm._setValue("");
		PspSitecheckCom.scxts._setValue("");
		PspSitecheckCom.scxts_kgl._setValue("");
		PspSitecheckCom.scx_zjqk._setValue("");
		PspSitecheckCom.scx_jsyy._setValue("");
		PspSitecheckCom.jqsb_sum._setValue("");
		PspSitecheckCom.jqsb_kgl._setValue("");
		PspSitecheckCom.jqsb_zjqk._setValue("");
		PspSitecheckCom.jqsb_jsyy._setValue("");
		PspSitecheckCom.bgry_sum._setValue("");
		PspSitecheckCom.bgcs._setValue("");
		PspSitecheckCom.chsl._setValue("");
		PspSitecheckCom.chsl_zjqk._setValue("");
		PspSitecheckCom.chsl_jsyy._setValue("");
		PspSitecheckCom.other_sm._setValue("");
		PspSitecheckCom.dzyxthzgzk._setValue("");
		PspSitecheckCom.dzyxthzgzk_ycsm._setValue("");
		PspSitecheckCom.dzybxnlpj._setValue("");
		PspSitecheckCom.dzybxnlpj_ycsm._setValue("");
		PspSitecheckCom.dzysdwcfdj._setValue("");
		PspSitecheckCom.djjgslcfdj._setValue("");
		PspSitecheckCom.cfdjsj._setValue("");
		PspSitecheckCom.cfdjyy._setValue("");
		PspSitecheckCom.cfdjsqr._setValue("");
		PspSitecheckCom.dzyrdbyy._setValue("");
		PspSitecheckCom.dywsfcz._setValue("");
		PspSitecheckCom.sfczyxzqsxfxys._setValue("");
		PspSitecheckCom.dzybhyxdbxl._setValue("");
		PspSitecheckCom.bgdzydjzmsfqq._setValue("");
		PspSitecheckCom.dzywcz_qtzk._setValue("");
		PspSitecheckCom.xmjhztz._setValue("");
		PspSitecheckCom.xmjhsjtr._setValue("");
		PspSitecheckCom.xmjhtrbl._setValue("");
		PspSitecheckCom.zbjjhtre._setValue("");
		PspSitecheckCom.ydwzbj._setValue("");
		PspSitecheckCom.zbjdwbl._setValue("");
		PspSitecheckCom.jhyhzrze._setValue("");
		PspSitecheckCom.jhyhdwje._setValue("");
		PspSitecheckCom.jhyhdwbl._setValue("");
		PspSitecheckCom.whrze._setValue("");
		PspSitecheckCom.whrzdwje._setValue("");
		PspSitecheckCom.whrzdwbl._setValue("");
		PspSitecheckCom.qtzjjhtre._setValue("");
		PspSitecheckCom.qtzjdwje._setValue("");
		PspSitecheckCom.qtzjdwbl._setValue("");
		PspSitecheckCom.xmjd._setValue("");
		PspSitecheckCom.xmyjwgsj._setValue("");
		PspSitecheckCom.jhgcjd._setValue("");
		PspSitecheckCom.sjgcjd._setValue("");
		PspSitecheckCom.zbjdwsyqk._setValue("");
		PspSitecheckCom.qtczzjsfdw._setValue("");
		PspSitecheckCom.dksyhxmjdsfpp._setValue("");
		PspSitecheckCom.gcjsjzsfsl._setValue("");
		PspSitecheckCom.xmsjtrcgs._setValue("");
		PspSitecheckCom.xmdcjdsfzc._setValue("");
		PspSitecheckCom.xmdcsfdyqxy._setValue("");
		PspSitecheckCom.scjssfddyq._setValue("");
		PspSitecheckCom.jsglryszsfdyq._setValue("");
		PspSitecheckCom.xswlhxsqdsfsc._setValue("");
	}

	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updatePspSitecheckComRecord.do" method="POST">
		
		<emp:gridLayout id="PspSitecheckComGroup" title="现场检查" maxColumn="2">
			<emp:text id="PspSitecheckCom.pk_id" label="主键" required="true" hidden="true"/>
			<emp:text id="PspSitecheckCom.task_id" label="任务号"  hidden="true"/>
			<emp:date id="PspSitecheckCom.check_time" label="检查时间"  required="true"/>
			<emp:text id="PspSitecheckCom.check_addr" label="检查地点" maxlength="100"   required="true" />
			<emp:text id="PspSitecheckCom.yjry" label="约见人员" maxlength="40" required="true" />
			<emp:select id="PspSitecheckCom.visit_type" label="现场检查类型"  required="true"  dictname="STD_ZB_VISIT_TYPE" onchange="changeVisitType();changeVisit()" readonly="true"/>	
			<emp:text id="PspSitecheckCom.dbqymc" label="担保企业名称" maxlength="100"  required="false" hidden="true"/>
		
		</emp:gridLayout>
		
		<div id="scjyzk" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGroup" title="生产经营状况" maxColumn="2"  >
			<emp:text id="PspSitecheckCom.worker_num" label="工人数量" required="true" dataType="Int" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.worker_zjqk" label="与上期对比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_worker()"/>
			<emp:textarea id="PspSitecheckCom.worker_jssm" label="工人数量减少说明原因" required="true" colSpan="2"/>
			<emp:text id="PspSitecheckCom.scxts" label="生产线条数" required="true" dataType="Int"/>
			<emp:text id="PspSitecheckCom.scxts_kgl" label="实际开工率"  required="true" dataType="Rate"/>
			<emp:radio id="PspSitecheckCom.scx_zjqk" label="与上期对比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_scxts()"/>
			<emp:textarea id="PspSitecheckCom.scx_jsyy" label="生产线减少原因" required="true" colSpan="2"/>
			<emp:text id="PspSitecheckCom.jqsb_sum" label="机器设备总台数" required="true" dataType="Int"/>
			<emp:text id="PspSitecheckCom.jqsb_kgl" label="实际开工率" required="true" dataType="Rate"/>
			<emp:radio id="PspSitecheckCom.jqsb_zjqk" label="与上期对比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_jqsb()"/>
			<emp:textarea id="PspSitecheckCom.jqsb_jsyy" label="机器设备减少原因" required="true" colSpan="2"/>
			<emp:text id="PspSitecheckCom.bgry_sum" label="办公场所：办公人员数量" required="true" dataType="Int" />
			<emp:radio id="PspSitecheckCom.bgcs" label="办公场所" required="true" dictname="STD_ZX_FIELD_OWNER" layout="false"/>
		</emp:gridLayout>
		</div>
		<div id="chxc" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGroup" title="存货现场" maxColumn="2">
			<emp:text id="PspSitecheckCom.chsl" label="存货数量" required="true"  dataType="Int" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.chsl_zjqk" label="与上期比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_chsl()"/>
			<emp:textarea id="PspSitecheckCom.chsl_jsyy" label="减少原因" required="true" colSpan="2"/>
			<emp:textarea id="PspSitecheckCom.other_sm" label="其他需说明原因" required="false" colSpan="2"/>
		</emp:gridLayout>
		</div>
		<div id="dzypmxjc" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGroup" title="抵质押品明细检查" maxColumn="2">
			<emp:radio id="PspSitecheckCom.dzyxthzgzk" label="抵（质）押物形态和占管状况" required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" onclick="convert_dzyxthzgzk()"/>
			<emp:textarea id="PspSitecheckCom.dzyxthzgzk_ycsm" label="抵（质）押物形态和占管状况异常说明" required="true" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.dzybxnlpj" label="抵（质）押物变现能力评价" required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" onclick="convert_dzybxnlpj()"/>
			<emp:textarea id="PspSitecheckCom.dzybxnlpj_ycsm" label="抵（质）押物变现能力异常说明" required="true" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.dzysdwcfdj" label="抵（质）押物实地（物）是否被查封、冻结" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_dzysdwcfdj()"/>
			<emp:radio id="PspSitecheckCom.djjgslcfdj" label="登记机关是否已受理查封、冻结" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_dzysdwcfdj()"/>
			<emp:date id="PspSitecheckCom.cfdjsj" label="查封、冻结时间"  required="true"/>
			<emp:textarea id="PspSitecheckCom.cfdjyy" label="原因说明" required="true" colSpan="2"/>	
			<emp:text id="PspSitecheckCom.cfdjsqr" label="查封、冻结申请人" required="true" />
			<emp:radio id="PspSitecheckCom.dzyrdbyy" label="抵（质）押人的担保意愿是否发生变化" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.dywsfcz" label="抵押物是否出租" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.sfczyxzqsxfxys" label="是否存在影响我行债权的顺利实现的风险因素" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.dzybhyxdbxl" label="抵（质）押物市场价值是否发生不利变化，影响担保效力" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:radio id="PspSitecheckCom.bgdzydjzmsfqq" label="保管的抵质押登记证明文件是否齐全，账实是否相符" required="true" dictname="STD_ZX_YES_NO" layout="false"/>
			<emp:textarea id="PspSitecheckCom.dzywcz_qtzk" label="抵质押物存在的其他状况" required="true" colSpan="2"/>
		</emp:gridLayout>
		</div>
		<div id="tzqk" style="display:none" >	
		<emp:gridLayout id="PspSitecheckComGroup" title="投资情况" maxColumn="2">
			<emp:text id="PspSitecheckCom.xmjhztz" label="项目计划总投资(万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.xmjhsjtr" label="实际已投入金额(万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.xmjhtrbl" label="项目计划投入比率" required="true" dataType="Rate" colSpan="2"/>
			<emp:text id="PspSitecheckCom.zbjjhtre" label="资本金计划投入额(万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.ydwzbj" label="已到位资本金（万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.zbjdwbl" label="到位比率" required="true" dataType="Rate" colSpan="2"/>
			
			<emp:text id="PspSitecheckCom.jhyhzrze" label="计划银行总融资额(万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.jhyhdwje" label="已到位金额（万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.jhyhdwbl" label="到位比率" required="true" dataType="Rate" colSpan="2"/>
			
			<emp:text id="PspSitecheckCom.whrze" label="我行融资额(万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.whrzdwje" label="已到位金额（万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.whrzdwbl" label="到位比率" required="true" dataType="Rate" colSpan="2"/>
			
			<emp:text id="PspSitecheckCom.qtzjjhtre" label="其他资金计划投入额(万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.qtzjdwje" label="已到位金额（万元）" required="true" dataType="Currency"/>
			<emp:text id="PspSitecheckCom.qtzjdwbl" label="到位比率" required="true" dataType="Rate" colSpan="2"/>		
		</emp:gridLayout>
		</div>
		<div id="xmjdqk" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGroup" title="项目进度情况" maxColumn="2">
			<emp:text id="PspSitecheckCom.xmjd" label="项目阶段" required="true" />
			<emp:date id="PspSitecheckCom.xmyjwgsj" label="预计完工时间"  required="true"/>
			<emp:text id="PspSitecheckCom.jhgcjd" label="计划工程进度" required="true" dataType="Rate"/>
			<emp:text id="PspSitecheckCom.sjgcjd" label="实际工程进度" required="true" dataType="Rate"/>
		</emp:gridLayout>
		</div>
		<div id="jcnr" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGroup" title="检查内容" maxColumn="2">
			<emp:radio id="PspSitecheckCom.zbjdwsyqk" label="资本金是否按计划足额到位，使用是否正常" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.qtczzjsfdw" label="其他筹资资金后续能否按计划到位" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.dksyhxmjdsfpp" label="贷款使用和项目形象进度是否匹配" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.gcjsjzsfsl" label="工程建设进展是否顺利，有无延期情况" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.xmsjtrcgs" label="项目实际投入是否超概算" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.xmdcjdsfzc" label="项目建成达产进度是否正常" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.xmdcsfdyqxy" label="项目达产后是否达到预期效益" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.scjssfddyq" label="生产技术、环保指标是否达到要求" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.jsglryszsfdyq" label="技术管理人员素质是否达到生产经营需要" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckCom.xswlhxsqdsfsc" label="销售网络和销售渠道是否顺畅" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
		</emp:gridLayout>
		</div>
		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

