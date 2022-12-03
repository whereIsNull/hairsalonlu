package com.skynet.javafx.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalUtils {

    public static BigDecimal toBigDecimal(String value) {
        if(StringUtils.isEmpty(value)) {
            return new BigDecimal(0);
        }
        try {
            String valueStr = value.replaceAll(",", "\\.");
            BigDecimal bd = (BigDecimal) new BigDecimal(valueStr);
            System.out.println(bd.toString());
            return bd;
        } catch (NumberFormatException e) {
//            showErrorWindow("Formato incorrecto");
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Formato de número incorrecto: " + value, ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                //do stuff
            }
            throw new RuntimeException(e);
        }
    }

    public static String toString(BigDecimal value) {
        if(value == null) {
            return "";
        }
        return value.toString().replaceAll("\\.", ",");
    }
}
