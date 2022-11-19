package com.skynet.javafx.views.def;

import com.skynet.javafx.views.CategoryView;
import com.skynet.javafx.views.CustomerView;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryGridDef implements FrameGridDef {

    public static String COLUMN_NAMES[] =
            { "serviceName","prize"};
    public static String COLUMN_DATA_NAMES[] =
            { "serviceName","prize"};
    public static Integer COLUMN_SIZES[] = { 200, 50 };
    public static String TITLE_POPUPS = "Categories";

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
        return CategoryView.class;
    }

    @Override
    public String getTitlePopups() {
        return TITLE_POPUPS;
    }
}
