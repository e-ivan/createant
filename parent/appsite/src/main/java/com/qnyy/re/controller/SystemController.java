package com.qnyy.re.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.VersionUpgrade;
import com.qnyy.re.base.enums.AppTypeEnum;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.mapper.LoginInfoMapper;
import com.qnyy.re.base.query.UserMsgLogQueryObject;
import com.qnyy.re.base.util.FileUploadUtils;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.UploadFileVO;
import com.qnyy.re.business.entity.SysPromoReap;
import com.qnyy.re.business.util.CreateantRequestUtil;
import com.qnyy.re.business.util.WeiXinMpRequestUtil;
import com.qnyy.re.business.vo.param.CreateInformantVO;
import com.qnyy.re.business.vo.param.SaveFeedbackVO;
import com.qnyy.re.util.SignUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统通用控制器
 * Created by E_Iva on 2017.12.19.0019.
 */
@RestController
@RequestMapping("system")
public class SystemController extends BaseController {
    private static Map<String, CharSequence> cacheMap = new ConcurrentHashMap<>();
    private static Map<String, LinkedList<CharSequence>> cacheMapHistory = new ConcurrentHashMap<>();

    /**
     * 查询消息日志
     */
    @RequestMapping(value = "queryMsgLog", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("查询消息日志")
    public Response queryMsgLog(UserMsgLogQueryObject qo) {
        qo.setTo(UserContext.getUserId());
        return new ObjectResponse<>(userMsgLogService.queryUserMsgLog(qo));
    }

    /**
     * 系统广播
     */
    @RequestMapping(value = "broadcast", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin
    @ApiDocument("系统广播")
    public Response broadcast() {
        return new ObjectResponse<>(sysBroadcastService.queryBroadcast());
    }

    /**
     * 保存反馈信息
     */
    @RequestMapping(value = "feedback", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("保存反馈信息")
    public Response feedback(HttpServletRequest request, SaveFeedbackVO vo) {
        vo.setUid(UserContext.getUserId());
        vo.setIp(getIpAddr(request));
        userFeedbackService.saveFeedback(vo);
        return new Response("反馈已提交");
    }

    /**
     * 用户举报
     */
    @RequestMapping(value = "informUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("用户举报")
    public Response informUser(HttpServletRequest request, CreateInformantVO vo) {
        vo.setApplierId(UserContext.getUserId());
        vo.setIp(getIpAddr(request));
        informantCenterService.createInform(vo);
        return new Response("已举报");
    }

    /**
     * 填写推荐人
     */
    @RequestMapping(value = "buildRelation", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("填写推荐人")
    public Response buildRelation(String promoCode) {
        UserInfo userInfo = userInfoService.selectUserByPromoCode(promoCode);
        userClassRelationService.createRelation(userInfo.getUid(), UserContext.getUserId());
        return new Response("感谢参与");
    }

    /**
     * 收益比例
     */
    @RequestMapping(value = "reapRatio", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiDocument("收益比例")
    public Response reapRatio() {
        SysPromoReap reapRatio = userClassRelationService.getReapRatio();
        Map<String, Object> map = new HashMap<>();
        map.put("reapRatio", reapRatio);
        map.put("promoCode", userInfoService.getByUid(UserContext.getUserId()).getPromoCode());
        return new ObjectResponse<>(map);
    }

    /**
     * 获取用户推广码
     */
    @RequestMapping(value = "getPromoCode", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("获取用户推广码")
    public Response getPromoCode(Long uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("promoCode", userInfoService.getByUid(uid).getPromoCode());
        return new ObjectResponse<>(map);
    }

    /**
     * 获取变量
     */
    @RequestMapping(value = "variable", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin
    @ApiDocument("获取变量")
    public Response variable(Byte appType, Integer versionCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("reMomentFeeRatio", SystemConstUtil.reMomentFeeRatio);
        map.put("userGeneralizeUrl", SystemConstUtil.userGeneralizeUrl);
        map.put("appUpgrade", appVersionService.selectLatestVersion(appType == null ? AppTypeEnum.IOS_PHONE.getCode() : appType, versionCode == null ? 0 : versionCode));
        return new ObjectResponse<>(map);
    }

    /**
     * 获取app升级
     */
    @RequestMapping(value = "appUpgrade", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin
    @ApiDocument("获取app升级")
    public Response appVersion(Byte appType, Integer versionCode) {
        return new ObjectResponse<>(appVersionService.selectLatestVersion(appType == null ? AppTypeEnum.IOS_PHONE.getCode() : appType, versionCode == null ? 0 : versionCode));
    }

    /**
     * 上传软件
     */
    @RequestMapping(value = "uploadApp", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin
    @ApiDocument("上传软件")
    public Response uploadApp(MultipartFile file, VersionUpgrade version) {
        appVersionService.saveVersion(file, version);
        return new Response("上传成功");
    }

    /**
     * 上传文件
     */
    @RequestMapping(value = "uploadFile", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("上传文件")
    public Response uploadFile(UploadFileVO vo) throws Exception {
        vo.setUid(UserContext.getUserId());
        Map<String, List> map = new HashMap<>();
        MultipartFile[] multipartFiles;
        if (vo.getFile() == null) {
            multipartFiles = new MultipartFile[]{FileUploadUtils.base64ToMultipart(vo.getBase64())};
        } else {
            multipartFiles = vo.getFile();
        }
        List<UploadFile> files = fileService.saveFile(vo.getFileType(), vo.getFilePurpose(), vo.getUid(), vo.getObjectId(), multipartFiles);
        map.put("urlList", files);
        return new ObjectResponse<>(map, "上传成功");
    }

    /**
     * 上传文件到微信服务器
     */
    @RequestMapping(value = "uploadFile2WxService", method = {RequestMethod.POST, RequestMethod.GET})
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("上传文件到微信服务器")
    public Response uploadFile2WxService(MultipartFile file) {
        File f = null;
        try {
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            f.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ret = "{\"url\":\"" + WeiXinMpRequestUtil.addMaterial(f) + "\"}";
        return new ObjectResponse<>(JSON.parseObject(ret));
    }

    /**
     * 获取微信公众号配置对象
     */
    @RequestMapping(value = "getWxMpJSApiConfig", method = RequestMethod.GET)
    @UnRequiredLogin
    @ApiDocument("获取微信公众号配置对象")
    public Response getWxMpJSApiConfig(String url) {
        Map<String, String> map = SignUtil.signWxJSApiParam(WeiXinMpRequestUtil.getJsapiTicket(), url);
        map.put("appId", WeiXinMpRequestUtil.appId);
        return new ObjectResponse<>(map);
    }

    @Resource
    private LoginInfoMapper loginInfoMapper;

    /**
     * 临时创蚁用户id绑定
     */
    @RequestMapping(value = "createantUidBind", method = RequestMethod.GET)
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("临时创蚁用户id绑定")
    public Response createantUidBind(Integer count, String password) {
        if (!StringUtils.equals(password, "ivan")) {
            throw new BusinessException(CommonErrorResultEnum.MCH_NO_PERMISSION);
        }
        List<LoginInfo> loginInfos = loginInfoMapper.query(count);
        List<String> tokens = loginInfos.stream().map(LoginInfo::getCreateantToken).collect(Collectors.toList());
        if (tokens.isEmpty()) {
            return new Response("无可更新用户");
        }
        Map<String, Long> map = CreateantRequestUtil.queryUserIdByToken(tokens);
        map.forEach(loginInfoMapper::updateUserCreateantUid);
        return new Response("更新成功" + map.size() + "条内容");
    }

    @RequestMapping(value = "putData")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("保存数据")
    public Response putData(String key, String value, boolean replace) {
        CharSequence cacheValue = cacheMap.get(key);
        if (key.matches("[^.]\\S+(\\.)\\S+")) {
            putValueByKey(key, value, replace);
        }
        //保存原来的值到历史
        if (cacheValue != null) {
            LinkedList<CharSequence> values = cacheMapHistory.computeIfAbsent(key, s -> new LinkedList<>());
            if (!StringUtils.equals(values.peek(), value)) {
                values.push(cacheValue);
            }
            if (values.size() > 10) {
                values.pollLast();
            }
        }
        cacheMap.put(key, value);
        return new Response("保存成功");
    }

    private static final Pattern KEY_INDEX_PATTERN = Pattern.compile("(\\S+)\\[(\\d+)]$");

    private static JSONObject putValueByKey(String key, String value, boolean replace) {
        String[] nameSplit = key.trim().split("\\.");
        LinkedList<String> collect = Stream.of(nameSplit).collect(Collectors.toCollection(LinkedList::new));
        String putKey = collect.pollLast();
        String first = collect.pollFirst();
        if (first != null && putKey != null) {
            JSONObject source = getJsonWithKey(null, first);
            if (source != null) {
                JSONObject target = getJsonWithKey(source, collect);
                KeyIndex keyIndex = new KeyIndex(putKey);
                Object putObj = target.get(keyIndex.getKey());
                Object valueObj = parseValue(value);
                if ((!replace || keyIndex.isArr) && putObj instanceof JSONArray) {
                    JSONArray array = (JSONArray) putObj;
                    if (keyIndex.isArr) {
                        //这里是替换
                        array.set(keyIndex.getIndex(), valueObj);
                    } else {
                        if (valueObj instanceof Collection) {
                            //集合处理
                            array.addAll((Collection<?>) valueObj);
                        } else {
                            array.add(valueObj);
                        }
                    }
                } else {
                    //否则直接替换
                    target.put(keyIndex.getKey(), valueObj);
                }
                cacheMap.put(first, JSON.toJSONString(source));
                return source;
            }
        }
        return null;
    }

    private static Object parseValue(String value) {
        if (value.matches(jsonRegex)) {
            return JSON.parseObject(value);
        } else if (value.matches(arrRegex)) {
            return JSON.parseArray(value);
        }
        return value;
    }


    @Getter
    @Setter
    private static class KeyIndex {
        KeyIndex(String sourceKey) {
            boolean arrKey = sourceKey.matches(arrKeyRegex);
            if (arrKey) {
                Matcher m = KEY_INDEX_PATTERN.matcher(sourceKey);
                if (m.find()) {
                    this.key = m.group(1);
                    this.index = Integer.parseInt(m.group(2));
                    this.isArr = true;
                }
            } else {
                this.key = sourceKey;
            }
        }

        private String key;
        private Integer index;
        private boolean isArr;
    }

    private static JSONObject getJsonWithKey(JSONObject json, LinkedList<String> keys) {
        String key = keys.pollFirst();
        if (key != null) {
            JSONObject j = getJsonWithKey(json, key);
            if (j == null) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "不存在");
            }
            return getJsonWithKey(j, keys);
        }
        return json;
    }

    private static String arrKeyRegex = "\\S+\\[\\d+]$";
    private static String arrRegex = "\\s*\\[\\S+]\\s*";
    private static String jsonRegex = "\\s*\\{\\S+}\\s*";

    private static JSONObject getJsonWithKey(JSONObject json, String key) {
        //查看name是否以[d]结尾,如果是说明是数组,数组获取索引所在的对象
        KeyIndex keyIndex = new KeyIndex(key);
        if (keyIndex.isArr) {
            JSONArray array;
            if (json == null) {
                array = JSON.parseArray(String.valueOf(cacheMap.get(keyIndex.getKey())));
            } else {
                array = json.getJSONArray(keyIndex.getKey());
            }
            return array.getJSONObject(keyIndex.getIndex());
        }
        if (json == null) {
            try {
                return JSONObject.parseObject(String.valueOf(cacheMap.get(key)));
            } catch (JSONException e) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "不是json对象");
            }
        }
        try {
            return json.getJSONObject(key);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "为JsonArray,请指定下标");
        }
    }

    @RequestMapping(value = "getData", produces = "application/json;charset=UTF-8")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("获取数据")
    public Object getData(String key, Integer type) {
        Object o = StringUtils.equals("ALL_DATA", key) ? cacheMap : cacheMap.get(key);
        return type != null && type == 0 ? o : new ObjectResponse<>(o);
    }

    @RequestMapping(value = "clearData")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("清除数据")
    public Object clearData(String key) {
        if (StringUtils.equals("ALL_DATA", key)) {
            cacheMap.clear();
        } else {
            cacheMap.remove(key);
        }
        return new Response("清除成功");
    }
}
