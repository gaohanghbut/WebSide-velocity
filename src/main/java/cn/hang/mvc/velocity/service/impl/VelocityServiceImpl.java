package cn.hang.mvc.velocity.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.util.SimplePool;

import cn.hang.common.annotation.ThreadSafe;
import cn.hang.mvc.common.util.ServletUtils;
import cn.hang.mvc.velocity.service.VelocityService;
import cn.hang.mvc.velocity.util.VelocityConstants;

/**
 * Velocity渲染服务
 * 
 * @author Hang
 *
 */
@ThreadSafe
public class VelocityServiceImpl implements VelocityService {

	/**
	 * 日志记录
	 */
	private final Log log = LogFactory.getLog(VelocityService.class);
	
	/**
	 * 默认ContentType
	 */
	protected static String defaultContentType;
	/**
	 * 对象池
	 */
	private static SimplePool writerPool = new SimplePool(40);

	public VelocityServiceImpl() {
		defaultContentType = RuntimeSingleton.getString(VelocityConstants.CONTENT_TYPE, VelocityConstants.DEFAULT_CONTENT_TYPE);
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#requestCleanup(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context)
	 */
	public void requestCleanup(HttpServletRequest request, HttpServletResponse response, Context context) {
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#mergeTemplate(org.apache.velocity.Template, org.apache.velocity.context.Context, javax.servlet.http.HttpServletResponse)
	 */
	public void mergeTemplate(Template template, Context context, HttpServletResponse response) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException, UnsupportedEncodingException, Exception {
		ServletOutputStream output = response.getOutputStream();
		VelocityWriter vw = null;
		String encoding = response.getCharacterEncoding();
	
		try {
			vw = (VelocityWriter) writerPool.get();
	
			if (vw == null) {
				vw = new VelocityWriter(
						new OutputStreamWriter(output, encoding), 4 * 1024,
						true);
			} else {
				vw.recycle(new OutputStreamWriter(output, encoding));
			}
	
			template.merge(context, vw);
		} finally {
			if (vw != null) {
				try {
					vw.flush();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
	
				vw.recycle(null);
				writerPool.put(vw);
			}
		}
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#setContentType(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void setContentType(HttpServletRequest request, HttpServletResponse response) {
		String contentType = defaultContentType;
		int index = contentType.lastIndexOf(';') + 1;
		if (index <= 0 || (index < contentType.length() && contentType.indexOf("charset", index) == -1)) {
			String encoding = getCharacterEncoding(request);
			contentType += "; charset=" + encoding;
		}
		response.setContentType(contentType);
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#chooseCharacterEncoding(javax.servlet.http.HttpServletRequest)
	 */
	public String chooseCharacterEncoding(HttpServletRequest request) {
		return RuntimeSingleton.getString(RuntimeConstants.OUTPUT_ENCODING, VelocityConstants.DEFAULT_OUTPUT_ENCODING);
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.result.impl.VelocityService#getTemplate(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#getTemplate(java.lang.String)
	 */
	public Template getTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
		return RuntimeSingleton.getTemplate(name);
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.result.impl.VelocityService#getTemplate(java.lang.String, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#getTemplate(java.lang.String, java.lang.String)
	 */
	public Template getTemplate(String name, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
		return RuntimeSingleton.getTemplate(name, encoding);
	}

	/* (non-Javadoc)
	 * 
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.lang.String)
	 */
	public Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context ctx, String path) throws Exception {
		Template t = handleRequest(ctx, path);
	
		if (t == null) {
			throw new Exception("handleRequest(Context) returned null - no template selected!");
		}
		return t;
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#handleRequest(org.apache.velocity.context.Context, java.lang.String)
	 */
	public Template handleRequest(Context ctx, String path) throws Exception {
		Template template = getTemplate(path, getCharacterEncoding(null));
		return template;
	}

	/* (non-Javadoc)
	 * @see cn.hang.mvc.velocity.service.impl.VelocityService#error(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Exception)
	 */
	public void error(HttpServletRequest request, HttpServletResponse response, Exception cause) throws ServletException, IOException {
		StringBuffer html = new StringBuffer();
		html.append("<html>");
		html.append("<title>Error</title>");
		html.append("<body bgcolor=\"#ffffff\">");
		html.append("<h2>VelocityServlet: Error processing the template</h2>");
		html.append("<pre>");
		String why = cause.getMessage();
		if (why != null && why.trim().length() > 0) {
			html.append(why);
			html.append("<br>");
		}
	
		StringWriter sw = new StringWriter();
		cause.printStackTrace(new PrintWriter(sw));
	
		html.append(sw.toString());
		html.append("</pre>");
		html.append("</body>");
		html.append("</html>");
		response.getOutputStream().print(html.toString());
	}

	public String getCharacterEncoding(HttpServletRequest request) {
		return chooseCharacterEncoding(request);
	}

	public void service() {
		initVelocity();
	}

	/**
	 * 初始化Velocity
	 */
	protected void initVelocity() {
		try {
			Properties props = loadConfiguration();
			Velocity.init(props);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 加载Velocity初始化配置文件
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected Properties loadConfiguration() throws IOException, FileNotFoundException {
		String propsFile = getVelocityInitConfigurationFileName();
		Properties p = new Properties();

		if (propsFile != null) {
			p.load(getServletContext().getResourceAsStream(propsFile));
		}

		return p;
	}

	private ServletContext getServletContext() {
		return ServletUtils.getServletContext();
	}
	
	/**
	 * 返回Velocity初始化配置文件的路径
	 * @return
	 */
	protected String getVelocityInitConfigurationFileName() {
		return "/WEB-INF/velocity-init.properties";
	}
	

}