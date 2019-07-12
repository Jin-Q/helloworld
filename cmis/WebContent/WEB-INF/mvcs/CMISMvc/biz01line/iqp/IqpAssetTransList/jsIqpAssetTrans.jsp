<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
   function getCusForm(){
		var cus_id = IqpAssetTransList.cus_id._getValue();
		if(cus_id != "" && cus_id != null && cus_id != "null"){
		   var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };

    function getBillNoForm(){
    	var bill_no = IqpAssetTransList.bill_no._getValue();
		if(bill_no != "" && bill_no != null && bill_no != "null"){
		   var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+bill_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
    function getContForm(){
    	var cont_no = IqpAssetTransList.cont_no._getValue();
		if(cont_no != "" && cont_no != null && cont_no != "null"){
		   var url = "<emp:url action='getAllCtrDetailView.do'/>&pvp=pvp&cont_no="+cont_no;
		   url=EMPTools.encodeURI(url);  
     	   window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
           alert("请先选择借据!");
	    }
    };
</script>