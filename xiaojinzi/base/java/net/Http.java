package xiaojinzi.base.java.net;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import xiaojinzi.base.java.net.exception.ResponseCodeException;
import xiaojinzi.base.java.io.FileUtil;


/**
 * 封装了网络请求,Get请求和Post请求都可以
 *
 * @author xiaojinzi
 */
public class Http {


    /**
     * 默认超时的时间,5秒
     */
    public static int TIMEOUTMILLIS = 5000;

    /**
     * post请求提交数据的时候的内容的前缀
     */
    public static final String CONTENTPREFIX = "--";

    /**
     * 报文中的一行的结束标志
     */
    public static final String LINEEND = "\r\n";

    /**
     * POST请求的时候,如果要传送文件的时候,那么content-type就是这个
     */
    public static final String MULTIPART_FROM_DATA = "multipart/form-data";

    /**
     * 当没有文件的时候的内容类型,get请求和post请求都可以是这个
     */
    public static final String NORMAL_FROM_DATA = "application/x-www-form-urlencoded";

    /**
     * 报文中的内容开始的标识
     */
    public static final String contentPrefix = "--";


    /**
     * android提供的类似于Map的集合对象
     */
    private static Map<Long, Integer> array = new HashMap<Long, Integer>();

    /**
     * 在执行方法{@link Http#sendRequest(NetTask)}
     * 中,如果请求正常,那么返回内容的长度将会通过这个方法存放到集合中
     *
     * @param threadId      线程的id
     * @param contentLength 线程成功执行的请求返回数据的长度
     */
    private synchronized static void putContentLength(Long threadId, Integer contentLength) {
        array.put(threadId, contentLength);
    }

    /**
     * 外界可以通过这个方法获取返回结果的长度
     * 获取一次之后,这个数据将从集合中移除
     *
     * @param threadId 线程的id
     * @return 线程成功执行的请求返回数据的长度
     */
    public synchronized static Integer getContentLength(Long threadId) {
        Integer result = array.get(threadId);
        array.remove(threadId);
        return result;
    }

    /**
     * 发送请求,同步的,并非异步
     *
     * @param netTask
     * @return
     */
    public static InputStream sendRequest(NetTask netTask) throws IOException {

        //网络任务开始之前的检查
        checkBeforRequestBegin(netTask);

        // 创建Url对象
        URL url = new URL(netTask.getRequesutUrl());

        // 获取连接对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 设置超时的时间
        conn.setConnectTimeout(TIMEOUTMILLIS);

        //添加一些基本的头信息
        addBaseRequestProperty(conn);

        if (netTask.getRequestMethod() == NetTask.POST) { //如果要post请求

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //拿到一个随机生成的标识
            String boundary = java.util.UUID.randomUUID().toString();
            //设置文件类型
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + boundary);

            //下面开始输出要提交的数据

            //拿到输出流
            OutputStream outputStream = conn.getOutputStream();

            //创建一个StringBuffer避免频繁的字符串的操作
            StringBuffer sb = new StringBuffer();

            sb.append(LINEEND).append(LINEEND);

            //拿到普通字段的迭代器
            Iterator<Map.Entry<String, String>> it = netTask.getTextParameter().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                //拿到key和value
                String key = entry.getKey();
                String value = entry.getValue();

                //------WebKitFormBoundaryuw6PpLVEjOL1wGBw
                sb.append(CONTENTPREFIX).append(boundary).append(LINEEND);
                //Content-Disposition: form-data; name="name"
                sb.append("Content-Disposition: form-data; name=\"" + key + "\"").append(LINEEND);
                sb.append(LINEEND);
                sb.append(value).append(LINEEND);
            }

            byte[] textParameterBytes = getTextParameterByte(sb.toString());

            //输出普通的字段的数据
            outputStream.write(textParameterBytes);

            List<FileInfo> filesParameter = netTask.getFilesParameter();

            for (int i = 0; i < filesParameter.size(); i++) {
                FileInfo fileInfo = filesParameter.get(i);

                sb.delete(0, sb.length());

                //------WebKitFormBoundaryuw6PpLVEjOL1wGBw
                sb.append(CONTENTPREFIX).append(boundary).append(LINEEND);
                //Content-Disposition: form-data; name="file"; filename="PasswordBreak4@81_403226.exe"
                sb.append("Content-Disposition: form-data; name=\"" + fileInfo.key + "\"; filename=\"" + fileInfo.file.getName() + "\"").append(LINEEND);
                //Content-Type: application/x-msdownload
//                sb.append("Content-Type: application/octet-stream").append(LINEEND);
                sb.append(LINEEND);

                outputStream.write(getTextParameterByte(sb.toString()));

                //文件真正的数据输出
                FileUtil.readFileToOutputStream(fileInfo.file, outputStream);

                outputStream.write(getTextParameterByte(LINEEND));


            }

            sb.delete(0, sb.length());

            //添加结尾的标识
            sb.append(CONTENTPREFIX).append(boundary).append(CONTENTPREFIX).append(LINEEND);
            outputStream.write(getTextParameterByte(sb.toString()));

        }

        return getInputStream(netTask, conn);

    }

    /**
     * 请求开始的之前对请求做一个检查
     *
     * @param netTask
     */
    private static void checkBeforRequestBegin(NetTask netTask) {
        //检查网址
        if (netTask.getRequesutUrl() == null || netTask.getRequesutUrl().equals("")) {
            throw new IllegalArgumentException("the request url can not be null or empty");
        }
        //检查请求的方法
        if (netTask.getRequestMethod() != NetTask.GET && netTask.getRequestMethod() != NetTask.POST) {
            throw new IllegalArgumentException("unknown request method");
        }
    }

    /**
     * 添加一些固定的头信息
     *
     * @param conn
     */
    private static void addBaseRequestProperty(HttpURLConnection conn) {
        // 以下是设置一些头信息
        conn.addRequestProperty("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.addRequestProperty("Accept-Charset", "GB2312,GBK,utf-8;q=0.7,*;q=0.7");
        conn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        // conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
        conn.addRequestProperty("Connection", "keep-alive");
    }

    /**
     * @param netTask
     * @param conn
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(NetTask netTask, HttpURLConnection conn) throws IOException {
        // 获取相应码
        int responseCode = conn.getResponseCode();

        // 如果请求成功
        if (responseCode == HttpURLConnection.HTTP_OK) {

            // 获取输入流
            InputStream is = conn.getInputStream();

            long threadId = Thread.currentThread().getId();
            putContentLength(threadId, conn.getContentLength());

            return is;

        } else {
            throw new ResponseCodeException("the responseCode is not HTTP_OK:200");
        }
    }

    /**
     * 获取普通字段的数据
     *
     * @param content
     * @return
     */
    private static byte[] getTextParameterByte(String content) {
        return content.getBytes();
    }


}
