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
import java.util.concurrent.ConcurrentSkipListMap;
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
    /**
     * key[index]
     */
    private static final Pattern KEY_INDEX_PATTERN = Pattern.compile("(\\w+)\\[(\\d+)]$");
    /**
     * 正则表达式存在字符
     */
    private static final Pattern REGEX_PATTERN = Pattern.compile("[\\\\ !$%^&*()+=|{}\\[\\].<>/?]|\n|\r|\t");
    /**
     * 链表结构
     */
    private static final String LINK_KEY_REGEX = "[^.]\\w+(\\[\\d+])?(\\.\\w+(\\[\\d+])?)+";
    /**
     * 数组key
     */
    private static final String ARR_KEY_REGEX = "\\w+\\[\\d+]$";
    /**
     * 数组
     */
    private static final String ARR_REGEX = "\\s*\\[.*]\\s*";
    /**
     * json
     */
    private static final String JSON_REGEX = "\\s*\\{\\S+}\\s*";
    private static final String ALL_DATA = "ALL_DATA";
    private static final String ALL_KEY = "ALL_KEY";
    private static final String MAX_HISTORY_ROW = "MAX_HISTORY_ROW";
    private static Map<String, CharSequence> cacheMap = new ConcurrentSkipListMap<>();
    private static Map<String, LinkedList<CharSequence>> cacheMapHistory = new ConcurrentSkipListMap<>();

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
        String[] keyValue = disposeKeyValue(key, value, replace);
        final String putKey = keyValue[0];
        final String putValue = keyValue[1];
        saveCacheHistory(putKey, putValue);
        cacheMap.put(putKey, putValue);
        return new Response("保存成功");
    }

    private static String compatibilityKey(String key) {
        if (StringUtils.isNotBlank(key)) {
            return key.replaceAll("【", "[").replaceAll("】", "]");
        }
        return key;
    }

    private static String[] disposeKeyValue(final String key, final String value, final boolean replace) {
        if (key.matches(LINK_KEY_REGEX)) {
            String[] keyValue = saveLinkValue(key, value, replace);
            if (keyValue != null) {
                return new String[]{keyValue[0], keyValue[1]};
            } else {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, "保存失败");
            }
        }
        boolean isRegex = REGEX_PATTERN.matcher(key).find();
        if (isRegex || StringUtils.containsAny(key, ALL_DATA, ALL_KEY)) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "不能存在特殊字符");
        }
        return new String[]{key, value};
    }

    private static void saveCacheHistory(String key, String value) {
        CharSequence cacheValue = cacheMap.get(key);
        //保存原来的值到历史
        if (cacheValue != null) {
            LinkedList<CharSequence> values = cacheMapHistory.computeIfAbsent(key, s -> new LinkedList<>());
            if (!StringUtils.equals(values.peek(), value)) {
                values.push(cacheValue);
            }
            clearValuesOfSize(values, getMaxHistoryRow());
        }
    }

    private static void clearValuesOfSize(LinkedList<?> values, final int size) {
        if (values.size() > size) {
            values.pollLast();
            clearValuesOfSize(values, size);
        }
    }

    private static int getMaxHistoryRow() {
        CharSequence row = cacheMap.getOrDefault(MAX_HISTORY_ROW, "10");
        try {
            return Integer.parseInt(String.valueOf(row));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    private static CharSequence rollbackCache(String key, int index) {
        LinkedList<CharSequence> values = cacheMapHistory.computeIfAbsent(key, s -> new LinkedList<>());
        CharSequence historyValue = values.get(index);
        if (StringUtils.isNotBlank(historyValue)) {
            cacheMap.put(key, historyValue);
        }
        return historyValue;
    }

    private static String[] saveLinkValue(final String key, final String value, final boolean replace) {
        String[] nameSplit = key.trim().split("\\.");
        LinkedList<String> collect = Stream.of(nameSplit).collect(Collectors.toCollection(LinkedList::new));
        String putKey = collect.pollLast();
        String first = collect.pollFirst();
        if (first != null && putKey != null) {
            JSONObject source = getJsonWithKey(null, first);
            if (source == null) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST, first + "不存在");
            }
            JSONObject target = getJsonWithKey(source, collect);
            KeyIndex keyIndex = new KeyIndex(putKey);
            Object putObj = target.get(keyIndex.getKey());
            Object valueObj = parseValue(value);
            if (!replace && keyIndex.isArr && !(putObj instanceof JSONArray)) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, keyIndex.getKey() + "不是数组");
            } else if ((!replace || keyIndex.isArr) && putObj instanceof JSONArray) {
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
            return new String[]{first, JSON.toJSONString(source)};
        }
        return null;
    }

    private static Object parseValue(String value) {
        if (value != null) {
            if (value.matches(JSON_REGEX)) {
                return JSON.parseObject(value);
            } else if (value.matches(ARR_REGEX)) {
                return JSON.parseArray(value);
            }
        }
        return value;
    }


    @Getter
    @Setter
    private static class KeyIndex {
        KeyIndex(String sourceKey) {
            boolean arrKey = sourceKey.matches(ARR_KEY_REGEX);
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
        private int index;
        private boolean isArr;
    }

    private static JSONObject getJsonWithKey(JSONObject json, LinkedList<String> keys) {
        String key = keys.pollFirst();
        if (key != null) {
            JSONObject j = getJsonWithKey(json, key);
            if (j == null) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST, key + "不存在");
            }
            return getJsonWithKey(j, keys);
        }
        return json;
    }

    private static JSONObject getJsonWithKey(JSONObject json, String key) {
        //查看name是否以[d]结尾,如果是说明是数组,数组获取索引所在的对象
        KeyIndex keyIndex = new KeyIndex(key);
        if (keyIndex.isArr) {
            JSONArray array;
            String keyIndexKey = keyIndex.getKey();
            try {
                if (json == null) {
                    array = JSON.parseArray(String.valueOf(cacheMap.get(keyIndexKey)));
                } else {
                    array = json.getJSONArray(keyIndexKey);
                }
            } catch (ClassCastException e) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, keyIndexKey + "不是JsonArray");
            }
            try {
                return array.getJSONObject(keyIndex.getIndex());
            } catch (ClassCastException e) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, keyIndexKey + "不是Json对象");
            }
        }
        if (json == null) {
            try {
                return JSONObject.parseObject(String.valueOf(cacheMap.get(key)));
            } catch (JSONException e) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "不是Json对象");
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
        key = compatibilityKey(key);
        Object retValue = null;
        if (key.matches(LINK_KEY_REGEX)) {
            String[] nameSplit = key.trim().split("\\.");
            LinkedList<String> collect = Stream.of(nameSplit).collect(Collectors.toCollection(LinkedList::new));
            String endKey = collect.pollLast();
            if (endKey != null) {
                KeyIndex keyIndex = new KeyIndex(endKey);
                JSONObject json = getJsonWithKey(null, collect);
                Object o = json.get(keyIndex.getKey());
                if (keyIndex.isArr) {
                    if (o instanceof JSONArray) {
                        retValue = ((JSONArray) o).get(keyIndex.getIndex());
                    } else {
                        throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, keyIndex.getKey() + "不是数组");
                    }
                } else {
                    retValue = o;
                }
            }
        } else {
            if (REGEX_PATTERN.matcher(key).find()) {
                String regex = key;
                Set<String> keySet = cacheMap.keySet();
                retValue = keySet.stream().filter(k -> k.matches(regex)).collect(Collectors.toMap(o -> o, o -> parseValue((String) cacheMap.get(o))));
            } else {
                if (StringUtils.equals(ALL_DATA, key)) {
                    retValue = cacheMap;
                } else if (StringUtils.equals(ALL_KEY, key)) {
                    retValue = cacheMap.keySet();
                } else {
                    retValue = parseValue((String) cacheMap.get(key));
                }
            }
        }
        return type != null && type == 0 ? retValue : new ObjectResponse<>(retValue);
    }

    @RequestMapping(value = "clearData")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("清除数据")
    public Object clearData(String key) {
        key = compatibilityKey(key);
        Map<String, CharSequence> remove = new HashMap<>(10);
        if (key.matches(LINK_KEY_REGEX)) {
            String[] nameSplit = key.trim().split("\\.");
            LinkedList<String> collect = Stream.of(nameSplit).collect(Collectors.toCollection(LinkedList::new));
            String endKey = collect.pollLast();
            String first = collect.pollFirst();
            if (first != null && endKey != null) {
                JSONObject source = getJsonWithKey(null, first);
                JSONObject target = getJsonWithKey(source, collect);
                KeyIndex keyIndex = new KeyIndex(endKey);
                Object removeValue = null;
                if (keyIndex.isArr) {
                    Object o = target.get(keyIndex.getKey());
                    if (o instanceof JSONArray) {
                        JSONArray array = (JSONArray) o;
                        removeValue = array.remove(keyIndex.index);
                    }
                } else {
                    removeValue = target.remove(keyIndex.getKey());
                }
                String value = JSON.toJSONString(source);
                saveCacheHistory(first, value);
                cacheMap.put(first, value);
                remove.put(key, JSON.toJSONString(removeValue));
            }
        } else {
            if (REGEX_PATTERN.matcher(key).find()) {
                String regex = key;
                Set<String> keySet = cacheMap.keySet();
                remove.putAll(keySet.stream().filter(k -> k.matches(regex)).collect(Collectors.toMap(o -> o, o -> {
                    saveCacheHistory(o, null);
                    return cacheMap.remove(o);
                })));
            } else {
                if (StringUtils.equals(ALL_DATA, key)) {
                    remove.putAll(cacheMap);
                    cacheMap.clear();
                } else {
                    remove.put(key, cacheMap.remove(key));
                }
            }
        }
        return new ObjectResponse<>(remove, "清除成功");
    }

    @RequestMapping(value = "historyData", produces = "application/json;charset=UTF-8")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("历史数据")
    public Object historyData(String key, Integer index,boolean format) {
        key = compatibilityKey(key);
        Object ret = null;
        if (index != null) {
            //撤销数据
            if (StringUtils.isNotBlank(key)) {
                ret = rollbackCache(key, index);
            }
        } else {
            if (StringUtils.isNotBlank(key)) {
                LinkedList<CharSequence> list = cacheMapHistory.get(key);
                if (format && list != null) {
                    ret = list.stream().map(s -> parseValue((String) s)).collect(Collectors.toCollection(LinkedList::new));
                } else {
                    ret = list;
                }
            } else {
                ret = cacheMapHistory;
            }
        }
        return ret;
    }
}
