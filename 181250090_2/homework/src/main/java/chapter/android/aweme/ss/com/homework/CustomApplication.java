package chapter.android.aweme.ss.com.homework;

import android.app.Application;

/**
 * 继承Application
 *
 * @author admin
 *
 */
public class CustomApplication extends Application
{
    private static final String VALUE = "";

    private String value;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //init value
        setValue(VALUE);
    }

    public void setValue(String value)
    {
        this.value += value;
    }

    public String getValue()
    {
        return value;
    }
}