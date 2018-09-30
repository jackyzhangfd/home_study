package com.autobusi.home.study.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.aip.ocr.AipOcr;

@Component
public class BaiduApi {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${baidu.ai.img.app.id}")
	private String AI_IMG_APP_ID;
	
	@Value("${baidu.ai.img.api.key}")
    private String AI_IMG_API_KEY;
	
	@Value("${baidu.ai.img.secret.key}")
    private String AI_IMG_SECRET_KEY;
    
	public JSONObject extractTextFromPicture(byte[] imageBytes){
		
		//this.logger.debug("app id: " + this.AI_IMG_APP_ID );
		
    	// 初始化一个AipOcr
        AipOcr client = new AipOcr(AI_IMG_APP_ID, AI_IMG_API_KEY, AI_IMG_SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        
        /*
        //get image bytes
        byte[] imageBytes;
        File image = new File(filePath);
        try {
			InputStream fs = new FileInputStream(image);
			imageBytes = IOUtils.toByteArray(fs);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}*/
        
        // 调用接口
        JSONObject res = client.basicGeneral(imageBytes, new HashMap<String, String>());
        return res;
    }
}
