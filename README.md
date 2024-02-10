# Edge-Digit-Classification
This repository contains an Android application that captures handwritten digit images, divides them into four parts, and distributes these parts to four different client mobile devices. The distributed images are then classified using pre-trained deep learning models, and the results are consolidated on the master mobile device.

## Objective
The primary objective of this project is to create an Android application for edge computing that demonstrates the following functionalities:

+ Divide the captured images of handwritten digits into four parts and Distribute these parts to different mobile devices.
+ Classify the handwritten digits on each client and consolidate the results on the master mobile device.
+ Organize the classified digits into their respective folders on the master mobile.

## Approach
Mobile Application
The Android application allows users to take pictures of handwritten digit images. These images are then divided into four parts and sent to four different mobile devices (one master and three clients).

### Deep Learning Models
We created four separate deep learning models for each part of the image. These models were trained using the MNIST dataset.

### Model Architecture
+ Each model starts with a convolutional layer with 32 filters.
+ A second convolutional layer with 64 filters is added.
+ A flattened layer connects the dense layer and the convolutional layer.
+ The output layer is a dense layer with ten output values, using the 'softmax' activation function to produce a probability distribution over the digits.
+ Models are compiled and trained on the training data, achieving an accuracy of 0.80 on the testing data.

## Result Calculation
Finally, to classify the digit, we calculate the final output as the geometric mean (nth root product of n numbers) of the resulting probability arrays from all four models.
