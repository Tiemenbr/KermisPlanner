package Objects;

import Gui.Pathfinding.Tile;
import Gui.SimulatorView.MapGenerator;
import Objects.Observers.AttractionsObserver;
import Objects.Observers.LocationsObserver;
import Objects.Observers.ScheduleItemsObserver;

import java.util.HashMap;
import java.util.UUID;

public class Schedule {

    private static final MapGenerator mapGenerator = new MapGenerator("festivalMap.json");
    private HashMap<UUID, ScheduleItem> scheduleItems;
    private HashMap<UUID, Attraction> attractions;
    private HashMap<String, Location> locations;
    private ScheduleItemsObserver scheduleItemsObserver;
    private AttractionsObserver attractionsObserver;
    private LocationsObserver locationsObserver;

    public Schedule() {
        this.scheduleItems = new HashMap<>();
        this.attractions = new HashMap<>();
        this.locations = new HashMap<>();
        this.scheduleItemsObserver = new ScheduleItemsObserver(this);
        this.attractionsObserver = new AttractionsObserver(this);
        this.locationsObserver = new LocationsObserver(this);

        // Get all locations
        for (Location location : mapGenerator.getLocations()) {
            this.addLocation(location);
        }
    }

    public static MapGenerator getMapGenerator() {
        return mapGenerator;
    }

    public HashMap<UUID, ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    public ScheduleItem getScheduleItem(UUID id) {
        return scheduleItems.get(id);
    }

    public void addScheduleItem(ScheduleItem scheduleItem) {
        System.out.println(scheduleItem);
        this.scheduleItems.put(scheduleItem.getId(), scheduleItem);
        this.scheduleItemsObserver.update();
    }

    public void deleteScheduleItem(UUID id) {
        this.scheduleItems.remove(id);
        this.scheduleItemsObserver.update();
    }

    public HashMap<UUID, Attraction> getAttractions() {
        return attractions;
    }

    public Attraction getAttraction(UUID id) {
        return attractions.get(id);
    }

    public void addAttraction(Attraction attraction) {
        this.attractions.put(attraction.getId(), attraction);
        this.attractionsObserver.update();
    }

    public void deleteAttraction(UUID id) {
        this.attractions.remove(id);
        this.attractionsObserver.update();
    }

    public HashMap<String, Location> getLocations() {
        return locations;
    }

    public Location getLocation(String name) {
        return locations.get(name);
    }

    public void addLocation(Location location) {
        this.locations.put(location.getName(), location);
        this.locationsObserver.update();
    }

//    public void deleteLocation(UUID id) {
//        this.locations.remove(id);
//        this.locationsObserver.update();
//    }

    public void setScheduleItemsObserver(ScheduleItemsObserver observer) {
        this.scheduleItemsObserver = observer;
    }

    public void setAttractionsObserver(AttractionsObserver attractionsObserver) {
        this.attractionsObserver = attractionsObserver;
    }

    public void setLocationsObserver(LocationsObserver locationsObserver) {
        this.locationsObserver = locationsObserver;
    }

}
