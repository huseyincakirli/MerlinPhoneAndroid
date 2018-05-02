package vaxsoft.com.vaxphone.MainTab.CallTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import vaxsoft.com.vaxphone.CustomViews.SurfaceView.CustomSurfaceView;

public class RemoteSurfaceView extends CustomSurfaceView implements SurfaceHolder.Callback
{
    public RemoteSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void DisplayFrameRGB(int nDeviceId, byte[] pFrameRGB, int nFrameSize, int nFrameWidth, int nFrameHeight)
    {
        super.DisplayFrameRGB(nDeviceId, pFrameRGB, nFrameSize, nFrameWidth, nFrameHeight);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void DrawColorOnRemoteSurfaceView()
    {
        super.DrawColorOnRemoteSurfaceView();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void AdjustPreviewLayoutToThumbnail()
    {
        super.AdjustPreviewLayoutToThumbnail();
    }

    public void AdjustPreviewLayoutToFullScreen()
    {
        super.AdjustPreviewLayoutToFullScreen();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
}
