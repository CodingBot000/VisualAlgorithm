<h1 align="center">VisualAlgorithm</h1>

<p align="center">  
 This ToyProject make modern Android development with Compose, Dagger-Hilt, Coroutines, Jetpack based on MVI architecture.
</br>
</br>
It visually represents the operation of the algorithm. 
<ul>
<li>Pause and resume are possible during playback.</li>
<li>While paused, you can track the history of the algorithm's operations using back and forward buttons.</li>
<li>Each time you enter the screen, data is randomly extracted, allowing you to see the algorithm operating on different elements every time.</li>
 <li>Can track the log and view it through the bottom sheet bar.</li>
 </ul>
</br>


[![Video Label](http://img.youtube.com/vi/9wV2rSu_nkI/0.jpg)](https://youtu.be/9wV2rSu_nkI)
- Click to view as a 'YouTube' video.  

    
## Download APK for install
Go to the [apk file](https://www.dropbox.com/scl/fi/svqqde3ipv3v4eyv0e37c/visual-algorithm-app-debug.apk?rlkey=aeo96lqsbbkma0xzscppax1tk&dl=0) to download the latest APK.


## Tech stack
- [Kotlin] based, [Coroutines] + [Flow]  for asynchronous.
- Compose
- [Hilt] for dependency injection.
- Jetpack
- MVI Architecture


## Data Flow Description
<ul>
 <li>The core algorithm code is designed to be independent of any UI platform.</li>
 
 <li>Additionally, while we use Flow in the ViewModel to update the View, I do not use Flow for updating the algorithm data. Instead, updates are handled through an interface.</li>
 
 <li>The reason for this is to facilitate future expansion to other platforms.</li>
 
 <li>When developing for a new platform, managing streams can be challenging in terms of resource cleanup, stopping, and restarting. However, implementing it without streams is relatively easier.</li>
 
 <li>One of the goals is to structure the code so that it can be easily adapted to new platforms, as long as the basic structure of the new platform's UI development is understood.</li>
</ul>

## Multi Module Structure
![Alt text](https://github.com/CodingBot000/VisualAlgorithm/blob/main/VisualAlgorithm_MultiModule.drawio.png)

## Project Structure
![Alt text](https://github.com/CodingBot000/VisualAlgorithm/blob/main/VisualAlgorithm.drawio.png)

## 
![Alt text](https://github.com/CodingBot000/VisualAlgorithm/blob/main/VisualAlgorithm_MVI_Preview.drawio.png)


