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


public class Driver {

    public static Properties OR;

    public excelReader excelConstruct;
    public excelReader excelConstruct1;


    public static ActionKeyword actionKeyword;
    public static Method method[];

    public  String SuitId;
    public String suiteRunmode;
    public static int sRow;

    public String tCaseId;
    public String tCaseRunmode;
    public static int tRow;
    public String testCaseStepId;
    public static int tStep;
    public String sActionKeyword;
    public String sPageObject;
    public String sData;

    public static boolean bResult;

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

        Driver driverObj = new Driver();
        driverObj.start();



    }

    private  void start() throws InvocationTargetException, IllegalAccessException {
         excelConstruct = new excelReader(Constant.pathTestSuiteSheet);

        int rowCount = excelConstruct.getRowCount(Constant.Sheet_TestCases);
        System.out.println(rowCount);
        for (sRow=2;sRow<=rowCount;sRow++) {
            SuitId = excelConstruct.getCellData(Constant.Sheet_TestCases, Constant.Suite_TestCaseID, sRow);
            suiteRunmode = excelConstruct.getCellData(Constant.Sheet_TestCases,Constant.Col_RunMode, sRow);
            if (suiteRunmode.equalsIgnoreCase("yes")){
//                bResult=true;

                excelConstruct1= new excelReader(Constant.pathTestCaseSheet+SuitId+".xlsx");
                int tCaseSheetRow = excelConstruct1.getRowCount(Constant.Sheet_TestCases);
                System.out.println(tCaseSheetRow);
                for (tRow=2;tRow<=tCaseSheetRow;tRow++){
                    tCaseId = excelConstruct1.getCellData(Constant.Sheet_TestCases, Constant.Suite_TestCaseID, tRow);
                    tCaseRunmode = excelConstruct1.getCellData(Constant.Sheet_TestCases, Constant.Col_RunMode, tRow);
                    bResult=true;
                    if (tCaseRunmode.equalsIgnoreCase("yes")){

                        int tCaseStepsRow = excelConstruct1.getRowCount(Constant.Sheet_TestSteps);
                        System.out.println("steps  "+tCaseStepsRow);
                        bResult=true;
                        for (tStep=2;tStep<=tCaseStepsRow;tStep++) {

                            testCaseStepId = excelConstruct1.getCellData(Constant.Sheet_TestSteps, Constant.TestCase_ScenarioID, tStep);
                            //System.out.println(testCaseStepId);
                            if (testCaseStepId.equalsIgnoreCase(tCaseId)) {
                                Log.startTestCase(testCaseStepId);

                                //System.out.println(testCaseStepId);

                                sActionKeyword = excelConstruct1.getCellData(Constant.Sheet_TestSteps,Constant.ActionKeyword,tStep);
                                sPageObject = excelConstruct1.getCellData(Constant.Sheet_TestSteps,Constant.PageObject,tStep);
                                sData = excelConstruct1.getCellData(Constant.Sheet_TestSteps,Constant.DataSet,tStep);

                                System.out.print(sActionKeyword+" "+ sPageObject+" "+" "+sData);
                                execute_Actions();

                                if(bResult==false){
                                    //If 'false' then store the test case result as Fail
                                    excelConstruct1.setCellData(Constant.Sheet_TestCases, Constant.Col_TestStepResult, tRow,"Fail");
                                    //End the test case in the logs
                                    Log.endTestCase(testCaseStepId);
                                    //By this break statement, execution flow will not execute any more test step of the failed test case
                                    break;
                                }
                                //This will only execute after the last step of the test case, if value is not 'false' at any step


                            }


                        }

                        if(bResult==true){
                            //Storing the result as Pass in the excel sheet

                            excelConstruct1.setCellData(Constant.Sheet_TestCases, Constant.Col_TestStepResult, tRow,"Pass");
                            Log.endTestCase(testCaseStepId);
                        }
                    }


                }

            }

        }
    }

    private void execute_Actions() throws InvocationTargetException, IllegalAccessException {

//        for (Method value : method) {
//
//            if (value.getName().equals(sActionKeyword)) {
//                value.invoke(actionKeyword,sPageObject,sData);
//                if(bResult==true){
//                    //If the executed test step value is true, Pass the test step in Excel sheet
//                    excelConstruct1.setCellData(Constant.Sheet_TestSteps, Constant.Col_TestStepResult, tStep,"Pass");
//                    break;
//                }else{
//                    System.out.println("inside else");
//                    //If the executed test step value is false, Fail the test step in Excel sheet
//                    excelConstruct1.setCellData(Constant.Sheet_TestSteps, Constant.Col_TestStepResult, tStep,"Fail");
//                    //In case of false, the test execution will not reach to last step of closing browser
//                    //So it make sense to close the browser before moving on to next test case
//                    ActionKeyword.closeBrowser("","");
//                    break;
//                }
//
//
//            }
//        }
//
//    }
    for(int i=0;i<method.length;i++){

        if(method[i].getName().equals(sActionKeyword)){
            method[i].invoke(sActionKeyword,sPageObject, sData);
            if(bResult==true){
                excelConstruct1.setCellData(Constant.Sheet_TestSteps, Constant.Col_TestStepResult, tStep,"Pass");
                break;
            }else{

                excelConstruct1.setCellData(Constant.Sheet_TestSteps, Constant.Col_TestStepResult, tStep,"Fail");
                ActionKeyword.closeBrowser("","");
                break;
            }
        }
    }
}

}
