<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">

	//选择借据POP，先判断该笔资产是否已做过卖出
	function returnAcc(data){
		IqpAverageAssetApp.bill_no._setValue(data.bill_no._getValue());
		IqpAverageAssetApp.cont_no._setValue(data.cont_no._getValue());
		IqpAverageAssetApp.cus_id._setValue(data.cus_id._getValue());
		IqpAverageAssetApp.cus_id_displayname._setValue(data.cus_id_displayname._getValue());

		IqpAverageAssetApp.loan_amt._setValue(data.bill_amt._getValue());
		IqpAverageAssetApp.loan_balance._setValue(data.bill_bal._getValue());
		IqpAverageAssetApp.distr_date._setValue(data.start_date._getValue());
		IqpAverageAssetApp.end_date._setValue(data.end_date._getValue());
		IqpAverageAssetApp.reality_ir_y._setValue(data.reality_ir_y._getValue());
		IqpAverageAssetApp.five_class._setValue(data.five_class._getValue());
		IqpAverageAssetApp.twelve_cls_flg._setValue(data.twelve_cls_flg._getValue());
		IqpAverageAssetApp.inner_owe_int._setValue(data.inner_owe_int._getValue());
		IqpAverageAssetApp.out_owe_int._setValue(data.out_owe_int._getValue());
		IqpAverageAssetApp.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		IqpAverageAssetApp.fina_br_id_displayname._setValue(data.fina_br_id_displayname._getValue());
		
		var bill_no = data.bill_no._getValue();
		var url = '<emp:url action="checkIqpAverageAssetApp.do"/>?bill_no='+bill_no;
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
				var msg = jsonstr.msg;
				if(flag == "success"){
					
				}else {
					alert(msg);

					IqpAverageAssetApp.bill_no._setValue("");
					IqpAverageAssetApp.cont_no._setValue("");
					IqpAverageAssetApp.cus_id._setValue("");
					IqpAverageAssetApp.cus_id_displayname._setValue("");

					IqpAverageAssetApp.loan_amt._setValue("");
					IqpAverageAssetApp.loan_balance._setValue("");
					IqpAverageAssetApp.distr_date._setValue("");
					IqpAverageAssetApp.end_date._setValue("");
					IqpAverageAssetApp.reality_ir_y._setValue("");
					IqpAverageAssetApp.five_class._setValue("");
					IqpAverageAssetApp.twelve_cls_flg._setValue("");
					IqpAverageAssetApp.inner_owe_int._setValue("");
					IqpAverageAssetApp.out_owe_int._setValue("");
					IqpAverageAssetApp.manager_br_id_displayname._setValue("");
					IqpAverageAssetApp.fina_br_id_displayname._setValue("");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	};
	function getCusForm(){
		var cus_id = IqpAverageAssetApp.cus_id._getValue();
		if(cus_id != "" && cus_id != null && cus_id != "null"){
		   var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };

    function getBillNoForm(){
    	var bill_no = IqpAverageAssetApp.bill_no._getValue();
		if(bill_no != "" && bill_no != null && bill_no != "null"){
		   var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+bill_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
    function getContForm(){
    	var cont_no = IqpAverageAssetApp.cont_no._getValue();
		if(cont_no != "" && cont_no != null && cont_no != "null"){
		   var url = "<emp:url action='getAllCtrDetailView.do'/>&pvp=pvp&cont_no="+cont_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
</script>