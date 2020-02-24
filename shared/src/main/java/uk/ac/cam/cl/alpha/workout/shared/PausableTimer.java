package uk.ac.cam.cl.alpha.workout.shared;

import android.os.CountDownTimer;

// TODO Wrap access of variables with synchronised blocks

public abstract class PausableTimer {
    /**
     * Underlying timer object
     */
    private CountDownTimer mTimer;
    /**
     * boolean representing if the timer is running
     */
    protected boolean mRunning = false;
    /**
     * boolean representing if the timer was cancelled
     */
    protected boolean mCancelled = false;
    /**
     * Timer duration
     */
    protected long mMillisInFuture;
    /**
     * Original timer duration
     */
    protected final long mOrigMillisInFuture;
    /**
     * The interval in millis that the user receives callbacks
     */
    protected final long mCountdownInterval;

    /**
     * The CountDownTimer underlying this object
     */
    private class UnderlyingCountDownTimer extends CountDownTimer {
        public UnderlyingCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (PausableTimer.this.mRunning) {
                // Timer running
                PausableTimer.this.mMillisInFuture -= PausableTimer.this.mCountdownInterval;
                PausableTimer.this.onTick(millisUntilFinished);
            } else {
                // Timer paused
                // Do nothing
            }
        }

        @Override
        public void onFinish() {
            PausableTimer.this.onFinish();
        }
    }

    public PausableTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mOrigMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
        mTimer = new UnderlyingCountDownTimer(millisInFuture, countDownInterval);
    }

    /**
     * @return A boolean denoting whether or not the timer is running
     */
    public synchronized boolean isRunning() {
        return mRunning;
    }

    /**
     * @return Milliseconds remaining on this timer
     */
    public synchronized long msRemaining() {
        return mMillisInFuture;
    }

    /**
     * Resumes the timer from its current position
     */
    public synchronized void resume() {
        if (mCancelled) return;
        mRunning = true;
        mTimer = new UnderlyingCountDownTimer(mMillisInFuture,mCountdownInterval);
        mTimer.start();
    }

    /**
     * Pauses the timer
     */
    public synchronized void pause() {
        mRunning = false;
        mTimer.cancel();
    }

    /**
     * Resets the countdown to it's original duration
     */
    public synchronized void reset() {
        mMillisInFuture = mOrigMillisInFuture;
        mTimer = new UnderlyingCountDownTimer(mMillisInFuture,mCountdownInterval);
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mRunning = false;
        mTimer.cancel();
    }

    /**
     * Start the countdown.
     */
    public synchronized final void start() {
        if (mCancelled) return;
        if (mRunning) {
            // May want to do something if start() called while running
            return;
        }
        mRunning = true;
//        return mTimer.start();
        mTimer.start();
    }


    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();
}
