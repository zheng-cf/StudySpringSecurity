package com.zcf.security.core.validate.code;

import com.zcf.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class ValidateCodeController {
    /**
     * 收集所有实现了{@link ValidateCodeProcessor}接口的实现类
     */
    @Autowired
    private Map<String,ValidateCodeProcessor> validateCodeProcessorMap;

    /**
     * 根据验证码的类型不同，调用不同的ValidateCodeProcessor接口的实现
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
       validateCodeProcessorMap.get(type+ SecurityConstants.VALIDATE_CODE_PROCESSOR_SUFFIX).create(new ServletWebRequest(request,response));
    }

}
