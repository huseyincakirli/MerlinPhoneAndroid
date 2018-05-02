package vaxsoft.com.vaxphone.VaxStorage.Store;

import vaxsoft.com.vaxphone.MainUtil.PreferenceUtil;

public class StoreDialNo
{
    private final String DIAL_NO = "VaxStoreDialNumber";
    private final String DIAL_NO_DEFAULT_VALUE = "";

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    public void SaveDialNo(String sDialNo)
    {
        PreferenceUtil.WritePreferenceValue(DIAL_NO, sDialNo);
    }

    public String GetDialNo()
    {
        return PreferenceUtil.ReadPreferenceValue(DIAL_NO, DIAL_NO_DEFAULT_VALUE);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
}