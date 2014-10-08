package cn.hang.mvc.result.impl;

import org.springframework.util.Assert;

import cn.hang.mvc.RequestContext;
import cn.hang.mvc.common.util.RequestContextConstants;
import cn.hang.mvc.common.util.ServiceManagers;
import cn.hang.mvc.common.util.StringUtils;
import cn.hang.mvc.service.ResultTypeRewriteService;
import cn.hang.mvc.service.ServiceManager;
import cn.hang.mvc.velocity.TemplateMergeHandlerFactory;
import cn.hang.mvc.velocity.service.VelocityService;
import cn.hang.mvc.velocity.util.VelocityConstants;

/**
 * 跳转到Velocity模板文件并输入
 * 
 * @author Hang
 *
 */
public class VelocityResultTypeHandler extends ForwardResultTypeHandler {
	
	/**
	 * 渲染服务
	 */
	private VelocityService velocityService;
	
	public VelocityResultTypeHandler() {
		velocityService = (VelocityService) ServiceManagers.getServiceManager().getService(VelocityConstants.VELOCITY_SERVICE_NAME);
	}

	public boolean handleResult(RequestContext requestContext, String path) {
		Assert.notNull(requestContext, "The Context is null!");
		Assert.notNull(path, "The view to be shown is null!");
		String resultType = requestContext.getParameter(RequestContextConstants.TYPE_PARAMETER_NAME);
		if (StringUtils.isEmpty(resultType)) {
			resultType = RequestContextConstants.DEFAULT_RESULT_TYPE;
		}
		ResultTypeRewriteService resultTypeRewriteService = (ResultTypeRewriteService) ServiceManagers.getServiceManager().getService(ServiceManager.RESULT_TYPE_REWRITE_SERVICE);
		resultType = resultTypeRewriteService.getFinalResultType(resultType);
//		if (! resultType.equals("vm")) {//返回类型不是vm
//			return super.handleResult(requestContext, path);
//		}
		return mergeToClient(requestContext, path);
	}

	/**
	 * 调用TemplateMergeHandler
	 * 
	 * @param requestContext
	 * @param path
	 * @return
	 */
	protected boolean mergeToClient(RequestContext requestContext, String path) {
		return TemplateMergeHandlerFactory.getTemplateMergeHandler().mergeToClient(requestContext, path);
	}

	/**
	 * @return the velocityService
	 */
	public VelocityService getVelocityService() {
		return velocityService;
	}

	/**
	 * @param velocityService the velocityService to set
	 */
	public void setVelocityService(VelocityService velocityService) {
		this.velocityService = velocityService;
	}
	
}
