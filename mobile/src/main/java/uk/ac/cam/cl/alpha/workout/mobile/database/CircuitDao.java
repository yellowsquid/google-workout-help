package uk.ac.cam.cl.alpha.workout.mobile.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

@Dao
public interface CircuitDao {
    @Insert
    long insertCircuit(BareCircuit circuit);

    @Update
    void updateCircuit(BareCircuit circuit);

    @Delete
    void deleteCircuit(BareCircuit circuit);

    @Query("SELECT * FROM circuits")
    LiveData<List<BareCircuit>> getBareCircuits();

    @Transaction
    @Query("SELECT * FROM circuits")
    LiveData<List<Circuit>> getCircuits();

    @Query("SELECT * FROM circuits WHERE rowid = :id")
    LiveData<BareCircuit> getBareCircuit(long id);

    @Transaction
    @Query("SELECT * FROM circuits WHERE rowid = :id")
    LiveData<Circuit> getCircuit(long id);

    @Query("SELECT laps FROM circuits WHERE rowid = :id")
    LiveData<Integer> getLaps(long id);
}
