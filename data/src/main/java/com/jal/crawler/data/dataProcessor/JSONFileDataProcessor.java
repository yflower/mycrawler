package com.jal.crawler.data.dataProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jal.crawler.data.Data;
import com.jal.crawler.data.DataTypeEnum;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class JSONFileDataProcessor implements DataProcessor {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<Data> processor(List<Map<String, Object>> data) {
        String s = null;
        ResponseEntity responseEntity;
        try {
            s = objectMapper.writeValueAsString(data);
            responseEntity = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename=test.json")
                    .body(s.getBytes());
        } catch (JsonProcessingException e) {
            responseEntity = ResponseEntity.notFound().build();
        }

        Data result = new Data();
        result.setDataTypeEnum(DataTypeEnum.JSON_STRING_FILE);
        result.setResponse(responseEntity);
        return Optional.of(result);
    }
}
