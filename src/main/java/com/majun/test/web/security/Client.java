package com.majun.test.web.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.majun.test.web.util.HttpConnectionManager;

public class Client {

	public static void main(String[] args) throws Exception {
		
		String url = "http://localhost:8080/webtest/security/getOrder.jspa";
		
		//构造请求参数
		Map<String,String> map = new HashMap<String,String>();
		map.put("orderid", "1101");
		map.put("paydate", System.currentTimeMillis()+"");
		map.put("partnerid", "1");
		//生成待摘要字符串
		String waitSignStr = getContent(map, true);
		System.out.println("客户端待摘要字符串："+waitSignStr);
		//生成摘要
		MD5 md5 = new MD5();
		String sign = md5.encodeAsHex(waitSignStr);
		System.out.println("生成摘要为："+sign);
		//摘要加密
		String encryptStr = RSA.encrypt(sign);
		System.out.println("加密后的摘要："+encryptStr);
		
		//发送请求
		map.put("sign", encryptStr);
		HttpConnectionManager http = new HttpConnectionManager();
		System.out.println("发送请求");
		List<String> results = http.doHttpPost(url, map);
		
		//获取服务端返回参数
		String json = results.get(0);
		System.out.println("获取服务端返回参数:"+json);
		Gson gson = new Gson();
		Map<String,String> resultMap = gson.fromJson(json, HashMap.class);
		//获取到加密摘要
		String encryptStrServer = resultMap.get("sign");
		System.out.println(encryptStrServer);
		//解密摘要
		String serverSign = RSA.decrypt(encryptStrServer);
		System.out.println("客户端获取客户端的摘要为："+serverSign);
		//客户端对参数生成自己的摘要
		resultMap.remove("sign");
		String clientSign = md5.encodeAsHex(Client.getContent(resultMap, true));
		System.out.println("客户端自己生成的摘要为："+serverSign);
		System.out.println("与服务端的摘要比对结果："+clientSign.equals(serverSign));
	}

	/**
     * 获取签名字符串
     * @param params
     * @param encodeFlag=true  url 编码处理
     * @return
     */
	public static String getContent(Map<String, String> params, boolean encodeFlag) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer prestr = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (value == null || value.trim().length() == 0) {
				continue;
			}
			prestr.append("&");
			prestr.append(key);
			prestr.append("=");
			if(encodeFlag){
				try {
					prestr.append(URLEncoder.encode(value, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
				prestr.append(value);
			}
		}
		return prestr.substring(1);
	}
}
