package main.csvexample;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

import java.util.List;
import java.util.Map;

/**
 * Created by a589234 on 2014-11-06.
 */
public class CsvToPojoRoute extends RouteBuilder
{
    @Override
    public void configure() throws Exception
    {
        DataFormat bindy = new BindyCsvDataFormat("main.csvexample");

        from("direct:start")
                .unmarshal(bindy)
                .process(new Processor()
                {
                    @Override
                    public void process(Exchange exchange) throws Exception
                    {
                        Message in = exchange.getIn();
                        List<Map<String, Object>> modelMap = (List<Map<String, Object>>) in.getBody();
                        Employee employee = (Employee) modelMap.get(0).get(Employee.class.getCanonicalName());
                        System.out.println("Employee name: " + employee.getFirstName() + " " + employee.getLastName() );
                    }
                })
                .end();
    }
}
