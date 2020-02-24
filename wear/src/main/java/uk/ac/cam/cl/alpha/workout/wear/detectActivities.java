package uk.ac.cam.cl.alpha.workout.wear;

import android.util.Log;

import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public class detectActivities {
    private static long lastActivity = 0L;
    private static float lastZ = 0;

    // custom helper values
    private static long lastDown = 0L;


    static boolean detectActivity(float[] values, long timestamp, Exercise exercise){
        float X = values[0];
        float Y = values[1];
        float Z = values[2];
        boolean result = false;
        //Todo: Fix so works with enum
        switch (""+exercise.getExerciseType()){
            case "STAR_JUMPS":
                result = detectStarJump(X, Z, timestamp);
                break;
            case "SQUATS":
                //result = detectSquat(X, Z, timestamp);
                break;
            case "BURPEES":
                result = detectBurpee(X, Z, timestamp);
                break;
            case "SITUPS":
                result = detectSitup(X, Z, timestamp);
                break;
            case "PUSHUPS":
              //  result = detectPushup(X, Z, timestamp);
                break;
            case "RUSSIAN_TWISTS":
                result = detectRussianTwist(X, Y, timestamp);
                break;
            default:

                result = false;

        }


        lastZ = Z;

        return result;
    }

    //
    private static boolean detectStarJump(float X, float Z, long timestamp){
        boolean done = false;
        long minimumPause = 400000000;
        // Stopping upwards = 1 jump
        if (X+Z < -12 && lastZ > 3){
            // Don't count same jump multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("StarJump------", "StarJump");
                done = true;
            }
            lastActivity = timestamp;
        }

        return done;
    }



    public static boolean detectSquat(float X, float Z, long timestamp){
        boolean done = false;
        long minimumPause = 600000000L;
        long minUp =        400000000L;
        long maxUp =       2000000000L;
        if (X+Z < -1.3){
            lastDown = timestamp;
        } else if (X+Z > 1.5 && timestamp-lastDown < maxUp && timestamp-lastDown > minUp){
            // Don't count same squat multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("Squat------", "UP ");
                done = true;
            }
            lastActivity = timestamp;
        }

        return done;
    }


    private static boolean detectBurpee(float X, float Z, long timestamp){
        boolean done = false;
        long minimumPause = 1500000000L;

        long maxUp =         800000000L;
        // Stopping upwards = 1 jump
        if (X+Z < -9){
            lastDown = timestamp;
        } else if (X+Z > 2 && timestamp-lastDown < maxUp){
            // Don't count same jump multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("Burpee------", "Burpee");
                done = true;
            }
            lastActivity = timestamp;
        }
        return done;
    }

    private static boolean detectSitup(float X, float Z, long timestamp){


        boolean done = false;
        long minimumPause = 1500000000L;
        long minUp =        500000000L;
        long maxUp =        2500000000L;
        // Stopping upwards = 1 jump
        if (X+Z < -5){
            lastDown = timestamp;
        } else if (X+Z > 0.2 && timestamp-lastDown < maxUp && timestamp-lastDown > minUp){
            // Don't count same squat multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("SITUP------", "SITUP ");
                done = true;
            }
            lastActivity = timestamp;
        }
        return done;
    }

    public static boolean detectPushup(float X, float Z, long timestamp){
        boolean done = false;
        long minimumPause = 600000000L;
        long maxUp =        2000000000L;
        // Stopping upwards = 1 jump
        if (X+Z > 0.4){
            lastDown = timestamp;
        } else if (X+Z < -0.4 && timestamp-lastDown < maxUp){
            // Don't count same squat multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("Pushup------", "Pushup");
                done = true;
            }
            lastActivity = timestamp;
        }
        return done;
    }

    private static boolean first = true;
    private static boolean detectRussianTwist(float X, float Y, long timestamp){

        boolean done = false;
        long minimumPause = 400000000L;
        long maxUp =        2500000000L;
        // Stopping upwards = 1 jump
        if (X+Y < -1){
            lastDown = timestamp;
        } else if (X+Y > 1 && timestamp-lastDown < maxUp){
            // Don't count same squat multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("Twist------", "Twist");
                if (first){
                    first = false;
                    done = false;
                } else {
                    first = true;
                    done = true;
                }

            }
            lastActivity = timestamp;
        }
        return done;
    }



}
