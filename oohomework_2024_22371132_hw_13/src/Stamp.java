import com.oocourse.library1.LibraryBookId;

import java.time.LocalDate;

public class Stamp {
    private LocalDate date;
    private LibraryBookId id;
    private String from;

    public Stamp(LocalDate date, LibraryBookId id) {
        this.date = date;
        this.id = id;
    }

    public Stamp(LibraryBookId id, String from) {
        this.id = id;
        this.from = from;
    }

    public LibraryBookId getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

}
