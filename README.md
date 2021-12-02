# Endcrypt
Java application that performs AES file encryption and decryption

There are currently 2 versions of the program.  The first version utilizing a user interface and the second version operating  via the command-line only.

 >> EncryptThis_UI.jar -GUI -
 
 >> EncryptThis_CMD.jar -Command-line - 

EncryptThis_UI.jar 

** Please Note --  The GUI version does not accept command-line arguments.  **

This jar can be executed by double-clicking the .jar file from within Windows.  Additionally, the jar can be executed from the command-line using the following command:  java -jar EncryptThis_UI.jar

The UI is very minimal as it only contains a single editable text area where the user can enter the file system path to the specified file or they can use the associated button to open a file chooser to select a file.  Three buttons below the file selection are the available options, which are "Encrypt", "Decrypt", and "Exit".

The Text Area at the bottom is an output area that shows the status of Encryption or Decryption success or failure.  If Encryption is successful, the location of the encrypted file with extension .aes is shown.  The same is true for decryption.  The output shows the files original filename (without the aes extension).

#EncryptThis_CMD.jar

![Endcrypt-gui](https://user-images.githubusercontent.com/42433776/144497932-817e6c8e-6399-45a5-9045-97d06aeb872c.png)


** Please Note -- This version is only runnable from the command line. **

When executing the jar, the user is able to pass in command line arguments as follows:

     java -jar EncryptThis_CMD.jar [mode] [filepath]
     [mode] = mode of operation - valid options are encrypt or decrypt
     [filepath] = the absolute path to the file.  Relative paths may possibly be used, but the functionality has not been fully tested yet.
     
The user may optionally execute the jar using no arguments. 
    When using no arguments, the user is presented with an option to use the GUI or continue with the command line.  
      If GUI is selected, the UI is shown and the user can continue operation using that.
      If No GUI, the user is prompted to select the appropriate mode of operation.  Encryption or Decryption
      After mode entry is complete, the user is prompted to enter the path to the file.  
        **  Note:  Relative paths may possibly be used, however, the functionality has not been fully tested yet. **
        
      Once the appropriate options have been entered, the user is prompted to enter a password.  This password is not stored as it is used immediately. 


**Both Versions**
When Encrypt or Decrypt is selected, the user is presented with a dialog box prompting the user to enter a password. On Encryption, the password is combined with a salt and then used to generate a private key.  An Initialization Vector is then generated and The encryption cipher is initialized.  The selected file is then read into the cipher and output to a new file with the salt and iv.

When Decryption is selected, the user is again prompted to enter a password.  The salt and IV are read in from the encrypted file, the cipher initialized in DECRYPT_MODE, and the remaining ciphertext is passed to the cipher and decrypted.  Output is the normal file of its originating type (txt, pdf, jpg, etc..).


At this point in time, the application is only capable of encryption and decryption of individual files locally on the users' system.

Will be expanded to include directory encryption and, if possible, provide active editing of encrypted content.
