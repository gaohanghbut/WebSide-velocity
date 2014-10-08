package cn.hang.mvc.run;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hang.mvc.RequestContext;

/**
 * 基于模板引擎的请求上下文工厂，与DefaultRequestContextFactory不同的是，它生产的Request范围的上下文实现了Velocity中
 * 的Context接口
 * 
 * @author Hang
 *
 */
public class VelocityBasedRequestContextFactory extends DefaultRequestContextFactory {

	@Override
	public RequestContext getGenericRequestContext(HttpServletRequest req, HttpServletResponse resp, RequestContext parentRequestContext) {
		return new VelocityRequestContext(req, resp, parentRequestContext);
	}

}
