/**
 * 页面加载完成后，如果按钮tip属性不为空，则鼠标移动到按钮上时，给出自定义提示，只在皮肤为"concise"的情况下生效
 * 1. 消费合并：添加.toolbar下的button的弹出支持
 */
$(function(){
	if(layout != "concise"){
		return;
	}
	
	//如果在"concise"皮肤下，toolbar中linkbutton的text不需要设置，只需要设置tip
	$(".datagrid-toolbar a.easyui-linkbutton").each(function(){
		if($(this).linkbutton("options").iconCls) {
			var text = $(this).linkbutton("options").text;
			$(this).attr("tip", text);
			$(this).find(".l-btn-text").text("");
		}
	});
	
	//鼠标悬浮时创建p标签 
	$(".datagrid-toolbar a.easyui-linkbutton").mouseover(function(){
		if($(this).attr("tip")){
			$("<p>"+$(this).attr("tip")+"</p>").appendTo($(this));
			var _wid=$(this).children("p").width();
			$(this).children("p").css("margin-left",-(_wid/2+12)+"px");
		}
	});
	//鼠标离开时删除p标签 
	$(".datagrid-toolbar a.easyui-linkbutton").mouseout(function(){
		$(this).children("p").remove();
	});
	
	// 消费合并：鼠标悬浮时创建p标签
    $(".toolbar a.easyui-linkbutton").mouseover(function() {
        $("<p>" + $(this).attr("tip") + "</p>").appendTo($(this));
        var _wid = $(this).children("p").width();
        $(this).children("p").css("margin-left", -(_wid / 2 + 12) + "px");
    });
    // 消费合并：鼠标离开时删除p标签
    $(".toolbar a.easyui-linkbutton").mouseout(function() {
        $(this).children("p").remove();
    });
})
