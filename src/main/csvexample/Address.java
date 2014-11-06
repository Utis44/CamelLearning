package main.csvexample;

import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.Link;

/**
 * Created by a589234 on 2014-11-06.
 */
@Link
public class Address
{
    @DataField(pos = 5)
    private String city;
    @DataField(pos = 6)
    private String state;
    @DataField(pos = 7)
    private String zip;

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }
}
