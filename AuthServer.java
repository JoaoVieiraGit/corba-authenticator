/*
  
  Compile: javac AuthServer.java Run: tnameserv -J-Djava.net.preferIPv4Stack=true -ORBInitialPort xxxx & java -Djava.net.preferIPv4Stack=true AuthServer -ORBInitialPort xxxx
*/
import ModAuthFIT.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.io.*;
import java.util.*;

import java.util.Properties;

class authImpl extends AuthFITPOA{

    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    // implement authUser() method
    public boolean authUser(String login, String password) {

        //--------------
        String temp[] = new String[2];
        String line = "";

        //--------------Gets the list of users and passwords from a text file

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./Credentials"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------Separates the users and passwords and stores them in an array
        while (line != null){
            temp = line.split("\\|");
            usernames.add(temp[0]);
            passwords.add(temp[1]);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------

        //--------------Checks if the user/password combination exists

        for(int i = 0; i < usernames.size(); i++){
            if (usernames.get(i).equals(login) && passwords.get(i).equals(password)){
                return true;
            }
        }
        return false;
    }

    // implement addUser() method
    public boolean addUser(String login, String password) {

        //--------------Adds a new user to the "database"

        String temp[] = new String[2];
        String line = "";

        //--------------Gets the list of users and passwords from a text file
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./Credentials"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------Separates the users and passwords and stores them in an array
        while (line != null){
            temp = line.split("\\|");
            usernames.add(temp[0]);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------

        //--------------Return false if there is already a user with that username

        for(int i = 0; i < usernames.size(); i++){
            if (usernames.get(i).equals(login)){
                return false;
            }
        }

        //--------------Adds the new user at the end of the list of usernames/passwords

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("./Credentials", true));
            writer.write("\n" + login + "|" + password);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }

    // implement shutdown() method
    public void shutdown() {
        orb.shutdown(false);
    }

    // implement getUsers() method
/*  public String[] getUsers() {

    String [] temp = new String[2];
    String line;

    BufferedReader reader = new BufferedReader(new FileReader("Credentials.txt"));
    ArrayList<String> usernames = new ArrayList<String>();

    line = reader.readLine();

    while (line != null){
      temp = line.split("|");
      usernames.add(temp[0]);
      line = reader.readLine();
    }

    reader.close();

    //--------------

    String[] username_list;
    for(int i = 0; i < usernames.size(); i++){
      Arrays.add(username_list, usernames.get(i));
    }
    return username_list;
  }

  */

    public boolean changePassword(String login, String password, String new_password){
        //--------------Allows the user to change his own password
        String temp[] = new String[2];
        String line = "";

        //--------------Gets the list of users and password from file

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./Credentials"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------Stores the usernames and passwords in an array

        while (line != null){
            temp = line.split("\\|");
            usernames.add(temp[0]);
            passwords.add(temp[1]);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //--------------

        //--------------Creates a temporary file for storing the new list of usernames and password
        File temporary = new File("./Credentialstemp");
        File original = new File("./Credentials");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("./Credentialstemp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------Writes the new list (with password changed) in the temporary file
        for(int i = 0; i < usernames.size(); i++){
            System.out.println("i: " + usernames.get(i) + " pass: " + passwords.get(i));
            System.out.println("user: " + login + "password: " + password);
            if (usernames.get(i).equals(login) && passwords.get(i).equals(password)){
                for (int j = 0; j < usernames.size(); j++){
                    if (j != i){
                        try {
                            writer.write(usernames.get(j) + "|" + passwords.get(j));
                            if (j != usernames.size() - 1) writer.write("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                    }
                    else{
                        try {
                            writer.write(usernames.get(j) + "|" + new_password);
                            if (j != usernames.size() - 1) writer.write("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                    }
                }
                //--------------Changes the temporary file into the original
                temporary.renameTo(original);
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteUser(String login, String password){
        String temp[] = new String[2];
        String line = "";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./Credentials"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (line != null){
            temp = line.split("\\|");
            usernames.add(temp[0]);
            passwords.add(temp[1]);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        

        //--------------

        File temporary = new File("./Credentialstemp");
        File original = new File("./Credentials");

        //--------------Creates a temporary file for storing new list
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("./Credentialstemp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------Writes a new list 
        for(int i = 0; i < usernames.size(); i++){
            if (usernames.get(i).equals(login) && passwords.get(i).equals(password)){
                for (int j = 0; j < usernames.size(); j++){
                  if(j != i){
                    try {
                        writer.write(usernames.get(j) + "|" + passwords.get(j));
                        if (j != usernames.size() - 1) writer.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  }
                }
                //--------------Changes the name of the temporary file into the original
                temporary.renameTo(original);
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}


public class AuthServer {

    public static void main(String args[]) {
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            authImpl AuthImpl = new authImpl();
            AuthImpl.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(AuthImpl);
            AuthFIT href = AuthFITHelper.narrow(ref);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            String name = "AuthFIT";
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);

            System.out.println("Authentication server is ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("AuthFIT server exiting ...");

    }
}