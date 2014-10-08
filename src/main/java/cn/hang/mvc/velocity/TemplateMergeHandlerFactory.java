package cn.hang.mvc.velocity;

/**
 * 静态工厂，返回TemplateMergeHandler实例
 * 
 * @author Hang
 *
 */
public class TemplateMergeHandlerFactory {

	/**
	 * 单例的处理器
	 */
	private static final TemplateMergeHandler TEMPLATE_MERGE_HANDLER = new TemplateMergeHandlerImpl();
	
	/**
	 * 工厂方法
	 * 
	 * @return TemplateMergeHandler对象
	 */
	public static TemplateMergeHandler getTemplateMergeHandler() {
		return TEMPLATE_MERGE_HANDLER;
	}
}
