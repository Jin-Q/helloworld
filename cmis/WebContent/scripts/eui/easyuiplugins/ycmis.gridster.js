/**
 * 初始化磁贴布局
 */
$(function(){
	var gridsters;
	var gridster;
	
	gridsters = $(".gridster-f");

    $.each(gridsters, function() {
      var gridsterId = $(this).attr("id");
      var gridsterPanels = $(this).find("li");
      //循环每一个小磁贴
      gridsterPanels.each(function(pi) {
    	  //点击磁贴事件
    	  var clickUrl = $(this).attr("clickUrl");
    	  //将磁贴事件绑定到磁贴
    	  if(typeof(clickUrl) == 'string' && clickUrl.length > 0) {
    		  $(this).bind("click", function(){
    			  eval(clickUrl+"()"); 
    		  });
    	  }
    	  //内嵌panel的src
    	  var contentLink = $(this).attr("contentLink");
    	  //若有contentLink，则磁贴内填充panel，而contentLink为panel的src
    	  if(typeof(contentLink) == 'string' && contentLink.length > 0) {
    		  //清除磁贴里内容
    		  $(this).empty();
    		  $(this).append("<div class='easyui-panel' fit='true' href='"+contentLink+"'></div>");
          }else {
        	  //背景图片
        	  var imgSrc = $(this).attr("imgSrc");
        	  if(typeof(imgSrc) == 'string' && imgSrc.length > 0) {
        		  $(this).css("background", "url("+imgSrc+") no-repeat center");
        	  }
        	  //标题
        	  var title = $(this).attr("title");
        	  var titleClass = $(this).attr("titleClass");
        	  if(typeof(title) == 'string' && title.length > 0) {
        		//清除磁贴里内容
        		$(this).empty();
        		$(this).append("<div class='"+titleClass+"'>"+title+"</div>");
        	  }
          }
    	  
      });
      
        //必填项 每个模块的基本单位的宽度和高度  模块的边距
  	  var widget_base_width = Number($(this).attr("widgetBaseWidth"));
  	  var widget_base_height = Number($(this).attr("widgetBaseHeight"));
  	  var widget_top_margins = Number($(this).attr("widgetTopMargins"));
  	  var widget_left_margins = Number($(this).attr("widgetLeftMargins"));
  	  //初始化时是否可以重叠
  	  var avoidOverlappedWidgets = $(this).attr("avoidOverlappedWidgets");
  	  //序列化调用函数
  	  var serializeParams = $(this).attr("serializeParams");
  	  //拖拽时函数
  	  var onDraggableStart = $(this).attr("onDraggableStart");
  	  var onDraggableDrag = $(this).attr("onDraggableDrag");
  	  var onDraggableStop = $(this).attr("onDraggableStop");
		  //改变大小属性
  	  var resizeEnabled = $(this).attr("resizeEnabled");
  	  var resizeAxes = $(this).attr("resizeAxes");
  	  if(typeof(resizeAxes) == 'string' && resizeAxes.length > 0) {
  		  resizeAxes = 'both';
  	  }
  	  var resizeHandleClass = $(this).attr("resizeHandleClass");
  	  var handleAppendTo = $(this).attr("handleAppendTo");
  	  var resizeMaxSizeWidth = Number($(this).attr("resizeMaxSizeWidth"));
  	  var resizeMaxSizeHeight = Number($(this).attr("resizeMaxSizeHeight"));
		  var resizeMaxSize = [];
		  resizeMaxSize.push(resizeMaxSizeWidth == 0 ? 20 : resizeMaxSizeWidth);
		  resizeMaxSize.push(resizeMaxSizeHeight == 0 ? 20 : resizeMaxSizeHeight);

  	  var resizeStart = $(this).attr("resizeStart");
  	  var resizeResize = $(this).attr("resizeResize");
  	  var resizeStop = $(this).attr("resizeStop");
  	  
  	  gridster = $(this).find("ul").gridster({
  	        widget_base_dimensions: [widget_base_width, widget_base_height],
  	        widget_margins: [widget_top_margins, widget_left_margins],
  	        avoid_overlapped_widgets: avoidOverlappedWidgets == "false" ? false : true, 
  	        serialize_params : function($w, wgd) { 
  	        	if(typeof(serializeParams) == 'string' && serializeParams.length > 0) {
          	        	return eval(serializeParams+"($w, wgd)"); 
              	}
         		return { col: wgd.col,   
                     row: wgd.row,   
                     size_x: wgd.size_x,   
                     size_y: wgd.size_y}  
      	        
      	    },
      	    draggable:{
          	    start: function(event, ui){
          	    	if(typeof(onDraggableStart) == 'string' && onDraggableStart.length > 0) {
          	            eval(onDraggableStart+"(event, ui)"); 
                  	}
              	},
              	drag: function(event, ui){
              		if(typeof(onDraggableDrag) == 'string' && onDraggableDrag.length > 0) {
          	            eval(onDraggableDrag+"(event, ui)"); 
                  	}
                },
                  stop: function(event, ui){
                	if(typeof(onDraggableStop) == 'string' && onDraggableStop.length > 0) {
          	            eval(onDraggableStop+"(event, ui)"); 
                  	}
         	 	},
         	 	resize: {
             	 	enabled: resizeEnabled == "false" ? false : true,
             	    axes: [resizeAxes],
             	 	handle_class:resizeHandleClass,
             	 	handle_append_to:handleAppendTo,
             	 	max_size:resizeMaxSize,
             	 	start: function(e, ui, $widget) {
             	 		if(typeof(resizeStart) == 'string' && resizeStart.length > 0) {
             	 			eval(resizeStart+"(e, ui, $widget)"); 
	                	}
                 	},
             	 	resize: function(e, ui, $widget) {
             	 		if(typeof(resizeResize) == 'string' && resizeResize.length > 0) {
            	            eval(resizeResize+"(e, ui, $widget)"); 
	                	}
                 	},
             	 	stop: function(e, ui, $widget) {
             	 		if(typeof(resizeStop) == 'string' && resizeStop.length > 0) {
            	            eval(resizeStop+"(e, ui, $widget)"); 
	                	}
                 	}
             	}
      	    }
  	  }).data('gridster');
  	  $.parser.parse();
    });
})
