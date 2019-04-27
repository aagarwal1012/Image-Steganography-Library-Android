<div align="center"><img src="https://github.com/aagarwal1012/Image-Steganography-Library-Android/blob/master/images/cover.png?raw=true"/></div>  
<div align= "center">
	<img src="https://img.shields.io/badge/platform-Android-brightgreen.svg" alt="Platform" />
	
<img src="https://img.shields.io/badge/API-16%2B-blue.svg" alt="API" /> 
	
<a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-red.svg"
      alt="License: MIT" />
  </a>
	<a href="https://jitpack.io/#aagarwal1012/Image-Steganography-Library-Android"><img src="https://jitpack.io/v/aagarwal1012/Image-Steganography-Library-Android.svg" alt="version" /></a>
	<a href="https://www.paypal.me/aagarwal1012">
    <img src="https://img.shields.io/badge/Donate-PayPal-green.svg"
      alt="Donate" />
  </a>
	
</div>

# Image Steganography
Steganography is the process of hiding a secret message within a larger one in such a way that someone  cannot know the presence or contents of the hidden message. Although related, Steganography is not to be confused with Encryption, which is the process of making a message unintelligible—Steganography attempts to hide the existence of communication.
The main advantage of steganography algorithm is because of its simple security mechanism. Because the steganographic message is integrated invisibly and covered inside other harmless sources, it is very difficult to detect the message without knowing
the existence and the appropriate encoding scheme .

