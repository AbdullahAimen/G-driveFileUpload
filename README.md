# G-driveFileUpload
Simple app that enables the user to sign in to his/her google account to create and upload text files to google drive. 
The app serves as file creation/upload/read from logged-in user through google account.

# Setup steps
1. create ur project throuh android studio

2. create ur own firebase authentication project from <a href='https://console.firebase.google.com/u/0/'>console.firebase</a> and download the .json file then save it inside ur app module

3. go to ur <a href='https://console.firebase.google.com/u/0/project/poised-honor-289814/settings/general/android:com.google.drivedemo'>project settings</a> and add ur SHA certificate fingerprints for ur release and debug key
u can get ur SHA1 debug key from running the gradle->appModuleName->Tasks->android->signingReport task, and for release version u can run this command :> keytool -list -v -keystore {keystore_name} -alias {alias_name}

4. follow <a href='https://firebase.google.com/docs/auth/android/google-signin?authuser=0#kotlin+ktx'>this guide</a> to implement firebase authentication with google sign in

5. enable sign in method from <a href='https://console.firebase.google.com/u/0/project/poised-honor-289814/authentication/providers'>authentication settings </a>

6. replace the .json file from the project with the new created one and run the sample.

have fun :)
