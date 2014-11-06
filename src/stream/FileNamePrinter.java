package stream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by a589234 on 2014-10-30.
 */
public class FileNamePrinter implements Processor
{

    @Override
    public void process(Exchange exchange) throws Exception
    {
        System.out.println("Processing file: " + exchange.getIn().getHeader("CamelFileName"));
    }
}
