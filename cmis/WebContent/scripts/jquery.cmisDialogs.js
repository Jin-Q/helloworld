(function($){
	//行政区划Dialog
	jQuery.fn.regStateDialog = function(regStateCode,regAreaName, scrollWindow, dlgName, jsonObj){
		
	    var dlgPosition = getElementPosition(regStateCode);
	 
	    var dlgWidth = "250";
	    //var dlgHeight = "100";
	 
	    var dlgContent = "";
	    var oddColor = "#F4F4F4";
	    var evenColor = "#FFFFFF"
	    var color = oddColor;
	    var city = jsonObj.children[0].label;

	    isAbleScrollBar("false");

	    isShowCoverIframe("true",scrollWindow);
	
	    regStateCode.after("<div id=" + dlgName + "></div>");//$('#coverIframe')

	    alert(jsonObj);
	    alert(jsonObj.children[0].children.length);
	    for (var i = 0; i < jsonObj.children[0].children.length; i++) {
	    	
            var code = jsonObj.children[0].children[i].id;
            var area = jsonObj.children[0].children[i].label;
            dlgContent += "<div style='float:left;width:" + 1/dlgRow*100 + "%;font-size:14;background-color:"+color+";'><a id=" + code + " hef='#' title=" + area + " style='cursor:hand;'>" + area + "</a></div>";
            if ((i + 1) % dlgRow == 0) {
                dlgContent += "<br>";
                if(color==oddColor){
	            	color=evenColor;
	            }else{
	            	color=oddColor;
	            }
            }
        }

	    $('#' + dlgName).append("<span id='options'>" + dlgContent + "</span>");
	
	    $('#' + dlgName).dialog({
	        position: [dlgPosition[0] , dlgPosition[1]-document.body.scrollTop],//dlgPosition[1]
	        title: dlgName,
	        draggable: false,
	        modal: true,
	        resizable: false
	    });
	    $("#options a").bind('click', function(){
	        regStateCode.val($(this).attr("id"));
	        regAreaName.val(city+"->"+$(this).attr("title"));
	        isAbleScrollBar("true");
	        isShowCoverIframe("false",scrollWindow);
	        $('#' + dlgName).remove();
	    });
	    $('#' + dlgName).bind('dialogbeforeclose', function(){
	        isAbleScrollBar("true");
	        isShowCoverIframe("false",scrollWindow);
	        $(this).remove();
	    });
	    $('#' + dlgName).dialog("option", "width", dlgWidth);
	};

	//贷款种类Dialog
	jQuery.fn.LoanType = function(prdCode, prdName, prdPk, dlgName, jsonObj){
	    var dlgPosition = getElementPosition(prdCode);
	    var dlgWidth = "400";
	    //var dlgHeight = "300";
	    var dlgRow = "2";
	    var dlgContent = "";
	    var oddColor = "#F4F4F4";
	    var evenColor = "#FFFFFF"
	    var color = oddColor;
	    //isAbleScrollBar("false");
	    isShowCoverIframe("true","default");
	    $('#coverIframe').after("<div id=" + dlgName + "></div>");
	    for (var i = 0; i < jsonObj.PrdBasicinfoList.length; i++) {
	        var Code = jsonObj.PrdBasicinfoList[i].prd_code;
	        var Name = jsonObj.PrdBasicinfoList[i].prd_name;
	        var Pk = jsonObj.PrdBasicinfoList[i].prd_pk;
	        dlgContent += "<div style='float:left;height:20;width:" + 1/dlgRow*100 + "%;font-size:14;background-color:"+color+";'><img src='/cmis-main/images/annew.gif'/><a id=" + Code + " hef='#' name=" + Name + " title=" + Pk + " style='cursor:hand;'>" + Name + "</a></div>";
	        if ((i + 1) % dlgRow == 0) {
	            dlgContent += "<br>";
	            if(color==oddColor){
	            	color=evenColor;
	            }else{
	            	color=oddColor;
	            }
	        }
	    }
	    $('#' + dlgName).append("<div id='options'>" + dlgContent + "</div>");
	    $('#' + dlgName).dialog({
	        position: [dlgPosition[0], dlgPosition[1]],
	        title: dlgName,
	        draggable: true,
	        modal: true,
	        resizable: false
	    });
	    $("#options a").bind('click', function(){
	        //prdCode.val("123");
	        prdCode.val($(this).attr("id"));
	        prdName.val($(this).attr("name"));
	        prdPk.val($(this).attr("title"));
	        //isAbleScrollBar("true");
	        isShowCoverIframe("false","default");
	        $('#' + dlgName).remove();
	    });
	    $('#' + dlgName).dialog("option", "width", dlgWidth);
	    $('#' + dlgName).bind('dialogbeforeclose', function(){
	        //isAbleScrollBar("true");
	        isShowCoverIframe("false","default");
	        $(this).remove();
	    });
	};
//    jQuery.fn.cmisDialogs = function(){};

    function isAbleScrollBar(isTrue){
    	if(isTrue=="true"){//开启滚动条
			$("body").attr({"scroll":"auto"});
			$("div:not(.emp_gridlayout_title)").css({
	            "overflow": "auto"
	        });
    	}else if(isTrue=="false"){//关闭滚动条
			$("body").attr({"scroll":"no"});
			$("div:not(.emp_gridlayout_title)").css({
	            "overflow": "hidden"
	        });
    	}
    };

    function isShowCoverIframe(isTrue,scrollWindow){
		if(isTrue=="true"){
			if(scrollWindow=="default"){
				$("body div:last-child").after("<iframe id='coverIframe' src='#'/>");
			}else{
				scrollWindow.after("<iframe id='coverIframe' src='#'/>");
			}
	        $('#coverIframe').css({
	            "position": "absolute",
	            "filter": "alpha(opacity=1)",
	            "left": 0,
	            "top": 0,
	            "width": "100%",
	            "height": "100%"
	        });
	        $('#coverIframe').show();
    	}else if(isTrue=="false"){
			var cif=$('#coverIframe');
			if(cif.length>0){
				$('#coverIframe').remove();
			}
    	}
    };

    function getElementPosition(regStateCode){//计算控件位置
		var offset = regStateCode.offset();
        var left = offset.left;
        var top = offset.top + regStateCode.height();
        return [left, top];
    };
})(jQuery);