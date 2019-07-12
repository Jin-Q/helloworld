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
		CusHandoverLst._toForm(form);
		CusHandoverLstList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusHandoverLstGroup.reset();
	};

	//添加移交明细
	function doGetCusLstPage() {
        var scope = '${context.scope}';    //移交范围
        var handBrId = '${context.handover_br_id}';
        var handId = '${context.handover_id}';
        var recBrId = '${context.receiver_br_id}';
        var recId = '${context.receiver_id}';
        var serno = '${context.serno}';
        var handover_mode = '${context.handover_mode}';
        
        if(scope!=null&& scope!=""){
        	if(scope!="1"){
            	//其他都是自动过渡，所以要提醒他删除完成，再增加
            	var pageSize = "${context.pageInfo.recordSize}";
				if(pageSize>0){
                      alert("已添加移交客户，为避免添加移交范围外的移交客户，请删除移交明细，重新添加客户！");
                      return;
				}
			}
            
		   var str = "handover_br_id="+handBrId+"&handover_id="+handId
			+"&receiver_br_id="+recBrId+"&receiver_id="+recId+"&serno="+serno
			+"&handover_mode="+handover_mode
			+"&handover_scope=" + scope;
		var url = '<emp:url action="addCusHandoverLstRecordMany.do"/>&'+str;
		url = EMPTools.encodeURI(url);
		    
		var handleSuccess = function(o) {
    		if (o.responseText) {
				try {
		    		var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
				    alert("Parse jsonstr define error!" + e);
				    return;
				}
				var flag = jsonstr.flag;
				if (flag == "suc") {
			    	//范围 为 1 按客户移交 需要断则，其他的都是自动过渡到对应的 移交明细表中
				    if(scope != '1' ){
						alert("添加成功!");
						doQuery();
				    } else{
						var url = '<emp:url action="getCusListForHandover.do"/>&'+str;
						url = EMPTools.encodeURI(url);
						EMPTools.openWindow(url,'newwindow');
					}
				} else {
				    alert("添加失败");
				    return;
				}
			}
		};
		var handleFailure = function(o) {
		    alert("添加失败");
		    return;
		};
		
		var callback = {
		    success : handleSuccess,
		    failure : handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}
	};

	//删除明细信息
	function doDeleteCusHandoverLst() {
		var paramStr = CusHandoverLstList._obj.getParamStr(['serno','cus_id']);
        //如果为所有客户移交，删除时一次删除
		var scope = '${context.scope}'; //移交范围
		if(scope != "1"){
		     //如果移交范围 不是按 客户移交 由于增加的时候是 全部增加，那么删除将会全部删除
		     paramStr = "&scope=" + scope+"&serno="+'${context.serno}';
		}
        if (paramStr != null) {
            if(confirm("是否确认要删除？")){
                var url = '<emp:url action="deleteCusHandoverLstRecord.do"/>?'+paramStr;
                url = EMPTools.encodeURI(url);
                var handleSuccess = function(o){
                    if(o.responseText !== undefined) {
                         try {
                             var jsonstr = eval("("+o.responseText+")");
                         } catch(e) {
                             alert("Parse jsonstr define error!"+e);
                             return;
                         }
                         var flag = jsonstr.flag;
                         if(flag=="删除成功"){
                             alert("删除成功!");
							 doQuery();
                        }else {
                          alert(flag);
                          return;
                        }
                    }
                };
                var handleFailure = function(o){    
                };
                var callback = {
                    success:handleSuccess,
                    failure:handleFailure
                }; 
                var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
            }
        } else {
            alert('请先选择一条记录！');
        }
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm" >
	</form>
	<emp:text id="CusHandoverLst.serno" label="申请流水号" defvalue="${context.CusHandoverApp.serno}" hidden="true" />
	
	<div align="left">
		<emp:button id="getCusLstPage" label="添加移交明细" op="add"/>
		<emp:button id="deleteCusHandoverLst" label="删除" op="remove"/>
	</div>

	<emp:table icollName="CusHandoverLstList" pageMode="true" url="pageCusHandoverLstQuery.do" reqParams="CusHandoverLst.serno=$CusHandoverLst.serno;">
		<emp:text id="serno" label="申请流水号" hidden="true"/>
		<emp:text id="handover_type" label="业务类型" dictname="STD_ZB_OP_TYPE" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="business_detail" label="移交业务说明" />
	</emp:table>
	
</body>
</html>
</emp:page>
    