import com.oocourse.library1.LibraryBookId;

public class Book {
    private LibraryBookId libraryBookId;
    private LibraryBookId.Type type;
    private String uid;

    public Book(LibraryBookId libraryBookId) {
        this.libraryBookId = libraryBookId;
        this.type = libraryBookId.getType();
        this.uid = libraryBookId.getUid();
    }

    public LibraryBookId.Type getType() {
        return type;
    }

    public LibraryBookId getLibraryBookId() {
        return libraryBookId;
    }

    public String getUid() {
        return uid;
    }

}
