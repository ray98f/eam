package com.wzmtr.eam.config;

import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
 
import java.util.Iterator;

/**
 * 导出Excel自适应行高配置
 * @author  Ray
 * @version 1.0
 * @date 2023/12/06
 */
public class CustomCellWriteHeightConfig extends AbstractRowHeightStyleStrategy {

    /**
     * 默认高度
     */
    private static final Integer DEFAULT_HEIGHT = 300;
    private static final Integer FIFTY = 50;
    private static final Integer ZERO = 0;

    @Override
    protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
    }

    @Override
    protected void setContentColumnHeight(Row row, int relativeRowIndex) {
        Iterator<Cell> cellIterator = row.cellIterator();
        if (!cellIterator.hasNext()) {
            return;
        }
        // 默认为 1行高度
        int maxHeight = 1;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() == CellType.STRING) {
                String value = getStringBuilder(cell.getStringCellValue());
                if (value.contains("\n")) {
                    int length = value.split("\n").length;
                    maxHeight = Math.max(maxHeight, length) + 1;
                }
            }
        }
        row.setHeight((short) ((maxHeight) * DEFAULT_HEIGHT));
    }

    /**
     * 字符串拼接
     * @param str 字符串
     * @return 拼接后的字符串
     */
    private static String getStringBuilder(String str) {
        StringBuilder value = new StringBuilder(str);
        int len = value.length();
        int num = 0;
        if (len > FIFTY) {
            num = len % 50 > 0 ? len / 50 : len / 2 - 1;
        }
        if (num > ZERO) {
            for (int i = 0; i < num; i++) {
                value.append(value.substring(0, (i + 1) * 50 + i)).append("\n").append(value.substring((i + 1) * 50 + i, len + i));
            }
        }
        return value.toString();
    }
}