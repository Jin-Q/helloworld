/**
 * <p>
 * easyui扩展组件：picklist
 * 
 * 属性名                   可用值
 * sourcePosition       :   待选列表位置可用值["left"|"right"]      [默认：left]
 * sourceIdField        :   指明待选列表中哪一个字段是标识字段       [默认："id"] 
 * sourceNameField      :   指明待选列表中哪一个字段是显示字段       [默认："name"]
 * sourceUrl            :   待选列表加载数据的url
 * sourceQueryParams    :   数据查询的过滤参数                      [默认：{} ]
 * sourceData           :   待选列表的数据(url未定义时生效)
 * sourceTitle          :   待选列表栏的标题                        [默认："待选列表"]

 * targetIdField        :   指明已选列表中哪一个字段是标识字段       [默认："id"] 
 * targetNameField      :   指明已选列表中哪一个字段是显示字段       [默认："name"]
 * targetUrl            :   已选列表加载数据的url
 * targetQueryParams    :   数据查询的过滤参数                      [默认：{} ]
 * targetData           :   已选列表的数据(url未定义时生效)
 * targetTitle          :   已选列表栏的标题                        [默认："已选列表"]
 *
 * toRightTitle         :   移动到右侧按钮文字                      [默认："&gt;"]
 * allToRightTitle      :   全部移动到右侧的按钮文字                [默认："&gt;&gt;"]
 * toLeftTitle          :   移动到左侧的按钮文字                    [默认："&lt;"]
 * allToLeftTitle       :   全部移动到左侧的按钮文字                [默认："&lt;&lt;"]
 * showAllBtns          :   是否显示移动全部的按钮                  [默认：true]
 *
 * bodyHeight           :   待选和已选列表栏的高度                   [默认：200]
 * bodyWidth            :   待选和已选列表栏内容体的宽               [默认：220]
 * buttonsWidth         :   按钮的宽度                              [默认：60]
 * buttonHeight         :   每个按钮的高度                           [默认：30]
 * titleHeight          :   标题高度                                [默认：26]
 * 
 * </p>
 */

