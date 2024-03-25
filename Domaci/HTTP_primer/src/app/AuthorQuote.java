package app;
public class AuthorQuote {
    private String author;
    private String quote;

    public AuthorQuote(String author, String quote) {
        this.author = author;
        this.quote = quote;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public String getQuote() {
        return quote;
    }

    @Override
    public String toString() {
        return   author + ": " + quote;
    }
}
