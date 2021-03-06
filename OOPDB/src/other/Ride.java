///////////////////////////////////////////////////////////////////////////////
// Project:     Zwischen
// File:        Ride.java
// Group:       3
// Date:        November 26, 2018
// Description: Concrete class ride represents a ride generated by user input
///////////////////////////////////////////////////////////////////////////////

package other;

import static other.Globals.getCurrentUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import messages.MessagesController;

public class Ride {

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public void setRider(String rider) { this.rider = rider; }

  public String getDest() {
    return goingTo;
  }

  public void setDest(String dest) {
    this.goingTo = dest;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getStartP() {
    return comingFrom;
  }

  public void setStartP(String startP) {
    this.comingFrom = startP;
  }

  public int getIdnumber() {
    return idnumber;
  }

  public void setIdnumber(int idnumber) {
    this.idnumber = idnumber;
  }

  public Integer getSeats() {
    return seats;
  }

  public void setSeats(Integer seats) {
    this.seats = seats;
  }

  private static int nextIDNumber = 0;
  private Button message;

  public CheckBox getCheckBox() {
    return checkBox;
  }

  public void setCheckBox(CheckBox checkBox) {
    this.checkBox = checkBox;
  }

  public Button getMessage() {
        return message;
    }

    public void setMessage(Button message) {
        this.message = message;
    }

  private String driver;
  private String rider;
  private String goingTo;
  private String comingFrom;
  private Date date;
  private Integer seats;
  private CheckBox checkBox;
  private int idnumber;

  /**
   * Constructor fo the class Ride.
   *
   * @param driver the driver
   * @param rider the rider
   * @param goingTo the destination
   * @param comingFrom the starting location
   * @param date the date of the ride
   * @param seats the number of seats in the car
   */
  public Ride(String driver, String rider, String goingTo, String comingFrom, Date date, int seats) {
    setDriver(driver);
    setRider(rider);
    setDest(goingTo);
    setStartP(comingFrom);
    setDate(date);
    setSeats(seats);
    setIdnumber(nextIDNumber++);
    this.message = new Button();

    this.message.setOnAction((ActionEvent event) -> {
      Globals.changeScene("messages/Messages.fxml");
      try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getClassLoader().getResource("messages/Messages.fxml"));
        MessagesController controller = loader.getController();
        ComboBox<String> comboBox = controller.getRecipient();
        comboBox.getSelectionModel().select(changeAndMessage(idnumber));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    this.checkBox = new CheckBox();
  }

  /**
   * An overloaded constructor for the class Ride.
   *
   * @param driver the driver
   * @param dest the destination
   * @param startP the starting location
   * @param date the data of the ride
   */
  public Ride(String driver, String dest, String startP, Date date) {
    this.driver = driver;
    this.goingTo = dest;
    this.date = date;
    this.comingFrom = startP;
    this.message = new Button();

    this.message.setOnAction((ActionEvent event) -> {
      Globals.changeScene("messages/Messages.fxml");
      try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getClassLoader().getResource("messages/Messages.fxml"));
        MessagesController controller = loader.getController();
        ComboBox<String> comboBox = controller.getRecipient();
        comboBox.getSelectionModel().select(changeAndMessage(idnumber));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    this.checkBox = new CheckBox();

  }

  String changeAndMessage(int p) throws SQLException {
    PastRide[] pastRides = new PastRide[p];
    try (Connection conn126 = DriverManager.getConnection(
            "jdbc:derby:lib/ZwischenDB");
            Statement stmt126 = conn126.createStatement();
            ResultSet resultSet126 = stmt126
                    .executeQuery(String.format("SELECT * FROM PAST_RIDE WHERE DRIVER = '%s' OR RIDER = '%s'",
                 getCurrentUser().getUsername(), getCurrentUser().getUsername()))) {

      //String query1 = "SELECT USERNAME FROM LOGIN WHERE UserName='"+ username+"';
      if (resultSet126.next()) {
        while (resultSet126.next()) {
          int i = 0;
          PastRide pastRide = new PastRide(resultSet126.getString("DRIVER"),
              resultSet126.getString("RIDER"),
              resultSet126.getString("GOINTTO"),
                  resultSet126.getString("COMINGFROM"),
              resultSet126.getDate("OCCURRANCE"));
          pastRides[i] = pastRide;
        }
      }
    }
    if (getCurrentUser().getUsername().equals(pastRides[p].getRider())) {
      return pastRides[p].getDriver();
    } else {
      return pastRides[p].getRider();
    }

  }
}
