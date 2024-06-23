import com.oocourse.library2.LibraryBookId;

import java.time.LocalDate;

public class Stamp {
    private LocalDate date;
    private LibraryBookId id;
    private int period;

    public Stamp(LocalDate date, LibraryBookId id) {
        // for ao order
        this.date = date;
        this.id = id;
    }

    public Stamp(int period, LocalDate date, LibraryBookId id) {
        // for student borrowed
        this.id = id;
        this.date = date;
        this.period = period;
    }

    public LibraryBookId getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getDue() {
        // still available
        return date.plusDays(period);
    }

    public boolean isOverdue(LocalDate date) {
        return getDue().isBefore(date);
    }

    public void addPeriod(int plus) {
        period += plus;
    }

}
