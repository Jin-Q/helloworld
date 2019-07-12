<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		agr_no._toForm(form);
		sub_type._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){//(2014-06-27 bug3871 去除,getBusBalanceByArgno中判断去掉)
		var outstnd_amt = '${context.outstnd_amt}';   //本次占用金额
		
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
					var lmt_amt = jsonstr.lmt_amt;   //协议占用额度
					var crd_amt = LmtAgrDetailsList._obj.getParamValue(['crd_amt']);	//行业授信总额
					//if(outstnd_amt>(crd_amt-lmt_amt)){  //行业授信总额-占用金额
					//	alert("该授信台账额度["+crd_amt+"]，剩余额度["+(crd_amt-lmt_amt)+"]，本次应占用["+outstnd_amt+"]，不能覆盖业务敞口["+outstnd_amt+"]！");
					//	return false;
					//}
					
					var arr = new Array();
					arr[0] = LmtAgrDetailsList._obj.getParamValue(['agr_no']);	//授信协议编号
					arr[1] = crd_amt-lmt_amt-outstnd_amt;	//剩余额度=台账可用-已占用-本次占用
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
		var data = LmtAgrDetailsList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var prdIds = data[0]['prd_id']._getValue();  //适用产品
			var prd_id = '${context.prd_id}';
			if("" != prdIds && prdIds.indexOf(prd_id) < 0){
				alert("选择的额度适用产品不包含该业务品种，请重新选择！");
				return false;
			}
			var single_amt = data[0]['single_amt']._getValue();  //单户限额
			//if(outstnd_amt-single_amt>0){
			//	alert("该合作方授信单户限额["+single_amt+"]，不能覆盖业务敞口["+outstnd_amt+"]！");
			//	return false;
			//}
			var url = '<emp:url action="searchLmtAmt.do"/>&agr_no='+agr_no+'&lmt_type=${context.lmt_type}';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doSelect(){
		doReturnMethod();
	}

	function doViewAgrDetail(){
		var data = LmtAgrDetailsList._obj.getSelectedData();  //得到选中记录行
		if (data != null && data.length !=0) {
			var agr_no = LmtAgrDetailsList._obj.getSelectedData()[0].agr_no._getValue();
			//var url = '<emp:url action="getLmtIndusAgrViewPage.do"/>&agr_no='+agr_no+'&type=surp&showButton=N&menuId=indus_crd_agr&op=view';
			var url = '<emp:url action="viewLmtAgrInfo.do"/>&agr_no='+agr_no+'&type=surp&showButton=N&menuIdTab=indus_crd_agr&op=view';
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:1000px';
			window.open(url,'',param);
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
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:select id="sub_type" label="行业类型" dictname="STD_ZB_INDUS_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		本次应占用授信额度：<font color="red">${context.outstnd_amt}</font>
	</div>
	
	<div align="left">
		<emp:button id="returnMethod" label="选择返回"/>
		<emp:button id="viewAgrDetail" label="查看"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="true" url="pageSelectLmtAgrDetails.do?lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=3">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="sub_type" label="行业类型" dictname="STD_ZB_INDUS_TYPE"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="single_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="prd_id" label="适用产品" hidden="true"/>
	</emp:table>
	
	<div align="left">
		<emp:button id="returnMethod" label="选择返回"/>
	</div>
</body>
</html>
</emp:page>
    