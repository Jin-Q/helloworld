(function($) {
	$.fn.uigridster = function(options, param) {
		// options 是使用js初始化时传入的配置项
		if (typeof options == "string") {
			var fun = $.fn.uigridster.methods[options];
			if (fun) {
				return fun(this, param);
			} else {
				// do nothing
			}
		}
		options = options || {};
		var thisEach = this.each(function() {
			var state = $.data(this, 'uigridster');
			if (state) { // 已经初始化过
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'uigridster', {
					options : $.extend({}, $.fn.uigridster.defaults, $.fn.uigridster.parseOptions(this), options)
				});
			}
			initComb(this);
		});
		return thisEach;
	};

	function initComb(target) {
		var uigridster = $.data(target, 'uigridster');
		var uigridsterUl = $(target).find("ul");
		if (!uigridsterUl || uigridsterUl.length == 0) {
			$(target).append("<ul></ul>");
			uigridsterUl = $(target).find("ul");
		}
		uigridster.options.namespace = "#" + $(target).attr("id");
		var thisGridster = $(uigridsterUl).gridster(uigridster.options).data('gridster');
		uigridster.gridster = thisGridster;
	}

	$.fn.uigridster.defaults = {
		avoid_overlapped_widgets : true,
		widget_base_dimensions : [ 132, 132 ], // 一单位的大小
		widget_margins : [ 5, 5 ], // 每单位的边距
	};

	// 解析data-options属性中的配置项
	$.fn.uigridster.parseOptions = function(target) {
		// 初始化参数都放到data-options中，
		// 所以不使用parseOptions中的第二个参数parseOptions(target, [ "namespacex",{"enabledx":"boolean"} ])
		return $.parser.parseOptions(target);
	};

	$.fn.uigridster.methods = {
		gridster : function(jq) {
			var gridster = $.data(jq[0], "uigridster").gridster;
			return gridster;
		}
	};

})(jQuery);
