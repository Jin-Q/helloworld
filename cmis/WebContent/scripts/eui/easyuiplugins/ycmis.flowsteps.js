/**
 * <p>导航条组件</p>
 * <p>导航条为用户提供分步式操作导航。
 *   1.当步骤state=doing时会执行onNext事件；
 *   2.当所有步骤state=done才能finish整个导航；
 *   3.finish时会调用onFinish事件
 * </p>
 * @author wangbin 2014-4-16 16:02:45
 * 修改历史
 * 1. 消费合并:修改state!=doing时也会执行onNext事件
 * 
 */
(function( $ ){

    $.fn.steps = function( options ,params) {  
        if(typeof options=="string"){
            return $.fn.steps.methods[options](this,params);
        }
        options=options||{};
        return this.each(function(){
            var steps=$.data(this,"steps");
            if(steps){
                $.extend(steps.options,options);
            }else{
                steps=$.data(this,"steps",{options:$.extend({},$.fn.steps.defaults,$.parser.parseOptions(this),options)});
            }
            init(this);
            });
                
    };
    /**
     * 初始化ul导航条内容
     */
    var init=function(target){
        $(target).addClass('wrapper');
        var ul=$(target).find('.flow-steps:eq(0)');
        //清空ul重新生成导航内容
        ul.empty();
        var opts=$.data(target, 'steps').options;
        var steps=opts.steps;
        $.each(steps,function(i,step){
            var li=$('<li/>').appendTo(ul);
            var b=$('<b/>').appendTo(li);
            if(i==0)
                b.addClass('f');
            var a=$('<a/>').appendTo(li);
            a.attr('href','javascript:void(0)');
            a.html(step.title);
            var s=$('<s/>').appendTo(li);
        });
        setStep(target,opts.step);
    }
    $.fn.steps.methods = {
            options:function(jq){
                return $.data(jq[0],"steps").options
            },
            destroy:function(jq){
                return jq.each(function(){
                    $(this).removeData('steps');
                });
            },
            //取得当前步骤索引
            getStep: function(jq) {
                return $.data(jq[0],"steps").options.step;
            },
            //根据索引取得步骤的panel对象
            getStepPanel: function(jq,index) {
                var opts=$.data(jq[0],"steps").options;
                if(!index){
                    index=opts.step;
                }
                return $(jq[0]).find('.div-step:eq(' + index  + ')');
            },//根据索引取得步骤属性配置对象,参数缺省时取当前步骤
            getStepOptions: function(jq,index) {
                var opts=$.data(jq[0],"steps").options;
                if(!index){
                    index=opts.step;
                }
                return opts.steps[index];
            },
            setStep:function(jq,step){//设置步骤
                return jq.each(function(){
                    var opts=$.data(this,"steps").options;
                    var currentIndex=opts.step;
                    if(step!=currentIndex){
                        var currentDiv=$(this).find('.div-step:eq(' + currentIndex  + ')');
                        /**
                         * 1.取得当前步骤配置信息
                         * 2.如果当前步骤状态不为done状态，调用当前步骤onNext事件检查
                         */
                        var currentStep=opts.steps[currentIndex];
                        if(currentStep.onNext){
                        var res=currentStep.onNext.call(this,currentDiv,false);
                            if(!res)
                                return false;
                        }
                        //当前步骤完成
                        currentStep.state='done';
                    }
                    setStep(this,step);
                });
            },
            prev:    function(jq) { //上一步
                return jq.each(function(){
                    var opts=$.data(this,"steps").options;
                    var step=opts.step;
                    //第一步直接返回
                    if(step==0)
                        return false;
                    var currentIndex=opts.step;
                    var currentDiv=$(this).find('.div-step:eq(' + currentIndex  + ')');
                    /**
                     * 1.取得当前步骤配置信息
                     * 2.若当前步骤未完成（done）调用当前步骤onNext事件检查
                     */
                    var currentStep=opts.steps[currentIndex];
                    if(currentStep.onNext){
                        var res=currentStep.onNext.call(this,currentDiv,false);
                        if(!res)
                            return false;
                    }
                    //当前步骤完成
                    currentStep.state='done';
                    
                    var l    = $(this).find('ul li').length;
                    if(step==0) return false;
                    if(step == l) step = step-1;
                    return setStep(this,step - 1); 
                });
            },
            next:    function(jq) {  //下一步
                return jq.each(function(){
                    var opts=$.data(this,"steps").options;
                    var step=opts.step;
                    //最后一步直接返回
                    if(step==opts.steps.length-1)
                        return false;
                    var currentDiv=$(this).find('.div-step:eq(' + step  + ')');
                    /**
                     * 1.取得当前步骤配置信息
                     * 2.若当前步骤未完成（done）调用当前步骤onNext事件检查
                     */
                    var currentStep=opts.steps[step];
//                  if(currentStep.onNext&&currentStep.state!='done'){
                        var res=currentStep.onNext.call(this,currentDiv,true);
                        if(!res)
                            return false;
//                  }
                    //当前步骤完成
                    currentStep.state='done';
                    //取得当前div对象
                    var l = $(this).find('ul li').length-1;
                    step=step+1;
                    if(step < 0) step = 0;
                    if(step > l) step = l;
                    setStep(this,step);
                });
                
            },
            finish:function(jq){//结束导航任务
                return jq.each(function(){
                    var opts=$.data(this,"steps").options;
                    var steps=opts.steps;
                    //如果当前步骤未完成（done），结束前执行当前步骤检查
                    var index=opts.step;
                    var endStep=steps[index];
                    var endDiv=$(this).find('.div-step:eq(' +  index + ')');
                    if(endStep.onNext&&endStep.state!='done'){
                        var res=endStep.onNext.call(this,endDiv);
                        if(!res)
                            return false;
                    }
                    //步骤完成
                    endStep.state='done';
                
                    
                    var flag=true;
                    //检查所有步骤是否已经完成
                    $.each(steps,function(i,step){
                        if(!(step.state=='done')){
                            flag=false;
                            return false;
                        }
                    });
                    //检查通过
                    if(flag){
                        $.messager.confirm("提示",opts.confirmMsg,function(r){
                            if (r){
                                //调用onFinish
                                if(opts.onFinish){
                                    flag=opts.onFinish.call(this);
                                }
                            }
                        });
                    }else{//未完成提示
                        $.messager.alert('提示',opts.undoneMsg,'error');
                    }
                    
                    
                    return flag;
                });
            },
            enableStep:function(jq,step){//设置步骤连接可用，当参数step=all时设置所有连接可用
                return jq.each(function(){
                    var opts=$.data(this,"steps").options;
                    var id=$(this).attr('id');
                    if(step=='all'){
                        var list=$(this).find('ul li');
                        $.each(list,function(i,li){
                            $(li).bind('click',function(){$('#'+id).steps('setStep',i);});
                        });
                    }else{
                        var li=$(this).find('ul li:nth(' + step + ')');
                        $(li).bind('click',function(){$('#'+id).steps('setStep',step);});
                    }
                });
            },
            disableStep:function(jq,step){//设置步骤连接不可用，当参数step=all时设置所有连接不可用
                return jq.each(function(){
                    var opts=$.data(this,"steps").options;
                    var id=$(this).attr('id');
                    if(step=='all'){
                        var list=$(this).find('ul li');
                        $.each(list,function(i,li){
                            $(li).unbind('click');
                        });
                    }else{
                        var li=$(this).find('ul li:nth(' + step + ')');
                        $(li).unbind('click');
                    }
                });
            }
    };
    var setStep=function(self,stepNumber ) {  
        var opts=$.data(self,"steps").options;
        // 重置
        $(self).find('ul li').removeClass('current');
        // 隐藏所有div
        $(self).find('.div-step').hide();
        // 显示当前div
        var currentDiv= $(self).find('.div-step:eq(' + stepNumber  + ')');
        currentDiv.show();
        //打开步骤之前触发
        if(opts.beforeOpen){
            opts.beforeOpen.call(self,stepNumber);
        }
        
        $(self).find('ul li:nth(' + stepNumber + ')').addClass('current');
        
        $.data(self,"steps").options.step = stepNumber;
        
        /** 解决步骤过多显示不全问题 begin */
        var stepsnum=opts.steps.length;
        var stwd = $(self).find("ul.flow-steps li").width();
        $(self).find("ul.flow-steps").width(stepsnum * stwd);
        if(stepNumber>=3){
        	$(".flow-steps-div").animate({
    			scrollLeft : (stepNumber-3) * stwd
    		}, 1000);

        }
        /** 解决步骤过多显示不全问题 end */
        return stepNumber;
    }
    $.fn.steps.defaults=$.extend({}, {
        id:null,
        onFinish:function(){},//结束时触发
        /**
         * 步骤定义[{title:'步骤1',onNext:function(){return true;},cache:true},{...}]
         * 每个步骤需要定义：title标题；onNext离开当前步骤时触发；state状态：undealt未处理，doing处理中，done已完成；cache是否缓存内容:默认true；
         */
        steps:null,
        step:0,//当前步骤
        undoneMsg:"您还有未完成的步骤！",//未完成提示信息
        confirmMsg:"你确定要提交任务吗？",
        beforeOpen:function(index){//在打开触发：默认根据步骤url为div添加iframe，如果cache=true则刷新。
            var steps=$(this).steps('options').steps;
            var step=steps[index];
            var stepDiv=$(this).find('.div-step:eq(' + index + ')')[0];
            if(!stepDiv){
                stepDiv=$('<div/>').appendTo(this);
                stepDiv.addClass('div-step');
            }
            var frame=$(stepDiv).find('iframe');
            //刷新
            if(frame.length > 0){
                if(!step.cache){
                    frame[0].src = 'about:blank';
                    frame[0].contentWindow.document.write('');
                    $(frame[0]).empty();
                    frame[0].contentWindow.close();
                    frame.remove();
                }else{
                    return;
                }
            }
            //初始化添加iframe
            if(step.url){
                var id='iframe-step'+index;
                var iframe=$('<iframe scrolling="auto" id="'+id+'" name="'+id+'" onload="autoHeight(\''+id+'\')" frameborder="0"  src="'+step.url+'" style="width:100%;height:99%;" allowtransparency="true"></iframe>').appendTo(stepDiv);
            }
        }
    });
})( jQuery );

//iframe高度自适应
function autoHeight(id){
    var mainheight = $('#'+id).contents().find("body").height()+50;
    if(mainheight<300)
        mainheight=300;
    $('#'+id).height(mainheight);
}
