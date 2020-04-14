# Digital Doctor #
Houses the code for the app Digital Doctor based on Your Body's Red Light Warning Signals by Dr. Neil Shulman

**Digital Doctor v 1.0.0**

**Release Notes (4/20/2020):**
* New Features:
  * First public release is now available on the Google Play Store. This is available to those with Android 4.4 and above, which is 96.2% of Android users.
  * Symptom search and recognition for every diagnosis in the book Your Body’s Red Light Warning Signals
  * Page creation for Your Body’s Red Light Warning Signals to differentiate the application Digital Doctor and the book. This includes a button that links to the Amazon purchase page for the book.
  * Added Google Maps functionality for nearby emergency rooms
  * Numerous reformatting changes to code to allow greater simplicity when programming
  * Added a button to immediately search for nearby emergency rooms on the home screen in the case of an easily-recognizable emergency.
* Bugfixes:
  * Removed inconsistent padding from symptom search list
  * Fixed inconsistent placement of back arrow across the diagnosis detail and search pages
  * Fixed an issue where the book page was not scrolling properly
  * Fixed an issue where the back button was displaying even when you were unable to go back a level in the search
  * Fixed an issue where text was improperly loading into pop-up windows
  * Fixed inconsistent text sizes across different pages
* Known Bugs:
  * Google Maps is slow to load on older devices

**Install Guide:**
* Pre-Requisites
  * An Android Device using APK 26 or greater
  * 2 MB Memory Space
* Dependent Libraries
  * None
* Download Instructions
  * Visit the Google Play Store on your Android Device.
  * Search “Digital Doctor”
  * Download the application developed by Digital Doctor Team
  * Run the app once downloaded and installed to verify
* Build instructions:
  * None
* Installation of Actual Application:
  * Once you download the application from the Play Store, the installation to your device is automatic, unless you lack memory space.
* Run Instructions
  * Once the application is installed and on your device, press the icon to open and run the application
* Troubleshooting
  * I have an iPhone, can I access this app?
    * Unfortunately not. This is potential future development for Digital Doctor.
  * Let us know any other problems you encounter so that we can fill out our guide!
    * Email: gtteam9303@gmail.com

**Development Guide:**
* Pre-requisites:
  * Requires Android Studio 3.5.2 or higher
  * System Requirements specified here: https://developer.android.com/studio#downloads
  * Android SDK version 26
    * https://developer.android.com/studio/intro/update#sdk-manager
  * Java Version 8
  * JDK 1.8 or higher
    * https://www.oracle.com/java/technologies/javase-jdk8-downloads.html
  * If intending to make code changes for updates/releases: Git
* Dependent libraries that must be installed:
  * None; Gradle should be downloaded with Android Studio
* Download instructions
  * Github Repository: https://github.com/kcozzone3/DigitalDoctorApp
  * Click the green “Clone or Download” button to create a new repository offline. This will pull all the code from the most recent update.
  * If you already have the repository downloaded, enter in, and run the command “git pull origin master” to retrieve the most recent changes.
  * For more information on git, check out this link: https://product.hubspot.com/blog/git-and-github-tutorial-for-beginners
* Build instructions:
  * Open the project in Android Studio
  * Click the green run button once you have completed emulator setup.
  * A tutorial on emulator setup can be found here: https://developers.foxitsoftware.com/kb/article/create-an-emulator-for-testing-in-android-studio/
* Installation of actual application: 
  * Once the app is ran (step above), it should download and install automatically to the emulator. It should then open automatically.
* Run instructions:
  * Once the app is run, it should open automatically. If it does not open automatically, check if the installation was successful. If it was, there should be an icon you can open on the app page of the emulator.
* Troubleshooting:
  * Project isn’t building in Android Studio:
    * Check that you have opened the project folder, and not the repository folder.
  * Let us know any other problems you encounter so that we can fill out our guide!
    * Email: gtteam9303@gmail.com

