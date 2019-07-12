<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = context.getDataValue("type").toString();
%>
<emp:page>

<html>
<head>

<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function addActrecRepay() {
		var po_no = "${context.po_no}";
		var cont_no = "${context.cont_no}";
		var invc_no = "${context.invc_no}";
		//var idx = IqpCoreBailDetailList._obj.getSelectedIdx();  //得到多选的记录行号
		var data = IqpCoreBailDetailList._obj.getSelectedData();
		if(data.length >= 1){
			//var paramStr = IqpCoreBailDetailList._obj.getParamStr(["serno","bail_amt"]);
			//组装多记录选择返回参数
			var paramStr = "";
			for(var i=0;i<data.length;i++){
				paramStr += data[i].serno._getValue()+","+data[i].bail_amt._getValue()+";";
			}
			if(""!=paramStr){
				paramStr = paramStr.substr(0,paramStr.length-1);
			}
			var url = '<emp:url action="addRIqpActrecRepay.do"/>?po_no='+po_no+"&cont_no="+cont_no+"&invc_no="+invc_no+"&params="+paramStr;

			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						window.parent.location.reload();
					}else if(flag == "confirm"){
						if(confirm(msg)){
							updateActrecStatus();
						}else{   //如果不确定回款完成，直接刷新回款明细页面及回款登记页面
							window.parent.location.reload();
							window.location.reload();
						}
					}else{
						alert(msg);
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
			url = EMPTools.encodeURI(url);

			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		}else{
			alert('请至少选择一笔流水明细！');
		}
	};

	//异步修改应收账款的状态
	function updateActrecStatus(){
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
					alert("该应收账款回款已完成。");
					//window.parent.parent.location.reload();  //刷新应收账款列表页面
					//window.parent.location.reload();  //刷新回款登记页面
					window.parent.opener.location.reload();  //刷新应收账款列表页面
					window.parent.close();
				}else {
					alert(jsonstr.msg);
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
		var po_no = "${context.po_no}";
		var cont_no = "${context.cont_no}";
		var invc_no = "${context.invc_no}";
		var url = '<emp:url action="updateActrecStatus.do"/>?po_no='+po_no+"&cont_no="+cont_no+"&invc_no="+invc_no;
		
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function deleteActrecRepay(){
		var paramStr = RIqpActrecRepayList._obj.getParamStr(['tran_serno']);
		if (paramStr != null) {
			if(confirm("是否确认解除选择回款明细与应收账款的关系？")){
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
							alert("关系解除成功！");
							window.parent.location.reload();
							window.location.reload();
						}else {
							alert(jsonstr.msg);
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

				var cont_no = "${context.cont_no}";
				var invc_no = "${context.invc_no}";
				
				var url = '<emp:url action="deleteRIqpActrecRepay.do"/>?cont_no='+cont_no+"&invc_no="+invc_no+"&"+paramStr
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>
		<%if(!type.equals("view")){ %>
		<b style="font-size: 13">待回款明细</b>
		<div align="left">
			<input type="button" value="关联" onclick="addActrecRepay()" class="button80" >
		</div>
		
		<emp:table icollName="IqpCoreBailDetailList" selectType="2" pageMode="false" url="">
			<emp:text id="serno" label="流水号"/>
			<emp:text id="organno_displayname" label="机构码" />
			<emp:text id="input_date" label="回款日期" cssTDClass="tdCenter"/>
			<emp:text id="bail_acct_no" label="保证金账号" />
			<emp:text id="bail_amt" label="金额" dataType="Currency"/>
			<emp:text id="gylsh" label="柜员流水号" />
		</emp:table>
		<br>
		<% } %>
		<b style="font-size: 13">已回款明细</b>
		<div align="left">
		<%if(!type.equals("view")){ %> 
			<input type="button" value="解除关联" onclick="deleteActrecRepay()" class="button80">
		<% } %>
		</div>
		<emp:table icollName="RIqpActrecRepayList" selectType="2" pageMode="false" url="">
			<emp:text id="po_no" label="池编号" hidden="true"/>
			<emp:text id="tran_serno" label="流水号" />
			<emp:text id="cont_no" label="贸易合同编号" />
			<emp:text id="organno_displayname" label="机构码" />
			<emp:text id="input_date" label="回款日期" cssTDClass="tdCenter"/>
			<emp:text id="bail_acct_no" label="保证金账号" hidden="true"/>
			<emp:text id="bail_amt" label="金额" dataType="Currency"/>
			<emp:text id="gylsh" label="柜员流水号" />
		</emp:table>
</body>
</html>
</emp:page>
    