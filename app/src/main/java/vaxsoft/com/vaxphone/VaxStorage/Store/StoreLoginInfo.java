package vaxsoft.com.vaxphone.VaxStorage.Store;

import java.util.concurrent.atomic.AtomicBoolean;

import vaxsoft.com.vaxphone.MainUtil.PreferenceUtil;

public class StoreLoginInfo
{
    private final String USERNAME = "VaxStoreLoginInfo_Username";
    private final String DISPLAY_NAME = "VaxStoreLoginInfo_DisplayName";
    private final String AUTH_LOGIN = "VaxStoreLoginInfo_AuthLogin";
    private final String PASSWORD = "VaxStoreLoginInfo_Password";
    private final String DOMAIN_REALM = "VaxStoreLoginInfo_DomainRealm";
    private final String SERVER_IP = "VaxStoreLoginInfo_Server_IP";
    private final String SERVER_PORT = "VaxStoreLoginInfo_Server_Port";
    private final String SIP_REGISTRATION = "VaxStoreLoginInfo_Registration_SIP";
    private final String REDIS_SERVER = "VaxStoreLoginInfo_Redis_Server";
    private final String REDIS_PORT = "VaxStoreLoginInfo_Redis_Port";
    private final String REDIS_PASSWORD = "VaxStoreLoginInfo_Redis_Password";
    private final String SYSTEM_ID = "VaxStoreLoginInfo_System_Id";

    private String USERNAME_DEFAULT_VALUE = "";
    private String DISPLAY_NAME_DEFAULT_VALUE = "";
    private String AUTH_LOGIN_DEFAULT_VALUE = "";
    private String PASSWORD_DEFAULT_VALUE = "";
    private String DOMAIN_REALM_DEFAULT_VALUE = "";
    private String SERVER_IP_DEFAULT_VALUE = "";
    private String SERVER_PORT_DEFAULT_VALUE = "5060";
    private boolean SIP_REGISTRATION_DEFAULT_VALUE = true;

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    public void GetLoginInfo(StringBuilder sUsername, StringBuilder sDisplayName, StringBuilder sAuthLogin, StringBuilder sAuthPassword, StringBuilder sDoaminRealm,
                             StringBuilder sServerIP, StringBuilder sServerPort, AtomicBoolean bRegistrationSIP,StringBuilder sRedisServer,StringBuilder sRedisPassword,
                             StringBuilder sRedisPort,StringBuilder systemId)
    {
        if (sUsername != null)
            sUsername.append(PreferenceUtil.ReadPreferenceValue(USERNAME, USERNAME_DEFAULT_VALUE));

        if (sDisplayName != null)
        sDisplayName.append(PreferenceUtil.ReadPreferenceValue(DISPLAY_NAME, DISPLAY_NAME_DEFAULT_VALUE));

        if (sAuthLogin != null)
            sAuthLogin.append(PreferenceUtil.ReadPreferenceValue(AUTH_LOGIN, AUTH_LOGIN_DEFAULT_VALUE));

        if (sAuthPassword != null)
            sAuthPassword.append(PreferenceUtil.ReadPreferenceValue(PASSWORD, PASSWORD_DEFAULT_VALUE));

        if (sDoaminRealm != null)
            sDoaminRealm.append(PreferenceUtil.ReadPreferenceValue(DOMAIN_REALM, DOMAIN_REALM_DEFAULT_VALUE));

        if (sServerIP != null)
            sServerIP.append(PreferenceUtil.ReadPreferenceValue(SERVER_IP, SERVER_IP_DEFAULT_VALUE));

        if (sServerPort != null)
            sServerPort.append(PreferenceUtil.ReadPreferenceValue(SERVER_PORT, SERVER_PORT_DEFAULT_VALUE));

        if (bRegistrationSIP != null)
            bRegistrationSIP.set(PreferenceUtil.ReadPreferenceValue(SIP_REGISTRATION, SIP_REGISTRATION_DEFAULT_VALUE));

        if (sRedisServer != null)
            sRedisServer.append(PreferenceUtil.ReadPreferenceValue(REDIS_SERVER,""));

        if(sRedisPassword != null)
            sRedisPassword.append(PreferenceUtil.ReadPreferenceValue(REDIS_PASSWORD,""));

        if (sRedisPort != null)
            sRedisPort.append(PreferenceUtil.ReadPreferenceValue(REDIS_PORT,""));

        if (systemId != null)
            systemId.append(PreferenceUtil.ReadPreferenceValue(SYSTEM_ID,""));
    }

    public void SetLoginInfo(String sUsername, String sDisplayName, String sAuthLogin, String sAuthPassword, String sDoaminRealm, String sServerIP, String sServerPort, boolean bRegistrationSIP
            ,String sRedisServer,String sRedisPassword,String sRedisPort,String systemId)
    {
        if (sUsername != null)
             PreferenceUtil.WritePreferenceValue(USERNAME, sUsername);

        if (sDisplayName != null)
            PreferenceUtil.WritePreferenceValue(DISPLAY_NAME, sDisplayName);

        if (sAuthLogin != null)
            PreferenceUtil.WritePreferenceValue(AUTH_LOGIN, sAuthLogin);

        if (sAuthPassword != null)
            PreferenceUtil.WritePreferenceValue(PASSWORD, sAuthPassword);

        if (sDoaminRealm != null)
            PreferenceUtil.WritePreferenceValue(DOMAIN_REALM, sDoaminRealm);

        if (sServerIP != null)
            PreferenceUtil.WritePreferenceValue(SERVER_IP, sServerIP);

        if (sServerPort != null)
            PreferenceUtil.WritePreferenceValue(SERVER_PORT, sServerPort);

        if (sRedisServer != null)
            PreferenceUtil.WritePreferenceValue(REDIS_SERVER, sRedisServer);

        if (sRedisPassword != null)
            PreferenceUtil.WritePreferenceValue(REDIS_PASSWORD, sRedisPassword);

        if (sRedisPort != null)
            PreferenceUtil.WritePreferenceValue(REDIS_PORT, sRedisPort);
        if (systemId != null)
            PreferenceUtil.WritePreferenceValue(SYSTEM_ID, systemId);

        PreferenceUtil.WritePreferenceValue(SIP_REGISTRATION, bRegistrationSIP);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
}
