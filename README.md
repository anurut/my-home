# my-home (Tasmota, MQTT)
This is a crude Android application which can be used to control switches which are using [Tasmota](https://github.com/arendst/Tasmota) firmware through MQTT. 
Currently it supports only switches (eg. lights, fans etc).

## Geting Started
**(Pre-requisite)** You need to have a MQTT broker running and Tasmotized device

Build and install this project on your android phone using Android Studio.

Steps to configure the application
1. Generate a config.json file.
  To generate a config.json file either copy the following and save it in your phone's storage
  ```javascript
  {
  "Room1":
      {
      "name":"master bedroom",
      "type":"bedroom", // can be bedroom, bathroom or kitchen as of now
      "show_image":true,
      "Button1":
          {
          "name":"tube light",
          "type":"light", // can be light, night light, fan, water heater or blank("")
          "command_topic":"cmnd/masterbedroom/POWER1",
          "state_topic":"stat/masterbedroom/POWER1",
          "payload_on":"ON",
          "payload_off":"OFF",
          "lwt_topic":"tele/masterbedroom/LWT",
          "lwt_available":"Online",
          "lwt_unavailable":"Offline"
          },
      "Button2":
           {
           "name":"night light",
           "type":"light",
           "command_topic":"cmnd/masterbedroom/POWER2",
           "state_topic":"stat/masterbedroom/POWER2",
           "payload_on":"ON",
           "payload_off":"OFF",
           "lwt_topic":"tele/masterbedroom/LWT",
           "lwt_available":"Online",
           "lwt_unavailable":"Offline"
           },
      "Button3":
           {
           "name":"ceiling fan",
           "type":"fan",
           "command_topic":"cmnd/masterbedroom/POWER3",
           "state_topic":"stat/masterbedroom/POWER3",
           "payload_on":"ON",
           "payload_off":"OFF",
           "lwt_topic":"tele/masterbedroom/LWT",
           "lwt_available":"Online",
           "lwt_unavailable":"Offline"
           },
      "Button4":
           {
           "name":"dummy light",
           "type":"light","command_topic":"cmnd/masterbedroom/POWER4",
           "state_topic":"stat/masterbedroom/POWER4",
           "payload_on":"ON",
           "payload_off":"OFF",
           "lwt_topic":"tele/masterbedroom/LWT",
           "lwt_available":"Online",
           "lwt_unavailable":"Offline"
           }
      }
  }
  ```
  to add another room just copy the "ROOM1" section and use incremental name (eg. Room2, Room3 ...), same in the case of Button.
  or on the welcome screen in the application
  
  <img src="https://drive.google.com/uc?export=view&id=18zF_VdzWlzVO7kt7gKYpoyw0s89kq61d" height ="500"/>
  
  Click on the *CREATE FILE* button and then *SAVE* button on the next screen.
  
   <img src="https://drive.google.com/uc?export=view&id=199Jctk3ML8Js-GJEC7qNMGZLYXF3binr" height ="500"/>
  
  Click on the *SAVE CONFIG* button and choose *config.json* to save the above contents in the Downloads folder of your phone. You can now update this json as per your requirements and then use the *READ CONFIG* button to load the updated json into the application.

2. Enter MQTT credentials 

Click on the *Menu* buttons(three dots on the top right corner) and click on *Mqtt Setup* to get to the screen below
<img src="https://drive.google.com/uc?export=view&id=191OqShxiOSucOyW8Glp2J2ZOnVOis4Dy" height ="500"/>
Insert your credentials here and click on the *OK* button
