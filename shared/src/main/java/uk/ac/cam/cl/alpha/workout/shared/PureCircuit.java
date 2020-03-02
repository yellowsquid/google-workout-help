package uk.ac.cam.cl.alpha.workout.shared;

import java.io.Serializable;

public interface PureCircuit extends Serializable {
    long getId();

    String getName();

    int getLaps();
}
