package Objects;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ScheduleItem implements CRUD, Serializable {
    private int id;
    private Location location;  //todo replace with locationId
    private Attraction attraction;  //replace with attractionId
    private LocalTime startTime, endTime;

    public ScheduleItem(int id, Location location, Attraction attraction, String startTime, String endTime) {
        this.id = id; //todo add proper dynamic id assignment
        this.location = location;
        this.attraction = attraction;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
    }

    public ScheduleItem(int id, Location location, Attraction attraction, String startTime, int minuteDuration) {
        this(id,
                location,
                attraction,
                startTime,
                LocalTime.parse(startTime).plusMinutes(minuteDuration).toString()
        );
    }

    public Integer getId() {
        return this.id;
    }

    public Location getLocation() {
        //return Schedule.getLocation(id); //todo
        return this.location;
    }

    public Attraction getAttraction() {
        //return Schedule.getAttraction(id); //todo
        return this.attraction;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }


    public LocalTime getDurration() {
        LocalTime returnTime = LocalTime.MIDNIGHT;
        return returnTime.plusMinutes(this.startTime.until(this.endTime, ChronoUnit.MINUTES));
    }

    @Override
    public void update() {
        IOController.update(this.id, this, IOController.ObjectType.SCHEDULE_ITEM);
    }

    @Override
    public void delete(Schedule schedule) {
        //todo idk if it's right to pass the schedule... but it needs to access it somewhere I think
        schedule.deleteScheduleItem(this.getId());
        IOController.delete(this.id, IOController.ObjectType.SCHEDULE_ITEM);
    }


}
