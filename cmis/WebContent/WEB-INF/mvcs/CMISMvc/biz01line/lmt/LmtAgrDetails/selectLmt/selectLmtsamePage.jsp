<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<html>
<head>
<title>选择额度品种占用</title>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String lmt_type = context.getDataValue("lmt_type").toString();
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	var lmt_amt =0.00;
	var grp_lmt_amt = 0.00;
	function doQuery(){
		var form = document.getElementById('queryForm');
		sign_agr_no._toForm(form);
		same_cus_id._toForm(form);
		same_cus_id_displayname._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){//(2014-06-27 bug3871 去除,getBusBalanceByArgno中判断去掉)
		var lmt_type = '02';   //授信类型
		var prd_id = '${context.prd_id}';   //产品
		var data = LmtAgrDetailsList._obj.getSelectedData();  //得到选中记录行
		if (data != null && data.length !=0) {  //判断如果选中的记录未空提示需选择记录
			var prdIds = data[0]['prd_id']._getValue();  //适用产品
			if("" != prdIds && prdIds.indexOf(prd_id) < 0){
				alert("选择的额度适用产品不包含该业务品种，请重新选择！");
				return false;
			}
			//实时查询剩余额度信息 （业务模块提供接口）	
			if("01"==lmt_type){  //单一法人授信
				getBusBalanceByArgno(data[0].limit_code._getValue(),lmt_type);
			}else{   //同业
				getBusBalanceByArgno(data[0].agr_no._getValue(),lmt_type);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*
	 * 根据授信协议编号和授信类型查询协议下关联业务合同余额。
	 * agr_no: 授信协议编号
	 * lmt_type:授信类型
	 */
	function getBusBalanceByArgno(agr_no,lmt_type){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					var lmt_amt = jsonstr.lmt_amt;   //额度编码占用额度
					var grp_lmt_amt = jsonstr.grp_lmt_amt;  //集团占用总金额

					var outstnd_amt = '${context.outstnd_amt}';   //本次占用金额
					//if(outstnd_amt>(crd_lmt-lmt_amt)){
					//	alert("该授信台账可用额度["+crd_lmt+"]，剩余额度["+(crd_lmt-lmt_amt)+"]，本次应占用["+outstnd_amt+"]，不能覆盖业务敞口["+outstnd_amt+"]！");
					//	return false;
					//}

					var agr_no = ""; 
					if("01"==lmt_type ){   //单一法人授信
						var crd_lmt = LmtAgrDetailsList._obj.getParamValue(['crd_amt']);  //授信额度（减去冻结金额）
						agr_no = LmtAgrDetailsList._obj.getParamValue(['limit_code']);	//额度台账编号
						/**根据集团融资模式，判断是否需要校验集团授信总额   */
						var finance_type = '${context.finance_type}';	//集团融资模式
						if("01"==finance_type){
							var grp_amt = '${context.grp_amt}';	//集团授信总额
						
							//if(grp_amt<(grp_lmt_amt+grp_lmt_amt)){   //集团授信总额小于集团占用总金额+本次占用金额
							//	alert("集团授信总额["+grp_amt+"]，已用额度["+grp_lmt_amt+"]，本次应占用["+outstnd_amt+"]，集团授信总额不够！");
							//	return false;
							//}
						}
					}else if("02"==lmt_type){  //同业
						crd_lmt = LmtAgrDetailsList._obj.getParamValue(['lmt_amt']);
						agr_no = LmtAgrDetailsList._obj.getParamValue(['agr_no']);	//额度台账编号
					}

					var arr = new Array();
					arr[0] = agr_no;
					arr[1] = crd_lmt-lmt_amt-outstnd_amt;	//剩余额度=台账可用-已占用-本次占用
					window.top.opener["${context.returnMethod}"](arr);	 	
					window.top.close();
				}else {
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
		var url = '<emp:url action="searchLmtAmt.do"/>&agr_no='+agr_no+'&lmt_type='+lmt_type;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	}

	function doSelect(){
		doReturnMethod();
	}
	window.onload =function(){
		//LmtAgrDetailsList._obj._addSelectEvents(this.tBodyMain, "click", this.rowClick, this);
		//EMP.util.Tools.addEvent (LmtAgrDetailsList.tBodyMain, "click", rowClick(), this);
		//LmtAgrDetailsList._obj.getSelectedData().click = rowClick();
	}
	
	function rowClick(){
		alert("rowClick.....");
	}

	function viewDetail(){
		var paramStr = AccpDetailList._obj.getParamStr(['serno']);
	}

	function doViewAgrDetail(){
		var data = LmtAgrDetailsList._obj.getSelectedData();  //得到选中记录行
		if (data != null && data.length !=0) {
			var limit_code = LmtAgrDetailsList._obj.getSelectedData()[0].agr_no._getValue();
			var url = '<emp:url action="viewLmtAgrInfo.do"/>&agr_no='+limit_code+'&type=surp&menuIdTab=LmtIntbankAcc&op=view&type=surp&showButton=N';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		}else{
			alert('请先选择一条记录！');
			return;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="LmtAgrDetailsGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="sign_agr_no" label="授信协议编号" />
		<emp:text id="same_cus_id" label="客户码" />
		<emp:text id="same_cus_id_displayname" label="客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		本次应占用授信额度：<font color="red">${context.outstnd_amt}</font>
	</div>
	
	<div align="left">
		<emp:button id="returnMethod" label="选择返回"/>
		<emp:button id="viewAgrDetail" label="查看"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="true" 
		url="pageSelectLmtAgrDetails.do?lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=5">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="lmt_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="enable_amt" label="可用金额" dataType="Currency"/>
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
		<emp:text id="prd_id" label="适用产品" hidden="true"/>
	</emp:table>
	
	<div align="left">
		<emp:button id="returnMethod" label="选择返回"/>
	</div>
	
</body>
</html>
</emp:page>
    