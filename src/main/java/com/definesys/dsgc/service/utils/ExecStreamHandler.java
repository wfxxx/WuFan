package com.definesys.dsgc.service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExecStreamHandler
{
    private Thread _outThread;

    public ExecStreamHandler(Process process, OutputStream outputStream)
    {
        try
        {
            process.getOutputStream().close();
        }
        catch (IOException localIOException)
        {
        }
        this._outThread = create(process.getInputStream(), outputStream);
    }

    private Thread create(InputStream is, OutputStream os)
    {
        Runnable pump = new ExecStreamPump(is, os);
        Thread t = new Thread(pump);
        t.setDaemon(true);
        return t;
    }

    public void start()
    {
        this._outThread.start();
    }

    public void stop()
    {
        try
        {
            this._outThread.join();
        }
        catch (InterruptedException localInterruptedException)
        {
        }
    }
}