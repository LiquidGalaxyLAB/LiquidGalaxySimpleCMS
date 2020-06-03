package com.example.simple_cms.connection;

import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Properties;

/**
 * LGConnectionManager is in charge of sending all the commands and creating the connection to liquid galaxy.
 * Also, this class is a singleton.
 */
public class LGConnectionManager implements Runnable {

    private final static String TAG_DEBUG = "LGConnectionManager";

    private static LGConnectionManager instance = null;
    private String user;
    private String password;
    private String hostname;
    private int port;
    private Session session;

    /**
     * Enforce private constructor and add the default values
     */
    private LGConnectionManager() {
        user = "lg";
        password = "1234";
        this.hostname = "192.168.0.17";
        this.port = 22;
        session = null;
    }

    public static LGConnectionManager getInstance() {
        if (instance == null) {
            instance = new LGConnectionManager();
            new Thread(instance).start();
        }
        return instance;
    }


    @Override
    public void run() {
        try{
            getSession();
        } catch (Exception e){
            Log.w(TAG_DEBUG, e.getMessage());
        }
    }

    /**
     * Create a session if the old session is nul
     * @return Session
     */
    private Session getSession() {
        Session oldSession = this.session;
        if (oldSession == null || !oldSession.isConnected()) {
            JSch jsch = new JSch();
            Session session;
            try {
                session = jsch.getSession(user, hostname, port);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "Get Session:" + e.getMessage());
                return null;
            }
            session.setPassword(password);

            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            try {
                session.connect(Integer.MAX_VALUE);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "Session Connect:" +e.getMessage());
                return null;
            }

            Log.w(TAG_DEBUG, "Logrado");
            this.session = session;
            return session;
        }


        try {
            oldSession.sendKeepAliveMsg();
            return oldSession;

        } catch (Exception e) {
            Log.w(TAG_DEBUG, e.getMessage());
            return null;
        }
    }

}