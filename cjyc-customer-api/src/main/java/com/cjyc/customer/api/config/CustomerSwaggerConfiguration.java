package com.cjyc.customer.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 * 访问地址：localhost:端口/项目baseurl/swagger-ui.html
 * http://localhost:9502/cjyc/customer/swagger-ui.html
 * 修改版地址：localhost:端口/项目baseurl/doc.html
 * @Author JPG
 * @Date 2019/7/12 8:57
 */
@Configuration
@EnableSwagger2
public class CustomerSwaggerConfiguration {

    @Bean
    public Docket apiConfig() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(0).message("成功").build());
        responseMessageList.add(new ResponseMessageBuilder().code(9).message("处理失败").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("客户端api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cjyc.customer.api.controller"))//扫描的API包
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList);
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("长久韵车客户端接口文档")
                .contact("韵车后台组")
                .description("长久韵车客户端接口文档")
                .termsOfServiceUrl("http://ip:9501/")
                .version("1.0.0")
                .build();
    }
}