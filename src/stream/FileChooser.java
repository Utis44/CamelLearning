package stream;

/**
 * Created by a589234 on 2014-10-30.
 */
public class FileChooser
{
    public boolean isBigFile(String body)
    {
        if (body.length() > 2048)
        {
            return true;
        }
        return false;
    }

    public boolean isSmallFile(String body)
    {
        if (body.length() <= 2048)
        {
            return true;
        }
        return false;
    }
}
