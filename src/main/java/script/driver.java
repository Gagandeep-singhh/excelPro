package script;
import config.ActionKeyword;
import config.Constant;
import excelUtil.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class driver {

    public static Properties OR;

    public static ActionKeyword actionKeyword;
    public static Method method[];

    public  String SuitId;
    public String suiteRunmode;
    public String tCaseId;
    public String tCaseRunmode;
    public String testCaseStepId;
    public String sActionKeyword;
    public String sPageObject;
    public String sData;

    static  {


        actionKeyword = new ActionKeyword();
        method = actionKeyword.getClass().getMethods();

    }

    public static void main(String[] args) throws FileNotFoundException, IOException, InvocationTargetException, IllegalAccessException {

        String Path_OR = Constant.Path_OR;
        FileInputStream fs = new FileInputStream(Path_OR);
        //Creating an Object of properties
        OR = new Properties(System.getProperties());
        //Loading all the properties from Object Repository property file in to OR object
        OR.load(fs);

        driver driverObj = new driver();
        driverObj.start();



    }

    private  void start() throws InvocationTargetException, IllegalAccessException {
        excelReader excelConstruct = new excelReader("/Users/gagandeepsingh/IdeaProjects/ExcelPro/src/main/java/excel/dataEngine.xlsx");

        int rowCount = excelConstruct.getRowCount("Test Cases");
        System.out.println(rowCount);
        for (int sRow=2;sRow<=rowCount;sRow++) {
            SuitId = excelConstruct.getCellData("Test Cases", "TestCase ID", sRow);
            suiteRunmode = excelConstruct.getCellData("Test Cases", "RunMode", sRow);
            if (suiteRunmode.equalsIgnoreCase("yes")){

                excelConstruct= new excelReader("/Users/gagandeepsingh/IdeaProjects/ExcelPro/src/main/java/excel/"+SuitId+".xlsx");
                int tCaseSheetRow = excelConstruct.getRowCount("Test Cases");
                System.out.println(tCaseSheetRow);
                for (int tRow=2;tRow<=tCaseSheetRow;tRow++){
                    tCaseId = excelConstruct.getCellData("Test Cases", "TestCase ID", tRow);
                    tCaseRunmode = excelConstruct.getCellData("Test Cases", "RunMode", tRow);
                    if (tCaseRunmode.equalsIgnoreCase("yes")){
                        int tCaseStepsRow = excelConstruct.getRowCount("Test Steps");
                        System.out.println(tCaseStepsRow);
                        for (int tStep=2;tStep<=tCaseStepsRow;tStep++) {

                            testCaseStepId = excelConstruct.getCellData("Test Steps", "TestCase ID", tStep);
                            //System.out.println(testCaseStepId);
                            if (testCaseStepId.equalsIgnoreCase(tCaseId)) {
                                //System.out.println(testCaseStepId);
                                sActionKeyword = excelConstruct.getCellData("Test Steps","Action_Keyword",tStep);
                                sPageObject = excelConstruct.getCellData("Test Steps","Page Object",tStep);
                                sData = excelConstruct.getCellData("Test Steps","Data Set",tStep);

                                System.out.print(sActionKeyword+" "+ sPageObject+" "+" "+sData);
                                execute_Actions();



                            }

                        }

                    }


                }

            }

        }
    }

    private void execute_Actions() throws InvocationTargetException, IllegalAccessException {

        for (Method value : method) {
            if (value.getName().equals(sActionKeyword)) {
                value.invoke(actionKeyword,sPageObject,sData);

            }
        }

    }

}
