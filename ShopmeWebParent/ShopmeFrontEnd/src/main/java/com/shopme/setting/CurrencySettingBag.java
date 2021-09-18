package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

public class CurrencySettingBag extends SettingBag {

    public CurrencySettingBag(List<Setting> listSetting) {
	super(listSetting);
    }

    public String getSymbol() {
	return super.getValue("CURRENCY_SYMBOL");
    }

    public String getSymbolPosition() {
	return super.getValue("CURRENCY_SYMBOL_POSITION");
    }

    public String getDecimalPointType() {
	return super.getValue("DECIMAL_POINT_TYPE");
    }

    public String getThousandsPointType() {
	return super.getValue("THOUSANDS_POINT_TYPE");
    }

    public int getDecimalDigits() {
	return Integer.parseInt(super.getValue("DECIMAL_DIGITS"));
    }
}
