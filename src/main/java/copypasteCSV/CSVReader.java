package copypasteCSV;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVReader {
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please select (n/d) to choose the way find the elements: ");
        String path = reader.readLine();
        int number = 0;
        String dateS = "" ;
        String numberS = "";
        SimpleDateFormat dateformated = null;
        if (path.equals("n") || path.equals("N")) {
            System.out.print("Your path (" + path + ") was chosen. Please enter the amount: ");
            numberS = reader.readLine();
            number = Integer.parseInt(numberS);
            System.out.println("Entered N = " + number);
            System.out.print("All fruits with balance less than N = " + number + " are listed below: ");
            System.out.println();
            System.out.println();
        }
        else if( path.equals("d") || path.equals("D")) {
            System.out.print("Your path (" + path + ") was chosen. Please enter the expiration date (yyyy-mm-dd): ");
            dateS = reader.readLine();
            String[] dt = dateS.split("-");
            int dty = Integer.parseInt(dt[0]); //year
            int dtm = Integer.parseInt(dt[1]); //month
            int dtd = Integer.parseInt(dt[2]); //day
            if (dtm == 0 || dtm > 12) {
                System.out.println("The date is not correct. ");
            } else if ((dtm == 1 || dtm == 3 || dtm == 5 || dtm == 7 || dtm == 8 || dtm == 10 || dtm == 12) && dtd >= 32) {
                System.out.println("The date is not correct. ");
            } else if ((dtm == 4 || dtm == 6 || dtm == 9 || dtm == 11) && dtd >= 31) {
                System.out.println("The date is not correct. ");
            } else if ((dtm == 2) && dtd >= 30) {
                System.out.println("The date is not correct. ");
            } else if ((dty % 4 != 0) && dtm == 2 && dtd >= 29) {
                System.out.println("The date is not correct. ");
            } else if ((dty % 4 == 0) && dtm == 2 && dtd >= 30) {
                System.out.println("The date is not correct. ");
            }
            dateformated = new SimpleDateFormat(dateS);
            System.out.println("Entered expiration date: = " + dateS);
            System.out.print("All fruits with expiration date before date: " + dateS + " are listed below: ");
            System.out.println();
        }
        else {
            System.out.println("Unfortunately, input is not correct. Good luck next time! ");
        }
        List<Item> items = readFromCSV("sample.csv");
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getFrname().compareTo(o2.getFrname());
            }
        });

        for(Item i:items){
            if(numberS != "" ){
                if (number > i.getFrquantity()){
                    System.out.println(i);
                }
            } else if (dateformated.toLocalizedPattern().compareTo(i.getFrexp().toLocalizedPattern()) > 0) {
                System.out.println(i);
            }

        }
    }

    private static List<Item> readFromCSV(String filename) {
        List<Item> items = new ArrayList<>();
        Path pathTofile = Paths.get(filename);
        int countl = 0;
        try (BufferedReader br = Files.newBufferedReader(pathTofile)) {
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                countl++;
                String[] attributes = line.split(",");
                Item item = createItem(attributes);
                items.add(item);
                line = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("File was not found");
        }
        return items;
    }

    private static Item createItem(String[] metadata) {
        String frname = metadata[0];
        long frcode = Long.parseLong(metadata[1]);
        int frquantity = Integer.parseInt(metadata[2]);
        SimpleDateFormat frexp = new SimpleDateFormat(metadata[3]);

        return new Item(frname, frcode, frquantity, frexp);
    }
}

class Item {
    private String frname;
    private long frcode;
    private int frquantity;
    private SimpleDateFormat frexp;

    public Item (String frname, long frcode, int frquantity, SimpleDateFormat frexp){
        this.frname = frname;
        this.frcode = frcode;
        this.frquantity = frquantity;
        this.frexp = frexp;
    }

    public String getFrname() {
        return frname;
    }

    public void setFrname(String frname) {
        this.frname = frname;
    }

    public long getFrcode() {
        return frcode;
    }

    public void setFrcode(long frcode) {
        this.frcode = frcode;
    }

    public int getFrquantity() {
        return frquantity;
    }

    public void setFrquantity(int frquantity) {
        this.frquantity = frquantity;
    }

    public SimpleDateFormat getFrexp() {
        return frexp;
    }

    public void setFrexp(SimpleDateFormat frexp) {
        this.frexp = frexp;
    }

    public int compareTo(Item compareItem) {

        int compareQuantity = ((Item) compareItem).getFrquantity();

        //ascending order
        //return this.fquantity - compareQuantity;

        //descending order
        return compareQuantity - this.frquantity;

    }

    public static Comparator<Item> FruitNameComparator = new Comparator<Item>() {

        public int compare(Item fruit1, Item fruit2) {

            String fruitName1 = fruit1.getFrname().toUpperCase();
            String fruitName2 = fruit2.getFrname().toUpperCase();


            //ascending order
            return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };

    @Override
    public String toString() {

        return
                "[name = " + frname + " code = " + frcode + " Expiration = " + frexp.toLocalizedPattern() + " " + " Quantity = " + frquantity + "]" ;
    }
}
