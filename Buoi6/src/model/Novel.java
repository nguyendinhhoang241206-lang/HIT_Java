package model;

public class Novel extends Book {
    private String genre;

    public Novel() {
    }

    public Novel(int id, String title, String author, double price, int quantity,String genre) {
        super(id, title, author, price, quantity);
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String displayInfo() {
        return "Novel{ " + super.displayInfo() + " genre=" + genre + " }";
    }
}
