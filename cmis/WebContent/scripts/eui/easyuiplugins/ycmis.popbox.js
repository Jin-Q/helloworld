/**
 * <p>easyui扩展组件：popbox（pop框）</p>
 * <ul>$fn.popbox：提供基于easyui的pop框,pop框打开的页面需要实现函数doGetSelected($dialog,$window)</ul>
 * <ul>注：$dialog参数为当前打开的窗口jq对象，$window为触发弹窗的父窗口对象</ul>
 * @author wangbin 2014年3月10日16:33:53
 * <ul>修改：1.增加queryParams属性用于额外参数传递，支持el表达式等！</ul>
 * <ul>修改：2.增加POP的标题、宽度和高度可以设置 add by yuhq 2014-5-22 14:47:21</ul>
 * <ul>修改：3.修改设置readonly=true对查询按钮不生效问题 add by zangys 2014-7-15 17:46:37</ul>
 * <ul>修改：4.disable方法在谷歌浏览器不生效的问题。add by zangys 2014-7-16 12:28:40</ul>
 * <ul>增加：5.增加setValue方法，调用触发onChange事件。add by zangys at 2014-10-31 14:38:12</ul>
 * <ul>增加：6.增加onChange事件。add by zangys at 2014-10-31 14:38:12</ul>
 * <ul>修改：7.在setValue方法中，先设置输入框值，后触发onchange事件。add by zangys at 2015-05-05 11:25
 * <ul>修改：8.修改设置tipPosition为right时，必输提示框覆盖住放大镜按钮的问题。add by zangys at 2015-05-05 11:26</ul>
 * <ul>修改：9.添加对于beforeOnchange和afterOnchange的支持。add by 李伟 at 2015-12-24 11:00</ul>
 * <ul>修改：10.添加支持双击返回。add by liucheng3 at 2016-04-14 11:00</ul>
 */
