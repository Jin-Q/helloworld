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
		limit_code._toForm(form);
		search_cus_id._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){//(2014-06-27 bug3871 去除,getBusBalanceByArgno中判断去掉)
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

					var crd_lmt = LmtAgrDetailsList._obj.getParamValue(['crd_amt']);  //授信额度（减去冻结金额）
					var outstnd_amt = '${context.outstnd_amt}';   //本次占用金额
					//if(outstnd_amt>(crd_lmt-lmt_amt)){
					//	alert("该授信台账额度["+crd_lmt+"]，剩余额度["+(crd_lmt-lmt_amt)+"]，本次应占用["+outstnd_amt+"]，不能覆盖业务敞口["+outstnd_amt+"]！");
					//	return false;
					//}
					
					/**根据集团融资模式，判断是否需要校验集团授信总额   */
					var is_grp = jsonstr.is_grp;	//是否集团客户
					if("1"==is_grp){  //是集团客户时校验
						var grp_amt = '${context.grp_amt}';	//集团授信总额
					
						//if(grp_amt<(grp_lmt_amt+grp_lmt_amt)){   //集团授信总额小于集团占用总金额+本次占用金额
						//	alert("集团授信总额["+grp_amt+"]，已用额度["+grp_lmt_amt+"]，本次应占用["+outstnd_amt+"]，集团授信总额不够！");
						//	return false;
						//}
					}

					var arr = new Array();
					arr[0] = LmtAgrDetailsList._obj.getParamValue(['limit_code']);	//额度台账编号
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
		var data = LmtAgrDetailsList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var agr_no = data[0]['limit_code']._getValue();
			var url = '<emp:url action="searchLmtAmt.do"/>&agr_no='+agr_no+'&lmt_type=01';
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		} else {
			alert('请先选择一条记录！');
		}
		
	};

	function doSelect(){
		doReturnMethod();
	}

	function onLoad(){
		//var prd_id = '${context.prd_id}';   //产品
		//if("300020"!=prd_id){
		//	alert("该笔业务非商票贴现业务，不能使用商贴额度！");
		//	return false;
		//}
	}

	function doViewAgrDetail(){
		var data = LmtAgrDetailsList._obj.getSelectedData();  //得到选中记录行
		if (data != null && data.length !=0) {
			var agr_no = LmtAgrDetailsList._obj.getSelectedData()[0].agr_no._getValue();
				var url = '<emp:url action="getLmtAgrInfoViewPage.do"/>&agr_no='+agr_no+'&type=surp&showButton=N&menuIdTab=LmtIntbankAcc';
				url = EMPTools.encodeURI(url);
				var param = 'dialogWidth:1000px';
				window.open(url,'',param);
		}else{
			alert('请先选择一条记录！');
			return;
		}
	};

	function returnCus(data){
		search_cus_id._setValue(data.cus_id._getValue());
		cus_name._setValue(data.cus_name._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<form  method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="LmtAgrDetailsGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="额度编码" />
		<emp:pop id="cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
		<emp:text id="search_cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		本次应占用授信额度：<font color="red">${context.outstnd_amt}</font>
	</div>
	
	<div align="left">
		<emp:button id="returnMethod" label="选择返回"/>
		<emp:button id="viewAgrDetail" label="查看"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="true" url="pageSelectLmtAgrDetails.do?lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=4">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:select id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:text id="limit_code" label="额度编码" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/> 
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
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
    