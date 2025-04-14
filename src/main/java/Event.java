import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter

public class Event {
    private String theme;
    private String location;
    private LocalDate date;
    private List<Guest> invited = new ArrayList<>();
    private List<Guest> confirmed = new ArrayList<>();
    private List<Guest> confDidntShow = new ArrayList<>();
    private List<Guest> declined = new ArrayList<>();
    private Random random = new Random();
    private boolean completed;
    private boolean canceled = false;
    private boolean upcoming;
    private List<EmailSchedule.EmailTask> scheduledEmails = new ArrayList<>();
    EmailSchedule schedule = new EmailSchedule();
    public Event(String theme, String location, LocalDate date) {
        this.theme = theme;
        this.location = location;
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean isUpcoming() {
        return upcoming;
    }

    public void checkStatus(LocalDate currentDate){
        if(currentDate.isBefore(date)){
            upcoming = true;
            completed = false;

            // 20% sansa da se event otkaze
            if(random.nextDouble() < 0.20){
                canceled = true;
            }
        }
        else if(currentDate.isAfter(date)){
            upcoming = false;
            completed = true;
        }
    }

    public void addInvited(Guest guest) {
        if(completed){
            invited.add(guest);

            confirmOrDeclineEvent(guest);
        }
        else{
            casesForActiveInactiveEvents(guest);
        }
    }

    private void casesForActiveInactiveEvents(Guest guest) {
        // event i dalje aktivan
        if(!canceled){
            invited.add(guest);

            confirmOrDeclineEvent(guest);
        }
        // event je otkazan
        else{
            System.out.println(this + " is canceled");
            for(Guest tmpGuest: confirmed){
                System.out.println("Sending email to notify about the cancellation of " + getTheme() + " event to " + tmpGuest.getEmail());
                scheduledEmails.add(new EmailSchedule.EmailTask(tmpGuest.getEmail(), "Event Cancellation", "The event " + this + " has been canceled", LocalDate.now()));
            }
            schedule.processEmails(scheduledEmails);
        }
    }

    private void confirmOrDeclineEvent(Guest guest) {
        //75% sansa da potvrde
        if(random.nextDouble() < 0.75){
            confirmed.add(guest);
            guest.increaseConfirmation();

            if(!completed){
                System.out.println("An email reminder for the " + this + " will be sent on " + getDate().minusDays(1) + " to " + guest.getName());
                scheduledEmails.add(new EmailSchedule.EmailTask(guest.getEmail(), "Event reminder", "This is a reminder for the event " + this, getDate().minusDays(1)));
                schedule.processEmails(scheduledEmails);
            }

            checkIfAttendsAfterConfirmation(guest);
        }
        //25% sansa da odbiju
        else{
            declined.add(guest);
            guest.increaseDeclined();
        }
    }

    private void checkIfAttendsAfterConfirmation(Guest guest) {
        // 15% sansa da ne dodje nakon potvrde dolaska
        if(random.nextDouble() < 0.15){
            if(upcoming){
                return;
            }

            confDidntShow.add(guest);
            guest.increaseNotShownUp();
        }
        // 85% sansa da zapravo dodju nakon potvrde
        else{
            guest.increaseAttendance();
        }
    }

    public Optional<Guest> findGuestByEmail(String email){
        for(Guest tmpGuest: invited){
            if(email.equals(tmpGuest.getEmail())){
                return Optional.of(tmpGuest);
            }
        }

        return Optional.empty();
    }

    public Optional<Guest> findGuestByName(String name){
        for(Guest tmpGuest: invited){
            if(name.equals(tmpGuest.getName())){
                return Optional.of(tmpGuest);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Event{" +
                "theme='" + theme + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                '}';
    }
}
