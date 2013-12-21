package Timers;

import org.andengine.engine.handler.IUpdateHandler;

public class playTimer implements IUpdateHandler
{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private final ITimerCallback    mCallback;
    private float                                   mInterval;

    private float                                   mSecondsElapsed;
    protected boolean                               mPause  = false;

    private float                                   pausedSecond;

    // ===========================================================
    // Constructors
    // ===========================================================
    public playTimer(final float interval, final ITimerCallback pCallback)
    {
	    this.mInterval = interval;
	    this.mCallback = pCallback;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public void setInterval(final float pInterval)
    {
    	this.mInterval = pInterval;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onUpdate(float pSecondsElapsed)
    {
	    this.mSecondsElapsed += pSecondsElapsed;
	    if (this.mSecondsElapsed >= this.mInterval)
	    {
	    	if (!this.mPause)
		    {
			    this.mSecondsElapsed -= this.mInterval;
			    this.mCallback.onTick();
		    }
	
	    }
    }

    @Override
    public void reset()
    {
    	this.mSecondsElapsed = 0;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public interface ITimerCallback {
            public void onTick();
    }

    public void pause()
    {
	    this.mPause = true;
	    this.pausedSecond = this.mSecondsElapsed;
    }

    public void resume()
    {
	    this.mPause = false;
	    this.mSecondsElapsed = this.pausedSecond;
    }
}
