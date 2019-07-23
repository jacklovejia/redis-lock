package com.jack.market.fundSource;

import com.jack.market.baseInterface.AccessInterface;
import com.jack.market.baseInterface.LoanInterface;
import com.jack.market.baseInterface.ShouXinInterface;

import java.util.Map;

public class SN implements AccessInterface, ShouXinInterface, LoanInterface {
    @Override
    public Map<String, Object> access(Map<String, Object> param) {
        System.out.println("SN 准入..............");
        return null;
    }

    @Override
    public Map<String, Object> loan(Map<String, Object> param) {
        System.out.println("SN 用信..............");
        return null;
    }

    @Override
    public Map<String, Object> shouXin(Map<String, Object> param) {
        System.out.println("SN 授信..............");
        return null;
    }
}
