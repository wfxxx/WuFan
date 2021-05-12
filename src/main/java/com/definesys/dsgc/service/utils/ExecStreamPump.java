package com.definesys.dsgc.service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExecStreamPump
        implements Runnable
{
    private InputStream _is;
    private OutputStream _os;

    public ExecStreamPump(InputStream is, OutputStream os)
    {
        this._is = is;
        this._os = os;
    }

    public void run()
    {
        byte[] buf = new byte[''];
        try
        {
            int length;
            while ((length = this._is.read(buf)) > 0)
            {
                this._os.write(buf, 0, length);
            }
        }
        catch (IOException localIOException)
        {
        }
    }
}