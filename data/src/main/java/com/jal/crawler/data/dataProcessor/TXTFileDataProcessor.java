package com.jal.crawler.data.dataProcessor;

import com.jal.crawler.data.Data;
import com.jal.crawler.data.DataTypeEnum;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class TXTFileDataProcessor implements DataProcessor {
    @Override
    public Optional<Data> processor(List<Map<String, Object>> data) {
        String s = "";
        ResponseEntity responseEntity;
        Data result = new Data();
        try {
            String title = data.get(0).keySet().stream().collect(Collectors.joining(" ")).concat("\n");
            s = s.concat(title);
            for (Map<String, Object> line : data) {
                String tmp = line.values().stream().map(Object::toString).collect(Collectors.joining(" "));
                s = s.concat(tmp.concat("\n"));

            }
            responseEntity = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename=test.txt")
                    .body(s.getBytes());
        } catch (Exception e) {
            responseEntity = ResponseEntity.notFound().build();
        }

        result.setDataTypeEnum(DataTypeEnum.TXT_STRING_FILE);
        result.setResponse(responseEntity);
        return Optional.of(result);


    }
}
