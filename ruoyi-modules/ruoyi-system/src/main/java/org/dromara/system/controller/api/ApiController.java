package org.dromara.system.controller.api;


import lombok.RequiredArgsConstructor;
import org.dromara.system.service.IRuleService;
import org.springframework.web.bind.annotation.*;
import org.dromara.common.core.domain.R;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/scrm/customer/clue/platformClue")
public class ApiController {

    private final IRuleService ruleService;

    /**
     * 三方回调接口
     * @param platform
     * @param ruleId
     * @param msg
     * @return
     */
    @PostMapping("/callback")
    public R<Boolean> callback(@RequestParam("platform") String platform, @RequestParam("ruleId") String ruleId, @RequestBody String msg) {

        return R.ok("success", ruleService.callback(platform, ruleId, msg));
    }
}
