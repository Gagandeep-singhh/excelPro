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
import config.Constant;

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
        excelReader excelConstruct = new excelReader(Constant.pathTestSuiteSheet);

        int rowCount = excelConstruct.getRowCount(Constant.Sheet_TestCases);
        System.out.println(rowCount);
        for (int sRow=2;sRow<=rowCount;sRow++) {
            SuitId = excelConstruct.getCellData(Constant.Sheet_TestCases, Constant.Suite_TestCaseID, sRow);
            suiteRunmode = excelConstruct.getCellData(Constant.Sheet_TestCases,Constant.Col_RunMode, sRow);
            if (suiteRunmode.equalsIgnoreCase("yes")){

                excelConstruct= new excelReader(Constant.pathTestCaseSheet+SuitId+".xlsx");
                int tCaseSheetRow = excelConstruct.getRowCount(Constant.Sheet_TestCases);
                System.out.println(tCaseSheetRow);
                for (int tRow=2;tRow<=tCaseSheetRow;tRow++){
                    tCaseId = excelConstruct.getCellData(Constant.Sheet_TestCases, Constant.Suite_TestCaseID, tRow);
                    tCaseRunmode = excelConstruct.getCellData(Constant.Sheet_TestCases, Constant.Col_RunMode, tRow);
                    if (tCaseRunmode.equalsIgnoreCase("yes")){
                        int tCaseStepsRow = excelConstruct.getRowCount(Constant.Sheet_TestSteps);
                        System.out.println(tCaseStepsRow);
                        for (int tStep=2;tStep<=tCaseStepsRow;tStep++) {

                            testCaseStepId = excelConstruct.getCellData(Constant.Sheet_TestSteps, Constant.TestCase_ScenarioID, tStep);
                            //System.out.println(testCaseStepId);
                            if (testCaseStepId.equalsIgnoreCase(tCaseId)) {
                                //System.out.println(testCaseStepId);
                                sActionKeyword = excelConstruct.getCellData(Constant.Sheet_TestSteps,Constant.ActionKeyword,tStep);
                                sPageObject = excelConstruct.getCellData(Constant.Sheet_TestSteps,Constant.PageObject,tStep);
                                sData = excelConstruct.getCellData(Constant.Sheet_TestSteps,Constant.DataSet,tStep);

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
