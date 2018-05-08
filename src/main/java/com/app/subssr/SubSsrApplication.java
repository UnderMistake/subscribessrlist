package com.app.subssr;

import net.sf.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.InetAddress;
import java.util.Base64;

@SpringBootApplication
@RestController
public class SubSsrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubSsrApplication.class, args);
    }

    @RequestMapping("/getList")
    public String GetSsrList() {
        String json="";
        String ssrlist="";
        String localIp=getLocalHostIP();
        try {
            //ssr配置文件地址
            json=ReadFile("/usr/local/shadowsocksr/mudb.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray items=JSONArray.fromObject(json);
        int size=items.size();

        for (int i = 0; i < size; i++) {

            String method = items.getJSONObject(i).getString("method");
            String port = items.getJSONObject(i).getString("port");
            String protocol =items.getJSONObject(i).getString("protocol");
            String obfs=items.getJSONObject(i).getString("obfs");
            String passwd=items.getJSONObject(i).getString("passwd");
            try {
                passwd=Base64.getEncoder().encodeToString(passwd.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String subUrl=localIp+":"+port+":"+protocol+":"+method+":"+obfs+":"+passwd;

            String endStr="";
            try {
                endStr="remarks="+Base64.getEncoder().encodeToString(("节点"+String.valueOf(i)).getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            subUrl=subUrl+"/?"+endStr;
            try {
                subUrl=Base64.getEncoder().encodeToString(subUrl.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //最终格式为ssr://base64【host:port:ptotocol:method:obfs:base64(passwd)/?param】
            //param格式为obfsparam=base64(param)&protoparam=base64(param)&remarks=base64(remarks&g)roup=base64(group)&udpport=0&uot=0
            //param根据需要添加相应参数,/?之前为必须项,之后为可选项
            subUrl="ssr://"+subUrl+" ";

            ssrlist=ssrlist+subUrl;

        }
        //encode final context
        try {
            ssrlist=Base64.getEncoder().encodeToString(ssrlist.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ssrlist;
    }

    /**
     *
     * @param path 文件地址
     * @return 文件内的字符串
     * @throws IOException 错误
     */
    public static String ReadFile(String path) throws IOException {

        File file = new File(path);

        if(!file.exists()||file.isDirectory()) {
            throw new FileNotFoundException();
        }

        StringBuffer sb = new StringBuffer();
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                sb.append(lineTxt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取本机的IP
     * @return Ip地址
     */
    public static String getLocalHostIP() {
        String ip;
        try {
            /**返回本地主机。*/
            InetAddress addr = InetAddress.getLocalHost();
            /**返回 IP 地址字符串（以文本表现形式）*/
            ip = addr.getHostAddress();
        } catch(Exception ex) {
            ip = "";
        }

        return ip;
    }
}
