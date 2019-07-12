/**
 * <p>
 * easyui扩展组件：datatable 数据表格，支持数据的三行显示
 * 
 * 属性名                   说明
 * id                   :   HTML标准属性
 * idField              :   指明哪一个字段是标识字段
 * url                  :   获取列表数据的URL
 * queryParams          :   查询的过滤参数,会加到url的数据请求参数中
 * hideMiddle           :   默认是否隐藏中间一行的内容[default: true]
 * pagination           :   分页模式:是否显示分页工具栏[default: true]
 * pageList             :   分页模式:可供选择的每页显示记录数[default: [5,10,15]]
 * pageSize             :   分页模式:每页显示记录数[default: 10]
 * middleRowHandler     :   对一条数据中间层的渲染函数
 * onLoadSuccess        :   事件加载完成触发的事件
 * onAfterRender        :   页面渲染完成后执行
 * data                 :   静态数据列表，当url无效时生效
 * columns              :   数据列配置
 * {
 *      title           :   当前列的标题 
 *      field           :   当前列的字段名称
 *      formatter       :   当前列数据的格式化函数
 *      position        :   当前列的显示位置['top'|'bottom']	[default: 'bottom']
 *      showTitle       :   当前列是否显示标题(position='top'时生效)[default: true]
 *      topPostion      :   当前列在顶部的显示位置(position='top'时生效)['left'|'right'][default: 'left']
 *      width           :   当前列的宽度为多少像素(position='bottom'时生效)[default: 200]
 * }
 * 中间行div的id为:datatable-row-middle-[idFiled],如果idField没有定义或者无效则为datatable-row-middle-[index]
 * </p>
 * 修改说明
 * 1. 添加属性 singleSelect 是否单选，默认false
 * 2. 添加方法 getSelected   返回第一个被选中的行或如果没有选中的行则返回null
 * 3. 添加方法 getSelections 返回所有被选中的行，当没有记录被选中的时候将返回一个空数组
 * 4. 添加方法 clearSelections 清除所有选择的行
 * 5. 添加方法 showColumn      显示指定的列
 * 6. 添加方法 hideColumn 隐藏指定的列
 * 7. 添加方法 getRows  返回当前页的所有行
 * 8. 添加方法 getData  返回当前页的所有数据对象（获取数据data   data.rows与getRows返回值相同）
 * 9. 添加方法 selectAll 全选 选中当前页所有行
 */

