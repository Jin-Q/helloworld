//快捷菜单加载
/**
<ul> 
 <li><a><img src="<emp:file fileName='images/right_ico1.png'/>" /></a></li>
 <li><a><img src="<emp:file fileName='images/right_ico2.png'/>" /></a></li>
 <li><a><img src="<emp:file fileName='images/right_ico3.png'/>" /></a></li>
 <li><a><img src="<emp:file fileName='images/right_ico4.png'/>" /></a></li>
 <li><a><img src="<emp:file fileName='images/right_ico5.png'/>" /></a></li> 
</ul>
var shortcutCfg = {list:
	[{id:'sc1',url:'getCustInfoAddPage.do',menuid:'indivOpen',ico:'right_ico1.png',tip:'对私开户',order:1},  
	 {id:'sc2',url:'queryCustInfoListForIndiv.do',menuid:'indivedit',ico:'right_ico2.png',tip:'对私客户信息',order:2},
   {id:'sc3',url:'queryLmLoanList.do',menuid:'LmPmShd1',ico:'right_ico3.png',tip:'还款计划查询',order:3}
	]};
 */
//快捷菜单最大添加数
var MaxNumOfShortCut = 10;
/**
 * 快捷菜单初始化
 * @param EMP_SID
 */
function loadShortCut(EMP_SID){
	//快捷菜单查询URL设定，并且格式化
	var shortCutQryUrl = 'queryAllShortcutOfUsrSet.do?EMP_SID='+EMP_SID+'&sType=s';
	//发起异步查询请求
	$.ajax({
		type: "POST", 
		url:shortCutQryUrl,
		async:true,
		dataType:'text',
		success: function(_data) { 
			var shortcutBook;
			try{
				shortcutBook = eval("("+_data+")");
			}catch(e){
				alert("异步异常："+_data);//提示异步错误信息
				return ;
			}
			genHtmlpage(shortcutBook);//拼接html
		}
	});
};

//快捷菜单点击操作
function onSelectMenu(url, title){
	window.infoFrame.location = url; 
}

/**
 * Orgin风格首页右侧快捷菜单
 * @param selectlist
 */
function genHtmlpage(selectlist){
	$('#shortcutContent').empty();
	//设置返回的HTML元素
	var _html = "<ul>";
	if(selectlist != undefined){
		for(var n=0; n<selectlist.list.length; n++){
			if(selectlist.list[n].selected == 'true'){
				var _url = selectlist.list[n].url+'?EMP_SID='+empId+ '&menuId=' + selectlist.list[n].menuid;
				var _title = selectlist.list[n].tip;
				_html += "<li>";
				_html += "<a onclick='onSelectMenu(\""+_url+"\",\""+_title+"\")' title='"+_title+"' >";
				_html += "<img src='images/platform/common/shortcut/orgin/"+selectlist.list[n].ico+"'/>";
				_html += "</a>";
				_html += "</li>";
			}
		}
	}
	_html += "</ul>";
	_html += "<ul><div style='position:relative;top:12px;text-align:center;width:40px;'><font size='2'><strong><a onclick='doConfigSC();'>设置</a></strong></font></div></ul>";
	$('#shortcutContent').html(_html);
}


/****************************快捷菜单定制  start*****************/
/**
 * 点击设置，打开快捷菜单定制div
 */
