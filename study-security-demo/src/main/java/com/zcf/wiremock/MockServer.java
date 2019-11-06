package com.zcf.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * 我已经在类上直接导入wiremock的静态方法了，所以这里可以直接调用静态方法
 * import static com.github.tomakehurst.wiremock.client.WireMock.*;
 * wiremock client端。
 */
public class MockServer {
    public static void main(String[] args) throws IOException {
        configureFor(8082); //指定wiremock服务端的地址，本机就写端口
        removeAllMappings(); //清除以前的配置，把新的配置注入进去
        mock("/order/1","01");
        //mock("/order/2","02");
    }

    private static void mock(String url, String file) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/"+file+".txt"); //文件路径
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8").toArray(),"\n"); //读取文件
        stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(content).withStatus(200)));
    }
}
