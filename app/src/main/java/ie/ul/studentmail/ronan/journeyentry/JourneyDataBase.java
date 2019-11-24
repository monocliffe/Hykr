package ie.ul.studentmail.ronan.journeyentry;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Journey.class}, version = 3)
public abstract class JourneyDataBase extends RoomDatabase {

    public abstract JourneyDAO getJourneyDAO();
}


