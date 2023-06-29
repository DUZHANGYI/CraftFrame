package com.craft.frame.commons.lang.utils;

import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DURR
 * @desc Bean类转换工具
 * @date 2023/6/23 20:10
 */

@SuppressWarnings({"unchecked", "unused"})
public class BeanUtils {

	public static <S, T> T convert(S source, Class<T> cls) {
		if (source == null || cls == null) {
			return null;
		}
		return JSON.parseObject(JSON.toJSONString(source), cls);
	}

	public static <S, T> T convert(S source, T target) {
		if (source == null || target == null) {
			return null;
		}
		return (T) convert(source, target.getClass());
	}

	public static <S, T> List<T> convertList(List<S> sourceList, Class<T> cls) {
		List<T> list = new ArrayList<>();
		if (sourceList == null || cls == null) {
			return list;
		}
		return JSON.parseArray(JSON.toJSONString(sourceList), cls);
	}

}
