/**
 * Software License Declaration.
 * <p>
 * wandaph.com, Co,. Ltd.
 * Copyright ? 2017 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to wandaph contracting agent or authorized programmer only.
 * This source code is written and edited by wandaph Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to wandaph Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise wandaph will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of wandaph. If Any problem cannot be solved in the
 * procedure of programming should be feedback to wandaph Co,. Ltd Inc in time, Thank you!
 */
package com.qnyy.re.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.qnyy.re.util.Constant.*;


/**
 * @author lianghaifeng
 * @version Id: DataUtil.java, v 0.1 2019.6.25 025 9:37 lianghaifeng Exp $$
 */
public class DataUtil {
    private static Map<String, CharSequence> cacheMap = new ConcurrentSkipListMap<>();
    private static Map<String, LinkedList<CharSequence>> cacheMapHistory = new ConcurrentSkipListMap<>();
    private static List<String> noRecordKeys = Collections.synchronizedList(new ArrayList<>());
    private static int maxRow = 10;

    public static String saveData(String key, String value, boolean replace) {
        if (StringUtils.containsAny(key, NO_RECORD_HISTORY_KEY, MAX_HISTORY_ROW)) {
            disposeSysConfig(key, value);
            return "配置已更新";
        }
        String[] keyValue = disposeKeyValue(key, value, replace);
        final String putKey = keyValue[0];
        final String putValue = keyValue[1];
        saveCacheHistory(putKey, putValue);
        cacheMap.put(putKey, putValue);
        return "保存成功";
    }

