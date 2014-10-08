package cn.hang.mvc.velocity.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import cn.hang.mvc.service.Service;

public interface VelocityService extends Service {

	public abstract void requestCleanup(HttpServletRequest request, HttpServletResponse response, Context context);

	public abstract void mergeTemplate(Template template, Context context, HttpServletResponse response) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException, UnsupportedEncodingException, Exception;

	public abstract void setContentType(HttpServletRequest request, HttpServletResponse response);

	public abstract String getCharacterEncoding(HttpServletRequest request);

	public abstract Template getTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception;

	public abstract Template getTemplate(String name, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception;

	public abstract Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context ctx, String path) throws Exception;

	/**
	 * 获取模板
	 * 
	 * @param ctx
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public abstract Template handleRequest(Context ctx, String path) throws Exception;

	public abstract void error(HttpServletRequest request, HttpServletResponse response, Exception cause) throws ServletException, IOException;

}