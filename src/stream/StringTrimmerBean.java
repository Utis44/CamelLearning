package stream;

/**
 * Created by a589234 on 2014-10-31.
 */
public class StringTrimmerBean
{
    public static final int MAXSIZE = 1000;

    public static String trim(String content)
    {
        return content.substring(0, Math.min(MAXSIZE, content.length()));
    }
}
