import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Guest guest1 = new Guest("John", "john@test.com", "1654561");
        Guest guest2 = new Guest("David", "david@test.com", "65106");
        Guest guest3 = new Guest("Garry", "garry@test.com", "184165");
        Guest guest4 = new Guest("Frenkie", "frenkie@test.com", "1812361");
        Guest guest5 = new Guest("Robert", "rob@test.com", "674565456");
        Guest guest6 = new Guest("Mark", "mark@test.com", "45646876");
        Guest guest7 = new Guest("Jason", "jason@test.com", "3140407");
        Guest guest8 = new Guest("George", "george@test.com", "47564");
        Guest guest9 = new Guest("Josh", "josh@test.com", "7897240");
        Guest guest10 = new Guest("Harry", "harry@test.com", "045047597");
        Guest guest11 = new Guest("Adrien", "adrien@test.com", "04467321");

        Event event1 = new Event("Black Tie Gala", "O2 Arena" , LocalDate.of(2025, 1, 18));
        Event event2 = new Event("Masquerade Ball", "CopperBox Arena" , LocalDate.of(2025, 6, 22));
        Event event3 = new Event("80s Retro Night", "Nobu" , LocalDate.of(2025, 7, 7));
        Event event4 = new Event("Black Tie Gala", "Wembley Arena", LocalDate.of(2024, 1, 31));

        List<Event> listOfEvents = new ArrayList<>();
        listOfEvents.add(event1);
        listOfEvents.add(event2);
        listOfEvents.add(event3);
        listOfEvents.add(event4);

        // provera eventova
        eventStatusCheck(listOfEvents);

        List<Event> upcomingEvents = new ArrayList<>();
        List<Event> completedEvents = new ArrayList<>();
        List<Event> cancelledEvents = new ArrayList<>();

        sortEvents(listOfEvents, completedEvents, upcomingEvents, cancelledEvents);

        List<Guest> allGuests = new ArrayList<>();
        Random random = new Random();
        allGuests.add(guest1);
        allGuests.add(guest2);
        allGuests.add(guest3);
        allGuests.add(guest4);
        allGuests.add(guest5);
        allGuests.add(guest6);
        allGuests.add(guest7);
        allGuests.add(guest8);
        allGuests.add(guest9);
        allGuests.add(guest10);
        allGuests.add(guest11);

        fillEvents(allGuests, random, listOfEvents);
        eventGuestList(listOfEvents);

//        findByEmail("jacob@test.com", listOfEvents, allGuests);
//        findByName("Harry", listOfEvents, allGuests);

//        allGuestInfo(allGuests);
//        mostReliable(allGuests);
//        frequentNoShow(allGuests);
        listEvents(listOfEvents, completedEvents, upcomingEvents, cancelledEvents);
//        lowAttendance(upcomingEvents);

        String searchingTheme = "Black Tie Gala";
