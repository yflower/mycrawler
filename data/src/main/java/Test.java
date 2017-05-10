import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jianganlan on 2017/5/8.
 */
public class Test {
    public static void main(String[] args) throws IOException {

        InputStream inputStream=new FileInputStream("/Users/jianganlan/Downloads/jj1.xls");
        OutputStream outputStream=new FileOutputStream("/Users/jianganlan/Downloads/jj1_1.xls");

        Workbook workbook=new HSSFWorkbook(inputStream);
        Sheet result=workbook.createSheet("result");
        List<Integer> rows=new ArrayList<>();
        int resultRowIndex=0;

        Set<String> target=new HashSet<>();
        Sheet sheet=workbook.getSheetAt(1);
        for(Row row:sheet){
            target.add(row.getCell(1).getStringCellValue());
        }


        Sheet sheet1 = workbook.getSheetAt(0);
        for(Row row:sheet1){
            for(int i=2;i<16;++i){
                try {
                    String cellValue = row.getCell(i).getStringCellValue();
                    if(target.contains(cellValue)){
                        if(!rows.contains(row.getRowNum())){
                            Row resultRow = result.createRow(resultRowIndex++);
                            rowCopy(row,resultRow);
                            rows.add(row.getRowNum());
                        }
                    }
                }catch (Exception e){
                    continue;
                }

            }
        }
        workbook.write(outputStream);
    }

    private static void rowCopy(Row from,Row to){
        int i=0;
        for(Cell cell:from){
            try {
                to.createCell(i).setCellValue(cell.getStringCellValue());
                i++;
            }catch (Exception e){
                to.createCell(i).setCellValue(cell.getNumericCellValue());
            }
        }
    }
}
