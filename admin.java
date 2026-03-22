import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Admin {
    public ArrayList<Book> allBooks;
    public Map<String, ArrayList<Book>> issued = new HashMap<>();


    public Admin(ArrayList<Book> allBooks) {
        this.allBooks = allBooks;
    }

    public void displayAllBooks() {
        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }

        String alignmentFormat = "| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n";

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n",
                "Book ID", "Title", "Author", "Category", "Qty", "Available");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

        for (Book b : allBooks) {
            System.out.printf(alignmentFormat,
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.getQuantity(),
                    b.isAvailable() ? "Yes" : "No");
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void displayAllIssuedBooks() {
        if (issued == null || issued.isEmpty()) {
            System.out.println("📭 No books have been issued yet.");
            return;
        }

        System.out.println("=======================================================================================================================");
        System.out.printf("| %-20s | %-6s | %-35s | %-25s | %-15s |%n",
                "Member Name", "BookID", "Title", "Author", "Category");
        System.out.println("=======================================================================================================================");

        for (Map.Entry<String, ArrayList<Book>> entry : issued.entrySet()) {
            String memberName = entry.getKey();
            ArrayList<Book> books = entry.getValue();

            for (Book b : books) {
                System.out.printf("| %-20s | %-6s | %-35s | %-25s | %-15s |%n",
                        memberName,
                        b.getBookId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getCategory(), 15);
            }
            System.out.println("|----------------------|--------|-------------------------------------|---------------------------|-----------------|");
        }

        System.out.println("=======================================================================================================================");
    }
    
    public void addBook() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Book ID: ");
        String bookId = sc.nextLine();

        for (Book b : allBooks) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                System.out.println("Book with this ID already exists. Updating quantity...");
                System.out.print("Enter quantity to add: ");
                int addQty = sc.nextInt();
                b.setQuantity(b.getQuantity() + addQty);
                System.out.println("Quantity updated successfully!");
                return;
            }
        }

        System.out.print("Enter Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Author: ");
        String author = sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        Book newBook = new Book(bookId, title, author, category, quantity);
        allBooks.add(newBook);

        System.out.println("Book added successfully!");
        System.out.println("---------------------------------------------");
    }

    
    public void issueBook(String bookId, String memberName) {
        Book bookToIssue = null;

        for (Book b : allBooks) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                bookToIssue = b;
                break;
            }
        }

        if (bookToIssue == null) {
            System.out.println("Book with ID " + bookId + " not found.");
            return;
        }

        if (bookToIssue.getQuantity() <= 0) {
            System.out.println("No copies of '" + bookToIssue.getTitle() + "' are currently available.");
            return;
        }

        issued.computeIfAbsent(memberName, k -> new ArrayList<>()).add(bookToIssue);
        bookToIssue.setQuantity(bookToIssue.getQuantity() - 1);
        System.out.println("Book '" + bookToIssue.getTitle() + "' issued successfully to " + memberName + ".");
    }

    public void returnBook(String bookId, String memberName) {
        if (!issued.containsKey(memberName)) {
            System.out.println("No books found for member: " + memberName);
            return;
        }

        ArrayList<Book> memberBooks = issued.get(memberName);

        Book bookToReturn = null;
        for (Book b : memberBooks) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                bookToReturn = b;
                break;
            }
        }

        if (bookToReturn == null) {
            System.out.println("The member has not issued this book (Book ID: " + bookId + ").");
            return;
        }

        memberBooks.remove(bookToReturn);
        if (memberBooks.isEmpty()) {
            issued.remove(memberName); 
        } else {
            issued.put(memberName, memberBooks);
        }

        for (Book b : allBooks) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                b.setQuantity(b.getQuantity() + 1);
                break;
            }
        }

        System.out.println("Book returned successfully!");
        System.out.printf("Book ID: %s | Member: %s%n", bookId, memberName);
    }

    public void searchBook(String keyword) {
        boolean found = false;
        keyword = keyword.toLowerCase();

        System.out.println("====================================================================================================================================");
        System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n", 
                        "BookID", "Title", "Author", "Category", "Qty", "Available");
        System.out.println("====================================================================================================================================");

        for (Book b : allBooks) {
            if (b.getBookId().toLowerCase().contains(keyword) ||
                b.getTitle().toLowerCase().contains(keyword) ||
                b.getAuthor().toLowerCase().contains(keyword) ||
                b.getCategory().toLowerCase().contains(keyword)) {

                System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n",
                                b.getBookId(),
                                b.getTitle(),
                                b.getAuthor(),
                                b.getCategory(),
                                b.getQuantity(),
                                b.isAvailable() ? "Yes" : "No");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found matching: " + keyword);
        }

        System.out.println("====================================================================================================================================");
    }

    public void categorizeBooks() {
        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }

        Map<String, List<Book>> categoryMap = new TreeMap<>(); // Sorted by category name

        for (Book b : allBooks) {
            categoryMap.computeIfAbsent(b.getCategory(), k -> new ArrayList<>()).add(b);
        }

        System.out.println("Books Categorized:");
        System.out.println("=====================================================================================================================");

        for (Map.Entry<String, List<Book>> entry : categoryMap.entrySet()) {
            String category = entry.getKey();
            List<Book> books = entry.getValue();

            System.out.println("\nCategory: " + category);
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-6s | %-45s | %-25s | %-8s | %-10s |%n", 
                            "BookID", "Title", "Author", "Qty", "Available");
            System.out.println("---------------------------------------------------------------------------------------------------------------------");

            for (Book b : books) {
                System.out.printf("| %-6s | %-45s | %-25s | %-8d | %-10s |%n",
                                b.getBookId(),
                                b.getTitle(),
                                b.getAuthor(),
                                b.getQuantity(),
                                b.isAvailable() ? "Yes" : "No");
            }
            System.out.println("\n=====================================================================================================================");
        }
    }

    public void sortBooks(String sortBy) {
        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("No books available to sort.");
            return;
        }

        List<Book> sortedList = new ArrayList<>(allBooks);

        switch (sortBy.toLowerCase()) {
            case "title":
                sortedList.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
                break;
            case "author":
                sortedList.sort(Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER));
                break;
            case "category":
                sortedList.sort(Comparator.comparing(Book::getCategory, String.CASE_INSENSITIVE_ORDER));
                break;
            case "quantity":
                sortedList.sort(Comparator.comparingInt(Book::getQuantity).reversed());
                break;
            default:
                System.out.println("Invalid sort type. Use: title, author, category, or quantity.");
                return;
        }

        System.out.println("📚 Books Sorted by " + sortBy.toUpperCase());
        System.out.println("=====================================================================================================================");
        System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n", 
                        "BookID", "Title", "Author", "Category", "Qty", "Available");
        System.out.println("=====================================================================================================================");

        for (Book b : sortedList) {
            System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n",
                            b.getBookId(),
                            b.getTitle(),
                            b.getAuthor(),
                            b.getCategory(),
                            b.getQuantity(),
                            b.isAvailable() ? "Yes" : "No");
        }

        System.out.println("=====================================================================================================================");
    }

    public void recommendBooks(String memberName, String selectedBookId) {
        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("No books available for recommendation.");
            return;
        }

        Set<Book> recommendationPool = new HashSet<>();

        if (memberName != null && issued.containsKey(memberName)) {
            List<Book> issuedBooks = issued.get(memberName);

            for (Book ib : issuedBooks) {
                for (Book b : allBooks) {
                    if (!b.getBookId().equals(ib.getBookId()) &&
                        (b.getCategory().equalsIgnoreCase(ib.getCategory()) ||
                        b.getAuthor().equalsIgnoreCase(ib.getAuthor()))) {
                        recommendationPool.add(b);
                    }
                }
            }
        }

        if (selectedBookId != null) {
            Book selectedBook = null;
            for (Book b : allBooks) {
                if (b.getBookId().equalsIgnoreCase(selectedBookId)) {
                    selectedBook = b;
                    break;
                }
            }

            if (selectedBook != null) {
                for (Book b : allBooks) {
                    if (!b.getBookId().equals(selectedBook.getBookId()) &&
                        (b.getCategory().equalsIgnoreCase(selectedBook.getCategory()) ||
                        b.getAuthor().equalsIgnoreCase(selectedBook.getAuthor()))) {
                        recommendationPool.add(b);
                    }
                }
            } else {
                System.out.println("Book with ID " + selectedBookId + " not found.");
                return;
            }
        }

        if (recommendationPool.isEmpty()) {
            System.out.println("No recommendations available based on the current data.");
            return;
        }

        System.out.println("Recommended Books:");
        System.out.println("=====================================================================================================================");
        System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n",
                        "BookID", "Title", "Author", "Category", "Qty", "Available");
        System.out.println("=====================================================================================================================");

        for (Book b : recommendationPool) {
            System.out.printf("| %-6s | %-45s | %-25s | %-20s | %-8s | %-10s |%n",
                            b.getBookId(),
                            b.getTitle(),
                            b.getAuthor(),
                            b.getCategory(),
                            b.getQuantity(),
                            b.isAvailable() ? "Yes" : "No");
        }

        System.out.println("=====================================================================================================================");
    }


}