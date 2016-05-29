package xiaojinzi.base.java.net;


import xiaojinzi.base.java.net.NetTask;

/**
 * 任务执行完毕后结果的处理
 *
 * @author cxj QQ:347837667
 * @date 2015年12月8日
 */
public class ResultInfo<Parameter> {

    /**
     * 网络请求对象
     */
    public NetTask netTask;

    /**
     * 请求的结果
     */
    public Object result;

}