(function($) {
    var tdDefaultWidth = '200px';
    var topMiddleIdStart = 'datatable-row-middle-';
    $.fn.datatable = function(options, param) {
        if (typeof options == "string") {
            var fun = $.fn.datatable.methods[options];
            if (fun) {
                return fun(this, param);
            }
        }
        options = options || {};
        return this.each(function() {
            var state = $.data(this, 'datatable');
            if (state) {
                $.extend(state.options, options);
            } else {
                state = $.data(this, 'datatable', {
                    options : $.extend({}, $.fn.datatable.defaults, $.fn.datatable.parseOptions(this), options),
                });
            }
            initDatatable(this);
        });
    };

    /**
     * 根据Url通过ajax获取后台数据
     * @param target this对象
     */
    function initDatatable(target) {
        $(target).addClass("datatable panel-body");
        initContent(target);
        // 初始化表头
        initPageTitle(target);
        // 初始化 pagination
        initPagination(target);
        // 加载数据
        loadData(target);
    }
    function initContent(target) {
        var content = $('<div class="datatable-content"></div>');
        var contentTable = $('<table border="0" cellpadding="0" cellspacing="0"><thead></thead><tbody></tbody></table>');
        content.append(contentTable);
        $(target).append(content);
    }
    /**
     * 初始化表头
     */
    function initPageTitle(target) {
        var opts = $.data(target, 'datatable').options;
        var contentThead = $(target).find(".datatable-content table thead");
        var colNums = 0;
        titleTr = $('<tr></tr>');
        if (opts.columns && opts.columns.length > 0) {
            var columns = opts.columns;
            for ( var i = 0; i < columns.length; i++) {
                var column = columns[i];
                if (!column.position || column.position == 'bottom') {
                    colNums++;
                    var titleTd = $('<td field = '+ column.field +'>' + column.title + '</td>');
                    titleTd.css({
                        'width' : column.width ? column.width + 'px' : tdDefaultWidth
                    });
                    if (column.hidden == true) {
                        titleTd.css({
                            'display' : "none"
                        });
                    }
                    titleTr.append(titleTd);
                }
            }
        }
        opts.colNums = colNums;
        contentThead.append(titleTr);
    }

    /**
     * 初始化分页信息
     */
    function initPagination(target) {
        var opts = $.data(target, 'datatable').options;
        if (opts.pagination) {
            var pagin = $('<div class="datagrid-pager pagination"></div>');
            $(target).append(pagin);
            pagin.pagination({
                total : 0,
                pageNumber : opts.pageNumber,
                pageSize : opts.pageSize,
                pageList : opts.pageList,
                onSelectPage : function(pageNumber, pageSize) {
                    opts.pageNumber = pageNumber;
                    opts.pageSize = pageSize;
                    loadData(target);
                }
            });
        }
    }
    /**
     * 加载数据
     * @params 如果带查询参数，则使用新的查询参数 。不传入查询参数使用queryParams中的参数
     */
    function loadData(target, params) {
        $(target).datatable("getPager").pagination("loading");
        var datatable = $.data(target, 'datatable');
        var opts = $.data(target, 'datatable').options;
        var url = opts.url;
        var postData = {};
        postData.page = opts.pageNumber;
        postData.rows = opts.pageSize;
        // 如果带查询参数，则使用新的查询参数
        // 不传入查询参数使用queryParams中的参数
        if (!params) {
            postData = $.extend({}, opts.queryParams, postData);
        } else {
            postData = $.extend({}, params, postData);
        }

        // 加载数据
        if (url) {
            $.ajax({
                type : "POST",
                url : url,
                dataType : "json",
                data : postData,
                success : function(data, textStatus, jqXHR) {
                    if (opts.onLoadSuccess && typeof (opts.onLoadSuccess) == 'function') {
                        var loadSuccess = opts.onLoadSuccess.call(target, data);
                        // 如果返回值为false，则不进行数据渲染，直接返回
                        if (loadSuccess == false) {
                            $(target).datatable("getPager").pagination("loaded");
                            return false;
                        }
                    }
                    datatable.data = data;
                    // 更新数据内容
                    updateBody(target, data);
                    // 更新pagination
                    updatePagination(target);
                    if (opts.onAfterRender && typeof (opts.onAfterRender) == 'function') {
                        opts.onAfterRender.call(target, data);
                    }
                    $(target).datatable("getPager").pagination("loaded");
                },
                error : function(rsp) {
                    $(target).datatable("getPager").pagination("loaded");
                    $.messager.alert(CusLang.EUIExt.datatable.errorTitle, CusLang.EUIExt.datatable.errorMsg);
                }
            });
        } else {// url不存在是，使用data中的静态数据
            if (opts.data && opts.data.length > 0) {
                var pageNumber = opts.pageNumber;
                var pageSize = opts.pageSize;
                var rows = [];
                for ( var i = (pageNumber - 1) * pageSize; i < (pageSize * pageNumber) && i < opts.data.length; i++) {
                    rows.push(opts.data[i]);
                }
                datatable.data = {
                    total : opts.data.length,
                    rows : rows
                }
                // 更新数据内容
                updateBody(target, datatable.data);
                // 更新pagination
                updatePagination(target);
                $(target).datatable("getPager").pagination("loaded");
            }
        }
    }
    /**
     * 更新内容
     */
    function updateBody(target, data) {
        var body = $(target).find("div.datatable-content table tbody");
        // 清空已有的数据
        body.children().remove();
        if (data && data.rows) {
            for ( var i = 0; i < data.rows.length; i++) {
                var rowData = data.rows[i];
                var row1 = $('<tr></tr>');
                var row2 = $('<tr></tr>');
                var row3 = $('<tr></tr>');
                // 添加top
                addBodyTop(target, row1, rowData, i);
                // 添加middle
                addBodyMiddle(target, row2, rowData, i);
                // 添加bottom
                addBodyBottom(target, row3, rowData, i);
                body.append(row1);
                body.append(row2);
                body.append(row3);
            }
            updateEvents(target);
        }
    }
    /**
     * 添加一行的头部内容
     */
    function addBodyTop(target, $row, rowData, index) {
        var opts = $.data(target, 'datatable').options;
        $row.addClass("datatable-row-top");
        $row.attr("targetId", rowData[opts.idField]);
        var topDiv = $('<td colspan="' + opts.colNums + '"></td>');
        var topLeftDiv = $('<div class="datatable-row-top_l"></div>');
        var topRightDiv = $('<div class="datatable-row-top_r"></div>');
        if (opts.columns && opts.columns.length > 0) {
            var columns = opts.columns;
            for ( var i = 0; i < columns.length; i++) {
                var column = columns[i];
                if (column.position && column.position == 'top' && column.field && rowData[column.field]) {
                    var fieldTitle = column.title ? column.title + ": " : "";
                    // 如果设置了showTitle=false 则不显示字段label，默认为true
                    if (column.showTitle != undefined && column.showTitle == false) {
                        fieldTitle = '';
                    }
                    var fieldVal = rowData[column.field];
                    // 格式化数据
                    var fieldSpan = $('<span></span>  ');
                    if (column.formatter && typeof (column.formatter) == 'function') {
                        fieldVal = column.formatter.call(fieldSpan, fieldVal, rowData, index);
                    }
                    fieldSpan.html(fieldTitle + fieldVal);
                    if (column.topPostion == 'right') {
                        topRightDiv.append(fieldSpan);
                    } else {
                        topLeftDiv.append(fieldSpan);
                    }
                }
            }
        }
        topDiv.append(topLeftDiv);
        topDiv.append(topRightDiv);
        topDiv.append('<div class="datatable-row-top_clear"></div>');
        $row.append(topDiv);
    }
    /**
     * 添加中间一行的内容
     */
    function addBodyMiddle(target, $row, rowData, index) {
        var opts = $.data(target, 'datatable').options;
        $row.addClass("datatable-row-middle");
        $row.attr("targetId", rowData[opts.idField]);
        var middleTrTd = $('<td colspan="' + opts.colNums + '"></td>');
        /*
         * if (opts.columns && Array.isArray(opts.columns) && opts.columns.length > 0) { var columns = opts.columns; for ( var i = 0; i < columns.length; i++) { var column = columns[i]; if (column.position && column.position == 'middle' && column.field && rowData[column.field]) {
         * var fieldVal = rowData[column.field]; //格式化数据 if (column.formatter && typeof (column.formatter) == 'function') { fieldVal = column.formatter(fieldVal, rowData, index); } var fieldSpan = $('<span>' + fieldVal + '</span> '); middleTrTd.append(fieldSpan); } } }
         */
        if (opts.middleRowHandler && typeof (opts.middleRowHandler) == 'function') {
            opts.middleRowHandler.call(middleTrTd, rowData, index);
        }
        var divId = index;
        if (opts.idField && rowData[opts.idField]) {
            divId = rowData[opts.idField];
        }
        $row.attr('id', topMiddleIdStart + divId);
        // 如果设置中间层默认隐藏则添加display:none样式
        if (opts.hideMiddle) {
            $row.css({
                'display' : 'none'
            });
        }
        $row.append(middleTrTd);
    }
    /**
     * 添加最底层一行的内容
     */
    function addBodyBottom(target, $row, rowData, index) {
        var opts = $.data(target, 'datatable').options;
        $row.addClass("datatable-row-bottom");
        $row.attr("targetId", rowData[opts.idField]);
        $row.attr("datatable-row-index", index);
        
        if (opts.columns && opts.columns.length > 0) {
            var columns = opts.columns;
            for ( var i = 0; i < columns.length; i++) {
                var column = columns[i];
                if ((!column.position || column.position == 'bottom') && column.field) {
                    var fieldVal = rowData[column.field];
                    if (!fieldVal) {
                        fieldVal = "";
                    }
                    // 格式化数据
                    if (column.formatter && typeof (column.formatter) == 'function') {
                        fieldVal = column.formatter(fieldVal, rowData, index);
                    }
                    var bottomTd = $('<td field = '+ column.field +'>' + fieldVal + '</td>');
                    if (column.hidden == true) {
                        bottomTd.css({
                            'display' : "none"
                        });
                    }
                    $row.append(bottomTd);
                }
            }
        }
    }
    /**
     * 更新分页信息
     */
    function updatePagination(target) {
        var datatable = $.data(target, 'datatable');
        $(target).datatable("getPager").pagination('refresh', { // 改变选项并刷新分页栏信息
            total : datatable.data.total
        });
    }

    function updateEvents(target) {
        $(target).find("tr.datatable-row-bottom").unbind().bind("mouseover", function(e) {
            $(this).addClass("datatable-row-over");
        }).bind("mouseout", function(e) {
            $(this).removeClass("datatable-row-over");
        }).bind("click", function(e) {
            var thisHasClass = false;
            if ($(this).hasClass("datagrid-row-selected")) {
                thisHasClass = true;
            }
            
            
            var opts = $.data(target, 'datatable').options;
            if(opts.singleSelect){//单选
            	$(target).find("tr.datatable-row-bottom").each(function() {
                    $(this).removeClass("datagrid-row-selected");
                });
            	 $(this).addClass("datagrid-row-selected");
            }else{//多选
            	 if (thisHasClass) {
                     $(this).removeClass("datagrid-row-selected");
                 }else{
                	 $(this).addClass("datagrid-row-selected");
                 }
            }
            
            /*if (!thisHasClass) {
                $(this).addClass("datagrid-row-selected");
            }*/
        });
    }
    
    //获取选中项
    function _getSelect(target){
    	 var data = $.data(target, "datatable").data;
    	 var opts = $.data(target, "datatable").options;
    	 var rows =[];
    	 $(target).find("tr.datagrid-row-selected").each(function() {
    		 
    		 var index = parseInt($(this).attr("datatable-row-index"));
				rows.push(data.rows[index]);
 		});
    	return rows;
    }
    /**
     * 默认值配置
     */
    $.fn.datatable.defaults = {// 默认属性定义
        url : undefined,//
        queryParams : {},
        pageSize : 10,
        pageNumber : 1,
        columns : undefined,// 列信息
        data : undefined,
        idField : undefined, // 指明哪一个字段是标识字段
        pageList : [ 5, 10, 15 ],
        pagination : true,
        hideMiddle : true,
        middleRowHandler : function(row, index) {

        },
        onLoadSuccess : function(data) {

        },
        onAfterRender : function(data) {

        }
    };

    /**
     * 扩展方法
     */
    $.fn.datatable.methods = {
        // 获取datatable的options的数据
        options : function(jq) {
            var opts = $.data(jq[0], "datatable").options;
            return opts;
        },
        // 重新加载数据，此方法只用于重新加载不可修改查询参数
        reload : function(jq) {
            return jq.each(function() {
                loadData(this);
            });
        },
        // 加载数据，可以修改此次的查询参数
        load : function(jq, params) {
            if (!params) {
                params = {};
            }
            return jq.each(function() {
                loadData(this, params);
            });
        },
        // 获取分页对象
        getPager : function(jq) {
            return $(jq[0]).children("div.datagrid-pager");
        },
        //返回第一个被选中的行或如果没有选中的行则返回
        getSelected : function(jq){
        	var rows = _getSelect(jq[0]);
			return rows.length > 0 ? rows[0] : null;
        },
        //返回所有被选中的行，当没有记录被选中的时候将返回一个空数组
        getSelections : function(jq){
        	return _getSelect(jq[0]);
        },
        //清除所有选择的行
        clearSelections : function(jq){
        	return  $(jq[0]).find("tr.datatable-row-bottom").each(function() {
        		    	$(this).removeClass("datagrid-row-selected");
    		        });
        },
        //全选
        selectAll : function(jq) {
        	return  $(jq[0]).find("tr.datatable-row-bottom").each(function() {
		    	$(this).addClass("datagrid-row-selected");
	        });
		},
		//显示指定列
		showColumn : function(jq,params){
			$(jq[0]).find("td[field=\"" + params + "\"]").show();
		    var opts = $.data(jq[0], "datatable").options;
		    var columns = opts.columns;
		    if(columns){
			   for(var i=0; i<columns.length; i++){
				   var column = columns[i];
				   if(column.field == params){
					   column.hidden = false;
				   }
			   }
		    }
		},
		//隐藏指定列
		hideColumn : function(jq,params){
			$(jq[0]).find("td[field=\"" + params + "\"]").hide();
		    var opts = $.data(jq[0], "datatable").options;
		    var columns = opts.columns;
		    if(columns){
			   for(var i=0; i<columns.length; i++){
				   var column = columns[i];
				   if(column.field == params){
					   column.hidden = true;
				   }
			   }
		    }
		},
        getData : function(jq) {
			return $.data(jq[0], "datatable").data;
		},
		getRows : function(jq) {
			return $.data(jq[0], "datatable").data.rows;
		}
    };

    /**
     * 解析data-options中数据
     */
    $.fn.datatable.parseOptions = function(target) {
        var t = $(target);
        return $.extend({}, $.fn.panel.parseOptions(target), $.parser.parseOptions(target, [ "url", "columns" ]));// 解析 data-options 中的初始化参数
    };

    $.parser.plugins.push('datatable');
})(jQuery);