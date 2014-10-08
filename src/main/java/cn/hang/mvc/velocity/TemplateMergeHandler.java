package cn.hang.mvc.velocity;

import cn.hang.mvc.RequestContext;

/**
 * 模板处理接口
 * 
 * @author Hang
 *
 */
public interface TemplateMergeHandler {

	/**
	 * 执行模板渲染
	 * 
	 * @param requestContext
	 * @param path
	 * @return
	 */
	boolean mergeToClient(RequestContext requestContext, String path);

}