package com.skynet.javafx.views.def;

import com.skynet.javafx.views.ProductView;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductGridDef implements FrameGridDef {

    public static String COLUMN_NAMES[] =
            { "Categoría", "Servicio","Precio"};
    public static String COLUMN_DATA_NAMES[] =
            { "category", "productName","price"};
    public static Integer COLUMN_SIZES[] = { 200, 200, 50 };
    public static String TITLE_POPUPS = "Servicios";

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList(COLUMN_NAMES);
    }

    @Override
    public List<Integer> getColumnSizes() {
        return Arrays.asList(COLUMN_SIZES);
    }

    @Override
    public List<String> getColumnDataName() {
        return Arrays.asList(COLUMN_DATA_NAMES);
    }

    @Override
    public Class<?> getCreateView() {
        return ProductView.class;
    }

    @Override
    public String getTitlePopups() {
        return TITLE_POPUPS;
    }
}
