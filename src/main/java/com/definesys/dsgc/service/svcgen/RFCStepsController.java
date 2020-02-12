package com.definesys.dsgc.service.svcgen;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import io.swagger.annotations.Api;
        import org.springframework.web.bind.annotation.*;

//@AuthAspect(menuCode = "服务快速配置-RFC配置步骤", menuName = "服务快速配置-RFC配置步骤")
@Api(description = "服务快速配置-RFC配置步骤", tags = "服务快速配置-RFC配置步骤")
@RequestMapping(value = "/dsgc/svcgen")
@RestController
@Deprecated
public class RFCStepsController {


}
