package com.zjw.spring.mvc.scanner;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjw.spring.mvc.annotation.RequestMapping;
import com.zjw.spring.mvc.bean.RequestMethod;
import com.zjw.spring.scanner.AnnotationScanner;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationMvcScanner {

    /**
     * key为url,value为请求方法对象
     */
    private Map<String, RequestMethod> methods=new HashMap<String, RequestMethod>();

    private AnnotationScanner annotationScanner;

    public AnnotationMvcScanner(String packageName,AnnotationScanner annotationScanner) {
        this.annotationScanner=annotationScanner;
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(packageName)
                .addScanners(new MethodAnnotationsScanner() )
        );
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        for (Method method:
             methodsAnnotatedWith) {
            Class<?> controllerClass = method.getDeclaringClass();
            RequestMapping controllerAnnotation = controllerClass.getAnnotation(RequestMapping.class);
            String value = controllerAnnotation.value();
            String uri="";
            if(StringUtils.isNotBlank(value)){
                uri=value;
            }
            RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
            uri+=methodAnnotation.value();
            RequestMethod requestMethod=new RequestMethod();
            requestMethod.setType(methodAnnotation.type());
            requestMethod.setMethod(method);
            methods.put(uri,requestMethod);
        }
    }

    public Object execute(HttpServletRequest request) throws InvocationTargetException, IllegalAccessException, IOException {
        RequestMethod requestMethod = methods.get(request.getRequestURI());
        if (requestMethod==null){
            throw new RuntimeException("404，请求不存在！");
        }

        Method method = requestMethod.getMethod();
        Class<?> controllerClass = method.getDeclaringClass();
        Object controller = annotationScanner.getBean(controllerClass);
        Map<String, String[]> parameterMap = request.getParameterMap();
        BufferedReader reader = request.getReader();
        StringBuilder json=new StringBuilder();
        String str=null;
        while ((str=reader.readLine())!=null){
            json.append(str);
        }
        List list=new ArrayList();
        for (Class c :
             method.getParameterTypes()) {
            Object o = JSONObject.parseObject(json.toString(), c);
            list.add(o);
        }
        Object invoke = method.invoke(controller, list.toArray());
        Object returnJson = JSONObject.toJSON(invoke);
        return returnJson;
    }

}