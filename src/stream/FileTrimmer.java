package stream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by a589234 on 2014-10-31.
 */
public class FileTrimmer implements Processor
{
    public static final int MAXSIZE = 1000;

    @Override
    public void process(Exchange exchange) throws Exception
    {
        //limit size to fixed size
        String body = exchange.getIn().getBody(String.class);

        String trimmed = body.substring(0, Math.min(MAXSIZE, body.length()));

        exchange.getIn().setBody(trimmed);
    }
}
