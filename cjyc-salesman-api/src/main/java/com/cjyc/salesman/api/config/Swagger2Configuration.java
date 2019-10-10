package com.cjyc.salesman.api.config;

import com.cjyc.common.model.enums.ResultEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
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
 * 修改版地址：localhost:端口/项目baseurl/doc.html
 * @author JPG
 */
@Configuration
@ConditionalOnProperty(name = "cjyc.swagger.enabled", havingValue = "true")
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    @ConditionalOnMissingBean
    public Swagger2Property swagger2Property() {
        return new Swagger2Property();
    }

    @Bean
    public Docket createRestApi(Swagger2Property swagger2Property) {

        List<ResponseMessage> responseMessageList = new ArrayList<>();
        for(ResultEnum item : ResultEnum.values()){
            responseMessageList.add(new ResponseMessageBuilder()
                    .code(item.getCode())
                    .message(item.getMsg())
                    .build());
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(swagger2Property.getGroupName())
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(swagger2Property.getUseDefaultResponseMessages())
                .forCodeGeneration(swagger2Property.getForCodeGeneration())
                .pathMapping(swagger2Property.getUsePathMapping() ? swagger2Property.baseUrl : null)
                .apiInfo(apiInfo(swagger2Property))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger2Property.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(Swagger2Property swagger2Property) {
        return new ApiInfoBuilder()
                .title(swagger2Property.getTitle())
                .description(swagger2Property.getDescription())
                .termsOfServiceUrl(swagger2Property.getTermsOfServiceUrl())
                .version(swagger2Property.getVersion())
                .build();
    }
}