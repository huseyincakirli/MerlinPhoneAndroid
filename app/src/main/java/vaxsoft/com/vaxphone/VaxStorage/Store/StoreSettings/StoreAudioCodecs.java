package vaxsoft.com.vaxphone.VaxStorage.Store.StoreSettings;

import VaxVoIP.VaxUserAgentLib.VaxUserAgentLib;
import vaxsoft.com.vaxphone.MainUtil.PreferenceUtil;
import vaxsoft.com.vaxphone.VaxPhoneSIP;

public class StoreAudioCodecs
{
    private final String AUDIO_CODEC_G711uLAW = "VaxAudioCodecG711uLaw";
    private final String AUDIO_CODEC_G711aLAW = "VaxAudioCodecG711aLaw";
    private final String AUDIO_CODEC_G729 = "VaxAudioCodecG729";
    private final String AUDIO_CODEC_GSM = "VaxAudioCodecGSM";
    private final String AUDIO_CODEC_iLBC = "VaxAudioCodeciLBC";

    private final boolean AUDIO_CODEC_G711uLAW_DEFAULT_VALUE = true;
    private final boolean AUDIO_CODEC_G711aLAW_DEFAULT_VALUE = true;
    private final boolean AUDIO_CODEC_G729_DEFAULT_VALUE = true;
    private final boolean AUDIO_CODEC_GSM_DEFAULT_VALUE = true;
    private final boolean AUDIO_CODEC_iLBC_DEFAULT_VALUE = true;

    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    public void GetAudioCodecs(boolean[] AudioCodecs)
    {
        AudioCodecs[VaxUserAgentLib.VAX_CODEC_G711U] = PreferenceUtil.ReadPreferenceValue(AUDIO_CODEC_G711uLAW, AUDIO_CODEC_G711uLAW_DEFAULT_VALUE);
        AudioCodecs[VaxUserAgentLib.VAX_CODEC_G711A] = PreferenceUtil.ReadPreferenceValue(AUDIO_CODEC_G711aLAW, AUDIO_CODEC_G711aLAW_DEFAULT_VALUE);
        AudioCodecs[VaxUserAgentLib.VAX_CODEC_GSM610] = PreferenceUtil.ReadPreferenceValue(AUDIO_CODEC_GSM, AUDIO_CODEC_GSM_DEFAULT_VALUE);
        AudioCodecs[VaxUserAgentLib.VAX_CODEC_ILBC] = PreferenceUtil.ReadPreferenceValue(AUDIO_CODEC_iLBC, AUDIO_CODEC_iLBC_DEFAULT_VALUE);
        AudioCodecs[VaxUserAgentLib.VAX_CODEC_G729] = PreferenceUtil.ReadPreferenceValue(AUDIO_CODEC_G729, AUDIO_CODEC_G729_DEFAULT_VALUE);
    }

    public void SetAudioCodecs(boolean[] AudioCodecs)
    {
        PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_G711uLAW, AudioCodecs[VaxUserAgentLib.VAX_CODEC_G711U]);
        PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_G711aLAW, AudioCodecs[VaxUserAgentLib.VAX_CODEC_G711A]);
        PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_GSM, AudioCodecs[VaxUserAgentLib.VAX_CODEC_GSM610]);
        PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_iLBC, AudioCodecs[VaxUserAgentLib.VAX_CODEC_ILBC]);
        PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_G729, AudioCodecs[VaxUserAgentLib.VAX_CODEC_G729]);
    }

    public void SetAudioCodec(int codecId)
    {
        switch (codecId)
        {
            case 0:
                PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_G711uLAW, true);
                break;
            case 1:
                PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_G711aLAW, true);
                break;
            case 2:
                PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_GSM, true);
                break;
            case 3:
                PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_iLBC,true);
                break;
            case 4:
                PreferenceUtil.WritePreferenceValue(AUDIO_CODEC_G729, true);
                break;
            default:
                break;
        }
    }

//    public static final int VAX_CODEC_G711U  = 0;
//    public static final int VAX_CODEC_G711A  = 1;
//    public static final int VAX_CODEC_GSM610 = 2;
//    public static final int VAX_CODEC_ILBC   = 3;
//    public static final int VAX_CODEC_G729   = 4;


    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
}
