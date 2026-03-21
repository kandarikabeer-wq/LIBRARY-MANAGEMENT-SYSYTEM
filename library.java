public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private boolean isAvailable;


    public Book(String bookId, String title, String author,String category, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.isAvailable = (quantity > 0) ? true : false;
    }


    public String getBookId() {
        return bookId;
    }


    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }

    public String getCategory(){
        return category;
    }

    public int getQuantity() {
        return quantity;
    }


    public boolean isAvailable() {
        return isAvailable;
    }


    public void setBookId(String bookId) {
        this.bookId = bookId;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
 
    
}
class library{
    public static void main(String [] args){

    } 
}