    public static Object getData(String key) {
        Object retValue = null;
        if (key.matches(LINK_KEY_REGEX)) {
            String[] nameSplit = key.trim().split("\\.");
            LinkedList<String> collect = Stream.of(nameSplit).collect(Collectors.toCollection(LinkedList::new));
            String endKey = collect.pollLast();
            if (endKey != null) {
                KeyIndex keyIndex = new KeyIndex(endKey);
                JSONObject json = getJsonWithKey(null, collect);
                Object o = json.get(keyIndex.getKey());
                if (keyIndex.isArr()) {
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
                Set<String> keySet = cacheMap.keySet();
                retValue = keySet.stream().filter(k -> k.matches(key)).collect(Collectors.toMap(o -> o, o -> parseValue((String) cacheMap.get(o))));
            } else {
                switch (key) {
                    case ALL_DATA :
                        retValue = cacheMap;
                        break;
                    case ALL_KEY :
                        retValue = cacheMap.keySet();
                        break;
                    case ALL_CONFIG:
                        retValue = new JSONObject().fluentPut(NO_RECORD_HISTORY_KEY, noRecordKeys).fluentPut(MAX_HISTORY_ROW, maxRow);
                        break;
                    default:
                        retValue = parseValue((String) cacheMap.get(key));
                }
            }
        }
        return retValue;
    }

    public static Object getHistory(String key, boolean format) {
        Object ret;
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
        return ret;
    }

    public static Map<String, CharSequence> removeData(String key) {
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
                if (keyIndex.isArr()) {
                    Object o = target.get(keyIndex.getKey());
                    if (o instanceof JSONArray) {
                        JSONArray array = (JSONArray) o;
                        removeValue = array.remove(keyIndex.getIndex());
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
                Set<String> keySet = cacheMap.keySet();
                remove.putAll(keySet.stream().filter(k -> k.matches(key)).collect(Collectors.toMap(o -> o, o -> {
                    saveCacheHistory(o, null);
                    return cacheMap.remove(o);
                })));
            } else {
                if (StringUtils.equals(ALL_DATA, key)) {
                    cacheMap.forEach((k, v) -> saveCacheHistory(k, null));
                    remove.putAll(cacheMap);
                    cacheMap.clear();
                } else {
                    saveCacheHistory(key, null);
                    remove.put(key, cacheMap.remove(key));
                }
            }
        }
        return remove;
    }

    /**
     * 处理系统配置
     */
    private static void disposeSysConfig(String key, String value) {
        //保存配置数据
        switch (key) {
            case NO_RECORD_HISTORY_KEY:
                //解析
                String[] keys = StringUtils.split(value, ",");
                noRecordKeys.clear();
                //清理原来的历史数据
                for (String k : keys) {
                    if (StringUtils.isNotBlank(k)) {
                        cacheMapHistory.remove(k);
                        noRecordKeys.add(k);
                    }
                }
                break;
            case MAX_HISTORY_ROW:
                try {
                    maxRow = Integer.parseInt(String.valueOf(value));
                } catch (NumberFormatException ignored) {
                }
                break;
            default:
        }
    }

    /**
     * 兼容key模式
     */
    public static String compatibilityKey(String key) {
        if (StringUtils.isNotBlank(key)) {
            return key.replaceAll("【", "[").replaceAll("】", "]");
        }
        return key;
    }

    /**
     * 处理键对应的值
     */
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

    /**
     * 保存历史记录
     */
    private static void saveCacheHistory(String key, String value) {
        CharSequence cacheValue;
        //保存原来的值到历史
        if (!noRecordKeys.contains(key) && (cacheValue = cacheMap.get(key)) != null) {
            LinkedList<CharSequence> values = cacheMapHistory.computeIfAbsent(key, s -> new LinkedList<>());
            if (!StringUtils.equals(values.peek(), value)) {
                values.push(cacheValue);
            }
            clearValuesOfSize(values, maxRow);
        }
    }

    /**
     * 清除指定数量的值
     */
    private static void clearValuesOfSize(LinkedList<?> values, final int size) {
        if (values.size() > size) {
            values.pollLast();
            clearValuesOfSize(values, size);
        }
    }

    /**
     * 缓存回滚
     */
    public static CharSequence rollbackCache(String key, int index) {
        LinkedList<CharSequence> values = cacheMapHistory.computeIfAbsent(key, s -> new LinkedList<>());
        CharSequence historyValue = values.get(index);
        if (StringUtils.isNotBlank(historyValue)) {
            cacheMap.put(key, historyValue);
        }
        return historyValue;
    }

    /**
     * 保存链值
     */
    private static String[] saveLinkValue(final String key, final String value, final boolean replace) {
        String[] nameSplit = key.trim().split("\\.");
        LinkedList<String> collect = Stream.of(nameSplit).collect(Collectors.toCollection(LinkedList::new));
        String putKey = collect.pollLast();
        String first = collect.pollFirst();
        if (first != null && putKey != null) {
            JSONObject source = getJsonWithKey(null, first);
            if (source == null) {
                source = new JSONObject();
            }
            JSONObject target = getJsonWithKey(source, collect);
            KeyIndex keyIndex = new KeyIndex(putKey);
            Object putObj = target.get(keyIndex.getKey());
            Object valueObj = parseValue(value);
            if (!replace && keyIndex.isArr() && !(putObj instanceof JSONArray)) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, keyIndex.getKey() + "不是数组");
            } else if ((!replace || keyIndex.isArr()) && putObj instanceof JSONArray) {
                JSONArray array = (JSONArray) putObj;
                if (keyIndex.isArr()) {
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

    /**
     * 解析值
     */
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

    private static JSONObject getJsonWithKey(JSONObject json, LinkedList<String> keys) {
        String key;
        if ((key = keys.pollFirst()) != null) {
            JSONObject j = getJsonWithKey(json, key);
            if (j == null) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST, key + "不存在");
            }
            return getJsonWithKey(j, keys);
        }
        return json;
    }

    /**
     * 定位获取json
     */
    private static JSONObject getJsonWithKey(JSONObject json, String key) {
        //查看name是否以[d]结尾,如果是说明是数组,数组获取索引所在的对象
        KeyIndex keyIndex = new KeyIndex(key);
        if (keyIndex.isArr()) {
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
        } catch (JSONException e) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "不是Json对象");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP, key + "为JsonArray,请指定下标");
        }
    }
}
