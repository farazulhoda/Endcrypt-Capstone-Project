/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capstone.UI;
import static capstone.UI.EncUI.fio;
import capstone.encryption.Decryption;
import capstone.encryption.Encryption;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;

/**
 *
 * @author jerem
 */
public class encCMD {
    
    static Scanner input = new Scanner(System.in);
    
    /**
     * <p>
     * This method provides 3 options dependant on the number of arguments passed.
     * No arguments presents user with an option to continue with a GUI or without
     * If user selects GUI, then GUI will load and run, otherwise program will continue 
     * using the command line.
     * 
     * In the event a single argument is passed, the argument is checked to determine if it is a file path or 
     * mode to be used such as encrypt or decrypt.
     * 
     * If 2 arguments are passed, the first argument should be the mode and the second argument should be the file path.
     * Both arguments are checked to determine validity.  If an entry is invalid, the user is prompted to re-enter the correct information.
     * </p>
     * 
     * <p> Please Note**  The program path is determined by the number of arguments passed from the command line.   **</p>
     * 
     * @param args  The command line arguments
     * 
     * 
     */
    public static void main(String[] args){
        System.out.println("\n\nWelcome to EncryptThis.\n\n");
        
        switch(args.length){
            
            case 1:
                oneArgs(args[0]);
                break;
            case 2:
                twoArgs(args[0],args[1]);
                break;
            default:                
                noArgs();
                
        }        
        
    }
    
    /**
     * Method called when no arguments are passed via the command line
     */
    private static void noArgs(){
        System.out.println("No arguments entered. Please select an option:");
        showOptions();           
    }
    
    /**
     * Method providing the user options to continue with or without a GUI.
     * Program path is changed dependant on the option selected by the user.
     */
    private static void showOptions(){
        System.out.println("1.  Show GUI");
        System.out.println("2.  Continue without GUI");
        int answer=input.nextInt();
        switch(answer){
            case 1:
                startGUI();
                break;
            case 2:
                noGUI();
                break;
            default:
                System.out.println("Invalid Entry - Please enter an option...");
                showOptions();              
            
        }
    }
    
    /**
     * Loads the GUI in a thread-safe manner.
     */
    private static void startGUI(){
        
        
        java.awt.EventQueue.invokeLater(() -> {
            new EncUI().setVisible(true);
        });
        
        
    }
    
