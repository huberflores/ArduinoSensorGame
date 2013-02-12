#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
#include <math.h> 
#define I0 A0


const int analogInPin = I0;  // Analog input pin that the Themistor is attached to
AndroidAccessory acc("Manufacturer",
		     "Model",
		     "Description",
		     "1.0",
		     "http://yoursite.com",
		     "0000000012345678");


float vcc = 4.91;                       // only used for display purposes, if used
                                        // set to the measured Vcc.
float pad = 9850;                       // balance/pad resistor value, set this to
                                        // the measured resistance of your pad resistor
float thermr = 5000;                   // thermistor nominal resistance

float Thermistor(int RawADC) {
  long Resistance;  
  float Temp;  // Dual-Purpose variable to save space.

  Resistance=((1024 * pad / RawADC) - pad); 
  Temp = log(Resistance); // Saving the Log(resistance) so not to calculate  it 4 times later
  Temp = 1 / (0.001129148 + (0.000234125 * Temp) + (0.0000000876741 * Temp * Temp * Temp));
  Temp = Temp - 273.15;  // Convert Kelvin to Celsius                      
  
  return Temp;                                      // Return the Temperature
}


void setup() {
  // initialize serial communications at 9600 bps:
  Serial1.begin(115200); 
  Serial.begin(115200); // For wiFly
  acc.powerOn();
  pinMode(13, OUTPUT);  
}


byte i;
void loop() {
  //read the analog in value:
  i = Thermistor(analogRead(analogInPin));            
 // Serial.println(val);
  
  //print the results to the serial monitor:
  //Serial1.print("sensor = " );                       
  //Serial1.println(msg[0]);   
  //if (acc.isConnected()) { 
     
    //acc.write(msg, 1);  
    Serial.print(i);
    digitalWrite(13, HIGH);   // set the LED on
    delay(1000);              // wait for a second
    digitalWrite(13, LOW);  

 //}
   delay(1000);                     
}

 