# Proposed Algorithm
The algorithm is more dedicated towards the algorithm proposed by Rosziati Ibrahim and Teoh Suk Kuan in their [Research Paper](https://arxiv.org/ftp/arxiv/papers/1112/1112.2809.pdf) published on February 25, 2011.
## Encoding Algorithm
- Firstly, the secret message that is extracted is `compressed` as the contents in the compressed string will significantly hard to detect and read, furthermore it reduces the size of string.
- Secondly, the compressed string is `encrypted` with the secret key.
- Finally, `encoding` the encrypted message in the image. It uses `LSB steganographic embedding` to encode data into an image. Once the message is encoded the process stops.
### LSB(Least Significant Bit) Embedding
The LSB is the lowest significant bit in the byte value of the image pixel.
The  LSB  based  image  steganography  embeds  the  secret  in the  least  significant  bits  of  pixel  values  of  the  cover  image (CVR).
The concept of LSB Embedding is simple. It exploits the fact that the level of precision in many image formats is far greater than that perceivable by average human vision. Therefore, an altered image with slight variations in its colors will be indistinguishable from the original by a human being, just by looking at it. In conventional LSB technique, which requires eight bytes of pixels to store 1byte of secret data but in proposed LSB technique, just four bytes of pixels are sufficient to hold one message byte. Rest of the bits in the pixels remains the same.
Following shows the bit level interpretation of the algorithm :  
<div align="center"><img src="/images/lsb1.png"/></div>

## Decoding Algorithm
- Firstly, `decode` the message from the encrypted image using LSB decoding.

- Secondly, `decrypt` the compressed message from the decoded message using the secret key.

- Finally, `decompress` the message to get the original compressed message.   

Consider the following encoding, it is totally undetectable by human eyes.
<div align="center"><img src="/images/original_encoded.png"/></div>

# Usage
Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Now add the following dependency in your app's `build.gradle`
```groovy
dependencies {
      implementation 'com.github.aagarwal1012:Image-Steganography-Library-Android:v1.0'
}
```
## How to encode message into an image ?
**Note :** Your Activity class should implements `TextEncodingCallback` interface and also contains its `override` methods.

```java
public class MainActivity extends AppCompatActivity implements TextEncodingCallback 
```
- Firstly, instantiate the `ImageSteganography` object and pass the values `Message	`, `Secret Key` and `Image Bitmap` to the constructor.

  ```java
  ImageSteganography imageSteganography = new ImageSteganography(message, 
                                          secret_key, 
                                          original_image);
  ```

- Secondly, instantiate the `TextEncoding` object and pass the values `Activity` and `TextEncodingCallback` object to the constructor.

  ```java
  TextEncoding textEncoding = new TextEncoding(MainActivity.this,
                              MainActivity.this);
  ```

- Finally, execute our `encoding` task.

  ```java
  textEncoding.execute(imageSteganography);
  ```
**Note** : By default a `ProgressDialog` will appear in the process of text encoding.

#### Override Methods

- `OnStartTextEncoding()` : In this method you can whatever by the start of text encoding process.  

  ```java
  @Override
      public void onStartTextEncoding() {
          //Whatever you want to do at the start of text encoding
      }
  ```

- `onCompleteTextEncoding()` : After the completion of text encoding, this method is called and gives result.

  ```java
  @Override
      public void onCompleteTextEncoding(ImageSteganography result) {
  
          //After the completion of text encoding.
          
          //result object is instantiated 
          this.result = result;
  
          if (result != null && result.isEncoded()){
              
              //encrypted image bitmap is extracted from result object
              encoded_image = result.getEncrypted_image();
              
              //set text and image to the UI component.
              textView.setText("Encoded");
              imageView.setImageBitmap(encoded_image);
      }
  ```
## How to decode message from an image ?
**Note :** Your Activity class should implements `TextDecodingCallback` interface and also contains its `override` methods.

```java
public class MainActivity extends AppCompatActivity implements TextDecodingCallback 
```
- Firstly, instantiate the `ImageSteganography` object and pass the values `Secret Key` and `Image Bitmap` to the constructor.

  ```java
  ImageSteganography imageSteganography = new ImageSteganography(secret_key,
                                          original_image);
  ```

- Secondly, instantiate the `TextDecoding` object and pass the values `Activity` and `TextDecodingCallback` object to the constructor.

  ```java
  TextDecoding textDecoding = new TextDecoding(MainActivity.this, 
                              MainActivity.this);
  ```

- Finally, execute our `decoding` task.

  ```java
  textDecoding.execute(imageSteganography);
  ```
  **Note** : By default a `ProgressDialog` will appear in the process of text decoding.

#### Override Methods

- `OnStartTextDecoding()` : In this method you can whatever by the start of text decoding process.  

  ```java
  @Override
      public void onStartTextDecoding() {
          //Whatever you want to do at the start of text encoding
      }
  ```

- `onCompleteTextDecoding()` : After the completion of text decoding, this method is called and gives result.

  ```java
  @Override
      public void onCompleteTextEncoding(ImageSteganography result) {
  
          //After the completion of text encoding.
          
          //result object is instantiated 
          this.result = result;
  
          if (result != null){
              
              /* If result.isDecoded() is false, it means no Message was found in 					the image. */
              if (!result.isDecoded())
                  textView.setText("No message found");
              
              else{
                  /* If result.isSecretKeyWrong() is true, it means that secret key provided 				is wrong. */ 
                  if (!result.isSecretKeyWrong()){
                      //set the message to the UI component.
                      textView.setText("Decoded");
                      message.setText("" + result.getMessage());
                  }
                  else {
                      textView.setText("Wrong secret key");
                  }
              }
          }
          else {
              //If result is null it means that bitmap is null
              textView.setText("Select Image First");
          }
      }
  ```
# Documentation

#### ImageSteganography Class

| Java attribute     | Java set methods                | Description                                                  | Default Value |
| :---------------- | :------------------------------ | :----------------------------------------------------------- | :-----------: |
| Message | setMessage(...) , getMessage() | Set the value of message, Get the value of message. | Null      |
| Secret_Key | setSecret_key(...) | Set the value of secret key. | Null      |
| Image  | setImage(...) | Set the value of image.              | Null      |
| Encoded_Image | getEncoded_image() | Get the value of encoded image after text encoding. | Null       |
| Encoded | isEncoded() | Check that the encoding is over or not | false       |
| Decoded | isDecoded() | Check that the decoding is over or not. | false       |
| SecretKeyWrong | isSecretKeyWrong() | Check that the secret key provided was right or wrong but after decoding was done. | true     |

#### Class Domain Diagram

![](https://raw.githubusercontent.com/aagarwal1012/Image-Steganography-Library-Android/master/UML/UMLDOC.PNG)

### Example App

There are two options `Encode` and `Decode`. In the encode section you can hide a secret message into an image without making any noticeable changes. In the decode section you can extract the message from the encoded image by inserting the correct key.

**Note** - After pressing the `Save` button, both the original and encoded images are saved at the location ```Android/data/com.ayush.steganography/files/Documents/<UUID>/ ```

|                            Encode                            |                            Decode                            |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| <img src="https://github.com/aagarwal1012/Image-Steganography-Library-Android/blob/master/images/encode.gif" height = "500px"/> | <img src="https://github.com/aagarwal1012/Image-Steganography-Library-Android/blob/master/images/decode.gif" height = "500px"/> |
# Want to contribute !

This is the well `documented` library. I have documented each and every method that I have used, so have a good read to the code and suggest some changes and new feature to be added in the library.  
Feel free to <a href = "https://github.com/aagarwal1012/Image-Steganography-Library-Android/issues">open an issue</a>.

# Donate
> If you found this project helpful or you learned something from the source code and want to thank me, consider buying me a cup of :coffee:
>
> - [PayPal](https://www.paypal.me/aagarwal1012/)


# License
**Image Steganography** is licensed under `MIT license`.

```
MIT License

Copyright (c) 2018 Ayush Agarwal

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

