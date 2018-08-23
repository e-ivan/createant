package com.qnyy.re.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.*;


public class SHA1Util {

	public static final String SIGN_KEY_NAME = "sign";
	private static final Logger LOG = LoggerFactory.getLogger(SHA1Util.class);		
	
	public static String Encrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;

		try {
			byte[] bt = strSrc.getBytes("UTF-8");
			//LOG.info("bt=" + Arrays.toString(bt));//字节数组打印
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (Exception ex) { //NoSuchAlgorithmException
			LOG.error("SHA1Util.Encrypt fail, ex=" + ex.getMessage());	
			//LoggerUtils.error(ex, LOG);
			return null;
		}
		
		LOG.debug("SHA1 Encrypt strSrc=" + strSrc + ", strDes=" + strDes);
		return strDes;
	}

	public static String bytes2Hex(byte[] bytes) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bytes.length; i++) {
			tmp = (Integer.toHexString(bytes[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	//比较tokenKey
	public static boolean compareTokenkey(Map<String,String> params,String token, String tokenKey) {
		String strEncrpyt = signTopRequest(params, token);
		LOG.info("input tokenKey=" + tokenKey + ", strEncrpyt=" + strEncrpyt);
		return StringUtils.equals(strEncrpyt, tokenKey);
	}

	//检查签名合法
	public static boolean checkSign(Map<String,String> params, String tokenKey) {
		return compareTokenkey(params,null,tokenKey);
	}
	//排序和组装字符串
	public static String  sortIdentityHashMap(IdentityHashMap<String,String> map, String secret){
		LOG.debug("sortIdentityHashMap...");
		StringBuilder strBuild = new StringBuilder();
		List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
		System.out.println("infoIds = " + infoIds);
		/*
		//排序前
		for(int i = 0; i < infoIds.size(); i++) {
			//String id = infoIds.get(i).toString();
			LOG.info("key="+infoIds.get(i).getKey()+",value="+infoIds.get(i).getValue());
		}*/
		
		//排序
		infoIds.sort((map1, map2) -> {
            if (StringUtils.equals(map1.getKey(), map2.getKey())) {
                return (map1.getValue()).compareTo(map2.getValue());
            } else {
                return (map1.getKey()).compareTo(map2.getKey());
            }
        });
		
		//排序后
		for (Map.Entry<String, String> infoId : infoIds) {
			String key = infoId.getKey();
			String value = infoId.getValue();
			//LOG.info("key="+key+",value="+value);
			if (!StringUtils.equals(value, "") && !StringUtils.equals(SIGN_KEY_NAME, key)) {
				strBuild.append(key).append(value);
			}
		}	
		if (StringUtils.isNoneBlank(secret)) {
			strBuild.append(secret);
		}
		LOG.info("strBuild=" + strBuild);		
		return strBuild.toString();
	}
	
	@SuppressWarnings("unused")
	private static String sortHashMap(Map<String,String> params, String secret){
		LOG.debug("sortHashMap...");
		StringBuilder sb = new StringBuilder();
		//1.参数排序
		Map<String,String> mapsort = new TreeMap<>(String::compareTo);
		mapsort.putAll(params);
		
		//2.组装参数
        for (String key : mapsort.keySet()){
			String value = mapsort.get(key);
        	if(!StringUtils.equals(value, "") && !StringUtils.equals(SIGN_KEY_NAME, key)) {
	            sb.append(key).append(value);
	            LOG.debug("key="+key+",value="+value);
        	}
        }
		sb.append(secret);
		LOG.debug("sb=" + sb);
		
		return sb.toString();
	}
	
	public static String signTopRequest(Map<String,String> params, String secret) {
		LOG.debug("signTopRequest...");
		if(null == params) {
			return null;
		}
	
		//1.参数排序和组装参数
		IdentityHashMap<String,String> mapsort = new IdentityHashMap<>();
		mapsort.putAll(params);
		String sb = sortIdentityHashMap(mapsort, secret);		
		
		//2.SHA1加密
		return Encrypt(sb);
	}
	
//	public static void main(String[] args) {
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("dd", "dd1");
//		params.put("hh", "hh1");
//		params.put("rr", "rr1");
//		params.put("oo", "oo1");
//		params.put("aa", "aa1");
//		signTopRequest(params, "token");
//	}

}