//        themeLoyalty(listOfEvents, searchingTheme, allGuests);
    }

    private static void themeLoyalty(List<Event> listOfEvents, String searchingTheme, List<Guest> allGuests) {
        List<Event> matchingThemeEvents = new ArrayList<>();

        for(Event event: listOfEvents){
            if(event.getTheme().equals(searchingTheme)){
                matchingThemeEvents.add(event);
            }
        }

        if (matchingThemeEvents.isEmpty()) {
            System.out.println("No events found with the theme: " + searchingTheme);
            System.out.println("--------------------------");
            return;
        }

        List<Event> completedEvents = getCompletedEventsWithTheme(matchingThemeEvents);

        List<Event> upcomingEvents = getUpcomingEventsWithTheme(matchingThemeEvents);

        List<Event> canceledEvents = getCanceledEventsWithTheme(matchingThemeEvents);

        if (completedEvents.isEmpty()) {
            if (!upcomingEvents.isEmpty() && canceledEvents.isEmpty()) {
                System.out.println("All events with the theme: " + searchingTheme + " are upcoming.");
            }
            else if (!canceledEvents.isEmpty() && upcomingEvents.isEmpty()) {
                System.out.println("All events with the theme: " + searchingTheme + " are canceled.");
            }
            else {
                System.out.println("No completed events found for the theme: " + searchingTheme + ". The rest are upcoming or canceled.");
            }
            System.out.println("--------------------------");
            return;
        }

        System.out.println("Guests who attended every completed event with the theme: " + searchingTheme);

        for (Guest guest : allGuests) {
            boolean attendedAll = true;

            for (Event event : completedEvents) {
                if (!event.getInvited().contains(guest)) {
                    System.out.println(guest + " wasn't on the invited list for: " + event);
                    attendedAll = false;
                }

                if (event.getDeclined().contains(guest)) {
                    System.out.println(guest + " declined the invitation for: " + event);
                    attendedAll = false;
                }

                if (event.getConfDidntShow().contains(guest)) {
                    System.out.println(guest + " confirmed but didn't show up for: " + event);
                    attendedAll = false;
                }
            }

            if (attendedAll) {
                System.out.println(guest);
            }
        }

        System.out.println("-----------------------");
    }

    private static List<Event> getCanceledEventsWithTheme(List<Event> matchingThemeEvents) {
        List<Event> canceledEventsWithTheme = new ArrayList<>();
        for(Event event: matchingThemeEvents){
            if(event.isCanceled()){
                canceledEventsWithTheme.add(event);
            }
        }
        return canceledEventsWithTheme;
    }

    private static List<Event> getUpcomingEventsWithTheme(List<Event> matchingThemeEvents) {
        List<Event> upcomingEventsWithTheme = new ArrayList<>();
        for(Event event: matchingThemeEvents){
            if(event.isUpcoming()){
                upcomingEventsWithTheme.add(event);
            }
        }
        return upcomingEventsWithTheme;
    }

    private static List<Event> getCompletedEventsWithTheme(List<Event> matchingThemeEvents) {
        List<Event> completedEventsWithTheme = new ArrayList<>();
        for(Event event: matchingThemeEvents){
            if(event.isCompleted()){
                completedEventsWithTheme.add(event);
            }
        }
        return completedEventsWithTheme;
    }

    private static void fillEvents(List<Guest> allGuests, Random random, List<Event> listOfEvents) {
        for(Event event: listOfEvents){
            int eventSize = random.nextInt(allGuests.size()) + 1;

            for(int i = 0; i < eventSize; i++){
                boolean found = false;
                Guest tmpGuest = allGuests.get(random.nextInt(allGuests.size()));

                if(event.getInvited().size() == 0){
                    event.addInvited(tmpGuest);
                    continue;
                }

                for(Guest eventGuest: event.getInvited()){
                    if(tmpGuest == eventGuest) {
                        found = true;
                        break;
                    }
                }

                if(!found){
                    event.addInvited(tmpGuest);
                }
            }
        }
        System.out.println();
    }

    private static void eventGuestList(List<Event> listOfEvents) {
        for(Event event: listOfEvents){
            if(event.isCanceled()){
                System.out.println("No guest list for " + event);
                System.out.println("-------------------");
                continue;
            }

            System.out.println("Invited guests for " + event);
            for(Guest guest : event.getInvited()){
                System.out.println(guest);
            }
            System.out.println();

            System.out.println("Guests who confirmed and didn't show up to " + event);
            for(Guest guest: event.getConfDidntShow()){
                System.out.println(guest);
            }
            System.out.println();

            System.out.println("Guests who declined to go to " + event);
            for(Guest guest: event.getDeclined()){
                System.out.println(guest);
            }
            System.out.println("--------------------------");
        }
    }

    private static void eventStatusCheck(List<Event> listOfEvents) {
        LocalDate currentDate = LocalDate.now();
        for(Event event: listOfEvents){
            event.checkStatus(currentDate);
        }
    }

    private static void lowAttendance(List<Event> upcomingEvents) {
        System.out.println("Upcoming events where less than 5 guests have confirmed their attendance");
        for(Event tmpEvent: upcomingEvents){
            if(tmpEvent.getConfirmed().size() < 5){
                System.out.println(tmpEvent + " has " + tmpEvent.getConfirmed().size() + " confirmed guests");
            }
        }
        System.out.println("-------------------------");
    }

    private static void sortEvents(List<Event> listOfEvents, List<Event> completedEvents, List<Event> upcomingEvents, List<Event> cancelledEvents) {
        for(Event tmpEvent: listOfEvents){
            if(tmpEvent.isUpcoming() && !tmpEvent.isCanceled()){
                upcomingEvents.add(tmpEvent);
            }
            else if(tmpEvent.isCompleted()){
                completedEvents.add(tmpEvent);
            }
            else if(tmpEvent.isCanceled()){
                cancelledEvents.add(tmpEvent);
            }
        }
    }

    private static void listEvents(List<Event> listOfEvents, List<Event> completedEvents, List<Event> upcomingEvents, List<Event> cancelledEvents) {
        System.out.println("All the events:");
        for(Event event: listOfEvents){
            System.out.println(event);
        }
        System.out.println();

        System.out.println("Completed events:");
        for(Event event: completedEvents){
            System.out.println(event);
        }
        System.out.println();

        System.out.println("Upcoming events:");
        for(Event event: upcomingEvents){
            System.out.println(event);
        }
        System.out.println();

        System.out.println("Cancelled events:");
        for(Event event: cancelledEvents){
            System.out.println(event);
        }
        System.out.println("--------------------");
    }

    private static void frequentNoShow(List<Guest> allGuests) {
        System.out.println("Guests who have confirmed at least 3 events and didn't show up for at least 2 are:");
        for(Guest tmpGuest: allGuests){
            if(tmpGuest.getConfirmed() >= 3 && tmpGuest.getNotShownUp() >= 2){
                System.out.println(tmpGuest);
            }
        }
        System.out.println("-------------------");
    }

    public static void mostReliable(List<Guest> allGuests){
        System.out.println("Guests who have confirmed and attended the most events:");

        allGuests.sort(
                Comparator.comparing((Guest g) -> g.getAttended() == 0)
                        .thenComparing(Comparator.comparingInt(Guest::getConfirmed).reversed())
                        .thenComparing(Comparator.comparingInt(Guest::getAttended).reversed()));

        int counter = 0;
        for(Guest tmpGuest: allGuests){
            if(counter == 5){
                break;
            }

            System.out.println(tmpGuest.getName() + " has confirmed " + tmpGuest.getConfirmed() + " and attended " + tmpGuest.getAttended() + " events");
            counter++;
        }
        System.out.println("----------------------");
    }

    public static void findByName(String name, List<Event> allEvents, List<Guest> allGuests){
        boolean exists = false;
        for(Guest tmpGuest: allGuests){
            if(tmpGuest.getName().equals(name)){
                exists = true;
                break;
            }
        }

        if(exists){
            boolean found = false;
            for(Event event: allEvents){
                if(event.findGuestByName(name).isPresent()){
                    System.out.println("Guest with the name " + name + " was on the list of invited people for the " + event);
                    found = true;
                }
            }

            if(!found){
                System.out.println("Guest with the name " + name + " wasn't on any list of invited people");
            }
        }
        else{
            System.out.println("Guest with the name " + name + " doesn't exist");
        }
    }

    public static void findByEmail(String email, List<Event> allEvents, List<Guest> allGuests){
        boolean exists = false;
        for(Guest tmpGuest: allGuests){
            if(tmpGuest.getEmail().equals(email)){
                exists = true;
                break;
            }
        }

        if(exists){
            boolean found = false;
            for(Event event: allEvents){
                if(event.findGuestByEmail(email).isPresent()){
                    System.out.println("Guest with the email " + email + " was on the list of invited people for the " + event);
                    found = true;
                }
            }

            if(!found){
                System.out.println("Guest with the email " + email + " wasn't on any list of invited people");
            }
        }
        else{
            System.out.println("Guest with the email " + email + " doesn't exist");
        }
    }

    public static void allGuestInfo(List<Guest> allGuests){
        for(Guest tmpGuest: allGuests){
            System.out.println("Guest " + tmpGuest.getName() + " has confirmed " + tmpGuest.getConfirmed() +
                    " events, has actually attended " + tmpGuest.getAttended() + " events, has not shown up for "
                    + tmpGuest.getNotShownUp() + " events, and declined " + tmpGuest.getDeclined() + " events");
        }
        System.out.println("------------------------");
    }
}
