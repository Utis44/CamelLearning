package main.csvexample;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.Link;

/**
 * Created by a589234 on 2014-11-06.
 */
@CsvRecord(separator = ",")
public class Employee
{
    @DataField(pos = 1, trim = true)
    private String firstName;
    @DataField(pos = 2)
    private String lastName;
    @DataField(pos = 3)
    private String dept;
    @DataField(pos = 4, trim = true)
    private String fullTime;

    @Link
    private Address address;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getDept()
    {
        return dept;
    }

    public void setDept(String dept)
    {
        this.dept = dept;
    }

    public String getFullTime()
    {
        return fullTime;
    }

    public void setFullTime(String fullTime)
    {
        this.fullTime = fullTime;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }
}
