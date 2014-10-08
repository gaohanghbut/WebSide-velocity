package cn.hang.mvc.run;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import cn.hang.mvc.RequestContext;
import cn.hang.mvc.common.util.ApplicationContextUtils;
import cn.hang.mvc.common.util.RequestContextConstants;
import cn.hang.mvc.common.util.ServiceManagers;
import cn.hang.mvc.common.util.StringUtils;
import cn.hang.mvc.service.ResultTypeRewriteService;
import cn.hang.mvc.service.ServiceManager;
import cn.hang.mvc.spring.holder.ApplicationContextHolder;
import cn.hang.mvc.velocity.TemplateMergeHandlerFactory;

/**
 * 支持模板引擎Velocity的请求上下文，一次请求对应一个此类的实例
 * 
 * @author Hang
 *
 */
public class VelocityRequestContext extends DefaultRequestContext implements org.apache.velocity.context.Context {
    /**
     * The context key for the HTTP request object.
     */
    public static final String REQUEST = "req";

    /**
     * The context key for the HTTP response object.
     */
    public static final String RESPONSE = "res";

	/**
	 * Velocity的上下文
	 */
	private Context context;

	public VelocityRequestContext() {
		super();
		context = new VelocityContext();
	}

	public VelocityRequestContext(HttpServletRequest servletRequest, HttpServletResponse servletResponse, RequestContext parent) {
		super(servletRequest, servletResponse, parent);
		context = new VelocityContext();
		context.put(REQUEST, servletRequest);
		context.put(RESPONSE, servletResponse);
	}

	public VelocityRequestContext(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		super(servletRequest, servletResponse);
		context = new VelocityContext();
		context.put(RESPONSE, servletResponse);
	}

	public VelocityRequestContext(RequestContext parent) {
		super(parent);
		context = new VelocityContext();
	}

	public Object put(String key, Object value) {
		return context.put(key, value);
	}

	public Object get(String key) {
		Object value = context.get(key);
		if (value == null) {
			value = super.getValue(key);
		}
		if (value == null) {//没有找到则到依赖注入容器中查找
			value = ApplicationContextUtils.getBean(ApplicationContextHolder.ROOT_APPLICATION_CONTEXT, key);
		}
		if (value == null) {
			value = super.getValue(key);
		}
		return value;
	}

	public boolean containsKey(Object key) {
		return context.containsKey(key) || super.contains(key.toString());
	}

	public Object[] getKeys() {
		return context.getKeys();
	}

	public Object remove(Object key) {
		return context.remove(key);
	}

	public Object removeValue(String key) {
		return remove(key);
	}

	public Object getValue(String key) {
		return get(key);
	}

	public void putValue(String key, Object value) {
		put(key, value);
	}

	public String[] keys() {
		Object[] keys = this.getKeys();
		String[] strkey = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			strkey[i] = keys[i].toString();
		}
		return strkey;
	}

	@Override
	public void forward(String path) {
		if (path.endsWith(".vm") || path.endsWith(".VM")) {
			TemplateMergeHandlerFactory.getTemplateMergeHandler().mergeToClient(this, path);
			this.setReturned(true);
			return;
		}
		String resultType = this.getParameter(RequestContextConstants.TYPE_PARAMETER_NAME);
		if (StringUtils.isEmpty(resultType)) {
			resultType = RequestContextConstants.DEFAULT_RESULT_TYPE;
		}
		ResultTypeRewriteService resultTypeRewriteService = (ResultTypeRewriteService) ServiceManagers.getServiceManager().getService(ServiceManager.RESULT_TYPE_REWRITE_SERVICE);
		resultType = resultTypeRewriteService.getFinalResultType(resultType);
		if (! resultType.equals("vm")) {//返回类型不是vm
			super.forward(path);
		} else {//调用模板引擎服务
			TemplateMergeHandlerFactory.getTemplateMergeHandler().mergeToClient(this, path);
			this.setReturned(true);
		}
	}
	
}