    /**
     * Path program takes when user selects to continue without the GUI
     */
    private static void noGUI(){
        System.out.println("Proceeding without GUI...");
        String mode = enterMode();        
        String filepath = enterFileName();
        String pw = getUserPassword(); 
        if(mode.equals("encrypt")){
            try{
                System.out.println("Encrypting File...");
                String output = Encryption.encrypt(new File(filepath),pw);
                System.out.println("Encryption Successful"+
                        "\nEncrypted File:  " + output);
            } catch (Exception ex) {
                Logger.getLogger(encCMD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(mode.equals("decrypt")){
            try{
                System.out.println("Decrypting File...");
                String output = Decryption.decrypt(new File(filepath),pw);
                System.out.println("Decryption Successful"+
                        "\nDecrypted File:  " + output);
            } catch (BadPaddingException ex) {
                System.out.println("Decryption Failed:  Invalid Password");
                
            } catch (Exception ex) {
                Logger.getLogger(encCMD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * TODO:  Mask user input
     * @return The password value entered by the user
     */
    private static String getUserPassword() {
        
        
        String pw;
        System.out.print("Enter a password:");
        pw=input.next();
        if(pw.equals("")){
            System.out.println("No password entered...");
            getUserPassword();
        }
       
        return pw;
    }
    
    
    /**
     * TODO:  Not Yet Implemented
     * 
     * Program path taken when a single argument is passed
     * @param argument Command line argument passed
     */
    private static void oneArgs(String argument){
        
    }
    
    /**
     * Program path taken when 2 arguments are passed
     * @param mode The first argument path specifying the mode of operation
     * @param filename The filename with which to perform encryption/decryption operations
     */
    private static void twoArgs(String mode, String filename){
        
        System.out.println("2 args called");  //For Diagnostic Purposes only        
        Path path=Paths.get(filename).toAbsolutePath();
        
        while(!fio.isValidFile(path)){
            System.out.println("File does not exist.  Please check the path entered.");
            filename = enterFileName();
        }
        
        while(!validateMode(mode)){
            System.out.println("Invalid mode");
            mode = enterMode();
        }
        if(fio.isEncrypted(path)){
            fio.setEncFile(path);
            if(mode.equals("encrypt")){
                mode = confirm(mode);
            }else{
                try {
                    System.out.print("Encrypting file...");                    
                    String output = Encryption.encrypt(fio.getEncFile(), getUserPassword());
                    System.out.println("Encryption Successful");
                    System.out.println("Encrypted File Output:  "+output);
                    
                } catch (Exception ex) {
                    Logger.getLogger(encCMD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else if(!fio.isEncrypted(path)){
            fio.setDecFile(path);
            if(mode.equals("decrypt")){
                mode=confirm(mode);
            }else{
                try {
                    System.out.println("Decrypting File...");
                    String output = Decryption.decrypt(fio.getDecFile(),getUserPassword());
                    System.out.println("Decryption Successful");
                    System.out.println("Decrypted File Output:  " + output);
                } catch (Exception ex) {
                    Logger.getLogger(encCMD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
        
        
        
        
    }
    private static String confirm(String mode){
        switch(mode){
            case "encrypt":
                System.out.println("File entered is already encrypted, do you want to decrypt this file?"+
                        "\n*** Selecting 'No' will exit the program *** \n");
                System.out.println("1.  Yes \n2.  No");
                int result = input.nextInt();
                switch (result){
                    case 1:
                        mode="decrypt";
                        break;
                    case 2: 
                        System.out.println("Exiting...");
                        fio.cleanup();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Proceeding with Decryption...");
                }
                break;
                
            case "decrypt":
                System.out.println("File entered is not encrypted, do you want to encrypt this file?"+
                        "\n*** Selecting 'No' will exit the program *** \n");
                System.out.println("1.  Yes \n2.  No");
                result = input.nextInt();
                switch (result){
                    case 1:
                        mode="decrypt";
                        break;
                    case 2: 
                        System.out.println("Exiting...");
                        fio.cleanup();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Proceeding with Decryption...");
                }
                break;
                
        }
        return mode;
    }
    
    
    /**
     * Method to validate the mode entered by the user or passed in via the command line
     * @param mode  The mode of operation
     * @return Boolean value that selected mode is valid
     */
    private static boolean validateMode(String mode){
        //System.out.println("Mode Entered: " + mode);  //Diag Purposes Only
        
        switch (mode.toLowerCase()) {
            case "encrypt":
                return true;
            case "decrypt":
                return true;
            default:
                return false;        
        }
        
    }
       
    /**
     * Method prompting the user to re-enter/enter the mode of operation.
     * @return the valid mode of operation
     */
    private static String enterMode(){
        System.out.println("Enter mode: " +
                "\n 1.  Encrypt" +
                "\n 2.  Decrypt" +
                "\n Enter the desired mode:(1 or 2): ");
        
        while(!input.hasNextInt()){
            System.out.println("The value entered must be a number");
            input.next();
        }
        
        int result = input.nextInt();
        
        String mode="";
        switch(result){
            case 1:
                mode= "encrypt";
                break;
            case 2:
                mode= "decrypt";
                break;
            default: 
                System.out.println("***  Invalid Mode  ***");
                enterMode();
        }
        
        return mode;
        
    }
    
    /**
     * This method prompts the user to enter the path to the file they wish to use.
     * @return the path to the file.
     */
    private static String enterFileName(){
        //C:\Users\jerem\Documents\Comp2\wk2.docx wk2.docx.aes
        System.out.println("Enter the path to the file:");
        String filename = input.next();
        while(!fio.isValidFile(Paths.get(filename))){
            System.out.println("File does not exist.  Please enter the path to the file.");
            filename=input.next();
        }
        return filename;
    }
}
