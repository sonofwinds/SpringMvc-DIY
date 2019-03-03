package com.study.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.annotation.DIYController;
import com.study.annotation.DIYMapping;
import com.study.util.CommonUtil;

/**
 * Servlet implementation class DIYDispatchServlet
 */
public class DIYDispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 List<String> packageNames = new ArrayList<String>();
    private String scanPackage = "com.study";
    // 所有类的实例，key是注解的value,value是所有类的实例
    Map<String, Object> ioc = new HashMap<String, Object>();
    //key是请求的url value是对应的方法名
    Map<String, String> handerMap = new HashMap<String, String>();
    //key是请求的url value是对应的实例
    Map<String,Object> methodMap = new HashMap<String, Object>();
	@Override
	public void init() throws ServletException {
		//扫描包
		CommonUtil.scanPackage(packageNames,this.getClass(), scanPackage);
		initIOC();
	}    
	private void initIOC() {
		try {
			for(String packs : packageNames) {
				//筛选出里面的controller实例
				int endPoint = packs.lastIndexOf(".class");
				packs = packs.substring(0, endPoint);
				Class<?> cla = Class.forName(packs);
				DIYController diyController = cla.getAnnotation(DIYController.class);
				if(diyController == null) {
					continue;
				}
				String controllerName = diyController.value();
				if("".equals(controllerName)) {
					controllerName = cla.getSimpleName();
				}
				Object obj = cla.newInstance();
				ioc.put(controllerName, obj);
				Method[] methods = cla.getDeclaredMethods();
				for(Method method : methods) {
					method.setAccessible(true);
					DIYMapping diyMapping = method.getAnnotation(DIYMapping.class);
					if(diyMapping == null) {
						continue;
					}
					String url = diyMapping.value();
					methodMap.put(url, obj);
					handerMap.put(url, method.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DIYDispatchServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI();
		String contextPath = request.getContextPath();
		url = url.replace(contextPath, "");
		Object obj = methodMap.get(url);
		String methodName = handerMap.get(url);
		try {
			Method methodHandle = obj.getClass().getDeclaredMethod(methodName, null);
			Object returnResult = methodHandle.invoke(obj, null);
			String view = returnResult.toString();
			request.getRequestDispatcher("/"+view+".jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
