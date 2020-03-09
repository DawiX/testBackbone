package org.dawix.tests;

import org.dawix.listeners.AllureListener;
import org.dawix.tests.common.BaseTest;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Listeners({AllureListener.class})
public class TestExecutor extends BaseTest {

    @Test
    public void testRunner() {
        //create a virtual testng.xml file to list the specific method to be executed
        XmlSuite suite = new XmlSuite();
        suite.setName("GoogleTests");
        TestListenerAdapter tla = new TestListenerAdapter();

        XmlTest test = new XmlTest(suite);
        test.setName("TestSearch");
        List<XmlClass> classes = new ArrayList<>();

        //get methods for homepage test
        XmlClass testGoogle = new XmlClass("org.dawix.tests.google.TestGoogle");
        List<XmlInclude> testGoogleMethods = new ArrayList<>();
        testGoogleMethods.add(new XmlInclude("accessGoogle"));
        testGoogleMethods.add(new XmlInclude("testGoogle"));
        testGoogle.setIncludedMethods(testGoogleMethods);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("browser_name", "chrome");
        parameters.put("headless", "false");
        testGoogle.setParameters(parameters);

        //add this class to the list of classes to execute
        classes.add(testGoogle);

        //do this for every test classes that needs to be tested, one by one

        //run the tests
        test.setXmlClasses(classes);
        TestNG runner = new TestNG();
        //Using Allure for reports so we won't need to set this now.
        //runner.setOutputDirectory(System.getenv("WORKING_DIRECTORY")); //uses the custom artifacts to get the results
        runner.setPreserveOrder(true);
        List<XmlSuite> suitefiles = new ArrayList<>();
        suitefiles.add(suite);

        runner.setXmlSuites(suitefiles);
        //execute the tests
        runner.run();
    }
}
