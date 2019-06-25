package com.qnyy.re.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.VersionUpgrade;
import com.qnyy.re.base.enums.AppTypeEnum;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.enums.DataAsyncServerEnum;
import com.qnyy.re.base.enums.MethodUrlEnum;
import com.qnyy.re.base.mapper.LoginInfoMapper;
import com.qnyy.re.base.query.UserMsgLogQueryObject;
import com.qnyy.re.base.util.FileUploadUtils;
import com.qnyy.re.base.util.HttpClientUtils;
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
import com.qnyy.re.util.DataUtil;
import com.qnyy.re.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private static final String LINK_KEY_REGEX = "\\w+(\\[\\d+])?(\\.\\w+(\\[\\d+])?)+";
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
    private static final String JSON_REGEX = "\\s*\\{.*}\\s*";
    private static final String ALL_DATA = "ALL_DATA";
    private static final String ALL_KEY = "ALL_KEY";
    private static final String ALL_CONFIG = "ALL_CONFIG";
    private static final String MAX_HISTORY_ROW = "MAX_HISTORY_ROW";
    private static final String NO_RECORD_HISTORY_KEY = "NO_RECORD_HISTORY_KEY";
    private static Map<String, CharSequence> cacheMap = new ConcurrentSkipListMap<>();
    private static Map<String, LinkedList<CharSequence>> cacheMapHistory = new ConcurrentSkipListMap<>();
    private static List<String> noRecordKeys = Collections.synchronizedList(new ArrayList<>());
    private static int maxRow = 10;

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
    public Response putData(String key, String value, boolean replace, boolean exact) {
        key = DataUtil.compatibilityKey(key);
        if (StringUtils.isBlank(key)) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "key不能为空");
        }
        String msg = DataUtil.saveData(key, value, replace);
        if (exact) {
            DataAsyncServerEnum.SLAVE_SERVER.asyncOperate(MethodUrlEnum.PUT_DATA, key, value, "0", replace);
        }
        return new Response(msg);
    }

    @RequestMapping(value = "getData", produces = "application/json;charset=UTF-8")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("获取数据")
    public Object getData(String key, Integer type) {
        key = DataUtil.compatibilityKey(key);
        if (StringUtils.isBlank(key)) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "key不能为空");
        }
        Object retValue = DataUtil.getData(key);
        return type != null && type == 0 ? retValue : new ObjectResponse<>(retValue);
    }

    @RequestMapping(value = "clearData")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("清除数据")
    public Object clearData(String key, boolean exact) {
        key = DataUtil.compatibilityKey(key);
        Map<String, CharSequence> remove = DataUtil.removeData(key);
        if (exact) {
            DataAsyncServerEnum.SLAVE_SERVER.asyncOperate(MethodUrlEnum.REMOVE_DATA, key, "", "0", false);
        }
        return new ObjectResponse<>(remove, "清除成功");
    }

    @RequestMapping(value = "historyData", produces = "application/json;charset=UTF-8")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("历史数据")
    public Object historyData(String key, Integer index, boolean format) {
        key = DataUtil.compatibilityKey(key);
        Object ret = null;
        if (index != null) {
            //撤销数据
            if (StringUtils.isNotBlank(key)) {
                ret = DataUtil.rollbackCache(key, index);
            }
        } else {
            ret = DataUtil.getHistory(key, format);
        }
        return ret;
    }

    @RequestMapping(value = "pullData", produces = "application/json;charset=UTF-8")
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("拉取数据")
    public Object pullData(String url,String key,String type) {
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("type", type);
        HttpUriRequest post = HttpClientUtils.getRequestMethod(map, url, "post");
        CloseableHttpClient client = HttpClientUtils.getHttpClient();
        try (CloseableHttpResponse response = client.execute(post)) {
            String ret = EntityUtils.toString(response.getEntity(),"UTF-8");
            if (StringUtils.isNotBlank(ret)) {
                JSONObject jsonObject = JSON.parseObject(ret);
                jsonObject.forEach((k, v) -> DataUtil.saveData(k, v.toString(), true));
                return new ObjectResponse<>(jsonObject.keySet(), "拉取数据:" + jsonObject.size() + "条");
            }
            return new ObjectResponse<>(ret, "请求服务无数据");
        } catch (IOException e) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }
}