function doConfigSC(){
	var url = 'queryAllShortcutOfUsrAccess.do?EMP_SID='+empId+'&sType=s';
	url = EMPTools.encodeURI(url);
	$.ajax({
		type:'get',
		url:url,
		dataType:"html",
		success:function(data){
			$('<div id="shortcutId"></div>').insertAfter(".mask_in .mask_close");
			
			var obj=eval("("+data+")");
			var allShortcutList=obj.list;
			
			for(var i=0;i<allShortcutList.length;i++){
				var le1MenuId = allShortcutList[i].le1MenuId;
				var	le1MenuDesc = allShortcutList[i].le1MenuDesc;
				if($("#shortcutTop_"+le1MenuId).length > 0){
					var li=$('<li></li>');
					var span=$('<span></span>');
					if(allShortcutList[i].selected=="true"){//已选
						$(span).css("display","block");
					}
					$(span).appendTo($(li));
					var imgPath = "images/platform/common/shortcut/orgin/"+allShortcutList[i].ico+"";
					var a=$('<a id="shortcut-'+allShortcutList[i].menuid+'"  onclick="addOrDelShortcut(this)"><div style="background:url('+imgPath+') center center no-repeat"></div><p title='+allShortcutList[i].tip+'>'+allShortcutList[i].tip+'</p></a>');
					
					$(a).appendTo($(li));
					$(li).appendTo($("#shortcutTop_"+le1MenuId).find('ul'));
					
				}else{//所属一级菜单分组还不存在
					var maskOut=$('<div id=shortcutTop_'+le1MenuId+' class="mask_out"></div>');
					$('<div class="mask_top">'+le1MenuDesc+'</div>').appendTo(maskOut);
					var ul=$('<ul></ul>');
					var li=$('<li></li>');
					var span=$('<span></span>');
					if(allShortcutList[i].selected=="true"){//已选
						$(span).css("display","block");
					}
					$(span).appendTo($(li));
					var imgPath = "images/platform/common/shortcut/orgin/"+allShortcutList[i].ico+"";
					var a=$('<a id="shortcut-'+allShortcutList[i].menuid+'"  onclick="addOrDelShortcut(this)"><div style="background:url('+imgPath+') center center no-repeat"></div><p title='+allShortcutList[i].tip+'>'+allShortcutList[i].tip+'</p></a>');
					
					$(a).appendTo($(li));
					$(li).appendTo(ul);
					$(ul).appendTo(maskOut);
					$("#shortcutId").append(maskOut);
				}
			}
			
		}	
	});
	$gd=$("#Container").height();
	$(".mask").show().height($gd);
	$(".mask_in").show();
};

/**
 * 点击关闭图片，关闭快捷菜单定制div
 */
function closeMask(){
	$("#shortcutId").remove();
	$(".mask").hide();
	$(".mask_in").hide();
}

/**
 * 点击图标事件
 * 1.若为已选图标，点击后删除该快捷菜单
 * 2.若为未选图标，点击后新增该快捷菜单
 */
function addOrDelShortcut(obj){
	var _id = $(obj).attr("id");
	var menuId = _id.split('-')[1];
	if($(obj).prev().css("display")=="block"){//已选菜单，将其删除
		var delUrl = 'delShortcutToUsr2.do?EMP_SID='+empId+"&menuid="+menuId+'&sType=s';
		delUrl = EMPTools.encodeURI(delUrl);
		
		$.ajax({
		  	url:delUrl,
		  	async:true,
		  	dataType:'html',
		  	success:function(data){
	            try{
	            	var jstr=eval("("+data+")");
	            	if(jstr.flag=='success'){
	            		$(obj).prev().hide();
	            		top.loadShortCut(empId);//重新加载首页快捷菜单
	            	}else{
	            		$.messager.alert('系统提示','删除快捷菜单失败！');
	            	}
	            }catch(e){
                   	EMP.alertException(data);
                   	return ;
	            }
               	
	      	}
	  	});
		
	}else{//未选菜单，将其新增
		var lis = $("#shortcutContent ul").find('li');
		if(lis.length < MaxNumOfShortCut){
			var addUrl = 'addShortcutToUsr2.do?EMP_SID='+empId+"&menuid="+menuId+'&sType=s';
			addUrl = EMPTools.encodeURI(addUrl);
			
			$.ajax({
			  	url:addUrl,
			  	async:true,
			  	dataType:'html',
			  	success:function(data){
		            try{
		            	var jstr=eval("("+data+")");
		            	if(jstr.flag=='success'){
		            		$(obj).prev().show();
		            		top.loadShortCut(empId);//重新加载首页快捷菜单
		            	}else{
		            		alert('新增快捷菜单失败！');
		            	}
		            }catch(e){
	                   	EMP.alertException(data);
	                   	return ;
		            }
		      	}
		  	});
		}else{
			alert('最多只能添加'+MaxNumOfShortCut+'个快捷菜单！');
			return ;
		}
	};
}





/*************************** 快捷菜单定制   end  *****************/