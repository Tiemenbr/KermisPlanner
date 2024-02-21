import Objects.*;


public class Planner {
    private Schedule schedule = new Schedule();

    public void init() {
        schedule.addLocation(new Location(1,10, 10, "P1"));
        schedule.addLocation(new Location(2,170, 130, "P2"));
        schedule.addLocation(new Location(3,60, 45, "P3"));

        schedule.addAttraction(new Attraction(1,"draaimolen", 4, 5));
        schedule.addAttraction(new Attraction(2,"botsautos", 5, 7));

        schedule.addScheduleItem(new ScheduleItem(1, schedule.getLocation(1), schedule.getAttraction(1),"10:10", "10:30"));
        schedule.addScheduleItem(new ScheduleItem(2, schedule.getLocation(2), schedule.getAttraction(2),"10:30", "11:00"));

//        schedule.getVisitors().add(new Visitor());
//        schedule.getVisitors().add(new Visitor());
//        schedule.getVisitors().add(new Visitor());
//        schedule.getVisitors().add(new Visitor());
    }
}
