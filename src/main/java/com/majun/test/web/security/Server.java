package com.majun.test.web.security;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/security")
public class Server {

	@RequestMapping("getOrder")
	@ResponseBody
	public String getOrder(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//获取客户端参数
		Map<String,String> param = getPara(request);
		System.out.println("服务端获取客户端所发送的请求参数："+param);
		//获取到加密摘要
		String encryptStr = param.get("sign");
		//解密摘要
		String clientSign = RSA.decrypt(encryptStr,"/Users/majun/git/webtest/src/main/java/PrivateKey");
		System.out.println("服务端获取客户端的摘要为："+clientSign);
		//服务端对参数生成自己的摘要
		MD5 md5 = new MD5();
		param.remove("sign");
		String serverSign = md5.encodeAsHex(Client.getContent(param, true));
		System.out.println("服务端自己生成的摘要为："+serverSign);
		System.out.println("与客户端的摘要比对结果："+clientSign.equals(serverSign));
		
		if(clientSign.equals(serverSign)){
			//返回结果
			Map<String,String> map = new HashMap<String,String>();
			map.put("orderid", "1101");
			map.put("amount", "100");
			map.put("partnerid", "1");
			//生成待摘要字符串
			String waitSignStr = Client.getContent(map, true);
			System.out.println("客户端待摘要字符串："+waitSignStr);
			//生成摘要
			String sign = md5.encodeAsHex(waitSignStr);
			System.out.println("生成摘要为："+sign);
			//摘要加密
			String encryptStrServer = RSA.encrypt(sign,"/Users/majun/git/webtest/src/main/java/PublicKey");
			System.out.println("加密后的摘要："+encryptStrServer);
			map.put("sign", encryptStrServer);
			Gson gson = new Gson();
			return gson.toJson(map);
		}
		
		
		return null;
	}
	
	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	protected Map<String, String> getPara(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			Enumeration<String> em = req.getParameterNames();
			String name = null;
			while(em.hasMoreElements()){
				name = em.nextElement();
				map.put(name, req.getParameter(name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
