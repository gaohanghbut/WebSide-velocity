package cn.hang.mvc.velocity.runtime;

import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hang.mvc.RequestContext;
import cn.hang.mvc.run.VelocityRequestContext;

/**
 * 本场请求上下文，在没有HTTP请求的情况下使用
 * 
 * @author Hang
 * 
 */
public class VelocityNativeRequestContext extends VelocityRequestContext {
	
	/**
	 * 参数
	 */
	private Map<String, String> params; 

	public String getParameter(String paramName) {
		return null;
	}

	public String getParameter(String paramName, String charset) {
		return null;
	}

	public String getParameter(String paramName, Charset charset) {
		return null;
	}

	public String[] getParameterValues(String paramName) {
		return null;
	}

	public String getURI() {
		return null;
	}

	public void forward(String path) {
	}

	public void redirect(String path) {
	}

	public int getIntParameter(String param) {
		return 0;
	}

	public boolean getBooleanParameter(String param) {
		return false;
	}

	public long getLongParameter(String param) {
		return 0;
	}

	public short getShortParameter(String param) {
		return 0;
	}

	public <T> T getEntiry(Class<T> c) {
		return null;
	}

	public String getURL() {
		return null;
	}

	public boolean hasReturned() {
		return false;
	}

	public String getRequestModuleName() {
		return null;
	}

	public String getResource() {
		return null;
	}

	public String getScreenName() {
		return null;
	}

	public String getActionName() {
		return null;
	}

	public Map<String, String> getParameterMap() {
		return null;
	}

	public Object getValue(String key) {
		return null;
	}

	public void putValue(String key, Object value) {
	}

	public RequestContext getParent() {
		return null;
	}

	public Object removeValue(String key) {
		return null;
	}

	public boolean contains(String key) {
		return false;
	}

	public String[] keys() {
		return null;
	}

	public HttpServletRequest getHttpServletRequest() {
		return null;
	}

	public HttpServletResponse getHttpServletResponse() {
		return null;
	}
}
