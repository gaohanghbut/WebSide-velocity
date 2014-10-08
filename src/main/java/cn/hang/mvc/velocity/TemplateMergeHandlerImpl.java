package cn.hang.mvc.velocity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import cn.hang.mvc.RequestContext;
import cn.hang.mvc.common.util.ServiceManagers;
import cn.hang.mvc.run.VelocityRequestContext;
import cn.hang.mvc.velocity.service.VelocityService;
import cn.hang.mvc.velocity.util.VelocityConstants;

/**
 * 处理模板引擎的调用
 * 
 * @author Hang
 *
 */
public class TemplateMergeHandlerImpl implements TemplateMergeHandler {
	
	private final Log log = LogFactory.getLog(TemplateMergeHandlerImpl.class);
	
	/**
	 * Velocity服务
	 */
	private VelocityService velocityService;

	public TemplateMergeHandlerImpl() {
		velocityService = (VelocityService) ServiceManagers.getServiceManager().getService(VelocityConstants.VELOCITY_SERVICE_NAME);
	}
	/**
	 * 调用模板引擎
	 * 
	 * @param requestContext
	 * @param path
	 * @return
	 */
	public boolean mergeToClient(RequestContext requestContext, String path) {
		Context context = null;
		HttpServletRequest request = requestContext.getHttpServletRequest();
		HttpServletResponse response = requestContext.getHttpServletResponse();
		try {
			if (requestContext instanceof Context) {
				context = (Context) requestContext;
			} else {
			    context = new VelocityRequestContext(requestContext);
			}
			
			velocityService.setContentType(request, response);

			Template template = velocityService.handleRequest(request, response, context, path);

			if (template == null) {
				return Boolean.FALSE;
			}

			velocityService.mergeTemplate(template, context, response);
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			e.printStackTrace();
			try {
				velocityService.error(request, response, e);
			} catch (ServletException e1) {
				log.error(e1);
			} catch (IOException e1) {
				log.error(e1);
			}
			return Boolean.FALSE;
		} finally {
			velocityService.requestCleanup(request, response, context);
		}
	}

}
