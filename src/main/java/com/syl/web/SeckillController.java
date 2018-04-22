package com.syl.web;

import com.syl.dto.Exposer;
import com.syl.dto.SeckillExecution;
import com.syl.dto.SeckillResult;
import com.syl.entity.Seckill;
import com.syl.enums.SeckillStateEnum;
import com.syl.exception.RepeatKillException;
import com.syl.exception.SeckillException;
import com.syl.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

@Controller
@RequestMapping("/seckill")//模块 url:/模块/资源/{id}/细分
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String list(){
        return "list";
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        System.out.println("------------");
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if (seckillId == null)
            return "redirect:/seckill/list";
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    /**
     * ajax
     * @param seckillId
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage());
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
                                                   @PathVariable("md5")String md5,
                                                   @CookieValue(value = "killPhone",required = false)Long phone){
        if (phone == null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId,phone,md5);
            result = new SeckillResult<SeckillExecution>(true,execution);
        }catch (RepeatKillException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true,execution);
        }catch (SeckillException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            result = new SeckillResult<SeckillExecution>(true,execution);
        }
        catch (Exception e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true,execution);
            logger.error(e.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
