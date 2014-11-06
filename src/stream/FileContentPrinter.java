package stream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by a589234 on 2014-10-30.
 */
public class FileContentPrinter implements Processor
{
    @Override
    public void process(Exchange exchange) throws Exception
    {
        //use with care :)
        System.out.println("File body:");
        //prints something like:
        //GenericFile[C:\Users\a589234\IdeaProjects\CamelLearning\pointstart\a.txt]
        System.out.println(exchange.getIn().getBody().toString());
        //yep, that prints file content
        System.out.println(exchange.getIn().getBody(String.class));
    }
}
