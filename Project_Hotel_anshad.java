import java.util.*;
import java.sql.*;


class HotelManagementService {
    final String JDBC_URL = "jdbc:mysql://localhost:3306/db_hotel?characterEncoding=utf8";
    final String USERNAME = "root";
    final String PASSWORD = "";

    //ADD
    public void addReservation(String guestName, int numberOfGuests) {
        
        try {
             Class.forName("com.mysql.jdbc.Driver");//loads the MySQL JDBC (Java Database Connectivity) driver class into memory
             Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rs;

             String avl_room = "SELECT * FROM `rooms` WHERE is_booked = 0 LIMIT 1;";
             rs = st.executeQuery(avl_room); //The result of the query is stored in ResultSet object "rs".
             rs.next();
             int rs_chk=0;
             rs_chk = rs.getInt("room_number");

             if (rs_chk != 0) {
                //inserting values to table : reservations
                String str = "insert into reservations(room_number,guest_name,number_of_guests) values (";
                str = str + rs_chk + ",'";
                str = str + guestName + "',";
                str = str + numberOfGuests +")";
                // System.out.println("\n"+str);
                st.executeUpdate(str);//

                //getting reservation ID :
                str = "SELECT * FROM reservations WHERE room_number = "+rs_chk;
                // System.out.println("\n"+str);
                rs = st.executeQuery(str);
                rs.next();
                int res_id = rs.getInt("reservation_id");

                //updating values to table : rooms
                str = "update rooms set is_booked = 1 where room_number = "+rs_chk +";";
                System.out.println("Reservation successful!\n Reservation ID is : " + res_id);
                st.executeUpdate(str);
             } else {
                 System.out.println("Sorry, no available rooms for reservation.");
             }
         } catch (Exception e) {
             System.out.println("\nError : "+e);
         }
       
     }

     //CANCEL
     public void cancelReservation(int reservationId) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement st = con.createStatement();;
            ResultSet rs;
            String str = "select * from reservations";
            rs = st.executeQuery(str); //The result of the query is stored in ResultSet object "rs".
            boolean found = false;
            while(rs.next()){
                
                // System.out.println(rs.getInt("reservation_id")+" "+rs.getInt("room_number")+" "+rs.getString("guest_name")+" "+rs.getInt("number_of_guests"));
                int rs_chk = rs.getInt("reservation_id");
                int rm_no = rs.getInt("room_number");
                if ( rs_chk == reservationId) {
                    //deleting reservation
                    str = "DELETE FROM reservations WHERE reservation_id = "+rs_chk;
                    st.executeUpdate(str);

                    //updating values to table : rooms
                    str = "update rooms set is_booked = 0 where room_number = "+rm_no +";";
                    // System.out.println(str);
                    st.executeUpdate(str);
                    System.out.println("\nReservation with ID " + reservationId + " is Cancelled!");
                    found = true;
                    break;

                    }
                }
                if (!found) {
                    System.out.println("\nReservation with ID " + reservationId + " not found.");
                }
            } catch (Exception e) {
                System.out.println("\nError : "+e);
            }
       
    }

    // AVAILABLE ROOMS
    public void displayAvailableRooms() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            Statement st = con.createStatement();
            ResultSet rs;

            String str = "SELECT * FROM rooms WHERE is_booked = 0";
            rs = st.executeQuery(str);

            System.out.println("\nAvailable Rooms ---------->");
            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                System.out.println("Room Number : " + roomNumber);
                System.out.println("\n--------------------------------");
            }
        } catch (Exception e) {
            System.out.println("\nError : " + e);
        }
    }

    //RESERVED ROOMS
    public void displayAllReservations() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            Statement st = con.createStatement();
            String str = "SELECT * FROM reservations";
            ResultSet rs = st.executeQuery(str);
            
            System.out.println("\nDisplaying All Reservations --------->");
            while (rs.next()) {
                
                int reservationId = rs.getInt("reservation_id");
                int roomNumber = rs.getInt("room_number");
                String guestName = rs.getString("guest_name");
                int numberOfGuests = rs.getInt("number_of_guests");
                if (guestName != null) {
                    System.out.println("\nReservation ID: " + reservationId +
                            "\nGuest Name: " + guestName +
                            "\nRoom Number: " + roomNumber +
                            "\nNumber of Guests: " + numberOfGuests+"\n-----------------");
                }
                else{
                    System.out.println("Sorry,there are no reservations! ");
                }
            }
        } catch (Exception e) {
            System.out.println("\nError : "+e);
        }
   
    }

    //Find Room from Customer Name
    public void findRoom(String gname) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement st = con.createStatement();
            String str = "SELECT * FROM reservations";
            ResultSet rs = st.executeQuery(str);
            
            boolean found = false;
            System.out.println("\nDisplaying All Reservations --------->");
            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String guestName = rs.getString("guest_name");
                if (guestName.equals(gname)) {
                    System.out.println("\nRoom Number of " + gname + " is : " + roomNumber);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Sorry, there is no Room Reserved for the given Customer");
            }
        } catch (Exception e) {
            System.out.println("\nError : " + e);
        }
    }
}




//--------------------------------------------------------------
public class Project_Hotel_anshad {
    public static void main(String args[]){
        HotelManagementService hotelService = new HotelManagementService();
        int ch;
        Scanner s = new Scanner(System.in);

        while(true){
            System.out.println("======================================================");
            System.out.println("*            Hotel Management System                 *");
            System.out.println("======================================================");
            System.out.println("* 1. Make a Reservation                              *");
            System.out.println("* 2. Cancel Reservation                              *");
            System.out.println("* 3. View Available Rooms                            *");
            System.out.println("* 4. View ALL Reservations                           *");
            System.out.println("* 5. Find Room from Guest Name                       *");
            System.out.println("* 6. EXIT                                            *");
            System.out.println("======================================================");
            System.out.print("\nEnter your choice: ");
            ch = s.nextInt();

        switch(ch){
            case 1:
                try {
                    System.out.print("\nEnter the guest name: ");
                    s.nextLine();
                    String guestName = s.nextLine();
                    System.out.print("Enter number of guests: ");
                    int numberOfGuests = s.nextInt();
                    hotelService.addReservation(guestName, numberOfGuests);
                    break;
                } catch (Exception e) {
                    System.out.println("\nError : " + e);
                }
            case 2:
                    try {
                        System.out.print("\nEnter reservation ID to cancel: ");
                        int reservationId = s.nextInt();
                        hotelService.cancelReservation(reservationId);
                        break;
                    } catch (Exception e) {
                        System.out.println("\nError : " + e);
                    }
            case 3:
                    hotelService.displayAvailableRooms();
                    break;
            case 4:
                    hotelService.displayAllReservations();
                    break;
            case 5:
                    System.out.println("\nEnter the name of the Guest : ");
                    s.nextLine();
                    String gname = s.nextLine();
                    hotelService.findRoom(gname);
                    break;
            case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
            default:
                    System.out.println("Invalid choice. Please try again.");

            }
        }
        
    }
}

