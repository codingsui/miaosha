var seckill = {
    URL:{
        now:function(){return '/seckill/time/now';},
        exposer:function (seckillId) {
            return '/seckill/' +seckillId+'/exposer';
        },
        execution:function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },
    //验证手机号
    validatePhone:function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },
    handleSeckillkill:function (seckillId,node) {
        //时间到了，获取秒杀地址
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>')
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log('kilUrl:'+killUrl);
                    //绑定一次
                    $("#killBtn").one('click',function () {
                        //禁用按钮
                        $(this).addClass('disabled');
                        //发送请求
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //未开始
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                console.log("result:"+result)
            }
        })
    },
    countdown:function (sekillId,nowTime,startTime,endTime) {
        console.log(nowTime+" " +startTime+" " +endTime)
        var seckillBox = $("#seckill-box");
        if(nowTime>endTime){
            seckillBox.html('秒杀结束');
        }else if (nowTime <startTime){
            var killTime = new Date(startTime);
            console.log(killTime)
            seckillBox.countdown(killTime,function (event) {
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒')
                seckillBox.html(format)
            }).on('finish.countdown',function () {
                seckill.handleSeckillkill(seckillId,seckillBox);
            });
        }else{
            seckill.handleSeckillkill(sekillId,seckillBox);
        }
    },
    detail:{
        //详情页初始化
        init: function (params) {
            //用户手机验证，计时交互
            var killPhone = $.cookie("killPhone");
            //
            if(!seckill.validatePhone(killPhone)) {
                //绑定手机号
                var killPhoneModal = $("#killPhoneModal");
                //显示弹出层
                killPhoneModal.modal({show: true, backdrop: 'static', keyboard: false})
                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();
                    console.log('inputPhone'+inputPhone)//todo
                    if(seckill.validatePhone(inputPhone)){
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        window.location.reload();
                    }else{
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                })
            }

            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(seckill.URL.now(),{},function (result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                }else{
                    console.log("result:"+result)
                }
            })
        }

    }

}
