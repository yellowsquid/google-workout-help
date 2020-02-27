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
    void deleteCircuits(List<BareCircuit> circuits);

    @Transaction
    @Query("SELECT * FROM circuits ORDER BY name")
    LiveData<List<Circuit>> getCircuits();

    @Transaction
    @Query("SELECT * FROM circuits WHERE id = :id")
    LiveData<Circuit> getCircuit(long id);

    @Query("SELECT laps FROM circuits WHERE id = :id")
    LiveData<Integer> getLaps(long id);

    @Query("SELECT laps FROM circuits WHERE id = :id")
    int getLapsNow(long id);

    @Query("SELECT name FROM circuits WHERE id = :id")
    LiveData<String> getName(long id);

    @Query("SELECT name FROM circuits WHERE id = :id")
    String getNameNow(long id);
}
