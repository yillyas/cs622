package view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Model.Database;

import carRent.Account;
import carRent.BasicPlan;
import carRent.Booking;
import carRent.CarRent;
import carRent.Claim;
import carRent.IncorrectAccountException;
import carRent.InsurancePlan;
import carRent.Owner;
import carRent.PremiumPlan;
import carRent.Renter;
import carRent.StandardPlan;
import carRent.Vehicle;
import carRent.VehicleRepository;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainUI extends Application {
	private static HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
	private Account owner;
	private int bookingID;
	
	public void start(Stage stage) {
		// Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Welcome to RentMyCar");
        stage.show();
	}
	
	/*
	 * Creates an HBox with two buttons for the top region
	 */	    
    private HBox addHBox() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(50);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");

        Button winnerBtn = new Button("Last Months Winner!");        
        winnerBtn.setPrefSize(150, 20);
        winnerBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				Alert alert = getAlert("User Of The Month");
				alert.showAndWait();
			}
        });
        
        Button exitBtn = new Button("Exit");
        exitBtn.setPrefSize(100, 20);
        exitBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
        });
        hbox.getChildren().addAll(winnerBtn, exitBtn);
        
        return hbox;
    }
    
    private Alert getAlert(String title){
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		
		Account winnerAccount = CarRent.userOftheMonth(accounts.values());
		//Account winnerAccount = accounts.get(1);
		
		if (winnerAccount instanceof Owner ) {
			alert.setHeaderText("Winner is " + ((Owner)winnerAccount).getName() + "!!!");
			alert.setContentText("Added COUPON: " + CarRent.COUPON + " New Balance: " + ((Owner)winnerAccount).getBalance());
		}else {
			alert.setHeaderText("Winner is " + ((Renter)winnerAccount).getName() + "!!!");
			alert.setContentText("Winner is: " + ((Renter)winnerAccount).getName() + "Balance: " + ((Renter)winnerAccount).getBalance());
		}
		return alert;
    }

    /*
     * Creates a VBox with a list of links for the left region
     */
    private VBox addVBox() {
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Text title = new Text("Main Menu");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        
        Hyperlink createAccoutLink = new Hyperlink("Create Account");
        VBox.setMargin(createAccoutLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(createAccoutLink);
        createAccoutLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					createUserForm();
				}
        		});
        
        Hyperlink addVehicleLink = new Hyperlink("Add Vehicle");
        VBox.setMargin(addVehicleLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(addVehicleLink);
        addVehicleLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					addVehicleForm();
				}
        		});
        Hyperlink listVehicleLink = new Hyperlink("List Vehicle");
        VBox.setMargin(listVehicleLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(listVehicleLink);
        listVehicleLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					listForRentForm();
				}
        		});
        Hyperlink bookVehicleLink = new Hyperlink("Book Vehicle");
        VBox.setMargin(bookVehicleLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(bookVehicleLink);
        bookVehicleLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					bookVehicleMenu();
				}
        		});
        Hyperlink updateBookingLink = new Hyperlink("Update Booking");
        VBox.setMargin(updateBookingLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(updateBookingLink);
        updateBookingLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					updateBookingMenu();
				}
        		});
        Hyperlink cancelBookingLink = new Hyperlink("Cancel Booking");
        VBox.setMargin(cancelBookingLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(cancelBookingLink);
        cancelBookingLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					cancelBookingMenu();
				}
        		});
        Hyperlink fileClaimLink = new Hyperlink("File Claim");
        VBox.setMargin(fileClaimLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(fileClaimLink);
        fileClaimLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					fileClaimMenu();
				}
        		});
        Hyperlink rentalHistoryLink = new Hyperlink("Rental History");
        VBox.setMargin(rentalHistoryLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(rentalHistoryLink);
        rentalHistoryLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					showRentHistory();
				}
        		});
        Hyperlink accountBalanceLink = new Hyperlink("Account Information");
        VBox.setMargin(accountBalanceLink, new Insets(0, 0, 0, 8));
        vbox.getChildren().add(accountBalanceLink);
        accountBalanceLink.setOnAction(new EventHandler<ActionEvent>()
        		{
				@Override
				public void handle(ActionEvent event) {
					showAccountInformation();
				}
        		});
        return vbox;
    }
    
    public void createUserForm() {
	    		Stage stage = new Stage();
	    		GridPane gridPane = createRegistrationFormPane();
	    		Scene scene = new Scene(gridPane, 500, 500);
	    		stage.setScene(scene);
	    		stage.setTitle("User Registration Form");
    		
	    		// Add Header
            Label headerLabel = new Label("User Registration");
            headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gridPane.add(headerLabel, 0,0,2,1);
            GridPane.setHalignment(headerLabel, HPos.CENTER);
            GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

            // Add Name Label
            Label nameLabel = new Label("Full Name : ");
            gridPane.add(nameLabel, 0,1);

            // Add Name Text Field
            TextField nameField = new TextField();
            nameField.setPrefHeight(40);
            gridPane.add(nameField, 1,1);


            // Add State Label
            Label stateLabel = new Label("State : ");
            gridPane.add(stateLabel, 0, 2);

            // Add State Text Field
            TextField stateField = new TextField();
            stateField.setPrefHeight(40);
            gridPane.add(stateField, 1, 2);
            
            // Add City Label
            Label cityLabel = new Label("City : ");
            gridPane.add(cityLabel, 0, 3);

            // Add City Text Field
            TextField cityField = new TextField();
            cityField.setPrefHeight(40);
            gridPane.add(cityField, 1, 3);
            
            // Add Zip Label
            Label zipLabel = new Label("Zip : ");
            gridPane.add(zipLabel, 0, 4);

            // Add Zip Text Field
            TextField zipField = new TextField();
            zipField.setPrefHeight(40);
            gridPane.add(zipField, 1, 4);
            
            // Add Accout Label
            Label accountTypeLabel = new Label("Account Type : ");
            gridPane.add(accountTypeLabel, 0, 5);
            
            // create a toggle group 
            ToggleGroup tg = new ToggleGroup();
            // create radio buttons 
            RadioButton r1 = new RadioButton("Owner");
            RadioButton r2 = new RadioButton("Renter");
            // add radio buttons to toggle group 
            r1.setToggleGroup(tg); 
            r2.setToggleGroup(tg);
            HBox hbox = new HBox(r1, r2); // group the buttons on the UI
            gridPane.add(hbox, 1, 5);
            // Add Submit Button
            Button userInfoSubmit = new Button("Submit");
            userInfoSubmit.setPrefHeight(40);
            userInfoSubmit.setDefaultButton(true);
            userInfoSubmit.setPrefWidth(100);
            gridPane.add(userInfoSubmit, 0, 6, 2, 1);
            GridPane.setHalignment(userInfoSubmit, HPos.CENTER);
            GridPane.setMargin(userInfoSubmit, new Insets(20, 0,20,0));
            Label outputLabel = new Label();
            gridPane.add(outputLabel, 1, 7);
            userInfoSubmit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Account account;
					boolean accountType = tg.getSelectedToggle().toString().contains("Owner");
					String name = nameField.getText();
					String state = stateField.getText();
					String city = cityField.getText();
					int zipCode = Integer.parseInt(zipField.getText());
					if (accountType) {
						account = new Owner(name, state, city, zipCode);
						accounts.put(((Owner)account).getId(), account);
						Thread thread = new Thread(new Runnable() { // anonymous thread to write to database
							     @Override
								 public void run() {
							    	    // this will be run in a separate thread
									boolean inserted = Database.insertAccount(account); // insert user to database
									if (inserted) {
						    	    			Platform.runLater(()->{
						    	    				outputLabel.setText("Thank you! " + name + " Your ID is: " + ((Owner)account).getId());
						    	    			});
						    	    		} else {
						    	    			Platform.runLater(()->{
											outputLabel.setText("An Error Occured " + name +  " , Please try again later.");
										});
						    	    		}
								}
							  });
						thread.start();
						try {
							thread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						account = new Renter(name, state, city, zipCode);
						accounts.put(((Renter)account).getId(), account);
						Thread thread = new Thread(new Runnable() { // anonymous thread to write to database
						     @Override
							 public void run() {
					    	    		boolean inserted = Database.insertAccount(account); // insert user to database
					    	    		if (inserted) {
					    	    			Platform.runLater(()->{
					    	    				outputLabel.setText("Thank you! " + name + " Your ID is: " + ((Renter)account).getId());
					    	    			});
					    	    		} else {
					    	    			Platform.runLater(()->{
					    	    				outputLabel.setText("An Error Occured " + name +  " , Please try again later.");
									});
					    	    		}
							}
						  });
						thread.start();
						try {
							thread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println(account);
				}
            });
    		stage.show();
    }
    
    public void bookVehicleMenu() {
    		Stage stage = new Stage();
		stage.setTitle("Book Vehivle");
		Label label = new Label("Book Vehicle");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties

		TableView<Vehicle> table = new TableView<>();
        table.setEditable(true);
        	
        TableColumn ownerCol = new TableColumn("Vehicle ID");
        ownerCol.setCellValueFactory(new PropertyValueFactory("vehicleID"));
        TableColumn makeCol = new TableColumn("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory("make"));
        TableColumn modelCol = new TableColumn("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory("model"));
        TableColumn rentCol = new TableColumn("Rent");
        rentCol.setCellValueFactory(new PropertyValueFactory("rent"));
        TableColumn startDateCol = new TableColumn("Bkng StartDate");
        startDateCol.setCellValueFactory(new PropertyValueFactory("bkngStartDate"));
        startDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		Vehicle vehicle = (Vehicle) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((Vehicle) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setBkngStartDate((String)t.getNewValue());
                System.out.println(vehicle.getBkngStartDate());
            }
        });
        
        TableColumn endDateCol = new TableColumn("Bkng EndDate");
        endDateCol.setCellValueFactory(new PropertyValueFactory("bkngEndDate"));
        endDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        endDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		Vehicle vehicle = (Vehicle) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((Vehicle) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setBkngEndDate((String)t.getNewValue());
                System.out.println(vehicle.getBkngEndDate());
            }
        });
        
        table.getColumns().setAll(ownerCol, makeCol, modelCol, rentCol, startDateCol, endDateCol);
        table.setPrefWidth(600);
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        // Add id field
        TextField renterIDField = new TextField();
        renterIDField.setPromptText("Renter ID");
        renterIDField.setPrefWidth(80);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Zip Code");
        searchField.setPrefWidth(80);
        
        Button searchBtn = new Button("Search");
        searchBtn.setPrefWidth(80);
       
        HBox searchHb = new HBox();
        searchHb.setAlignment(Pos.CENTER);
        searchHb.getChildren().addAll(renterIDField, searchField, searchBtn);
        searchHb.setSpacing(10);
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int renterID = Integer.parseInt(renterIDField.getText());
				owner = accounts.get(renterID);
				if (owner == null) {
					System.out.println("ID not found, please try again.");
					outputLabel.setText("ID not found, please try again.");
				}
				int zipCode = Integer.parseInt(searchField.getText());
				System.out.println(zipCode);
				LinkedList<Vehicle> vehiclesByZip = Account.searchVehicle(zipCode);
				ObservableList<Vehicle> data = FXCollections.observableList(vehiclesByZip);
		        table.setItems(data);
			}
        });
        // Add Book button
        Button bookBtn = new Button("Book");
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().add(bookBtn);
        bookBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Vehicle vehicle = (Vehicle) table.getSelectionModel().getSelectedItem();
				System.out.println(vehicle);
				try {
					Booking booking = Account.newbooking(vehicle, vehicle.getOwner(), owner, vehicle.getBkngStartDate(), vehicle.getBkngEndDate());
					System.out.println(booking);
					Thread thread = new Thread(new Runnable() { // anonymous thread to write to database
					     @Override
						 public void run() {
					    	    // this will be run in a separate thread
					    	 	boolean added = Database.insertBooking(booking); // Add booking to Database
							if (added) {
				    	    			Platform.runLater(()->{
				    	    				System.out.println("Inserted Booking in Databse, bookingID: " + booking.getBookingID());
				    	    				outputLabel.setText("You have booked the Vehicle from: " + booking.getStartDate() + " To: "  + booking.getEndDate());
				    	    			});
				    	    		} else {
				    	    			Platform.runLater(()->{
									outputLabel.setText("An Error Occured, Please try later.");
								});
				    	    		}
						}
					  });
					thread.start();
					thread.join();
				} catch (IncorrectAccountException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        });
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,searchHb, table,buttonHb, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
    		
    }

    public void updateBookingMenu() {
    		Stage stage = new Stage();
		stage.setTitle("Update Booking");
		Label label = new Label("Update Booking");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties

		TableView<BookingView> table = new TableView<>();
        table.setEditable(true);
        	
        TableColumn bkingIDCol = new TableColumn("Booking ID");
        bkingIDCol.setCellValueFactory(new PropertyValueFactory("bookingID"));
        TableColumn vehicleIDCol = new TableColumn("Vehicle ID");
        vehicleIDCol.setCellValueFactory(new PropertyValueFactory("vehicleID"));
        TableColumn makeCol = new TableColumn("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory("make"));
        TableColumn modelCol = new TableColumn("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory("model"));
        TableColumn rentCol = new TableColumn("Rent");
        rentCol.setCellValueFactory(new PropertyValueFactory("rent"));
        TableColumn startDateCol = new TableColumn("Bkng StartDate");
        startDateCol.setCellValueFactory(new PropertyValueFactory("bkngStartDate"));
        startDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		BookingView bm = (BookingView) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((BookingView) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setBkngStartDate((String)t.getNewValue());
                System.out.println(bm.getBkngStartDate());
            }
        });
        
        TableColumn endDateCol = new TableColumn("Bkng EndDate");
        endDateCol.setCellValueFactory(new PropertyValueFactory("bkngEndDate"));
        endDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        endDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		BookingView bm = (BookingView)t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((BookingView) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setBkngEndDate((String)t.getNewValue());
                System.out.println(bm.getBkngEndDate());
            }
        });
        
        table.getColumns().setAll(bkingIDCol,vehicleIDCol, makeCol, modelCol, rentCol, startDateCol, endDateCol);
        table.setPrefWidth(600);
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        // Add id field
        TextField bookingIDField = new TextField();
        bookingIDField.setPromptText("Booking ID");
        bookingIDField.setPrefWidth(90);
        
        Button searchBtn = new Button("Search");
        searchBtn.setPrefWidth(90);
       
        HBox searchHb = new HBox();
        searchHb.setAlignment(Pos.CENTER);
        searchHb.getChildren().addAll( bookingIDField, searchBtn);
        searchHb.setSpacing(10);
        
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				bookingID = Integer.parseInt(bookingIDField.getText());
				Thread thread = new Thread(new Runnable() { // anonymous thread to select booking from database
				     @Override
					 public void run() {
				    	    // this will be run in a separate thread
				    	 	Map<String, Object> bookingInfo = Database.selectBooking(bookingID); // read from Database
						if (bookingInfo != null) {
			    	    			Platform.runLater(()->{
			    	    				int vehicleID = (int) bookingInfo.get("VEHICLEID");
		    						String bkngStartDate = (String) bookingInfo.get("STARTDATE");
		    						String bkngEndDate = (String) bookingInfo.get("ENDDATE");
		    						Map<String, Object> vehicleInfo = Database.selectVehicle((int)(bookingInfo.get("VEHICLEID")));
		    						String make = (String)vehicleInfo.get("MAKE");
		    						String model = (String)vehicleInfo.get("MODEL");
		    						int rent = (int)vehicleInfo.get("RENT");
		    						
		    						LinkedList<BookingView> output =new LinkedList<>();
		    						BookingView bv = new BookingView(bookingID,vehicleID,make,model,rent,bkngStartDate,bkngEndDate);
		    						output.add(bv);
		    						ObservableList<BookingView> data = FXCollections.observableList(output);
		    				        table.setItems(data);
			    	    			});
			    	    		} else {
			    	    			Platform.runLater(()->{
			    	    				System.out.println("Booking ID not found, please try again.");
								outputLabel.setText("Booking ID not found, please try again.");
							});
			    	    		}
					}
				  });
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        });
        
        // Add Book button
        Button updateBtn = new Button("Update");
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().add(updateBtn);
        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				BookingView booking = table.getSelectionModel().getSelectedItem();
				if (booking != null) {
					Thread thread = new Thread(new Runnable() { // anonymous thread to write to database
					     @Override
						 public void run() {
					    	    // this will be run in a separate thread
					    	 	boolean updated = Database.updateBooking(booking.getBookingID(), booking.getBkngStartDate(), booking.getBkngEndDate()); // update row in Database
							if (updated) {
				    	    			Platform.runLater(()->{
				    	    				System.out.println("Booking is updated.");
				    	    				outputLabel.setText("Updated Booking, Start Date: " + booking.getBkngStartDate() + " End Date: " + booking.getBkngEndDate());
				    	    			});
				    	    		} else {
				    	    			Platform.runLater(()->{
									outputLabel.setText("An Error Occured, Please try later.");
								});
				    	    		}
						}
					  });
					thread.start();
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("No booking is selected.");
					outputLabel.setText("Please select a booking first.");
				}
			}
        });
        
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
 
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,searchHb, table,buttonHb, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
    	
    }
    
    public void cancelBookingMenu() {
    		Stage stage = new Stage();
		stage.setTitle("Cancel Booking");
		Label label = new Label("Cancel Booking");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties
		TableView<BookingView> table = new TableView<>();
        table.setEditable(true);
        	
        TableColumn bkingIDCol = new TableColumn("Booking ID");
        bkingIDCol.setCellValueFactory(new PropertyValueFactory("bookingID"));
        TableColumn vehicleIDCol = new TableColumn("Vehicle ID");
        vehicleIDCol.setCellValueFactory(new PropertyValueFactory("vehicleID"));
        TableColumn makeCol = new TableColumn("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory("make"));
        TableColumn modelCol = new TableColumn("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory("model"));
        TableColumn rentCol = new TableColumn("Rent");
        rentCol.setCellValueFactory(new PropertyValueFactory("rent"));
        TableColumn startDateCol = new TableColumn("Bkng StartDate");
        startDateCol.setCellValueFactory(new PropertyValueFactory("bkngStartDate"));
        startDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		Vehicle vehicle = (Vehicle) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((Vehicle) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setBkngStartDate((String)t.getNewValue());
                System.out.println(vehicle.getBkngStartDate());
            }
        });
        
        TableColumn endDateCol = new TableColumn("Bkng EndDate");
        endDateCol.setCellValueFactory(new PropertyValueFactory("bkngEndDate"));
        endDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        endDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		Vehicle vehicle = (Vehicle) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((Vehicle) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setBkngEndDate((String)t.getNewValue());
                System.out.println(vehicle.getBkngEndDate());
            }
        });
        
        table.getColumns().setAll(bkingIDCol,vehicleIDCol, makeCol, modelCol, rentCol, startDateCol, endDateCol);
        table.setPrefWidth(600);
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        // Add id field
        TextField bookingIDField = new TextField();
        bookingIDField.setPromptText("Booking ID");
        bookingIDField.setPrefWidth(90);
        
        Button searchBtn = new Button("Search");
        searchBtn.setPrefWidth(90);
       
        HBox searchHb = new HBox();
        searchHb.setAlignment(Pos.CENTER);
        searchHb.getChildren().addAll( bookingIDField, searchBtn);
        searchHb.setSpacing(10);
        
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				bookingID = Integer.parseInt(bookingIDField.getText());
				Thread thread = new Thread(new Runnable() { // anonymous thread to select booking from database
				     @Override
					 public void run() {
				    	    // this will be run in a separate thread
				    	 	Map<String, Object> bookingInfo = Database.selectBooking(bookingID); // read from Database
						if (bookingInfo != null) {
			    	    			Platform.runLater(()->{
			    	    				int vehicleID = (int) bookingInfo.get("VEHICLEID");
		    						String bkngStartDate = (String) bookingInfo.get("STARTDATE");
		    						String bkngEndDate = (String) bookingInfo.get("ENDDATE");
		    						Map<String, Object> vehicleInfo = Database.selectVehicle((int)(bookingInfo.get("VEHICLEID")));
		    						String make = (String)vehicleInfo.get("MAKE");
		    						String model = (String)vehicleInfo.get("MODEL");
		    						int rent = (int)vehicleInfo.get("RENT");
		    						
		    						LinkedList<BookingView> output =new LinkedList<>();
		    						BookingView bv = new BookingView(bookingID,vehicleID,make,model,rent,bkngStartDate,bkngEndDate);
		    						output.add(bv);
		    						ObservableList<BookingView> data = FXCollections.observableList(output);
		    				        table.setItems(data);
			    	    			});
			    	    		} else {
			    	    			Platform.runLater(()->{
			    	    				System.out.println("Booking ID not found, please try again.");
								outputLabel.setText("Booking ID not found, please try again.");
							});
			    	    		}
					}
				  });
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        });
        // Add Book button
        Button cancelBtn = new Button("Cancel");
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().add(cancelBtn);
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Booking booking = Account.showBookingInformation(bookingID); // reading the object from the file
				System.out.println(booking);
				if (booking != null) {
					Account.cancelBooking(booking);
					Thread thread = new Thread(new Runnable() { // anonymous thread to write to database
					     @Override
						 public void run() {
					    	    // this will be run in a separate thread
					    	 	boolean deleted = Database.deleteBooking(booking); // Delete from Database
							if (deleted) {
				    	    			Platform.runLater(()->{
				    	    				System.out.println("Booking is deleted.");
				    	    				outputLabel.setText("Booking is cancled and your account balance is updated");
				    	    			});
				    	    		} else {
				    	    			Platform.runLater(()->{
									outputLabel.setText("An Error Occured, Please try later.");
								});
				    	    		}
						}
					  });
					thread.start();
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("Booking ID not found, please try again.");
					outputLabel.setText("Booking ID not found, please try again.");
				}
			}
        });
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
 
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,searchHb, table,buttonHb, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
    }
   
    public void addVehicleForm() {
		Stage stage = new Stage();
		GridPane gridPane = createRegistrationFormPane();
		Scene scene = new Scene(gridPane, 500, 500);
		stage.setScene(scene);
		stage.setTitle("Add Vehicle");
		// Add Header
        Label headerLabel = new Label("Add Vehicle");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Owner ID
        Label ownerIDLabel = new Label("Owner ID : ");
        gridPane.add(ownerIDLabel, 0,1);

        TextField ownerIDField = new TextField();
        ownerIDField.setPrefHeight(40);
        gridPane.add(ownerIDField, 1,1);
        // Add Make
        Label makeLabel = new Label("Make : ");
        gridPane.add(makeLabel, 0, 2);

        TextField makeField = new TextField();
        makeField.setPrefHeight(40);
        gridPane.add(makeField, 1, 2);
        // Add Model
        Label modelLabel = new Label("Model : ");
        gridPane.add(modelLabel, 0, 3);

        TextField modelField = new TextField();
        modelField.setPrefHeight(40);
        gridPane.add(modelField, 1, 3);
        // Add Year
        Label yearLabel = new Label("Year : ");
        gridPane.add(yearLabel, 0, 4);

        TextField yearField = new TextField();
        yearField.setPrefHeight(40);
        gridPane.add(yearField, 1, 4);
        // Add Rent
        Label rentLabel = new Label("Rent : ");
        gridPane.add(rentLabel, 0, 5);

        TextField rentField = new TextField();
        rentField.setPrefHeight(40);
        gridPane.add(rentField, 1, 5);
        // Add Zip 
        Label zipLabel = new Label("Zip : ");
        gridPane.add(zipLabel, 0, 6);

        TextField zipField = new TextField();
        zipField.setPrefHeight(40);
        gridPane.add(zipField, 1, 6);
        // Select Insurance Plan
        Label insuranceTypeLabel = new Label("Insurance : ");
        gridPane.add(insuranceTypeLabel, 0, 7);
        // create a toggle group 
        ToggleGroup tg = new ToggleGroup();
        // create radio buttons 
        RadioButton r1 = new RadioButton("Basic");
        RadioButton r2 = new RadioButton("Standard");
        RadioButton r3 = new RadioButton("Premium");
        // add radio buttons to toggle group 
        r1.setToggleGroup(tg); 
        r2.setToggleGroup(tg);
        r3.setToggleGroup(tg);
        HBox hbox = new HBox(r1, r2, r3); // group the buttons on the UI
        gridPane.add(hbox, 1, 7);
        // Add Submit Button
        Button userInfoSubmit = new Button("Submit");
        userInfoSubmit.setPrefHeight(40);
        userInfoSubmit.setDefaultButton(true);
        userInfoSubmit.setPrefWidth(100);
        gridPane.add(userInfoSubmit, 0, 8, 2, 1);
        GridPane.setHalignment(userInfoSubmit, HPos.CENTER);
        GridPane.setMargin(userInfoSubmit, new Insets(20, 0,20,0));
        // Add Output Text Field
        Label outputLabel = new Label();
        gridPane.add(outputLabel, 1, 9);
        userInfoSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int id = Integer.parseInt(ownerIDField.getText());
				Owner account = (Owner)(accounts.get(id));
				String make = makeField.getText();
				String model = modelField.getText();
				int year = Integer.parseInt(yearField.getText());
				int rent = Integer.parseInt(rentField.getText());
				int zipCode = Integer.parseInt(zipField.getText());
				InsurancePlan insuranceplan;
				if (tg.getSelectedToggle().toString().contains("Basic")) {
					insuranceplan = new BasicPlan();
				}
				else if (tg.getSelectedToggle().toString().contains("Standard")) {
					insuranceplan = new StandardPlan();
				}else {
					insuranceplan = new PremiumPlan();
				}
				Vehicle v = new Vehicle((Owner)account, make, year, model, insuranceplan, rent, zipCode);
				((Owner)account).addVehicle(v);
				Thread thread = new Thread(new Runnable() { // anonymous thread to write to database
				     @Override
					 public void run() {
				    	    // this will be run in a separate thread
				    	 	boolean added = Database.insertVehicle(v); // insert vehicle to database
						if (added) {
			    	    			Platform.runLater(()->{
			    	    				System.out.println("A Vehicle has been added to your account.");
			    	    				System.out.println(v);
			    	    				outputLabel.setText( account.getName() + " Added Vehicle, the vehicle ID: " + v.getVehicleID());
			    	    			});
			    	    		} else {
			    	    			Platform.runLater(()->{
								outputLabel.setText("An Error Occured, Please try later.");
							});
			    	    		}
					}
				  });
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        });
		stage.show();
    	}

    public void listForRentForm() {
    		Stage stage = new Stage();
		stage.setTitle("List For Rent");
		Label label = new Label("List Vehicle");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties

        TableView table = new TableView();
        table.setEditable(true);
        	
        TableColumn idCol = new TableColumn("VehicleID");
        idCol.setCellValueFactory(new PropertyValueFactory("vehicleID"));
        TableColumn makeCol = new TableColumn("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory("make"));
        TableColumn modelCol = new TableColumn("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory("model"));
        TableColumn rentCol = new TableColumn("Rent");
        rentCol.setCellValueFactory(new PropertyValueFactory("rent"));
        TableColumn startDateCol = new TableColumn("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory("lstngStartDate"));
        startDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        startDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		Vehicle vehicle = (Vehicle) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((Vehicle) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setStartDate((String)t.getNewValue());
                System.out.println(vehicle.getStartDate());
            }
        });
        TableColumn endDateCol = new TableColumn("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory("lstngEndDate"));
        endDateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        endDateCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
            @Override
            public void handle(CellEditEvent t) {
            		Vehicle vehicle = (Vehicle) t.getTableView().getItems().get(t.getTablePosition().getRow());
                ((Vehicle) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                ).setEndDate((String)t.getNewValue());
                System.out.println(vehicle.getEndDate());
            }
        });
        table.getColumns().setAll(idCol, makeCol, modelCol, rentCol, startDateCol, endDateCol);
        table.setPrefWidth(600);
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        TextField ownerIDField = new TextField();
        ownerIDField.setPromptText("Owner ID");
        ownerIDField.setPrefWidth(80);
        
        Button showBtn = new Button("Show Vehicles");
        showBtn.setPrefWidth(150);
       
        HBox showHb = new HBox();
        showHb.setAlignment(Pos.CENTER);
        showHb.getChildren().addAll(ownerIDField, showBtn);
        showHb.setSpacing(10);
        showBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int ownerID = Integer.parseInt(ownerIDField.getText());
				owner = accounts.get(ownerID);
				System.out.println(owner);
				if (owner == null) {
					System.out.println("ID not found, please try again.");
					outputLabel.setText("ID not found, please try again.");
				} 
				else {
					LinkedList<Vehicle>  vehicles = ((Owner)owner).getVehicle();
			        ObservableList<Vehicle> data = FXCollections.observableList(vehicles);
			        table.setItems(data);
				}
			}
        });
        
        // Add list button
        Button listbtn = new Button("List");
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().add(listbtn);
        listbtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Vehicle vehicle = (Vehicle) table.getSelectionModel().getSelectedItem();
				if (vehicle.getStartDate() != null && vehicle.getEndDate() != null) {
					boolean listed = ((Owner)owner).listVehicleforRent(vehicle, vehicle.getStartDate(), vehicle.getEndDate());
					if (listed) {
						Thread thread = new Thread(new Runnable() { // anonymous thread to update a row in database
						     @Override
							 public void run() {
						    	    // this will be run in a separate thread
						    	 	boolean added = Database.updateVehicle(vehicle); // insert vehicle to database
								if (added) {
					    	    			Platform.runLater(()->{
					    	    				System.out.println("Updated vehicle information in Database.");
					    	    				System.out.println(vehicle);
					    	    				outputLabel.setText("Vehicle is Listed for Rent");
					    	    			});
					    	    		} else {
					    	    			Platform.runLater(()->{
										outputLabel.setText("An Error Occured, Please try later.");
									});
					    	    		}
							}
						  });
						thread.start();
						try {
							thread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} 
					else {
						outputLabel.setText("Please select the start and end dates to list this vehicle.");
					}
				}
			 }
        });
        
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
 
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,showHb, table,buttonHb, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
 
        // Select the first row
        table.getSelectionModel().select(0);
    }
    
    public void fileClaimMenu() {
    		Stage stage = new Stage();
		stage.setTitle("Claims Menu");
		Label label = new Label("File Claim");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties

        @SuppressWarnings("rawtypes")
		TableView table = new TableView();
        	
        TableColumn claimIDCol = new TableColumn("Claim ID");
        claimIDCol.setCellValueFactory(new PropertyValueFactory("claimID"));
        TableColumn claimAmountCol = new TableColumn("Claimed Amount");
        claimAmountCol.setCellValueFactory(new PropertyValueFactory("claimedAmount"));
        TableColumn claimStatusCol = new TableColumn("Resolved");
        claimStatusCol.setCellValueFactory(new PropertyValueFactory("resloved"));
        
        table.getColumns().setAll(claimIDCol, claimAmountCol, claimStatusCol);
        table.setPrefWidth(400);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        // Add id field
        TextField bookingIDField = new TextField();
        bookingIDField.setPromptText("Booking ID");
        bookingIDField.setPrefWidth(90);
        
        TextField accountIDField = new TextField();
        accountIDField.setPromptText("Account ID");
        accountIDField.setPrefWidth(90);
        
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setPrefWidth(90);
        
        Button fileBtn = new Button("File");
        fileBtn.setPrefWidth(90);
       
        HBox searchHb = new HBox();
        searchHb.setAlignment(Pos.CENTER);
        searchHb.getChildren().addAll(accountIDField, bookingIDField, amountField, fileBtn);
        searchHb.setSpacing(10);
        
        fileBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Claim<Account, Booking>> list = new ArrayList<>();
				int accountID = Integer.parseInt(accountIDField.getText());
				int bookingID = Integer.parseInt(bookingIDField.getText());
				double claimedAmount = Double.parseDouble(amountField.getText());
				Account account = accounts.get(accountID);
				Booking booking = VehicleRepository.trackBooking.get(bookingID);
				if (booking != null) {
					Claim<Account, Booking> claim = account.fileClaim(bookingID, claimedAmount);
					System.out.println(claim);
					list.add(claim);
					ObservableList<Claim<Account, Booking>> data = FXCollections.observableList(list);
					table.setItems(data);
					outputLabel.setText(claim.getClaimedAmount() + " Is deposited to your Account, current balance : " + account.getBalance());
				}
			}
        });
        
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
 
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,searchHb, table, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
    	
    }

    public void showRentHistory() {
    		Stage stage = new Stage();
		stage.setTitle("Rent History");
		Label label = new Label("Vehicles Booked In Past");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties

		TableView<Vehicle> table = new TableView<>();
        	
        TableColumn vehicleIDCol = new TableColumn("Vehicle ID");
        vehicleIDCol.setCellValueFactory(new PropertyValueFactory("vehicleID"));
        TableColumn makeCol = new TableColumn("Vehicle Make");
        makeCol.setCellValueFactory(new PropertyValueFactory("make"));
        TableColumn modelCol = new TableColumn("Vehicle Model");
        modelCol.setCellValueFactory(new PropertyValueFactory("model"));
        TableColumn bkngStartCol = new TableColumn("Bkng StartDate");
        bkngStartCol.setCellValueFactory(new PropertyValueFactory("bkngStartDate"));
        TableColumn bkngEndCol = new TableColumn("Bkng EndDate");
        bkngEndCol.setCellValueFactory(new PropertyValueFactory("bkngEndDate"));
        TableColumn rentCol = new TableColumn("Rent/Day");
        rentCol.setCellValueFactory(new PropertyValueFactory("rent"));
        
        
        table.getColumns().setAll(vehicleIDCol, makeCol, modelCol, bkngStartCol, bkngEndCol, rentCol);
        table.setPrefWidth(500);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        // Add id field
        TextField accountIDField = new TextField();
        accountIDField.setPromptText("Account ID");
        accountIDField.setPrefWidth(90);
        
        Button historyBtn = new Button("Show History");
        historyBtn.setPrefWidth(100);
       
        HBox searchHb = new HBox();
        searchHb.setAlignment(Pos.CENTER);
        searchHb.getChildren().addAll(accountIDField, historyBtn);
        searchHb.setSpacing(10);
        
        historyBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				int accountID = Integer.parseInt(accountIDField.getText());
				Account account = accounts.get(accountID);
				if (account != null) {
					LinkedList<Vehicle> vehicleHistory = account.getUserRentHistory();
					System.out.println(vehicleHistory);
					ObservableList<Vehicle> data = FXCollections.observableList(vehicleHistory);
					table.setItems(data);
					outputLabel.setText("Showing History of Account ID: " + accountID);
				}
			}
        	
        });
        
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
 
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,searchHb, table, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
    }

    public void showAccountInformation() {
    		Stage stage = new Stage();
		stage.setTitle("Account Information");
		Label label = new Label("User Account Information");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(label);
        
		// Table view, data, columns and properties

		TableView<Account> table = new TableView<>();
        
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn stateCol = new TableColumn("State");
        stateCol.setCellValueFactory(new PropertyValueFactory("state"));
        TableColumn cityCol = new TableColumn("City");
        cityCol.setCellValueFactory(new PropertyValueFactory("city"));
        TableColumn zipCol = new TableColumn("Zip Code");
        zipCol.setCellValueFactory(new PropertyValueFactory("zipCode"));
        
        table.getColumns().setAll(nameCol, stateCol, cityCol, zipCol);
        table.setPrefWidth(500);
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // add a label to show output
        Label outputLabel = new Label();
        HBox labelHb = new HBox(10);
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(outputLabel);
        
        // Add id field
        TextField accountIDField = new TextField();
        accountIDField.setPromptText("Account ID");
        accountIDField.setPrefWidth(90);
        
        Button infoBtn = new Button("Show Info");
        infoBtn.setPrefWidth(100);
       
        HBox searchHb = new HBox();
        searchHb.setAlignment(Pos.CENTER);
        searchHb.getChildren().addAll(accountIDField, infoBtn);
        searchHb.setSpacing(10);
        
        infoBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				int accountID = Integer.parseInt(accountIDField.getText());
				Account account = accounts.get(accountID);
				if (account != null) {
					List<Account> list = new ArrayList<>();
					list.add(account);
					System.out.println(account);
					ObservableList<Account> data = FXCollections.observableList(list);
					table.setItems(data);
					outputLabel.setText("Showing the information of Account ID: " + accountID);
				}
			}
        	
        });
        
        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFill(Color.FIREBRICK);
 
        // Vbox
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(hb,searchHb, table, labelHb, actionStatus);
 
        // Scene
        Scene scene = new Scene(vbox, 700, 550); // w x h
        stage.setScene(scene);
        stage.show();
    }
   
    private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        
        return gridPane;
    }
   
    public static void main(String[] args) {
    		///*
    	     Thread databaseSetup = new Database();
    	     databaseSetup.start(); // Create Database Tables in a separate thread
    		 try {
    			 	System.out.println("Setting up the database.");
    			 	databaseSetup.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		//*/
		launch(MainUI.class, args);
	}
}
