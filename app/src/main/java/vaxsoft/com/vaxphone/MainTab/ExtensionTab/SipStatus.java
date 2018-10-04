package vaxsoft.com.vaxphone.MainTab.ExtensionTab;

import com.google.gson.annotations.SerializedName;

public class SipStatus {

    @SerializedName("Id")
    public int Id;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("Extension")
    public String Extension;

    @SerializedName("Status")
    public String Status;
}
