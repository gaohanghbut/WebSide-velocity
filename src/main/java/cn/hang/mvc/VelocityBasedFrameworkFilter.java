package cn.hang.mvc;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import cn.hang.mvc.run.VelocityBasedRequestContextFactory;

/**
 * 基于模板引擎Velocity的过虑器，此中心控制器在初始化时，除了与父类中相同的初始化数据外，
 * 还需要初始化模板引擎。并注册与父类中不同的RequestContextFactory，此处注册的工厂
 * 可用于生产与Velocity的Context相关的RequestContext。
 * 
 * @author Hang
 *
 */
public class VelocityBasedFrameworkFilter extends FrameworkFilter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected RequestContextFactory getRequestContextFactory() {
		return new VelocityBasedRequestContextFactory(); 
	}
}
