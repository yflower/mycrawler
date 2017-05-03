package com.jal.crawler.data.dataProcessor;

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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class ExcelFileDataProcessor implements DataProcessor {
    @Override
    public Optional<Data> processor(List<Map<String, Object>> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();

        if(!data.isEmpty()){
            Map<String, Object> title = data.get(0);
            int rowIndex=0,columnIndex=0;

            //excel第一行标题
            Set<String> titleSet = title.keySet();
            Row row=sheet.createRow(rowIndex++);
            for(String s:titleSet){
                row.createCell(columnIndex++).setCellValue(s);
            }

            //excel内容
            for(Map<String,Object> line:data){
                Row contentRow=sheet.createRow(rowIndex++);
                columnIndex=0;
                for(Object s:line.values()){
                    contentRow.createCell(columnIndex++).setCellValue(s.toString());
                }
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
}
