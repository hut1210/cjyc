package com.cjyc.customer.api.config;

import com.cjyc.customer.api.annotations.ApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cjyc.customer.api.until.ApiVersionContant.CUSTOMER_APP_100;

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
public class SwaggerConfiguration {

    @Bean
    public Docket vDefault() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(0).message("成功").build());
        responseMessageList.add(new ResponseMessageBuilder().code(9).message("处理失败").build());

        return new Docket(DocumentationType.SWAGGER_2)
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

    //app1.0.0版本对外接口
    @Bean
    public Docket vApp100(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(CUSTOMER_APP_100)
                .select()
                .apis(input -> {
                    ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                    if(apiVersion!=null&& Arrays.asList(apiVersion.group()).contains(CUSTOMER_APP_100)){
                        return true;
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
     return new ApiInfoBuilder()
     .title("长久韵车客户端api接口文档")
     .contact("韵车后台组")
     .description("韵车客户端api接口文档")
     .termsOfServiceUrl("http://ip:9501/")
     .version("1.0.0")
     .build();
    }
}