package com.jack.market.baseInterface;

import java.util.Map;

/**
 * 准入接口
 */
public interface AccessInterface {

    Map<String,Object> access(Map<String,Object> param);
}
