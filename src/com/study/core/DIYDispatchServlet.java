package com.study.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DIYDispatchServlet
 */
public class DIYDispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String scanPackage = "com.study";
	private void initMapping() {
		
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DIYDispatchServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		//扫描包下所有的类
		
	}
public static void scanPackage() {
	 // 第一个class类的集合
    Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
    // 是否循环迭代
    boolean recursive = true;
    // 获取包的名字 并进行替换
    String packageName = pack;
    String packageDirName = packageName.replace('.', '/');
    // 定义一个枚举的集合 并进行循环来处理这个目录下的things
    Enumeration<URL> dirs;
    try {
        dirs = Thread.currentThread().getContextClassLoader().getResources(
                packageDirName);
        // 循环迭代下去
        while (dirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = dirs.nextElement();
            // 得到协议的名称
            String protocol = url.getProtocol();
            // 如果是以文件的形式保存在服务器上
            if ("file".equals(protocol)) {
                System.err.println("file类型的扫描");
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                scanPackage(packageName, filePath,
                        recursive, classes);
            } else if ("jar".equals(protocol)) {
                // 如果是jar包文件
                // 定义一个JarFile
                System.err.println("jar类型的扫描");
                JarFile jar;
                try {
                    // 获取jar
                    jar = ((JarURLConnection) url.openConnection())
                            .getJarFile();
                    // 从此jar包 得到一个枚举类
                    Enumeration<JarEntry> entries = jar.entries();
                    // 同样的进行循环迭代
                    while (entries.hasMoreElements()) {
                        // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        // 如果是以/开头的
                        if (name.charAt(0) == '/') {
                            // 获取后面的字符串
                            name = name.substring(1);
                        }
                        // 如果前半部分和定义的包名相同
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            // 如果以"/"结尾 是一个包
                            if (idx != -1) {
                                // 获取包名 把"/"替换成"."
                                packageName = name.substring(0, idx)
                                        .replace('/', '.');
                            }
                            // 如果可以迭代下去 并且是一个包
                            if ((idx != -1) || recursive) {
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class")
                                        && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(
                                            packageName.length() + 1, name
                                                    .length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class
                                                .forName(packageName + '.'
                                                        + className));
                                    } catch (ClassNotFoundException e) {
                                        // log
                                        // .error("添加用户自定义视图类错误 找不到此类的.class文件");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    // log.error("在扫描用户定义视图时从jar包获取文件出错");
                    e.printStackTrace();
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return classes;
}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
