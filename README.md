# NewsAppProject

Project built for [Udacity's Android Basics Nanodegree](https://www.udacity.com/course/android-basics-nanodegree-by-google--nd803).

Android app that displays the news related to the user's topic of interest. The news are fetched from [The Guardian News](https://www.theguardian.com/).

## Features
* Connect to an API and parse the JSON response.
* Response validation/Handle error cases (Display error message when there is no internet connection or data to load).
* Display the JSON response (date formatting). Update the information when necessary.
* Use a loader to keep network operations independent of the Activity lifecycle.
* Intent to web browser.
* Create a Settings Activity from a menu in the Main Activity. 
* Implement the Preference Fragment to update and keep track of user's preferences.
* Use Uri.Builder class to add query parameters to the URL. 

The use of external libraries were not be permitted to complete this project._ 

<p align="center">
<img src="https://raw.githubusercontent.com/ootahiaoo/NewsAppProject/master/screenshot/Screenshot_1.png" width="200" title="">   

<img src="https://raw.githubusercontent.com/ootahiaoo/NewsAppProject/master/screenshot/Screenshot_2.png" width="200" title="">  

<img src="https://raw.githubusercontent.com/ootahiaoo/NewsAppProject/master/screenshot/Screenshot_3.png" width="200" title="">
</p>


## How to install

### Step 1
Clone the repository using git (or download it as a zip), then import the project in Android Studio.
```
git clone https://github.com/ootahiaoo/NewsAppProject.git
```

### Step 2
The app fetches information from the [Guardian API](http://open-platform.theguardian.com/documentation/). 
You need to register and get your own API KEY in order to use this app. 
Once you have a key, open the `gradle.properties` file and replace the `your-api-key` with your own key at line 14.
```
theGuardianApiKey="your-api-key"
```

## License
_To be added._

Feel free to make pull requests/suggest improvements.
