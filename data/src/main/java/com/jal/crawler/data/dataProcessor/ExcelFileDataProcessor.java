package com.jal.crawler.data.dataProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jal.crawler.data.Data;
import com.jal.crawler.data.DataTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class ExcelFileDataProcessor implements DataProcessor {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<Data> processor(List<Map<String, Object>> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();

        //设置标题行,返回标题和列号的映射
        Map<String, Integer> titleMap = titleRowSet(sheet, data);

        if (!data.isEmpty()) {
            int rowIndex = 0;
            //excel内容
            for (Map<String, Object> line : data) {
                Row contentRow = sheet.createRow(rowIndex++);
                line.forEach((k, v) -> {
                    if (v instanceof Map) {
                        try {
                            contentRow.createCell(titleMap.get(k)).setCellValue(objectMapper.writeValueAsString(v));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    } else if (v instanceof List) {
                        List<Object> list = (List<Object>) v;
                        int index = 0;
                        for (Object o : list) {
                            if (o instanceof List) {
                                throw new IllegalStateException("不能出现2层嵌套");
                            } else if (o instanceof Map) {
                                try {
                                    contentRow.createCell(titleMap.get(k + "_" + index++)).setCellValue(objectMapper.writeValueAsString(o));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                contentRow.createCell(titleMap.get(k + "_" + index++)).setCellValue(o.toString());

                            }
                        }
                    } else {
                        contentRow.createCell(titleMap.get(k)).setCellValue(v.toString());
                    }
                });
            }
            try {
                wb.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        byte[] bytes = outputStream.toByteArray();

        ResponseEntity responseEntity;
        responseEntity = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename=test.xls")
                .body(bytes);

        Data result = new Data();
        result.setDataTypeEnum(DataTypeEnum.EXCEL_FILE);
        result.setResponse(responseEntity);
        return Optional.of(result);
    }

    private Map<String, Integer> titleRowSet(Sheet sheet, List<Map<String, Object>> data) {
        Map<String, Integer> result = new HashMap<>();

        if (data.isEmpty()) {
            return result;
        }

        int rowIndex = 0, columnIndex = 0;
        Map<String, Object> title = data.get(0);
        Set<String> titleSet = new TreeSet<>();
        Row row = sheet.createRow(rowIndex++);
        title.forEach((k, v) -> {
            if (v instanceof List) {
                Object o = data.stream().max(Comparator.comparingInt(t -> ((List) (t.get(k))).size())).get().get(k);
                List<Object> objects = (List<Object>) o;
                int size = objects.size();
                for (int i = 1; i <= size; ++i) {
                    String t = k + "_" + i;
                    titleSet.add(t);
                }
            } else if (v instanceof Map) {
                titleSet.add(k);
            } else {
                titleSet.add(k);
            }
        });
        for (String s : titleSet) {
            result.put(s, columnIndex);
            row.createCell(columnIndex++).setCellValue(s);
        }
        return result;
    }
}
