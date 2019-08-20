package com.zjw.spring.mvc.servlet;


import com.zjw.spring.mvc.scanner.AnnotationMvcScanner;
import com.zjw.spring.scanner.AnnotationScanner;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet extends HttpServlet {

    private AnnotationMvcScanner annotationMvcScanner;

    private AnnotationScanner annotationScanner;

    @Override
    public void init(ServletConfig config) throws ServletException {
        String packageName = config.getInitParameter("packageName");
        annotationScanner=new AnnotationScanner(packageName);
        annotationMvcScanner=new AnnotationMvcScanner(packageName,annotationScanner);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            Object execute = annotationMvcScanner.execute(request);
            response.getWriter().println(execute);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