var POPBOX_PARENT_WINDOW=null;
var POPBOX_PARENT_DIALOG=null;
(function($) {
    $.fn.popbox = function(options, param) {
        if (typeof options == "string") {
            var fun = $.fn.popbox.methods[options];
            if (fun) {
                return fun(this, param);
            } else {//继承validatebox的方法
                return this.validatebox(options, param);
            }
        }
        options = options || {};
        return this.each(function() {
            var state = $.data(this, 'popbox');
            //继承validatebox对象
            if ($.fn.validatebox) {
                $(this).validatebox(options);
            }
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, 'popbox', {
                    options : $.extend({}, $.fn.popbox.defaults, $.fn.popbox.parseOptions(this), options),
                    field : init(this)
                });
            }
            bindSpan(this);
        });
        function init(target) {
            $(target).addClass("validatebox-text");
            var span = $("<span class=\"popbox-button\" style=\"height: 20px;\"></span>").insertAfter(target);
            //绑定事件

            return span;
        }
        function bindSpan(target) {
            var state = $.data(target, 'popbox');
            //*********当设置tipPosition为right时，修改pop必输提示框不覆盖放大镜按钮***********
            //-------------begin---------------
            var box = $(target);
            var _a = $.data(target, "validatebox");
            var _9 = box;
            var _8 = target;
            box.unbind("mouseenter.validatebox").bind("mouseenter.validatebox", function() {
                if (box.hasClass("validatebox-invalid")) {
                    _c(target);
                }
            }).unbind("focus.validatebox").bind("focus.validatebox", function() {
                _a.validating = true;
                _a.value = undefined;
                (function() {
                    if (_a.validating) {
                        if (_a.value != _9.val()) {
                            _a.value = _9.val();
                            if (_a.timer) {
                                clearTimeout(_a.timer);
                            }
                            _a.timer = setTimeout(function() {
                                $(_8).validatebox("validate");
                            }, _a.options.delay);
                        } else {
                            _10(_8);
                        }
                        setTimeout(arguments.callee, 200);
                    }
                })();
            })
            //-----------------end-------------------

            //设置readonly时，设置查询按钮不可用
            if (state.options && state.options.readonly) {
                $(target).popbox('disable');
                return;
            }
            //queryParam在click时才会被解析
            state.field.unbind('click').bind('click', function() {
                fieldClick(target);
            }).hover(function() {
                $(this).addClass('popbox-button-hover');
            }, function() {
                $(this).removeClass('popbox-button-hover');
            });
        }
    };
    function _c(_d) {
        var _e = $.data(_d, "validatebox").message;
        var _f = $.data(_d, "validatebox").tip;
        if (!_f) {
            _f = $(
                    "<div class=\"validatebox-tip\">" + "<span class=\"validatebox-tip-content\">" + "</span>" + "<span class=\"validatebox-tip-pointer\">"
                            + "</span>" + "</div>").appendTo("body");
            $.data(_d, "validatebox").tip = _f;
        }
        _f.find(".validatebox-tip-content").html(_e);
        _10(_d);
    }
    ;
    function _10(_11) {
        var _12 = $.data(_11, "validatebox");
        if (!_12) {
            return;
        }
        var tip = _12.tip;
        if (tip) {
            var box = $(_11);
            var _13 = tip.find(".validatebox-tip-pointer");
            var _14 = tip.find(".validatebox-tip-content");
            tip.show();
            tip.css("top", box.offset().top - (_14._outerHeight() - box._outerHeight()) / 2);
            if (_12.options.tipPosition == "left") {
                tip.css("left", box.offset().left - tip._outerWidth());
                tip.addClass("validatebox-tip-left");
            } else {
                tip.css("left", box.offset().left + box._outerWidth() + 22);
                tip.removeClass("validatebox-tip-left");
            }
            _13.css("top", (_14._outerHeight() - _13._outerHeight()) / 2);
        }
    }
    ;

    /**
     * 弹出数据列表
     */
    function fieldClick(target) {
        var state = $.data(target, 'popbox');
        var url = state.options.url + '&returnMethod=' + state.options.returnMethod;//POP框URL
        var popW = state.options.popWidth;//POP框宽度
        var popH = state.options.popHeight;//POP框高度
        var popT = state.options.popTitle;//POP框标题
        if (state.options.queryParams) {
            var params = eval('(' + state.options.queryParams + ')');
            url += '&' + jQuery.param(params);
        }
        var popDialog = top.$('<div/>').dialog({
            title : popT,
            content : '<iframe id="" src="' + url + '" allowTransparency="true" scrolling="auto" width="100%" height="98%" frameBorder="0" name=""></iframe>',
            width : popW,
            height : popH,
            modal : true,
            maximizable : true,
            resizable : true,
            buttons : [ {
                text : CusLang.EUIExt.popbox.btnDefaultText,
                iconCls : 'icon-ok',
                handler : function() {
                    //pop框必须实现doGetSelected方法
                    popDialog.find('iframe').get(0).contentWindow.doGetSelected(popDialog, window);
                }
            } ],
            onClose : function() {
                popDialog.dialog('destroy');
            }
        });
        top.POPBOX_PARENT_WINDOW = window;
        top.POPBOX_PARENT_DIALOG = popDialog;
    }

    /**
     * popbox继承和扩展的方法
     */
    $.fn.popbox.methods = {
        options : function(jq) {
            return $.data(jq[0], "popbox").options;
        },
        destroy : function(jq) {
            return jq.each(function() {
                $.data(this, "popbox").field.remove();
                $(this).validatebox("destroy");
                $(this).remove();
            });
        },
        enable : function(jq) {
            return jq.each(function() {
                var span = $(this).next('.popbox-button')[0];
                if (span) {
                    $(span).unbind('click').bind('click', function() {
                        fieldClick(jq[0]);
                    }).hover(function() {
                        $(this).addClass('popbox-button-hover');
                    }, function() {
                        $(this).removeClass('popbox-button-hover');
                    });
                    $(span).removeClass("popbox-disabled-cursor").addClass("popbox-abled-cursor");
                }
                $(this).attr("disabled", false);
            });
        },
        disable : function(jq) {
            return jq.each(function() {
                var span = $(this).next('.popbox-button')[0];
                if (span) {
                    $(span).unbind('click').hover(function() {
                        $(this).removeClass('popbox-button-hover');
                    }, function() {
                    });
                    $(span).removeClass("popbox-abled-cursor").addClass("popbox-disabled-cursor");
                }
            });
        },
        setValue : function(jq, value) {
            return jq.each(function() {
                var oldValue = $(this).val();
                var opts = $.data(jq[0], "popbox").options;
                $(this).val(value);
                if (oldValue != value && opts.beforeOnchange) {
                    opts.beforeOnchange.call(this, value, oldValue);
                }
                if (oldValue != value && opts.onChange) {
                    opts.onChange.call(this, value, oldValue);
                }
                if (oldValue != value && opts.afterOnchange) {
                    opts.afterOnchange.call(this, value, oldValue);
                }
            })
        }
    };

    $.fn.popbox.parseOptions = function(target) {
        var t = $(target);
        return $.extend({}, $.fn.validatebox.parseOptions(target), {
            required : (t.attr("required") ? true : undefined)
        });
    };
    /**
     * popbox继承自validatebox
     */
    $.fn.popbox.defaults = $.extend({}, $.fn.validatebox.defaults, {
        popWidth : 500,
        popHeight : 400,
        popTitle : CusLang.EUIExt.popbox.popDefaultTitle,
        url : '',
        returnMethod : '',
        queryParams : '',
        onChange : function(newValue, oldValue) {
        }
    });
})(jQuery);
