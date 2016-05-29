package xiaojinzi.base.java.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xiaojinzi.base.java.net.handler.ResponseHandler;

/**
 * 网络任务的对象
 *
 * @author cxj
 */
public class NetTask<Parameter> {

    /**
     * 请求是GET请求
     */
    public static final int GET = 0;

    /**
     * 请求是POST请求
     */
    public static final int POST = 1;

    /**
     * 本地文件的前缀标识
     */
    public static final String LOCALFILEURLPREFIX = "file:";


    /**
     * 加载数据是本地加载的
     */
    public static final int LOCAL = 11;

    /**
     * 加载数据是网络加载的
     */
    public static final int NET = 12;

    /**
     * 加载数据的方式
     */
    private int loadDataType = NET;

    /**
     * 请求的方式,默认是get请求
     */
    private int requestMethod = GET;


    /**
     * 请求的网址
     */
    private String requesutUrl;

    /**
     * 请求的数据类型,默认是字节流
     */
    private int responseDataStyle = ResponseHandler.INPUTSTREAMDATA;

    /**
     * 请求完成后回调处理数据的接口
     */
    private ResponseHandler<Parameter> responseHandler = null;

    /**
     * 参数的数组,每个请求可以携带数据,传递给处理的方法
     */
    private Parameter[] parameters;

    /**
     * post的时候要提交的文件
     */
    private List<FileInfo> filesParameter = new ArrayList<FileInfo>();

    /**
     * 普通字段
     */
    private Map<String, String> textParameter = new HashMap<String, String>();


    public NetTask<Parameter> setRequesutUrl(String requesutUrl) {
        this.requesutUrl = requesutUrl;
        return this;
    }

    public String getRequesutUrl() {
        return requesutUrl;
    }

    public NetTask<Parameter> setResponseDataStyle(int responseDataStyle) {
        this.responseDataStyle = responseDataStyle;
        return this;
    }

    public NetTask<Parameter> setResponseHandler(ResponseHandler<Parameter> responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }


    public NetTask<Parameter> setParameters(Parameter... parameters) {
        this.parameters = parameters;
        return this;
    }

    public NetTask<Parameter> setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public void setLoadDataType(int loadDataType) {
        this.loadDataType = loadDataType;
    }

    public int getRequestMethod() {
        return requestMethod;
    }


    public int getResponseDataStyle() {
        return responseDataStyle;
    }

    public ResponseHandler<Parameter> getResponseHandler() {
        return responseHandler;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public int getLoadDataType() {
        return loadDataType;
    }

    /**
     * 添加普通的参数
     *
     * @param name
     * @param value
     */
    public NetTask<Parameter> addTextParameter(String name, String value) {
        textParameter.put(name, value);
        return this;
    }

    /**
     * 获取所有的普通字段
     *
     * @return
     */
    public Map<String, String> getTextParameter() {
        return textParameter;
    }

    /**
     * 添加要post的文件,只有请求方式是POST的时候起作用
     *
     * @param key
     * @param file
     */
    public NetTask<Parameter> addPostFile(String key, File file) {
        //做了简装性质的判断
        if (key != null && !"".equals(key) && file != null && file.exists() && file.isFile()) {
            filesParameter.add(new FileInfo(key, file));
        }
        return this;
    }

    /**
     * 获取要提交的文件
     *
     * @return
     */
    public List<FileInfo> getFilesParameter() {
        return filesParameter;
    }

}
