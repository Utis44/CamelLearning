package main;

import main.csvexample.CsvToPojoRoute;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import stream.FileOperation;

/**
 * Created by a589234 on 2014-10-30.
 */
public class MyApp
{
    public static void main(String[] args)
    {
//        String[] folders = {"pointstart", "pointend", "enrich", "anotherend"};
//        String input = "csv start";
//        String output = "pointend";
//        System.out.println("Running some camel");
//        try
//        {
////            FileOperation.transferFiles("pointstart", "pointend", "anotherend");
////            FileOperation.transferAndDivideFiles("pointstart", "pointend", "anotherend");
////            FileOperation.enrichExample(folders);
//            FileOperation.marshalCsv(input, output);
//        }
//        catch (Exception e)
//        {
//            /////////////////////////
//            e.printStackTrace();
//        }

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("camel-spring-context.xml");
        SpringCamelContext springCamelContext = applicationContext.getBean("camelcontextbean", SpringCamelContext.class);

        try
        {
            springCamelContext.start();
            springCamelContext.addRoutes(applicationContext.getBean("csvtopojoroute", CsvToPojoRoute.class));

            ProducerTemplate producerTemplate = springCamelContext.createProducerTemplate();
            producerTemplate.sendBody("direct:start", "john, doe, d1, true, city1, state1, 1234");
        }
        catch (Exception e)
        {
            System.out.println("Exception trying to get pojo");
            e.printStackTrace();
        }

        try
        {
            Thread.sleep(5000);
            springCamelContext.stop();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