(function($) {
    $.fn.picklist = function(options, param) {
        if (typeof options == "string") {
            var fun = $.fn.picklist.methods[options];
            if (fun) {
                return fun(this, param);
            }
        }
        options = options || {};
        return this.each(function() {
            var state = $.data(this, 'picklist');
            if (state) {
                $.extend(state.options, options);
            } else {
                state = $.data(this, 'picklist', {
                    options : $.extend({}, $.fn.picklist.defaults, $.fn.picklist.parseOptions(this), options)
                });
            }
            initPicklist(this);
        });
    };

    /**
     * 初始化视图
     * 
     * @param target
     */
    function initPicklist(target) {
        $(target).addClass("cus-picklist");
        initLayout(target);
        initButtons(target);
        initSourceData(target);
        initTargetData(target);
    }
    /**
     * 初始化布局
     */
    function initLayout(target) {
        var opts = $.data(target, 'picklist').options;
        var titleHeight = opts.titleHeight;
        var bodyHeight = opts.bodyHeight;
        var bodyWidth = opts.bodyWidth;
        var buttonsWidth = opts.buttonsWidth;
        // 计算button占的高度
        var buttonsHeight = opts.buttonHeight * 4;
        if (!opts.showAllBtns) {
            buttonsHeight = opts.buttonHeight * 2;
        }
        // 计算组件高度
        var heigthSpace = opts.heigthSpace;
        var widthSpace = opts.widthSpace;
        var targetHeight = titleHeight + bodyHeight + heigthSpace;
        var targetWidth = bodyWidth + buttonsWidth + bodyWidth + widthSpace;
        $(target).css({
            "height" : targetHeight + "px",
            "width" : targetWidth + "px"
        });

        // 初始化左侧布局
        var left = $('<div class="cus-picklist-left"></div>');
        left.css({
            "width" : bodyWidth + "px"
        });
        var leftTitle = $('<div class="cus-picklist-title"></div>');
        var leftBody = $('<div class="cus-picklist-body"></div>');
        leftBody.css({
            "height" : bodyHeight + "px"
        });
        left.append(leftTitle);
        left.append(leftBody);

        // 初始化按钮部分布局，通过修改margin值使按钮上下居中
        var buttons = $('<div class="cus-picklist-buttons"></div>');
        var buttonsMarginTop = 0;
        var buttonsSpace = targetHeight - buttonsHeight;
        if (buttonsSpace > 0) {
            buttonsMarginTop = parseInt(buttonsSpace / 2);
        }

        buttons.css({
            "width" : buttonsWidth + "px",
            "height" : buttonsHeight + "px",
            "margin-top" : buttonsMarginTop + "px"
        });

        // 初始化右侧部分布局
        var right = $('<div class="cus-picklist-right"></div>');
        right.css({
            "width" : bodyWidth + "px"
        });
        var rightTitle = $('<div class="cus-picklist-title"></div>');
        var rightBody = $('<div class="cus-picklist-body"></div>');
        rightBody.css({
            "height" : bodyHeight + "px"
        });
        right.append(rightTitle);
        right.append(rightBody);

        // 根据配置指定待选和已选列表的左右位置
        if (opts.sourcePosition == "left") {
            left.addClass("cus-picklist-source");
            right.addClass("cus-picklist-target");
        } else {
            right.addClass("cus-picklist-source");
            left.addClass("cus-picklist-target");
        }

        $(target).append(left);
        $(target).append(buttons);
        $(target).append(right);
    }
    /**
     * 初始化button
     */
    function initButtons(target) {
        var opts = $.data(target, 'picklist').options;

        /*
         * 初始化按钮及其事件
         */
        var buttonsDiv = $(target).find(".cus-picklist-buttons");
        var toRightBtn = $('<button>' + opts.toRightTitle + '</button>');
        var allToRightBtn = $('<button>' + opts.allToRightTitle + '</button>');
        var toLeftBtn = $('<button>' + opts.toLeftTitle + '</button>');
        var allToLeftBtn = $('<button>' + opts.allToLeftTitle + '</button>');

        toRightBtn.unbind();
        toRightBtn.on('click', function(e) {
            onToRightBtnClick(target);
        });
        toLeftBtn.unbind();
        toLeftBtn.on('click', function(e) {
            onToLeftBtnClick(target);
        });
        allToRightBtn.unbind();
        allToRightBtn.on('click', function(e) {
            onAllToRightBtnClick(target);
        });
        allToLeftBtn.unbind();
        allToLeftBtn.on('click', function(e) {
            onAllToLeftBtnClick(target);
        });

        // 添加按钮到组件，根据配置判断是否添加移动全部的按钮
        buttonsDiv.append(toRightBtn);
        if (opts.showAllBtns) {
            buttonsDiv.append(allToRightBtn);
        }
        buttonsDiv.append(toLeftBtn);
        if (opts.showAllBtns) {
            buttonsDiv.append(allToLeftBtn);
        }
    }
    /**
     * 初始化待选列表
     */
    function initSourceData(target) {
        var opts = $.data(target, 'picklist').options;
        var sourceUrl = opts.sourceUrl;
        var retunDataHandler = opts.sourceUrlDataHandler;
        var postData = {};
        postData = $.extend({}, opts.sourceQueryParams, postData);
        // 加载数据
        if (sourceUrl) {
            $.ajax({
                type : "POST",
                url : sourceUrl,
                dataType : "json",
                data : postData,
                success : function(data, textStatus, jqXHR) {
                    updateSource(target, retunDataHandler.call(this, data));
                },
                error : function(rsp) {
                    $.messager.alert(CusLang.EUIExt.picklist.errorTitle, CusLang.EUIExt.picklist.errorMsg);
                }
            });
        } else {// url不存在是，使用data中的静态数据
            if (opts.sourceData) {
                updateSource(target, opts.sourceData);
            }
        }
    }
    /**
     * 更新待选列表视图
     */
    function updateSource(target, data) {
        var opts = $.data(target, 'picklist').options;
        var sourceDiv = $(target).find(".cus-picklist-source");

        sourceDiv.find(".cus-picklist-title").html(opts.sourceTitle);

        var sourceBodyDiv = sourceDiv.find(".cus-picklist-body");
        sourceBodyDiv.children("ul").remove();
        var sourceBodyUl = $("<ul></ul>");
        var sourceIdField = opts.sourceIdField; // 指明哪一个字段是标识字段
        var sourceNameField = opts.sourceNameField;
        if (data && data.length > 0) {
            for ( var i = 0; i < data.length; i++) {
                var row = data[i];
                var sourceId = row[sourceIdField];
                var sourceName = row[sourceNameField];
                sourceBodyUl.append('<li itemId="' + sourceId + '" title="' + sourceName + '">' + sourceName + '</li>');
            }
        }
        sourceBodyUl.on('dblclick', 'li', function(e) {
            onItemDblclick(target, "source", this);
        });
        sourceBodyUl.on('click', 'li', function(e) {
            onItemClick(target, "source", this);
        });
        sourceBodyDiv.append(sourceBodyUl);
    }
    /**
     * 初始化已选列表
     */
    function initTargetData(target) {
        var opts = $.data(target, 'picklist').options;
        var retunDataHandler = opts.targetUrlDataHandler;
        var targetUrl = opts.targetUrl;
        var postData = {};
        postData = $.extend({}, opts.targetQueryParams, postData);
        // 加载数据
        if (targetUrl) {
            $.ajax({
                type : "POST",
                url : targetUrl,
                dataType : "json",
                data : postData,
                success : function(data, textStatus, jqXHR) {
                    updateTarget(target, retunDataHandler.call(this, data));
                },
                error : function(rsp) {
                    $.messager.alert(CusLang.EUIExt.datatable.errorTitle, CusLang.EUIExt.datatable.errorMsg);
                }
            });
        } else {// url不存在是，使用data中的静态数据
            if (opts.targetData && opts.targetData.length > 0) {
                updateTarget(target, opts.targetData);
            }
        }
    }
    /**
     * 更新待选列表视图
     */
    function updateTarget(target, data) {
        var opts = $.data(target, 'picklist').options;
        var targetDiv = $(target).find(".cus-picklist-target");

        targetDiv.find(".cus-picklist-title").html(opts.targetTitle);

        var targetBodyDiv = targetDiv.find(".cus-picklist-body");
        targetBodyDiv.children("ul").remove();
        var targetBodyUl = $("<ul></ul>");
        var targetIdField = opts.targetIdField; // 指明哪一个字段是标识字段
        var targetNameField = opts.targetNameField;
        if (data && data.length > 0) {
            for ( var i = 0; i < data.length; i++) {
                var row = data[i];
                var targetId = row[targetIdField];
                var targetName = row[targetNameField];
                targetBodyUl.append('<li itemId="' + targetId + '" title="' + targetName + '">' + targetName + '</li>');
            }
        }

        targetBodyUl.on('dblclick', 'li', function(e) {
            onItemDblclick(target, "target", this);
        });
        targetBodyUl.on('click', 'li', function(e) {
            onItemClick(target, "target", this);
        });
        targetBodyDiv.append(targetBodyUl);
    }
    /**
     * 点击时切换选中状态
     */
    function onItemClick(target, itemType, thisItem) {
        if ($(thisItem).hasClass('cus-picklist-row-selected')) {
            $(thisItem).removeClass('cus-picklist-row-selected');
        } else {
            $(thisItem).addClass('cus-picklist-row-selected');
        }
    }
    /**
     * 双击时选中此项，并使所有其他项变为未选中状态
     */
    function onItemDblclick(target, itemType, thisItem) {
        $(target).find('.cus-picklist-' + itemType + ' ul li').each(function() {
            $(this).removeClass('cus-picklist-row-selected');
        });
        $(thisItem).addClass('cus-picklist-row-selected');
    }

    /**
     * 点击选择到右侧按钮的操作. 添加选中的项目到右侧 删除左侧选中的项目 删除选中标示
     */
    function onToRightBtnClick(target) {
        var leftUl = $(target).find('.cus-picklist-left ul');
        var rightUl = $(target).find('.cus-picklist-right ul');
        var leftSelected = leftUl.find('li.cus-picklist-row-selected');
        if (leftSelected && leftSelected.length > 0) {
            rightUl.append(leftSelected);
            leftSelected.removeClass('cus-picklist-row-selected');
            leftUl.remove('li.cus-picklist-row-selected');
        }
    }
    /**
     * 点击选择到左侧按钮的操作. 添加选中的项目到左侧 删除右侧选中的项目 删除选中标示
     */
    function onToLeftBtnClick(target) {
        var leftUl = $(target).find('.cus-picklist-left ul');
        var rightUl = $(target).find('.cus-picklist-right ul');
        var rightSelected = rightUl.find('li.cus-picklist-row-selected');
        if (rightSelected && rightSelected.length > 0) {
            leftUl.append(rightSelected);
            rightSelected.removeClass('cus-picklist-row-selected');
            rightUl.remove('li.cus-picklist-row-selected');
        }
    }
    /**
     * 点击全部到左侧按钮的操作. 添加全部项目到左侧 删除右侧的项目 删除选中标示
     */
    function onAllToRightBtnClick(target) {
        var leftUl = $(target).find('.cus-picklist-left ul');
        var rightUl = $(target).find('.cus-picklist-right ul');
        var leftSelected = leftUl.find('li');
        if (leftSelected && leftSelected.length > 0) {
            rightUl.append(leftSelected);
            leftSelected.removeClass('cus-picklist-row-selected');
            leftUl.remove('li');
        }
    }
    /**
     * 点击全部到右侧按钮的操作. 添加全部项目到右侧 删除左侧的项目 删除选中标示
     */
    function onAllToLeftBtnClick(target) {
        var leftUl = $(target).find('.cus-picklist-left ul');
        var rightUl = $(target).find('.cus-picklist-right ul');
        var rightSelected = rightUl.find('li');
        if (rightSelected && rightSelected.length > 0) {
            leftUl.append(rightSelected);
            rightSelected.removeClass('cus-picklist-row-selected');
            rightUl.remove('li');
        }
    }
    /**
     * 默认值配置，字段含义见注释
     */
    $.fn.picklist.defaults = {
        sourcePosition : "left",

        sourceIdField : "id",
        sourceNameField : "name",
        sourceUrl : undefined,
        sourceQueryParams : {},
        sourceData : undefined,
        sourceTitle : CusLang.EUIExt.picklist.defaultSourceTitle,

        targetIdField : "id",
        targetNameField : "name",
        targetUrl : undefined,//
        targetQueryParams : {},
        targetData : undefined,
        targetTitle : CusLang.EUIExt.picklist.defaultTargetTitle,

        toRightTitle : "&gt;",
        allToRightTitle : "&gt;&gt;",
        toLeftTitle : "&lt;",
        allToLeftTitle : "&lt;&lt;",
        showAllBtns : true,

        bodyHeight : 200,
        bodyWidth : 220,
        buttonsWidth : 60,
        buttonHeight : 30,
        titleHeight : 26,
        heigthSpace : 0,
        widthSpace : 6,

        sourceUrlDataHandler : function(returnData) {
            return returnData.rows;
        },

        targetUrlDataHandler : function(returnData) {
            return returnData.rows;
        }
    };

    /**
     * 扩展方法
     */
    $.fn.picklist.methods = {
        // 获取picklist的options的数据
        options : function(jq) {
            var opts = $.data(jq[0], "picklist").options;
            return opts;
        },
        // 重新加载数据
        reload : function(jq) {
            return jq.each(function() {
                initSourceData(this);
                initTargetData(this);
            });
        },
        // 获取待选列表数据
        getSourceData : function(jq) {
            var sourceIdNames = [];
            var sourceLis = $(jq[0]).find('.cus-picklist-source ul li');
            sourceLis.each(function() {
                var itemId = $(this).attr('itemid');
                var itemName = $(this).html();
                sourceIdNames.push({
                    id : itemId,
                    name : itemName
                });
            });
            return sourceIdNames;
        },
        // 获取已选列表数据
        getTargetData : function(jq) {
            var targetIdNames = [];
            var targetLis = $(jq[0]).find('.cus-picklist-target ul li');
            targetLis.each(function() {
                var itemId = $(this).attr('itemid');
                var itemName = $(this).html();
                targetIdNames.push({
                    id : itemId,
                    name : itemName
                });
            });
            return targetIdNames;
        }
    };

    /**
     * 解析data-options中数据
     */
    $.fn.picklist.parseOptions = function(target) {
        // data-options 中的初始化参数
        return $.extend({}, $.parser.parseOptions(target, [ "sourcePosition" ]));// 解析
    };

    $.parser.plugins.push('picklist');
})(jQuery);