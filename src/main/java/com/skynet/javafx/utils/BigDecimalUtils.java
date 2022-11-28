package com.skynet.javafx.utils;

import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalUtils {

    public static BigDecimal toBigDecimal(String value) throws ParseException {
        if(StringUtils.isEmpty(value)) {
            return new BigDecimal(0);
        }
        try {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
            df.setParseBigDecimal(true);
            BigDecimal bd = (BigDecimal) df.parseObject(value);
            System.out.println(bd.toString());
            return bd;
        } catch (ParseException e) {
            e.printStackTrace();
//            showErrorWindow("Formato incorrecto");
            throw e;
        }
    }

    public static String toString(BigDecimal value) throws ParseException {
        if(value == null) {
            return "";
        }
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        df.setParseBigDecimal(true);
        return df.format(value.setScale(2, RoundingMode.DOWN));
    }
}
