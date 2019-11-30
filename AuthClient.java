/*
  Authentication CORBA service - client
  Compile: javac AuthClient.java
  Run: java -Djava.net.preferIPv4Stack=true AuthClient -ORBInitialPort xxxx
*/
import ModAuthFIT.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.util.Scanner;

public class AuthClient
{
  static AuthFIT authImpl;

  public static void main(String args[])
    {
      try{
        // create and initialize the ORBInitialPort
	       ORB orb = ORB.init(args, null);

        // get the root naming context
        org.omg.CORBA.Object objRef = 
	       orb.resolve_initial_references("NameService");
        // Use NamingContextExt instead of NamingContext. This is 
        // part of the Interoperable naming Service.  
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
        // resolve the Object Reference in Naming
        String name = "AuthFIT";
        authImpl = AuthFITHelper.narrow(ncRef.resolve_str(name));
        System.out.println("Obtained a handle on server object: " + authImpl);



        //--------------
        boolean authorized = false;

        String username = "";
        String password = "";

        //--------------Begin login

        Scanner sc = new Scanner(System.in);
        while (authorized == false){
          System.out.println("Username?");
          username = sc.next();
          System.out.println("Password?");
          password = sc.next();
          //--------------Consult the server to see if the username/password combination is correct
          authorized = authImpl.authUser(username,password);
        }

        System.out.println("1. Add user\n2. Remove user\n3. Change password\n0. Exit\n");
        
        int choice;

        String temp1;
        String temp2;
        choice = sc.nextInt();

        while (choice != 0){

          if (choice == 1){

            //--------------Add a new user to the "database"

            System.out.println("Username for new user?");
            temp1 = sc.next();
            System.out.println("Password for new user?");
            temp2 = sc.next();

            //--------------There can't be two users with the same username. Warn client if necessary.
            if (authImpl.addUser(temp1, temp2) == false) System.out.println("User already exists");
            else System.out.println("Successful");
          }
          if (choice == 2){

            //--------------Deletes the client's user
            System.out.println("Password for this user?");
            temp1 = sc.next();

            //--------------Return error if the password entered is incorrect
            if (authImpl.deleteUser(username, temp1) == false) System.out.println("Error deleting user.");
            else{
              System.out.println("Successfully deleted user");
              //--------------Must log out, as the client deleted his user as is now in a dead account.
              choice = 0;
              continue; 
            }
          }
          if (choice == 3){

            //--------------Change the user's password

            System.out.println("Password for this user?");
            temp1 = sc.next();
            System.out.println("New password for this user?");
            temp2 = sc.next();

            //--------------Checks if the password entered is correct, warns client if it isn't.
            if (authImpl.changePassword(username, temp1, temp2) == false) System.out.println("Error changing password.");
            else System.out.println("Successfully changed password");
          }

          //--------------Return to main menu
          System.out.println("1. Add user\n2. Remove user\n3. Change password\n0. Exit");
          choice = sc.nextInt();
        }
        //--------------

	   } catch (Exception e) {
        System.out.println("ERROR : " + e) ;
	       e.printStackTrace(System.out);
	   }
    }

}
