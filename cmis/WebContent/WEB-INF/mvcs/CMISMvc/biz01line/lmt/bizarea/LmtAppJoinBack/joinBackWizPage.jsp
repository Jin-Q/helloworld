<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtAppJoinBackList.do"/>' + "&process=${context.process}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doNext(){
		//异步提交  //单户限额   single_max_amt 和  到期日期 end_date
		var paramStr = 'agr_no=' + LmtAppJoinBack.agr_no._getValue() + '&app_flag='+ LmtAppJoinBack.app_flag._getValue()+
		 '&single_max_amt=' + LmtAppJoinBack.single_max_amt._getValue() + 
		 '&end_date=' + LmtAppJoinBack.end_date._getValue();
		var url = '<emp:url action="addLmtAppJoinBackRecord.do"/>?'+paramStr + "&process=${context.process}";
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				var app_flag = LmtAppJoinBack.app_flag._getValue();//申请类型
				if(flag == 'suc'){
					var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr+'&app_flag='+app_flag+'&serno='+serno+'&process=${context.process}&op=update';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if( flag == 'existTaskUpd' ){//如果已存在待发起任务则跳转修改
					var alertMsg = '';
					if(app_flag=='1'){
						alertMsg = '已存在待发起的退圈申请,确认跳转修改?';
					}else{
						alertMsg = '已存在待发起的入圈申请,确认跳转修改?';
					}
					if(confirm(alertMsg)){
						var url = '<emp:url action="getLmtAppJoinBackUpdatePage.do"/>?'+paramStr+'&app_flag='+app_flag+'&serno='+serno+'&process=${context.process}&op=update';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}else if(flag == 'existTaskView'){//如果已经存在提交任务则跳转查看
					var url = '<emp:url action="getLmtAppJoinBackViewPage.do"/>?'+paramStr+'&app_flag='+app_flag+'&serno='+serno+'&process=${context.process}&op=view';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag == 'existTask'){//存在在途退圈申请不能发起
					if(app_flag=='1'){
						alert('存在在途入圈申请，不能发起新的退圈申请！');
					}else{
						alert('存在在途退圈申请，不能发起新的入圈申请！');
					}
				}else{
					alert("圈内无有效客户!");
			 	}
			}
		};
	    var handleFailure = function(o){};
	    var callback = {
	    	success:handleSuccess,
	    	failure:handleFailure
	    };
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
	
	function setBizAreaNo(data){
		LmtAppJoinBack.agr_no._setValue(data.agr_no._getValue());
		LmtAppJoinBack.single_max_amt._setValue(data.single_max_amt._getValue());
		LmtAppJoinBack.end_date._setValue(data.end_date._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<div>
		<br></br>
	</div>
	<emp:gridLayout id="LmtAppJoinBackGroup" title="入圈/退圈申请表" maxColumn="2">
			<emp:select id="LmtAppJoinBack.app_flag" label="申请标识" required="true" dictname="STD_LMT_APP_FLAG" colSpan="2"/>
			<emp:pop id="LmtAppJoinBack.agr_no" label="圈商编号" url="queryLmtAgrBizAreaList.do?type=select" required="true" returnMethod="setBizAreaNo"/>
			<emp:text id="LmtAppJoinBack.serno" label="业务编号" hidden="true"/>
			<emp:text id="LmtAppJoinBack.single_max_amt" label="单户限额" hidden="true"/>
			<emp:text id="LmtAppJoinBack.end_date" label="到期日期" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="next" label="下一步"/>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
