<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
    function onload(){
    	//PrdLiborMaintain.status._setValue('03');
    	PrdLiborMaintain.libor_date._setValue('${context.forwardDay}');
	    doQuery();
    };
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdLiborMaintain._toForm(form);
		PrdLiborMaintainList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdLiborMaintainPage() {
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("已提交成功！");
					window.location.reload();
				}else{
					alert("提交失败！");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="getPrdLiborMaintainUpdatePage.do"/>?flagV=fh';
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	
	function doViewPrdLiborMaintain() {
		var data = PrdLiborMaintainList._obj.getSelectedData();
		if (data.length == 1) {
			var pk_id = data[0].pk_id._getValue();
			var url = '<emp:url action="getPrdLiborMaintainViewPage.do"/>?pk_id='+pk_id;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdLiborMaintainPage() {
		var url = '<emp:url action="queryPrdLiborMaintainListImport.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
	};
	
	function doDeletePrdLiborMaintain() {
		var idx = PrdLiborMaintainList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length<=0){
			alert("请选择需要删除的记录！");
			return false;
        }
		var pk_ids = "";
		for(var i=0;i<idx.length;i++){
    		var status=PrdLiborMaintainList._obj.data[idx[i]]['status']._getValue();
    		if(status!="01"){
				alert("非初始化状态的记录，不能进行删除操作！");
				return;
			}	
    		pk_ids = pk_ids+PrdLiborMaintainList._obj.data[idx[i]]['pk_id']._getValue()+",";
        }
		pk_ids = pk_ids.substring(0,pk_ids.length-1);
		if(confirm("是否确认要删除？")){
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						alert("已删除！");
						window.location.reload();
					}else{
						alert("删除失败！");
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="deletePrdLiborMaintainRecord.do"/>?pk_ids='+pk_ids;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		}
	};
	
	function doReset(){
		page.dataGroups.PrdLiborMaintainGroup.reset();
	};
	//下载模板
	function doLoadPrdLiborMaintain(){
		var url = '<emp:url action="downLoadPrdLiborMaintain.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	//生效
	function doEffectPrdLiborMaintainPage(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("已生效！");
					window.location.reload();
				}else if("fail"==flag){
					alert("没有复核中状态的记录，无法做生效操作！");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="getPrdLiborMaintainUpdatePage.do"/>?flagV=sx';
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	//打回
	function doBackPrdLiborMaintainPage(){
		var idx = PrdLiborMaintainList._obj.getSelectedIdx();  //得到多选的记录行号
		if(idx.length<=0){
			alert("请选择需要打回的记录！");
			return false;
        }
		var pk_ids = "";
		for(var i=0;i<idx.length;i++){
    		var status=PrdLiborMaintainList._obj.data[idx[i]]['status']._getValue();
    		if(status!="02"){
    			alert("非复核中状态的记录，不能进行打回操作！");
				return;
			}	
    		pk_ids = pk_ids+PrdLiborMaintainList._obj.data[idx[i]]['pk_id']._getValue()+",";
        }
		pk_ids = pk_ids.substring(0,pk_ids.length-1);
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					alert("已打回成功！");
					window.location.reload();
				}else{
					alert("打回失败！");
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="getPrdLiborMaintainUpdatePage.do"/>?flagV=dh&pk_ids='+pk_ids;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">

	<form  method="POST" action="#" id="queryForm"  style="width: 1500">

	<emp:gridLayout id="PrdLiborMaintainGroup" title="输入查询条件" maxColumn="3">
			<emp:date id="PrdLiborMaintain.libor_date" label="LIBOR日期" />
			<emp:select id="PrdLiborMaintain.cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
			<emp:select id="PrdLiborMaintain.status" label="状态" dictname="STD_ZB_LIBOR_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="loadPrdLiborMaintain" label="下载模板" op="load"/>
		<emp:button id="getAddPrdLiborMaintainPage" label="导入" op="add"/>
		<emp:button id="viewPrdLiborMaintain" label="查看" op="view"/>
		<emp:button id="deletePrdLiborMaintain" label="删除" op="remove"/>
		<emp:button id="getUpdatePrdLiborMaintainPage" label="提交" op="update"/>
		<emp:button id="effectPrdLiborMaintainPage" label="生效" op="effect"/>
		<emp:button id="backPrdLiborMaintainPage" label="打回" op="back"/>
	</div>

	<emp:table icollName="PrdLiborMaintainList" pageMode="true" url="pagePrdLiborMaintainQuery.do" selectType="2">
		<emp:text id="pk_id" label="PK_ID" hidden="true"/>
		<emp:text id="libor_date" label="LIBOR日期" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="last_ir" label="隔夜" dataType="Rate"/>
		<emp:text id="one_week_ir" label="1周" dataType="Rate"/>
		<emp:text id="two_week_ir" label="2周" dataType="Rate"/>
		<emp:text id="one_month_ir" label="1个月" dataType="Rate"/>
		<emp:text id="two_month_ir" label="2个月" dataType="Rate"/>
		<emp:text id="three_month_ir" label="3个月" dataType="Rate"/>
		<emp:text id="four_month_ir" label="4个月" dataType="Rate"/>
		<emp:text id="five_month_ir" label="5个月" dataType="Rate"/>
		<emp:text id="six_month_ir" label="6个月" dataType="Rate"/>
		<emp:text id="seven_month_ir" label="7个月" dataType="Rate"/>
		<emp:text id="eight_month_ir" label="8个月" dataType="Rate"/>
		<emp:text id="nine_month_ir" label="9个月" dataType="Rate"/>
		<emp:text id="ten_month_ir" label="10个月" dataType="Rate"/>
		<emp:text id="eleven_month_ir" label="11个月" dataType="Rate"/>
		<emp:text id="twelve_month_ir" label="12个月" dataType="Rate"/>
		<emp:text id="imp_date" label="导入日期" />
		<emp:text id="maintain_id_displayname" label="维护人" />
		<emp:text id="check_id_displayname" label="复核人" />
		<emp:text id="status" label="状态" dictname="STD_ZB_LIBOR_STATUS" />
	</emp:table>
	</form>
</body>
</html>
</emp:page>
    