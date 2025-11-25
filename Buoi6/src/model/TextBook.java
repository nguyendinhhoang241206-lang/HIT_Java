package model;

public class TextBook extends Book {
    private String subject;

    public TextBook() {
    }

    public TextBook(int id, String title, String author, double price, int quantity,String subject) {
        super(id, title, author, price, quantity);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String displayInfo() {
        return "TextBook{ " + super.displayInfo() + " subject=" + subject + " }";
    }
}
