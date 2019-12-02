package ie.ul.studentmail.ronan.journeyentry;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JourneyDAO {

    @Insert
    long addJourney(Journey journey);

    @Update
    void updateJourney(Journey journey);

    @Delete
    void deleteJourney(Journey journey);

    @Query("select * from journeys")
    List<Journey> getJourneys();

    @Query("DELETE FROM journeys")
    void delete();


}
