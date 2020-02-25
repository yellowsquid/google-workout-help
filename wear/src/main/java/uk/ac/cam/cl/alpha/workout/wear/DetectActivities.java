package uk.ac.cam.cl.alpha.workout.wear;

import android.util.Log;

import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public enum DetectActivities {
    ;
    private static long lastActivity;
    private static float lastZ;

    // custom helper values
    private static long lastDown ;

    static boolean detectActivity(float[] values, long timestamp, Exercise exercise){
        float x = values[0];
        float y = values[1];
        float z = values[2];
        boolean result = false;


        switch (exercise.getExerciseType()){
                        case STAR_JUMPS:
                            result = detectStarJump(x, z, timestamp);
                            break;
                        case SQUATS:
                            //result = detectSquat(x, Z, timestamp);
                            break;
                        case BURPEES:
                            result = detectBurpee(x, z, timestamp);
                            break;
                        case SITUPS:
                            result = detectSitup(x, z, timestamp);
                            break;
                        case PUSHUPS:
                          //  result = detectPushup(x, Z, timestamp);
                            break;
                        case RUSSIAN_TWISTS:
                            result = detectRussianTwist(x, y, timestamp);
                            break;
                        default:
                            result = false;
                    }

        lastZ = z;

        return result;
    }

    //
    private static boolean detectStarJump(float x, float z, long timestamp){
        boolean done = false;
        long minimumPause = 400000000;
        // Stopping upwards = 1 jump
        if (x+z < -12 && lastZ > 3){
            // Don't count same jump multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("StarJump------", "StarJump");
                done = true;
            }
            lastActivity = timestamp;
        }

        return done;
    }



    public static boolean detectSquat(float x, float z, long timestamp){
        boolean done = false;
        long minimumPause = 600000000L;
        long minUp =        400000000L;
        long maxUp =       2000000000L;
        if (x+z < -1.3){
            lastDown = timestamp;
        } else if (x+z > 1.5 && timestamp-lastDown < maxUp && timestamp-lastDown > minUp){
            // Don't count same squat multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("Squat------", "UP ");
                done = true;
            }
            lastActivity = timestamp;
        }

        return done;
    }


    private static boolean detectBurpee(float x, float z, long timestamp){
        boolean done = false;
        long minimumPause = 1500000000L;

        long maxUp =         800000000L;
        // Stopping upwards = 1 jump
        if (x+z < -9){
            lastDown = timestamp;
        } else if (x+z > 2 && timestamp-lastDown < maxUp){
            // Don't count same jump multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("Burpee------", "Burpee");
                done = true;
            }
            lastActivity = timestamp;
        }
        return done;
    }

    private static boolean detectSitup(float x, float z, long timestamp){


        boolean done = false;
        long minimumPause = 1500000000L;
        long minUp =        500000000L;
        long maxUp =        2500000000L;
        // Stopping upwards = 1 jump
        if (x+z < -5){
            lastDown = timestamp;
        } else if (x+z > 0.2 && timestamp-lastDown < maxUp && timestamp-lastDown > minUp){
            // Don't count same squat multiple times
            if (timestamp-lastActivity > minimumPause){
                Log.d("SITUP------", "SITUP ");
                done = true;
            }
            lastActivity = timestamp;
        }
        return done;
    }

    public static boolean detectPushup(float x, float z, long timestamp){
        boolean done = false;
        long minimumPause = 600000000L;
        long maxUp =        2000000000L;
        // Stopping upwards = 1 jump
        if (x+z > 0.4){
            lastDown = timestamp;
        } else if (x+z < -0.4 && timestamp-lastDown < maxUp){
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
    private static boolean detectRussianTwist(float x, float y, long timestamp){

        boolean done = false;
        long minimumPause = 400000000L;
        long maxUp =        2500000000L;
        // Stopping upwards = 1 jump
        if (x+y < -1){
            lastDown = timestamp;
        } else if (x+y > 1 && timestamp-lastDown < maxUp){
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
