package stream;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.nio.file.FileSystems;

/**
 * Created by a589234 on 2014-10-30.
 */
public class FileOperation
{
    private static SimpleRegistry simpleRegistry = new SimpleRegistry();
    private static final String operation = "?noop=true";
    private static final String path = System.getProperty("user.dir");
    private static final String separator = FileSystems.getDefault().getSeparator();
    private static final String wireEnd = "wireend";


    private FileOperation()
    {
    }

    public static void transferFiles(String relativeStart, String relativeEnd, final String anotherRelativeEnd) throws Exception
    {
//        String path = System.getProperty("user.dir");
//        SimpleRegistry simpleRegistry = new SimpleRegistry();
        simpleRegistry.put("FileChooser", new FileChooser());

        //no change target source files
//        String operation = "?noop=true";
//        String separator = FileSystems.getDefault().getSeparator();
        final String origin = "file:" + path + separator + relativeStart + operation;
        final String target = "file:" + path + separator + relativeEnd;
        final String anotherTarget = "file:" + path + separator + anotherRelativeEnd;
        System.out.println("origin:" + path + separator + relativeStart);
        System.out.println("target " + path + separator + relativeEnd);

        final CamelContext camelContext = new DefaultCamelContext(simpleRegistry);
        camelContext.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
//                from(origin)
//                        .choice()
//                            .when(body().contains("ala")).to(anotherTarget)
//                            .otherwise().to(target)
//                        .end()
//                        .to(target);
                from(origin).process(new Processor()
                {
                    @Override
                    public void process(Exchange exchange) throws Exception
                    {
                        System.out.println("Processing some small files");
                        System.out.println(exchange.getIn().getHeader("CamelFileName"));
                    }
                })
                        .filter(method("FileChooser", "isSmallFile"))
                        .to(target);
//                from(origin).process(new Processor()
//                {
//                    @Override
//                    public void process(Exchange exchange) throws Exception
//                    {
//                        System.out.println("Processing some big files");
//                        System.out.println(exchange.getIn().getHeader("CamelFileName"));
//                    }
//                })
//                        .filter(method("FileChooser", "isBigFile"))
//                        .to(anotherTarget);
            }
        });

        camelContext.start();
        Thread.sleep(5000);
//        camelContext.getRoutes().clear();
//        camelContext.addRoutes(new RouteBuilder()
//        {
//            @Override
//            public void configure() throws Exception
//            {
//                from(origin).process(new Processor()
//                {
//                    @Override
//                    public void process(Exchange exchange) throws Exception
//                    {
//                        System.out.println("Processing some files");
//                        System.out.println(exchange.getIn().getHeader("CamelFileName"));
//                    }
//                })
//                        .filter(method("FileChooser", "isBigFile"))
//                        .to(anotherTarget);
//            }
//        });
//        Thread.sleep(5000);
        camelContext.stop();
    }

    public static void transferAndDivideFiles(String pointStart, String pointEnd, String anotherEnd) throws Exception
    {
//        simpleRegistry.put("FileChooser", new FileChooser());
        final String origin = "file:" + path + separator + pointStart + operation;
        final String target = "file:" + path + separator + pointEnd;
        final String anotherTarget = "file:" + path + separator + anotherEnd;
        final String wireTarget = "file:" + path + separator + wireEnd;
//        CamelContext camelContext = new DefaultCamelContext(simpleRegistry);
        CamelContext camelContext = new DefaultCamelContext();

        camelContext.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from(origin)
                        //copies everything
                        .wireTap(wireTarget)
                        .process(new FileContentPrinter())
//                        .process(new FileTrimmer())
                        //above and below - the same functionality
                        .bean(new StringTrimmerBean())
                        .choice()
//                        .when(header("CamelFileName").startsWith("p"))
                        .when(body().startsWith("a"))

                                //doesn't work
//                        .when(body().contains("ala"))

                        .to(anotherTarget)
                        .otherwise()
                        .to(target);

            }
        });
        camelContext.start();
        Thread.sleep(5000);
        camelContext.stop();
    }

    public static void enrichExample(String[] files) throws Exception
    {
        final String origin = "file:" + path + separator + files[0] + operation;
        final String target = "file:" + path + separator + files[1];
        //solution to lock -> readLock = none and idempotent = false
        final String enrichSource = "file:" + path + separator + files[2]+"?idempotent=false&fileName=enrich.txt&noop=true&readLock=none";
        final String enrichTarget = "file:" + path + separator + files[3];
        final String lineSeparator = System.getProperty("line.separator");
//        System.out.println(enrichSource);

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                //as it is now it manages properly the first file (a.txt) and hangs on the second (b.txt)
                //permanent lock
                //enrich.txt is used once and so can't be used again
                from(origin).pollEnrich(enrichSource, new AggregationStrategy()
                {
                    @Override
                    public Exchange aggregate(Exchange oldExchange, Exchange newExchange)
                    {
                        if (newExchange == null)
                        {
                            return oldExchange;
                        }
                        String basic = oldExchange.getIn()
                                .getBody(String.class);
                        String added = newExchange.getIn()
                                .getBody(String.class);
                        String body = basic + lineSeparator + lineSeparator +
                                "END OF OLD BODY" + lineSeparator + lineSeparator +
                                "START OF ADDED" + lineSeparator + lineSeparator + added;
                        oldExchange.getIn().setBody(body);
                        return oldExchange;
                    }
                }).to(enrichTarget);
            }
        });

        camelContext.start();
        Thread.sleep(5000);
        camelContext.stop();

    }


    //doesn't work :)
    public static void marshalCsv(String input, String output) throws Exception
    {
        final String origin = "file:" + path + separator + input + operation;
        final String target = "file:" + path + separator + output;

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from(origin)
                        .unmarshal().csv()
                        .split().tokenize("\n").streaming()
                        .to(target);
            }
        });

        camelContext.start();
        Thread.sleep(5000);
        camelContext.stop();
    }
}
