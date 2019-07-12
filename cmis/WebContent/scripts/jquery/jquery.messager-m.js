(function($) {

	var curBtHeight = 0;

	$.messager = {
		show : function(options) {
			var me = this;
			var defaults = {
				width : 200, // 提示框宽度
				height : 100, // 高度
				title : '提示信息', // 提示标题
				text : '', // 提示内容
				time : 3000, // 隐藏时间间隔
				type : 'slide', // 动画效果
				speed : 300
				// 动画速度
			};
			options = $.extend(defaults, options || {}); // 继承配置

			if (curBtHeight + options.height > window.screen.availHeight)
				return;

			var messager = $('<div name="message" style="border:#b9c9ef 1px solid;z-index:100;width:'
					+ options.width
					+ 'px;height:'
					+ options.height
					+ 'px;position:absolute; display:none;background-image:url(images/Header_bg.gif);background-repeat:repeat-x;bottom:0; right:0; overflow:hidden;"><div style="border:1px solid #fff;border-bottom:none;width:100%;height:30px;font-size:12px;overflow:hidden;background-image:url(images/Header_bg.gif);background-repeat:repeat-x;"><span id="message_close" style="float:right;padding:5px 0 5px 0;width:32px;line-height:auto;color:red;font-size:12px;font-weight:bold;text-align:center;cursor:pointer;overflow:hidden;">关闭&nbsp;&nbsp;</span><div style="padding:5px 0 5px 5px;width:220px;line-height:18px;background-image:url(images/Header_bg.gif);background-repeat:repeat-x;text-align:left;overflow:hidden;">'
					+ options.title
					+ '</div><div style="clear:both;"></div></div>'
					+ '<div style="padding-bottom:5px;border:1px solid #fff;border-top:none;width:100%;height:auto;font-size:12px;">'
					+ '<div id="message_content" style="margin:0 5px 0 5px;border:1px solid;background-image:url(images/center_bg2.gif);background-repeat:repeat-x;padding:10px 0 10px 5px;font-size:12px;width:'
					+ (options.width - 17)
					+ 'px;height:'
					+ (options.height - 50)
					+ 'px;text-align:left;">'
					+ options.text + '</div></div></div>')
					.appendTo(document.body);
			messager.data('height', curBtHeight);
			// 定义提示框位置
			var bottomHeight = curBtHeight - document.documentElement.scrollTop;
			messager.css("bottom", bottomHeight + "px");
			// 滚动条滚动时的处理
			$(window).scroll(function() {
				var bottomHeight = messager.data('height')
						- document.documentElement.scrollTop;
				messager.css("bottom", bottomHeight + "px");
			});
			curBtHeight += options.height + 5;

			// 显示提示框
			switch (options.type) {
				case 'slide' :
					messager.slideDown(options.speed);
					break;
				case 'fade' :
					messager.fadeIn(options.speed);
					break;
				case 'show' :
					messager.show(options.speed);
					break;
				default :
					messager.slideDown(options.speed);
					break;
			}
			// 关闭事件
			$("#message_close", messager).click(function() {
						me.close(messager);
					});
			// 计时
			if (options.time > 0) {
				messager.data('close', setTimeout(function() {
									return me.close(messager);
								}, options.time));
			}
			// 鼠标移入时停止计时，移出时重新计时
			messager.hover(function() {
				clearTimeout(messager.data('close'));
				}, function() {
					if (options.time > 0) {
						messager.data('close', setTimeout(function() {
											return me.close(messager);
										}, options.time));
					}
				});
			messager.data('options', options);
			return messager;
		},
		close : function(messager) {
			options = messager.data('options');
			if(options!= null) {
				messager.slideUp(options.speed);
				switch (options.type) {
					case 'slide' :
						messager.slideUp(options.speed);
						break;
					case 'fade' :
						messager.fadeOut(options.speed);
						break;
					case 'show' :
						messager.hide(options.speed);
						break;
					default :
						messager.slideUp(options.speed);
						break;
				};
           
				setTimeout(function() {
							messager.remove();
							var m = $('[name=message]:last');
							curBtHeight = m.length == 1 ? (m.data('height')
									+ m.data('options').height + 5) : 0;
						}, options.speed);
            }
		}

	}

})(jQuery);