package com.cjyc.web.api.config;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dto.web.waybill.SaveLocalDto;
import com.cjyc.web.api.annotations.RequestHeaderAndBody;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class RequestHeaderAndBodyResolver extends RequestResponseBodyMethodProcessor {

    public RequestHeaderAndBodyResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeaderAndBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        Object result = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        Map<String, String> headerParams = Maps.newHashMapWithExpectedSize(10);
        Iterator<String> headerNames = webRequest.getHeaderNames();
        headerNames.forEachRemaining(headerName -> headerParams.put(headerName, webRequest.getHeader(headerName)));
        BeanUtils.populate(result, headerParams);
        return result;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        SaveLocalDto dto = new SaveLocalDto();
        Map<String, String> headerParams = Maps.newHashMapWithExpectedSize(10);
        headerParams.put("roleId", "2123414");
        BeanUtils.populate(dto, headerParams);
        System.out.println(JSON.toJSONString(dto));
    }

}

