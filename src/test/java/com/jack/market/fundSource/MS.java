package com.jack.market.fundSource;

import com.jack.market.baseInterface.AccessInterface;
import com.jack.market.baseInterface.LoanInterface;

import java.util.Map;

public class MS implements AccessInterface, LoanInterface {
    @Override
    public Map<String, Object> access(Map<String, Object> param) {
        System.out.println("MS 准入-----------");
        return null;
    }

    @Override
    public Map<String, Object> loan(Map<String, Object> param) {
        System.out.println("MS 用信-----------");
        return null;
    }
}
